package com.example.javafx;
import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import java.io.InputStream;

public class FtpUploader {
    private final FTPClient ftpClient;

    public FtpUploader(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public FtpUploader() {
        this(new FTPClient());
    }

    public boolean subirArchivo(String servidor, int puerto, String usuario, String contrasena,
                              String carpetaRemota, String nombreArchivo, InputStream inputStream) {
        try {
            ftpClient.connect(servidor, puerto);
            if (!ftpClient.login(usuario, contrasena)) {
                return false;
            }

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            boolean dirCrear = ftpClient.makeDirectory(carpetaRemota);
            System.out.println("Creación de carpeta: " + (dirCrear ? "Exitosa" : "Ya existe"));

            boolean exito = ftpClient.storeFile(nombreArchivo, inputStream);
            System.out.println("Reply: " + ftpClient.getReplyString());
            if (exito) {
                System.out.println("Se ha guardado el archivo exitosamente");
            } else {
                System.out.println("No se ha guardado el archivo como debería");
            }

            return exito;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
