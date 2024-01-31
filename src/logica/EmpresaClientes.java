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
import miEmpresa.Productos;

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
	
	// no se incluye el id porque es auto incrementable
	public void añadirCliente(String nombre, String pais ){
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
	
	public void actualizarCliente(int id) {
		Clientes c; // aqui voy a guardar el cliente que borro
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

}
