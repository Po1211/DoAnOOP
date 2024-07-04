package com.mycompany.maybannuocuong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class VendingMachineGUI extends JFrame {
    private JPanel mainPanel;
    private JButton adminButton;
    private JPanel drinkPanel;
    private JButton buyButton;
    private JButton resetButton;  // Thêm nút reset
    private JLabel totalLabel;
    private JPanel selectedItemsPanel;  // Thay đổi để hiển thị ảnh và số lượng của tất cả đồ uống đã chọn
    private VendingMachineController controller;
    private List<SelectedItem> selectedItems = new ArrayList<>();  // Danh sách các đồ uống đã chọn

    public VendingMachineGUI(VendingMachineController controller) {
        this.controller = controller;
        setTitle("Vending Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);

        mainPanel = new JPanel(new BorderLayout());
        adminButton = new JButton("Admin Login");
        drinkPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        buyButton = new JButton("Buy");
        resetButton = new JButton("Reset");  // Khởi tạo nút reset
        totalLabel = new JLabel("Total: $0.00");

        // Khởi tạo panel để hiển thị tất cả các đồ uống đã chọn
        selectedItemsPanel = new JPanel();
        selectedItemsPanel.setLayout(new GridLayout(0, 4, 10, 10));  // Hiển thị ảnh với 4 cột

        // Panel chứa các nút và tổng tiền
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Sử dụng FlowLayout để đưa các nút nằm ngang
        topPanel.add(adminButton);  // Thêm nút adminButton
        topPanel.add(resetButton);  // Thêm nút reset vào topPanel
        topPanel.add(buyButton);  // Thêm nút buy vào topPanel
        topPanel.add(totalLabel);  // Thêm totalLabel vào topPanel

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(selectedItemsPanel), BorderLayout.CENTER);  // Hiển thị danh sách đồ uống đã chọn

        mainPanel.add(topPanel, BorderLayout.NORTH);  // Thay đổi để thêm topPanel vào phía Bắc
        mainPanel.add(new JScrollPane(drinkPanel), BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);  // Thêm rightPanel vào east của mainPanel

        adminButton.addActionListener(e -> {
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setVisible(true);
            if (loginDialog.isAuthenticated()) {
                controller.showAdminInterface();
            }
        });

        buyButton.addActionListener(e -> {
            if (!selectedItems.isEmpty()) {
                try {
                    for (SelectedItem item : selectedItems) {
                        controller.buyDrink(item.getDrink(), item.getQuantity());
                    }
                    JOptionPane.showMessageDialog(this, "Payment Successful! Thank you for your purchase.");
                    resetSelection();  // Hủy chọn tất cả đồ uống sau khi mua hàng
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "An error occurred during the purchase.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select at least one drink.");
            }
        });

        resetButton.addActionListener(e -> {
            resetSelection();  // Xử lý sự kiện khi nhấn nút reset
        });

        add(mainPanel);

        updateDrinkDisplay(controller.getAllDrinks());
    }

    public void updateDrinkDisplay(List<DoUong> drinks) {
        drinkPanel.removeAll();
        for (DoUong drink : drinks) {
            JPanel drinkItemPanel = new JPanel();
            drinkItemPanel.setLayout(new BoxLayout(drinkItemPanel, BoxLayout.Y_AXIS));
            drinkItemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel nameLabel = new JLabel(drink.getName());
            JLabel priceLabel = new JLabel("$" + String.format("%.2f", drink.getPrice()));
            JLabel quantityLabel = new JLabel("Quantity: " + drink.getQuantity());

            drinkItemPanel.add(nameLabel);
            drinkItemPanel.add(priceLabel);
            drinkItemPanel.add(quantityLabel);

            if (drink.getImagePath() != null && !drink.getImagePath().isEmpty()) {
                ImageIcon icon = new ImageIcon(drink.getImagePath());
                Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                drinkItemPanel.add(imageLabel);
            }

            drinkItemPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addOrUpdateSelectedItem(drink);  // Thay đổi phương thức để không cần nhập số lượng
                    updateSelectedItemsPanel();
                    updateTotalLabel();
                }
            });

            drinkPanel.add(drinkItemPanel);
        }
        drinkPanel.revalidate();
        drinkPanel.repaint();
    }

    private void addOrUpdateSelectedItem(DoUong drink) {
        for (SelectedItem item : selectedItems) {
            if (item.getDrink().equals(drink)) {
                // Nếu đồ uống đã có trong danh sách, tăng số lượng lên 1
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        // Nếu chưa có, thêm vào danh sách với số lượng là 1
        selectedItems.add(new SelectedItem(drink, 1));
    }

    private void updateSelectedItemsPanel() {
        selectedItemsPanel.removeAll();
        for (SelectedItem item : selectedItems) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel nameLabel = new JLabel(item.getDrink().getName());
            JLabel priceLabel = new JLabel("$" + String.format("%.2f", item.getDrink().getPrice()));
            JLabel quantityLabel = new JLabel("Quantity: " + item.getQuantity());
            JLabel totalPriceLabel = new JLabel("Total Price: $" + String.format("%.2f", item.getDrink().getPrice() * item.getQuantity()));

            itemPanel.add(nameLabel);
            itemPanel.add(priceLabel);
            itemPanel.add(quantityLabel);
            itemPanel.add(totalPriceLabel);

            if (item.getDrink().getImagePath() != null && !item.getDrink().getImagePath().isEmpty()) {
                ImageIcon icon = new ImageIcon(item.getDrink().getImagePath());
                Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);  // Thay đổi kích thước ảnh
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                itemPanel.add(imageLabel);
            }

            selectedItemsPanel.add(itemPanel);
        }
        selectedItemsPanel.revalidate();
        selectedItemsPanel.repaint();
    }

    private void updateTotalLabel() {
        double total = 0;
        for (SelectedItem item : selectedItems) {
            total += item.getDrink().getPrice() * item.getQuantity();
        }
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    private void resetSelection() {
        selectedItems.clear();
        totalLabel.setText("Total: $0.00");
        updateSelectedItemsPanel();  // Cập nhật lại panel hiển thị các đồ uống đã chọn
    }

    private static class SelectedItem {
        private DoUong drink;
        private int quantity;

        public SelectedItem(DoUong drink, int quantity) {
            this.drink = drink;
            this.quantity = quantity;
        }

        public DoUong getDrink() {
            return drink;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
