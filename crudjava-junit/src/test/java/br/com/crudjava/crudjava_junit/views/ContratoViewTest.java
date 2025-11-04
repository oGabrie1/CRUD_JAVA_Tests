package br.com.crudjava.crudjava_junit.views;

import br.com.crudjava.crudjava_junit.TelaInicial;
import br.com.crudjava.crudjava_junit.alerts.Alerts;
import br.com.crudjava.crudjava_junit.models.Boleto;
import br.com.crudjava.crudjava_junit.models.Contrato;
import br.com.crudjava.crudjava_junit.models.Locatario;
import br.com.crudjava.crudjava_junit.persistencias.ArquivoBoleto;
import br.com.crudjava.crudjava_junit.persistencias.ArquivoContrato;
import br.com.crudjava.crudjava_junit.persistencias.ArquivoLocatario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static javafx.collections.FXCollections.observableArrayList;

public class ContratoViewTest {

}