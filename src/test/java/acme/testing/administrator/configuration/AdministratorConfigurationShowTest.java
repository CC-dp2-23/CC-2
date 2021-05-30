package acme.testing.administrator.configuration;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class AdministratorConfigurationShowTest extends AcmePlannerTest {
	
	/* showPositive
	 *   Caso positivo de mostrar la Configuration.
	 *   No se infringe ninguna restricción.
	 *   Se espera que se muestre la Configuration, comprobando los atributos correctamente.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/administrator/configuration/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void showPositive(final String spamWordsES, final String spamWordsEN, final String threshold) {
		super.signIn("administrator", "administrator");
		
		super.clickOnMenu("Administrator", "Configuration");
		
		super.checkInputBoxHasValue("spamWordsES", spamWordsES);
		super.checkInputBoxHasValue("spamWordsEN", spamWordsEN);
		super.checkInputBoxHasValue("threshold", threshold);
		
		super.signOut();
	}
	
	/* showNegative
	 *   Caso negativo de mostrar la Configuration.
	 *   Se infringe restricción de acceso no autorizado.
	 *   Se espera que se recoja el panic de acceso no autorizado al intentar acceder a la Configuration sin ser Administrator.
	 * */
    @Test
    @Order(20)
    public void showNegative() {
		super.signIn("administrator", "administrator");
		
		super.clickOnMenu("Administrator", "Configuration");
        
        final String currentUrl = super.getCurrentUrl();
        
        super.signOut(); // Deslogueamos para hacer saltar el panic de acceso no autorizado.
        
        super.navigate(currentUrl,null);

        super.checkPanicExists();
        
    }

}
