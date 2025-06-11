package metier;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import dao.HibernateUtil;
import entity.Employe;

public class EmployeManager {

    private Session session;
    private Transaction tx;

    private void openSession() {
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
    }

    private void closeSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    public EmployeManager() {
    }

    public String addEmploye(Employe e) {
        try {
            openSession();
            session.persist(e);
            tx.commit();
            return "Insertion Bien Déroulée";
        } catch (Exception e2) {
            if (tx != null) tx.rollback();
            return "Insert PB : " + e2.getMessage();
        } finally {
            closeSession();
        }
    }

    public String deleteEmploye(int mat) {
        try {
            openSession();
            Employe employe = session.get(Employe.class, mat);
            if (employe != null) {
                session.remove(employe);
                tx.commit();
                return "Suppression Bien Déroulée";
            } else {
                tx.rollback();
                return "Employé introuvable pour suppression";
            }
        } catch (Exception e1) {
            if (tx != null) tx.rollback();
            return "Delete PB : " + e1.getMessage();
        } finally {
            closeSession();
        }
    }

    public String updateEmploye(Employe e) {
        try {
            openSession();
            session.merge(e);
            tx.commit();
            return "Mise à jour Bien Déroulée";
        } catch (Exception e2) {
            if (tx != null) tx.rollback();
            return "Update PB : " + e2.getMessage();
        } finally {
            closeSession();
        }
    }

    public List<Employe> allEmployes() {
        try {
            openSession();
            List<Employe> employes = session.createQuery("from Employe", Employe.class).list();
            tx.commit();
            return employes;
        } finally {
            closeSession();
        }
    }

    public Employe employeByMat(int matId) {
        try {
            openSession();
            Employe employe = session.get(Employe.class, matId);
            tx.commit();
            return employe;
        } finally {
            closeSession();
        }
    }

	public Employe employeByMat(Employe employe) {
		// TODO Auto-generated method stub
		return null;
	}
}

