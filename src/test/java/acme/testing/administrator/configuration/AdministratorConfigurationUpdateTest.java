package acme.testing.administrator.configuration;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class AdministratorConfigurationUpdateTest extends AcmePlannerTest {
	
	/* updatePositive
	 *   Caso positivo de actualizar la Configuration.
	 *   No se infringe ninguna restricción.
	 *   Se espera que se muestre la Configuration y se editen datos, y se comprueba que los atributos han sido editados correctamente.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/administrator/configuration/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void updatePositive(final String spamWordsES, final String spamWordsEN, final String threshold) {
		super.signIn("administrator", "administrator");
		
		super.clickOnMenu("Administrator", "Configuration");
		
		super.fillInputBoxIn("spamWordsES", spamWordsES);
		super.fillInputBoxIn("spamWordsEN", spamWordsEN);
		super.fillInputBoxIn("threshold", threshold);
		
		super.clickOnSubmitButton("Update");
		
		super.clickOnMenu("Administrator", "Configuration");
		
		super.checkInputBoxHasValue("spamWordsES", spamWordsES);
		super.checkInputBoxHasValue("spamWordsEN", spamWordsEN);
		super.checkInputBoxHasValue("threshold", threshold);
		
		super.signOut();
	}
	
	/* updateNegative
	 *   Caso negativo de actualizar la Configuration.
	 *   Restricciones que se infringen:
	 *   	- spamWordsES y spamWordsEN no deben ser vacios
	 *   	- threshold debe estar entre 0 y 100
	 *   Se espera que se muestre la Configuration y no se editen los datos al contener errores.
	 * */
	
	@ParameterizedTest
	@CsvFileSource(resources = "/administrator/configuration/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(20)
	public void updateNegative(final String spamWordsES, final String spamWordsEN, final String threshold) {
		super.signIn("administrator", "administrator");
		
		super.clickOnMenu("Administrator", "Configuration");
		
		super.fillInputBoxIn("spamWordsES", spamWordsES);
		super.fillInputBoxIn("spamWordsEN", spamWordsEN);
		super.fillInputBoxIn("threshold", threshold);
		
		super.clickOnSubmitButton("Update");
		
		super.checkErrorsExist();
		
		super.signOut();
	}

}
