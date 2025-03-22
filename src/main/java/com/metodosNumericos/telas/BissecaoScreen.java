package com.metodosNumericos.telas;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import com.metodosNumericos.metodos.MetodoBissecao;

public class BissecaoScreen extends BorderPane {

    // Cores com acessibilidade WCAG
    private static final String COR_FUNDO = "#1E1E1E";
    private static final String COR_CARD = "#2D2D2D";
    private static final String COR_TEXTO = "#E0E0E0";
    private static final String COR_PRIMARIA = "#4CAF50";
    private static final String COR_SECUNDARIA = "#2196F3";
    private static final String COR_ERRO = "#F44336";
    private static final String COR_BORDA = "#404040";

    private TextField txtEquacao, txtLimiteInferior, txtLimiteSuperior, txtErro, txtIteracoes;
    private VBox resultadosBox;
    private TableView<IteracaoRow> tabelaIteracoes;
    private Label lblResultado;
    private Button btnCalcular, btnLimpar;

    // Classe interna para representar cada linha da tabela de iterações
    public static class IteracaoRow {
        private final SimpleIntegerProperty iteracao;
        private final SimpleDoubleProperty valorX;
        private final SimpleDoubleProperty valorFx;
        private final SimpleDoubleProperty erro;

        public IteracaoRow(int iteracao, double valorX, double valorFx, double erro) {
            this.iteracao = new SimpleIntegerProperty(iteracao);
            this.valorX = new SimpleDoubleProperty(valorX);
            this.valorFx = new SimpleDoubleProperty(valorFx);
            this.erro = new SimpleDoubleProperty(erro);
        }

        public int getIteracao() { return iteracao.get(); }
        public double getValorX() { return valorX.get(); }
        public double getValorFx() { return valorFx.get(); }
        public double getErro() { return erro.get(); }
    }

    public BissecaoScreen() {
        inicializarComponentes();
        configurarEstilos();
        configurarLayout();
        configurarEventos();
    }

    private void inicializarComponentes() {
        txtEquacao = criarCampoTexto("Ex: x³ - x - 2");
        txtLimiteInferior = criarCampoTexto("Ex: 1");
        txtLimiteSuperior = criarCampoTexto("Ex: 2");
        txtErro = criarCampoTexto("Ex: 0.0001");
        txtIteracoes = criarCampoTexto("Ex: 100");

        // Inicializar componentes de resultado
        inicializarComponentesResultado();

        btnCalcular = criarBotao("Calcular Raiz", COR_PRIMARIA, "#3F8341");
        btnLimpar = criarBotao("Limpar Campos", COR_SECUNDARIA, "#1970B5");
    }

