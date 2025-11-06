package br.com.crudjava.crudjava_junit.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ContratoTest {

    private Contrato contrato;
    private Locatario locatario;

    @BeforeEach
    void setUp() {
        locatario = new Locatario(
                "12345678000199",
                "Empresa Teste LTDA",
                "teste@empresa.com",
                "(11)99999-9999"
        );

        contrato = new Contrato(
                locatario,
                LocalDate.of(2025, 1, 1),
                1500.00,
                true
        );
    }

    @Test
    @DisplayName("CT01 - Deve inicializar corretamente com o construtor")
    void testConstrutor() {
        assertEquals(locatario, contrato.getLocatario());
        assertEquals(LocalDate.of(2025, 1, 1), contrato.getDataInicio());
        assertEquals(1500.00, contrato.getValorMensal());
        assertTrue(contrato.isContratoStatus());
        assertNotNull(contrato.getBoletos());
        assertTrue(contrato.getBoletos().isEmpty());
    }

    @Test
    @DisplayName("CT02 - Deve permitir alterar todos os campos via setters e getters")
    void testSettersEGetters() {
        ArrayList<Boleto> listaBoletos = new ArrayList<>();
        LocalDate novaData = LocalDate.of(2026, 2, 2);
        Locatario novoLoc = new Locatario(
                "98765432000155",
                "Nova Empresa SA",
                "nova@empresa.com",
                "(11)88888-8888"
        );

        contrato.setContratoId(101);
        contrato.setLocatario(novoLoc);
        contrato.setDataInicio(novaData);
        contrato.setValorMensal(2500.75);
        contrato.setBoletos(listaBoletos);
        contrato.setContratoStatus(false);

        assertEquals(101, contrato.getContratoId());
        assertEquals(novoLoc, contrato.getLocatario());
        assertEquals(novaData, contrato.getDataInicio());
        assertEquals(2500.75, contrato.getValorMensal());
        assertEquals(listaBoletos, contrato.getBoletos());
        assertFalse(contrato.isContratoStatus());
    }

    @Test
    @DisplayName("CT03 - Deve permitir adicionar boletos à lista")
    void testAdicionarBoletos() {
        Boleto boleto1 = new Boleto(
                1200.00,
                LocalDate.of(2025, 3, 10),
                "Empresa ABC",
                "Banco X",
                "00123.45678 91234.567890 1 99990000012000",
                contrato
        );

        Boleto boleto2 = new Boleto(
                1300.00,
                LocalDate.of(2025, 4, 10),
                "Empresa ABC",
                "Banco X",
                "00123.45678 91234.567890 1 99990000013000",
                contrato
        );

        contrato.getBoletos().add(boleto1);
        contrato.getBoletos().add(boleto2);

        assertEquals(2, contrato.getBoletos().size());
        assertEquals(boleto1, contrato.getBoletos().get(0));
        assertEquals(boleto2, contrato.getBoletos().get(1));
    }

    @Test
    @DisplayName("CT04 - Deve manter dados após serialização e desserialização")
    void testSerializacao() throws Exception {
        contrato.setContratoId(50);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(contrato);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Contrato desserializado = (Contrato) ois.readObject();

        assertEquals(contrato.getContratoId(), desserializado.getContratoId());
        assertEquals(contrato.getValorMensal(), desserializado.getValorMensal());
        assertEquals(contrato.isContratoStatus(), desserializado.isContratoStatus());
        assertEquals(contrato.getDataInicio(), desserializado.getDataInicio());
        assertNotNull(desserializado.getBoletos());
    }

    @Test
    @DisplayName("CT05 - Deve retornar true em isAtivo() quando contratoStatus for true")
    void testIsAtivoTrue() {
        contrato.setContratoStatus(true);
        assertTrue(contrato.isAtivo());
    }

    @Test
    @DisplayName("CT06 - Deve retornar false em isAtivo() quando contratoStatus for false")
    void testIsAtivoFalse() {
        contrato.setContratoStatus(false);
        assertFalse(contrato.isAtivo());
    }
}