module org.net.demo {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.net.demo to javafx.fxml;
    exports org.net.demo;
}
