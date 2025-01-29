package edu.snhu.dayplanner.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.scene.control.Label;

/**
 * Defines the main application user interface layout and stylized components for the Day Planner application.
 */
public class NavigationView {
    // layout elements
    private final BorderPane rootLayout;
    private final Button appointmentNav;
    private final Button taskNav;
    private final Button contactNav;
    private final VBox navBar;


    public NavigationView() {
        rootLayout = new BorderPane();

        // initialize nav buttons and navbar
        appointmentNav = new Button("Appointments");
        taskNav = new Button("Tasks");
        contactNav = new Button("Contacts");
        navBar = new VBox(contactNav, taskNav, appointmentNav);
        // organize layout and style within rootLayout
        setView();
    }

    // organizes the layout and style within nav
    private void setView() {

        // center buttons
        navBar.setAlignment(Pos.CENTER);

        // set button Styles
        setButtonStyle(appointmentNav);
        setButtonStyle(taskNav);
        setButtonStyle(contactNav);

        // arrange elements in border layout
        rootLayout.setLeft(navBar);
        VBox startBox = new VBox(new Label("<- Click a button to get started"));
        startBox.setAlignment(Pos.CENTER);
        rootLayout.setCenter(startBox);
    }

    private void setButtonStyle(Button button) {
        button.setMinSize(50, 50);
        button.setPrefSize(150, 150);
        VBox.setMargin(button, new Insets(10.0d));

    }

    /**
     * Replaces the primary layouts center view with the provided {@code Node}. Used to switch between Contact, Task,
     * and Appointment management screens within {@code NavigationController}.
     * @param view The JavaFX element that should be displayed in the center of the layout
     */
    public void setCenterView(Node view) {
        rootLayout.setCenter(view);
    }

    /**
     * @return a reference to the appointment navigation button.
     */
    public Button getAppointmentButton() {
        return appointmentNav;
    }
    /**
     * @return a reference to the task navigation button.
     */
    public Button getTaskButton() {
        return taskNav;
    }
    /**
     * @return a reference to the contact navigation button.
     */
    public Button getContactButton() {
        return contactNav;
    }
    /**
     * @return a reference to the primary applications root {@BorderPane} component containing all app elements.
     */
    public Parent getView() {
        return rootLayout;
    }
}