    private void inicializarComponentesResultado() {
        // Área de resultados
        resultadosBox = new VBox(20);
        resultadosBox.setAlignment(Pos.TOP_LEFT); // Alterado para alinhamento à esquerda

        // Label para mostrar a raiz e outras informações principais
        lblResultado = new Label();
        lblResultado.setStyle("-fx-text-fill: " + COR_TEXTO + "; -fx-font-size: 14px;");
        lblResultado.setWrapText(true);
        lblResultado.setAlignment(Pos.TOP_LEFT); // Alinhamento à esquerda

        // Tabela de iterações
        tabelaIteracoes = new TableView<>();
        tabelaIteracoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Removido o prefHeight para que a tabela se ajuste dinamicamente ao número de linhas
        tabelaIteracoes.setFixedCellSize(40); // Define altura fixa para cada linha

        // Adiciona a classe CSS para aplicar estilos externos
        tabelaIteracoes.getStyleClass().add("table-view");

        // Definir colunas da tabela
        TableColumn<IteracaoRow, Number> colIteracao = new TableColumn<>("Iteração");
        colIteracao.setCellValueFactory(cellData -> cellData.getValue().iteracao);
        colIteracao.setStyle("-fx-alignment: CENTER;");
        colIteracao.setPrefWidth(100);

        TableColumn<IteracaoRow, Number> colX = new TableColumn<>("X");
        colX.setCellValueFactory(cellData -> cellData.getValue().valorX);
        colX.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.10f", item.doubleValue()));
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });
        colX.setStyle("-fx-alignment: CENTER;");
        colX.setPrefWidth(150);

        TableColumn<IteracaoRow, Number> colFx = new TableColumn<>("F(X)");
        colFx.setCellValueFactory(cellData -> cellData.getValue().valorFx);
        colFx.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.10f", item.doubleValue()));
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });
        colFx.setStyle("-fx-alignment: CENTER;");
        colFx.setPrefWidth(150);

        TableColumn<IteracaoRow, Number> colErro = new TableColumn<>("Erro");
        colErro.setCellValueFactory(cellData -> cellData.getValue().erro);
        colErro.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.10f", item.doubleValue()));
                    // Colorir célula com base no valor do erro (quanto menor, mais verde)
                    double valor = item.doubleValue();
                    if (valor < 1e-8) {
                        setStyle("-fx-background-color: rgba(76, 175, 80, 0.3); -fx-alignment: CENTER;");
                    } else if (valor < 1e-6) {
                        setStyle("-fx-background-color: rgba(76, 175, 80, 0.2); -fx-alignment: CENTER;");
                    } else if (valor < 1e-4) {
                        setStyle("-fx-background-color: rgba(76, 175, 80, 0.1); -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });
        colErro.setStyle("-fx-alignment: CENTER;");
        colErro.setPrefWidth(150);

        tabelaIteracoes.getColumns().addAll(colIteracao, colX, colFx, colErro);

        // Adicionando componentes ao VBox de resultados com alinhamento à esquerda
        Label lblResultadosTitulo = new Label("Resultados:");
        lblResultadosTitulo.setTextFill(Color.web(COR_PRIMARIA));
        lblResultadosTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblResultadosTitulo.setAlignment(Pos.CENTER_LEFT); // Alinhamento à esquerda

        Label lblIteracoesTitulo = new Label("Iterações:");
        lblIteracoesTitulo.setTextFill(Color.web(COR_PRIMARIA));
        lblIteracoesTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblIteracoesTitulo.setAlignment(Pos.CENTER_LEFT); // Alinhamento à esquerda

        resultadosBox.getChildren().addAll(
                lblResultadosTitulo,
                lblResultado,
                lblIteracoesTitulo,
                tabelaIteracoes
        );
    }

    private TextField criarCampoTexto(String prompt) {
        TextField campo = new TextField();
        campo.setPromptText(prompt);
        campo.setStyle("-fx-background-color: " + COR_CARD + ";" +
                "-fx-text-fill: " + COR_TEXTO + ";" +
                "-fx-padding: 10;" +
                "-fx-background-radius: 4;" +
                "-fx-border-color: " + COR_BORDA + ";" +
                "-fx-border-radius: 4;");
        campo.setMaxWidth(Double.MAX_VALUE);
        return campo;
    }

    private Button criarBotao(String texto, String cor, String cor2) {
        Button btn = new Button(texto);

        // Estilos principais
        String estiloPadrao = "-fx-background-color: " + cor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 12 20;" +
                "-fx-background-radius: 4;";

        String estiloHover = "-fx-background-color: " + cor2 + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 12 20;" +
                "-fx-background-radius: 4;";

        btn.setStyle(estiloPadrao);
        btn.setMaxWidth(Double.MAX_VALUE);

        // Efeito hover sem perder os estilos principais
        btn.setOnMouseEntered(e -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(e -> btn.setStyle(estiloPadrao));

        return btn;
    }

    private void configurarEstilos() {
        this.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        // Estilo para os componentes de resultado
        resultadosBox.setStyle("-fx-background-color: " + COR_FUNDO + ";");

        // Aplicar estilos CSS do arquivo table-styles.css
        this.getStylesheets().add(getClass().getResource("/styles/table-styles.css").toExternalForm());

        // Estilo para a área de resultados - complementar aos estilos CSS externos
        tabelaIteracoes.setStyle(
                "-fx-background-color: " + COR_CARD + ";" +
                        "-fx-text-fill: " + COR_TEXTO + ";" +
                        "-fx-control-inner-background: " + COR_CARD + ";" +
                        "-fx-table-cell-border-color: " + COR_BORDA + ";"
        );
    }

    private void configurarLayout() {
        // Cabeçalho
        Text titulo = new Text("Método da Bisseção");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titulo.setFill(Color.web(COR_PRIMARIA));

        VBox header = new VBox(10, titulo);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));

        // Corpo Principal - Agora vertical
        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(10, 20, 20, 20));
        mainContent.setAlignment(Pos.TOP_LEFT); // Alinhamento à esquerda

        // Seções de entrada
        VBox entradaBox = new VBox(20);
        entradaBox.getChildren().addAll(
                criarGrupoEntrada("Equação", txtEquacao),
                criarGrupoIntervalos(),
                criarGrupoParametros()
        );
        entradaBox.setAlignment(Pos.TOP_LEFT); // Alinhamento à esquerda

        // Botões
        HBox botoesBox = new HBox(15);
        botoesBox.setAlignment(Pos.CENTER);
        botoesBox.setPadding(new Insets(10, 0, 20, 0));
        botoesBox.getChildren().addAll(btnCalcular, btnLimpar);
        HBox.setHgrow(btnCalcular, Priority.ALWAYS);
        HBox.setHgrow(btnLimpar, Priority.ALWAYS);

        // Construção final
        mainContent.getChildren().addAll(entradaBox, botoesBox, resultadosBox);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + COR_FUNDO + "; -fx-background-color: " + COR_FUNDO + ";");

        this.setTop(header);
        this.setCenter(scrollPane);
    }

    private VBox criarGrupoEntrada(String titulo, TextField campo) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.TOP_LEFT); // Alinhamento à esquerda

        Label label = new Label(titulo);
        label.setTextFill(Color.web(COR_TEXTO));
        label.setFont(Font.font("Segoe UI", 14));
        label.setAlignment(Pos.CENTER_LEFT); // Alinhamento à esquerda

        box.getChildren().addAll(label, campo);
        return box;
    }

    private VBox criarGrupoIntervalos() {
        VBox box = new VBox(20);
        box.setStyle("-fx-background-color: " + COR_CARD + ";" +
                "-fx-padding: 15;" +
                "-fx-background-radius: 6;");
        box.setAlignment(Pos.TOP_LEFT); // Alinhamento à esquerda

        HBox intervalosBox = new HBox(15);
        intervalosBox.setAlignment(Pos.CENTER_LEFT);
        intervalosBox.setFillHeight(true);
        intervalosBox.setPrefWidth(Double.MAX_VALUE);

        VBox limiteInferior = criarGrupoEntrada("Limite Inferior (a)", txtLimiteInferior);
        VBox limiteSuperior = criarGrupoEntrada("Limite Superior (b)", txtLimiteSuperior);

        // Permite que os elementos cresçam e ocupem o espaço disponível
        HBox.setHgrow(limiteInferior, Priority.ALWAYS);
        HBox.setHgrow(limiteSuperior, Priority.ALWAYS);

        intervalosBox.getChildren().addAll(limiteInferior, limiteSuperior);

        Label tituloLabel = new Label("Definição do Intervalo:");
        tituloLabel.setTextFill(Color.web(COR_TEXTO));
        tituloLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        tituloLabel.setAlignment(Pos.CENTER_LEFT); // Alinhamento à esquerda

        box.getChildren().addAll(tituloLabel, intervalosBox);

        return box;
    }

    private VBox criarGrupoParametros() {
        VBox box = new VBox(15);
        box.setStyle("-fx-background-color: " + COR_CARD + ";" +
                "-fx-padding: 15;" +
                "-fx-background-radius: 6;");
        box.setAlignment(Pos.TOP_LEFT); // Alinhamento à esquerda

        HBox parametrosBox = new HBox(15);
        parametrosBox.setAlignment(Pos.CENTER_LEFT);
        parametrosBox.setFillHeight(true);
        parametrosBox.setPrefWidth(Double.MAX_VALUE);

        VBox erroBox = criarGrupoEntrada("Erro Máximo", txtErro);
        VBox iteracoesBox = criarGrupoEntrada("Iterações Máximas", txtIteracoes);

        // Garante que os elementos preencham o espaço disponível
        HBox.setHgrow(erroBox, Priority.ALWAYS);
        HBox.setHgrow(iteracoesBox, Priority.ALWAYS);

        parametrosBox.getChildren().addAll(erroBox, iteracoesBox);

        Label tituloLabel = new Label("Critérios de Parada:");
        tituloLabel.setTextFill(Color.web(COR_TEXTO));
        tituloLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        tituloLabel.setAlignment(Pos.CENTER_LEFT); // Alinhamento à esquerda

        Label subTituloLabel = new Label("Preencha um ou ambos os campos abaixo:");
        subTituloLabel.setTextFill(Color.web(COR_TEXTO));
        subTituloLabel.setFont(Font.font("Segoe UI", 12));
        subTituloLabel.setAlignment(Pos.CENTER_LEFT); // Alinhamento à esquerda

        box.getChildren().addAll(tituloLabel, subTituloLabel, parametrosBox);

        return box;
    }

    private void configurarEventos() {
        // Eventos dos Botões
        btnCalcular.setOnAction(e -> executarCalculo());
        btnLimpar.setOnAction(e -> limparCampos());
    }

    // ============== Funcionalidades ==============
    private void executarCalculo() {
        try {
            validarCamposObrigatorios();

            String equacao = txtEquacao.getText();
            double a = Double.parseDouble(txtLimiteInferior.getText());
            double b = Double.parseDouble(txtLimiteSuperior.getText());

            if (a >= b) throw new Exception("Intervalo inválido: a deve ser menor que b");

            // Verificar quais campos estão preenchidos
            boolean temErro = !txtErro.getText().isEmpty();
            boolean temIteracoes = !txtIteracoes.getText().isEmpty();

            double erroMax = temErro ? Double.parseDouble(txtErro.getText()) : 1e-10;
            int iterMax = temIteracoes ? Integer.parseInt(txtIteracoes.getText()) : 1000;

            // Chamar o método com histórico
            MetodoBissecao.ResultadoBissecao resultado =
                    MetodoBissecao.calcularComHistorico(equacao, a, b, erroMax, iterMax);

            exibirResultadoDetalhado(equacao, a, b, resultado, temErro, temIteracoes);

        } catch (NumberFormatException e) {
            exibirErro("Formato numérico inválido!");
        } catch (Exception e) {
            exibirErro(e.getMessage());
        }
    }

    private void exibirResultadoDetalhado(String equacao, double a, double b,
                                          MetodoBissecao.ResultadoBissecao resultado,
                                          boolean temErro, boolean temIteracoes) {
        if (Double.isNaN(resultado.raiz)) {
            lblResultado.setText("Nenhuma raiz encontrada no intervalo [" + a + ", " + b + "]");
            lblResultado.setTextFill(Color.web(COR_ERRO));
            tabelaIteracoes.setItems(FXCollections.observableArrayList());
            return;
        }

        // Exibir informações principais do resultado
        StringBuilder resultadoInfo = new StringBuilder();
        resultadoInfo.append(String.format("Equação: %s\n", equacao));
        resultadoInfo.append(String.format("Intervalo: [%.6f, %.6f]\n", a, b));
        resultadoInfo.append(String.format("Raiz encontrada: %.10f\n", resultado.raiz));
        resultadoInfo.append(String.format("Erro final: %.10f\n", resultado.erro));
        resultadoInfo.append(String.format("Número de iterações: %d\n", resultado.iteracoes));

        // Adicionar informações extras sobre convergência com ícones
        if (temErro && temIteracoes) {
            if (resultado.erro <= Double.parseDouble(txtErro.getText())) {
                resultadoInfo.append("\n✓ Convergiu para o erro especificado dentro do número de iterações definido.");
            } else {
                resultadoInfo.append("\n⚠ Número máximo de iterações atingido sem convergir para o erro especificado.");
            }
        } else if (temErro) {
            resultadoInfo.append("\n✓ Convergiu para o erro especificado.");
        } else if (temIteracoes) {
            resultadoInfo.append("\n✓ Concluiu o número de iterações especificado.");
        }

        lblResultado.setText(resultadoInfo.toString());
        lblResultado.setTextFill(Color.web(COR_TEXTO));

        // Preencher tabela de iterações
        ObservableList<IteracaoRow> dadosIteracoes = FXCollections.observableArrayList();

        for (MetodoBissecao.IteracaoInfo info : resultado.historico) {
            dadosIteracoes.add(new IteracaoRow(
                    info.iteracao,
                    info.valorX,
                    info.valorFx,
                    info.erro
            ));
        }

        tabelaIteracoes.setItems(dadosIteracoes);

        // Ajustar a altura da tabela com base no número de linhas
        int numRows = dadosIteracoes.size();
        double rowHeight = tabelaIteracoes.getFixedCellSize();
        double headerHeight = 30; // Altura aproximada do cabeçalho
        double borderHeight = 2;  // Altura adicional para bordas

        // Calcular altura total necessária
        double totalHeight = (numRows * rowHeight) + headerHeight + borderHeight;

        // Definir a altura preferida para acomodar todas as linhas sem scroll
        tabelaIteracoes.setPrefHeight(totalHeight);

        animarResultado();
    }

    private void validarCamposObrigatorios() throws Exception {
        if (txtEquacao.getText().isEmpty() ||
                txtLimiteInferior.getText().isEmpty() ||
                txtLimiteSuperior.getText().isEmpty()) {
            throw new Exception("Preencha todos os campos obrigatórios (equação e limites)!");
        }

        // Pelo menos um dos critérios de parada deve estar preenchido
        if (txtErro.getText().isEmpty() && txtIteracoes.getText().isEmpty()) {
            throw new Exception("Preencha pelo menos um dos critérios de parada (erro ou iterações)!");
        }
    }

    private void animarResultado() {
        FadeTransition ftTabela = new FadeTransition(Duration.millis(300), tabelaIteracoes);
        ftTabela.setFromValue(0.8);
        ftTabela.setToValue(1.0);
        ftTabela.setCycleCount(1);
        ftTabela.play();
    }

    private void exibirErro(String mensagem) {
        lblResultado.setText("Erro: " + mensagem);
        lblResultado.setTextFill(Color.web(COR_ERRO));
        tabelaIteracoes.setItems(FXCollections.observableArrayList());
    }

    private void limparCampos() {
        txtEquacao.clear();
        txtLimiteInferior.clear();
        txtLimiteSuperior.clear();
        txtErro.clear();
        txtIteracoes.clear();

        lblResultado.setText("");
        tabelaIteracoes.setItems(FXCollections.observableArrayList());
    }
}