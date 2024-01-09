package logica;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import miEmpresa.*;


public class HibernateEnterprise {
	/* Creamos una sesión que se usará para todas las conexiones*/
	private static SessionFactory sf;
	
	/* Constructor creamos la sesion*/
	public HibernateEnterprise() {
		sf=new Configuration().configure().buildSessionFactory();
	}
	
	public void addProduct(int id,String nombre, double precio ){
		// Abrimos una sesión para hacer el insert
		Session session=sf.openSession();
							//save para guardar productos
		Transaction tx=null;
		//create the product with the parameters in the method
		Productos p=new Productos();
		p.setNombre(nombre);
		p.setPrecio(precio);
		p.setId(id);
		//keep it in the database=session.save(p)
		try{
			System.out.printf("Inserting a row in the database: %s, %s, %s \n",id,nombre,precio);
			tx=session.beginTransaction();
			session.save(p);//we INSERT p into the table PRODUCTS
			tx.commit();//if session.save doesn't produce an exception, we "commit" the transaction
		}catch(Exception e){//if there is any exception, we "rollback" and close safely
			if (tx!=null) {
				tx.rollback();
			}
		}finally{
			session.close();   // Cerramos la sesión
		}
	}

	
}
