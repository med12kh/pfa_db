package main;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import view.ChambreView;
import view.ClientView;
import view.EmployeView;
import view.ReservationView;
import view.ServiceView;

public class MainView extends JFrame {

private JPanel contentPanel;

public MainView() {
    initialize();
}

private void initialize() {
    setTitle("Hotel Reservation Management");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 600);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // Sidebar panel for navigation
    JPanel sideBar = new JPanel();
    sideBar.setPreferredSize(new Dimension(200, getHeight()));
    sideBar.setBackground(new Color(34, 49, 63));
    sideBar.setLayout(new GridLayout(6, 1, 10, 10));
    sideBar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

    JLabel title = new JLabel("Gestion Hôtel");
    title.setForeground(Color.WHITE);
    title.setFont(new Font("Segoe UI", Font.BOLD, 22));
    title.setHorizontalAlignment(SwingConstants.CENTER);
    sideBar.add(title);

    // Buttons with modern style
    JButton btnChambre = createSidebarButton("Chambres");
    btnChambre.addActionListener(e -> openView(new ChambreViewPanel()));

    JButton btnClient = createSidebarButton("Clients");
    btnClient.addActionListener(e -> openView(new ClientViewPanel()));

    JButton btnEmploye = createSidebarButton("Employés");
    btnEmploye.addActionListener(e -> openView(new EmployeViewPanel()));

    JButton btnReservation = createSidebarButton("Réservations");
    btnReservation.addActionListener(e -> openView(new ReservationViewPanel()));

    JButton btnService = createSidebarButton("Services");
    btnService.addActionListener(e -> openView(new ServiceViewPanel()));

    // Add buttons to sidebar
    sideBar.add(btnChambre);
    sideBar.add(btnClient);
    sideBar.add(btnEmploye);
    sideBar.add(btnReservation);
    sideBar.add(btnService);

    add(sideBar, BorderLayout.WEST);

    // Content panel to show views
    contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBackground(Color.WHITE);
    add(contentPanel, BorderLayout.CENTER);

    // Open default view
    openView(new ChambreViewPanel());
}

private JButton createSidebarButton(String text) {
    JButton btn = new JButton(text);
    btn.setFocusPainted(false);
    btn.setBackground(new Color(52, 73, 94));
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(41, 57, 80));
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(52, 73, 94));
        }
    });
    return btn;
}

private void openView(JPanel panel) {
    contentPanel.removeAll();
    contentPanel.add(panel, BorderLayout.CENTER);
    contentPanel.revalidate();
    contentPanel.repaint();
}

// Wrapper panels for existing views - adapted minimal panels embedding existing JFrame UI components
// Here, I show simplified placeholders. In real scenario, you should refactor each view class to provide a JPanel for embedding
// For now, let's make placeholder panels with simple labels and a button to launch the full JFrame view.

private class ChambreViewPanel extends JPanel {
    public ChambreViewPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Gestion des Chambres", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        JTextArea info = new JTextArea("Cliquez sur le bouton ci-dessous pour ouvrir la gestion des chambres.");
        info.setEditable(false);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBackground(getBackground());
        add(info, BorderLayout.CENTER);

        JButton openBtn = new JButton("Ouvrir Chambres");
        openBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        openBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ChambreView.main(new String[]{});
            });
        });
        JPanel btnPanel = new JPanel();
        btnPanel.add(openBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
}

private class ClientViewPanel extends JPanel {
    public ClientViewPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Gestion des Clients", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        JTextArea info = new JTextArea("Cliquez sur le bouton ci-dessous pour ouvrir la gestion des clients.");
        info.setEditable(false);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBackground(getBackground());
        add(info, BorderLayout.CENTER);

        JButton openBtn = new JButton("Ouvrir Clients");
        openBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        openBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ClientView.main(new String[]{});
            });
        });
        JPanel btnPanel = new JPanel();
        btnPanel.add(openBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
}

private class EmployeViewPanel extends JPanel {
    public EmployeViewPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Gestion des Employés", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        JTextArea info = new JTextArea("Cliquez sur le bouton ci-dessous pour ouvrir la gestion des employés.");
        info.setEditable(false);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBackground(getBackground());
        add(info, BorderLayout.CENTER);

        JButton openBtn = new JButton("Ouvrir Employés");
        openBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        openBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                EmployeView.main(new String[]{});
            });
        });
        JPanel btnPanel = new JPanel();
        btnPanel.add(openBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
}

private class ReservationViewPanel extends JPanel {
    public ReservationViewPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Gestion des Réservations", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        JTextArea info = new JTextArea("Cliquez sur le bouton ci-dessous pour ouvrir la gestion des réservations.");
        info.setEditable(false);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBackground(getBackground());
        add(info, BorderLayout.CENTER);

        JButton openBtn = new JButton("Ouvrir Réservations");
        openBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        openBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ReservationView.main(new String[]{});
            });
        });
        JPanel btnPanel = new JPanel();
        btnPanel.add(openBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
}

private class ServiceViewPanel extends JPanel {
    public ServiceViewPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Gestion des Services", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        JTextArea info = new JTextArea("Cliquez sur le bouton ci-dessous pour ouvrir la gestion des services.");
        info.setEditable(false);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBackground(getBackground());
        add(info, BorderLayout.CENTER);

        JButton openBtn = new JButton("Ouvrir Services");
        openBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        openBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ServiceView.main(new String[]{});
            });
        });
        JPanel btnPanel = new JPanel();
        btnPanel.add(openBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            MainView window = new MainView();
            window.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
}
}