package model;

//This class helps calculate BMR and target calories
public class CaloriesCalculator {

    // This method calculates the BMR (Basal Metabolic Rate) based on gender
    public double calculateBMR(User user) {
        if (user.getGender().equalsIgnoreCase("erkek")) {
            // Formula for male
            return 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() + 5;
        } else {
            // Formula for female
            return 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() - 161;
        }
    }

    // This method calculates daily calorie target using BMR and activity level
    public double calculateTarget(User user, int goalChoice, double activityFactor) {
        double bmr = calculateBMR(user); // First calculate BMR
        double maintenanceCalories = bmr * activityFactor; // Calories to stay at current weight

        // Change calories based on user's goal
        return switch (goalChoice) {
            case 1 -> maintenanceCalories - 500; // Lose weight
            case 2 -> maintenanceCalories + 500; // Gain weight
            case 3 -> maintenanceCalories;       // Maintain weight
            default -> -1; // Invalid choice
        };
    }
}
