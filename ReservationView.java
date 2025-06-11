package view;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import metier.ClientManager;
import metier.ChambreManager;
import metier.ReservationManager;
import entity.Client;
import entity.Chambre;
import entity.Reservation;
import main.MainView; // Import MainView

public class ReservationView {

    private JFrame frame;
    private JTextField id;
    private JSpinner dateDebutSpinner;
    private JSpinner dateFinSpinner;
    private JTextField montantTotal;
    private JComboBox<String> typePaiement; // New JComboBox
    private JComboBox<Client> clientComboBox;
    private JComboBox<Chambre> chambreComboBox;

    private ReservationManager reservationManager;
    private ClientManager clientManager;
    private ChambreManager chambreManager;

    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scroll;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private MainView mainView; // Reference to MainView

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ReservationView window = new ReservationView(null); // Pass null for MainView reference
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ReservationView(MainView mainView) { // Constructor accepting MainView reference
        this.mainView = mainView; // Store the reference
        initialize();
    }

    private void initialize() {
        reservationManager = new ReservationManager();
        clientManager = new ClientManager();
        chambreManager = new ChambreManager();

        frame = new JFrame();
        frame.setBounds(100, 100, 860, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Gestion des Réservations");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBounds(320, 10, 250, 30);
        frame.getContentPane().add(lblTitle);

        // Labels
        String[] labels = {"ID :", "Date Début :", "Date Fin :", "Montant Total :", "Type Paiement :", "Client :", "Chambre :"};
        int y = 60;

        // ID
        JLabel lblId = new JLabel(labels[0]);
        lblId.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblId.setBounds(100, y, 120, 20);
        frame.getContentPane().add(lblId);

        id = new JTextField();
        id.setBounds(220, y, 150, 25);
        frame.getContentPane().add(id);
        y += 40;

        // Date Debut
        JLabel lblDateDebut = new JLabel(labels[1]);
        lblDateDebut.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblDateDebut.setBounds(100, y, 120, 20);
        frame.getContentPane().add(lblDateDebut);

        dateDebutSpinner = new JSpinner(new SpinnerDateModel());
        dateDebutSpinner.setBounds(220, y, 150, 25);
        dateDebutSpinner.setEditor(new JSpinner.DateEditor(dateDebutSpinner, "yyyy-MM-dd"));
        frame.getContentPane().add(dateDebutSpinner);
        y += 40;

        // Date Fin
        JLabel lblDateFin = new JLabel(labels[2]);
        lblDateFin.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblDateFin.setBounds(100, y, 120, 20);
        frame.getContentPane().add(lblDateFin);

        dateFinSpinner = new JSpinner(new SpinnerDateModel());
        dateFinSpinner.setBounds(220, y, 150, 25);
        dateFinSpinner.setEditor(new JSpinner.DateEditor(dateFinSpinner, "yyyy-MM-dd"));
        frame.getContentPane().add(dateFinSpinner);
        y += 40;

        // Montant Total
        JLabel lblMontantTotal = new JLabel(labels[3]);
        lblMontantTotal.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblMontantTotal.setBounds(100, y, 120, 20);
        frame.getContentPane().add(lblMontantTotal);

        montantTotal = new JTextField();
        montantTotal.setBounds(220, y, 150, 25);
        frame.getContentPane().add(montantTotal);
        y += 40;

        // Type Paiement ComboBox
        JLabel lblTypePaiement = new JLabel(labels[4]);
        lblTypePaiement.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTypePaiement.setBounds(100, y, 120, 20);
        frame.getContentPane().add(lblTypePaiement);

        typePaiement = new JComboBox<>();
        typePaiement.addItem("Virement");
        typePaiement.addItem("Cash");
        typePaiement.setSelectedIndex(-1);
        typePaiement.setBounds(220, y, 150, 25);
        frame.getContentPane().add(typePaiement);
        y += 40;

        // Client ComboBox
        JLabel lblClient = new JLabel(labels[5]);
        lblClient.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblClient.setBounds(100, y, 120, 20);
        frame.getContentPane().add(lblClient);

        clientComboBox = new JComboBox<>();
        clientComboBox.setBounds(220, y, 200, 25);
        frame.getContentPane().add(clientComboBox);
        y += 40;

        // Chambre ComboBox
        JLabel lblChambre = new JLabel(labels[6]);
        lblChambre.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblChambre.setBounds(100, y, 120, 20);
        frame.getContentPane().add(lblChambre);

        chambreComboBox = new JComboBox<>();
        chambreComboBox.setBounds(220, y, 200, 25);
        frame.getContentPane().add(chambreComboBox);
        y += 40;

        loadClients();
        loadChambres();

        // Buttons
        JButton btnInsert = new JButton("Insert");
        btnInsert.addActionListener(e -> {
            Reservation reservation = createReservationFromFields();
            if (reservation != null) {
                JOptionPane.showMessageDialog(null, reservationManager.addReservation(reservation));
                loadReservations();
                clearFields();
            }
        });
        btnInsert.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnInsert.setBounds(450, 60, 100, 30);
        frame.getContentPane().add(btnInsert);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> {
            try {
                Long reservationId = Long.parseLong(id.getText());
                Reservation result = reservationManager.reservationById(reservationId);
                if (result != null) {
                    dateDebutSpinner.setValue(
                        Date.from(result.getDateDebut().atStartOfDay(ZoneId.systemDefault()).toInstant())
                    );
                    dateFinSpinner.setValue(
                        Date.from(result.getDateFin().atStartOfDay(ZoneId.systemDefault()).toInstant())
                    );
                    montantTotal.setText(String.valueOf(result.getMontantTotal()));
                    typePaiement.setSelectedItem(result.getTypePaiement());
                    clientComboBox.setSelectedItem(result.getClient());
                    chambreComboBox.setSelectedItem(result.getChambre());
                } else {
                    JOptionPane.showMessageDialog(null, "Réservation Inexistante !");
                    clearFields();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID invalide !");
            }
        });
        btnSearch.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSearch.setBounds(450, 110, 100, 30);
        frame.getContentPane().add(btnSearch);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            Reservation reservation = createReservationFromFields();
            if (reservation != null) {
                int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous modifier cette réservation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, reservationManager.updateReservation(reservation));
                    loadReservations();
                    clearFields();
                }
            }
        });
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnUpdate.setBounds(450, 160, 100, 30);
        frame.getContentPane().add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            try {
                Long reservationId = Long.parseLong(id.getText());
                int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous supprimer cette réservation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Reservation reservation = new Reservation();
                    reservation.setId(reservationId);
                    JOptionPane.showMessageDialog(null, reservationManager.deleteReservation(reservation));
                    loadReservations();
                    clearFields();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID invalide !");
            }
        });
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnDelete.setBounds(450, 210, 100, 30);
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
        btnBack.setBounds(450, 260, 100, 30);
        frame.getContentPane().add(btnBack);

        // Table for reservations
        model = new DefaultTableModel(new String[]{"ID", "Date Début", "Date Fin", "Montant Total", "Type Paiement", "Client", "Chambre"}, 0);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        scroll.setBounds(50, 380, 770, 280);
        frame.getContentPane().add(scroll);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                if (rowIndex != -1) {
                    id.setText(model.getValueAt(rowIndex, 0).toString());
                    try {
                        LocalDate dateDebutLocal = LocalDate.parse(model.getValueAt(rowIndex, 1).toString());
                        LocalDate dateFinLocal = LocalDate.parse(model.getValueAt(rowIndex, 2).toString());
                        dateDebutSpinner.setValue(Date.from(dateDebutLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        dateFinSpinner.setValue(Date.from(dateFinLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    } catch (Exception ex) {
                        // fallback: do nothing
                    }
                    montantTotal.setText(model.getValueAt(rowIndex, 3).toString());
                    typePaiement.setSelectedItem(model.getValueAt(rowIndex, 4));
                    clientComboBox.setSelectedItem(model.getValueAt(rowIndex, 5));
                    chambreComboBox.setSelectedItem(model.getValueAt(rowIndex, 6));
                }
            }
        });

        loadReservations();
    }

    private void loadClients() {
        clientComboBox.removeAllItems();
        List<Client> clients = clientManager.allClients();
        for (Client client : clients) {
            clientComboBox.addItem(client);
        }
        clientComboBox.setSelectedIndex(-1);
    }

    private void loadChambres() {
        chambreComboBox.removeAllItems();
        List<Chambre> chambresDisponibles = chambreManager.chambresDisponibles();
        for (Chambre chambre : chambresDisponibles) {
            chambreComboBox.addItem(chambre);
        }
        chambreComboBox.setSelectedIndex(-1);
    }

    private void loadReservations() {
        model.setRowCount(0);
        List<Reservation> reservations = reservationManager.allReservations();
        for (Reservation r : reservations) {
            model.addRow(new Object[]{
                    r.getId(),
                    r.getDateDebut(),
                    r.getDateFin(),
                    r.getMontantTotal(),
                    r.getTypePaiement(),
                    r.getClient(),
                    r.getChambre()
            });
        }
    }

    private Reservation createReservationFromFields() {
        try {
            if (id.getText().isEmpty() || montantTotal.getText().isEmpty() ||
                    typePaiement.getSelectedItem() == null || clientComboBox.getSelectedItem() == null ||
                    chambreComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Tous les champs doivent être remplis.");
                return null;
            }

            Date debutDate = (Date) dateDebutSpinner.getValue();
            Date finDate = (Date) dateFinSpinner.getValue();

            Reservation reservation = new Reservation();
            reservation.setId(Long.parseLong(id.getText()));
            reservation.setDateDebut(debutDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            reservation.setDateFin(finDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            reservation.setMontantTotal(Double.parseDouble(montantTotal.getText()));
            reservation.setStatut(""); // statut cleared
            reservation.setTypePaiement(typePaiement.getSelectedItem().toString());
            reservation.setClient((Client) clientComboBox.getSelectedItem());
            reservation.setChambre((Chambre) chambreComboBox.getSelectedItem());

            return reservation;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur de saisie : " + e.getMessage());
            return null;
        }
    }

    private void clearFields() {
        id.setText("");
        montantTotal.setText("");
        typePaiement.setSelectedIndex(-1);
        clientComboBox.setSelectedIndex(-1);
        chambreComboBox.setSelectedIndex(-1);
        dateDebutSpinner.setValue(new Date());
        dateFinSpinner.setValue(new Date());
    }
}
