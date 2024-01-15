package logica;



import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
//import org.hibernate.mapping.List;

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
	
	public void cerrarSesionFactory() {
		sf.close();
	}
	
	public void mostrarProductos(){
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;              // abro una conexión??
		
		try {	
	         tx = sesion.beginTransaction();
	         List allproducts = sesion.createQuery("FROM Productos").list(); 

	         Iterator it =  allproducts.iterator();
	         while(it.hasNext()){
	        // for (Iterator iterator = allproducts.iterator(); iterator.hasNext();){
	            Productos p = (Productos) it.next(); 
	            System.out.print("Id: " + p.getId()); 
	            System.out.print("  ,Name: " + p.getNombre()); 
	            System.out.println("  ,Price: " + p.getPrecio()); 
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	    	  sesion.close(); 
	      }

	}
	
	public Productos encontrarProductoPorId(int id) {
		Session session = sf.openSession();
		Transaction tx = null;
		Productos p=new Productos();

		try{
			System.out.println("loading the object from the database");
			tx=session.beginTransaction();
			p=(Productos)session.load(Productos.class, id);
			tx.commit();
			System.out.println("The product with id="+id+" is:"+p.getNombre());
			
			}catch(ObjectNotFoundException e){
				if(tx!=null){
					System.out.println(e);
					System.out.println("Product not found");			
				}
			}catch(Exception e){
				if(tx!=null){
					System.out.println(e);
					tx.rollback();
				}
			}finally{
				session.close();
			}
		
		return p;
	}
	
	
	public void borrarProductoPorId(int id) {
		Productos p = new Productos();
		Session session=sf.openSession();
		Transaction tx=null;
		
		try{
			tx=session.beginTransaction();
			p=(Productos)session.load(Productos.class, id);
			session.delete(p);
			tx.commit();
			System.out.printf("Object deleted FROM THE DATABASE: %s, %s, %s \n",p.getId(),p.getNombre(),p.getPrecio());
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();
			}
		}finally{
			session.close();
		}
		
	}
	
	public void editarProductoPorId(int id, String nuevoNombre, double nuevoPrecio) {
		Productos p = new Productos();
		Session session=sf.openSession();
		Transaction tx=null;
		
		try{
			tx=session.beginTransaction();
			System.out.println("Updating a value");
			System.out.println("Before updating, we need to load the object");
			p=(Productos)session.load(Productos.class, id);//we load the pen drive
			p.setPrecio(nuevoPrecio);//we change the properties 
			p.setNombre(nuevoNombre);
			session.update(p);//we update the values in the database
			tx.commit();
			System.out.println("Object updated");
			
		// añadimos esta excepción para poder tratar el caso	
		}catch(ObjectNotFoundException ex) {
			// podemos hacer que si no existe lo cree
			//this.addProduct(id, nuevoNombre, nuevoPrecio);
			// o solo que nos avise de que no existe producto con ese id
			System.out.println("No existe un Producto con el id: "+id);
		}catch(Exception e){
			System.out.println(e);
			if(tx!=null){
				tx.rollback();
			}
		}finally{
			session.close();
		}	
		
	}
	
	
}
