module pl.smyk.servicedeskfrontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires spring.web;

    opens pl.smyk.servicedeskfrontend to javafx.fxml;
    exports pl.smyk.servicedeskfrontend;
    opens pl.smyk.servicedeskfrontend.controller to javafx.fxml;
}