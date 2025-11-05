package br.com.crudjava.crudjava_junit.persistencias;

import br.com.crudjava.crudjava_junit.models.Locatario;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;

public class ArquivoLocatarioTest {

    private static final String ARQ_LOC = "locatarios.dat";

    @BeforeEach
    void setup() {
        File f = new File(ARQ_LOC);
        if (f.exists()) f.delete();

        ArquivoLocatario.salvarLista(new ArrayList<>());
    }

    @Test
    void CT01_adicionarLocatarioValido_DeveAdicionar(){
        Locatario loc = new Locatario("12345678910123", "a", "@",
                "12345678910");
        boolean result = ArquivoLocatario.adicionarLocatario(loc);

        Assertions.assertTrue(result);
        Assertions.assertEquals(1, ArquivoLocatario.lerLista().size());
        Locatario l = ArquivoLocatario.lerLista().get(0);
        Assertions.assertEquals("12345678910123", l.getLocatarioCnpj());
    }

    @Test
    void CT02_cnpjAbaixoDoLimite_DeveRetornarMensagemErro() {
        Locatario loc = new Locatario("1234567891012", "a", "@",
                "12345678910");
        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals("O CNPJ deve ser preenchido completamente.", ArquivoLocatario.getUltimaMensagem());

    }

    @Test
    void CT03_cnpjAcimaDoLimite_DeveSerValido(){
        Locatario loc = new Locatario("123456789101234", "a", "@",
                "12345678910");
        Assertions.assertTrue(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals("Locatário cadastrado com sucesso!",
                ArquivoLocatario.getUltimaMensagem());
    }

    @Test
    void CT04_cnpjNaoNumerico_DeveRetonarMensagemErro(){
        Locatario loc = new Locatario("a @", "a", "@",
                "12345678910");
        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals("O CNPJ deve ser preenchido completamente.", ArquivoLocatario.getUltimaMensagem());
    }

    @Test
    void CT05_cnpjEmBranco_DeveRetornarMensagemErro(){
        Locatario loc = new Locatario(null, "a", "@",
                "12345678910");
        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals("O CNPJ deve ser preenchido completamente.", ArquivoLocatario.getUltimaMensagem());
    }

    @Test
    void CT06_cnpjDuplicado_naoDeveAdicionar(){
        Locatario loc1 = new Locatario("12345678910123", "b", "@",
                "12345678911");
        Locatario loc2 = new Locatario("12345678910123", "a","@", "12345678910");

        Assertions.assertTrue(ArquivoLocatario.adicionarLocatario(loc1));
        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc2));

        Assertions.assertEquals(
                "CNPJ já cadastrado. Locatário não adicionado.",
                ArquivoLocatario.getUltimaMensagem()
        );

