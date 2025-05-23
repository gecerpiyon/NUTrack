package view;

import model.DBManager;
import model.MealEntry;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

// This class creates a form to let user add a meal entry
public class MealEntryUI extends JFrame {
    // UI components and variables
    private JComboBox<String> mealComboBox;
    private JTextField customMealNameField, gramField, customCalorieField;
    private JTextArea resultArea;
    private User user;
    private DBManager dbManager;

    // Constructor
    public MealEntryUI(User user, DBManager dbManager) {
        this.user = user;
        this.dbManager = dbManager;

        // Set frame properties
        setTitle("Yemek Girişi");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true); // no title bar

        // Main panel with background color gradient
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(220, 220, 230), 0, getHeight(), new Color(245, 245, 250));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); // padding

        // Panel to hold input fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Font for labels
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Predefined meal options
        String[] meals = {
            "Pilav", "Kebap", "Çorba", "Salata", "Hamburger", "Pizza", "Yumurta",
            "Makarna", "Menemen", "Döner", "Lahmacun", "Börek", "Simit",
            "Balık", "Köfte", "Tavuk", "Et", "Patates Kızartması", "Çiğ Köfte", "Mantı",
            "Diğer (manuel giriş)"
        };

        // Initialize fields
        mealComboBox = new JComboBox<>(meals);
        customMealNameField = new JTextField();
        gramField = new JTextField();
        customCalorieField = new JTextField();
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false); // only show result

        // Meal label and combo box
        JLabel mealLabel = new JLabel("Yemek Seç:");
        mealLabel.setFont(labelFont);
        formPanel.add(mealLabel);
        formPanel.add(mealComboBox);

        // Custom meal name (if user selects 'Diğer')
        formPanel.add(Box.createVerticalStrut(10));
        JLabel customLabel = new JLabel("Yemek Adı (manuel):");
        customLabel.setFont(labelFont);
        formPanel.add(customLabel);
        formPanel.add(customMealNameField);
        customMealNameField.setEnabled(false); // disabled by default

        // Gram input
        formPanel.add(Box.createVerticalStrut(10));
        JLabel gramLabel = new JLabel("Gram:");
        gramLabel.setFont(labelFont);
        formPanel.add(gramLabel);
        formPanel.add(gramField);

        // Manual calorie input
        formPanel.add(Box.createVerticalStrut(10));
        JLabel calLabel = new JLabel("Kalori (manuel):");
        calLabel.setFont(labelFont);
        formPanel.add(calLabel);
        formPanel.add(customCalorieField);
        customCalorieField.setEnabled(false); // disabled unless "diğer" selected

        // Add button
        formPanel.add(Box.createVerticalStrut(20));
        JButton addButton = new JButton("Ekle");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(addButton);

        // Text area to show result
        formPanel.add(Box.createVerticalStrut(20));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        formPanel.add(scrollPane);

        // Enable custom input if "Diğer" is selected
        mealComboBox.addActionListener(e -> {
            boolean isCustom = mealComboBox.getSelectedItem().toString().toLowerCase().contains("diğer");
            customMealNameField.setEnabled(isCustom);
            customCalorieField.setEnabled(isCustom);
        });

        // Add button logic
        addButton.addActionListener((ActionEvent e) -> {
            try {
                String selected = mealComboBox.getSelectedItem().toString();
                String mealName = selected.toLowerCase().contains("diğer") ? customMealNameField.getText() : selected;
                double gram = Double.parseDouble(gramField.getText());
                Date today = new Date();

                // Create meal entry based on input
                MealEntry entry;
                if (customCalorieField.isEnabled() && !customCalorieField.getText().isEmpty()) {
                    int cal = Integer.parseInt(customCalorieField.getText());
                    entry = new MealEntry(0, user.getId(), new java.sql.Date(today.getTime()), mealName, gram, cal);
                } else {
                    entry = new MealEntry(0, user.getId(), new java.sql.Date(today.getTime()), mealName, gram);
                }

                // Save to DB
                dbManager.insertMeal(entry);

                // Show result
                resultArea.setText("Yemek başarıyla eklendi.\n\n" +
                        "Yemek: " + mealName + "\n" +
                        "Gram: " + gram + "\n" +
                        "Kalori: " + entry.getCalories() + " kcal");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Geçerli veri giriniz."); // show error
            }
        });

        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Header panel with close button
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(500, 40));

        JButton exitButton = new JButton("X");
        exitButton.setFocusPainted(false);
        exitButton.setBackground(new Color(200, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        exitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        exitButton.addActionListener(e -> dispose()); // close the window

        header.add(exitButton, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);
        add(mainPanel);

        setVisible(true); // show window
    }
}
