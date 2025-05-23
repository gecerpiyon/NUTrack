package view;

import model.DBManager;
import model.WorkoutPlanEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

// This class is for creating a new weekly workout plan for the user
public class WorkoutPlanForm extends JFrame {

    // UI components and list to hold selected body parts
    private JComboBox<String> comboBoxCount;
    private JPanel bodyPartPanel;
    private JTextArea txtExercises;
    private ArrayList<JComboBox<String>> bodyPartComboBoxes = new ArrayList<>();

    public WorkoutPlanForm(int userId, DBManager dbManager) {
        // Set basic properties of the frame
        setTitle("Haftalık Antrenman Planı");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a gradient background panel
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

        // Create form layout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        // Label to select workout day
        JLabel lblDay = new JLabel("Gün Seç:");
        lblDay.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDay.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDay.setFont(font);
        formPanel.add(lblDay);

        // Dropdown for workout days
        String[] days = {"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"};
        JComboBox<String> cmbDay = new JComboBox<>(days);
        cmbDay.setFont(font);
        formPanel.add(cmbDay);

        formPanel.add(Box.createVerticalStrut(10));

        // Label to ask how many body parts to work on
        JLabel lblCount = new JLabel("Kaç bölge çalışacaksın?");
        lblCount.setFont(font);
        lblCount.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblCount);

        // Dropdown to choose number of body parts
        comboBoxCount = new JComboBox<>(new String[]{"1", "2", "3"});
        comboBoxCount.setFont(font);
        formPanel.add(comboBoxCount);

        formPanel.add(Box.createVerticalStrut(10));

        // Panel to hold the body part dropdowns
        bodyPartPanel = new JPanel();
        bodyPartPanel.setLayout(new GridLayout(3, 1, 10, 10));
        bodyPartPanel.setOpaque(false);
        formPanel.add(bodyPartPanel);

        // Add listener to update body part fields based on selected count
        comboBoxCount.addActionListener(e -> updateBodyPartFields());
        updateBodyPartFields(); // initialize body part fields

        formPanel.add(Box.createVerticalStrut(10));

        // Label for exercise description
        JLabel lblExercises = new JLabel("Egzersizler:");
        lblExercises.setFont(font);
        lblExercises.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblExercises);

        // Text area for exercises input
        txtExercises = new JTextArea(5, 30);
        txtExercises.setFont(font);
        JScrollPane scroll = new JScrollPane(txtExercises);
        formPanel.add(scroll);

        formPanel.add(Box.createVerticalStrut(20));

        // Save button to store the plan
        JButton btnSave = new JButton("Kaydet");
        btnSave.setFont(font);
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(btnSave);

        // Save button action
        btnSave.addActionListener((ActionEvent e) -> {
            String dayOfWeek = (String) cmbDay.getSelectedItem();

            // Check if a plan already exists for the selected day
            if (dbManager.hasWorkoutEntryForDay(userId, dayOfWeek)) {
                JOptionPane.showMessageDialog(this, "Bu gün için zaten bir antrenman planın var!");
                return;
            }

            // Combine selected body parts into a string
            StringBuilder bodyParts = new StringBuilder();
            for (JComboBox<String> combo : bodyPartComboBoxes) {
                if (combo.getSelectedItem() != null) {
                    bodyParts.append(combo.getSelectedItem().toString()).append(", ");
                }
            }
            if (bodyParts.length() > 0) {
                bodyParts.setLength(bodyParts.length() - 2); // remove last comma
            }

            // Get exercise description
            String exercises = txtExercises.getText();

            // Create and save the workout plan
            WorkoutPlanEntry entry = new WorkoutPlanEntry(userId, dayOfWeek, bodyParts.toString(), exercises);
            dbManager.insertWorkoutPlan(entry);
            JOptionPane.showMessageDialog(this, "Antrenman planı kaydedildi.");
            dispose(); // close form
        });

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

        // Add header and form to main panel
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true); // show the window
    }

    // Method to refresh body part dropdowns based on selected count
    private void updateBodyPartFields() {
        bodyPartPanel.removeAll();
        bodyPartComboBoxes.clear();

        String[] bodyParts = {"Göğüs", "Sırt", "Bacak", "Omuz", "Kol", "Kardiyo", "Tüm Vücut"};
        int count = Integer.parseInt((String) comboBoxCount.getSelectedItem());

        for (int i = 0; i < count; i++) {
            JComboBox<String> combo = new JComboBox<>(bodyParts);
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            bodyPartComboBoxes.add(combo);
            bodyPartPanel.add(combo);
        }

        // Refresh the panel
        bodyPartPanel.revalidate();
        bodyPartPanel.repaint();
    }
}
