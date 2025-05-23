package view;

import model.DBManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

// This class allows user to update or delete their workout plan
public class WorkoutPlanEditForm extends JFrame {

    // GUI components and DB manager
    private JComboBox<String> cmbDay, cmbBodyPart;
    private JTextField exercisesField;
    private JButton deleteButton, updateButton;
    private DBManager dbManager;
    private int userId;

    public WorkoutPlanEditForm(DBManager dbManager, int userId) {
        this.dbManager = dbManager;
        this.userId = userId;

        // Set window properties
        setTitle("Plan Güncelle/Sil");
        setSize(500, 400);
        setUndecorated(true); // no window borders
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // only close this window

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 240, 245), 0, getHeight(), new Color(220, 220, 230));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); 

        // Form panel for input fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Fonts
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Dropdown for days
        String[] days = {"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"};
        cmbDay = new JComboBox<>(days);
        cmbDay.setFont(fieldFont);

        // Dropdown for body parts
        String[] bodyParts = {"Göğüs", "Sırt", "Bacak", "Omuz", "Kol", "Kardiyo", "Tüm Vücut"};
        cmbBodyPart = new JComboBox<>(bodyParts);
        cmbBodyPart.setFont(fieldFont);

        // Text field for exercises
        exercisesField = new JTextField();

        // Add form fields
        addField(formPanel, "Gün:", cmbDay, labelFont);
        addField(formPanel, "Bölge:", cmbBodyPart, labelFont);
        addField(formPanel, "Egzersizler:", exercisesField, labelFont, fieldFont);

        // Buttons panel (Update and Delete)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Delete button
        deleteButton = new JButton("Sil");
        deleteButton.setBackground(new Color(220, 53, 69)); // red
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(labelFont);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(this::deletePlan);
        buttonPanel.add(deleteButton);

        // Update button
        updateButton = new JButton("Güncelle");
        updateButton.setBackground(new Color(40, 167, 69)); // green
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(labelFont);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(this::updatePlan);
        buttonPanel.add(updateButton);

        formPanel.add(buttonPanel); // add buttons under fields

        // Top header with close button
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(500, 40));

        JButton exitButton = new JButton("X");
        exitButton.setFocusPainted(false);
        exitButton.setBackground(new Color(200, 0, 0)); // red
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        exitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        exitButton.addActionListener(e -> dispose()); // close window

        header.add(exitButton, BorderLayout.EAST);

        // Add everything to main panel
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    // Helper to add JComboBox with label
    private void addField(JPanel panel, String labelText, JComboBox<String> combo, Font labelFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);

        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(combo);
        panel.add(Box.createVerticalStrut(10)); // spacing
    }

    // Helper to add JTextField with label
    private void addField(JPanel panel, String labelText, JTextField field, Font labelFont, Font fieldFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);

        field.setFont(fieldFont);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createVerticalStrut(10)); // spacing
    }

    // Called when delete button is clicked
    private void deletePlan(ActionEvent e) {
        String day = (String) cmbDay.getSelectedItem();
        
        // Check if there is a plan to delete
        if (!dbManager.hasWorkoutEntryForDay(userId, day)) {
            JOptionPane.showMessageDialog(this, "Bu güne ait silinecek bir plan bulunamadı.");
            return;
        }

        // Try to delete plan
        boolean success = dbManager.deleteWorkoutPlan(userId, day);
        if (success) {
            JOptionPane.showMessageDialog(this, "Plan başarıyla silindi.");
            dispose(); // close window
        } else {
            JOptionPane.showMessageDialog(this, "Silme başarısız.");
        }
    }

    // Called when update button is clicked
    private void updatePlan(ActionEvent e) {
        String day = (String) cmbDay.getSelectedItem();
        String bodyPart = (String) cmbBodyPart.getSelectedItem();
        String exercises = exercisesField.getText().trim();

        // Check if input is valid
        if (exercises.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Egzersiz bilgisi boş olamaz.");
            return;
        }

        // Check if there's a plan for selected day
        if (!dbManager.hasWorkoutEntryForDay(userId, day)) {
            JOptionPane.showMessageDialog(this, "Bu güne ait bir plan bulunamadı.");
            return;
        }

        // Try to update the plan
        boolean success = dbManager.updateWorkoutPlan(userId, day, bodyPart, exercises);
        if (success) {
            JOptionPane.showMessageDialog(this, "Plan başarıyla güncellendi.");
            dispose(); // close window
        } else {
            JOptionPane.showMessageDialog(this, "Güncelleme başarısız.");
        }
    }
}
