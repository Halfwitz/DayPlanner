module edu.snhu.dayplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens edu.snhu.dayplanner to javafx.fxml;
    exports edu.snhu.dayplanner;
}