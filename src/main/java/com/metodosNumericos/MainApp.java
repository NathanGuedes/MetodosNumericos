package com.metodosNumericos;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.metodosNumericos.telas.BissecaoScreen;

public class MainApp extends Application {

    // Cores atualizadas com melhor contraste
    private static final String COR_FUNDO = "#1E1E1E";
    private static final String COR_TEXTO = "#E0E0E0";
    private static final String COR_PRIMARIA = "#4CAF50";
    private static final String COR_SECUNDARIA = "#2196F3";

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(25);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        Text titulo = new Text("Métodos Numéricos");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        titulo.setFill(javafx.scene.paint.Color.web(COR_PRIMARIA));

        Text subtitulo = new Text("Selecione um método para encontrar raízes");
        subtitulo.setFont(Font.font("Segoe UI", 16));
        subtitulo.setFill(javafx.scene.paint.Color.web(COR_TEXTO));

        VBox botoesBox = new VBox(15);
        botoesBox.setAlignment(Pos.CENTER);

        Button btnBissecao = criarBotao("Método da Bisseção", COR_PRIMARIA, "#3F8341");
        btnBissecao.setOnAction(e -> abrirTela(new BissecaoScreen(), "Bisseção"));

        botoesBox.getChildren().add(btnBissecao);

        root.getChildren().addAll(titulo, subtitulo, botoesBox);

        Scene scene = new Scene(root, 900, 650);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.setTitle("Solução Numérica");
        primaryStage.show();
    }

    private Button criarBotao(String texto, String cor, String cor2) {
        Button btn = new Button(texto);

        // Estilos principais
        String estiloPadrao = "-fx-background-color: " + cor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 12 12;" +
                "-fx-max-width: 200px;" +
                "-fx-max-height: 40px;" +
                "-fx-background-radius: 4;";

        String estiloHover = "-fx-background-color: " + cor2 + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 12 12;" +
                "-fx-max-width: 200px;" +
                "-fx-max-height: 40px;" +
                "-fx-background-radius: 4;";

        btn.setStyle(estiloPadrao);

        // Efeito hover sem perder os estilos principais
        btn.setOnMouseEntered(e -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(e -> btn.setStyle(estiloPadrao));

        return btn;
    }

    // Função para escurecer a cor corretamente
    private String escurecerCor(String corHex) {
        Color cor = Color.web(corHex);
        double fator = 0.8; // Reduz a luminosidade em 20%
        Color corEscurecida = new Color(
                cor.getRed() * fator,
                cor.getGreen() * fator,
                cor.getBlue() * fator,
                cor.getOpacity()
        );

        return String.format("#%02X%02X%02X",
                (int) (corEscurecida.getRed() * 255),
                (int) (corEscurecida.getGreen() * 255),
                (int) (corEscurecida.getBlue() * 255));
    }

    private void abrirTela(Object controller, String titulo) {
        try {
            Stage stage = new Stage();
            stage.setTitle(titulo);
            Scene scene = new Scene((Parent) controller, 1100, 750);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}