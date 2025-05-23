package model;

// This is an abstract class to represent a person
public abstract class Person {
    // Person's ID
    private int id;
    // Person's name
    private String name;
    // Person's age
    private int age;
    // Person's gender
    private String gender;

    // Constructor to set all fields when creating a person
    public Person(int id, String name, int age, String gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    // Getter for ID
    public int getId() { return id; }

    // Setter for ID
    public void setId(int id) { this.id = id; }

    // Getter for name
    public String getName() { return name; }

    // Setter for name
    public void setName(String name) { this.name = name; }

    // Getter for age
    public int getAge() { return age; }

    // Setter for age
    public void setAge(int age) { this.age = age; }

    // Getter for gender
    public String getGender() { return gender; }

    // Setter for gender
    public void setGender(String gender) { this.gender = gender; }

    // Method to print person's info to the console
    public void getInfo() {
        System.out.println("ID: " + id);
        System.out.println("Ad: " + name);
        System.out.println("Yaş: " + age);
        System.out.println("Cinsiyet: " + gender);
    }
}
