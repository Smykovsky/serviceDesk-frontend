module pl.smyk.servicedeskfrontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires spring.web;
    requires java.jwt;
    requires spring.core;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires org.apache.pdfbox.tools;
    requires javafx.swing;

    opens pl.smyk.servicedeskfrontend to javafx.fxml;
    exports pl.smyk.servicedeskfrontend.dto;
    exports pl.smyk.servicedeskfrontend.controller;
    exports pl.smyk.servicedeskfrontend;
    opens pl.smyk.servicedeskfrontend.controller to javafx.fxml;
}