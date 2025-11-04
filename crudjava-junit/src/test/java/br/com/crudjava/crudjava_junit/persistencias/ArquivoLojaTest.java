package br.com.crudjava.crudjava_junit.persistencias;
import br.com.crudjava.crudjava_junit.models.Loja;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ArquivoLojaTest {

    @BeforeEach
    void limparArquivo() {
        File arquivo = new File("lojas.dat");
        if (arquivo.exists()) arquivo.delete();
    }

   

    // CT01 - CT05: Cadastros válidos
    @Test void CT01_cadastroValido_Renner() {
        Loja loja = new Loja("Renner", "41991234567", "Roupas");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Loja cadastrada com sucesso!", resultado);
    }

    @Test void CT01_cadastroValido_Prada() {
        Loja loja = new Loja("Prada", "41991234567", "Jóias");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Loja cadastrada com sucesso!", resultado);
    }

    @Test void CT01_cadastroValido_Mizuno() {
        Loja loja = new Loja("Mizuno", "41991234567", "Esportes");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Loja cadastrada com sucesso!", resultado);
    }

    @Test void CT01_cadastroValido_McDonalds() {
        Loja loja = new Loja("McDonalds", "41991234567", "Restaurantes");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Loja cadastrada com sucesso!", resultado);
    }

    @Test void CT01_cadastroValido_Livrarias() {
        Loja loja = new Loja("Livrarias Curitiba", "41991234567", "Livros");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Loja cadastrada com sucesso!", resultado);
    }

    // CT02 - CT09: Cadastros inválidos
    @Test void CT02_nomeVazio() {
        Loja loja = new Loja("", "41991234567", "Roupas");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("O nome da loja não pode ser vazio.", resultado);
    }

    @Test void CT03_nomeDuplicado() {
        Loja loja1 = new Loja("Renner", "41991234567", "Roupas");
        ArquivoLoja.adicionarLoja(loja1);
        Loja loja2 = new Loja("Renner", "41999887766", "Esportes");
        String resultado = ArquivoLoja.adicionarLoja(loja2);
        System.out.println(resultado);
        assertEquals("Já existe uma loja com mesmo nome ou telefone.", resultado);
    }

    @Test void CT04_telefoneCurto() {
        Loja loja = new Loja("CoxaStore", "4199123456", "Roupas");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Preencha todos os campos corretamente!", resultado);
    }

    @Test void CT05_telefoneLongo() {
        Loja loja = new Loja("CoxaStore", "419912345678", "Roupas");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Preencha todos os campos corretamente!", resultado);
    }

    @Test void CT06_telefoneComLetras() {
        Loja loja = new Loja("CoxaStore", "342374@!", "Roupas");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Preencha todos os campos corretamente!", resultado);
    }

    @Test void CT07_telefoneVazio() {
        Loja loja = new Loja("CoxaStore", "", "Roupas");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Preencha todos os campos corretamente!", resultado);
    }

    @Test void CT08_telefoneDuplicado() {
        Loja loja1 = new Loja("CoxaStore", "41991234567", "Roupas");
        ArquivoLoja.adicionarLoja(loja1);
        Loja loja2 = new Loja("OutraLoja", "41991234567", "Roupas");
        String resultado = ArquivoLoja.adicionarLoja(loja2);
        System.out.println(resultado);
        assertEquals("Já existe uma loja com mesmo nome ou telefone.", resultado);
    }

    @Test void CT09_tipoNaoSelecionado() {
        Loja loja = new Loja("CoxaStore", "41991234567", "");
        String resultado = ArquivoLoja.adicionarLoja(loja);
        System.out.println(resultado);
        assertEquals("Preencha todos os campos corretamente!", resultado);
    }

    
    // EDIÇÃO DE LOJAS
    

    @Test void CT10_edicaoCompletaValida() {
        Loja loja = new Loja("CoxaStore", "41991234567", "Roupas");
        ArquivoLoja.adicionarLoja(loja);
        String resultado = ArquivoLoja.editarLoja("CoxaStore", "CoxaStoreOficial", "41999887766", "Esportes");
        System.out.println(resultado);
        assertEquals("Loja editada com sucesso!", resultado);
    }

    @Test void CT11_editarSemSelecionar() {
        String resultado = ArquivoLoja.editarLoja("NaoExiste", "NovoNome", "41991234567", "Roupas");
        System.out.println(resultado);
        assertEquals("Selecione uma loja para editar.", resultado);
    }

    @Test void CT12_editarMaisDeUm() {
        Loja loja1 = new Loja("L1", "41991234567", "Roupas");
        Loja loja2 = new Loja("L2", "41999887766", "Esportes");
        ArquivoLoja.adicionarLoja(loja1);
        ArquivoLoja.adicionarLoja(loja2);
        // No código ArquivoLoja atual ele sempre edita o primeiro selecionado
        String resultado = ArquivoLoja.editarLoja("L2", "L2_Edit", "41999887766", "Esportes");
        System.out.println(resultado);
        assertEquals("Loja editada com sucesso!", resultado);
    }

    @Test void CT13_editarNomeVazio() {
        Loja loja = new Loja("CoxaStore", "41991234567", "Roupas");
        ArquivoLoja.adicionarLoja(loja);
        String resultado = ArquivoLoja.editarLoja("CoxaStore", "", "41991234567", "Roupas");
        System.out.println(resultado);
        assertEquals("Preencha todos os campos corretamente!", resultado);
    }

    @Test void CT14_editarNomeDuplicado() {
        Loja loja1 = new Loja("CoxaStore", "41991234567", "Roupas");
        Loja loja2 = new Loja("OutraLoja", "41999887766", "Esportes");
        ArquivoLoja.adicionarLoja(loja1);
        ArquivoLoja.adicionarLoja(loja2);
        String resultado = ArquivoLoja.editarLoja("OutraLoja", "CoxaStore", "41999887766", "Esportes");
        System.out.println(resultado);
        assertEquals("Já existe outra loja com esse nome. A edição não foi salva.", resultado);
    }

    @Test void CT15_editarTelefoneVazio() {
        Loja loja = new Loja("CoxaStore", "41991234567", "Roupas");
        ArquivoLoja.adicionarLoja(loja);
        String resultado = ArquivoLoja.editarLoja("CoxaStore", "CoxaStore", "", "Roupas");
        System.out.println(resultado);
        assertEquals("Preencha todos os campos corretamente! O telefone deve estar completo (ex: (XX) XXXXX-XXXX).", resultado);
    }

    @Test void CT16_editarTelefoneDuplicado() {
        Loja loja1 = new Loja("CoxaStore", "41991234567", "Roupas");
        Loja loja2 = new Loja("OutraLoja", "41999887766", "Esportes");
        ArquivoLoja.adicionarLoja(loja1);
        ArquivoLoja.adicionarLoja(loja2);
        String resultado = ArquivoLoja.editarLoja("OutraLoja", "OutraLoja", "41991234567", "Esportes");
        System.out.println(resultado);
        assertEquals("Já existe outra loja com o telefone digitado. A edição não foi salva.", resultado);
    }

    @Test void CT17_editarTipoNaoSelecionado() {
        Loja loja = new Loja("CoxaStore", "41991234567", "Roupas");
        ArquivoLoja.adicionarLoja(loja);
        String resultado = ArquivoLoja.editarLoja("CoxaStore", "CoxaStore", "41999887766", "");
        System.out.println(resultado);
        assertEquals("Preencha todos os campos corretamente!", resultado);
    }

    
    // REMOÇÃO DE LOJAS
    

    @Test void CT18_removerLojaValida() {
        Loja loja = new Loja("CoxaStore", "41991234567", "Roupas");
        ArquivoLoja.adicionarLoja(loja);
        String resultado = ArquivoLoja.removerLoja("CoxaStore");
        System.out.println(resultado);
        assertEquals("Loja removida com sucesso!", resultado);
    }

    @Test void CT19_removerSemSelecionar() {
        String resultado = ArquivoLoja.removerLoja("VascoStore");
        System.out.println(resultado);
        assertEquals("Erro ao remover loja.", resultado);
    }

    @Test void CT20_removerMaisDeUmaSelecionada() {
        Loja loja1 = new Loja("L1", "41991234567", "Roupas");
        Loja loja2 = new Loja("L2", "41999887766", "Esportes");
        ArquivoLoja.adicionarLoja(loja1);
        ArquivoLoja.adicionarLoja(loja2);
        String resultado = ArquivoLoja.removerLoja("L2");
        System.out.println(resultado);
        assertEquals("Loja removida com sucesso!", resultado);
    }
}
