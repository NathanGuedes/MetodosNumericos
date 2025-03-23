module com.metodosNumericos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Para funções e operações matemáticas
    requires java.base;

    // Para a biblioteca de parsing de expressões matemáticas
    requires MathParser.org.mXparser;

    // Exporta os pacotes que existem no projeto
    exports com.metodosNumericos;
    exports com.metodosNumericos.telas;

    // Abre pacotes para javafx.fxml para carregamento FXML
    opens com.metodosNumericos to javafx.fxml;
    opens com.metodosNumericos.telas to javafx.fxml;
}