package application.models;

public class Doctor extends Person {
    private String specialty;

    public Doctor(int id, String name, String gender, String specialty) {
        super(id, name, gender);
        this.specialty = specialty;
    }

    public String getSpecialty() { return specialty; }

    @Override
    public String getRole() { return "Doctor"; }

    @Override
    public String toString() {
        return name + " (" + specialty + ")";
    }
}