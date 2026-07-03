package application.controllers;



import application.AestheticAnimations;
import application.DatabaseManager;
import application.StageManager;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class controllerWelcomeScreen {
	
	@FXML
	private RadioButton btnNeuro, btnCardio, btnOrtho, btnCancer;
	
	@FXML
	private Label txtClinic, txtCounterPatient, txtCounterBed;
	
	// I will use the static trick to give the welcome screen interactive and dynamic KPIs
	public static controllerWelcomeScreen instance;
	
	@FXML private Pane rootPane;
	@FXML private Button btnFollowUp, btnDoctors, btnRegister;
	
	private Timeline typewriterTimeline;
	
	
	@FXML
	private void clinicDESC(ActionEvent e) {
	// Give the Welcome Screen Dynamic interactions For Aesthetic Purposes 
		String fullText = "";
		if(btnCardio.isSelected()) {
			fullText = btnCardio.getText();
		} else if(btnNeuro.isSelected()) {
			fullText = btnNeuro.getText();
		} else if(btnOrtho.isSelected()) {
			fullText = btnOrtho.getText();
		} else if(btnCancer.isSelected()) {
			fullText = btnCancer.getText();
		}
		animateTextTypewriter(fullText);
	}
	
	@FXML
    public void initialize() {
		instance = this;
        // Fetch the count from our DatabaseManager
		int countPatient = DatabaseManager.getPatientCount();
        int countBed = DatabaseManager.getFreeBedCount();
        // Update the label text
        txtCounterPatient.setText(countPatient+"");
        txtCounterBed.setText(countBed+"");
        DatabaseManager.autoCancelPastAppointments();
        // Animations 
        AestheticAnimations.startHeartbeat(rootPane, "#ff0033");
        AestheticAnimations.applyGlassGlowAnimation(btnRegister);  
        AestheticAnimations.applyGlassGlowAnimation(btnFollowUp);  
        AestheticAnimations.applyGlassGlowAnimation(btnDoctors);
       
    }

    @FXML
    private void btnFollowUp(ActionEvent event) {
        System.out.println("Opening Follow-Up Login...");
        // Call the manager to open the specific FXML for this choice
        StageManager.openModalWindow("FXMLs/login.fxml", "Patient Follow-Up");
    }

    @FXML
    private void btnRegister(ActionEvent event) {
        System.out.println("Opening Registration...");
        StageManager.openModalWindow("FXMLs/register.fxml", "Patient Registration");
    }

    @FXML
    private void btnDoctors(ActionEvent event) {
        System.out.println("Opening Doctors Portal...");
        StageManager.openModalWindow("FXMLs/doctors.fxml", "Doctor Directory");
    }
    
    private void animateTextTypewriter(String targetText) {
		// If a previous text is still typing this will stop it immediately before starting the new one
		if (typewriterTimeline != null) {
			typewriterTimeline.stop();
		}
		
		// Clear the current text completely
		txtClinic.setText("");
		
		// ======> Ai Assisted here
		// We use a mutable container to track how many characters have been written across frames
		final StringBuilder wrapper = new StringBuilder();
		
		typewriterTimeline = new Timeline();
		typewriterTimeline.setCycleCount(targetText.length()); // Loop exactly as many times as there are characters
		
		// execute every 30 milliseconds as this is the clean reading speed
		KeyFrame frame = new KeyFrame(Duration.millis(30), event -> {
			int currentLength = wrapper.length();
			if (currentLength < targetText.length()) {
				// Append the next single character from our original text
				wrapper.append(targetText.charAt(currentLength));
				// Update the UI label display
				txtClinic.setText(wrapper.toString());
			}
		});
		
		typewriterTimeline.getKeyFrames().add(frame);
		typewriterTimeline.play();
	}
    
}