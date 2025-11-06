package br.com.crudjava.crudjava_junit.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocatarioTest {

    private Locatario locatario;

    @BeforeEach
    void setUp() {
        locatario = new Locatario("12345678000199", "Empresa X", "contato@empresa.com", "11999999999");
    }

    @Test
    void testConstrutor() {
        assertEquals("12345678000199", locatario.getLocatarioCnpj(), "O CNPJ deve ser inicializado corretamente.");
        assertEquals("Empresa X", locatario.getLocatarioNome(), "O nome deve ser inicializado corretamente.");
        assertEquals("contato@empresa.com", locatario.getLocatarioEmail(), "O e-mail deve ser inicializado corretamente.");
        assertEquals("11999999999", locatario.getLocatarioTelefone(), "O telefone deve ser inicializado corretamente.");
    }

    @Test
    void testSettersEGetters() {
        locatario.setLocatarioCnpj("98765432000155");
        locatario.setLocatarioNome("Empresa Y");
        locatario.setLocatarioEmail("email@empresay.com");
        locatario.setLocatarioTelefone("11988888888");

        assertAll("Verificando se os setters atualizaram corretamente os valores",
                () -> assertEquals("98765432000155", locatario.getLocatarioCnpj()),
                () -> assertEquals("Empresa Y", locatario.getLocatarioNome()),
                () -> assertEquals("email@empresay.com", locatario.getLocatarioEmail()),
                () -> assertEquals("11988888888", locatario.getLocatarioTelefone())
        );
    }

    @Test
    void testAlteracoesIndividuais() {
        locatario.setLocatarioNome("Novo Nome Ltda");
        assertEquals("Novo Nome Ltda", locatario.getLocatarioNome());

        locatario.setLocatarioEmail("novoemail@teste.com");
        assertEquals("novoemail@teste.com", locatario.getLocatarioEmail());
    }

    @Test
    void testCamposNulosOuVazios() {
        locatario.setLocatarioCnpj(null);
        locatario.setLocatarioNome("");
        locatario.setLocatarioEmail(null);
        locatario.setLocatarioTelefone("");

        assertNull(locatario.getLocatarioCnpj(), "CNPJ deve aceitar null.");
        assertEquals("", locatario.getLocatarioNome(), "Nome deve aceitar string vazia.");
        assertNull(locatario.getLocatarioEmail(), "Email deve aceitar null.");
        assertEquals("", locatario.getLocatarioTelefone(), "Telefone deve aceitar string vazia.");
    }
}