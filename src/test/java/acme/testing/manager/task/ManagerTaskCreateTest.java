package acme.testing.manager.task;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class ManagerTaskCreateTest extends AcmePlannerTest {

	/* createPositive
	 *   Caso positivo de crear una Task como Manager autentificado.
	 *   No se infringe ninguna restricción.
	 *   Se espera que la Task se cree correctamente y se comprueben los atributos.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/task/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void createPositive(final int recordIndex, final String taskId, final String title, final String startMoment,
		final String endMoment, final String workloadHours, final String workloadFraction, final String description, final String link,
		final String isPublic) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Create task");
			
		super.fillInputBoxIn("taskId", taskId);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("startMoment", startMoment);
		super.fillInputBoxIn("endMoment", endMoment);
		super.fillInputBoxIn("workloadHours", workloadHours);
		super.fillInputBoxIn("workloadFraction", workloadFraction);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("isPublic", isPublic);
		
		super.clickOnSubmitButton("Create");
		
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
	
	/* createNegative
	 *   Caso negativo de crear una Task con errores.
	 *   Las restricciones que se infringen son fecha de fin anterior a la de inicio,
	 *   carga de trabajo con valor negativo, spam y valor de carga de trabajo y fracción de trabajo vacíos.
	 *   Se espera que salten los mensajes de error, y que no se creen las tareas.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/task/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(20)
	public void createNegative(final int recordIndex, final String taskId, final String title, final String startMoment,
		final String endMoment, final String workloadHours, final String workloadFraction, final String description, final String link,
		final String isPublic) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Create task");
		
		super.fillInputBoxIn("taskId", taskId);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("startMoment", startMoment);
		super.fillInputBoxIn("endMoment", endMoment);
		super.fillInputBoxIn("workloadHours", workloadHours);
		super.fillInputBoxIn("workloadFraction", workloadFraction);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("isPublic", isPublic);
		
		super.clickOnSubmitButton("Create");
		
		super.checkErrorsExist();	
		
		super.signOut();
	}
	
	
}