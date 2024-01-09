package logica;

public class UsarHibernate {

	public static void main(String[] args) {
		
		HibernateEnterprise conexionHibernate = new HibernateEnterprise();
		
		conexionHibernate.addProduct(3, "monitor", 170);
		
		

	}

}
