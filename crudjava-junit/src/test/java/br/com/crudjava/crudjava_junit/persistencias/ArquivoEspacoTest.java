package br.com.crudjava.crudjava_junit.persistencias;

import br.com.crudjava.crudjava_junit.models.Espaco;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ArquivoEspacoTest {
    private static final String ARQ_DADOS = "espacos.dat";
    private static final String ARQ_ID = "proximoId_espaco.dat";


    @BeforeEach
    void setup() {

        File f_dados = new File(ARQ_DADOS);
        File f_id = new File(ARQ_ID);
        if (f_dados.exists()) f_dados.delete();
        if (f_id.exists()) f_id.delete();


        ArquivoEspaco.salvarLista(new ArrayList<>());
        ArquivoEspaco.salvarProximoId(1);
    }



    @Test
    void CT01_adicionarEspacoCamposValidosMinimos_DeveAdicionarComSucesso() {
        String area = "1";
        String piso = "1";

        boolean resultado = ArquivoEspaco.adicionarEspaco(piso, area);

        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Espaço cadastrado com sucesso!",
                ArquivoEspaco.getUltimaMensagem());

        ArrayList<Espaco> lista = ArquivoEspaco.lerLista();
        Assertions.assertEquals(1, lista.size());

        Espaco espacoSalvo = lista.get(0);
        Assertions.assertEquals(1, espacoSalvo.getId());
        Assertions.assertEquals(1, espacoSalvo.getPiso());
        Assertions.assertEquals(1.0, espacoSalvo.getArea());
    }
    @Test
    void CT02_adicionarEspacoAreaAbaixoLimiteZero_DeveFalhar() {
        String area = "0";
        String piso = "1";

        boolean resultado = ArquivoEspaco.adicionarEspaco(piso, area);

        Assertions.assertFalse(resultado);

        Assertions.assertEquals("Área inválida.",
                ArquivoEspaco.getUltimaMensagem());

        ArrayList<Espaco> lista = ArquivoEspaco.lerLista();
        Assertions.assertEquals(0, lista.size());
    }

    @Test
    void CT03_adicionarEspacoAreaNaoNumerica_DeveFalhar() {

        String area = "a @";
        String piso = "1";

        boolean resultado = ArquivoEspaco.adicionarEspaco(piso, area);

        Assertions.assertFalse(resultado);

        Assertions.assertEquals("Área inválida.",
                ArquivoEspaco.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoEspaco.lerLista().size());
    }

    @Test
    void CT04_adicionarEspacoAreaEmBranco_DeveFalhar() {
        String area = "";
        String piso = "1";

        boolean resultado = ArquivoEspaco.adicionarEspaco(piso, area);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Área inválida.",
                ArquivoEspaco.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoEspaco.lerLista().size());
    }

    @Test
    void CT05_adicionarEspacoPisoAbaixoLimiteZero_DeveFalhar() {
        String area = "78";
        String piso = "0";

        boolean resultado = ArquivoEspaco.adicionarEspaco(piso, area);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Piso inválido.",
                ArquivoEspaco.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoEspaco.lerLista().size());
    }

    @Test
    void CT06_adicionarEspacoPisoAcimaLimiteTres_DeveFalhar() {
        String area = "78";
        String piso = "3";

        boolean resultado = ArquivoEspaco.adicionarEspaco(piso, area);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Piso inválido.",
                ArquivoEspaco.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoEspaco.lerLista().size());
    }

    @Test
    void CT07_adicionarEspacoPisoEmBranco_DeveFalhar() {
        String area = "78";
        String piso = "";

        boolean resultado = ArquivoEspaco.adicionarEspaco(piso, area);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Piso inválido.",
                ArquivoEspaco.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoEspaco.lerLista().size());
    }

    @Test
    void CT08_editarEspacoValido_DeveEditarComSucesso() {

        ArquivoEspaco.adicionarEspaco("1", "50.5");

        String novaArea = "55";
        String novoPiso = "2";
        int idParaEditar = 1;

        boolean resultado = ArquivoEspaco.editarEspaco(idParaEditar, novoPiso, novaArea);

        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Espaço editado com sucesso!",
                ArquivoEspaco.getUltimaMensagem());

        ArrayList<Espaco> lista = ArquivoEspaco.lerLista();
        Assertions.assertEquals(1, lista.size());
        Espaco espacoEditado = lista.get(0);

        Assertions.assertEquals(1, espacoEditado.getId());
        Assertions.assertEquals(2, espacoEditado.getPiso());
        Assertions.assertEquals(55.0, espacoEditado.getArea());
    }

    @Test
    void CT09_editarEspacoIdNaoExistente_DeveFalhar() {

        String novaArea = "55";
        String novoPiso = "2";
        int idParaEditar = 999;

        boolean resultado = ArquivoEspaco.editarEspaco(idParaEditar, novoPiso, novaArea);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("O ID do espaço não foi encontrado.",
                ArquivoEspaco.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoEspaco.lerLista().size());
    }

    @Test
    void CT11_editarEspacoAreaNaoNumerica_DeveFalhar() {

        ArquivoEspaco.adicionarEspaco("1", "50.5");


        String novaArea = "a @";
        String novoPiso = "1";
        int idParaEditar = 1;

        boolean resultado = ArquivoEspaco.editarEspaco(idParaEditar, novoPiso, novaArea);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Área inválida.", ArquivoEspaco.getUltimaMensagem());

        Espaco espacoOriginal = ArquivoEspaco.lerLista().get(0);
        Assertions.assertEquals(1, espacoOriginal.getPiso());
        Assertions.assertEquals(50.5, espacoOriginal.getArea());
    }

    @Test
    void CT12_editarEspacoAreaEmBranco_DeveFalhar() {

        ArquivoEspaco.adicionarEspaco("1", "50.5");

        String novaArea = "";
        String novoPiso = "1";
        int idParaEditar = 1;

        boolean resultado = ArquivoEspaco.editarEspaco(idParaEditar, novoPiso, novaArea);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Área inválida.", ArquivoEspaco.getUltimaMensagem());


        Espaco espacoOriginal = ArquivoEspaco.lerLista().get(0);
        Assertions.assertEquals(50.5, espacoOriginal.getArea());
    }

    @Test
    void CT13_editarEspacoPisoForaDoIntervaloZero_DeveFalhar() {

        ArquivoEspaco.adicionarEspaco("1", "50.5");


        String novaArea = "88";
        String novoPiso = "0";
        int idParaEditar = 1;

        boolean resultado = ArquivoEspaco.editarEspaco(idParaEditar, novoPiso, novaArea);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Piso inválido.", ArquivoEspaco.getUltimaMensagem());

        Espaco espacoOriginal = ArquivoEspaco.lerLista().get(0);
        Assertions.assertEquals(1, espacoOriginal.getPiso());
    }

    @Test
    void CT14_editarEspacoPisoEmBranco_DeveFalhar() {

        ArquivoEspaco.adicionarEspaco("1", "50.5");


        String novaArea = "45";
        String novoPiso = "";
        int idParaEditar = 1;

        boolean resultado = ArquivoEspaco.editarEspaco(idParaEditar, novoPiso, novaArea);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Piso inválido.", ArquivoEspaco.getUltimaMensagem());

        Espaco espacoOriginal = ArquivoEspaco.lerLista().get(0);
        Assertions.assertEquals(1, espacoOriginal.getPiso());
    }
    @Test
    void CT15_removerEspacoSelecionadoValido_DeveRemoverComSucesso() {

        ArquivoEspaco.adicionarEspaco("1", "100");
        Assertions.assertEquals(1, ArquivoEspaco.lerLista().size());


        int idParaRemover = 1;
        boolean resultado = ArquivoEspaco.excluirEspaco(idParaRemover);


        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Espaço removido com sucesso!",
                ArquivoEspaco.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoEspaco.lerLista().size());
    }

    @Test
    void CT16_removerEspacoNaoExistente_DeveFalhar() {

        ArquivoEspaco.adicionarEspaco("1", "100");
        Assertions.assertEquals(1, ArquivoEspaco.lerLista().size());


        int idParaRemover = 999;
        boolean resultado = ArquivoEspaco.excluirEspaco(idParaRemover);


        Assertions.assertFalse(resultado);
        Assertions.assertEquals("O ID do espaço não foi encontrado.",
                ArquivoEspaco.getUltimaMensagem());
        Assertions.assertEquals(1, ArquivoEspaco.lerLista().size());
    }
}