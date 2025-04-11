package com.example.javafx;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FtpUploaderTest {
    //USARE EL MOCK PARA SIMULAR UN CLIENTE FTP REAL
    @Mock
    FTPClient mockFTPClient;

    private FtpUploader ftpUploader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ftpUploader = new FtpUploader(mockFTPClient);
    }

    @Test
    void subirArchivo_todosLosDatosCorrectos_returnExito() throws IOException {
        //Configurarremos el comportamiento del mock del FTP
        //usaremos la funciondoNothing() para los metodos como connect() que devuelven un void en vez de un booleano
        doNothing().when(mockFTPClient).connect(anyString(), anyInt());

        //El resto si que devuelve un booleano
        when(mockFTPClient.login(anyString(), anyString())).thenReturn(true);
        when(mockFTPClient.makeDirectory(anyString())).thenReturn(true);
        when(mockFTPClient.storeFile(anyString(), any(InputStream.class))).thenReturn(true);
        when(mockFTPClient.isConnected()).thenReturn(true);

        String mockXmlFile = "<tiempo><temperatura>25</temperatura></tiempo>";
        InputStream mockInputStream = new ByteArrayInputStream(mockXmlFile.getBytes());

        boolean exito = ftpUploader.subirArchivo("192.168.115.154", 21, "ikaslea", "ikaslea",
                "/Filezilla", "tiempoErronka2.xml", mockInputStream);

        assertTrue(exito, "El resultado de la operacion deberia haber sido true");
    }

    @Test
    void subirArchivo_UsuarioIncorrecto_returnFalse() throws IOException {
        doNothing().when(mockFTPClient).connect(anyString(), anyInt());
        //Queremos simular de que el usuario introducido es el incorrecto
        when(mockFTPClient.login(anyString(), anyString())).thenReturn(false);
        when(mockFTPClient.makeDirectory(anyString())).thenReturn(true);
        when(mockFTPClient.storeFile(anyString(), any(InputStream.class))).thenReturn(true);
        when(mockFTPClient.isConnected()).thenReturn(false);

        String moclXmlFile = "<tiempo><temperatura>25</temperatura></tiempo>";
        InputStream mockInputStream = new ByteArrayInputStream(moclXmlFile.getBytes());

        boolean exito = ftpUploader.subirArchivo("192.168.115.154", 21, "usuario-incorrecto", "ikaslea",
                "/Filezilla", "tiempoErronka2.xml", mockInputStream);

        assertFalse(exito, "No deberia de haber subido el archivo con el usuario incorrecto");
    }

    @Test
    void subirArchivo_conexionFallida_returnException() throws IOException {
        //Simularemos que la conexion falla al intentar conectarse al servidor FTP
        doThrow(new IOException("Error al intentar conectarse al servidor FTP")).when(mockFTPClient).connect(anyString(), anyInt());
        when(mockFTPClient.login(anyString(), anyString())).thenReturn(true);
        when(mockFTPClient.makeDirectory(anyString())).thenReturn(true);
        when(mockFTPClient.storeFile(anyString(), any(InputStream.class))).thenReturn(true);
        when(mockFTPClient.isConnected()).thenReturn(true);

        String mockXMLFile = "<tiempo><temperatura>25</temperatura></tiempo>";
        InputStream mockInputStream = new ByteArrayInputStream(mockXMLFile.getBytes());

        boolean exito = ftpUploader.subirArchivo("192.168.115.154", 21, "usuario-incorrecto", "ikaslea",
                "/Filezilla", "tiempoErronka2.xml", mockInputStream);

        assertFalse(exito, "No subira el archivo ya que la conexion falla");

    }

    @Test
    void subirArchivo_directorioInexistente_returnFalse() throws IOException {
        doNothing().when(mockFTPClient).connect(anyString(), anyInt());
        when(mockFTPClient.login(anyString(), anyString())).thenReturn(true);
        when(mockFTPClient.makeDirectory(anyString())).thenReturn(true);
        when(mockFTPClient.storeFile(anyString(), any(InputStream.class))).thenReturn(false);
        when(mockFTPClient.isConnected()).thenReturn(true);

        String mockXmlFile = "<tiempo><temperatura>25</temperatura></tiempo>";
        InputStream mockInputStream = new ByteArrayInputStream(mockXmlFile.getBytes());

        boolean exito = ftpUploader.subirArchivo("192.168.115.154", 21, "ikaslea", "ikaslea",
                "/Filezilla", "tiempoErronka2.xml", mockInputStream);

        assertFalse(exito, "Por alguna razon falla el inputStream");
    }
}