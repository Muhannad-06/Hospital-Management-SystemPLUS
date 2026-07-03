package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class StageManager {
    
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
    

    public static void closePrimaryStage() {
        if (primaryStage != null) {
            primaryStage.close();
        }
    }
    
    public static void openModalWindow(String fxmlPath, String title) {
        try {
            
            FXMLLoader loader = new FXMLLoader(StageManager.class.getResource("/application/" + fxmlPath));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle(title);
            
            // This blocks interaction with the Welcome Screen
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.initOwner(primaryStage);
            
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            
            // I removed this as it was causing a crash and Errors  ==> Muhannad
            // showAndWait pauses the code here until the popup is closed            
            //stage.showAndWait();
            
            stage.getIcons().add(new Image(StageManager.class.getResourceAsStream("/application/image/logo.png")));
            
            stage.show();
            
            
        } catch (IOException e) {
            System.err.println("Error: Could not find FXML file at " + fxmlPath);
            e.printStackTrace();
        }
    }
    
    public static void logout(Stage currentStage) {
        try {
            // Clear the session for safety
            Session.setCurrentUser(null);
            
            // Close the dashboard
            currentStage.close();
            
            // Load the Welcome Screen
            FXMLLoader loader = new FXMLLoader(StageManager.class.getResource("/application/FXMLs/welcomeScreen.fxml"));
            Parent root = loader.load();
            
            Stage welcomeStage = new Stage();
            welcomeStage.setTitle("Hospital Management System");
            welcomeStage.setResizable(false);
            welcomeStage.setScene(new Scene(root));
            
            // Reset the primary stage reference in StageManager
            setPrimaryStage(welcomeStage);
            
           // welcomeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}