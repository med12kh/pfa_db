package metier;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import dao.HibernateUtil;
import entity.Reservation;

public class ReservationManager {
    private Session session;
    private Transaction tx;

    private void openSession() {
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
    }

    private void closeSession() {
        session.close();
    }

    // Insertion d'une nouvelle réservation
    public String addReservation(Reservation r) {
        try {
            openSession();
            session.persist(r);  // Utilisation de persist pour l'insertion, client doit être déjà attaché ou en cascade
            tx.commit();
            closeSession();
            return "Insertion Bien Déroulée";
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            closeSession();
            e.printStackTrace();
            return "Insert PB : " + e.getMessage();
        }
    }

    // Suppression d'une réservation
    public String deleteReservation(Reservation r) {
        try {
            openSession();
            Reservation reservation = session.get(Reservation.class, r.getId());
            if (reservation != null) {
                session.remove(reservation);
                tx.commit();
            } else {
                tx.rollback();
                return "Réservation introuvable pour suppression";
            }
            closeSession();
            return "Suppression Bien Déroulée";
        } catch (Exception e1) {
            if (tx != null) tx.rollback();
            closeSession();
            return "Delete PB : " + e1.getMessage();
        }
    }

    // Mise à jour d'une réservation
    public String updateReservation(Reservation r) {
        try {
            openSession();
            session.merge(r);  // Utilisation de merge pour la mise à jour
            tx.commit();
            closeSession();
            return "Mise à jour Bien Déroulée";
        } catch (Exception e2) {
            if (tx != null) tx.rollback();
            closeSession();
            return "Update PB : " + e2.getMessage();
        }
    }

    // Afficher la liste de toutes les réservations
    public List<Reservation> allReservations() {
        openSession();
        List<Reservation> reservations = session.createQuery("from Reservation", Reservation.class).list();
        tx.commit();
        closeSession();
        return reservations;
    }

    // Afficher une réservation à partir de son id
    public Reservation reservationById(Long id) {
        openSession();
        Reservation reservation = session.get(Reservation.class, id);
        tx.commit();
        closeSession();
        return reservation;
    }
}
