package com.example.lab8;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class MeasurerApp extends Application
{
    private ArrayList<Measurer> measurers = new ArrayList<>(); // прилади
    private ComboBox<String> measurerComboBox; // комбобокс для вибору приладу
    private Label nameLabel, unitLabel, lowerLimitLabel, upperLimitLabel, inaccuracyLabel, statusLabel; // текст
    private TextField nameField, unitField, lowerLimitField, upperLimitField, inaccuracyField, statusField; // поля
    private Button createButton, getInfoButton, updateButton; // кнопки

    public static void main(String[] args) // Запуск
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        // Налаштування вікна
        primaryStage.setTitle("Лабораторна №8 АС-221 Мельник");
        primaryStage.setWidth(400);
        primaryStage.setHeight(570);

        // Створення екрану і сцени
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);


        VBox inputBox = new VBox(10); // вертикальний бокс для інших елементів
        inputBox.setPadding(new Insets(10)); // відступ
        inputBox.setStyle("-fx-background-color: #f0f0f0;"); // колір

        // текстові лейбли (ініціалізація)
        nameLabel = new Label("Name:");
        unitLabel = new Label("Measuring Unit:");
        lowerLimitLabel = new Label("Lower Limit:");
        upperLimitLabel = new Label("Upper Limit:");
        inaccuracyLabel = new Label("Inaccuracy:");
        statusLabel = new Label("Status:");

        // поля вводу (ініціалізація)
        nameField = new TextField();
        unitField = new TextField();
        lowerLimitField = new TextField();
        upperLimitField = new TextField();
        inaccuracyField = new TextField();
        statusField = new TextField();

        // кнопки (ініціалізація)
        createButton = new Button("Create Measurer");
        getInfoButton = new Button("Get Info");
        updateButton = new Button("Update Measurer");

        // визначення функцій для кнопок
        createButton.setOnAction(e -> createMeasurer());
        getInfoButton.setOnAction(e -> displayMeasurerInfo());
        updateButton.setOnAction(e -> updateMeasurer());

        // додавання об'єктів у вертикальний бокс
        inputBox.getChildren().addAll(nameLabel, nameField, unitLabel, unitField, lowerLimitLabel, lowerLimitField,
                upperLimitLabel, upperLimitField, inaccuracyLabel, inaccuracyField, statusLabel, statusField,
                createButton, getInfoButton, updateButton);

        // ініціалізація комбобоксу
        measurerComboBox = new ComboBox<>();
        measurerComboBox.setPromptText("Select Measurer");

        // вертикальний бокс для комбобоксу
        VBox comboBoxBox = new VBox(10);
        comboBoxBox.getChildren().addAll(measurerComboBox);
        comboBoxBox.setPadding(new Insets(10));

        // встановлення верт.боксів у головному контейнері
        borderPane.setCenter(inputBox);
        borderPane.setBottom(comboBoxBox);

        // встановлення сцени
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createMeasurer() // створення приладу
    {
        String name = nameField.getText();
        String unit = unitField.getText();
        double lowerLimit = 0;
        double upperLimit = 1;
        double inaccuracy = 0;
        String status = statusField.getText();

        // Перевірка на пусті поля
        if (name.isEmpty() || unit.isEmpty() || lowerLimitField.getText().isEmpty() ||
                upperLimitField.getText().isEmpty() || inaccuracyField.getText().isEmpty() || status.isEmpty())
        {
            showError("All fields must be filled in.");
            return;
        }

        if (getMeasurerByName(name) != null) // якщо такий прилад вже існує (бо пошук за іменем)
        {
            showError("Measurer with this name already exists.");
            return;
        }

        try // Перевірка нижньої границі
        {
            lowerLimit = Double.parseDouble(lowerLimitField.getText());
        }
        catch (NumberFormatException e)
        {
            showError("Lower limit must be a number.");
            return;
        }

        try // Перевірка верхньої границі
        {
            upperLimit = Double.parseDouble(upperLimitField.getText());
        }
        catch (NumberFormatException e)
        {
            showError("Upper limit must be a number.");
            return;
        }

        try // Перевірка похибки
        {
            inaccuracy = Double.parseDouble(inaccuracyField.getText());
        }
        catch (NumberFormatException e)
        {
            showError("Inaccuracy must be a number.");
            return;
        }

        // Створення приладу
        Measurer measurer = new Measurer(name, unit, lowerLimit, upperLimit, inaccuracy, status);
        measurers.add(measurer);
        measurerComboBox.getItems().add(name);

        // Очищення полей вводу
        nameField.clear();
        unitField.clear();
        lowerLimitField.clear();
        upperLimitField.clear();
        inaccuracyField.clear();
        statusField.clear();
    }

    private void displayMeasurerInfo() // виведення інформації про прилад
    {
        String selectedName = measurerComboBox.getValue();
        Measurer selectedMeasurer = getMeasurerByName(selectedName); // знайти за ім'ям
        if (selectedMeasurer != null) // якщо щось обрано
        {
            // виведення інформації про обраний прилад в окремому вікні
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Measurer Info");
            alert.setHeaderText(null);
            alert.setContentText("Name: " + selectedMeasurer.getName() + "\n" +
                    "Measuring Unit: " + selectedMeasurer.getUnit() + "\n" +
                    "Lower Limit: " + selectedMeasurer.getLowerLimit() + "\n" +
                    "Upper Limit: " + selectedMeasurer.getUpperLimit() + "\n" +
                    "Inaccuracy: " + selectedMeasurer.getInaccuracy() + "\n" +
                    "Status: " + selectedMeasurer.getStatus());
            alert.showAndWait();
        }
    }

    private void updateMeasurer() // оновити статус приладу
    {
        String selectedName = measurerComboBox.getValue();
        Measurer selectedMeasurer = getMeasurerByName(selectedName);
        if (selectedMeasurer != null)
        {
            // виведення діалогового вікна для оновлення статусу приладу
            TextInputDialog dialog = new TextInputDialog(selectedMeasurer.getStatus());
            dialog.setTitle("Update Measurer Status");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter new status:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newStatus -> selectedMeasurer.setStatus(newStatus));
        }
    }

    private Measurer getMeasurerByName(String name) // отримати прилад за ім'ям з колекції
    {
        for (Measurer measurer : measurers)
        {
            if (measurer.getName().equals(name))
            {
                return measurer;
            }
        }
        return null;
    }

    private void showError(String message) // вивести помилку
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
