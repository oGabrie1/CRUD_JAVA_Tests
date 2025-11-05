package br.com.crudjava.crudjava_junit.utils;

public class ValidacaoLocatario {
    public static String validarCnpjMensagem(String cnpj) {
        if (cnpj == null || cnpj.isEmpty())
            return "O CNPJ deve ser preenchido completamente.";

        String digitos = cnpj.replaceAll("\\D", "");

        if (digitos.length() > 14)
            digitos = digitos.substring(0, 14);

        if (digitos.length() < 14)
            return "O CNPJ deve ser preenchido completamente.";

        return "Locatário cadastrado com sucesso!";
    }





    public static String validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()){
            return "O Nome da empresa não pode ser vazio.";
        };
        return "Locatário cadastrado com sucesso!";
    }


    public static String validarEmailMensagem(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Insira um e-mail válido";
        }

        if (!email.contains("@")) {
            return "Insira um e-mail válido";
        }

        return "Locatário cadastrado com sucesso!";
    }




    public static String validarTelefoneMensagem(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return "O Telefone deve ser preenchido completamente.";
        }

        String digitos = telefone.replaceAll("\\D", "");

        if (digitos.length() > 11)
            digitos = digitos.substring(0, 11);

        if (digitos.length() < 11)
            return "O Telefone deve ser preenchido completamente.";

        return "Locatário cadastrado com sucesso!";
    }

}
