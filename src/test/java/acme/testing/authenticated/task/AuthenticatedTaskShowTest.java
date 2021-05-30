package acme.testing.authenticated.task;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class AuthenticatedTaskShowTest extends AcmePlannerTest {
	/* showPositive
	 *   Caso positivo de mostrar una Task como Authenticated.
	 *   No se infringe ninguna restricción.
	 *   Se espera que la Task se muestre correctamente y se compruebe los atributos.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/authenticated/task/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void showPositive(final int recordIndex, final String taskId, final String title, final String startMoment,
		final String endMoment, final String workloadHours, final String workloadFraction, final String description, final String link, final String ownerName) {
		super.signIn("administrator", "administrator");
		
		super.clickOnMenu("Authenticated", "Finished tasks list");
			
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, startMoment);
		super.checkColumnHasValue(recordIndex, 2, endMoment);
		super.checkColumnHasValue(recordIndex, 3, workloadHours);
		
		super.clickOnListingRecord(recordIndex);
		
		super.checkInputBoxHasValue("taskId", taskId);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("startMoment", startMoment);
		super.checkInputBoxHasValue("endMoment", endMoment);
		super.checkInputBoxHasValue("workloadHours", workloadHours);
		super.checkInputBoxHasValue("workloadFraction", workloadFraction);
		super.checkInputBoxHasValue("description", description);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("ownerName", ownerName);		
		
		super.signOut();
	}
	
	/* showNegative
	 *   Caso negativo de acceso al show de una Task sin autentificarse.
	 *   La restricción que se infringe es la de acceso no autorizado al ser un usuario Anonymous el que intenta acceder.
	 *   Se espera que salte un panic de acceso no autorizado y que sea capturado.
	 * */
	@Test
	@Order(20)
	public void showNegative() {
		super.signIn("administrator", "administrator");
		super.clickOnMenu("Authenticated", "Finished tasks list");
		super.clickOnListingRecord(0);
		
		final String currentUrl = super.getCurrentUrl();
		
		super.signOut(); //Deslogueamos para hacer saltar el panic al acceder al show desde anónimo
		
		super.navigate(currentUrl,null);

		super.checkPanicExists();			
	}
	
	
}