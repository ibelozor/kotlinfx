package ru.croc.ibelozor.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SampleController {
    public Button btn;


    public TextField textField;
    Model model = new Model();

    public void initialize() {
//        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
//            btn.textProperty().set("Ooops...");
//        }, 2, TimeUnit.SECONDS);


        textField.textProperty().bindBidirectional(model.userInput);
        model.userInput.addListener(((observable, oldValue, newValue) -> {
            logger.info("User input changed from " + oldValue + " to " + newValue);
        }));


    }

    class Model {
        public SimpleStringProperty userInput = new SimpleStringProperty("initial text");
    }


    private static final Logger logger = LogManager.getLogger();
}
