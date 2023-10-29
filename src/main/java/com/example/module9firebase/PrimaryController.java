package com.example.module9firebase;

import javafx.fxml.FXML;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PrimaryController {
    @FXML private TextField nameTextField;
    @FXML private TextField ageTextField;

    @FXML private Button readButton;
    @FXML private Button writeButton;
    @FXML private Button registerButton;
    @FXML private Button switchSecondaryViewButton;

    @FXML private TextArea outputTextArea;

    private boolean key;
    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;

    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    void initialize() {
        AccessDataView accessDataViewModel = new AccessDataView();
        nameTextField.textProperty().bindBidirectional(accessDataViewModel.userNameProperty());
        writeButton.disableProperty().bind(accessDataViewModel.isWritePossibleProperty().not());
    }


    @FXML
    void readButtonClicked(ActionEvent event) {
        readFirebase();
    }

    @FXML
    void registerButtonClicked(ActionEvent event) {
        registerUser();
    }


    @FXML
    void writeButtonClicked(ActionEvent event) {
        addData();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    public boolean readFirebase() {
        key = false;

        ApiFuture<QuerySnapshot> future = App.fstore.collection("Person").get();
        List<QueryDocumentSnapshot> documents;
        try{
            documents =future.get().getDocuments();
            if(documents.size()>0){
                System.out.println("Outing data from firabase database....");
                listOfUsers.clear();
                for(QueryDocumentSnapshot document : documents){
                    outputTextArea.setText(outputTextArea.getText()+document.getData().get("name")
                            +", Age: "+document.getData().get("age")+"\n");
                    System.out.println(document.getId() + " => "+document.getData().get("name"));
                    person = new Person();
                    person.setName(String.valueOf(document.getData().get("name")));
                    person.setAge(Integer.parseInt(document.getData().get("age").toString()));
                    listOfUsers.add(person);
                }
            }else{
                System.out.println("no data");
            }
            key =true;
        }catch (InterruptedException | ExecutionException ex){
            ex.printStackTrace();
        }
        return key;
    }

    public boolean registerUser() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail("user@exmaple.com")
                .setEmailVerified(false)
                .setPassword("secretPassword")
                .setPhoneNumber("+11234567890")
                .setDisplayName("John Doe")
                .setDisabled(false);
        UserRecord userRecord;
        try{
            userRecord = App.fauth.createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            return true;
        }catch (FirebaseAuthException ex){
            //Logger.getLogger(FirestoreContext.class.getName()).log(System.Logger.Level.SEVERE,null,ex);
            return false;
        }

    }

    public void addData() {
        DocumentReference docRef = App.fstore.collection("Person").document(UUID.randomUUID().toString());
        Map<String,Object> data = new HashMap<>();
        data.put("name",nameTextField.getText());
        data.put("age",Integer.parseInt(ageTextField.getText()));
        ApiFuture<WriteResult> result = docRef.set(data);

    }
}