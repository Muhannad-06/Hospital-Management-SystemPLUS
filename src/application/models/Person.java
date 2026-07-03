package application.models;

public abstract class Person {
    protected int id;
    protected String name;
    protected String gender;

    public Person(int id, String name, String gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public String getGender() { return gender; }

    public abstract String getRole(); 
    
    //Will be used to display the name in the dashboards
    @Override
    public String toString() {
        return this.name; 
    }
}