        Assertions.assertEquals(1, ArquivoLocatario.lerLista().size());
    }

    @Test
    void CT07_nomeEmBranco_DeveRetornarMensagemErro(){
        Locatario loc = new Locatario("12345678910123", null, "@",
                "12345678910");

        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals(
                "O Nome da empresa não pode ser vazio.",
                ArquivoLocatario.getUltimaMensagem()
        );
    }

    @Test
    void CT08_emailSemArroba_DeveRetornarMensagemErro(){
        Locatario loc = new Locatario("12345678910123", "a", "aaaa",
                "12345678910");

        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals(
                "Insira um e-mail válido",
                ArquivoLocatario.getUltimaMensagem()
        );
    }

    @Test
    void CT09_emailEmBranco_DeveRetornarMensagemErro(){
        Locatario loc = new Locatario("12345678910123", "a", null,
                "12345678910");

        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals(
                "Insira um e-mail válido",
                ArquivoLocatario.getUltimaMensagem()
        );
    }

    @Test
    void CT10_telefoneAcimaDoLimite_DeveSerValido(){
        Locatario loc = new Locatario("12345678910123", "a", "@",
                "123456789101");

        Assertions.assertTrue(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals(
                "Locatário cadastrado com sucesso!",
                ArquivoLocatario.getUltimaMensagem()
        );
    }

    @Test
    void CT11_telefoneAbaixoDoLimite_DeveRetornarMensagemDeErro(){
        Locatario loc = new Locatario("12345678910123", "a", "@",
                "123456789");

        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals(
                "O Telefone deve ser preenchido completamente.",
                ArquivoLocatario.getUltimaMensagem()
        );
    }

    @Test
    void CT12_telefoneNaoNumerico_DeveRetornarMensagemDeErro(){
        Locatario loc = new Locatario("12345678910123", "a", "@",
                "a @");

        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals(
                "O Telefone deve ser preenchido completamente.",
                ArquivoLocatario.getUltimaMensagem()
        );
    }

    @Test
    void CT13_telefoneEmBranco_DeveRetornarMensagemDeErro(){
        Locatario loc = new Locatario("12345678910123", "a", "@",
                null);

        Assertions.assertFalse(ArquivoLocatario.adicionarLocatario(loc));

        Assertions.assertEquals(
                "O Telefone deve ser preenchido completamente.",
                ArquivoLocatario.getUltimaMensagem()
        );
    }

    @Test
    void CT14_editarTodosOsCampos_DeveSerValido() {
        Locatario loc = new Locatario("12345678910123", "empresa1", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("12345678910123", "a", "@", "12345678910");

        Assertions.assertEquals("Locatário editado com sucesso!", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT15_editarSelecionadoMaisDeUm_DeveManterUltimoSelecionado() {

        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        // simula que apenas o último foi editado
        ArquivoLocatario.editarLocatario("12345678910123", "primeiraEdicao", "@", "12345678910");
        ArquivoLocatario.editarLocatario("12345678910123", "segundaEdicao", "@", "12345678910");

        ArrayList<Locatario> lista = ArquivoLocatario.lerLista();
        Assertions.assertEquals("segundaEdicao", lista.get(0).getLocatarioNome());
    }


    @Test
    void CT16_editarSemSelecionarNenhum_DeveRetornarMensagemDeErro() {
        ArquivoLocatario.editarLocatario(null, "a", "@", "12345678910");
        Assertions.assertEquals("Selecione um locatário para editar", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT17_editarCnpjAbaixoDoLimite_DeveManterAntigo() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("1234567891012", "a", "@", "12345678910");
        Assertions.assertEquals("Locatário editado com sucesso!", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT18_editarCnpjAcimaDoLimite_DeveManterAntigo() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("123456789101234", "a", "@", "12345678910");
        Assertions.assertEquals("Locatário editado com sucesso!", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT19_editarCnpjComCaracteresInvalidos_DeveManterAntigo() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("a @", "a", "@", "12345678910");
        Assertions.assertEquals("Locatário editado com sucesso!", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT20_editarCnpjEmBranco_DeveManterAntigo() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario(" ", "a", "@", "12345678910");
        Assertions.assertEquals("Locatário editado com sucesso!", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT21_editarCnpjJaExistente_DeveManterAntigo() {
        Locatario loc1 = new Locatario("12345678910123", "empresa1", "@", "12345678910");
        Locatario loc2 = new Locatario("98765432100000", "empresa2", "@", "12345678910");

        ArquivoLocatario.adicionarLocatario(loc1);
        ArquivoLocatario.adicionarLocatario(loc2);

        // Tentando mudar loc2 para o mesmo CNPJ do loc1
        ArquivoLocatario.editarLocatario("12345678910123", "empresa2", "@", "12345678910");

        Assertions.assertEquals("Locatário editado com sucesso!", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT22_editarNomeEmBranco_DeveRetornarMensagemDeErro() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("12345678910123", " ", "@", "12345678910");
        Assertions.assertEquals("O Nome da empresa não pode ser vazio.", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT23_editarEmailSemArroba_DeveRetornarMensagemDeErro() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("12345678910123", "a", "aaaa", "12345678910");
        Assertions.assertEquals("Insira um e-mail válido", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT24_editarEmailEmBranco_DeveRetornarMensagemDeErro() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("12345678910123", "a", " ", "12345678910");
        Assertions.assertEquals("Insira um e-mail válido", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT25_editarTelefoneAcimaDoLimite_DeveSerValido() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("12345678910123", "a", "@", "123456789101");
        Assertions.assertEquals("Locatário editado com sucesso!", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT26_editarTelefoneAbaixoDoLimite_DeveRetornarMensagemDeErro() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("12345678910123", "a", "@", "123456789");
        Assertions.assertEquals("O Telefone deve ser preenchido completamente.", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT27_editarTelefoneNaoNumerico_DeveRetornarMensagemDeErro() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("12345678910123", "a", "@", "a @");
        Assertions.assertEquals("O Telefone deve ser preenchido completamente.", ArquivoLocatario.getUltimaMensagem());
    }


    @Test
    void CT28_editarTelefoneEmBranco_DeveRetornarMensagemDeErro() {
        Locatario loc = new Locatario("12345678910123", "empresa", "@", "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        ArquivoLocatario.editarLocatario("12345678910123", "a", "@", " ");
        Assertions.assertEquals("O Telefone deve ser preenchido completamente.", ArquivoLocatario.getUltimaMensagem());
    }

    @Test
    void CT29_removerSelecionado_DeveRemover(){
        Locatario loc = new Locatario("12345678910123", "a", "@",
                "12345678910");
        ArquivoLocatario.adicionarLocatario(loc);

        boolean resultado = ArquivoLocatario.removerLocatario("12345678910123");
        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Locatário removido com sucesso",
                ArquivoLocatario.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoLocatario.lerLista().size());
    }

    @Test
    void CT30_removerSelecionadoMaisDeUm_DeveManterUltimoSelecionado(){
        Locatario loc1 = new Locatario("12345678910123", "a", "@",
                "12345678910");
        Locatario loc2 = new Locatario("12345678910124", "a", "@",
                "12345678911");
        ArquivoLocatario.adicionarLocatario(loc1);
        ArquivoLocatario.adicionarLocatario(loc2);

        // aqui simula remover segundo
        boolean resultado = ArquivoLocatario.removerLocatario("12345678910124");
        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Locatário removido com sucesso",
                ArquivoLocatario.getUltimaMensagem());
        Assertions.assertEquals(1, ArquivoLocatario.lerLista().size());
    }

    @Test
    void CT31_removerNenhumSelecionado_DeveRetornarMensagemDeErro(){
        boolean resultado = ArquivoLocatario.removerLocatario("");
        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Erro ao remover locatário",
                ArquivoLocatario.getUltimaMensagem());
    }

}
