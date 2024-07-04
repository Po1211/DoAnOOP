/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maybannuocuong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminGUI extends JFrame {
    private JTable drinkTable;
    private JButton addButton, editButton, deleteButton;
    private VendingMachineController controller;

    public AdminGUI(VendingMachineController controller) {
        this.controller = controller;
        setTitle("Admin Interface");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        String[] columnNames = {"ID", "Name", "Price", "Quantity", "Image Path"};
        drinkTable = new JTable();
        drinkTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(drinkTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Drink");
        editButton = new JButton("Edit Drink");
        deleteButton = new JButton("Delete Drink");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> showAddDrinkDialog());
        editButton.addActionListener(e -> showEditDrinkDialog());
        deleteButton.addActionListener(e -> deleteDrink());

        updateDrinkTable(controller.getAllDrinks());
    }

    private void showAddDrinkDialog() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField imagePathField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Image Path:"));
        panel.add(imagePathField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Drink",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                String imagePath = imagePathField.getText();

                DoUong newDrink = new DoUong(0, name, price, quantity, imagePath);
                controller.addDrink(newDrink);
                updateDrinkTable(controller.getAllDrinks());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for price and quantity.");
            }
        }
    }

    private void showEditDrinkDialog() {
        int selectedRow = drinkTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a drink to edit.");
            return;
        }

        int id = (int) drinkTable.getValueAt(selectedRow, 0);
        String name = (String) drinkTable.getValueAt(selectedRow, 1);
        double price = (double) drinkTable.getValueAt(selectedRow, 2);
        int quantity = (int) drinkTable.getValueAt(selectedRow, 3);
        String imagePath = (String) drinkTable.getValueAt(selectedRow, 4);

        JTextField nameField = new JTextField(name);
        JTextField priceField = new JTextField(String.valueOf(price));
        JTextField quantityField = new JTextField(String.valueOf(quantity));
        JTextField imagePathField = new JTextField(imagePath);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Image Path:"));
        panel.add(imagePathField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Drink",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newName = nameField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                int newQuantity = Integer.parseInt(quantityField.getText());
                String newImagePath = imagePathField.getText();

                DoUong updatedDrink = new DoUong(id, newName, newPrice, newQuantity, newImagePath);
                controller.updateDrink(updatedDrink);
                updateDrinkTable(controller.getAllDrinks());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for price and quantity.");
            }
        }
    }

    private void deleteDrink() {
        int selectedRow = drinkTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a drink to delete.");
            return;
        }

        int id = (int) drinkTable.getValueAt(selectedRow, 0);
        String name = (String) drinkTable.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete the drink: " + name + "?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteDrink(id);
            updateDrinkTable(controller.getAllDrinks());
        }
    }

    public void updateDrinkTable(List<DoUong> drinks) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Quantity", "Image Path"}, 0);
        for (DoUong drink : drinks) {
            model.addRow(new Object[]{
                drink.getId(),
                drink.getName(),
                drink.getPrice(),
                drink.getQuantity(),
                drink.getImagePath()
            });
        }
        drinkTable.setModel(model);
    }
}
