package br.com.crudjava.crudjava_junit.utils;

public class ValidacaoLoja {



    public static boolean validarNome(String nome) {
        return nome == null || nome.trim().isEmpty();
    }

    public static String validarFormatarTelefone(String telefone) {
        if (telefone == null) return null;

        String numeros = telefone.replaceAll("\\D", "");

        if (numeros.length() != 11) return null;

        String ddd = numeros.substring(0, 2);
        String parte1 = numeros.substring(2, 7);
        String parte2 = numeros.substring(7, 11);

        return "(" + ddd + ") " + parte1 + "-" + parte2;
    }

    public static boolean validarTipo(String tipo) {
        return tipo == null || tipo.trim().isEmpty();
    }
}
