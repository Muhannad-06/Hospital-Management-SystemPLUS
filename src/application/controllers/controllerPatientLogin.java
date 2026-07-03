package application.controllers;

import application.DatabaseManager;
import application.Session;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import application.StageManager;
import application.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class controllerPatientLogin {
	
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	
	@FXML
	private void btnLogin(ActionEvent e) {
	    String sql = "SELECT * FROM patients WHERE username = ? AND password = ?";
	    try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
	        ps.setString(1, txtUsername.getText());
	        ps.setString(2, txtPassword.getText());
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            // Pull ALL columns from the database row
	            int id = rs.getInt("id");
	            String name = rs.getString("first_name");
	            String gender = rs.getString("gender"); 
	            String dob = rs.getString("dob");       
	            double height = rs.getDouble("height");
	            double weight = rs.getDouble("weight");
	            String bloodType = rs.getString("blood_type");
	            String chronic = rs.getString("chronic_disease");

	            //  Create the Patient object with the updated 8 arguments
	            Patient loggedInPatient = new Patient(id, name, gender, dob, height, weight, bloodType, chronic);
	            
	            //  Save the full object to the global session
	            Session.setCurrentUser(loggedInPatient);
	            
	            // Navigation and Stage Management
	            javafx.stage.Stage stage = (javafx.stage.Stage) txtUsername.getScene().getWindow();
	            stage.close();

	            System.out.println("Patient logged in: " + loggedInPatient.getName());
	            StageManager.openModalWindow("FXMLs/patientDashboard.fxml", "Patient Dashboard");
	            
	        } else {
	            System.out.println("Invalid username or password.");
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
}
