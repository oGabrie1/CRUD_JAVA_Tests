package br.com.crudjava.crudjava_junit.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BoletoTest {

    private Boleto boleto;
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

        boleto = new Boleto(
                1500.75,
                LocalDate.of(2025, 12, 10),
                "Empresa XYZ",
                "Banco do Brasil",
                "00190.00009 01234.567890 12345.678901 2 98760000010000",
                contrato
        );
    }

    @Test
    @DisplayName("CT01 - Deve inicializar corretamente com o construtor")
    void testConstrutor() {
        assertEquals(1500.75, boleto.getValor());
        assertEquals(LocalDate.of(2025, 12, 10), boleto.getVencimento());
        assertEquals("Empresa XYZ", boleto.getCedente());
        assertEquals("Banco do Brasil", boleto.getBanco());
        assertEquals("00190.00009 01234.567890 12345.678901 2 98760000010000", boleto.getLinhaDigitavel());
        assertEquals(contrato, boleto.getContrato());
        assertEquals(locatario, contrato.getLocatario());
    }

    @Test
    @DisplayName("CT02 - Deve permitir alteração de todos os campos via setters")
    void testSetters() {
        LocalDate novaData = LocalDate.of(2025, 11, 5);
        boleto.setNumeroDocumento(100);
        boleto.setValor(2000.99);
        boleto.setVencimento(novaData);
        boleto.setCedente("Empresa ABC");
        boleto.setBanco("Caixa Econômica");
        boleto.setLinhaDigitavel("10490.00009 09876.543210 12345.678901 3 00000020009999");

        assertEquals(100, boleto.getNumeroDocumento());
        assertEquals(2000.99, boleto.getValor());
        assertEquals(novaData, boleto.getVencimento());
        assertEquals("Empresa ABC", boleto.getCedente());
        assertEquals("Caixa Econômica", boleto.getBanco());
        assertEquals("10490.00009 09876.543210 12345.678901 3 00000020009999", boleto.getLinhaDigitavel());
    }

    @Test
    @DisplayName("CT03 - Deve gerar string formatada corretamente com toString()")
    void testToString() {
        boleto.setNumeroDocumento(55);
        String texto = boleto.toString();

        assertTrue(texto.contains("Número do Documento: 55"));
        assertTrue(texto.contains("Valor: R$1500.75"));
        assertTrue(texto.contains("Vencimento: 2025-12-10"));
        assertTrue(texto.contains("Cedente: Empresa XYZ"));
        assertTrue(texto.contains("Banco: Banco do Brasil"));
        assertTrue(texto.contains("Linha digitável: 00190.00009"));
    }

    @Test
    @DisplayName("CT04 - Deve ser serializável e manter os dados após desserialização")
    void testSerializacao() throws Exception {
        boleto.setNumeroDocumento(10);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(boleto);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Boleto desserializado = (Boleto) ois.readObject();

        assertEquals(boleto.getValor(), desserializado.getValor());
        assertEquals(boleto.getCedente(), desserializado.getCedente());
        assertEquals(boleto.getBanco(), desserializado.getBanco());
        assertEquals(boleto.getLinhaDigitavel(), desserializado.getLinhaDigitavel());
    }

    @Test
    @DisplayName("CT05 - Deve aceitar contrato nulo")
    void testContratoNulo() {
        boleto.setContrato(null);
        assertNull(boleto.getContrato());
    }
}