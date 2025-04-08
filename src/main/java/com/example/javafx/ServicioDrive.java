package com.example.javafx;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class ServicioDrive {
    public static final String CREDENTIALS_FILE_PATH = "driveapiservices-f3bbe7d79f25.json";

    public Drive getDriveService() throws IOException {
        // Configurar el transporte HTTP
        HttpTransport httpTransport = new NetHttpTransport();
        // Usar GsonFactory para manejar JSON
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        // Crear las credenciales desde el archivo JSON
        InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
        if(credentialsStream == null) {
            throw new IOException(CREDENTIALS_FILE_PATH + " not found");
        }
        GoogleCredential credential = GoogleCredential.fromStream(credentialsStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        // Construir el servicio de Google Drive usando el Builder
        Drive service = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("SubirXML")
                .build();

        return service;
    }
}
