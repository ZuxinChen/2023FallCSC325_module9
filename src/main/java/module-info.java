module com.example.module9firebase {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.auth;
    requires java.base;
    requires google.cloud.core;
    requires google.cloud.firestore;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires com.google.api.apicommon;


    opens com.example.module9firebase to javafx.fxml;
    exports com.example.module9firebase;

}