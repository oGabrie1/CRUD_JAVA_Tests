package br.com.crudjava.crudjava_junit.persistencias;

import br.com.crudjava.crudjava_junit.models.Espaco;
import br.com.crudjava.crudjava_junit.utils.ValidacaoEspaco;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ArquivoEspaco {

    private static final Path ARQUIVO_DADOS_PATH = Paths.get("espacos.dat");
    private static final Path PROXIMO_ID_PATH = Paths.get("proximoId_espaco.dat");

    // Adicionado para testes JUnit
    private static String ultimaMensagem;

    public static String getUltimaMensagem() {
        return ultimaMensagem;
    }

    // --- MÉTODOS PRIVADOS (AGORA PACKAGE-PRIVATE PARA TESTES) ---

    private static int lerProximoId() {
        if (Files.exists(PROXIMO_ID_PATH)) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(PROXIMO_ID_PATH.toFile()))) {
                return dis.readInt();
            } catch (IOException e) {
                System.err.println("Erro ao ler " + PROXIMO_ID_PATH + ", reiniciando contador. Erro: " + e.getMessage());
                return 1;
            }
        }
        return 1;
    }

    /*
     * Alterado de private para package-private (sem modificador)
     * para que a classe de teste possa limpar o arquivo.
     */
    static void salvarProximoId(int proximoId) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(PROXIMO_ID_PATH.toFile()))) {
            dos.writeInt(proximoId);
        } catch (IOException e) {
            System.err.println("Erro ao salvar " + PROXIMO_ID_PATH + ": " + e.getMessage());
        }
    }

    /*
     * Alterado de private para package-private (sem modificador)
     * para que a classe de teste possa limpar o arquivo.
     */
    static void salvarLista(ArrayList<Espaco> espacos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS_PATH.toFile()))) {
            oos.writeObject(espacos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar " + ARQUIVO_DADOS_PATH + ": " + e.getMessage());
        }
    }

    public static ArrayList<Espaco> lerLista() {
        if (Files.notExists(ARQUIVO_DADOS_PATH)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS_PATH.toFile()))) {
            return (ArrayList<Espaco>) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Erro ao ler " + ARQUIVO_DADOS_PATH + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // --- MÉTODOS PÚBLICOS (MODIFICADOS PARA TESTES) ---

    public static boolean adicionarEspaco(String pisoStr, String areaStr) {
        // 1. VALIDAÇÃO
        if (!ValidacaoEspaco.validarPiso(pisoStr)) {
            ultimaMensagem = "Piso inválido.";
            System.out.println(ultimaMensagem); // <-- ADICIONADO PARA O SEU DOCUMENTO
            return false;
        }
        if (!ValidacaoEspaco.validarArea(areaStr)) {
            ultimaMensagem = "Área inválida.";
            System.out.println(ultimaMensagem); // <-- ADICIONADO PARA O SEU DOCUMENTO
            return false;
        }

        // 2. CONVERSÃO (Parsing)
        int piso = Integer.parseInt(pisoStr.trim());
        double area = Double.parseDouble(areaStr.trim().replace(",", "."));

        // 3. LÓGICA DE NEGÓCIO (Persistência)
        ArrayList<Espaco> espacos = lerLista();
        int novoId = lerProximoId();

        espacos.add(new Espaco(novoId, piso, area));
        salvarLista(espacos);
        salvarProximoId(novoId + 1);

        ultimaMensagem = "Espaço cadastrado com sucesso!";
        System.out.println(ultimaMensagem); // <-- ADICIONADO PARA O SEU DOCUMENTO
        return true;
    }

    public static boolean excluirEspaco(int id) {
        ArrayList<Espaco> espacos = lerLista();
        boolean removido = espacos.removeIf(espaco -> espaco.getId() == id);

        if (removido) {
            salvarLista(espacos);
            ultimaMensagem = "Espaço removido com sucesso!";
            System.out.println(ultimaMensagem); // <-- ADICIONADO PARA O SEU DOCUMENTO
            return true;
        } else {
            ultimaMensagem = "O ID do espaço não foi encontrado.";
            System.out.println(ultimaMensagem); // <-- ADICIONADO PARA O SEU DOCUMENTO
            return false;
        }
    }

    public static boolean editarEspaco(int id, String novoPisoStr, String novaAreaStr){
        // 1. VALIDAÇÃO
        if (!ValidacaoEspaco.validarPiso(novoPisoStr)) {
            ultimaMensagem = "Piso inválido.";
            System.out.println(ultimaMensagem); // <-- ADICIONADO PARA O SEU DOCUMENTO
            return false;
        }
        if (!ValidacaoEspaco.validarArea(novaAreaStr)) {
            ultimaMensagem = "Área inválida.";
            System.out.println(ultimaMensagem); // <-- ADICIONADO PARA O SEU DOCUMENTO
            return false;
        }

        // 2. CONVERSÃO (Parsing)
        int novoPiso = Integer.parseInt(novoPisoStr.trim());
        double novaArea = Double.parseDouble(novaAreaStr.trim().replace(",", "."));

        // 3. LÓGICA DE NEGÓCIO (Persistência)
        ArrayList<Espaco> espacos = lerLista();
        boolean encontrado = false;

        for (Espaco espaco : espacos) {
            if (espaco.getId() == id) {
                espaco.setPiso(novoPiso);
                espaco.setArea(novaArea);
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            salvarLista(espacos);
            ultimaMensagem = "Espaço editado com sucesso!";
            System.out.println(ultimaMensagem); // <-- ADICIONADO PARA O SEU DOCUMENTO
            return true;
        } else {
            ultimaMensagem = "O ID do espaço não foi encontrado.";
            System.out.println(ultimaMensagem); // <-- ADICIONADO PARA O SEU DOCUMENTO
            return false;
        }
    }
}