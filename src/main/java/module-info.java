module org.net.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires transitive javafx.graphics;

    opens org.net.demo to javafx.fxml,com.google.gson;

    exports org.net.demo;
}
