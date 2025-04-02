package com.example.javafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class TxatController extends BaseController {

    public static final String SHA_CRYPT = "SHA-256";
    public static final String AES_ALGORITHM = "AES";
    public static final String AES_ALGORITHM_GCM = "AES/GCM/NoPadding";

    public static final Integer IV_LENGTH_ENCRYPT = 12;
    public static final Integer TAG_LENGTH_ENCRYPT = 16;

    public static final String LOCAL_PASSPHRASE = "mySecurePassphrase123!"; // Store securely
    @FXML
    private Label erabiltzailea;  // Etiqueta para el nombre de usuario
    @FXML
    private TextArea messagesArea; // Área donde se mostrarán los mensajes
    @FXML
    private TextField messageInput; // Campo de texto para escribir el mensaje
    @FXML
    private Button sendButton; // Botón para enviar el mensaje
    @FXML
    private Button fileChooser;

    private Stage stage;

    private SecretKeySpec generateAesKeyFromPassphrase() throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance(SHA_CRYPT);
        byte[] keyBytes = sha256.digest(LOCAL_PASSPHRASE.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }

    //Encriptado del mensaje
    public String encriptacion(String mensaje) throws Exception{
        // Generate a random IV
        byte[] iv = new byte[IV_LENGTH_ENCRYPT];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        // Generate the AES key from the local passphrase
        SecretKeySpec llaveAes = generateAesKeyFromPassphrase();

        //Inicializa el cifrado en el modo AES-CGM
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM_GCM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_ENCRYPT * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, llaveAes, gcmSpec);

        //ENCIPTACION DEL TEXTO
        byte [] encriptarMensaje = cipher.doFinal(mensaje.getBytes(StandardCharsets.UTF_8));

        // Combine IV and encrypted text and encode them as Base64
        byte[] combinedIvAndCipherText = new byte[iv.length + encriptarMensaje.length];
        System.arraycopy(iv, 0, combinedIvAndCipherText, 0, iv.length);
        System.arraycopy(encriptarMensaje, 0, combinedIvAndCipherText, iv.length, encriptarMensaje.length);

        return Base64.getEncoder().encodeToString(combinedIvAndCipherText);
    }

    private String desencriptacion(String mensajeCifrado) throws Exception {
        byte[] decodedTextoCifrado = Base64.getDecoder().decode(mensajeCifrado);

        //Generar llave para la desencriptacion
        SecretKeySpec llaveAesDesencriptar = generateAesKeyFromPassphrase();

        //Extraer el vector y el texto encriptado
        byte[] nuevoByte = new byte[IV_LENGTH_ENCRYPT];
        System.arraycopy(decodedTextoCifrado, 0, nuevoByte, 0, IV_LENGTH_ENCRYPT);
        byte[] textoEncriptado = new byte[decodedTextoCifrado.length - IV_LENGTH_ENCRYPT];
        System.arraycopy(decodedTextoCifrado, IV_LENGTH_ENCRYPT, textoEncriptado, 0, textoEncriptado.length);

        //Inicializa el cifrado en el modo AES-CGM
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM_GCM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_ENCRYPT * 8, nuevoByte);
        cipher.init(Cipher.DECRYPT_MODE, llaveAesDesencriptar, gcmSpec);

        byte[] mensajeDesencriptado = cipher.doFinal(textoEncriptado);

        return new String(mensajeDesencriptado, StandardCharsets.UTF_8);
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private PrintWriter out; // Para enviar el mensaje al servidor
    private BufferedReader in; // Para recibir los mensajes del servidor
    private Socket socket; // Conexión del cliente con el servidor

    public void setErabiltzailea(String izena) {
        erabiltzailea.setText(izena);
    }

    public void conectarAServidor(String host, int puerto) {
        new Thread(() -> {
            try {
                System.out.println("Intentando conectar al servidor en " + host + ":" + puerto);

                // Conectar con el servidor
                socket = new Socket(host, puerto);
                System.out.println("Conexión con el servidor establecida.");

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                System.out.println("Canales de entrada y salida inicializados.");

                // Informar que la conexión ha sido establecida
                Platform.runLater(() -> messagesArea.appendText("Conectado al servidor en " + host + ":" + puerto + "\n"));

                // Escuchar mensajes del servidor
                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    System.out.println("Mensaje recibido del servidor: " + mensaje);
                    String mensajeCompleto;
                    //Ya que el formato es Usuario: mensajeCifrado
                    String[] partes = mensaje.split(": ", 2);
                    if(partes.length == 2){
                        String usuario = partes[0];
                        String mensajeCifrado = partes[1];
                        //Desencriptar el mensaje
                        try{
                            String mensajeOriginal = desencriptacion(mensajeCifrado);
                             mensajeCompleto = usuario + ": " + mensajeOriginal;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        mensajeCompleto = mensaje;
                    }
                    final String mensajeFinal = mensajeCompleto;
                    Platform.runLater(() -> messagesArea.appendText(mensajeFinal + "\n"));
                }

                System.out.println("El servidor ha cerrado la conexión o no hay más mensajes.");
            } catch (IOException e) {
                System.err.println("Error al conectar con el servidor: " + e.getMessage());
                Platform.runLater(() -> mostrarError("Error al conectar con el servidor", e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void onSendButtonClick() throws Exception {
        String mensaje = messageInput.getText();
        String mensajeEncriptado = encriptacion(mensaje);
        if (!mensaje.isEmpty()) {
            if (out != null) {
                String usuario = erabiltzailea.getText();
                String mensajeCompleto = usuario + ": " + mensajeEncriptado;

                // Depuración para verificar que se está enviando el mensaje
                System.out.println("Enviando mensaje: " + mensajeCompleto);

                // Enviar mensaje al servidor
                out.println(mensajeCompleto);

                // Mostrar el mensaje localmente en el área de mensajes
                messagesArea.appendText("Tú: " + mensaje + "\n");

                // Limpiar el campo de texto
                messageInput.clear();
            } else {
                mostrarError("Conexión no establecida", "No se puede enviar el mensaje porque no hay conexión.");
            }
        } else {
            mostrarError("Mensaje vacío", "No puedes enviar un mensaje vacío.");
        }
    }

    public void cerrarConexion() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            mostrarError("Error al cerrar conexión", e.getMessage());
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    @FXML
    public void onAtzeaBotoiaClick(MouseEvent mouseEvent) throws IOException {

        String erab = erabiltzailea.getText();

        FXMLLoader hasierakoMenua = new FXMLLoader(App.class.getResource("hasieraMenua.fxml"));
        Scene scene = new Scene(hasierakoMenua.load());
        HasierakoMenuaController hmc = hasierakoMenua.getController();
        Stage usingStage = this.getUsingStage();
        hmc.setErabiltzailea(erab);
        hmc.setUsingStage(usingStage);
        usingStage.setScene(scene);
        usingStage.setTitle("Hasierako Menua");
        usingStage.show();

    }

    @FXML
    public void initialize() {
        System.out.println("Método initialize ejecutado.");
        String host = "192.168.115.154"; // Dirección local
        int puerto = 5555; // El mismo puerto que el servidor

        try {
            conectarAServidor(host, puerto);
        } catch (Exception e) {
            mostrarError("Error al inicializar", e.getMessage());
        }
    }

    @FXML
    public void onArchivosClick(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione archivo");

        FileChooser.ExtensionFilter imagesFiler = new FileChooser.ExtensionFilter(
                "Imagenes", "*.png", "*.jpg", "*.gif");

        fileChooser.getExtensionFilters().add(imagesFiler);

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if(selectedFiles != null) {
            StringBuilder fileInfo = new StringBuilder();
            fileInfo.append("Archivo seleccionado: " + selectedFiles.size() + "\n");

            for (File file : selectedFiles) {
                fileInfo.append(" - ").append(file.getName()).append("\n");
            }
            messagesArea.appendText(fileInfo.toString());
        }


    }
}

