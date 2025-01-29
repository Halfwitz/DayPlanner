module edu.snhu.dayplanner {

    requires org.controlsfx.controls;
    requires tornadofx.controls;

    opens edu.snhu.dayplanner to javafx.fxml;
    exports edu.snhu.dayplanner;
    exports edu.snhu.dayplanner.service.appointmentservice;
    opens edu.snhu.dayplanner.service.appointmentservice to javafx.fxml;
    exports edu.snhu.dayplanner.service.contactservice;
    opens edu.snhu.dayplanner.service.contactservice to javafx.fxml;
    exports edu.snhu.dayplanner.service.taskservice;
    opens edu.snhu.dayplanner.service.taskservice to javafx.fxml;
    exports edu.snhu.dayplanner.control;
    opens edu.snhu.dayplanner.control to javafx.fxml;
}