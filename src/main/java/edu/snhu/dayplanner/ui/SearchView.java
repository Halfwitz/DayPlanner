package edu.snhu.dayplanner.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class SearchView<F extends Enum<F>> {
    HBox root;
    TextField searchField;
    ComboBox<F> comboBox;
    Button searchButton;

    public SearchView(List<F> fields) {
        root = new HBox();
        HBox.setHgrow(root, Priority.ALWAYS);
        root.setAlignment(Pos.CENTER);

        searchField = new TextField();

        comboBox = new ComboBox<>();
        comboBox.getItems().add(null);
        comboBox.getItems().addAll(fields);
        searchButton = new Button("Search");

        // add components together into root
        root.getChildren().addAll(
                new VBox(new Label("Search:"), searchField),
                new VBox(new Label("Field:"), comboBox),
                new VBox(new Label(), searchButton));
    }

    public TextField getSearchField() {
        return searchField;
    }

    public ComboBox<F> getFieldBox() {
        return comboBox;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public HBox getView() {
        return root;
    }
}
