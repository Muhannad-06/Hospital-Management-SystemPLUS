package application.controllers;

import java.sql.SQLException;
import java.time.LocalDate;

import application.DatabaseManager;
import application.Session;
import application.models.Bed;
import application.models.Doctor;
import application.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class controllerEmergency {
	
	@FXML private ComboBox<Patient> comboPatients;
	@FXML private ComboBox<Bed> comboBED;
    @FXML private DatePicker datePicker;
    @FXML private Label lblClinicTitle;
    @FXML private Button confirm;
    @FXML private TextField txtNote;
    
    
    
    @FXML
    public void initialize() { 
    	
        Doctor dr = (Doctor) Session.getCurrentUser();
        
        if (dr != null) {
            // Load the patients assigned to this doctor
            comboPatients.setItems(DatabaseManager.getAllPatientsByDoctor(dr.getId()));
            
            // Load the list of available bed numbers
            comboBED.setItems(DatabaseManager.getAvailableBed());
        }
    }

    @FXML
    private void btnConfirmBooking(ActionEvent e) {
        Doctor dr = (Doctor) Session.getCurrentUser();
        Patient p = comboPatients.getValue();
        Bed selectedBed = comboBED.getValue();
        LocalDate date = datePicker.getValue();
        String note = txtNote.getText();
        
        if(dr != null && p != null && date != null && selectedBed != null) {
            try {
                // Does the patient already have a bed?
                if (DatabaseManager.hasBed(p.getId())) {
                    showAlert("Admission Error", "Patient already has an assigned bed.");
                    return;
                }

                // Perform the Appointment Booking
                String finalNote = "Emergency: " + dr.getSpecialty() + " " + (note.length() > 0 ? note : "");
                DatabaseManager.bookAppointment(p.getId(), dr.getId(), date.toString(), "08:00 AM", finalNote);
                
                // Perform the Bed Assignment
                DatabaseManager.assignBed(selectedBed.getId(), p.getId());
                
                System.out.println("Emergency booking and bed assignment successful.");
                ((javafx.stage.Stage)comboPatients.getScene().getWindow()).close();
                
            } catch(SQLException ex) {
                ex.printStackTrace();
            }
            
            
            
            
        } else {
            showAlert("Missing Information", "Please select a patient, bed, and date.");
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
