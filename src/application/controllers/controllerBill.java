package application.controllers;

import application.DatabaseManager;
import application.Session;
import application.models.Patient;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class controllerBill {

    @FXML private Label lblName, lblID, lblAppCount, lblTotal, lblCheckECG, lblCheckXray, lblCheckOnco, lblCheckMri, lblCheckBed, lblAppsPrice;
    @FXML private Button btnPayBill;
    @FXML private CheckBox chkECG, chkXray, chkOnco, chkMri, chkBed;
    
    private final double ECG_PRICE = 150.0;
    private final double XRAY_PRICE = 300.0;
    private final double ONCOLOGY_PRICE = 500.0;
    private final double MRI_PRICE = 800.0;
    private final double BED_PRICE = 500.0;
    private final double Appointment_PRICE = 250.0;
    private final double Canceled_Appointment_PRICE = 100;
    
    private int NumAppointments = 0;
    private int CanceledApp = 0;
    private double AppoinmentsCost = 0;
    double total = 0;
    @FXML
    public void initialize() {

        Patient p = (Patient) Session.getCurrentUser();

        if (p != null) {
            lblName.setText(p.getName());
            lblID.setText(p.getId() + "");
            NumAppointments = DatabaseManager.getDoneAppointmentCountByPatient(p.getId());
            CanceledApp = DatabaseManager.getCanceledAppointmentCountByPatient(p.getId());
            lblAppCount.setText( (NumAppointments + CanceledApp) + "");
            AppoinmentsCost = (Appointment_PRICE * NumAppointments) + (CanceledApp * Canceled_Appointment_PRICE) ;
            lblAppsPrice.setText(AppoinmentsCost + "");
            clearPriceLabels();
            setupCheckBoxListeners();
            updateTotalCost();
        } else {
            System.out.println("Error: No patient is currently logged in.");
        }
        
    }
    
    private void clearPriceLabels() {
        lblCheckECG.setText("0 EGP");
        lblCheckXray.setText("0 EGP");
        lblCheckOnco.setText("0 EGP");
        lblCheckMri.setText("0 EGP");
        lblCheckBed.setText("0 EGP");
    }
    
    private void setupCheckBoxListeners() {
        
        chkECG.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblCheckECG.setText(ECG_PRICE + " EGP");
            } else {
                lblCheckECG.setText("0 EGP");
            }
            updateTotalCost();
        });
        
        chkXray.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblCheckXray.setText(XRAY_PRICE + " EGP");
            } else {
                lblCheckXray.setText("0 EGP");
            }
            updateTotalCost();
        });

        chkOnco.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblCheckOnco.setText(ONCOLOGY_PRICE + " EGP");
            } else {
                lblCheckOnco.setText("0 EGP");
            }
            updateTotalCost();
        });

        chkMri.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblCheckMri.setText(MRI_PRICE + " EGP");
            } else {
                lblCheckMri.setText("0 EGP");
            }
            updateTotalCost();
        });

        chkBed.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblCheckBed.setText(BED_PRICE + " EGP");
            } else {
                lblCheckBed.setText("0 EGP");
            }
            updateTotalCost();
        });
    }

    private void updateTotalCost() {
        total = AppoinmentsCost;

        if (chkECG.isSelected()) total += ECG_PRICE;
        if (chkXray.isSelected()) total += XRAY_PRICE;
        if (chkOnco.isSelected()) total += ONCOLOGY_PRICE;
        if (chkMri.isSelected()) total += MRI_PRICE;
        if (chkBed.isSelected()) total += BED_PRICE;

        lblTotal.setText(total + " EGP");
    }
    
    @FXML
    private void handlePayBill(ActionEvent event) throws SQLException {
        Patient p = (Patient) Session.getCurrentUser();
       
        String ecg = chkECG.isSelected() ? "YES" : "NO";
        String xray = chkXray.isSelected() ? "YES" : "NO";
        String oncology = chkOnco.isSelected() ? "YES" : "NO";
        String mri = chkMri.isSelected() ? "YES" : "NO";
        String bed = chkBed.isSelected() ? "YES" : "NO"; 
        
        String issuedDate = java.time.LocalDate.now().toString();
        
        DatabaseManager.saveBill(p.getId(), ecg, xray, oncology, mri, bed, NumAppointments, total, issuedDate, "Paid");
        
        int Bed_id = DatabaseManager.getBedId(p.getId());
        if (Bed_id == -1);
        else
  
        DatabaseManager.releaseBed(Bed_id);
        DatabaseManager.updateAppointmentsToPaid(p.getId());
        
        
        System.out.println("Bill paid successfully!");
        Stage stage = (Stage) btnPayBill.getScene().getWindow();
        stage.close();
    }
}