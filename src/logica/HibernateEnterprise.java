package logica;



import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
//import org.hibernate.mapping.List;

import miEmpresa.*;


/**
 * 
 */
public class HibernateEnterprise {
	/* Creamos una sesión que se usará para todas las conexiones*/
	private static SessionFactory sf;
	
	/* Constructor creamos la sesión*/
	public HibernateEnterprise() {
		sf=new Configuration().configure().buildSessionFactory();
	}
	
	
	
	/**
	 * Método que añade un Producto a la BBDD a aprtir de sus atributos
	 * @param id
	 * @param nombre
	 * @param precio
	 */
	public void addProduct(int id,String nombre, double precio ){
		// Abrimos la sesión para hacer el insert
		Session session=sf.openSession();
					// creamos una transacción		
		Transaction tx=null;
		// creamos un objeto producto con los parámetros de entrada
		Productos p=new Productos();
		p.setNombre(nombre);
		p.setPrecio(precio);
		p.setId(id);
		
		try{
			System.out.printf("Insertando en la BBDD: %s, %s, %s \n",id,nombre,precio);
			tx=session.beginTransaction();  // comenzamos la transacción
			session.save(p);//Aquí guardamos el Producto p en la BBDD
			tx.commit();//Si no ha habido excepción hacemos un commit para confirmar la operación
		}catch(Exception e){
			if (tx!=null) {
				tx.rollback(); //si hay una excepción hacemos rollback para evitar errores en la BBDD
			}
		}finally{
			session.close();   // Cerramos la sesión
		}
	}
	
	public void cerrarSesionFactory() {
		sf.close();
	}
	
