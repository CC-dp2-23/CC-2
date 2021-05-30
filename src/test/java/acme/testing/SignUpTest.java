/*
 * SignUpTest.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class SignUpTest extends AcmePlannerTest{

	/* positiveSingUp
	 *   Caso positivo de inscribir a varios usuarios nuevos al sistema correctamente e inicias sesión con cada uno de ellos.
	 *   No se infringe ninguna restricción.
	 *   Se espera que cada usuario se inscriba correctamente e inicie sesión.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/sign-up/positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveSignUp(final String username, final String password, final String name, final String surname, final String email) {
		super.signUp(username, password, name, surname, email);
		super.signIn(username, password);
		super.signOut();
	}
	
	
	/* negativeSingUp
	 *   Caso negativo de inscribir a un usuario en el sistema.
	 *   Las restricciones que se infringen son las de parámetros vacíos y que no se han aceptado los términos.
	 *   Se espera que salten los mensajes de error, y que el usuario no se inscriba.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/sign-up/negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(20)
	public void negativeSignUp(final String username, final String password, final String name, final String surname, final String email) {
		this.signUpNegative(username, password, name, surname, email);
	}
	
	// Método privado para probar el log in fallido, los parámetros son string vacíos
	private void signUpNegative(final String username, final String password, final String name, final String surname, final String email) {		
		super.navigateHome();
		super.clickOnMenu("Sign up", null);
		super.fillInputBoxIn("username", username);
		super.fillInputBoxIn("password", password);
		super.fillInputBoxIn("confirmation", password);
		super.fillInputBoxIn("identity.name", name);
		super.fillInputBoxIn("identity.surname", surname);
		super.fillInputBoxIn("identity.email", email);
		super.fillInputBoxIn("accept", "false");
		super.clickOnSubmitButton("Sign up");
		
		super.checkErrorsExist();
		
		super.checkSimplePath("/anonymous/user-account/create");
	}
	
	
}
