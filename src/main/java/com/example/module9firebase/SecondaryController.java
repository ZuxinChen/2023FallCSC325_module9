package com.example.module9firebase;

import javafx.fxml.FXML;

import java.io.IOException;

public class SecondaryController {
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
