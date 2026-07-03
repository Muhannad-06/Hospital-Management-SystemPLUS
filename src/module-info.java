module HospitalManagmentApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Required for SQLite
    requires org.xerial.sqlitejdbc;
	requires javafx.base;
	requires javafx.graphics;

	opens application.controllers to javafx.fxml;
	opens application.FXMLs to javafx.fxml;
	
	exports application;
}