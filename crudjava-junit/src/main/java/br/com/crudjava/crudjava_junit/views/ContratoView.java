package br.com.crudjava.crudjava_junit.views;
import br.com.crudjava.crudjava_junit.TelaInicial;
import br.com.crudjava.crudjava_junit.alerts.Alerts;
import br.com.crudjava.crudjava_junit.models.Boleto;
import br.com.crudjava.crudjava_junit.models.Locatario;
import br.com.crudjava.crudjava_junit.persistencias.ArquivoBoleto;
import br.com.crudjava.crudjava_junit.persistencias.ArquivoLocatario;
import br.com.crudjava.crudjava_junit.views.LocatarioView;

import br.com.crudjava.crudjava_junit.models.Contrato;
import br.com.crudjava.crudjava_junit.persistencias.ArquivoContrato;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static javafx.collections.FXCollections.observableArrayList;

public class ContratoView {

    private Stage stage;
    private ObservableList<Contrato> contratosObservable = observableArrayList();

    // Armazena a lista de locatários para evitar recarregá-la
    private ArrayList<Locatario> locatarios;

    public ContratoView(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        criarUI();
        this.stage.show();
    }

    private void criarUI() {
        stage.setTitle("Gestão de Contratos");

        ArrayList<Contrato> contratos = ArquivoContrato.lerLista();
        // Carrega os locatários uma vez
        locatarios = ArquivoLocatario.lerLista();
        contratosObservable.setAll(contratos);

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-padding: 10;");

        HBox navBar = criarMenuNavegacao();
        borderPane.setTop(navBar);

        VBox painelFormulario = new VBox(10);
        painelFormulario.setStyle("-fx-padding: 10;");
        painelFormulario.setPrefWidth(250);

        // Campos do formulário
        Label labelNomeEmpresa = new Label("Nome de Locatário");
        ComboBox<String> locatarioComboBox = new ComboBox<>();
        locatarioComboBox.setPromptText("Selecione a empresa");
        Set<String> cnpjComContratos = new HashSet<>();
        for (Contrato c : contratos){

            // --- CORREÇÃO DE TYPO 1 ---
            // Era: c.getlocatario()
            cnpjComContratos.add(c.getLocatario().getLocatarioCnpj());
        }
        for (Locatario l : locatarios){
            if (!cnpjComContratos.contains(l.getLocatarioCnpj())){
                locatarioComboBox.getItems().add(l.getLocatarioNome());
            }

        }

        Label labelDataInicio = new Label("Data de Início");
        DatePicker datePickerInicio = new DatePicker(LocalDate.now());
        datePickerInicio.setEditable(false);

        Label labelValorMensal = new Label("Valor Mensal");
        TextField txtValorMensal = new TextField();
        txtValorMensal.setPromptText("Ex: 1500.50");
        adicionarFiltroApenasNumeros(txtValorMensal);


        Label labelStatus = new Label("Status do Contrato");
        CheckBox checkStatus = new CheckBox("Ativo");
        checkStatus.setSelected(true);

        Button btnCadastrar = new Button("Cadastrar Contrato");
        btnCadastrar.setMaxWidth(Double.MAX_VALUE);

        Button btnAtualizar = new Button("Atualizar página");
        btnAtualizar.setMaxWidth(Double.MAX_VALUE);
        btnAtualizar.setOnAction(e -> {
            contratosObservable.setAll(ArquivoContrato.lerLista());
            // Recarrega a lista de locatários disponíveis no ComboBox
            recarregarComboBoxLocatarios(locatarioComboBox);
        });

        painelFormulario.getChildren().addAll(
                labelNomeEmpresa, locatarioComboBox,
                labelDataInicio, datePickerInicio,
                labelValorMensal, txtValorMensal,
                labelStatus, checkStatus,
                btnCadastrar, btnAtualizar
        );
        borderPane.setLeft(painelFormulario);

        VBox painelTabela = new VBox(10);
        painelTabela.setStyle("-fx-padding: 10;");

        TableView<Contrato> contratoTable = criarTabelaContratos();

        Button btnRemover = new Button("Remover Selecionado");
        Button btnVerBoletos = new Button("Ver boletos");
        painelTabela.getChildren().addAll(contratoTable, btnRemover,
                btnVerBoletos);
        borderPane.setCenter(painelTabela);


        // --- BLOCO btnCadastrar TOTALMENTE REFEITO ---
        btnCadastrar.setOnAction(e -> {
            // 1. Coletar dados brutos da View
            String nomeEmpresa = locatarioComboBox.getValue();
            LocalDate dataInicio = datePickerInicio.getValue();
            String valorTexto = txtValorMensal.getText(); // Passa o texto puro
            boolean status = checkStatus.isSelected();

            // 2. Encontrar o objeto Locatario (necessário para o método)
            Locatario empresa = null;
            if (nomeEmpresa != null) {
                for (Locatario l : locatarios){ // Usa a lista de locatários já carregada
                    if (nomeEmpresa.equals(l.getLocatarioNome())){
                        empresa = l;
                        break;
                    }
                }
            }

            // 3. Chamar o NOVO método de persistência (que faz a validação)
            boolean sucesso = ArquivoContrato.adicionarContrato(
                    empresa,       // O objeto Locatario
                    dataInicio,    // O LocalDate
                    valorTexto,    // A String do valor
                    status         // O boolean
            );

            // 4. Tratar o resultado
            if (sucesso) {
                Alerts.alertInfo("Sucesso", ArquivoContrato.getUltimaMensagem());

                // --- Lógica de Geração de Boleto (mantida do seu original) ---
                // Precisamos encontrar o contrato que acabamos de salvar
                ArrayList<Contrato> listaAtualizada = ArquivoContrato.lerLista();
                Contrato contratoSalvo = null;
                for(Contrato c : listaAtualizada) {
                    if (c.getLocatario().getLocatarioCnpj().equals(empresa.getLocatarioCnpj())) {
                        contratoSalvo = c;
                        break;
                    }
                }

                if (contratoSalvo != null) {
                    String linhaDig = "1000000000000";
                    BigInteger linhaDigNum = new BigInteger(linhaDig);

                    for (int i = 1; i <= 12; i++) {
                        LocalDate vencimento = dataInicio.plusMonths(i);
                        linhaDigNum = linhaDigNum.add(BigInteger.ONE);
                        linhaDig = linhaDigNum.toString();
                        ArquivoBoleto.adicionarBoleto(new Boleto(3000, vencimento,
                                "Tijucas Open", "Banco do Brasil", linhaDig,
                                contratoSalvo), contratoSalvo.getContratoId());
                    }
                    ArrayList<Boleto> boletos = ArquivoBoleto.lerLista(contratoSalvo.getContratoId());
                    contratoSalvo.setBoletos(boletos);
                    ArquivoContrato.atualizarContrato(contratoSalvo);
                }
                // --- Fim da Lógica de Boleto ---

                // Limpar e Atualizar a UI
                contratosObservable.setAll(ArquivoContrato.lerLista());
                recarregarComboBoxLocatarios(locatarioComboBox); // Atualiza o combo
                locatarioComboBox.setValue(null);
                datePickerInicio.setValue(LocalDate.now());
                txtValorMensal.clear();
                checkStatus.setSelected(true);

            } else {
                // Se falhou, a camada de persistência nos diz o porquê
                Alerts.alertError("Erro de Validação", ArquivoContrato.getUltimaMensagem());
            }
        });

        btnRemover.setOnAction(e -> {
            Contrato contratoSelecionado = contratoTable.getSelectionModel().getSelectedItem();
            if (contratoSelecionado == null) {
                Alerts.alertWarning("Nenhuma Seleção", "Por favor, selecione um " +
                        "contrato na " +
                        "tabela para remover.");
                return;
            }

            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja remover o contrato selecionado?", ButtonType.YES, ButtonType.NO);
            confirmacao.showAndWait().ifPresent(resposta -> {
                if (resposta == ButtonType.YES) {
                    ArquivoContrato.removerContrato(
                            contratoSelecionado.getContratoId());
                    contratosObservable.remove(contratoSelecionado);

                    // Atualiza o ComboBox caso o locatário fique livre
                    recarregarComboBoxLocatarios(locatarioComboBox);

                    exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Contrato removido com sucesso!");
                }
            });
        });

        btnVerBoletos.setOnAction(e -> {
            try {
                Contrato contratoSelecionado =
                        contratoTable.getSelectionModel().getSelectedItem();
                if (contratoSelecionado != null) {
                    new BoletoView(stage, contratoSelecionado).mostrar();
                } else {
                    Alerts.alertWarning("Nenhuma seleção",
                            "Por favor, selecione um " +
                                    "contrato na " +
                                    "tabela para ver boletos.");
                }
            } catch (NullPointerException ex){
                Alerts.alertError("Erro",
                        "Nenhum contrato selecionado. Erro: " + ex.getMessage());
            }
        });

        Scene cena = new Scene(borderPane, 900, 600);
        this.stage.setScene(cena);
    }

