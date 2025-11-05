package br.com.crudjava.crudjava_junit.persistencias;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import br.com.crudjava.crudjava_junit.models.Loja;
import br.com.crudjava.crudjava_junit.utils.ValidacaoLoja;

public class ArquivoLoja {
    private static final String CAMINHO_ARQUIVO = "lojas.dat";

    public static void salvarLista(ArrayList<Loja> lojas) {
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (!arquivo.exists()) arquivo.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo));
            oos.writeObject(lojas);
            oos.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista de lojas: " + e.getMessage());
        }
    }

    public static ArrayList<Loja> lerLista() {
        ArrayList<Loja> lista = new ArrayList<>();
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (arquivo.exists() && arquivo.length() > 0) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CAMINHO_ARQUIVO));
                lista = (ArrayList<Loja>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler lista de lojas: " + e.getMessage());
        }
        return lista;
    }

    public static String adicionarLoja(Loja novaLoja) {
        if (ValidacaoLoja.validarNome(novaLoja.getLojaNome()))
            return "O nome da loja não pode ser vazio.";
        String telefoneFormatado = ValidacaoLoja.validarFormatarTelefone(novaLoja.getLojaTelefone());
        if (telefoneFormatado == null) return "Preencha todos os campos corretamente!";
        if (ValidacaoLoja.validarTipo(novaLoja.getLojaTipo()))
            return "Preencha todos os campos corretamente!";

        ArrayList<Loja> lojas = lerLista();
        for (Loja l : lojas) {
            if (novaLoja.getLojaNome().equalsIgnoreCase(l.getLojaNome()) ||
                telefoneFormatado.equals(l.getLojaTelefone())) {
                return "Já existe uma loja com mesmo nome ou telefone.";
            }
        }
        novaLoja.setLojaTelefone(telefoneFormatado);
        lojas.add(novaLoja);
        salvarLista(lojas);
        return "Loja cadastrada com sucesso!";
    }

    public static String editarLoja(String nomeOriginal, String novoNome, String novoTelefone, String novoTipo) {
        ArrayList<Loja> lojas = lerLista();
        Loja lojaParaEditar = null;

        for (Loja l : lojas) {
            if (Objects.equals(l.getLojaNome(), nomeOriginal)) {
                lojaParaEditar = l;
                break;
            }
        }
        if (lojaParaEditar == null) return "Selecione uma loja para editar.";
        if (ValidacaoLoja.validarNome(novoNome) || ValidacaoLoja.validarTipo(novoTipo))
            return "Preencha todos os campos corretamente!";
        String telefoneFormatado = ValidacaoLoja.validarFormatarTelefone(novoTelefone);
        if (telefoneFormatado == null)
            return "Preencha todos os campos corretamente! O telefone deve estar completo (ex: (XX) XXXXX-XXXX).";

        for (Loja l : lojas) {
            if (l != lojaParaEditar) {
                if (novoNome.equalsIgnoreCase(l.getLojaNome()))
                    return "Já existe outra loja com esse nome. A edição não foi salva.";
                if (telefoneFormatado.equals(l.getLojaTelefone()))
                    return "Já existe outra loja com o telefone digitado. A edição não foi salva.";
            }
        }

        lojaParaEditar.setLojaNome(novoNome);
        lojaParaEditar.setLojaTelefone(telefoneFormatado);
        lojaParaEditar.setLojaTipo(novoTipo);
        salvarLista(lojas);
        return "Loja editada com sucesso!";
    }

    public static String removerLoja(String nomeLoja) {
        ArrayList<Loja> lojas = lerLista();
        for (Loja l : lojas) {
            if (Objects.equals(l.getLojaNome(), nomeLoja)) {
                lojas.remove(l);
                salvarLista(lojas);
                return "Loja removida com sucesso!";
            }
        }
        return "Erro ao remover loja.";
    }
}
