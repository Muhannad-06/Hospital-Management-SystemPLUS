package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class WelcomeScreen extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		DatabaseManager.connect();
		StageManager.setPrimaryStage(primaryStage);
		
		primaryStage.setResizable(false);
		primaryStage.setTitle("Hospital Management System");
	
		Parent root = FXMLLoader.load(getClass().getResource("FXMLs/welcomeScreen.fxml"));
		Scene scene = new Scene(root);
			
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/application/image/logo.png")));
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}