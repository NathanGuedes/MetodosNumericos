module MetodosNumericos {
    requires javafx.controls;
    requires javafx.graphics;
    requires MathParser.org.mXparser;

    // For math functions and operations
    requires java.base;

    // Open packages to javafx.fxml for FXML loading
    opens com.metodosNumericos to javafx.fxml;
    opens com.metodosNumericos.controllers to javafx.fxml;
    opens com.metodosNumericos.models to javafx.fxml;
}