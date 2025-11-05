package br.com.crudjava.crudjava_junit.utils;

public class ValidacaoLoja {


    //nome da loja 
    public static boolean validarNome(String nome) {
        return nome == null || nome.trim().isEmpty();
    }

    // Valida e formata telefone no padrão (XX) XXXXX-XXXX
    public static String validarFormatarTelefone(String telefone) {
        if (telefone == null) return null;

        // Remove tudo que não for número
        String numeros = telefone.replaceAll("\\D", "");

        // Deve ter exatamente 11 dígitos
        if (numeros.length() != 11) return null;

        String ddd = numeros.substring(0, 2);
        String parte1 = numeros.substring(2, 7);
        String parte2 = numeros.substring(7, 11);

        return "(" + ddd + ") " + parte1 + "-" + parte2;
    }

    // Valida tipo da loja
    public static boolean validarTipo(String tipo) {
        return tipo == null || tipo.trim().isEmpty();
    }
}
