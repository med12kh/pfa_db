package metier;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import dao.HibernateUtil;
import entity.Client;

public class ClientManager {

    private Session session;
    private Transaction tx;

    private void openSession() {
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
    }

    private void closeSession() {
        session.close();
    }

    // Insertion d'un nouveau client
    public String addClient(Client c) {
        try {
            openSession();
            session.persist(c);
            tx.commit();
            closeSession();
            return "Insertion Bien Déroulée";
        } catch (Exception e2) {
            if (tx != null) tx.rollback();
            return "Insert PB : " + e2.getMessage();
        }
    }

    // Suppression d'un client
    public String deleteClient(Client c) {
        try {
            openSession();
            Client client = session.get(Client.class, c.getId());
            if (client != null) {
                session.remove(client);
                tx.commit();
            } else {
                tx.rollback();
                return "Client introuvable pour suppression";
            }
            closeSession();
            return "Suppression Bien Déroulée";
        } catch (Exception e1) {
            if (tx != null) tx.rollback();
            return "Delete PB : " + e1.getMessage();
        }
    }

    // Mise à jour d'un client
    public String updateClient(Client c) {
        try {
            openSession();
            session.merge(c);
            tx.commit();
            closeSession();
            return "Mise à jour Bien Déroulée";
        } catch (Exception e2) {
            if (tx != null) tx.rollback();
            return "Update PB : " + e2.getMessage();
        }
    }

    // Afficher tous les clients
    public List<Client> allClients() {
        openSession();
        List<Client> clients = session.createQuery("from Client", Client.class).list();
        tx.commit();
        closeSession();
        return clients;
    }

    // Chercher un client par id
    public Client clientById(Long id) {
        openSession();
        Client client = session.get(Client.class, id);
        tx.commit();
        closeSession();
        return client;
    }
}
