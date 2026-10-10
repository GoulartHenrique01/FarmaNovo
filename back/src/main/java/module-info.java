module com.example.back {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens com.senac.br.back to javafx.fxml;
    exports com.senac.br.back;
}