package com.mycompany.maybannuocuong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/Admin/Documents/vending_machine.db";
    
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            // Create drinks table
            stmt.execute("CREATE TABLE IF NOT EXISTS drinks " +
                         "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "name TEXT NOT NULL, " +
                         "price REAL NOT NULL, " +
                         "quantity INTEGER NOT NULL, " +
                         "image_path TEXT)");

            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users " +
                         "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "username TEXT NOT NULL, " +
                         "password TEXT NOT NULL, " +
                         "is_admin BOOLEAN NOT NULL)");

            // Insert default admin user if not exists
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE is_admin = 1");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO users (username, password, is_admin) VALUES ('admin', 'admin123', 1)");
            }

        } catch (SQLException e) {
            System.err.println("Error initializing database:");
            e.printStackTrace();
        }
    }

    public static List<DoUong> getAllDrinks() {
        List<DoUong> drinks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM drinks")) {
            
            while (rs.next()) {
                DoUong drink = new DoUong(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("image_path")
                );
                drinks.add(drink);
            }
        } catch (SQLException e) {
            System.err.println("Error getting drinks:");
            e.printStackTrace();
        }
        return drinks;
    }

    public static void addDrink(DoUong drink) {
        String sql = "INSERT INTO drinks (name, price, quantity, image_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, drink.getName());
            pstmt.setDouble(2, drink.getPrice());
            pstmt.setInt(3, drink.getQuantity());
            pstmt.setString(4, drink.getImagePath());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding drink:");
            e.printStackTrace();
        }
    }

    public static void updateDrink(DoUong drink) {
        String sql = "UPDATE drinks SET name = ?, price = ?, quantity = ?, image_path = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, drink.getName());
            pstmt.setDouble(2, drink.getPrice());
            pstmt.setInt(3, drink.getQuantity());
            pstmt.setString(4, drink.getImagePath());
            pstmt.setInt(5, drink.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating drink:");
            e.printStackTrace();
        }
    }

    public static void deleteDrink(int drinkId) {
        String sql = "DELETE FROM drinks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, drinkId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting drink:");
            e.printStackTrace();
        }
    }

    public static void updateDrinkQuantity(int drinkId, int newQuantity) {
        String sql = "UPDATE drinks SET quantity = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, drinkId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating drink quantity:");
            e.printStackTrace();
        }
    }

    public static boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error authenticating user:");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAdmin(String username) {
        String sql = "SELECT is_admin FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("is_admin");
            }

        } catch (SQLException e) {
            System.err.println("Error checking admin status:");
            e.printStackTrace();
        }
        return false;
    }
}
