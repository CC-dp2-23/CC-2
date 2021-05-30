package acme.testing.manager.task;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class ManagerTaskListTest extends AcmePlannerTest {

	/* listPositive
	 *   Caso positivo de listar Tasks como Manager autentificado.
	 *   No se infringe ninguna restricción.
	 *   Se espera que las Tasks se muesten correctamente y se comprueben los atributos.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/task/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void listPositive(final int recordIndex, final String taskId, final String title, final String startMoment,
		final String endMoment, final String workloadHours, final String workloadFraction, final String description, final String link,
		final String isPublic) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Tasks list");
			
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
		super.checkInputBoxHasValue("isPublic", isPublic);
		
		super.signOut();
	}
	
	/* listNegative
	 *   Caso negativo de acceso a la lista de Tasks sin autentificarse.
	 *   La restricción que se infringe es la de acceso no autorizado al ser un usuario Anonymous el que intenta acceder.
	 *   Se espera que salte un panic de acceso no autorizado y que sea capturado.
	 * */
	@Test
	@Order(20)
	public void listNegative() {
		super.signIn("manager01", "manager01");
		super.clickOnMenu("Manager", "Tasks list");
		
		final String currentUrl = super.getCurrentUrl();
		
		super.signOut(); //Deslogueamos para hacer saltar el panic al acceder al listado desde anónimo
		
		super.navigate(currentUrl,null);

		super.checkPanicExists();		
	}
	
	
}