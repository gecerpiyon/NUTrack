package view;

import model.DBManager;
import model.MealEntry;
import model.User;
import model.WorkoutPlanEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import view.RoundedPanel;

// This class shows a full user dashboard with personal info, meals, workouts, and stats
public class UserInfoDisplay extends JFrame {

    public UserInfoDisplay(User user, DBManager dbManager) {
        // Window settings
        setUndecorated(true);
        setTitle("Kullanıcı Bilgileri");
        setSize(700, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(200, 200, 200);
                Color color2 = new Color(240, 240, 240);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Stats chart panel for last 7 days
        JPanel statsPanel = new RoundedPanel(20);
        statsPanel.setLayout(new BorderLayout());
        statsPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        statsPanel.setBackground(new Color(250, 250, 250));

        // Data for drawing chart
        String[] days = {"Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz"};
        int[] calories = new int[7];
        List<MealEntry> allMeals = dbManager.getMealEntriesByUserId(user.getId());
        LocalDate today = LocalDate.now();
        for (MealEntry m : allMeals) {
            LocalDate date = ((java.sql.Date) m.getDate()).toLocalDate();
            int index = today.minusDays(6).until(date).getDays();
            if (index >= 0 && index < 7) {
                calories[index] += m.getCalories();
            }
        }

        // Custom panel for drawing bar chart
        JPanel chartPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int barWidth = width / 9;
                int maxCal = 1;
                for (int cal : calories) maxCal = Math.max(maxCal, cal);

                for (int i = 0; i < calories.length; i++) {
                    int barHeight = (int) ((double) calories[i] / maxCal * (height - 40));
                    int x = 30 + i * (barWidth + 10);
                    int y = height - barHeight - 20;

                    g2.setColor(new Color(76, 175, 80));
                    g2.fillRect(x, y, barWidth, barHeight);

                    g2.setColor(Color.DARK_GRAY);
                    g2.drawString(String.valueOf(calories[i]), x, y - 5);
                    g2.drawString(days[i], x, height - 5);
                }
            }
        };
        chartPanel.setPreferredSize(new Dimension(600, 300));
        statsPanel.add(chartPanel, BorderLayout.CENTER);

        // User info section
        JPanel userPanel = new RoundedPanel(20);
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        userPanel.setBackground(new Color(250, 250, 250));

        // Meal info section
        JPanel mealPanel = new RoundedPanel(20);
        mealPanel.setLayout(new BoxLayout(mealPanel, BoxLayout.Y_AXIS));
        mealPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        mealPanel.setBackground(new Color(250, 250, 250));

        // Workout section
        JPanel workoutPanel = new RoundedPanel(20);
        workoutPanel.setLayout(new BoxLayout(workoutPanel, BoxLayout.Y_AXIS));
        workoutPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        workoutPanel.setBackground(new Color(250, 250, 250));

        Font sectionTitleFont = new Font("Segoe UI", Font.BOLD, 18);
        Font infoFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Add user info
        JLabel userInfoLabel = createSectionLabel("KULLANICI BİLGİLERİ", new Color(33, 150, 243));
        userPanel.add(userInfoLabel);
        userPanel.add(createInfoLabel("Ad: " + user.getName(), infoFont));
        userPanel.add(createInfoLabel("Cinsiyet: " + user.getGender(), infoFont));
        userPanel.add(createInfoLabel("Boy: " + user.getHeight() + " cm", infoFont));
        userPanel.add(createInfoLabel("Kilo: " + user.getWeight() + " kg", infoFont));

        // Show calorie target
        double target = user.getTargetCalories();
        String targetText = target > 0
            ? "Günlük Kalori Hedefi: " + (int) target + " kcal"
            : "Günlük Kalori Hedefi: Henüz belirlenmemiş";

        JLabel hedefLabel = new JLabel(targetText);
        hedefLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        hedefLabel.setForeground(new Color(255, 87, 34));
        hedefLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        hedefLabel.setBorder(new EmptyBorder(5, 0, 10, 0));
        userPanel.add(hedefLabel);

        // Add meals
        JLabel mealLabel = createSectionLabel("YEMEK GİRİŞLERİ", new Color(76, 175, 80));
        mealPanel.add(mealLabel);

        List<MealEntry> meals = dbManager.getMealEntriesByUserId(user.getId());
        int totalCalories = 0;

