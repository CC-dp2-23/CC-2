package acme.testing.manager.task;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class ManagerTaskDeleteTest extends AcmePlannerTest {

	/* deletePositive
	 *   Caso positivo de borrar una Task como Manager autentificado.
	 *   No se infringe ninguna restricción.
	 *   Se espera que la Task se elimine correctamente.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/task/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void deletePositive(final int recordIndex, final String taskId, final String title, final String startMoment,
		final String endMoment, final String workloadHours, final String workloadFraction, final String description, final String link,
		final String isPublic) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Tasks list");
		
		super.clickOnListingRecord(recordIndex);
		
		super.clickOnSubmitButton("Delete");
				
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
		
		super.signIn("manager02", "manager02");
		
		super.clickOnMenu("Manager", "Workplans list");

		super.clickOnListingRecord(recordIndex);
		
		super.clickOnReturnButton("Tasks"); 
		 
		super.clickOnListingRecord(recordIndex);
		
		super.clickOnSubmitButton("Delete");
		
		super.signOut();
	
	}
	
	/* deleteNegative
	 *   Caso negativo de eliminar una Task.
	 *   La restricción que se infringe es intentar acceder a las Task siendo Anonymous
	 *   para borrarlas, siendo solo el propietario el que las puede borrar.
	 *   Se espera que salte un error de acceso no permitido y que no se pueda acceder.
	 * */
	@Test
	@Order(20)
	public void deleteNegative() {
		super.signIn("manager02", "manager02");
		
		super.clickOnMenu("Manager", "Tasks list");
		
		super.clickOnListingRecord(0);
		
		final String currentURL = super.getCurrentUrl();
		
		super.signOut();
		
		super.navigate(currentURL, null);
		
		super.checkPanicExists();		
	}
	
	
}