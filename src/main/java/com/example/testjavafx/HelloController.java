package com.example.testjavafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloController {
    private double number1;
    private String operator = "";
    private boolean start = true;

    @FXML
    private Label output;

    @FXML
    private void processNumPad(ActionEvent event) {
        if (start) {
            output.setText("");
            start = false;
        }

        String value = ((Button) event.getSource()).getText();
        if (value.equals("e")) {
            output.setText(Math.exp(1) + "");
//            return;
        } else if (value.equals("π")) {
            output.setText(Math.PI + "");
//            return;
        }
//        else if (value.equals("rand")) {
//            output.setText(String.valueOf(Math.round(Math.random() * 100 + 1)));
//        }
        else {
            output.setText(output.getText() + value);
        }
    }

    @FXML
    private void processOperator(ActionEvent event) {
        if (output.getText().equals("ERROR 1")) {
            return;
        }

        String value = ((Button) event.getSource()).getText();
        if (!value.equals("=")) {
            if (!operator.isEmpty()) {
                return;
            }
            operator = value;
            number1 = Double.parseDouble(output.getText());
            output.setText("");
        } else {
            if (operator.isEmpty()) {
                return;
            }
            // calculation with unary operators
            boolean isUnary = operator.equals("x!") || operator.equals("x^2") || operator.equals("x^3") || operator.equals("√x")
                    || operator.equals("∛x") || operator.equals("%") || operator.equals("+/-") || operator.equals("IEEE")
                    || operator.equals("sin()") || operator.equals("cos()") || operator.equals("tan()")
                    || operator.equals("asin()") || operator.equals("acos()") || operator.equals("atan()")
                    || operator.equals("Rad") || operator.equals("Deg") || operator.equals("ln") || operator.equals("BIN");
            if (output.getText().isEmpty() && isUnary) {
                output.setText(calculate(number1, operator));

                operator = "";
                start = true;
                return;
            }
            // if calculate with binary operators but not having the second number
//            if (output.getText().isEmpty()) {
            else if (output.getText().isEmpty()) {
                System.out.println(operator);
                output.setText("ERROR");
                operator = "";
                start = true;
                return;
            }
            // calculation with binary operators

            output.setText(calculate(number1, Double.parseDouble(output.getText()), operator));

            operator = "";
            start = true;
        }
    }

    @FXML
    private void clearOutput(ActionEvent event) {
        output.setText("0");
        start = true;
        operator = "";
    }

    private String calculate(double number1, String op) {
        switch (op) {
            case "x!":
                return String.valueOf(factorial(number1));
            case "x^2":
                return String.valueOf(number1 * number1);
            case "x^3":
                return String.valueOf(number1 * number1 * number1);
            case "√x":
                return String.valueOf(Math.sqrt(number1));
            case "∛x":
                return String.valueOf(Math.pow(number1, (double) 1 / 3));
            case "%":
                return String.valueOf(number1 * 100) + "%";
            case "+/-":
                return String.valueOf(number1 * -1);
            case "IEEE":
                return String.valueOf(GetBinary32((float) number1));
            case "sin()":
                if(checkAppropriateZero(Math.sin(number1))) return "0";
                return String.valueOf(Math.sin(number1));
            case "cos()":
                if(checkAppropriateZero(Math.cos(number1))) return "0";
                return String.valueOf(Math.cos(number1));
            case "tan()":
                if(checkAppropriateZero(Math.tan(number1))) return "0";
                return String.valueOf(Math.tan(number1));
            case "asin()":
                return String.valueOf(Math.asin(number1));
            case "acos()":
                return String.valueOf(Math.acos(number1));
            case "atan()":
                return String.valueOf(Math.atan(number1));
            case "Rad":
                return String.valueOf(Math.toRadians(number1));
            case "Deg":
                return String.valueOf(Math.toDegrees(number1));
            case "ln":
                return String.valueOf(Math.log(number1));
            case "BIN":
                return Integer.toBinaryString((int)number1);

        }
        return "ERROR";
    }

    private String calculate(String op, double number1) {
        switch (op) {
            case "√x":
                return String.valueOf(Math.sqrt(number1));
        }
        return "ERROR";
    }

    private String calculate(double number1, double number2, String op) {
        switch (op) {
            case "+":
                return String.valueOf(number1 + number2);
            case "-":
                return String.valueOf(number1 - number2);
            case "x":
                return String.valueOf(number1 * number2);
            case "/":
                if (number2 == 0) return "ERROR";
                return String.valueOf(number1 / number2);
            case "x^y":
                return String.valueOf(Math.pow(number1, number2));
            case "rand":
                return String.valueOf(Math.round((Math.random()) * ((number2 - number1))) + number1);
        }

        return "ERROR";
    }

    //factorial method
    private double factorial(double y) {
        double fact = 1;
        if (y == 0 || y == 1) {
            fact = 1;
        } else {
            for (int i = 2; i <= y; i++) {
                fact *= i;
            }
        }
        return fact;
    }

    // IEEE
    private String GetBinary32(float value) {
        System.out.println(value);
        int intBits = Float.floatToIntBits(Math.abs(value));
        String ans = Integer.toBinaryString(intBits);
        System.out.println(ans);
        if (value < 0) return "1" + ans;
        return "0" + ans;
    }

    private boolean checkAppropriateZero(double num) {
        if(num <= 0.00000001) return true;
        return false;
    }

    private Stage stage;
    private Scene scene;
    @FXML
    private void switchToFullScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("scientificCalculator.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Calculator");
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void switchToSimpleScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("simpleCalculator.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Calculator");
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void exitProgram(ActionEvent event) throws IOException {
        Platform.exit();
    }
}


// ================================== done video ====================================
//package com.example.testjavafx;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//
//public class HelloController {
//    private long number1;
//    private String operator = "";
//    private boolean start = true;
//
//    @FXML
//    private Label output;
//
//    @FXML
//    private void processNumPad(ActionEvent event) {
//        if (start) {
//            output.setText("");
//            start = false;
//        }
//
//        String value = ((Button) event.getSource()).getText();
//        output.setText(output.getText() + value);
//    }
//
//    @FXML
//    private void processOperator(ActionEvent event) {
//        if (output.getText().equals("ERROR")) {
//            return;
//        }
//
//        String value = ((Button) event.getSource()).getText();
//        if (!value.equals("=")) {
//            if (!operator.isEmpty()) {
//                return;
//            }
//            operator = value;
//            number1 = Long.parseLong(output.getText());
//            output.setText("");
//        } else {
//            if (operator.isEmpty()) {
//                return;
//            }
//            if (output.getText().isEmpty()) {
//                output.setText("ERROR");
//                operator = "";
//                start = true;
//                return;
//            }
//            output.setText(calculate(number1, Long.parseLong(output.getText()), operator));
//            operator = "";
//            start = true;
//        }
//    }
//
//    @FXML
//    private void clearOutput(ActionEvent event) {
//        output.setText("0");
//        start = true;
//        operator = "";
//    }
//
//    private String calculate(long number1, long number2, String op) {
//        switch (op) {
//            case "+":
//                return String.valueOf(number1 + number2);
//            case "-":
//                return String.valueOf(number1 - number2);
//            case "x":
//                return String.valueOf(number1 * number2);
//            case "/":
//                if (number2 == 0) return "ERROR";
//                return String.valueOf(number1 / number2);
//        }
//
//        return "ERROR";
//    }
//}

//===================================== initial file ===================================
//package com.example.testjavafx;
//
//        import javafx.fxml.FXML;
//        import javafx.scene.control.Label;
//
//public class HelloController {
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
//}
//=======================================================================================