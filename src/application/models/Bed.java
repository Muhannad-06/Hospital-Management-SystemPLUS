package application.models;

public class Bed {
    private int id;
    private String bedNumber;
    private String clinicName;
    private Patient occupant; // Composition: The bed "has a" patient

    public Bed(int id, String bedNumber, String clinicName, Patient occupant) {
        this.id = id;
        this.bedNumber = bedNumber;
        this.clinicName = clinicName;
        this.occupant = occupant;
    }

    // Helper method to check status
    public boolean isOccupied() {
        return occupant != null;
    }

    public String getOccupantName() {
        return (occupant != null) ? occupant.getName() : "Available";
    }

    // Standard Getters
    public String getBedNumber() { return bedNumber; }
    public String getClinicName() { return clinicName; }
    public int getId() {  return id; }
    
    @Override
    public String toString() {
        return this.bedNumber; // This forces the ComboBox to show the actual name
    }
    
}