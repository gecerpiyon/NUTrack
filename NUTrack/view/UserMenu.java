package view;

import model.DBManager;
import model.User;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class UserMenu extends JFrame {

    private User loggedInUser;

    public UserMenu(User user, DBManager dbManager) {
        this.loggedInUser = user;

        // Set window title and size
        setTitle("NUTrack - Kullanıcı Menüsü");
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Hoş geldin, " + user.getName() + "!");
        welcomeLabel.setBounds(100, 10, 200, 25);
        add(welcomeLabel);

        // Button to set calorie target
        JButton calorieButton = new JButton("Kalori Hedefi Belirle");
        calorieButton.setBounds(75, 50, 200, 30);
        add(calorieButton);

        // Button to add workout plan
        JButton addPlanButton = new JButton("Antrenman Planı Ekle");
        addPlanButton.setBounds(75, 90, 200, 30);
        add(addPlanButton);

        // Button to edit or delete workout plan
        JButton editPlanButton = new JButton("Plan Güncelle / Sil");
        editPlanButton.setBounds(75, 130, 200, 30);
        add(editPlanButton);

        // Button to enter a meal
        JButton mealEntryButton = new JButton("Yemek Ekle");
        mealEntryButton.setBounds(75, 170, 200, 30);
        add(mealEntryButton);

        // Button to show user info screen
        JButton showInfoButton = new JButton("Bilgilerimi Göster");
        showInfoButton.setBounds(75, 210, 200, 30);
        add(showInfoButton);

        // Exit button
        JButton exitButton = new JButton("Çıkış");
        exitButton.setBounds(75, 250, 200, 30);
        add(exitButton);

        // Button actions

        // Opens calorie target form
        calorieButton.addActionListener(e -> new CalorieTargetForm(user, dbManager));

        // Opens workout plan add form
        addPlanButton.addActionListener(e -> new WorkoutPlanForm(user.getId(), dbManager));

        // Opens workout plan edit/delete form
        editPlanButton.addActionListener(e -> new WorkoutPlanEditForm(dbManager, user.getId()));

        // Opens meal entry screen
        mealEntryButton.addActionListener(e -> new MealEntryUI(user, dbManager));

        // Reloads user info and shows dashboard
        showInfoButton.addActionListener(e -> {
            User updatedUser = dbManager.validateLogin(user.getUsername(), user.getPassword());
            new UserInfoDisplay(updatedUser, dbManager).setVisible(true);
        });

        // Closes the program
        exitButton.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        // Show the menu
        setVisible(true);
    }
}
