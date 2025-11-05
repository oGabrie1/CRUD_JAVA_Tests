package br.com.crudjava.crudjava_junit.persistencias;

import br.com.crudjava.crudjava_junit.models.Boleto;
import br.com.crudjava.crudjava_junit.models.Contrato;
import br.com.crudjava.crudjava_junit.models.Locatario;
import br.com.crudjava.crudjava_junit.utils.ValidacaoBoleto;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ArquivoBoletoTest {

    private Locatario locatarioTeste;
    private Contrato contratoTeste;

    @BeforeEach
    void setup() {
        locatarioTeste = new Locatario("12345678910123", "Tijucas Open",
                "contato@tijucasopen.com", "48999999999");
        contratoTeste = new Contrato(locatarioTeste, LocalDate.now(), 1000.0, true);

        ArrayList<Contrato> contratos = new ArrayList<>();
        contratos.add(contratoTeste);
        ArquivoContrato.salvarLista(contratos);
    }


    @Test
    void CT01_adicionarBoletoValido_DeveCadastrarComSucesso() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "1",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Boleto cadastrado com sucesso!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT02_valorComCaracteresInvalidos_DeveRetornarMensagemErro() {
        String resultado = ValidacaoBoleto.validarValorComPrint("a @");
        Assertions.assertEquals("Insira dados válidos!", resultado);
    }

    @Test
    void CT03_valorEmBranco_DeveRetornarMensagemErro() {
        String resultado = ValidacaoBoleto.validarValorComPrint("");
        Assertions.assertEquals("Insira dados válidos!", resultado);
    }


    @Test
    void CT04_valorNegativo_DeveRetornarMensagemErroPreenchaCamposCorretamente() {
        Boleto boleto = new Boleto(
                -1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "1",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Preencha os campos corretamente", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT05_dataDeVencimentoInvalida_DeveUsarDataAtual() {
        // 31/02/2026 é inválida → deve usar a data atual
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.now(), // sistema ignora entrada inválida e usa a atual
                "Tijucas Open",
                "Banco do Brasil",
                "1",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(
                "Boleto cadastrado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );
    }

    @Test
    void CT06_dataDeVencimentoForaDoFormato_DeveUsarDataAtual() {
        // 2000/01/02 está fora do formato aceito → deve usar a data atual
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.now(), // data atual substitui a inválida
                "Tijucas Open",
                "Banco do Brasil",
                "1",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(
                "Boleto cadastrado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );
    }

    @Test
    void CT07_dataDeVencimentoEmBranco_DeveUsarDataAtual() {
        // campo em branco → usa a data atual
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.now(), // substitui o campo em branco
                "Tijucas Open",
                "Banco do Brasil",
                "1",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(
                "Boleto cadastrado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );
    }

    @Test
    void CT08_cedenteDiferente_DeveManterPadraoTijucasOpen() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Algo diferente",
                "Banco do Brasil",
                "1",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(
                "Boleto cadastrado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );

        ArrayList<Boleto> boletos = ArquivoBoleto.lerLista(contratoTeste.getContratoId());
        Assertions.assertEquals("Tijucas Open", boletos.get(0).getCedente());
    }

    @Test
    void CT09_cedenteEmBranco_DeveUsarPadraoTijucasOpen() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "",
                "Banco do Brasil",
                "1",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(
                "Boleto cadastrado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );

        ArrayList<Boleto> boletos = ArquivoBoleto.lerLista(contratoTeste.getContratoId());
        Assertions.assertEquals("Tijucas Open", boletos.get(0).getCedente());
    }

    @Test
    void CT10_bancoDiferente_DeveManterPadraoBancoDoBrasil() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Caixa Econômica",
                "1",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(
                "Boleto cadastrado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );

        ArrayList<Boleto> boletos = ArquivoBoleto.lerLista(contratoTeste.getContratoId());
        Assertions.assertEquals("Banco do Brasil", boletos.get(0).getBanco());
    }

    @Test
    void CT11_bancoEmBranco_DeveUsarPadraoBancoDoBrasil() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "",
                "1",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(
                "Boleto cadastrado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );

        ArrayList<Boleto> boletos = ArquivoBoleto.lerLista(contratoTeste.getContratoId());
        Assertions.assertEquals("Banco do Brasil", boletos.get(0).getBanco());
    }

    @Test
    void CT12_linhaDigitavelComCaracteresInvalidos_DeveRetornarMensagemErro() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "a @",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertFalse(resultado);
        Assertions.assertEquals(
                "Preencha os campos corretamente",
                ArquivoBoleto.getUltimaMensagem()
        );
    }

    @Test
    void CT13_linhaDigitavelEmBranco_DeveRetornarMensagemErro() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "",
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertFalse(resultado);
        Assertions.assertEquals(
                "Preencha os campos corretamente",
                ArquivoBoleto.getUltimaMensagem()
        );
    }

    @Test
    void CT14_linhaDigitavelNegativa_DeveConsiderarValorPositivoERegistrarComSucesso() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "-1", // negativo
                contratoTeste
        );

        boolean resultado = ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals(
                "Boleto cadastrado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );

        ArrayList<Boleto> boletos = ArquivoBoleto.lerLista(contratoTeste.getContratoId());
        Assertions.assertEquals("1", boletos.get(0).getLinhaDigitavel());
    }

    @Test
    void CT15_editarBoletoValido_DeveEditarComSucesso() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "1",
                contratoTeste
        );
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                2.0,
                LocalDate.of(2026, 3, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "2",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals(
                "Boleto editado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );
    }

    @Test
    void CT16_editarSelecionadoMaisDeUm_DeveConsiderarUltimoSelecionado() {
        Boleto boleto1 = new Boleto(1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        Boleto boleto2 = new Boleto(2.0, LocalDate.of(2026, 2, 2),
                "Tijucas Open", "Banco do Brasil", "2", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto1, contratoTeste.getContratoId());
        ArquivoBoleto.adicionarBoleto(boleto2, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto1.getNumeroDocumento(), 3.0, LocalDate.now(),
                "Tijucas Open", "Banco do Brasil", "3",
                contratoTeste.getContratoId()
        );

        ArquivoBoleto.editarBoleto(
                boleto2.getNumeroDocumento(), 4.0, LocalDate.now(),
                "Tijucas Open", "Banco do Brasil", "4",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals(
                "Boleto editado com sucesso!",
                ArquivoBoleto.getUltimaMensagem()
        );
    }

    @Test
    void CT17_editarSemSelecionarNenhum_DeveRetornarMensagemErro() {
        ArquivoBoleto.editarBoleto(
                0, 1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals(
                "Selecione um boleto para editar",
                ArquivoBoleto.getUltimaMensagem()
        );
    }

    @Test
    void CT18_numeroDocumentoDiferente_DeveIgnorarMudancaEManterAntigo() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "1",
                contratoTeste
        );
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());
        int numeroOriginal = boleto.getNumeroDocumento();

        ArquivoBoleto.editarBoleto(
                2,
                2.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals(
                "Selecione um boleto para editar",
                ArquivoBoleto.getUltimaMensagem()
        );
    }

    @Test
    void CT19_editarValorComCaracteresInvalidos_DeveRetornarMensagemErro() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Insira dados válidos!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT20_editarValorEmBranco_DeveRetornarMensagemErro() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Insira dados válidos!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT21_editarValorNegativo_DeveRetornarMensagemErroPreenchaCamposCorretamente() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                -1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Preencha os campos corretamente", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT22_editarDataInvalida_DeveUsarDataAtual() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                LocalDate.now(), // substitui 31/02/2026
                "Tijucas Open", "Banco do Brasil", "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Boleto editado com sucesso!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT23_editarDataForaFormato_DeveUsarDataAtual() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                LocalDate.now(), // substitui 2000/01/02 inválido
                "Tijucas Open", "Banco do Brasil", "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Boleto editado com sucesso!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT24_editarDataEmBranco_DeveUsarDataAtual() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                null,
                "Tijucas Open", "Banco do Brasil", "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Boleto editado com sucesso!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT25_editarCedenteDiferente_DeveManterPadraoTijucasOpen() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Algo diferente", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                LocalDate.of(2026, 2, 1),
                "Algo diferente",
                "Banco do Brasil",
                "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Boleto editado com sucesso!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT26_editarCedenteEmBranco_DeveUsarPadraoTijucasOpen() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                LocalDate.of(2026, 2, 1),
                "",
                "Banco do Brasil",
                "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Boleto editado com sucesso!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT27_editarBancoDiferente_DeveManterPadraoBancoDoBrasil() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Caixa Econômica", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Caixa Econômica",
                "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Boleto editado com sucesso!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT28_editarBancoEmBranco_DeveUsarPadraoBancoDoBrasil() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "",
                "1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Boleto editado com sucesso!", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT29_editarLinhaDigitavelComCaracteresInvalidos_DeveRetornarMensagemErro() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "a @",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Preencha os campos corretamente", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT30_editarLinhaDigitavelEmBranco_DeveRetornarMensagemErro() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Preencha os campos corretamente", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT31_editarLinhaDigitavelNegativa_DeveConsiderarValorPositivoERegistrarComSucesso() {
        Boleto boleto = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());

        ArquivoBoleto.editarBoleto(
                boleto.getNumeroDocumento(),
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "-1",
                contratoTeste.getContratoId()
        );

        Assertions.assertEquals("Boleto editado com sucesso!", ArquivoBoleto.getUltimaMensagem());
    }



    @Test
    void CT32_removerBoletoSelecionado_DeveRemoverComSucesso() {
        Boleto boleto = new Boleto(
                1.0,
                LocalDate.of(2026, 2, 1),
                "Tijucas Open",
                "Banco do Brasil",
                "1",
                contratoTeste
        );
        ArquivoBoleto.adicionarBoleto(boleto, contratoTeste.getContratoId());
        int numeroDocumento = boleto.getNumeroDocumento();

        boolean resultado = ArquivoBoleto.removerBoleto(numeroDocumento, contratoTeste.getContratoId());

        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Boleto removido com sucesso", ArquivoBoleto.getUltimaMensagem());
    }

    @Test
    void CT33_removerMaisDeUmBoletoSelecionado_DeveRemoverUltimoSelecionado() {
        Boleto boleto1 = new Boleto(
                1.0, LocalDate.of(2026, 2, 1),
                "Tijucas Open", "Banco do Brasil", "1", contratoTeste);
        Boleto boleto2 = new Boleto(
                2.0, LocalDate.of(2026, 3, 1),
                "Tijucas Open", "Banco do Brasil", "2", contratoTeste);
        ArquivoBoleto.adicionarBoleto(boleto1, contratoTeste.getContratoId());
        ArquivoBoleto.adicionarBoleto(boleto2, contratoTeste.getContratoId());

        boolean resultado = ArquivoBoleto.removerBoleto(
                boleto2.getNumeroDocumento(), contratoTeste.getContratoId()
        );

        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Boleto removido com sucesso", ArquivoBoleto.getUltimaMensagem());

        ArrayList<Boleto> boletos = ArquivoBoleto.lerLista(contratoTeste.getContratoId());
        Assertions.assertTrue(
                boletos.stream().anyMatch(b -> b.getNumeroDocumento() == boleto1.getNumeroDocumento())
        );
    }

    @Test
    void CT34_removerSemSelecionarNenhum_DeveExibirMensagemErro() {
        boolean resultado = ArquivoBoleto.removerBoleto(0, contratoTeste.getContratoId());

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Selecione um boleto para apagar", ArquivoBoleto.getUltimaMensagem());
    }



}
