package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//This class handles all database operations
public class DBManager {
    // Database connection info
    private final String url = "jdbc:mysql://localhost:3306/fitnessapp";
    private final String user = "root";
    private final String password = "+ooP4.Jatry-";

    // This method connects to the database
    public Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Veritabanına bağlandı.");
            return conn;
        } catch (SQLException e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
            return null;
        }
    }

    // This method adds a new user to the database
    public boolean insertUser(User u) {
        String sql = "INSERT INTO users (name, age, gender, username, password, height, weight) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getName());
            stmt.setInt(2, u.getAge());
            stmt.setString(3, u.getGender());
            stmt.setString(4, u.getUsername());
            stmt.setString(5, u.getPassword());
            stmt.setDouble(6, u.getHeight());
            stmt.setDouble(7, u.getWeight());

            stmt.executeUpdate();
            System.out.println("Kayıt veritabanına eklendi.");
            return true;

        } catch (SQLException e) {
            System.out.println("Kayıt başarısız: " + e.getMessage());
            return false;
        }
    }

    // This method checks if username and password match a user
    public User validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getDouble("height"),
                    rs.getDouble("weight")
                );

                // Set user's target calories
                user.setTargetCalories(rs.getDouble("target_calories"));

                return user;
            }

        } catch (SQLException e) {
            System.out.println("Giriş hatası: " + e.getMessage());
        }

        return null;
    }

    // This method returns all meals added by the user
    public List<MealEntry> getMealEntriesByUserId(int userId) {
        List<MealEntry> meals = new ArrayList<>();
        String sql = "SELECT * FROM meals WHERE user_id = ? ORDER BY date DESC";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MealEntry meal = new MealEntry(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getDate("date"),
                    rs.getString("meal_name"),
                    rs.getDouble("gram")
                );
                meal.setCalories(rs.getInt("calories"));
                meals.add(meal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return meals;
    }

    // This method returns all workout plans of the user
    public List<WorkoutPlanEntry> getWorkoutEntriesByUserId(int userId) {
        List<WorkoutPlanEntry> workouts = new ArrayList<>();
        String sql = "SELECT * FROM workout_plan WHERE user_id = ? ORDER BY day_of_week DESC";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                WorkoutPlanEntry workout = new WorkoutPlanEntry(
                    0, // ID is not stored in the table
                    userId,
                    rs.getString("day_of_week"),
                    rs.getString("body_part"),
                    rs.getString("exercises")
                );
                workouts.add(workout);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workouts;
    }

    // This method saves a new workout plan to the database
    public boolean saveWorkoutPlan(WorkoutPlanEntry entry) {
        String sql = "INSERT INTO workout_plans (user_id, day, body_part, exercises) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entry.getUserId());
            stmt.setString(2, entry.getDay());
            stmt.setString(3, entry.getBodyPart());
            stmt.setString(4, entry.getExercises());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // This method deletes a workout plan for a specific day
    public boolean deleteWorkoutPlan(int userId, String day) {
        String sql = "DELETE FROM workout_plan WHERE user_id = ? AND day_of_week = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, day);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // This method updates a workout plan with new values
    public boolean updateWorkoutPlan(int userId, String day, String bodyPart, String exercises) {
        String sql = "UPDATE workout_plan SET body_part = ?, exercises = ? WHERE user_id = ? AND day_of_week = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bodyPart);
            stmt.setString(2, exercises);
            stmt.setInt(3, userId);
            stmt.setString(4, day);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // This method checks if a workout already exists for that day
    public boolean hasWorkoutEntryForDay(int userId, String day) {
        String sql = "SELECT COUNT(*) FROM workout_plan WHERE user_id = ? AND day_of_week = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, day);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // This method updates the user's target calories in the database
    public boolean updateUserTargetCalories(int userId, double targetCalories) {
        String sql = "UPDATE users SET target_calories = ? WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, targetCalories);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertUser(String name, String username, String password2, int age, String gender, double height, double weight) {
        return false;
    }

    // Another version of workout plan insert with different column names
    public boolean insertWorkoutPlan(WorkoutPlanEntry plan) {
        String sql = "INSERT INTO workout_plan (user_id, day_of_week, body_part, exercises) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, plan.getUserId());
            stmt.setString(2, plan.getDay());
            stmt.setString(3, plan.getBodyPart());
            stmt.setString(4, plan.getExercises());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // This method saves a new meal to the database
    public boolean insertMeal(MealEntry entry) {
        String sql = "INSERT INTO meals (user_id, date, meal_name, gram, calories) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entry.getUserId());
            stmt.setDate(2, new java.sql.Date(entry.getDate().getTime()));
            stmt.setString(3, entry.getMealName());
            stmt.setDouble(4, entry.getGram());
            stmt.setInt(5, entry.getCalories());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // This method updates user's height and weight
    public boolean updateUserHeightWeight(int userId, double height, double weight) {
        String sql = "UPDATE users SET height = ?, weight = ? WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, height);
            stmt.setDouble(2, weight);
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