	/**
	 * Método que muestra por consola todos los registros de la tabla Productos
	 */
	public void mostrarProductos(){
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;      // creamos una transacción		        
		
		try {	
	         tx = sesion.beginTransaction();
	         List allproducts = (sesion.createQuery("FROM Productos")).list(); 

	         Iterator it =  allproducts.iterator();
	         while(it.hasNext()){
	        // for (Iterator iterator = allproducts.iterator(); iterator.hasNext();){
	            Productos p = (Productos) it.next(); 
	            System.out.print("Id: " + p.getId()); 
	            System.out.print("  ,Nombre: " + p.getNombre()); 
	            System.out.println("  ,Precio: " + p.getPrecio());  
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback(); //si hay una excepción hacemos rollback para evitar errores en la BBDD
	         e.printStackTrace(); 
	      } finally {
	    	  sesion.close(); 
	      }

	}
	
	/**
	 * Método que muestra por consola todos los registros de la tabla productos que tengan el
	 * nombre facilitado.
	 * @param nombre
	 */
	public void mostrarProductosporNombre(String nombre) {
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;    // consulta parametrizada
		String query = "FROM Productos p WHERE p.nombre = :nombre";
		
		try {	// entiendo que podría haber mas de un producto con el mismo nombre
	         tx = sesion.beginTransaction();
	         List allproducts = sesion.createQuery(query)
	        		 .setParameter("nombre", nombre)
	        		 .list(); 
	         
	         Iterator it =  allproducts.iterator();
	         while(it.hasNext()){
	        // for (Iterator iterator = allproducts.iterator(); iterator.hasNext();){
	            Productos p = (Productos) it.next(); 
	            System.out.print("Id: " + p.getId()); 
	            System.out.print("  ,Nombre: " + p.getNombre()); 
	            System.out.println("  ,Precio: " + p.getPrecio()); 
	         }
	         tx.commit();
			}catch(ObjectNotFoundException e){
				if(tx!=null){
					e.printStackTrace();  // si no lo encuentro informo
					System.out.println("Producto no encontrado");			
				}
			}catch (HibernateException e) {
				if (tx!=null) tx.rollback(); //si hay una excepción hacemos rollback para evitar errores en la BBDD
				e.printStackTrace(); 
			}finally {
	    	  sesion.close(); 
			}		
	}
	
	/**
	 * Método que muestra por consola el precio de todos los productos cuyo nombre coincida con el
	 * facilitado por parámetro. 
	 * @param nombre
	 */
	public void precioDe(String nombre) {
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;    // consulta parametrizada
		String query = "SELECT DISTINCT p.precio FROM Productos p WHERE p.nombre = :nombre";
		
		try {	
	         tx = sesion.beginTransaction();
	         List preciosUnicos = sesion.createQuery(query)
	        		 .setParameter("nombre", nombre)
	        		 .list(); 

	         Iterator it =  preciosUnicos.iterator();
	         System.out.println("Precios de: " + nombre); 
	         while(it.hasNext()) {
	            double precio = (double) it.next(); 	            
	            System.out.println(precio); 	            
	         }
	         tx.commit();  // confirmamos operación en la BBDD
			}catch(ObjectNotFoundException e){
				if(tx!=null){
					e.printStackTrace();  // si no lo encuentro informo
					System.out.println("Producto con nombre: "+nombre+" no encontrado");			
				}
			}catch (HibernateException e) {
				if (tx!=null) tx.rollback(); //si hay una excepción hacemos rollback para evitar errores en la BBDD
				e.printStackTrace(); 
			}finally {
	    	  sesion.close(); 
			}		
	}
	
	/**
	 * Método que muestra por consola el producto con la ID facilitada
	 * @param id
	 */
	public void buscaProducto(int id) {
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;              // creamos una transacción		
		String consulta = "FROM Productos p WHERE p.id = :id";
		Productos p = new Productos();
		
		try {
			tx = sesion.beginTransaction();
			Query query = sesion.createQuery(consulta).setParameter("id", id);
			// query.setMaxResults(10); podría indicar el numero máximo de resultados a obtener
			p = (Productos) query.uniqueResult();
			
			System.out.print("Id: " + p.getId()); 
	        System.out.print("  ,Nombre: " + p.getNombre()); 
	        System.out.println("  ,Precio: " + p.getPrecio()); 
		}catch(ObjectNotFoundException e){
			if(tx!=null){
				e.printStackTrace();  // si no lo encuentro informo
				System.out.println("Producto con id: "+id+" no encontrado");			
			}
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback(); //si hay una excepción hacemos rollback para evitar errores en la BBDD
			e.printStackTrace(); 
		}finally {
    	  sesion.close(); 
		}
	}
	
	/**
	 * Método que muestra por consola todos los productos ordenados por precio
	 * en orden ascendente. 
	 */
	public void mostrarProductosOrdenadosPorPrecio() {
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;              // creamos una transacción		
		
		try {	
	         tx = sesion.beginTransaction();
	         List allproducts = sesion.createQuery("FROM Productos p ORDER BY p.precio").list(); 

	         Iterator it =  allproducts.iterator();   // iteramos por la lista mostrando los Productos
	         while(it.hasNext()){
	        // for (Iterator iterator = allproducts.iterator(); iterator.hasNext();){
	            Productos p = (Productos) it.next(); 
	            System.out.print("Id: " + p.getId()); 
	            System.out.print("  ,Nombre: " + p.getNombre()); 
	            System.out.println("  ,Precio: " + p.getPrecio());          
	         }
	         tx.commit();	      
		} catch (HibernateException e) {	         
			if (tx!=null) tx.rollback(); //si hay una excepción hacemos rollback para evitar errores en la BBDD	         
			e.printStackTrace();  	      
		} finally {	    	  
			sesion.close(); 	      
		}
	}
	
	
	
	/**
	 * Método que busca un registro producto por su ID en la BBDD y devuelve un objeto Producto con sus atributos.
	 * @param id
	 * @return Producto p
	 */
	public Productos encontrarProductoPorId(int id) {
		Session session = sf.openSession();
		Transaction tx = null;  // creamos una transacción		
		Productos p=new Productos();

		try{
			System.out.println("Cargando objeto de la base de datos");
			tx=session.beginTransaction();
			p=(Productos)session.load(Productos.class, id);
			tx.commit();
			System.out.println("Producto con id="+id+" es:"+p.getNombre());
			
			}catch(ObjectNotFoundException e){
				if(tx!=null){
					e.printStackTrace();
					System.out.println("Producto no encontrado");			
				}
			}catch(Exception e){
				if(tx!=null){
					e.printStackTrace();
					tx.rollback();  //si hay una excepción hacemos rollback para evitar errores en la BBDD
				}
			}finally{
				session.close();
			}
		
		return p;
	}
	
	
	/**
	 * Método que borra un producto a partir de su id.
	 * @param id
	 */
	public void borrarProductoPorId(int id) {
		Productos p = new Productos();
		Session session=sf.openSession();
		Transaction tx=null;  // creamos una transacción		
		
		try{
			tx=session.beginTransaction();
			p=(Productos)session.load(Productos.class, id);  // primero cargamos el producto en la session
			session.delete(p);  // luego lo borramos
			tx.commit();   // confirmamos
			System.out.printf("Objeto eliminado de la BBDD: %s, %s, %s \n",p.getId(),p.getNombre(),p.getPrecio());
		}catch(Exception e){
			if(tx!=null){
				tx.rollback();  // o volvemos atrás si ha habido algún error
			}
		}finally{
			session.close();
		}
		
	}
	
	/**
	 * Método que edita un registro de producto en la BBDD a partir de los datos facilitados
	 * @param id
	 * @param nuevoNombre
	 * @param nuevoPrecio
	 */
	public void editarProductoPorId(int id, String nuevoNombre, double nuevoPrecio) {
		Productos p = new Productos();
		Session session=sf.openSession();
		Transaction tx=null;  // creamos una transacción		
		
		try{
			tx=session.beginTransaction();
			System.out.println("Updating a value");
			System.out.println("Before updating, we need to load the object");
			p=(Productos)session.load(Productos.class, id);//cargo el producto en la sesion
			p.setPrecio(nuevoPrecio);//le asignamos las nuevas propiedades
			p.setNombre(nuevoNombre);
			session.update(p);//lo modificamos en la BBDD con las nuevas propiedades
			tx.commit();  // si todo va bien confirmamos los cambios
			System.out.println("Object updated");
			
		// añadimos esta excepción para poder tratar el caso	
		}catch(ObjectNotFoundException ex) {
			// podemos hacer que si no existe lo cree
			// this.addProduct(id, nuevoNombre, nuevoPrecio);
			// o solo que nos avise de que no existe producto con ese id
			System.out.println("No existe un Producto con el id: "+id);
		}catch(Exception e){
			System.out.println(e);
			if(tx!=null){
				tx.rollback();  // si hay error revertimos
			}
		}finally{
			session.close();
		}	
		
	}
	
	
}
