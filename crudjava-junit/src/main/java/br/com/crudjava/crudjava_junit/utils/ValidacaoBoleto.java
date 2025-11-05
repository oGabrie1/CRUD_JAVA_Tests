package br.com.crudjava.crudjava_junit.utils;

import java.time.LocalDate;

public class ValidacaoBoleto {

    public static String validarValor(String valorTexto) {
        if (valorTexto == null || valorTexto.trim().isEmpty()) {
            return "Insira dados válidos!";
        }

        try {
            double valor = Double.parseDouble(valorTexto);

            if (valor == 0.0) {
                return "Insira dados válidos!";
            }

            if (valor < 0.0) {
                return "Preencha os campos corretamente";
            }
        } catch (NumberFormatException e) {
            return "Insira dados válidos!";
        }

        return "Boleto cadastrado com sucesso!";
    }


    public static String validarData(LocalDate vencimento) {
        if (vencimento == null) {
            return "Boleto cadastrado com sucesso!";
        }

        return "Boleto cadastrado com sucesso!";
    }

    public static String validarLinhaDigitavel(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            return "Preencha os campos corretamente";
        }

        String apenasNumeros = linha.replaceAll("\\D", "");
        if (apenasNumeros.isEmpty()) {
            return "Preencha os campos corretamente";
        }

        return "Boleto cadastrado com sucesso!";
    }

    public static String validarValorComPrint(String valor) {
        String resultado = validarValor(valor);
        System.out.println(resultado);
        return resultado;
    }


}
