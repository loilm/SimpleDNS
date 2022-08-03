module com.simpledns {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.simpledns to javafx.fxml;
    exports com.simpledns;

    opens com.simpledns.controller to javafx.fxml;
    exports com.simpledns.controller;
}