package view;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.*;
import java.util.List;

import metier.ChambreManager;
import entity.Chambre;
import main.MainView;

public class ChambreView {

    private JFrame frame;
    private JTextField id;
    private JTextField numero;
    private JTextField type;
    private JTextField prixNuitee;
    private JTextField description;
    private JCheckBox disponibilite;
    private ChambreManager manager;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;
    private MainView mainView; // Reference to MainView

    public ChambreView(MainView mainView) {
        this.mainView = mainView; // Store the reference
        initialize();
    }

    private void initialize() {
        manager = new ChambreManager();
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Gestion des Chambres");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBounds(300, 10, 250, 30);
        frame.getContentPane().add(lblTitle);

        String[] labels = {"ID :", "Numéro :", "Type :", "Prix Nuitée :", "Description :", "Disponible :"};
        int y = 60;
        JTextField[] fields = new JTextField[labels.length - 1];

        for (int i = 0; i < labels.length - 1; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Tahoma", Font.BOLD, 12));
            lbl.setBounds(100, y, 120, 20);
            frame.getContentPane().add(lbl);

            JTextField txt = new JTextField();
            txt.setBounds(220, y, 150, 20);
            frame.getContentPane().add(txt);
            fields[i] = txt;
            y += 40;
        }

        id = fields[0];
        numero = fields[1];
        type = fields[2];
        prixNuitee = fields[3];
        description = fields[4];

        JLabel lblDisponibilite = new JLabel(labels[5]);
        lblDisponibilite.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblDisponibilite.setBounds(100, y, 120, 20);
        frame.getContentPane().add(lblDisponibilite);

        disponibilite = new JCheckBox();
        disponibilite.setBounds(220, y, 150, 20);
        frame.getContentPane().add(disponibilite);

        JButton btnInsert = new JButton("Insert");
        btnInsert.addActionListener(e -> {
            Chambre chambre = createChambreFromFields();
            JOptionPane.showMessageDialog(null, manager.addChambre(chambre));
            loadChambres();
            clearFields();
        });
        btnInsert.setBounds(450, 60, 100, 30);
        frame.getContentPane().add(btnInsert);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> {
            Chambre result = manager.chambreById(Long.parseLong(id.getText()));
            if (result != null) {
                numero.setText(result.getNumero());
                type.setText(result.getType());
                prixNuitee.setText(String.valueOf(result.getPrixNuitee()));
                description.setText(result.getDescription());
                disponibilite.setSelected(result.isDisponibilite());
            } else {
                JOptionPane.showMessageDialog(null, "Chambre Inexistante !");
                clearFields();
            }
        });
        btnSearch.setBounds(450, 110, 100, 30);
        frame.getContentPane().add(btnSearch);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            Chambre chambre = createChambreFromFields();
            int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous modifier cette chambre ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, manager.updateChambre(chambre));
                loadChambres();
                clearFields();
            }
        });
        btnUpdate.setBounds(450, 160, 100, 30);
        frame.getContentPane().add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            Chambre chambre = new Chambre();
            chambre.setId(Long.parseLong(id.getText()));
            int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous supprimer cette chambre ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, manager.deleteChambre(chambre));
                loadChambres();
                clearFields();
            }
        });
        btnDelete.setBounds(450, 210, 100, 30);
        frame.getContentPane().add(btnDelete);

        // Back button to return to MainView
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            if (mainView != null) {
                mainView.setVisible(true);
            }
            frame.dispose();
        });
        btnBack.setBounds(450, 260, 100, 30);
        frame.getContentPane().add(btnBack);

        model = new DefaultTableModel(new String[]{"ID", "Numéro", "Type", "Prix", "Description", "Disponible"}, 0);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        scroll.setBounds(50, 320, 680, 300);
        frame.getContentPane().add(scroll);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                if (rowIndex != -1) {
                    id.setText(model.getValueAt(rowIndex, 0).toString());
                    numero.setText(model.getValueAt(rowIndex, 1).toString());
                    type.setText(model.getValueAt(rowIndex, 2).toString());
                    prixNuitee.setText(model.getValueAt(rowIndex, 3).toString());
                    description.setText(model.getValueAt(rowIndex, 4).toString());
                    disponibilite.setSelected(Boolean.parseBoolean(model.getValueAt(rowIndex, 5).toString()));
                }
            }
        });

        loadChambres();
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    private void loadChambres() {
        model.setRowCount(0);
        List<Chambre> chambres = manager.allChambres();
        for (Chambre c : chambres) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNumero(),
                c.getType(),
                c.getPrixNuitee(),
                c.getDescription(),
                c.isDisponibilite()
            });
        }
    }

    private Chambre createChambreFromFields() {
        Chambre c = new Chambre();
        c.setId(Long.parseLong(id.getText()));
        c.setNumero(numero.getText());
        c.setType(type.getText());
        c.setPrixNuitee(Double.parseDouble(prixNuitee.getText()));
        c.setDescription(description.getText());
        c.setDisponibilite(disponibilite.isSelected());
        return c;
    }

    private void clearFields() {
        id.setText("");
        numero.setText("");
        type.setText("");
        prixNuitee.setText("");
        description.setText("");
        disponibilite.setSelected(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ChambreView window = new ChambreView(null);
                window.showFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

