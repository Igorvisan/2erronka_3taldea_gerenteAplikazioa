package com.example.javafx;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class UploadToDrive {
    private static final String CARPETA_ID = "1owlmdAYq5yYdcuRkCs4AjMft1BsaZ535";
    private static final String NOMBRE_XML = "tiempoErronka2.xml";

    public static void subirDocumento(InputStream inputStream, Drive driveService) throws IOException {
        List<File> archivos = driveService.files().list()
                .setQ("'" + CARPETA_ID + "' in parents and name = '" + NOMBRE_XML + "'")
                .setFields("files(id, name)")
                .execute()
                .getFiles();

        if (archivos.isEmpty()) {
            // Si el archivo no existe, crear uno nuevo
            File fileMetadata = new File();
            fileMetadata.setName(NOMBRE_XML);
            fileMetadata.setParents(Collections.singletonList(CARPETA_ID));

            InputStreamContent mediaContent = new InputStreamContent("application/xml", inputStream);

            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            System.out.println("Archivo subido con ID: " + uploadedFile.getId());
        } else {
            // Si el archivo existe, actualizar su contenido
            File file = archivos.get(0);
            String fileId = file.getId();

            InputStreamContent mediaContent = new InputStreamContent("application/xml", inputStream);

            File updatedFile = driveService.files().update(fileId, null, mediaContent)
                    .setFields("id")
                    .execute();

            System.out.println("Archivo actualizado con ID: " + updatedFile.getId());
        }
    }
}
