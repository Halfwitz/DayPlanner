package edu.snhu.dayplanner;

import edu.snhu.dayplanner.control.NavController;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Serves as the entry point for the Day Planner application.
 * Initializes and launches the JavaFX application, sets up the primary scene using {@code NavController}, and shows the
 * stage.
 *
 * <p> The application allows users to manage their appointments, tasks, and contacts through a simple JavaFX graphical
 * interface by enabling viewing, creating, editing, deleting of items in each category. Navigation is managed through
 * a top-level menu sidebar. Execute {@code main} to launch the application.
 * </p>
 * @author Michael Lorenz
 *  @version 1.0, 1/25/2025
 */
public class Application extends javafx.application.Application
{
    /**
     * Sets up the primary application view using {@code NavController} to generate a functional view with navigation
     * to each screen. Automatically called by {@code launch} from within {@code main}.
     * @param stage the window containing the application display
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        NavController navControl = new NavController();

        Scene scene = new Scene(navControl.getNavView(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}