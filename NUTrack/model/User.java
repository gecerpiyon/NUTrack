package model;

import java.util.Scanner;

// This class represents a user and extends Person class
public class User extends Person {
    // Username of the user
    private String username;
    // Password of the user
    private String password;
    // Height in centimeters
    private double height;
    // Weight in kilograms
    private double weight;
    // User's daily target calorie value
    private double targetCalories;

    // Constructor to create a user with all info
    public User(int id, String name, int age, String gender, String username, String password, double height, double weight) {
        super(id, name, age, gender);
        this.username = username;
        this.password = password;
        this.height = height;
        this.weight = weight;
    }

    // Getter and setter for username
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // Getter and setter for password
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Getter and setter for height
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    // Getter and setter for weight
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    // Getter and setter for targetCalories
    public double getTargetCalories() {
        return targetCalories;
    }
    public void setTargetCalories(double targetCalories) {
        this.targetCalories = targetCalories;
    }

    // This method gets user input from console and saves user in database
    public void register(DBManager db) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Ad: ");
        setName(scan.nextLine());

        System.out.print("Yaş: ");
        setAge(scan.nextInt()); scan.nextLine();

        System.out.print("Cinsiyet: ");
        setGender(scan.nextLine());

        System.out.print("Kullanıcı adı: ");
        setUsername(scan.nextLine());

        System.out.print("Şifre: ");
        setPassword(scan.nextLine());

        System.out.print("Boy (cm): ");
        setHeight(scan.nextDouble());

        System.out.print("Kilo (kg): ");
        setWeight(scan.nextDouble());

        // Try to save user in database
        boolean result = db.insertUser(this);
        System.out.println(result ? "Kayıt başarılı!" : "Kayıt başarısız.");
    }

    // This method logs in the user by checking the database
    public User login(DBManager db) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Kullanıcı adı: ");
        String uname = scan.nextLine();

        System.out.print("Şifre: ");
        String pass = scan.nextLine();

        // Return user if username and password match
        return db.validateLogin(uname, pass);
    }
}


