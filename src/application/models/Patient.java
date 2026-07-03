package application.models;

public class Patient extends Person {
	private String dob;
    private double height;
    private double weight;
    private String bloodType;
    private String chronicDisease;

    public Patient(int id, String name, String gender, String dob, double height, double weight, String bloodType, String chronicDisease) {
        super(id, name, gender);
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.bloodType = bloodType;
        this.chronicDisease = chronicDisease;
    }

    
    //Setter for More interactive Result instead of restarting the program
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    
    // Getters for the new fields
    public String getDob() { return dob; }
    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public String getBloodType() { return bloodType; }
    public String getChronicDisease() { return chronicDisease; }

    @Override
    public String getRole() { return "Patient"; }
}