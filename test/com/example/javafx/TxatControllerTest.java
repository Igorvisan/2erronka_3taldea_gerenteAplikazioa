package com.example.javafx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TxatControllerTest {


    private TxatController controller;

    @Mock
    private PrintWriter mockPrintWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        controller = new TxatController();

        // Usar reflexión para establecer campos privados para testing
        Field outField = TxatController.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(controller, mockPrintWriter);

        // Configurar componentes JavaFX mediante mocks esa seria la idea
        // Esto depende de tu entorno de pruebas
    }

    @Test
    void testEncriptacionDesencriptacion_conMensaje_returnsMensajeDesecriptado() throws Exception {
        // Probar que un mensaje puede ser encriptado y luego desencriptado al original
        String mensajeOriginal = "Mensaje de prueba";

        // Encriptar el mensaje
        String mensajeEncriptado = controller.encriptacion(mensajeOriginal);

        // Desencriptar el mensaje
        String mensajeDesencriptado = controller.desencriptacion(mensajeEncriptado);

        // Comprobar que el mensaje desencriptado coincide con el original
        assertEquals(mensajeOriginal, mensajeDesencriptado, "El mensaje desencriptado debe coincidir con el mensaje original");

        // Asegurar que el mensaje encriptado es diferente del original
        assertNotEquals(mensajeOriginal, mensajeEncriptado, "El mensaje encriptado debe ser diferente del original");
    }

    @Test
    void testEncriptacion_ConMensajeVacio_ReturnsMissingMessage() throws Exception {
        // Probar encriptación con cadena vacía
        String mensajeVacio = "";
        String encriptadoVacio = controller.encriptacion(mensajeVacio);
        String desencriptadoVacio = controller.desencriptacion(encriptadoVacio);

        assertEquals(mensajeVacio, desencriptadoVacio, "Debería manejar correctamente mensajes vacíos");
    }

    @Test
    void testEncriptacionCon_CaracteresEspeciales_returnsMessageDone() throws Exception {
        // Probar con caracteres especiales y texto no ASCII
        String mensajeCaracteresEspeciales = "¡Hola! こんにちは 123#$%^&*()";

        String encriptado = controller.encriptacion(mensajeCaracteresEspeciales);
        String desencriptado = controller.desencriptacion(encriptado);

        assertEquals(mensajeCaracteresEspeciales, desencriptado, "Debería manejar correctamente caracteres especiales");
    }

    @Test
    void testDesencriptacion_desencriptacionInvalida_retursFalse() {
        // Probar intentar desencriptar un mensaje inválido
        String mensajeInvalido = "EstoNoEsUnMensajeEncriptadoValido";

        // Debería lanzar una excepción al intentar desencriptar datos inválidos
        assertThrows(Exception.class, () -> {
            controller.desencriptacion(mensajeInvalido);
        });
    }

    @Test
    void testEnviarMensaje_conMensaje_returnsSuccessMessage() throws Exception {
        //Para conseguir el nombre del erabiltzaile usaremos erabiltzaile.getText (usaremos mock)

        // Probar enviar un mensaje válido
        // Necesitarías configurar controller.erabiltzailea con un mock

        //Ejecutaria el seguiente metodo dol TxatController
        int resultado = controller.enviarMensaje("Hola mundo");
        //Este imprimiria el mensaje en el messageArea del txatController si todso fuese bien
        //Habria que mockear tambien el message area

        // Verificar que PrintWriter fue llamado con el mensaje correcto
        verify(mockPrintWriter).println(contains("TestUser: "));

        // Comprobar código de resultado
        assertEquals(TxatController.SUCCESS_MESSAGE, resultado, "Debería devolver SUCCESS_MESSAGE para un mensaje válido");
    }

    @Test
    void testEnviarMensaje_MensajeVacio_ReturnsMissingMessage() throws Exception {
        // Probar enviar un mensaje vacío
        int resultado = controller.enviarMensaje("");

        // No debería llamar a PrintWriter para un mensaje vacío
        verify(mockPrintWriter, never()).println(anyString());

        // Comprobar código de resultado
        assertEquals(TxatController.MISSING_MESSAGE, resultado, "Debería devolver MISSING_MESSAGE para un mensaje vacío");
    }

    @Test
    void testProcesarMensajes_MensajeDePrueba_ImprimeMensaje() throws Exception {
        // Esta es una prueba compleja que requeriría mockear componentes JavaFX
        // Para hacerlo mas simple, probaremos la lógica de análisis usando reflexión para acceder al metod privado

        // Crear un mensaje de prueba encriptado
        String mensajeOriginal = "Mensaje de prueba";
        String encriptado = controller.encriptacion(mensajeOriginal);
        String mensajeCompleto = "UsuarioPrueba: " + encriptado;

        // Usar reflexión para llamar al metodo privado (Esto me ayudo chat GPT)
        Method procesarMethod = TxatController.class.getDeclaredMethod("procesarMensajes", String.class);
        procesarMethod.setAccessible(true);

        // Necesitaríamos mockear Platform.runLater y messagesArea
        // Esto es un esquema de lo que la prueba verificaría
        // procesarMetodo.invoke(controller, mensajeCompleto);
        // verificar que messagesArea añadió "UsuarioPrueba: Mensaje de prueba\n" con exito
    }

    @Test
    void testProcesamientoImagen_pasandoleFormatoAdecuado_returnImagenProcesada() {
        // Probar procesamiento de mensajes de imagen
        String mensajeImg = "IMG_FILE:UsuarioPrueba:test.png:datosBase64";

        // Similar al anterior, necesitaría usar mocking para simular los componentes
        // Esta prueba verificaría:
        // 1. La imagen se guarda temporalmente
        // 2. El mensaje se muestra correctamente
        // 3. La referencia de la imagen se almacena en el mapa imagenesRecibidas
        // 4. Y que devuelve true imagen procesada con exito
    }

    @Test
    void testProcesamientoImagen_formatoInadecuado_returnImegenNoProcesada() {
        // Probar procesamiento de mensajes de imagen
        String mensajeImg = "UsuarioPrueba:test.png:datosBase64";

        // Aqui tambien necesitariamos mockear los componentes de JavaFx, como el file chooser el messeageArea...
        // Esta prueba verificaría:
        // 1. Si la imagen tiene el formato correcto
        // 2. Que no mostrase el mensaje de imagen
        // 3. En cambio se mostraria como un mensaje normal y corriente
    }

    @Test
    void testConexion_conHostYpuerto_returnConexionEstablecida() throws Exception {
        // Probar conexión al servidor y cierre de conexión
        // Necesitaría mockear Socket, BufferedReader, etc.
        // Pasarle al Socket el host y el puertio

        // Mockear una respuesta del servidor imprimiendolo en el textArea (mock)
        // Verificar que aparece el mensaje de conexión establecida
        // Probar el cierre de conexión
    }
}