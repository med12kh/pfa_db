package view;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.*;
import java.util.List;
import metier.ServiceManager;
import entity.Service;
import main.MainView; // Import MainView

public class ServiceView {

    private JFrame frame;
    private JTextField id;
    private JTextField nom;
    private JTextField description;
    private JTextField prix;
    private ServiceManager manager;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;
    private MainView mainView; // Reference to MainView

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ServiceView window = new ServiceView(null); // Pass null for MainView reference
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ServiceView(MainView mainView) { // Constructor accepting MainView reference
        this.mainView = mainView; // Store the reference
        initialize();
    }

    private void initialize() {
        manager = new ServiceManager();
        frame = new JFrame();
        frame.setBounds(100, 100, 750, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Gestion des Services");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBounds(270, 10, 200, 30);
        frame.getContentPane().add(lblTitle);

        String[] labels = {"ID :", "Nom :", "Description :", "Prix :"};
        int y = 60;
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Tahoma", Font.BOLD, 12));
            lbl.setBounds(100, y, 100, 20);
            frame.getContentPane().add(lbl);

            JTextField txt = new JTextField();
            txt.setBounds(220, y, 150, 20);
            frame.getContentPane().add(txt);
            fields[i] = txt;
            y += 40;
        }

        id = fields[0];
        nom = fields[1];
        description = fields[2];
        prix = fields[3];

        JButton btnInsert = new JButton("Insert");
        btnInsert.addActionListener(e -> {
            Service service = createServiceFromFields();
            JOptionPane.showMessageDialog(null, manager.addService(service));
            loadServices();
            clearFields();
        });
        btnInsert.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnInsert.setBounds(420, 60, 100, 30);
        frame.getContentPane().add(btnInsert);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> {
            int serviceId = Integer.parseInt(id.getText());
            Service result = manager.serviceById(serviceId);
            if (result != null) {
                nom.setText(result.getNom());
                description.setText(result.getDescription());
                prix.setText(String.valueOf(result.getPrix()));
            } else {
                JOptionPane.showMessageDialog(null, "Service Inexistant !");
                clearFields();
            }
        });
        btnSearch.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSearch.setBounds(420, 110, 100, 30);
        frame.getContentPane().add(btnSearch);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            Service service = createServiceFromFields();
            int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous modifier ce service ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, manager.updateService(service));
                loadServices();
                clearFields();
            }
        });
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnUpdate.setBounds(420, 160, 100, 30);
        frame.getContentPane().add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            int serviceId = Integer.parseInt(id.getText());
            int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous supprimer ce service ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Service service = new Service();
                service.setId(serviceId);
                JOptionPane.showMessageDialog(null, manager.deleteService(service));
                loadServices();
                clearFields();
            }
        });
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnDelete.setBounds(420, 210, 100, 30);
        frame.getContentPane().add(btnDelete);

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            if (mainView != null) {
                mainView.setVisible(true); // Show the MainView
            }
            frame.dispose(); // Close the current window
        });
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnBack.setBounds(420, 260, 100, 30);
        frame.getContentPane().add(btnBack);

        model = new DefaultTableModel(new String[]{"ID", "Nom", "Description", "Prix"}, 0);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        scroll.setBounds(50, 320, 620, 250);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                if (rowIndex != -1) {
                    id.setText(model.getValueAt(rowIndex, 0).toString());
                    nom.setText(model.getValueAt(rowIndex, 1).toString());
                    description.setText(model.getValueAt(rowIndex, 2).toString());
                    prix.setText(model.getValueAt(rowIndex, 3).toString());
                }
            }
        });

        frame.getContentPane().add(scroll);
        loadServices();
    }

    private void loadServices() {
        model.setRowCount(0);
        List<Service> services = manager.allServices();
        for (Service service : services) {
            model.addRow(new Object[]{
                service.getId(),
                service.getNom(),
                service.getDescription(),
                service.getPrix()
            });
        }
    }

    private Service createServiceFromFields() {
        Service service = new Service();
        service.setId(Integer.parseInt(id.getText()));
        service.setNom(nom.getText());
        service.setDescription(description.getText());
        service.setPrix(Double.parseDouble(prix.getText()));
        return service;
    }

    private void clearFields() {
        id.setText("");
        nom.setText("");
        description.setText("");
        prix.setText("");
    }
}
