module com.example.javafx {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javahelp;
    requires javafx.swing;
    requires javafx.web;
    requires mysql.connector.j;

    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
}