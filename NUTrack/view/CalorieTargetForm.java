package view;

import model.CaloriesCalculator;
import model.DBManager;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

// This class shows a form where user can set their calorie goal
public class CalorieTargetForm extends JFrame {

    public CalorieTargetForm(User user, DBManager dbManager) {
        // Set title and window settings
        setTitle("Kalori Hedefi Belirle");
        setSize(400, 300);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 240, 250), 0, getHeight(), new Color(220, 220, 235));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); // Padding

        // Form area (goal + activity level + button)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Label for goal selection
        JLabel label = new JLabel("Hedefinizi seçin:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 17));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(label);
        formPanel.add(Box.createVerticalStrut(10)); // Spacing

        // Combo box for goal choices
        String[] hedefler = {"Kilo kaybet", "Kilo al", "Kilonu koru"};
        JComboBox<String> hedefBox = new JComboBox<>(hedefler);
        hedefBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hedefBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        hedefBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(hedefBox);
        formPanel.add(Box.createVerticalStrut(20));

        // Label for activity level selection
        JLabel actLabel = new JLabel("Aktivite Seviyeniz:");
        actLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        actLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(actLabel);
        formPanel.add(Box.createVerticalStrut(5));

        // Combo box for activity levels
        String[] activityLevels = {
                "Hareketsiz",
                "Az Hareketli (1-3 gün) ",
                "Hareketli (3-5 gün) ",
                "Aşırı Hareketli (5-7 gün)"
        };
        JComboBox<String> activityBox = new JComboBox<>(activityLevels);
        activityBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        activityBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        activityBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(activityBox);
        formPanel.add(Box.createVerticalStrut(20));

        // Save button
        JButton kaydetButton = new JButton("Kaydet");
        kaydetButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        kaydetButton.setBackground(new Color(76, 175, 80));
        kaydetButton.setForeground(Color.WHITE);
        kaydetButton.setFocusPainted(false);
        formPanel.add(kaydetButton);

        // When user clicks "Kaydet" button
        kaydetButton.addActionListener((ActionEvent e) -> {
            String selected = (String) hedefBox.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Bir hedef seçmelisiniz.");
                return;
            }

            // Set activity multiplier
            String actLevel = (String) activityBox.getSelectedItem();
            double activityFactor = switch (actLevel) {
                case "Hareketsiz" -> 1.2;
                case "Az Hareketli (1-3 gün)" -> 1.375;
                case "Hareketli (3-5 gün)" -> 1.55;
                case "Aşırı Hareketli (5-7 gün)" -> 1.725;
                default -> 1.0;
            };

            // Convert goal to integer choice
            int secim = switch (selected) {
                case "Kilo kaybet" -> 1;
                case "Kilo al" -> 2;
                case "Kilonu koru" -> 3;
                default -> 0;
            };

            // If goal is invalid
            if (secim == 0) {
                JOptionPane.showMessageDialog(this, "Geçersiz seçim yapıldı.");
                return;
            }

            // Calculate target calories and update DB
            CaloriesCalculator calc = new CaloriesCalculator();
            double targetCalories = calc.calculateTarget(user, secim, activityFactor);

            boolean success = dbManager.updateUserTargetCalories(user.getId(), targetCalories);
            if (success) {
                JOptionPane.showMessageDialog(this, "Kalori hedefi güncellendi: " + Math.round(targetCalories) + " kcal");

                // Refresh user data after update
                User updatedUser = dbManager.validateLogin(user.getUsername(), user.getPassword());

                // Show updated info in new screen
                new UserInfoDisplay(updatedUser, dbManager);
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Veritabanı hatası! Güncellenemedi.");
            }
        });

        // Top panel with close button
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(400, 40));

        JButton exitButton = new JButton("X");
        exitButton.setFocusPainted(false);
        exitButton.setBackground(new Color(200, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        exitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        exitButton.addActionListener(e -> dispose());

        header.add(exitButton, BorderLayout.EAST);

        // Add header and form to main panel
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }
}
