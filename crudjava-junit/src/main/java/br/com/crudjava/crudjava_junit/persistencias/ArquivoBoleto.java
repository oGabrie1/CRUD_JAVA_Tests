package br.com.crudjava.crudjava_junit.persistencias;

import br.com.crudjava.crudjava_junit.models.Boleto;
import br.com.crudjava.crudjava_junit.models.Contrato;
import br.com.crudjava.crudjava_junit.utils.ValidacaoBoleto;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ArquivoBoleto {

    private static final String PROXIMO_NUM_CAMINHO = "proximoNum_Boleto.dat";
    private static String ultimaMensagem = "";

    private static int lerProximoNum() {
        try {
            File arquivoNumDoc = new File(PROXIMO_NUM_CAMINHO);
            if (arquivoNumDoc.exists()) {
                DataInputStream dis = new DataInputStream(new FileInputStream(arquivoNumDoc));
                return dis.readInt();
            }
            return 1;
        } catch (IOException e) {
            System.err.println("Erro ao ler " + PROXIMO_NUM_CAMINHO + ": " + e.getMessage());
            return 1;
        }
    }

    private static void salvarProximoNumDoc(int proximoNumDoc) {
        try {
            File arquivoNumDoc = new File(PROXIMO_NUM_CAMINHO);
            if (!arquivoNumDoc.exists()) {
                arquivoNumDoc.createNewFile();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(arquivoNumDoc));
            dos.writeInt(proximoNumDoc);
        } catch (IOException e) {
            System.err.println("Erro ao salvar número do boleto: " + e.getMessage());
        }
    }

    public static ArrayList<Boleto> lerLista(int contratoId) {
        ArrayList<Contrato> contratos = ArquivoContrato.lerLista();
        ArrayList<Boleto> boletosContrato = new ArrayList<>();

        for (Contrato c : contratos) {
            if (c.getContratoId() == contratoId) {
                boletosContrato = c.getBoletos();
                break;
            }
        }
        return boletosContrato;
    }

    public static boolean adicionarBoleto(Boleto novoBoleto, int contratoId) {
        String msgValor = ValidacaoBoleto.validarValor(String.valueOf(novoBoleto.getValor()));
        if (!msgValor.equals("Boleto cadastrado com sucesso!")) {
            ultimaMensagem = msgValor;
            System.out.println(ultimaMensagem);
            return false;
        }

        String msgData = ValidacaoBoleto.validarData(novoBoleto.getVencimento());
        if (!msgData.equals("Boleto cadastrado com sucesso!")) {
            ultimaMensagem = msgData;
            System.out.println(ultimaMensagem);
            return false;
        }

        if (novoBoleto.getCedente() == null ||
                novoBoleto.getCedente().trim().isEmpty() ||
                !novoBoleto.getCedente().equalsIgnoreCase("Tijucas Open")) {
            novoBoleto.setCedente("Tijucas Open");
        }

        if (novoBoleto.getBanco() == null ||
                novoBoleto.getBanco().trim().isEmpty() ||
                !novoBoleto.getBanco().equalsIgnoreCase("Banco do Brasil")) {
            novoBoleto.setBanco("Banco do Brasil");
        }

        String msgLinha = ValidacaoBoleto.validarLinhaDigitavel(novoBoleto.getLinhaDigitavel());
        if (!msgLinha.equals("Boleto cadastrado com sucesso!")) {
            ultimaMensagem = msgLinha;
            System.out.println(ultimaMensagem);
            return false;
        }

        if (novoBoleto.getLinhaDigitavel() != null) {
            String apenasNumeros = novoBoleto.getLinhaDigitavel().replaceAll("\\D", "");
            if (!apenasNumeros.isEmpty()) {
                try {
                    int numero = Integer.parseInt(apenasNumeros);
                    numero = Math.abs(numero);
                    novoBoleto.setLinhaDigitavel(String.valueOf(numero));
                } catch (NumberFormatException e) {
                    novoBoleto.setLinhaDigitavel("0");
                }
            } else {
                novoBoleto.setLinhaDigitavel("0");
            }
        }


        ArrayList<Contrato> contratos = ArquivoContrato.lerLista();
        int novoNumDoc = lerProximoNum();

        for (Contrato c : contratos) {
            if (c.getContratoId() == contratoId) {
                for (Boleto b : c.getBoletos()) {
                    if (novoBoleto.getNumeroDocumento() == b.getNumeroDocumento()) {
                        ultimaMensagem = "Boleto já existente!";
                        System.out.println(ultimaMensagem);
                        return false;
                    }
                }

                novoBoleto.setNumeroDocumento(novoNumDoc);
                c.getBoletos().add(novoBoleto);
                salvarProximoNumDoc(novoNumDoc + 1);
                break;
            }
        }

        ArquivoContrato.salvarLista(contratos);
        ultimaMensagem = "Boleto cadastrado com sucesso!";
        System.out.println(ultimaMensagem);
        return true;
    }


    public static void editarBoleto(int numeroDocumento, double valor, LocalDate vencimento, String cedente, String banco, String linhaDigitavel, int contratoId) {
        ArrayList<Contrato> contratos = ArquivoContrato.lerLista();

        for (Contrato c : contratos) {
            for (Boleto b : c.getBoletos()) {
                if (b.getNumeroDocumento() == numeroDocumento) {

                    String msgValor = ValidacaoBoleto.validarValor(String.valueOf(valor));
                    if (!msgValor.equals("Boleto cadastrado com sucesso!")) {
                        ultimaMensagem = msgValor;
                        System.out.println(ultimaMensagem);
                        return;
                    }

                    String msgData = ValidacaoBoleto.validarData(vencimento);
                    if (!msgData.equals("Boleto cadastrado com sucesso!")) {
                        ultimaMensagem = msgData;
                        System.out.println(ultimaMensagem);
                        return;
                    }

                    String msgLinha = ValidacaoBoleto.validarLinhaDigitavel(linhaDigitavel);
                    if (!msgLinha.equals("Boleto cadastrado com sucesso!")) {
                        ultimaMensagem = msgLinha;
                        System.out.println(ultimaMensagem);
                        return;
                    }

                    b.setCedente("Tijucas Open");
                    b.setBanco("Banco do Brasil");

                    b.setValor(valor);
                    b.setVencimento(vencimento);
                    b.setLinhaDigitavel(linhaDigitavel.replaceAll("\\D", ""));

                    ArquivoContrato.salvarLista(contratos);
                    ultimaMensagem = "Boleto editado com sucesso!";
                    System.out.println(ultimaMensagem);
                    return;
                }
            }
        }

        ultimaMensagem = "Selecione um boleto para editar";
        System.out.println(ultimaMensagem);
    }

    public static boolean removerBoleto(int numeroDocumento, int contratoId) {
        ArrayList<Contrato> contratos = ArquivoContrato.lerLista();

        if (numeroDocumento <= 0) {
            ultimaMensagem = "Selecione um boleto para apagar";
            System.out.println(ultimaMensagem);
            return false;
        }

        boolean removido = false;

        for (Contrato c : contratos) {
            if (c.getContratoId() == contratoId) {
                removido = c.getBoletos().removeIf(b -> b.getNumeroDocumento() == numeroDocumento);
                if (removido) {
                    ArquivoContrato.salvarLista(contratos);
                    ultimaMensagem = "Boleto removido com sucesso";
                    System.out.println(ultimaMensagem);
                    return true;
                }
            }
        }

        ultimaMensagem = "Erro ao remover boleto";
        System.out.println(ultimaMensagem);
        return false;
    }


    public static String getUltimaMensagem() {
        return ultimaMensagem;
    }
}
