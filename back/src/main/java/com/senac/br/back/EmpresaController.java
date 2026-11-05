package com.senac.br.back;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class EmpresaController {

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtNomeFantasia;

    @FXML
    private TextField txtRazaoSocial;

    @FXML
    public void salvarEmpresa(ActionEvent event) throws Exception {

        String jsonRequest = String.format(
                "{\n" +
                        "  \"cnpj\": \"%s\",\n" +
                        "  \"nomeFantasia\": \"%s\",\n" +
                        "  \"razaoSocial\": \"%s\",\n" +
                        "  \"secretKey\": \"%s\"\n" +
                        "}",
                txtCnpj.getText(),
                txtNomeFantasia.getText(),
                txtRazaoSocial.getText(),
                "sakjbhaskjcdhaiubshLKVAShfbdw68asd65d"
        );

        int respostaApi = executaMetodoAPI(
                "http://localhost:8080/empresas",
                jsonRequest,
                "POST"
        );

        if (respostaApi == 200 || respostaApi == 201) {

            showMessage(Alert.AlertType.INFORMATION,
                    "Empresa salva com sucesso!");

            voltar(event);

        } else {

            showMessage(Alert.AlertType.ERROR,
                    "Erro ao salvar empresa! Código: " + respostaApi);
        }
    }

    @FXML
    public void voltar(ActionEvent event) throws IOException {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/example/back/menu-view.fxml"));

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.setScene(scene);
    }

    private int executaMetodoAPI(
            String url,
            String json,
            String protocoloHttp
    ) throws Exception {

        URL urlAPI = new URL(url);

        HttpURLConnection connection =
                (HttpURLConnection) urlAPI.openConnection();

        connection.setRequestMethod(protocoloHttp);
        connection.setDoOutput(true);


        if (!json.isEmpty()) {

            connection.setRequestProperty(
                    "Content-Type",
                    "application/json; charset=UTF-8"
            );

            try (OutputStream os = connection.getOutputStream()) {

                os.write(json.getBytes(StandardCharsets.UTF_8));
            }
        }

        int resposta = connection.getResponseCode();

        connection.disconnect();

        return resposta;
    }

    private void showMessage(Alert.AlertType type, String msg) {

        Alert alerta = new Alert(type);

        alerta.setTitle("Mensagem do Sistema!");
        alerta.setHeaderText(null);
        alerta.setContentText(msg);

        alerta.showAndWait();
    }
}