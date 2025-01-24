package edu.snhu.dayplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import service.contactservice.ContactService;
import service.contactservice.Contact;

import java.io.IOException;

public class HelloApplication extends Application
{
    static ContactService contacts = new ContactService();
    static Contact contact;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(new Label(contact.toString()));

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        contacts.add("Michael", "Lorenz", "2153039298", "387 W Broad St");
        contact = contacts.getEntityById("0");
        System.out.println(contact.getFirstName());

        launch();
    }
}