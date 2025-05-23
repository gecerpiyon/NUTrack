package controller;

import model.DBManager;
import view.MainMenu;

public class Main {

    public static void main(String[] args) {
        DBManager dbManager = new DBManager(); // Create a DBManager object to connect database

        // Try to connect to the database (not required, but useful to check)
        if (dbManager.connect() != null) {
            System.out.println("Veritabanına bağlantı başarılı!"); // If connected, show success
        } else {
            System.out.println("Veritabanı bağlantısı başarısız!"); // If not, show error
        }

        // Start the main menu GUI screen
        new MainMenu(dbManager);
    }
}
