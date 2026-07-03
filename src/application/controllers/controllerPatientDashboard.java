package application.controllers;

import java.sql.*;
import java.util.Random;
import application.DatabaseManager;
import application.AestheticAnimations;
import application.BloodType;
import application.Session;
import application.StageManager;
import application.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class controllerPatientDashboard {

    @FXML
    private Label txtPatientName, txtDOB, txtGender, txtBloodType, txtWeight, txtHeight, txtPatientID, txtBedID, txtChronic, txtECG, txtXRay, txtOncology, txtMRI, txtDiagnosis, txtNextAppointment;
    
    @FXML
    private Button btnBloodCheck, btnOrtho, btnCardio, btnNeuro, btnCancer, btnExit, btnAIChat, payBill;
    
    @FXML private Pane rootPane;
    
    BloodType type;

    // This runs automatically when the Dashboard opens
    @FXML
    public void initialize() {
        // Pull the logged-in patient from the global session
        Patient p = (Patient) Session.getCurrentUser();

        if (p != null) {
            // Fill labels using the Patient object's data
            txtPatientName.setText(p.getName());
            txtPatientID.setText("#" + p.getId());
            txtWeight.setText(p.getWeight() + " kg");
            txtHeight.setText(p.getHeight() + " cm");
            txtChronic.setText(p.getChronicDisease());
            txtDOB.setText(p.getDob());
            txtGender.setText(p.getGender());
            
            try(ResultSet rs = DatabaseManager.getNearestAppointment(p.getId(), false)){
            	if(rs.next()) {
            		String date = rs.getString("date");
            		txtNextAppointment.setText(date);
            	}else {
            		txtNextAppointment.setText("No Upcoming Appointments");
            	}
            }catch (SQLException ex) {
                    ex.printStackTrace();
                    txtNextAppointment.setText("Error loading schedule");
                }
            
            String sql = "SELECT ecg, x_ray, oncology, mri, diagnosis FROM patients WHERE id = ?";
    	    try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
    	        ps.setInt(1, p.getId());
    	        ResultSet rs = ps.executeQuery();

    	        if (rs.next()) {
    	            // Set text if data exists, otherwise keep it blank ("")
    	            txtECG.setText(rs.getString("ecg") == null ? "" : rs.getString("ecg"));
    	            txtXRay.setText(rs.getString("x_ray") == null ? "" : rs.getString("x_ray"));
    	            txtOncology.setText(rs.getString("oncology") == null ? "" : rs.getString("oncology"));
    	            txtMRI.setText(rs.getString("mri") == null ? "" : rs.getString("mri"));
    	            txtDiagnosis.setText(rs.getString("diagnosis") == null ? "" : rs.getString("diagnosis"));
    	        }
    	    } catch (SQLException ex) {
    	        ex.printStackTrace();
    	    }
           
    	    DatabaseManager.autoCancelPastAppointments();
    	    // Convert The DNA to red to alert the USER
    	    if(DatabaseManager.hasEmergencyAppointment(p.getId())) {
    	    	AestheticAnimations.startDNARotation(rootPane, 300, 150, "#ff0033", Color.RED);
    	    }else {
    	    	AestheticAnimations.startDNARotation(rootPane, 300, 150, "#00ffff", Color.WHITE);
    	    }
            
            }
            
            // Handle the case where blood type is still null in DB
            String blood = p.getBloodType();
            txtBloodType.setText((blood == null || blood.isEmpty()) ? "Pending" : blood);
        
        String bedSql = "SELECT bed_number FROM beds WHERE patient_id = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(bedSql)) {
            ps.setInt(1, p.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtBedID.setText(rs.getString("bed_number"));
            } else {
                txtBedID.setText("Not Assigned");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        // Animations
        AestheticAnimations.applyGlassGlowAnimation(btnBloodCheck);  
        AestheticAnimations.applyGlassGlowAnimation(btnOrtho);  
        AestheticAnimations.applyGlassGlowAnimation(btnCardio);
        AestheticAnimations.applyGlassGlowAnimation(btnNeuro);  
        AestheticAnimations.applyGlassGlowAnimation(btnOrtho);  
        AestheticAnimations.applyGlassGlowAnimation(btnCancer);
        AestheticAnimations.applyGlassGlowAnimation(payBill);  
        AestheticAnimations.applyGlassGlowAnimation(btnAIChat);
        AestheticAnimations.applyGlassGlowAnimation(btnExit);
        
        
        
    } // ==================> End initialize

    @FXML
    private void handleBloodCheck(ActionEvent e) {
        
    	
    	Patient p = (Patient) Session.getCurrentUser(); 
    	
        // Check if blood type is already known
    	if (p.getBloodType() != null && !p.getBloodType().equals("Pending")) {
    		
    	    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
    	    alert.setTitle("Medical Record Exists");
    	    alert.setHeaderText("Blood Type Confirmed");
    	    // FIX: Create a manual Label to bypass the bug ===> AI Suggested that as there was a Bug That the Box's Content is Hidden 
    	    javafx.scene.control.Label label = new javafx.scene.control.Label("Patient blood type is already: " + p.getBloodType());
    	    label.setWrapText(true);
    	    label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;"); // Ensure text isn't white-on-white
    	    
    	    alert.getDialogPane().setContent(label);
    	    alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
    	    
    	    alert.showAndWait();
    	    return; 
    	}
       
        System.out.println("Check in Progress....");
        
        BloodType[] bloodtypes = BloodType.values();
        
        BloodType checkResult = bloodtypes[new Random().nextInt(bloodtypes.length)];
        
        String sql = "UPDATE patients SET blood_type = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
               ps.setString(1, checkResult.name());
               ps.setInt(2, p.getId());
               ps.executeUpdate();
               p.setBloodType(checkResult.name());
               txtBloodType.setText(checkResult.name());
               System.out.println("BloodType updated to " + checkResult.name() + " for Patient: " + p.getName());
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
    }

    private void openBooking(String specialty) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/FXMLs/booking.fxml"));
            Parent root = loader.load();

            // Pass the specialty data to the controller
            controllerBooking bookingController = loader.getController();
            bookingController.setSpecialty(specialty);

            Stage stage = new Stage();
            stage.setTitle(specialty + " Reservation");
            
            // Link the popup to the Dashboard window so it stays on top
            stage.initOwner(btnCardio.getScene().getWindow()); 
            
            // Set modality to block the dashboard while booking
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL); 
            
            stage.setScene(new Scene(root));
            
            // Force the window to fit the FXML size 
            stage.sizeToScene(); 
            stage.setResizable(false);
            
            // To maintain proper focus
            stage.showAndWait();

        } catch (Exception e) {
            System.err.println("Error opening booking window:");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCardio(ActionEvent e) { openBooking("Cardiology"); }

    @FXML
    private void handleNeuro(ActionEvent e) { openBooking("Neurology"); }

    @FXML
    private void handleOrth(ActionEvent e) { openBooking("Orthopedics"); }

    @FXML
    private void handleCancer(ActionEvent e) { openBooking("Cancer Care"); }
    
    @FXML
    private void handleAIChat(ActionEvent e) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/application/FXMLs/aiChat.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Emergency Doctor Consultant");
            stage.initOwner(btnAIChat.getScene().getWindow());
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception ex) {
            System.err.println("Error opening AI Chat window:");
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void handlepayBill(ActionEvent e) {
    	
    	try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/application/FXMLs/bill.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Bill");
            stage.initOwner(btnAIChat.getScene().getWindow());
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception ex) {
            System.err.println("Error opening AI Chat window:");
            ex.printStackTrace();
        }
    	
    }

    @FXML
    
    private void handleExit(ActionEvent e) {
    	
    	Stage currentStage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
    	StageManager.logout(currentStage);
    	
    	
    }


} 