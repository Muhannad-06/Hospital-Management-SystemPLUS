package application.controllers;

import application.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.sql.*;

public class controllerRegister {

	@FXML
	private TextField txtUsername, txtPassword, txtFirstName, txtSecondName, txtNumber, txtHeight, txtWeight;
	@FXML
	private DatePicker dateOfBirth;
	@FXML
	private RadioButton btnMale, btnFemale, btnYES, btnNO;
	
	@FXML
	private void btnSignUp(ActionEvent e) {
	    try {
	        // Safe parsing for numbers
	        if (txtHeight.getText().isEmpty() || txtWeight.getText().isEmpty()) {
	            System.err.println("Height and Weight are required!");
	            showAlert("Lacking Information", "Height and Weight are required!");
	            return;
	        }
	        if (dateOfBirth.getValue() == null) {
	            System.err.println("Please select a Date of Birth.");
	            showAlert("Lacking Information", "Please select a Date of Birth");
	            return;
	        }
	        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
	            System.err.println("Username and Password are required!");
	            showAlert("Username & Password", "Username and Password are required!");
	            return;
	        }
	        if(DatabaseManager.isUsernameExists(txtUsername.getText())) {
	        	System.err.println("This Username already Exists");
	        	showAlert("Username", "This Username already Exists");
	        	return;
	        }
	        if (txtFirstName.getText().isEmpty() || txtSecondName.getText().isEmpty() || txtNumber.getText().isEmpty()) {
	            System.err.println("Please Fill Your Information");
	            showAlert("Lacking Information", "First and Second Name are Required");
	            return;
	        }
	        

	        double height = Double.parseDouble(txtHeight.getText());
	        double weight = Double.parseDouble(txtWeight.getText());
	        String gender = btnMale.isSelected() ? "Male" : "Female";
	        String chronic = btnYES.isSelected() ? "YES" : "NO";

	        // SQL string with 11 parameters to fill it all at once
	        String sql = "INSERT INTO patients (first_name, second_name, phone, gender, dob, username, password, height, weight, blood_type, chronic_disease) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
	            ps.setString(1, txtFirstName.getText());
	            ps.setString(2, txtSecondName.getText());
	            ps.setString(3, txtNumber.getText());
	            ps.setString(4, gender);
	            ps.setString(5, dateOfBirth.getValue().toString());
	            ps.setString(6, txtUsername.getText());
	            ps.setString(7, txtPassword.getText());
	            ps.setDouble(8, height);
	            ps.setDouble(9, weight);
	            
	            // Set blood_type to null as To perform The blood test in the Patient's Dashboard
	            ps.setNull(10, java.sql.Types.VARCHAR); 
	            
	     
	            ps.setString(11, chronic);
	            ps.executeUpdate();
	            System.out.println("Patient registered successfully!");
	            if (controllerWelcomeScreen.instance != null) {
	                controllerWelcomeScreen.instance.initialize(); // Force the welcome screen to recount
	            }
	            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
	            
                stage.close();
	            
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }

	    } catch (NumberFormatException ex) {
	        // This catches cases where txtHeight or txtWeight are not valid numbers
	        System.err.println("Error: Please enter numbers only for Height and Weight.");
	    } catch (NullPointerException ex) {
	    	// I made this exception in the developing process as there was an error due to much UIs so I had to figure out was it due to Missing Id
	        // This catches cases where fx:id is still missing in Scene Builder
	        System.err.println("Error: One of the UI components (fx:id) is not linked in Scene Builder!");
	    }
	}
	
	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
    	alert.setTitle(title);
    	alert.setContentText(content);
    	alert.setHeaderText(null);
    	alert.showAndWait();
	}
	
	
}
