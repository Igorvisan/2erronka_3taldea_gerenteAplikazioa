module com.example.javafx {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javahelp;
    requires javafx.swing;
    requires javafx.web;
    requires mysql.connector.j;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.services.drive;
    requires com.google.api.client.json.gson;
    requires org.apache.commons.net;
    requires net.sf.jasperreports.core;

    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
}