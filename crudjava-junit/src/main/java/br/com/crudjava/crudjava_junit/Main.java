package br.com.crudjava.crudjava_junit;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        TelaInicial telaInicial = new TelaInicial(stage);
        telaInicial.mostrar();
    }
}
