package br.com.crudjava.crudjava_junit.persistencias;
import br.com.crudjava.crudjava_junit.utils.ValidacaoContrato;

import br.com.crudjava.crudjava_junit.models.Locatario;
import br.com.crudjava.crudjava_junit.models.Boleto;
import br.com.crudjava.crudjava_junit.models.Contrato;

import java.time.LocalDate;
import java.io.*;
import java.util.ArrayList;

public class ArquivoContrato {
    private static final String CAMINHO_ARQUIVO = "contratos.dat";
    private static final String PROXIMO_ID_CAMINHO = "proximoId_contrato.dat";

    // Adicionado para testes JUnit
    private static String ultimaMensagem;

    public static String getUltimaMensagem() {
        return ultimaMensagem;
    }

    // Alterado de private para package-private (sem modificador) para testes
    static int lerProximoId(){
        try {
            File arquivoId = new File(PROXIMO_ID_CAMINHO);
            if (arquivoId.exists()){
                DataInputStream dis =
                        new DataInputStream(new FileInputStream(arquivoId));
                return dis.readInt();
            }
            return 1;
        } catch (IOException e){
            System.err.println("Erro ao ler " + PROXIMO_ID_CAMINHO + ", " +
                    "reiniciando contador. Erro: " + e.getMessage());
            return 1;
        }
    }

    // Alterado de private para package-private (sem modificador) para testes
    static void salvarProximoId(int id){
        try {
            File arquivoId = new File(PROXIMO_ID_CAMINHO);
            if (!arquivoId.exists()){
                arquivoId.createNewFile();
            }
            DataOutputStream dos =
                    new DataOutputStream(new FileOutputStream(arquivoId));
            dos.writeInt(id);
        } catch (IOException e){
            System.err.println("Erro ao salvar em " + PROXIMO_ID_CAMINHO + "." +
                    " Erro: " + e.getMessage());
        }
    }

    // Alterado de public para package-private (sem modificador) para testes
    static void salvarLista(ArrayList<Contrato> contratos){
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (!arquivo.exists()){
                arquivo.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo));
            oos.writeObject(contratos);
            oos.close();
        } catch (FileNotFoundException e){
            System.err.println("Erro ao salvar lista de contratos: " + e.getMessage());
        } catch (IOException e){
            System.err.println("Erro ao salvar lista de contratos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Contrato> lerLista(){
        ArrayList<Contrato> lista = new ArrayList<>();
        try {
            File arquivo = new File(CAMINHO_ARQUIVO);
            if (arquivo.exists() && arquivo.length() > 0) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CAMINHO_ARQUIVO));
                lista = (ArrayList<Contrato>) ois.readObject();
                ois.close();
            }
        } catch (EOFException e) {
            System.err.println("Arquivo de contratos vazio ou corrompido. Iniciando com lista vazia.");
        } catch (ClassNotFoundException e){
            System.err.println("Erro ao ler lista de contratos: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao ler lista de contratos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método modificado para aceitar dados brutos da View, realizar validação
     * e retornar um status de sucesso/falha para testes.
     */
    public static boolean adicionarContrato(
            Locatario locatario,
            LocalDate dataInicio,
            String valorMensalStr,
            boolean status)
    {
        // --- 1. VALIDAÇÃO DE FRONT-END (UTILS) ---
        if (!ValidacaoContrato.validarLocatario(locatario)) {
            ultimaMensagem = "Selecione um locatário para continuar"; // CT02
            return false;
        }
        if (!ValidacaoContrato.validarDataInicio(dataInicio)) {
            if (dataInicio == null) {
                ultimaMensagem = "Informe uma data de início válida."; // CT03
            } else {
                ultimaMensagem = "A data de início não pode ser anterior à data atual."; // Limite
            }
            return false;
        }
        if (!ValidacaoContrato.validarValorMensal(valorMensalStr)) {
            if (valorMensalStr == null || valorMensalStr.trim().isEmpty()) {
                ultimaMensagem = "Informe um valor mensal válido."; // CT04
            } else {
                try {
                    double val = Double.parseDouble(valorMensalStr.trim().replace(",", "."));
                    if (val <= 0) {
                        ultimaMensagem = "O valor mensal deve ser maior que zero"; // CT05
                    } else {
                        ultimaMensagem = "Informe um valor mensal válido."; // Genérico
                    }
                } catch (NumberFormatException e) {
                    ultimaMensagem = "Informe um valor mensal válido."; // CT06
                }
            }
            return false;
        }

        // --- 2. VALIDAÇÃO DE REGRA DE NEGÓCIO ---
        ArrayList<Contrato> contratos = lerLista();
        String cnpjLocatario = locatario.getLocatarioCnpj();
        for (Contrato c : contratos) {
            // Regra: Não pode adicionar um contrato ATIVO se o locatário já tem um ATIVO.
            if (c.getLocatario().getLocatarioCnpj().equals(cnpjLocatario) && c.isAtivo() && status) {
                ultimaMensagem = "Contrato já cadastrado para este locatário."; // CT08
                return false;
            }
        }

        // --- 3. SUCESSO - PERSISTÊNCIA ---
        double valorMensal = Double.parseDouble(valorMensalStr.trim().replace(",", "."));
        int novoId = lerProximoId();

        // Usamos o construtor do seu modelo
        Contrato novoContrato = new Contrato(locatario, dataInicio, valorMensal, status);
        novoContrato.setContratoId(novoId); // O ID é setado após a criação.

        contratos.add(novoContrato);
        salvarLista(contratos);
        salvarProximoId(novoId + 1);

        ultimaMensagem = "Contrato cadastrado com sucesso!"; // CT01
        return true;
    }

    /**
     * Método modificado para retornar boolean e mensagem para testes.
     * Também foi corrigido um bug de ConcurrentModificationException.
     */
    public static boolean removerContrato(int contratoId){
        ArrayList<Contrato> contratos = lerLista();

        // A forma correta de remover de uma lista (evita ConcurrentModificationException)
        boolean removido = contratos.removeIf(c -> c.getContratoId() == contratoId);

        if (removido){
            salvarLista(contratos);
            ultimaMensagem = "Contrato removido com sucesso."; // CT09
            return true;
        } else {
            // CT11 (implícito)
            ultimaMensagem = "Contrato não encontrado. Nenhuma remoção foi feita";
            return false;
        }
    }

    public static void atualizarContrato(Contrato contratoAtualizado) {
        ArrayList<Contrato> contratos = lerLista();
        for (int i = 0; i < contratos.size(); i++) {
            if (contratos.get(i).getContratoId() == contratoAtualizado.getContratoId()) {
                contratos.set(i, contratoAtualizado);
                break;
            }
        }
        salvarLista(contratos);
    }

}