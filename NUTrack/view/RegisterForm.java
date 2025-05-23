package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.DBManager;
import model.User;

// This class shows the user registration screen
public class RegisterForm extends JFrame {

	public RegisterForm(DBManager db) {
		// Window settings
		setTitle("Kayıt Ol");
		setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create main panel
        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(null);

        ////*---Labels and Fields---*////

        // Name field
        JLabel nameLabel = new JLabel("Ad-Soyad:");
        nameLabel.setBounds(20, 20, 100, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(130, 20, 200, 25);
        panel.add(nameField);

        // Age field
        JLabel ageLabel = new JLabel("Yaş:");
        ageLabel.setBounds(20, 60, 100, 25);
        panel.add(ageLabel);

        JTextField ageField = new JTextField();
        ageField.setBounds(130, 60, 200, 25);
        panel.add(ageField);

        // Gender combo box
        JLabel genderLabel = new JLabel("Cinsiyet:");
        genderLabel.setBounds(20, 100, 100, 25);
        panel.add(genderLabel);

        String[] genders = { "Erkek", "Kadın" };
        JComboBox<String> genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBounds(130, 100, 200, 25);
        panel.add(genderComboBox);

        // Username
        JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
        usernameLabel.setBounds(20, 140, 100, 25);
        panel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(130, 140, 200, 25);
        panel.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Şifre:");
        passwordLabel.setBounds(20, 180, 100, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(130, 180, 200, 25);
        panel.add(passwordField);

        // Height input
        JLabel heightLabel = new JLabel("Boy (cm):");
        heightLabel.setBounds(20, 220, 100, 25);
        panel.add(heightLabel);

        JTextField heightField = new JTextField();
        heightField.setBounds(130, 220, 200, 25);
        panel.add(heightField);

        // Weight input
        JLabel weightLabel = new JLabel("Kilo (kg):");
        weightLabel.setBounds(20, 260, 100, 25);
        panel.add(weightLabel);

        JTextField weightField = new JTextField();
        weightField.setBounds(130, 260, 200, 25);
        panel.add(weightField);

        // REGISTER BUTTON //
        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.setBounds(185, 300, 90, 30);
        panel.add(registerButton);

        // Button's listener //
        registerButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    // Get all user input
        	    String name = nameField.getText();
        	    String gender = (String) genderComboBox.getSelectedItem();
        	    String username = usernameField.getText();
        	    String password = new String(passwordField.getPassword());

        	    int age;
        	    double height, weight;

        	    try {
        	        // Try to convert numeric inputs
        	        age = Integer.parseInt(ageField.getText().trim());
        	        height = Double.parseDouble(heightField.getText().trim());
        	        weight = Double.parseDouble(weightField.getText().trim());
        	    } catch (NumberFormatException ex) {
        	        // Show error if input is invalid
        	        JOptionPane.showMessageDialog(null, "Lütfen yaş, boy ve kilo alanlarına geçerli sayılar girin.", "Hatalı Giriş", JOptionPane.ERROR_MESSAGE);
        	        return;
        	    }

        	    // Create user and try to insert into database
        	    User newUser = new User(0, name, age, gender, username, password, height, weight);
        	    boolean success = db.insertUser(newUser);

        	    if (success) {
        	        // Show success message
        	        JOptionPane.showMessageDialog(null, "Kayıt başarılı!");
        	        // Log in the new user and open user menu
        	        User user = db.validateLogin(username, password);
        	        new UserMenu(user, db);
        	        dispose(); // Close registration form
        	    } else {
        	        // Show fail message
        	        JOptionPane.showMessageDialog(null, "Kayıt başarısız!");
        	    }
        	}
        });

        setVisible(true); // Show the registration form
	}

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegisterForm(null).setVisible(true); // Open the window
        });
	}
}
