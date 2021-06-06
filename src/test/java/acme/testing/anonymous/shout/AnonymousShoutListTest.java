package acme.testing.anonymous.shout;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class AnonymousShoutListTest extends AcmePlannerTest {
	
	/* listPositive
	 *   Caso positivo de listar Shouts junto con el patataTicker de su Patata.
	 *   No se infringe ninguna restricción.
	 *   Se espera que se muestre el listado, se comprueban los valores de las columnas y se navega al show, 
	 *   	comprobando los atributos correctamente del Shout y su correspondiente Patata.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/anonymous/shout/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void listPositive(final int recordIndex, final String author, final String text, final String info, final String moment, 
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
	 *   Caso negativo de listar Shouts.
	 *   Se infringe restricción de acceso no autorizado.
	 *   Se espera que se recoja el panic de acceso no autorizado tras loguearnos como Authenticated e intentar acceder al listado.
	 * */
    @Test
    @Order(20)
    public void listNegative() {
        super.clickOnMenu("Anonymous", "Shouts list");
        
        final String currentUrl = super.getCurrentUrl();
        
        super.signIn("administrator","administrator"); //Logueamos como authenticated para hacer saltar el panic de acceso no autorizado.
        
        super.navigate(currentUrl,null);

        super.checkPanicExists();
        
        super.signOut();
    }

}
