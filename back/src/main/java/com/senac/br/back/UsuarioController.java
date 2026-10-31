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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UsuarioController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    public void salvarUsuario(ActionEvent event) {
        String jsonRequest = "{\n"
                + "  \"nome\": " + jsonString(txtNome.getText()) + ",\n"
                + "  \"email\": " + jsonString(txtEmail.getText()) + ",\n"
                + "  \"senha\": " + jsonString(txtSenha.getText()) + ",\n"
                + "  \"cpf\": " + jsonString(txtCpf.getText()) + ",\n"
                + "  \"secretKey\": " + jsonString("sakjbhaskjcdhaiubshLKVAShfbdw68asd65d") + "\n"
                + "}";

        ApiResponse respostaApi;
        try {
            respostaApi = executaMetodoAPI(
                    "http://localhost:8080/usuarios/admin", jsonRequest, "POST");
        } catch (IOException e) {
            showMessage(Alert.AlertType.ERROR,
                    "Não foi possível conectar à API para salvar o usuário: " + e.getMessage());
            return;
        }

        if (respostaApi.statusCode() == HttpURLConnection.HTTP_OK) {
            showMessage(Alert.AlertType.INFORMATION, "Usuário salvo com sucesso!");
            try {
                voltar(event);
            } catch (IOException e) {
                showMessage(Alert.AlertType.ERROR,
                        "Usuário salvo, mas não foi possível voltar ao menu: " + e.getMessage());
            }
        } else {
            String detalhe = respostaApi.body().isBlank() ? "" : "\n" + respostaApi.body();
            showMessage(Alert.AlertType.ERROR,
                    "Erro ao salvar usuário (HTTP " + respostaApi.statusCode() + ")." + detalhe);
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

    private ApiResponse executaMetodoAPI(String url, String json, String protocoloHttp)
            throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setRequestMethod(protocoloHttp);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int statusCode = connection.getResponseCode();
            InputStream responseStream = statusCode >= HttpURLConnection.HTTP_BAD_REQUEST
                    ? connection.getErrorStream()
                    : connection.getInputStream();
            String body = "";
            if (responseStream != null) {
                try (responseStream) {
                    body = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8).trim();
                }
            }
            return new ApiResponse(statusCode, body);
        } finally {
            connection.disconnect();
        }
    }

    private String jsonString(String value) {
        StringBuilder json = new StringBuilder("\"");
        for (char character : value.toCharArray()) {
            switch (character) {
                case '"' -> json.append("\\\"");
                case '\\' -> json.append("\\\\");
                case '\b' -> json.append("\\b");
                case '\f' -> json.append("\\f");
                case '\n' -> json.append("\\n");
                case '\r' -> json.append("\\r");
                case '\t' -> json.append("\\t");
                default -> {
                    if (character < 0x20) {
                        json.append(String.format("\\u%04x", (int) character));
                    } else {
                        json.append(character);
                    }
                }
            }
        }
        return json.append('"').toString();
    }

    private record ApiResponse(int statusCode, String body) {
    }
    private void showMessage(Alert.AlertType type, String msg){
        Alert alerta =new Alert(type);
        alerta.setTitle("Mensagem do Sistema!");
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }

}
