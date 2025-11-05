package br.com.crudjava.crudjava_junit.views;

import br.com.crudjava.crudjava_junit.alerts.Alerts;
import br.com.crudjava.crudjava_junit.models.Espaco;
import br.com.crudjava.crudjava_junit.persistencias.ArquivoEspaco;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ModalEspacoEdit {

    private Stage stage;
    private Espaco espaco;

    public ModalEspacoEdit(Stage stageOwner, Espaco espaco) {
        this.stage = new Stage();
        stage.initOwner(stageOwner);
        stage.initModality(Modality.WINDOW_MODAL);

        this.espaco = espaco;
    }

    public void mostrar() {
        criarUI();
        stage.showAndWait();
    }

    private void criarUI() {
        stage.setTitle("Editar Espaço - ID: " + espaco.getId());

        VBox painelCampos = new VBox(10);
        painelCampos.setStyle("-fx-padding: 20;");

        Label labelId = new Label("ID do Espaço");
        TextField txtId = new TextField(String.valueOf(espaco.getId()));
        txtId.setEditable(false);

        Label labelArea = new Label("Área do espaço (m²)");
        TextField txtArea = new TextField(String.format("%.2f", espaco.getArea()));
        txtArea.setPromptText("Digite a nova área");
        Pattern pattern = Pattern.compile("\\d*([,.]\\d{0,2})?");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        txtArea.setTextFormatter(formatter);


        Label labelPiso = new Label("Piso do espaço (1 ou 2)");
        TextField txtPiso = new TextField(String.valueOf(espaco.getPiso()));
        txtPiso.setPromptText("Digite o novo piso");

        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.setMaxWidth(Double.MAX_VALUE);
        btnSalvar.setOnAction(e -> {
            // 1. Pegamos o ID (que já temos) e os novos valores de texto puros
            int id = espaco.getId();
            String areaStr = txtArea.getText();
            String pisoStr = txtPiso.getText();

            // 2. Chamamos o método que aceita Strings e faz a validação interna
            boolean sucesso = ArquivoEspaco.editarEspaco(id, pisoStr, areaStr);

            // 3. Verificamos o resultado
            if (sucesso) {
                // Se deu certo, mostra a mensagem de sucesso e fecha o modal
                Alerts.alertInfo("Sucesso", ArquivoEspaco.getUltimaMensagem());
                this.stage.close();
            } else {
                // Se deu errado, a camada de persistência nos informa o porquê
                Alerts.alertError("Erro de Validação", ArquivoEspaco.getUltimaMensagem());
                // O modal continua aberto para o usuário corrigir
            }
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setMaxWidth(Double.MAX_VALUE);
        btnCancelar.setOnAction(e -> this.stage.close());

        painelCampos.getChildren().addAll(
                labelId, txtId,
                labelArea, txtArea,
                labelPiso, txtPiso,
                btnSalvar, btnCancelar
        );

        Scene cena = new Scene(painelCampos, 400, 350);
        this.stage.setScene(cena);
    }
}