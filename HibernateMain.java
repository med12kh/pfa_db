package demo;

import java.util.List;

import org.hibernate.Session;

import dao.HibernateUtil;
import entity.Employe;
import metier.EmployeManager;

public class HibernateMain {

	public static void main(String[] args) {
		//Permet d'ouvrir une session sur la base de données MYSQL
		//Session session=HibernateUtil.getSessionFactory().openSession();
		EmployeManager manager=new EmployeManager();
		Employe employe=new Employe();
		employe.setMat(2);employe.setNom("New Name");employe.setSal(13500);
		//manager.updateEmploye(employe);
		List<Employe> employes=manager.allEmployes();
		for (Employe e : employes) {
			System.out.println(e.getMat()+"\t"+e.getNom()+"\t"+e.getSal());
		}
		System.out.println("-----------------------------");
		Employe emp=manager.employeByMat(employe);
		if(emp!=null) {
			System.out.println(emp.getMat()+"\t"+emp.getNom()+"\t"+emp.getSal());
		}
		else {
			System.out.println("Employe Inexiste");
		}
	}

}






