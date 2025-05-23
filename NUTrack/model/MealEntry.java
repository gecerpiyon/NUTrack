package model;

import java.util.Date;

//This class holds information about a meal entry
public class MealEntry implements Calculable {

    // Fields for meal information
    private int id;
    private int userId;
    private Date date;
    private String mealName;
    private double gram;
    private int calories;
    private boolean isCustomEntry = false; // true if user entered calorie manually

    // Getter for ID
    public int getId() {
        return id;
    }

    // Setter for ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for user ID
    public int getUserId() {
        return userId;
    }

    // Setter for user ID
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter for date
    public Date getDate() {
        return date;
    }

    // Setter for date
    public void setDate(Date date) {
        this.date = date;
    }

    // Getter for meal name
    public String getMealName() {
        return mealName;
    }

    // Setter for meal name
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    // Getter for gram
    public double getGram() {
        return gram;
    }

    // Setter for gram
    public void setGram(double gram) {
        this.gram = gram;
    }

    // Getter for custom entry flag
    public boolean isCustomEntry() {
        return isCustomEntry;
    }

    // Setter for custom entry flag
    public void setCustomEntry(boolean isCustomEntry) {
        this.isCustomEntry = isCustomEntry;
    }

    // Setter for calories
    public void setCalories(int calories) {
        this.calories = calories;
    }

    // Constructor without custom calories
    public MealEntry(int id, int userId, Date date, String mealName, double gram) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.mealName = mealName;
        this.gram = gram;
    }

    // Constructor with custom calories
    public MealEntry(int id, int userId, Date date, String mealName, double gram, int customCalories) {
        this(id, userId, date, mealName, gram);
        this.calories = customCalories;
        this.isCustomEntry = true;
    }

    // Returns calories depending on if it's custom or calculated
    public int getCalories() {
        if (isCustomEntry) return calories;
        return calculateCalories();
    }

    // Calculates calories based on food type and grams
    @Override
    public int calculateCalories() {
        String meal = mealName.toLowerCase();
        int caloriesPer100g;

        // Each case has a known calorie per 100g
        switch (meal) {
            case "pilav": caloriesPer100g = 130; break;
            case "kebap": caloriesPer100g = 250; break;
            case "çorba": caloriesPer100g = 70; break;
            case "salata": caloriesPer100g = 40; break;
            case "hamburger": caloriesPer100g = 295; break;
            case "pizza": caloriesPer100g = 266; break;
            case "yumurta": caloriesPer100g = 155; break;
            case "makarna": caloriesPer100g = 131; break;
            case "menemen": caloriesPer100g = 90; break;
            case "döner": caloriesPer100g = 215; break;
            case "lahmacun": caloriesPer100g = 210; break;
            case "börek": caloriesPer100g = 290; break;
            case "simit": caloriesPer100g = 260; break;
            case "balık": caloriesPer100g = 180; break;
            case "köfte": caloriesPer100g = 240; break;
            case "tavuk": caloriesPer100g = 165; break;
            case "et": caloriesPer100g = 250; break;
            case "patates kızartması": caloriesPer100g = 312; break;
            case "çiğ köfte": caloriesPer100g = 170; break;
            case "mantı": caloriesPer100g = 190; break;
            default: caloriesPer100g = 200; // default value if unknown
        }

        // Calculate total calories based on gram amount
        return (int) ((gram / 100.0) * caloriesPer100g);
    }
}
