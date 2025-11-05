package br.com.crudjava.crudjava_junit.persistencias;

import br.com.crudjava.crudjava_junit.models.Contrato;
import br.com.crudjava.crudjava_junit.models.Locatario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class ArquivoContratoTest {

    private static final String ARQ_DADOS = "contratos.dat";
    private static final String ARQ_ID = "proximoId_contrato.dat";

    @BeforeEach
    void setup() {

        File f_dados = new File(ARQ_DADOS);
        File f_id = new File(ARQ_ID);
        if (f_dados.exists()) f_dados.delete();
        if (f_id.exists()) f_id.delete();


        ArquivoContrato.salvarLista(new ArrayList<>());
        ArquivoContrato.salvarProximoId(1);


        File f_loc = new File("locatarios.dat");
        if (f_loc.exists()) f_loc.delete();

    }

    // --- ADIÇÃO DE CONTRATOS ---

    @Test
    void CT01_adicionarContratoValido_DeveAdicionarComSucesso() {

        Locatario locatario = new Locatario("11222333000144",
                "Empresa Alfa LTDA", "contato@alfa.com", "4199998888");

        LocalDate dataInicio = LocalDate.now();
        String valorMensal = "1.00";
        boolean status = true;


        boolean resultado = ArquivoContrato.adicionarContrato(locatario, dataInicio, valorMensal, status);


        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Contrato cadastrado com sucesso!",
                ArquivoContrato.getUltimaMensagem());


        ArrayList<Contrato> lista = ArquivoContrato.lerLista();
        Assertions.assertEquals(1, lista.size());

        Contrato contratoSalvo = lista.get(0);
        Assertions.assertEquals(1, contratoSalvo.getContratoId());
        Assertions.assertEquals("Empresa Alfa LTDA", contratoSalvo.getLocatario().getLocatarioNome());
        Assertions.assertEquals(dataInicio, contratoSalvo.getDataInicio());
        Assertions.assertEquals(1.0, contratoSalvo.getValorMensal());
        Assertions.assertTrue(contratoSalvo.isAtivo());
    }

    @Test
    void CT02_adicionarContratoLocatarioEmBranco_DeveFalhar() {
        Locatario locatario = null;
        LocalDate dataInicio = LocalDate.now();
        String valorMensal = "1500.00";
        boolean status = true;

        boolean resultado = ArquivoContrato.adicionarContrato(locatario, dataInicio, valorMensal, status);

        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Selecione um locatário para continuar",
                ArquivoContrato.getUltimaMensagem());

        Assertions.assertEquals(0, ArquivoContrato.lerLista().size());
    }

    private Locatario getLocatarioValido() {
        return new Locatario("11222333000144",
                "Empresa Teste", "teste@teste.com", "4199998888");
    }

    @Test
    void CT03_adicionarContratoDataEmBranco_DeveFalhar() {
        // --- Entradas ---
        Locatario locatario = getLocatarioValido();
        LocalDate dataInicio = null; // CT03: Campo em branco
        String valorMensal = "1200.00";
        boolean status = true;

        // --- Ação ---
        boolean resultado = ArquivoContrato.adicionarContrato(locatario, dataInicio, valorMensal, status);

        // --- Verificação ---
        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Informe uma data de início válida.",
                ArquivoContrato.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoContrato.lerLista().size());
    }

    @Test
    void CT04_adicionarContratoValorEmBranco_DeveFalhar() {
        // --- Entradas ---
        Locatario locatario = getLocatarioValido();
        LocalDate dataInicio = LocalDate.now();
        String valorMensal = ""; // CT04: Campo em branco
        boolean status = true;

        // --- Ação ---
        boolean resultado = ArquivoContrato.adicionarContrato(locatario, dataInicio, valorMensal, status);

        // --- Verificação ---
        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Informe um valor mensal válido.",
                ArquivoContrato.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoContrato.lerLista().size());
    }

    @Test
    void CT05_adicionarContratoValorAbaixoLimiteZero_DeveFalhar() {
        // --- Entradas ---
        Locatario locatario = getLocatarioValido();
        LocalDate dataInicio = LocalDate.now();
        String valorMensal = "0.00"; // CT05: Valor limite inferior inválido
        boolean status = true;

        // --- Ação ---
        boolean resultado = ArquivoContrato.adicionarContrato(locatario, dataInicio, valorMensal, status);

        // --- Verificação ---
        Assertions.assertFalse(resultado);
        Assertions.assertEquals("O valor mensal deve ser maior que zero",
                ArquivoContrato.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoContrato.lerLista().size());
    }

    @Test
    void CT06_adicionarContratoValorNaoNumerico_DeveFalhar() {
        // --- Entradas ---
        Locatario locatario = getLocatarioValido();
        LocalDate dataInicio = LocalDate.now();
        String valorMensal = "ab@"; // CT06: Caracteres não numéricos
        boolean status = true;

        // --- Ação ---
        boolean resultado = ArquivoContrato.adicionarContrato(locatario, dataInicio, valorMensal, status);

        // --- Verificação ---
        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Informe um valor mensal válido.",
                ArquivoContrato.getUltimaMensagem());
        Assertions.assertEquals(0, ArquivoContrato.lerLista().size());
    }

    @Test
    void CT08_adicionarContratoDuplicadoAtivo_DeveFalhar() {
        // --- Configuração ---
        Locatario locatario = getLocatarioValido();
        // Adiciona o primeiro contrato (válido)
        boolean resultado1 = ArquivoContrato.adicionarContrato(
                locatario, LocalDate.now(), "1500", true);
        Assertions.assertTrue(resultado1);
        Assertions.assertEquals(1, ArquivoContrato.lerLista().size());

        // --- Ação (Tenta adicionar o segundo contrato ATIVO para o MESMO locatário) ---
        boolean resultado2 = ArquivoContrato.adicionarContrato(
                locatario, LocalDate.now().plusDays(1), "2000", true);

        // --- Verificação ---
        Assertions.assertFalse(resultado2);
        // Verifica a mensagem de regra de negócio (CT08)
        Assertions.assertEquals("Contrato já cadastrado para este locatário.",
                ArquivoContrato.getUltimaMensagem());
        // Garante que o segundo contrato não foi salvo
        Assertions.assertEquals(1, ArquivoContrato.lerLista().size());
    }

