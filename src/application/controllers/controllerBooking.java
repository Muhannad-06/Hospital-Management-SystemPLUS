package application.controllers;

import application.DatabaseManager;
import application.Session;
import application.models.Doctor;
import application.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.time.LocalDate;

public class controllerBooking {

    @FXML private ComboBox<Doctor> comboDoctors;
    @FXML private DatePicker datePicker;
    @FXML private Label lblClinicTitle;

    private String currentSpecialty;

    public void setSpecialty(String specialty) {
        this.currentSpecialty = specialty;
        lblClinicTitle.setText(specialty + " Reservation");
        // Filter doctors immediately based on the clinic clicked
        comboDoctors.setItems(DatabaseManager.getDoctorsBySpecialty(specialty));
    }

    @FXML
    private void btnConfirmBooking(ActionEvent event) {
        Patient p = (Patient) Session.getCurrentUser(); //
        Doctor d = comboDoctors.getValue();
        LocalDate date = datePicker.getValue();

        if (d != null && date != null) {
            try {
                //
                DatabaseManager.bookAppointment(p.getId(), d.getId(), date.toString(), "10:00 AM", "Pending");
                
                // Close the booking window after success
                ((javafx.stage.Stage)comboDoctors.getScene().getWindow()).close();
                
                System.out.println("Appointment booked with Dr. " + d.getName() + " on " + date);
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}