package acme.testing.anonymous.shout;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class AnonymousShoutShowTest extends AcmePlannerTest {
	
	/* showPositive
	 *   Caso positivo de mostrar un Shout y su correspondiente Patata.
	 *   No se infringe ninguna restricción.
	 *   Se espera que se muestre el Shout y su correspondiente Patata, comprobando los atributos de ambos correctamente.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/anonymous/shout/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void showPositive(final int recordIndex, final String author, final String text, final String info, final String moment, 
		final String patataTicker, final String patataMoment, final String patataValue, final String patataBoolean) {
		super.clickOnMenu("Anonymous", "Shouts list");
		
		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, author);
		super.checkColumnHasValue(recordIndex, 2, text);
		super.checkColumnHasValue(recordIndex, 3, patataTicker);
		
		super.clickOnListingRecord(recordIndex);

		super.checkInputBoxHasValue("moment", moment);
		super.checkInputBoxHasValue("author", author);
		super.checkInputBoxHasValue("text", text);
		super.checkInputBoxHasValue("info", info);
		super.checkInputBoxHasValue("patata.patataTicker", patataTicker);
		super.checkInputBoxHasValue("patata.patataMoment", patataMoment);
		super.checkInputBoxHasValue("patata.patataValue", patataValue);
		super.checkInputBoxHasValue("patata.patataBoolean", patataBoolean);
	}
	
	/* listNegative
	 *   Caso negativo de mostrar un Shout.
	 *   Se infringe restricción de acceso no autorizado.
	 *   Se espera que se recoja el panic de acceso no autorizado tras loguearnos como Authenticated e intentar hacer show.
	 * */
    @Test
    @Order(20)
    public void showNegative() {
        super.clickOnMenu("Anonymous", "Shouts list");
        
        super.clickOnListingRecord(0);
        
        final String currentUrl = super.getCurrentUrl();
        
        super.signIn("administrator","administrator"); //Logueamos como authenticated para hacer saltar el panic de acceso no autorizado.
        
        super.navigate(currentUrl,null);

        super.checkPanicExists();
        
        super.signOut();
    }

}