// --- REMOÇÃO DE CONTRATOS ---

    @Test
    void CT09_removerContratoValido_DeveRemover() {
        // --- Configuração ---
        // Adiciona um contrato para poder removê-lo
        ArquivoContrato.adicionarContrato(
                getLocatarioValido(), LocalDate.now(), "1500", true);
        Assertions.assertEquals(1, ArquivoContrato.lerLista().size());
        int idParaRemover = 1; // O ID do primeiro contrato adicionado

        // --- Ação ---
        boolean resultado = ArquivoContrato.removerContrato(idParaRemover);

        // --- Verificação ---
        Assertions.assertTrue(resultado);
        Assertions.assertEquals("Contrato removido com sucesso.",
                ArquivoContrato.getUltimaMensagem());
        // Garante que a lista está vazia
        Assertions.assertEquals(0, ArquivoContrato.lerLista().size());
    }

    @Test
    void CT11_removerContratoIdNaoExistente_DeveFalhar() {
        // --- Configuração ---
        // Adiciona um contrato
        ArquivoContrato.adicionarContrato(
                getLocatarioValido(), LocalDate.now(), "1500", true);
        Assertions.assertEquals(1, ArquivoContrato.lerLista().size());
        int idInexistente = 999; // ID que não está na lista

        // --- Ação ---
        boolean resultado = ArquivoContrato.removerContrato(idInexistente);

        // --- Verificação ---
        Assertions.assertFalse(resultado);
        Assertions.assertEquals("Contrato não encontrado. Nenhuma remoção foi feita",
                ArquivoContrato.getUltimaMensagem());
        // Garante que o contrato original não foi removido
        Assertions.assertEquals(1, ArquivoContrato.lerLista().size());
    }
}