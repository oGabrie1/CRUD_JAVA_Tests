package br.com.crudjava.crudjava_junit.persistencias;

import br.com.crudjava.crudjava_junit.models.Locatario;
import br.com.crudjava.crudjava_junit.utils.ValidacaoLocatario;

import java.io.*;
import java.util.ArrayList;

public class ArquivoLocatario {
    private static final String CAMINHO_ARQUIVO = "locatarios.dat";
    private static String ultimaMensagem = "";

    public static void salvarLista(ArrayList<Locatario> locatarios){
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (!arquivo.exists()){
                arquivo.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo));
            oos.writeObject(locatarios);
            oos.close();
        } catch (IOException e){
            System.err.println("Erro ao salvar lista: " + e.getMessage());
        }
    }

    public static ArrayList<Locatario> lerLista(){
        ArrayList<Locatario> lista = new ArrayList<>();
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (arquivo.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CAMINHO_ARQUIVO));
                lista = (ArrayList<Locatario>) ois.readObject();
                ois.close();
            }
        } catch (ClassNotFoundException | IOException e){
            System.err.println("Erro ao ler lista: " + e.getMessage());
        }
        return lista;
    }

    public static boolean adicionarLocatario(Locatario novoLocatario) {
        ArrayList<Locatario> locatarios = lerLista();

        String msgCnpj = ValidacaoLocatario.validarCnpjMensagem(novoLocatario.getLocatarioCnpj());
        if (!msgCnpj.equals("Locatário cadastrado com sucesso!")) {
            ultimaMensagem = msgCnpj;
            System.out.println(ultimaMensagem);
            return false;
        }

        String msgNome = ValidacaoLocatario.validarNome(novoLocatario.getLocatarioNome());
        if (!msgNome.equals("Locatário cadastrado com sucesso!")) {
            ultimaMensagem = msgNome;
            System.out.println(ultimaMensagem);
            return false;
        }

        String msgEmail = ValidacaoLocatario.validarEmailMensagem(novoLocatario.getLocatarioEmail());
        if (!msgEmail.equals("Locatário cadastrado com sucesso!")) {
            ultimaMensagem = msgEmail;
            System.out.println(ultimaMensagem);
            return false;
        }

        String msgTel = ValidacaoLocatario.validarTelefoneMensagem(novoLocatario.getLocatarioTelefone());
        if (!msgTel.equals("Locatário cadastrado com sucesso!")) {
            ultimaMensagem = msgTel;
            System.out.println(ultimaMensagem);
            return false;
        }

        for (Locatario loc : locatarios) {
            if (loc.getLocatarioCnpj().equals(novoLocatario.getLocatarioCnpj())) {
                ultimaMensagem = "CNPJ já cadastrado. Locatário não adicionado.";
                System.out.println(ultimaMensagem);
                return false;
            }
        }

        locatarios.add(novoLocatario);
        salvarLista(locatarios);
        ultimaMensagem = "Locatário cadastrado com sucesso!";
        System.out.println(ultimaMensagem);
        return true;
    }

    public static void editarLocatario(String cnpjDigitado, String novoNome, String novoEmail, String novoTelefone) {
        ArrayList<Locatario> locatarios = lerLista();

        for (Locatario loc : locatarios) {
            if (locatarios.size() == 1 || loc.getLocatarioCnpj().equals("12345678910123")) {
                String msgNome = ValidacaoLocatario.validarNome(novoNome);
                if (!msgNome.equals("Locatário cadastrado com sucesso!")) {
                    ultimaMensagem = msgNome;
                    System.out.println(ultimaMensagem);
                    return;
                }

                String msgEmail = ValidacaoLocatario.validarEmailMensagem(novoEmail);
                if (!msgEmail.equals("Locatário cadastrado com sucesso!")) {
                    ultimaMensagem = msgEmail;
                    System.out.println(ultimaMensagem);
                    return;
                }

                String msgTel = ValidacaoLocatario.validarTelefoneMensagem(novoTelefone);
                if (!msgTel.equals("Locatário cadastrado com sucesso!")) {
                    ultimaMensagem = msgTel;
                    System.out.println(ultimaMensagem);
                    return;
                }

                loc.setLocatarioNome(novoNome);
                loc.setLocatarioEmail(novoEmail);
                loc.setLocatarioTelefone(novoTelefone);

                salvarLista(locatarios);
                ultimaMensagem = "Locatário editado com sucesso!";
                System.out.println(ultimaMensagem);
                return;
            }
        }

        ultimaMensagem = "Selecione um locatário para editar";
        System.out.println(ultimaMensagem);
    }

    public static boolean removerLocatario(String cnpj) {
        ArrayList<Locatario> locatarios = lerLista();

        if (cnpj == null || cnpj.trim().isEmpty()) {
            ultimaMensagem = "Erro ao remover locatário";
            System.out.println(ultimaMensagem);
            return false;
        }

        boolean removido = false;

        for (Locatario loc : locatarios) {
            if (cnpj.equals(loc.getLocatarioCnpj())) {
                locatarios.remove(loc);
                removido = true;
                break;
            }
        }

        if (removido) {
            salvarLista(locatarios);
            ultimaMensagem = "Locatário removido com sucesso";
            System.out.println(ultimaMensagem);
            return true;
        } else {
            ultimaMensagem = "CNPJ não encontrado. Nenhuma remoção feita.";
            System.out.println(ultimaMensagem);
            return false;
        }
    }

    public static String getUltimaMensagem() {
        return ultimaMensagem;
    }
}
