package metier;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import dao.HibernateUtil;
import entity.Service;

public class ServiceManager {

    private Session session;
    private Transaction tx;

    private void openSession() {
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
    }

    private void closeSession() {
        session.close();
    }

    public ServiceManager() {
    }

    // Insertion d'un nouveau service
    public String addService(Service s) {
        try {
            openSession();
            session.persist(s); 
            tx.commit();
            closeSession();
            return "Insertion Bien Déroulée";
        } catch (Exception e2) {
            if (tx != null) tx.rollback();
            return "Insert PB : " + e2.getMessage();
        }
    }

    // Suppression d'un service
    public String deleteService(Service s) {
        try {
            openSession();
            Service service = session.get(Service.class, s.getId());
            if (service != null) {
                session.remove(service);
                tx.commit();
            } else {
                tx.rollback();
                return "Service introuvable pour suppression";
            }
            closeSession();
            return "Suppression Bien Déroulée";
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return "Delete PB : " + e.getMessage();
        }
    }

    // Mise à jour d'un service
    public String updateService(Service s) {
        try {
            openSession();
            session.merge(s);
            tx.commit();
            closeSession();
            return "Mise à jour Bien Déroulée";
        } catch (Exception e2) {
            if (tx != null) tx.rollback();
            return "Update PB : " + e2.getMessage();
        }
    }

    // Afficher la liste de tous les services
    public List<Service> allServices() {
        openSession();
        List<Service> services = session.createQuery("from Service", Service.class).list();
        tx.commit();
        closeSession();
        return services;
    }

    // Afficher un service à partir de son ID
    public Service serviceById(int id) {
        openSession();
        Service service = session.get(Service.class, id);
        tx.commit();
        closeSession();
        return service;
    }
}
