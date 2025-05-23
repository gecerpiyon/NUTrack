package model;

// This class stores one workout plan entry for a user
public class WorkoutPlanEntry {
    // Unique ID for the entry
    private int id;
    // User ID who owns this workout
    private int userId;
    // Day of the week for the workout
    private String day;
    // Body part that will be trained
    private String bodyPart;
    // List of exercises for that day
    private String exercises;

    // Constructor with ID (used when loading from database)
    public WorkoutPlanEntry(int id, int userId, String day, String bodyPart, String exercises) {
        this.id = id;
        this.userId = userId;
        this.day = day;
        this.bodyPart = bodyPart;
        this.exercises = exercises;
    }

    // Constructor without ID (for new entries)
    public WorkoutPlanEntry(int userId, String day, String bodyPart, String exercises) {
        this(0, userId, day, bodyPart, exercises); // id = 0
    }

    // Getter for ID
    public int getId() { return id; }

    // Getter for user ID
    public int getUserId() { return userId; }

    // Getter for workout day
    public String getDay() { return day; }

    // Getter for body part
    public String getBodyPart() { return bodyPart; }

    // Getter for exercises
    public String getExercises() { return exercises; }

    // Setter for workout day
    public void setDay(String day) { this.day = day; }

    // Setter for body part
    public void setBodyPart(String bodyPart) { this.bodyPart = bodyPart; }

    // Setter for exercises
    public void setExercises(String exercises) { this.exercises = exercises; }
}
