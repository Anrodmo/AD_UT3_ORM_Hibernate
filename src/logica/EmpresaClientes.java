package logica;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import miEmpresa.Clientes;

public class EmpresaClientes {
	/* Creamos una sesión que se usará para todas las conexiones*/
	private static SessionFactory sf;
	
	/* Constructor creamos la sesión*/
	public EmpresaClientes() {
		sf=new Configuration().configure().buildSessionFactory();
	}
	
	public void cerrarSesionFactory() {
		sf.close();
	}
	
	
	/**
	 * Método que recoge y muestra todos los registros de la tabla clientes
	 */
	public void mostrarClientes(){
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;      // creamos una transacción		        
		
		try {	
	         tx = sesion.beginTransaction();
	         List allproducts = (sesion.createQuery("FROM Clientes")).list(); 

	         Iterator it =  allproducts.iterator();
	         while(it.hasNext()){
	        // for (Iterator iterator = allproducts.iterator(); iterator.hasNext();){
	            Clientes c = (Clientes) it.next(); 
	            System.out.print("Id: " + c.getId()); 
	            System.out.print("  ,Nombre: " + c.getNombre());	            
	            System.out.println("  ,Pais: " + c.getPais());  
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
	 * Método  que añade un cliente a la tabla clientes
	 * @param nombre  Nombre del cliente
	 * @param pais    País del cliente
	 */
	public void añadirCliente(String nombre, String pais ){  // no se incluye el id porque es auto incrementable
		// Abrimos la sesión para hacer el insert
		Session session=sf.openSession();
					// creamos una transacción		
		Transaction tx=null;
		// creamos un objeto cliente con los parámetros de entrada
		Clientes c=new Clientes();
		c.setNombre(nombre);
		c.setPais(pais);				
		try{
			System.out.printf("Insertando en la BBDD: %s, %s, \n",nombre,pais);
			tx=session.beginTransaction();  // comenzamos la transacción
			session.save(c);//Aquí guardamos el Producto p en la BBDD
			tx.commit();//Si no ha habido excepción hacemos un commit para confirmar la operación
			System.out.println("Cliente creado con éxito");
		}catch(Exception e){
			if (tx!=null) {
				System.out.println("Error en la creación del Cliente");
				tx.rollback(); //si hay una excepción hacemos rollback para evitar errores en la BBDD
			}
		}finally{
			session.close();   // Cerramos la sesión
		}
	}
	
	/**
	 * Método que elimina un cliente de la BBDD a partir de su ID
	 * @param id  ID del cliente que se quiere eliminar.
	 */
	public void borrarCliente(int id) {
		Clientes c; // aqui voy a guardar el cliente que borro
		Session session=sf.openSession();
		Transaction tx=null;  // creamos una transacción				
		try{
			tx=session.beginTransaction();
			c=(Clientes)session.get(Clientes.class, id);  // recupero el cliente y compruebo si existe a la vez
			if(c != null) {
				System.out.println("Borrando de la BBDD cliente con id: "+id);
				session.delete(c);;   // lo borro
				tx.commit();  // confirmo
				System.out.printf("... Cliente eliminado de la BBDD: %s, %s, %s \n",c.getId(),c.getNombre(),c.getPais());	
			}else {
				System.out.println("El cliente con id "+id+" no existe en la BBBDD");
			}
					
		}catch(Exception e){
			System.out.println("Error, al intentar eliminar al cliente.");
			if(tx!=null){
				tx.rollback();  // si hay excepción deshago lo que se haya quedado a medio
			}
		}finally{
			session.close();
		}		
	}
	
	/**
	 * Método que actual0zia un cliente a partir de su ID.. Según los parámetros del ejercicio
	 * este método debía pedir los datos a actualizar. Se llama al método auxiliar 
	 *  Cliente pedirDatosCliente(Cliente c) que dado un cliente pide los datos nuevos y devuelve el objeto
	 *  actualizado.
	 * @param id  del cliente a actualizar
	 */
	public void actualizarCliente(int id) {
		Clientes c; // aquí voy a guardar el cliente que borro
		Session session=sf.openSession();
		Transaction tx=null;  // creamos una transacción				
		try{
			tx=session.beginTransaction();
			c=(Clientes)session.get(Clientes.class, id);  // recupero el cliente y compruebo si existe a la vez
			if(c != null) {
				c= pedirDatosCliente(c);  // método privado auxiliar que pide y actualiza datos
				session.update(c);;   // lo actualizo en la BBDD
				tx.commit();  // confirmo
				System.out.printf("... Cliente actualziado en la BBDD: %s, %s, %s \n",c.getId(),c.getNombre(),c.getPais());	
			}else {
				System.out.println("El cliente con id "+id+" no existe en la BBDD");
			}
					
		}catch(Exception e){
			System.out.println("Error, al intentar eliminar al cliente.");
			if(tx!=null){
				tx.rollback();  // si hay excepción deshago lo que se haya quedado a medio
			}
		}finally{
			session.close();
		}		
	}
	
	/**
	 * Método auxiliar para actualizar datos de un cliente
	 * @param c Cliente a actualizar datos
	 * @return  Cliente actualizado
	 */
	private Clientes pedirDatosCliente(Clientes c) {
		Scanner teclado = new Scanner(System.in);
		String auxiliar;
		System.out.printf("Los datos del cliente actual son: %s, %s, %s \n",
				c.getId(),c.getNombre(),c.getPais());
		System.out.println("¿ Desea cambiar el nombre del cliente ?");
		System.out.print("Solo si es si.  si / no ->");
		auxiliar = teclado.next();
		if(auxiliar.equalsIgnoreCase("si")) {
			System.out.print("Introduzca el nuevo nombre de cliente -> ");
			c.setNombre(teclado.nextLine());
		}
		System.out.println("¿ Desea cambiar el pais del cliente ?");
		System.out.print("Solo si es si.  si / no ->");
		auxiliar = teclado.next();
		if(auxiliar.equalsIgnoreCase("si")) {
			System.out.print("Introduzca el nuevo pais de cliente -> ");
			c.setPais(teclado.nextLine());
		}		
		return c;
	}
	
	/**
	 * Método que elimina todos los cliente de la BBDD según su nombre
	 * @param nombre de clientes que se desean eliminar.
	 */
	public void borrarCliente(String nombre) {
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;    // consulta parametrizada
		String query = "FROM Clientes p WHERE p.nombre = :nombre";
		
		try {	
	         tx = sesion.beginTransaction();
	         List todosClientes = sesion.createQuery(query)
	        		 .setParameter("nombre", nombre)
	        		 .list(); 
	         if(todosClientes.size()>0) {
	        	 Iterator it =  todosClientes.iterator();
		         while(it.hasNext()){
		        // for (Iterator iterator = allproducts.iterator(); iterator.hasNext();){
		            Clientes c = (Clientes) it.next(); 
		            System.out.println("Borrando de la BBDD cliente con nombre: "+nombre);
		            sesion.delete(c);;   // lo borro
					
					System.out.printf("... Cliente eliminado de la BBDD: %s, %s, %s \n",c.getId(),c.getNombre(),c.getPais());	
		         }
		         tx.commit();  // si todo va bien confirmo	        	 
	         }else {
	        	 System.out.println("No se ha encontrado ningun cliente con nombre: "+nombre);	
	         }	         
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback(); //si hay una excepción hacemos rollback para evitar errores en la BBDD
			e.printStackTrace(); 
		}finally {
    	  sesion.close(); 
		}		
	}
	
	/**
	 * Método que recoge y muestra todos los registros de la tabla clientes de un país
	 * @param País de los clientes que se quieren mostrar
	 */
	public void mostrarClientesPorPais(String pais){
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;      // creamos una transacción
		String query = "FROM Clientes c Where c.pais = :pais";
		
		try {	
	         tx = sesion.beginTransaction();
	         List clientesPais = (sesion.createQuery(query))
	        		 .setParameter("pais", pais)
	        		 .list(); 

	         Iterator it =  clientesPais.iterator();
	         System.out.println("Hay un total de "+clientesPais.size()+" clientes de "+pais);
	         while(it.hasNext()){
	        // for (Iterator iterator = allproducts.iterator(); iterator.hasNext();){
	            Clientes c = (Clientes) it.next(); 
	            System.out.print("Id: " + c.getId()); 
	            System.out.print("  ,Nombre: " + c.getNombre());	            
	            System.out.println("  ,Pais: " + c.getPais());  
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
	 * Método que muestra una relación de todos los países de los clientes con el nombre facilitado
	 * @param Nombre del cliente cuyo país se quiere mostrar
	 */
	public void buscarPaisDe(String nombre){
		Session sesion = sf.openSession();  // abro sesión del SesionFactory
		Transaction tx = null;      // creamos una transacción
		String query = "FROM Clientes c Where c.nombre = :nombre";
		
		try {	// no doy por hecho que haya un único cliente con ese nombre
	         tx = sesion.beginTransaction();
	         List clientesPais = (sesion.createQuery(query))
	        		 .setParameter("nombre", nombre)
	        		 .list(); 

	         Iterator it =  clientesPais.iterator();
	         System.out.println("Relaciones de paises de clientes con el nombre: "+nombre);
	         while(it.hasNext()){
	        // for (Iterator iterator = allproducts.iterator(); iterator.hasNext();){
	            Clientes c = (Clientes) it.next();           
	            System.out.print("Nombre: " + c.getNombre());	            
	            System.out.println("  ,Pais: " + c.getPais());  
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback(); //si hay una excepción hacemos rollback para evitar errores en la BBDD
	         e.printStackTrace(); 
	      } finally {
	    	  sesion.close(); 
	      }

	}

}
