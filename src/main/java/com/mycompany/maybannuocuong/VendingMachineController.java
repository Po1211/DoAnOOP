/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maybannuocuong;

/**
 *
 * @author Admin
 */
import java.util.List;

public class VendingMachineController {
    private DatabaseManager dbManager;
    private VendingMachineGUI userView;
    private AdminGUI adminView;

    public VendingMachineController() {
        dbManager = new DatabaseManager();
        userView = new VendingMachineGUI(this);
        userView.setVisible(true);
    }

    public List<DoUong> getAllDrinks() {
        return DatabaseManager.getAllDrinks();
    }

    public void buyDrink(DoUong drink, int quantity) {
        if (drink.getQuantity() >= quantity) {
            double total = drink.getPrice() * quantity;
            // Process payment (in a real application, you'd integrate with a payment system here)
            boolean paymentSuccessful = true; // Assume payment is always successful for this example

            if (paymentSuccessful) {
                DatabaseManager.updateDrinkQuantity(drink.getId(), drink.getQuantity() - quantity);
                userView.updateDrinkDisplay(getAllDrinks());
                javax.swing.JOptionPane.showMessageDialog(userView, "Purchase successful. Total: $" + String.format("%.2f", total));
            } else {
                javax.swing.JOptionPane.showMessageDialog(userView, "Payment failed. Please try again.");
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(userView, "Not enough stock");
        }
    }

    public void showAdminInterface() {
        adminView = new AdminGUI(this);
        adminView.setVisible(true);
    }

    public void addDrink(DoUong drink) {
        DatabaseManager.addDrink(drink);
        if (adminView != null) {
            adminView.updateDrinkTable(getAllDrinks());
        }
        userView.updateDrinkDisplay(getAllDrinks());
    }

    public void updateDrink(DoUong drink) {
        DatabaseManager.updateDrink(drink);
        if (adminView != null) {
            adminView.updateDrinkTable(getAllDrinks());
        }
        userView.updateDrinkDisplay(getAllDrinks());
    }

    public void deleteDrink(int drinkId) {
        DatabaseManager.deleteDrink(drinkId);
        if (adminView != null) {
            adminView.updateDrinkTable(getAllDrinks());
        }
        userView.updateDrinkDisplay(getAllDrinks());
    }
}
