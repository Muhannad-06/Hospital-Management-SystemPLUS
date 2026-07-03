package application.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import application.AestheticAnimations;
import application.DatabaseManager;
import application.Session;
import application.StageManager;
import application.models.Doctor;
import application.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class controllerDoctorDashboard {
	
	@FXML
	public Button btnUrgentAppointment, btnExit, update;
	
	@FXML
	public Label txtDrName, txtAppointmentCounter, txtPatientNextAppointment, txtDrNextAppointment, txtDOB, txtGender, txtBloodType, txtWeight, txtTasksDone,txtHeight, txtPatientID, txtBednum, txtChronic, txtToday ; 
	
	@FXML
	public TextArea txtDiagnosis;
	
	@FXML
	public ComboBox<Patient> comboBoxPatients;

	@FXML
	public TextField txtECG, txtXray, txtOnc, txtMRI;
	
	@FXML
	public CheckBox checkPatient1, checkPatient2, checkPatient3, checkPatient4, checkPatient5, checkPatient6, checkPatient7;
	
	private int tasksDoneCount = 0;
	
	public void initialize() {
		
		Doctor dr = (Doctor) Session.getCurrentUser();
		
		if(dr != null) {
			
			txtDrName.setText(dr.getName());
			
			try(ResultSet rs = DatabaseManager.getNearestAppointment(dr.getId(), true)){
				if(rs.next()) {
					txtDrNextAppointment.setText(rs.getString("date"));
					
				}else {   System.out.println("No Assigned Appointments");      }
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			
			int appCount = DatabaseManager.getPendingAppointmentCountByDoctor(dr.getId());
			txtAppointmentCounter.setText(appCount+"");
			
			comboBoxPatients.setItems(DatabaseManager.getAllPatientsByDoctor(dr.getId()));
			
			comboBoxPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
	            if (newVal != null) {
	                updatePatientUI(newVal);
	            }
	        });
			
			
			
			CheckBox[] checkboxes = { checkPatient1, checkPatient2, checkPatient3, checkPatient4, checkPatient5, checkPatient6, checkPatient7 };
			for(CheckBox cb : checkboxes) {
				cb.setVisible(false);
			}
			
			
			
			javafx.collections.ObservableList<Patient> sortedPatients = DatabaseManager.getPatientsByDoctor(dr.getId());
			
			for(CheckBox cb : checkboxes) {
				cb.setVisible(false);
			}
			
			for( int i=0 ; i < sortedPatients.size() && i < checkboxes.length ; i++ ) {
				Patient p = sortedPatients.get(i);
				checkboxes[i].setVisible(true);
				checkboxes[i].setText(p.getName());
				CheckBox cb = checkboxes[i];
				cb.setSelected(false);
				
				cb.setOnAction(event -> {
			        if (cb.isSelected()) {
			            try {
			                //  Find the appointment ID
			                int appId = DatabaseManager.getScheduledAppointmentId(p.getId(), dr.getId());
			                
			                if (appId != -1) {
			                    // Update status to 'Completed' 
			                    DatabaseManager.finishTreatment(appId, "Standard Checkup Completed");
			                    
			                    // Update the KPI Counter
			                    tasksDoneCount++;
			                    txtTasksDone.setText(String.valueOf(tasksDoneCount));
			                    
			                    // Update the "Pending Appointments" counter
			                    int newAppCount = Integer.parseInt(txtAppointmentCounter.getText()) - 1;
			                    txtAppointmentCounter.setText(String.valueOf(Math.max(0, newAppCount)));
			                    
			                    // Disable the checkbox so it can't be unchecked
			                    cb.setDisable(true);
			                    System.out.println("Treatment finished for: " + p.getName());
			                }
			            } catch (SQLException ex) {
			                ex.printStackTrace();
			            }
			        }
			    });
				
			}
			
			 LocalDate today = LocalDate.now();
			 String dayName = today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			 txtToday.setText(dayName + "");
			
			 
			 
			 DatabaseManager.autoCancelPastAppointments();

			 AestheticAnimations.applyGlassGlowAnimation(update);
			 AestheticAnimations.applyGlassGlowAnimation(btnUrgentAppointment);
			 AestheticAnimations.applyGlassGlowAnimation(btnExit);
		}
	} // ======================> End of Initialize
	
	
	private void updatePatientUI(Patient p) {
	    // Basic Info
	    txtPatientID.setText("#" + p.getId());
	    txtDOB.setText(p.getDob());
	    txtGender.setText(p.getGender());
	    txtWeight.setText(p.getWeight() + " kg");
	    txtHeight.setText(p.getHeight() + " cm");
	    txtBloodType.setText(p.getBloodType() == null ? "Pending" : p.getBloodType());
	    txtChronic.setText(p.getChronicDisease());

	    //  Clear fields
	    txtECG.clear();
	    txtXray.clear();
	    txtOnc.clear();
	    txtMRI.clear();
	    txtDiagnosis.clear();
	    
	    String bedSql = "SELECT bed_number FROM beds WHERE patient_id = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(bedSql)) {
            ps.setInt(1, p.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	txtBednum.setText(rs.getString("bed_number"));
            } else {
            	txtBednum.setText("Not Assigned");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

	    // Fetch medical records
	    String sql = "SELECT ecg, x_ray, oncology, mri, diagnosis FROM patients WHERE id = ?";
	    try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
	        ps.setInt(1, p.getId());
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            txtECG.setText(rs.getString("ecg") == null ? "" : rs.getString("ecg"));
	            txtXray.setText(rs.getString("x_ray") == null ? "" : rs.getString("x_ray"));
	            txtOnc.setText(rs.getString("oncology") == null ? "" : rs.getString("oncology"));
	            txtMRI.setText(rs.getString("mri") == null ? "" : rs.getString("mri"));
	            txtDiagnosis.setText(rs.getString("diagnosis") == null ? "" : rs.getString("diagnosis"));
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }

	    // Fetch Next Appointment for this specific patient
	    try(ResultSet rs = DatabaseManager.getNearestAppointment(p.getId(), false)){
	        if(rs.next()) {
	            txtPatientNextAppointment.setText(rs.getString("date"));
	        } else {
	            txtPatientNextAppointment.setText("None");
	        }
	    } catch(SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	
	
	@FXML
	private void handleSaveRecords(ActionEvent e) {
	    Patient p = comboBoxPatients.getValue();
	    if (p != null) {
	        try {
	            
	            DatabaseManager.updateMedicalRecords(
	                p.getId(),
	                txtECG.getText(),
	                txtXray.getText(),
	                txtOnc.getText(),
	                txtMRI.getText(),
	                txtDiagnosis.getText()
	            );
	            System.out.println("Medical records updated for: " + p.getName());
	            
	            
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	
	
	public void btnUrgentAPP(ActionEvent e){
		
		try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/application/FXMLs/emergencyBooking.fxml"));
            javafx.scene.Parent root = loader.load();

           
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Emergency Reservation");
            
            
            
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL); 
            
            stage.setScene(new javafx.scene.Scene(root));
            
            //  fit the FXML 
            stage.sizeToScene(); 
            stage.setResizable(false);
            
            
            stage.show();

        } catch (Exception ex) {
            System.err.println("Error opening booking window:");
            ex.printStackTrace();
        }
		
	}
	
	public void btnEXIT(ActionEvent e) {
		controllerWelcomeScreen.instance.initialize();
		Stage currentStage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
    	StageManager.logout(currentStage);
	}
	
	
}