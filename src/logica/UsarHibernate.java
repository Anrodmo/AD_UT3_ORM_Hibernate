package logica;

import java.util.logging.Level;
import java.util.logging.LogManager;

import miEmpresa.Clientes;

public class UsarHibernate {

	public static void main(String[] args) {
		/* Para eliminar los avisos de INFO de log */
		LogManager.getLogManager().getLogger("").setLevel(Level.SEVERE);
						
		EmpresaClientes conexionHibernateClientes = new EmpresaClientes();
		System.out.println("==================================================\n\n");
				
		conexionHibernateClientes.mostrarClientes();
		System.out.println("\n\n");
		conexionHibernateClientes.buscarPaisDe("luis");
		conexionHibernateClientes.cerrarSesionFactory();
		
		
//		HibernateEnterprise conexionHibernate = new HibernateEnterprise();
//		conexionHibernate.buscaProducto(10);
//		conexionHibernate.cerrarSesionFactory();
		/* Este método abre y cierra una sesión en la sesSesionFactoryión y hace el insert*/
		//conexionHibernate.addProduct(3, "Torre AMD", 55);
		
		/* Mostramos todos los productos de la BBDD*/
		//conexionHibernate.mostrarProductos();
		// conexionHibernate.mostrarProductosporNombre("monitor");
		/* Buscamos un producto por id*/
		//conexionHibernate.encontrarProductoPorId(9);
		/* Vamos a borrar por id, el 9 en este caso */
		
		//conexionHibernate.borrarProductoPorId(4);
		
		/* Vamos a modificar el id 5*/
		//conexionHibernate.editarProductoPorId(55, "Radeon 5000", 465.99);
		// conexionHibernate.mostrarProductosOrdenadosPorPrecio();
		
		//conexionHibernate.precioDe("Torre");
		
		//System.out.println("=====================================");
		//conexionHibernate.buscaProducto(2);
		/* Cerramos la SesionFactory*/ 
		

	}

}
