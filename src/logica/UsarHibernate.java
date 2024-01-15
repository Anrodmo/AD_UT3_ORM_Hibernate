package logica;

import java.util.logging.Level;
import java.util.logging.LogManager;

public class UsarHibernate {

	public static void main(String[] args) {
		/* Para eliminar los avisos de INFO de log */
		LogManager.getLogManager().getLogger("").setLevel(Level.SEVERE);
		
		/* Creamos la SesionFactory de la conexión*/
		HibernateEnterprise conexionHibernate = new HibernateEnterprise();
		
		
		
		/* Este método abre y cierra una sesión en la sesSesionFactoryión y hace el insert*/
		//conexionHibernate.addProduct(3, "Torre AMD", 55);
		
		/* Mostramos todos los productos de la BBDD*/
		conexionHibernate.mostrarProductos();
		
		/* Buscamos un producto por id*/
		conexionHibernate.encontrarProductoPorId(9);
		
		System.out.println("==================================================\n\n");
		
		/* Vamos a borrar por id, el 9 en este caso */
		
		//conexionHibernate.borrarProductoPorId(4);
		
		/* Vamos a modificar el id 5*/
		conexionHibernate.editarProductoPorId(55, "Radeon 5000", 465.99);
		
		/* Cerramos la SesionFactory*/ 
		conexionHibernate.cerrarSesionFactory();

	}

}
