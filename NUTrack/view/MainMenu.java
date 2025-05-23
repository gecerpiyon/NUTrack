package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.DBManager;

// This class shows the main menu screen of the application
public class MainMenu extends JFrame {
	
	// Database manager to be used in forms
	private DBManager dbManager;

	public MainMenu(DBManager db) {
		
	    this.dbManager = db;

		// Set title and basic frame settings
		setTitle("Ana Menü");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(410, 200);
		setLocationRelativeTo(null); // center the window
		
		// Create panel and set layout
		JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);
		
		// Welcome message
		JLabel welcomeLabel = new JLabel("NUTrack uygulamasına hoşgeldiniz! ");
		welcomeLabel.setBounds(100, 10, 1000, 17);
		panel.add(welcomeLabel);
		
		// Create login and register buttons
		JButton loginButton = new JButton("Giriş Yap");
		JButton registerButton = new JButton("Kayıt Ol");
		
		// Set button positions
		loginButton.setBounds(150, 80, 100, 25);
	 	registerButton.setBounds(150, 30, 100, 25);
		
	 	// When "Giriş Yap" button is clicked
	 	loginButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            dispose(); // close main menu window
	            new LoginForm(); // open login form
	        }
	    });
		 
		// When "Kayıt Ol" button is clicked
		registerButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            dispose(); // close main menu
	            new RegisterForm(db); // open register form
	        }
	    });
		 
	    // Add buttons to panel
	    panel.add(loginButton);
	    panel.add(registerButton);
	     
	    // Show the main menu
	    setVisible(true);
	}
}
