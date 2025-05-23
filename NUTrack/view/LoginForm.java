package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.DBManager;
import model.User;

// This class creates the login screen
public class LoginForm extends JFrame {
	
	public LoginForm() {
		setTitle("Login Panel"); // set window title
		setSize(400,300); // set window size
		setLocationRelativeTo(null); // center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the program when window closes
        
        // create a panel to hold UI elements
        JPanel panel = new JPanel();
        panel.setLayout(null); // use absolute positioning
        add(panel); // add panel to frame
        
        // ----- creating labels ----- //
        
        // Label for username
        JLabel userLabel = new JLabel("Kullanıcı adı:");
        userLabel.setBounds(20, 20, 100, 25);
        panel.add(userLabel);
        
        // Label for password
        JLabel passLabel = new JLabel("Şifre:");
        passLabel.setBounds(60, 60, 100, 25);
        panel.add(passLabel);
        
        // ----- creating input fields ----- //
        
        // Text field for username input
        JTextField userTextF = new JTextField();
        userTextF.setBounds(120, 20, 180, 25);
        panel.add(userTextF);
        
        // Password field for password input
        JPasswordField passField = new JPasswordField();
        passField.setBounds(120, 60, 180, 25);
        panel.add(passField);
        
        // Login button
        JButton loginButton = new JButton("Giriş Yap");
        loginButton.setBounds(145, 90, 130, 25);
        panel.add(loginButton);
        
        // Add action when login button is clicked
        loginButton.addActionListener(new ActionListener() {
        	 	@Override
        	    public void actionPerformed(ActionEvent e) {
        	        String username = userTextF.getText(); // get username from text field
        	        String password = String.valueOf(passField.getPassword()); // get password

        	        DBManager db = new DBManager(); // connect to database
        	        User user = db.validateLogin(username, password); // check login

        	        if (user != null) {
        	            // if login is successful
        	            JOptionPane.showMessageDialog(LoginForm.this, "Giriş başarılı! Hoş geldin " + user.getName());

        	            // open main menu for the user
        	            new UserMenu(user, db);

        	            dispose(); // close login form

        	        } else {
        	            // if login fails
        	            JOptionPane.showMessageDialog(LoginForm.this, "Hatalı kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
        	        }
        	    }
        });

        setVisible(true); // show the window
	}
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true); // open login window
        });
	}
}
