package br.com.crudjava.crudjava_junit.models;

import java.io.Serial;
import java.io.Serializable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EspacoTest {

    private Espaco espaco;

    @BeforeEach
    void setUp() {
        espaco = new Espaco(1, 2, 35.5);
    }

    @Test
    void testConstrutor() {
        assertEquals(1, espaco.getId(), "O ID deve ser inicializado corretamente.");
        assertEquals(2, espaco.getPiso(), "O piso deve ser inicializado corretamente.");
        assertEquals(35.5, espaco.getArea(), "A área deve ser inicializada corretamente.");
    }

    @Test
    void testSettersEGetters() {
        espaco.setId(10);
        espaco.setPiso(3);
        espaco.setArea(50.0);
        espaco.setStatus(true);

        assertAll("Verificando se os setters atualizaram corretamente os valores",
                () -> assertEquals(10, espaco.getId()),
                () -> assertEquals(3, espaco.getPiso()),
                () -> assertEquals(50.0, espaco.getArea()),
                () -> assertTrue(espaco.isStatus())
        );
    }

    @Test
    void testAlteracoesIndividuais() {
        espaco.setPiso(4);
        assertEquals(4, espaco.getPiso(), "O piso deve ter sido atualizado.");

        espaco.setArea(60.3);
        assertEquals(60.3, espaco.getArea(), "A área deve ter sido atualizada.");
    }

    @Test
    void testValoresLimiteOuInvalidos() {
        espaco.setArea(0);
        assertEquals(0, espaco.getArea(), "A área deve aceitar valor zero.");

        espaco.setPiso(-1);
        assertEquals(-1, espaco.getPiso(), "O piso deve aceitar número negativo (caso não haja validação).");
    }

    @Test
    void testStatusEspaco() {
        espaco.setStatus(true);
        assertTrue(espaco.isStatus(), "O status deve ser verdadeiro.");

        espaco.setStatus(false);
        assertFalse(espaco.isStatus(), "O status deve ser falso.");
    }

    @Test
    void testToString() {
        String resultado = espaco.toString();
        assertTrue(resultado.contains("Piso: 2"), "O toString deve exibir o piso corretamente.");
        assertTrue(resultado.contains("Area: 35.5"), "O toString deve exibir a área corretamente.");
    }
}
