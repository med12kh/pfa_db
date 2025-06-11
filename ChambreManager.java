package metier;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import dao.HibernateUtil;
import entity.Chambre;

public class ChambreManager {
    private Session session;
    private Transaction tx;

    private void openSession() {
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
    }

    private void closeSession() {
        session.close();
    }

    // Insertion d'une nouvelle chambre
    public String addChambre(Chambre c) {
        try {
            openSession();
            session.persist(c);
            tx.commit();
            return "Insertion Bien Déroulée";
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return "Insert PB : " + e.getMessage();
        } finally {
            closeSession();
        }
    }

    // Suppression d'une chambre
    public String deleteChambre(Chambre c) {
        try {
            openSession();
            Chambre chambre = session.get(Chambre.class, c.getId());
            if (chambre != null) {
                session.remove(chambre);
                tx.commit();
                return "Suppression Bien Déroulée";
            } else {
                return "Chambre introuvable pour suppression";
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return "Delete PB : " + e.getMessage();
        } finally {
            closeSession();
        }
    }

    // Mise à jour d'une chambre
    public String updateChambre(Chambre c) {
        try {
            openSession();
            session.merge(c);
            tx.commit();
            return "Mise à jour Bien Déroulée";
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return "Update PB : " + e.getMessage();
        } finally {
            closeSession();
        }
    }

    // Afficher la liste de toutes les chambres
    public List<Chambre> allChambres() {
        try {
            openSession();
            List<Chambre> chambres = session.createQuery("from Chambre", Chambre.class).list();
            tx.commit();
            return chambres;
        } finally {
            closeSession();
        }
    }

    // Afficher une chambre à partir de son id
    public Chambre chambreById(Long id) {
        try {
            openSession();
            Chambre chambre = session.get(Chambre.class, id);
            tx.commit();
            return chambre;
        } finally {
            closeSession();
        }
    }

    // Obtenir les chambres disponibles
    public List<Chambre> chambresDisponibles() {
        try {
            openSession();
            List<Chambre> chambresDisponibles = session.createQuery("from Chambre where disponibilite = true", Chambre.class).list();
            tx.commit();
            return chambresDisponibles;
        } finally {
            closeSession();
        }
    }
}
