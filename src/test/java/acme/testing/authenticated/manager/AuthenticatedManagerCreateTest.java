package acme.testing.authenticated.manager;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import acme.testing.AcmePlannerTest;


public class AuthenticatedManagerCreateTest extends AcmePlannerTest {

	/*   positive
	 *   Caso positivo de convertirse en Manager como Authenticated.
	 *   No se infringe ninguna restricción.
	 *   Se espera que el usuario se convierta en Manager y pueda acceder a una opción del menú.
	 * */
	@Test
	@Order(10)
	public void positive() {
		super.signIn("authenticated", "authenticated");
		
		super.clickOnMenu("Account", "Become a manager");
		
		super.clickOnSubmitButton("Yes");
		
		super.clickOnMenu("Manager", "Tasks list");
		
		super.signOut();
	}
}