package com.senac.br.back;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MedicamentoController {
    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtTipo;

    @FXML
    public void salvarMedicamento(ActionEvent event) throws Exception {
        //Validar Dados

        String jsonRequest = String.format("{ " +
                        "  \"nome\": \"string\",\n" +
                        "  \"tipo\": \"string\",\n" +
                        "}", txtNome.getText(),
                txtNome.getText(),
                txtTipo.getText(),
                "sakjbhaskjcdhaiubshLKVAShfbdw68asd65d");

        int respostaApi = executaMetodoAPI("http://localhost:8080/medicamentos",jsonRequest,"POST");


        if(respostaApi ==200){
            showMessage(Alert.AlertType.INFORMATION,"Medicamento salvo com sucesso!");
            voltar(event);

        }else {
            showMessage(Alert.AlertType.ERROR,"Erro ao salvar Medicamento!");
        }

    }


    @FXML
    public void voltar(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/senac/br/back/menu-view.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

    }

    private int executaMetodoAPI(String url,String json,String protocoloHttp)
            throws Exception {


        URL urlAPI = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlAPI.openConnection();
        connection.setRequestMethod(protocoloHttp);
        connection.setDoOutput(true);


        if(!json.isEmpty()){
            connection.setRequestProperty("Content-Type","application/json");

            try (OutputStream os = connection.getOutputStream()){
                os.write(json.getBytes());
            }

        }

//        var  br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
//       var sb = new StringBuilder();
//        String output;
//        while ((output = br.readLine()) != null) {
//            sb.append(output);
//        }
//
//        var retornoBody = sb.toString();

        return connection.getResponseCode();

    }
    private void showMessage(Alert.AlertType type, String msg){
        Alert alerta =new Alert(type);
        alerta.setTitle("Mensagem do Sistema!");
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
