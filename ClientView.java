package view;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.*;
import java.util.List;

import metier.ClientManager;
import entity.Client;
import main.MainView;

public class ClientView {

    private JFrame frame;
    private JTextField id;
    private JTextField nom;
    private JTextField prenom;
    private JTextField email;
    private JTextField adresse;
    private JTextField tel;
    private ClientManager manager;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;
    private MainView mainView; // Reference to MainView

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ClientView window = new ClientView(null); // Pass null for MainView reference
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ClientView(MainView mainView) { // Constructor accepting MainView reference
        this.mainView = mainView; // Store the reference
        initialize();
    }

    private void initialize() {
        manager = new ClientManager();
        frame = new JFrame();
        frame.setBounds(100, 100, 850, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Gestion des Clients");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBounds(350, 10, 200, 30);
        frame.getContentPane().add(lblTitle);

        String[] labels = {"ID :", "Nom :", "Prénom :", "Email :", "Adresse :", "Téléphone :"};
        JTextField[] fields = new JTextField[labels.length];
        int y = 60;

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
        prenom = fields[2];
        email = fields[3];
        adresse = fields[4];
        tel = fields[5];

        JButton btnInsert = new JButton("Insert");
        btnInsert.addActionListener(e -> {
            Client client = createClientFromFields();
            JOptionPane.showMessageDialog(null, manager.addClient(client));
            loadClients();
            clearFields();
        });
        btnInsert.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnInsert.setBounds(450, 60, 100, 30);
        frame.getContentPane().add(btnInsert);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> {
            Long idClient = Long.parseLong(id.getText());
            Client result = manager.clientById(idClient);
            if (result != null) {
                nom.setText(result.getNom());
                prenom.setText(result.getPrenom());
                email.setText(result.getEmail());
                adresse.setText(result.getAdresse());
                tel.setText(result.getTel());
            } else {
                JOptionPane.showMessageDialog(null, "Client Inexistant !");
                clearFields();
            }
        });
        btnSearch.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSearch.setBounds(450, 110, 100, 30);
        frame.getContentPane().add(btnSearch);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            Client client = createClientFromFields();
            int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous modifier ce client ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, manager.updateClient(client));
                loadClients();
                clearFields();
            }
        });
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnUpdate.setBounds(450, 160, 100, 30);
        frame.getContentPane().add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            Long idClient = Long.parseLong(id.getText());
            int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous supprimer ce client ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Client client = new Client();
                client.setId(idClient);
                JOptionPane.showMessageDialog(null, manager.deleteClient(client));
                loadClients();
                clearFields();
            }
        });
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnDelete.setBounds(450, 210, 100, 30);
        frame.getContentPane().add(btnDelete);

        // Back button to return to MainView
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            if (mainView != null) {
                mainView.setVisible(true); // Show the MainView
            }
            frame.dispose(); // Close the current ClientView
        });
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnBack.setBounds(450, 260, 100, 30);
        frame.getContentPane().add(btnBack);

        model = new DefaultTableModel(new String[]{"ID", "Nom", "Prénom", "Email", "Adresse", "Téléphone"}, 0);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        scroll.setBounds(50, 350, 720, 280);
        frame.getContentPane().add(scroll);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                if (rowIndex != -1) {
                    id.setText(model.getValueAt(rowIndex, 0).toString());
                    nom.setText(model.getValueAt(rowIndex, 1).toString());
                    prenom.setText(model.getValueAt(rowIndex, 2).toString());
                    email.setText(model.getValueAt(rowIndex, 3).toString());
                    adresse.setText(model.getValueAt(rowIndex, 4).toString());
                    tel.setText(model.getValueAt(rowIndex, 5).toString());
                }
            }
        });

        loadClients();
    }

    public void showFrame() {
        frame.setVisible(true); // Method to show the frame
    }

    private void loadClients() {
        model.setRowCount(0);
        List<Client> clients = manager.allClients();
        for (Client client : clients) {
            model.addRow(new Object[]{
                client.getId(),
                client.getNom(),
                client.getPrenom(),
                client.getEmail(),
                client.getAdresse(),
                client.getTel()
            });
        }
    }

    private Client createClientFromFields() {
        Client client = new Client();
        client.setId(Long.parseLong(id.getText()));
        client.setNom(nom.getText());
        client.setPrenom(prenom.getText());
        client.setEmail(email.getText());
        client.setAdresse(adresse.getText());
        client.setTel(tel.getText());
        return client;
    }

    private void clearFields() {
        id.setText("");
        nom.setText("");
        prenom.setText("");
        email.setText("");
        adresse.setText("");
        tel.setText("");
    }
}
