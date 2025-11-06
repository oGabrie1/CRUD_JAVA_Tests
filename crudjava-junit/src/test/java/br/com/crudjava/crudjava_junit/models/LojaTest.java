package br.com.crudjava.crudjava_junit.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LojaTest {

    private Loja loja;

    @BeforeEach
    void setUp() {
        loja = new Loja("Tech Store", "11988887777", "Eletrônicos");
    }

    @Test
    void testConstrutor() {
        assertEquals("Tech Store", loja.getLojaNome(), "O nome deve ser inicializado corretamente.");
        assertEquals("11988887777", loja.getLojaTelefone(), "O telefone deve ser inicializado corretamente.");
        assertEquals("Eletrônicos", loja.getLojaTipo(), "O tipo deve ser inicializado corretamente.");
    }

    @Test
    void testSettersEGetters() {
        loja.setLojaNome("Gamer Zone");
        loja.setLojaTelefone("1133334444");
        loja.setLojaTipo("Jogos");

        assertAll("Verificando se os setters atualizaram corretamente os valores",
                () -> assertEquals("Gamer Zone", loja.getLojaNome()),
                () -> assertEquals("1133334444", loja.getLojaTelefone()),
                () -> assertEquals("Jogos", loja.getLojaTipo())
        );
    }

    @Test
    void testToStringFormatadoCorretamente() {
        String resultado = loja.toString();
        assertTrue(resultado.contains("Nome: Tech Store"), "O toString deve conter o nome.");
        assertTrue(resultado.contains("Telefone: 11988887777"), "O toString deve conter o telefone.");
        assertTrue(resultado.contains("Tipo: Eletrônicos"), "O toString deve conter o tipo.");
    }

    @Test
    void testCamposNulosOuVazios() {
        loja.setLojaNome(null);
        loja.setLojaTelefone("");
        loja.setLojaTipo(null);

        assertNull(loja.getLojaNome(), "O nome deve aceitar null.");
        assertEquals("", loja.getLojaTelefone(), "O telefone deve aceitar string vazia.");
        assertNull(loja.getLojaTipo(), "O tipo deve aceitar null.");
    }

    @Test
    void testAlteracaoIndividualDeCampos() {
        loja.setLojaTipo("Vestuário");
        assertEquals("Vestuário", loja.getLojaTipo(), "O tipo deve ser atualizado corretamente.");
    }
}