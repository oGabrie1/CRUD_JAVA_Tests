package br.com.crudjava.crudjava_junit.utils;
import br.com.crudjava.crudjava_junit.models.Locatario;

import java.time.LocalDate;

public class ValidacaoContrato {
    public static boolean validarLocatario(Locatario locatario) {
        return locatario != null;
    }
    public static boolean validarDataInicio(LocalDate dataInicio) {
        if (dataInicio == null) {
            return false;
        }
        return !dataInicio.isBefore(LocalDate.now());
    }
    public static boolean validarValorMensal(String valorMensalStr) {
        if (valorMensalStr == null || valorMensalStr.trim().isEmpty()) {
            return false;
        }
        String valorNormalizado = valorMensalStr.trim().replace(",", ".");

        try {
            double valor = Double.parseDouble(valorNormalizado);


            return valor > 0;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}

