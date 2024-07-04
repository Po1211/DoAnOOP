package com.mycompany.maybannuocuong;

public class MayBanNuocUong {

    public static void main(String[] args) {
       DatabaseManager.initializeDatabase();
       javax.swing.SwingUtilities.invokeLater(() -> new VendingMachineController());
    }
}