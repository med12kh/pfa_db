package view;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.*;
import java.util.List;
import metier.EmployeManager;
import entity.Employe;
import main.MainView; // Import MainView

public class EmployeView {

    private JFrame frame;
    private JTextField mat;
    private JTextField nom;
    private JTextField sal;
    private EmployeManager manager;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;
    private MainView mainView; // Reference to MainView

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EmployeView window = new EmployeView(null); // Pass null for MainView reference
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EmployeView(MainView mainView) { // Constructor accepting MainView reference
        this.mainView = mainView; // Store the reference
        initialize();
    }

    private void initialize() {
        manager = new EmployeManager();
        frame = new JFrame();
        frame.setBounds(100, 100, 750, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Gestion des Employés");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBounds(270, 10, 200, 30);
        frame.getContentPane().add(lblTitle);

        String[] labels = {"Matricule :", "Nom :", "Salaire :"};
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

        mat = fields[0];
        nom = fields[1];
        sal = fields[2];

        JButton btnInsert = new JButton("Insert");
        btnInsert.addActionListener(e -> {
            try {
                Employe employe = createEmployeFromFields();
                JOptionPane.showMessageDialog(null, manager.addEmploye(employe));
                loadEmployes();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage());
            }
        });
        btnInsert.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnInsert.setBounds(420, 60, 100, 30);
        frame.getContentPane().add(btnInsert);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> {
            try {
                int matId = Integer.parseInt(mat.getText());
                Employe result = manager.employeByMat(matId);
                if (result != null) {
                    nom.setText(result.getNom());
                    sal.setText(String.valueOf(result.getSal()));
                } else {
                    JOptionPane.showMessageDialog(null, "Employé Inexistant !");
                    clearFields();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un matricule valide !");
            }
        });
        btnSearch.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSearch.setBounds(420, 110, 100, 30);
        frame.getContentPane().add(btnSearch);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            try {
                Employe employe = createEmployeFromFields();
                int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous modifier cet employé ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, manager.updateEmploye(employe));
                    loadEmployes();
                    clearFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage());
            }
        });
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnUpdate.setBounds(420, 160, 100, 30);
        frame.getContentPane().add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            try {
                int matId = Integer.parseInt(mat.getText());
                int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous supprimer cet employé ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, manager.deleteEmploye(matId));
                    loadEmployes();
                    clearFields();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un matricule valide !");
            }
        });
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnDelete.setBounds(420, 210, 100, 30);
        frame.getContentPane().add(btnDelete);

        // Back button to return to MainView
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            if (mainView != null) {
                mainView.setVisible(true); // Show the MainView
            }
            frame.dispose(); // Close the current EmployeView
        });
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnBack.setBounds(420, 260, 100, 30);
        frame.getContentPane().add(btnBack);

        model = new DefaultTableModel(new String[]{"Matricule", "Nom", "Salaire"}, 0);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        scroll.setBounds(50, 320, 620, 250);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                if (rowIndex != -1) {
                    mat.setText(model.getValueAt(rowIndex, 0).toString());
                    nom.setText(model.getValueAt(rowIndex, 1).toString());
                    sal.setText(model.getValueAt(rowIndex, 2).toString());
                }
            }
        });

        frame.getContentPane().add(scroll);
        loadEmployes();
    }

    private void loadEmployes() {
        model.setRowCount(0);
        List<Employe> employes = manager.allEmployes();
        for (Employe employe : employes) {
            model.addRow(new Object[]{
                employe.getMat(),
                employe.getNom(),
                employe.getSal()
            });
        }
    }

    private Employe createEmployeFromFields() {
        Employe employe = new Employe();
        employe.setMat(Integer.parseInt(mat.getText()));
        employe.setNom(nom.getText());
        employe.setSal(Float.parseFloat(sal.getText()));
        return employe;
    }

    private void clearFields() {
        mat.setText("");
        nom.setText("");
        sal.setText("");
    }
}
