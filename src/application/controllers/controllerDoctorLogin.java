package application.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DatabaseManager;
import application.Session;
import application.StageManager;
import application.models.Doctor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class controllerDoctorLogin {
	
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	
	@FXML 
	public void btnLogin(ActionEvent e) {
	    String sql = "SELECT * FROM doctors WHERE username = ? AND password = ?";
	    try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
	        ps.setString(1, txtUsername.getText());
	        ps.setString(2, txtPassword.getText());
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            // Create one generic Doctor object with their specific specialty from the DB
	            Doctor loggedInDoctor = new Doctor(
	                rs.getInt("id"), 
	                rs.getString("username"),
	                rs.getString("gender"),
	                rs.getString("specialty")
	            );
	            
	            // Save to global session
	            Session.setCurrentUser(loggedInDoctor);
	            javafx.stage.Stage stage = (javafx.stage.Stage) txtUsername.getScene().getWindow();
	            stage.close();
	            
	            System.out.println("Doctor logged in: " + loggedInDoctor.getName() + " (" + loggedInDoctor.getSpecialty() + ")");
	            StageManager.openModalWindow("FXMLs/doctorDashboard.fxml", "Doctor Dashboard");
	        } else {
	            System.out.println("Invalid username or password.");
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
}