    private TableView<Contrato> criarTabelaContratos() {
        TableView<Contrato> table = new TableView<>();
        table.setItems(contratosObservable);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Contrato, String> colNomeEmpresa = new TableColumn<>("Nome Empresa");

        // --- CORREÇÃO DE TYPO 2 ---
        // Era: cell.getValue().getlocatario()
        colNomeEmpresa.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLocatario().getLocatarioNome()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        TableColumn<Contrato, String> colDataInicio = new TableColumn<>("Data de Início");
        colDataInicio.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDataInicio().format(formatter)));

        TableColumn<Contrato, String> colValor = new TableColumn<>("Valor Mensal");
        colValor.setCellValueFactory(cell -> new SimpleStringProperty(String.format("R$ %.2f", cell.getValue().getValorMensal())));

        TableColumn<Contrato, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isAtivo() ? "Ativo" : "Inativo"));

        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle(item.equals("Ativo") ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                }
            }
        });


        table.getColumns().addAll(colNomeEmpresa, colDataInicio, colValor, colStatus);
        return table;
    }

    private HBox criarMenuNavegacao() {
        HBox navBar = new HBox(15);
        navBar.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: lightgrey;");
        String styleBtn = "-fx-background-color: transparent; -fx-font-weight: bold;";

        Button btnHome = new Button("Home");
        btnHome.setStyle(styleBtn);
        btnHome.setOnAction(e -> new TelaInicial(stage).mostrar());

        Button btnLocatarios = new Button("Locatários");
        btnLocatarios.setStyle(styleBtn);
        btnLocatarios.setOnAction(e -> new LocatarioView(stage).mostrar());

        Button btnContratos = new Button("Contratos");
        btnContratos.setStyle(styleBtn);
        btnContratos.setOnAction(e -> this.mostrar()); // Recarrega a tela atual

        Button btnLojas = new Button("Lojas");
        btnLojas.setStyle(styleBtn);
        btnLojas.setOnAction(e -> new LojaView(stage).mostrar());


        Button btnEspacos = new Button("Espaços");
        btnEspacos.setStyle(styleBtn);
        btnEspacos.setOnAction(e -> new EspacoView(stage).mostrar());

        navBar.getChildren().addAll(btnHome, btnLocatarios, btnContratos, btnLojas, btnEspacos);
        return navBar;
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String conteudo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(conteudo);
        alerta.showAndWait();
    }

    private void adicionarFiltroApenasNumeros(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d*(\\.\\d{0,2}|,\\d{0,2})?$")) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Método auxiliar para recarregar os locatários no ComboBox
     */
    private void recarregarComboBoxLocatarios(ComboBox<String> locatarioComboBox) {
        locatarioComboBox.getItems().clear();

        ArrayList<Contrato> contratos = ArquivoContrato.lerLista();
        locatarios = ArquivoLocatario.lerLista(); // Recarrega a lista de locatários

        Set<String> cnpjComContratos = new HashSet<>();
        for (Contrato c : contratos){
            if (c.isAtivo()) { // Considera apenas contratos ativos
                cnpjComContratos.add(c.getLocatario().getLocatarioCnpj());
            }
        }
        for (Locatario l : locatarios){
            if (!cnpjComContratos.contains(l.getLocatarioCnpj())){
                locatarioComboBox.getItems().add(l.getLocatarioNome());
            }
        }
    }
}