        if (meals.isEmpty()) {
            mealPanel.add(createInfoLabel("Henüz yemek girişi yapılmamış.", infoFont));
        } else {
            // Table data for meals
            String[] mealColumns = {"Tarih", "Yemek", "Gram", "Kalori"};
            String[][] mealData = new String[meals.size()][4];

            for (int i = 0; i < meals.size(); i++) {
                MealEntry m = meals.get(i);
                int cal = m.getCalories();
                LocalDate mealDate = ((java.sql.Date) m.getDate()).toLocalDate();
                if (mealDate.equals(today)) totalCalories += cal;

                mealData[i][0] = m.getDate().toString();
                mealData[i][1] = m.getMealName();
                mealData[i][2] = m.getGram() + " g";
                mealData[i][3] = cal + " kcal";
            }

            JTable mealTable = new JTable(mealData, mealColumns);
            styleTable(mealTable);
            JScrollPane mealScroll = new JScrollPane(mealTable);
            mealScroll.setPreferredSize(new Dimension(600, 160));
            mealPanel.add(mealScroll);

            // Show today's calories
            JLabel totalLabel = createInfoLabel("Toplam Kalori (Bugün): " + totalCalories + " kcal", infoFont);
            totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            mealPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            mealPanel.add(totalLabel);

            // Show remaining calories if target is set
            String kalanKaloriText;
            if (target > 0) {
                double kalan = target - totalCalories;
                kalanKaloriText = "Kalan Kalori (Bugün): " + (int) kalan + " kcal";
            } else {
                kalanKaloriText = "Kalan Kalori: (Hedef belirlenmemiş)";
            }

            JLabel kalanLabel = new JLabel(kalanKaloriText);
            kalanLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            kalanLabel.setForeground(new Color(33, 150, 243));
            kalanLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
            mealPanel.add(kalanLabel);

            // Calorie progress bar
            if (target > 0) {
                int progress = (int) ((totalCalories / target) * 100);
                if (progress > 100) progress = 100;

                JProgressBar calorieBar = new JProgressBar(0, 100);
                calorieBar.setValue(progress);
                calorieBar.setStringPainted(true);
                calorieBar.setForeground(progress >= 100 ? new Color(244, 67, 54) : new Color(76, 175, 80));
                calorieBar.setBorder(BorderFactory.createTitledBorder("Günlük Kalori İlerlemesi"));
                calorieBar.setPreferredSize(new Dimension(500, 30));
                mealPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                mealPanel.add(calorieBar);
            }
        }

        // Button to update user info
        JButton openUpdateDialogButton = new JButton("Bilgileri Güncelle");
        openUpdateDialogButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        openUpdateDialogButton.addActionListener(e -> {
            new UpdateUserInfoDialog(this, user, dbManager);
        });
        userPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userPanel.add(openUpdateDialogButton);

        // Workout table
        JLabel workoutLabel = createSectionLabel("ANTRENMAN PROGRAMI", new Color(255, 87, 34));
        workoutPanel.add(workoutLabel);

        List<WorkoutPlanEntry> workouts = dbManager.getWorkoutEntriesByUserId(user.getId());
        if (workouts.isEmpty()) {
            workoutPanel.add(createInfoLabel("Henüz antrenman girilmemiş.", infoFont));
        } else {
            String[] workoutColumns = {"Gün", "Bölge", "Egzersizler"};
            String[][] workoutData = new String[workouts.size()][3];

            for (int i = 0; i < workouts.size(); i++) {
                WorkoutPlanEntry w = workouts.get(i);
                workoutData[i][0] = w.getDay();
                workoutData[i][1] = w.getBodyPart();
                workoutData[i][2] = w.getExercises();
            }

            JTable workoutTable = new JTable(workoutData, workoutColumns);
            styleTable(workoutTable);
            JScrollPane workoutScroll = new JScrollPane(workoutTable);
            workoutScroll.setPreferredSize(new Dimension(600, 160));
            workoutPanel.add(workoutScroll);
        }

        // Tabs for different views
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.addTab("Kullanıcı Bilgileri", new JScrollPane(userPanel));
        tabbedPane.addTab("Günlük Kalori", new JScrollPane(mealPanel));
        tabbedPane.addTab("Egzersizler", new JScrollPane(workoutPanel));
        tabbedPane.addTab("İstatistikler", statsPanel);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Top bar with exit
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(45, 45, 45));
        header.setPreferredSize(new Dimension(700, 50));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel profileLabel = new JLabel("Hoş geldin, " + user.getName());
        profileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        profileLabel.setForeground(Color.WHITE);

        JButton exitButton = new JButton("Çıkış");
        exitButton.setFocusPainted(false);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(200, 0, 0));
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        exitButton.addActionListener(e -> dispose());

        header.add(profileLabel, BorderLayout.WEST);
        header.add(exitButton, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);
        add(mainPanel);
        setVisible(true);
    }

    // Method for creating label for info rows
    private JLabel createInfoLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(2, 0, 2, 0));
        return label;
    }

    // Method for creating title section labels
    private JLabel createSectionLabel(String title, Color color) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // Method to style JTable for consistency
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
    }
}
