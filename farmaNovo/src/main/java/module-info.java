module com.senac.br.farmanovo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.senac.br.farmanovo to javafx.fxml;
    exports com.senac.br.farmanovo;
}