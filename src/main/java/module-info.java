module org.katyshevtseva {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports org.katyshevtseva;
    opens org.katyshevtseva to javafx.fxml;
}