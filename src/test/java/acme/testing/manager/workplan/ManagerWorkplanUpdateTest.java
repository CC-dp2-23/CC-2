package acme.testing.manager.workplan;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class ManagerWorkplanUpdateTest extends AcmePlannerTest {

	/* updatePositive
	 *   Caso positivo de actualizar un Work Plan como Manager autentificado.
	 *   No se infringe ninguna restricción.
	 *   Se espera que el Work Plan se actualice correctamente y se comprueben los atributos.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/workplan/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void updatePositive(final int recordIndex, final String title, final String executionPeriodStart, final String executionPeriodEnd,
		final String modelTasks, final String isPublic) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Workplans list");

		super.clickOnListingRecord(recordIndex);
		
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("executionPeriodStart", executionPeriodStart);
		super.fillInputBoxIn("executionPeriodEnd", executionPeriodEnd);
		super.fillInputBoxIn("modelTasks", modelTasks);
		super.fillInputBoxIn("isPublic", isPublic);
		
		super.clickOnSubmitButton("Update");
		
		super.clickOnListingRecord(recordIndex);
		
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("executionPeriodStart", executionPeriodStart);
		super.checkInputBoxHasValue("executionPeriodEnd", executionPeriodEnd);
		super.checkInputBoxHasValue("modelTasks", modelTasks);
		super.checkInputBoxHasValue("isPublic", isPublic);
		
		super.signOut();
	}
	
	/* updateNegative
	 *   Caso negativo de actualizar un Work Plan como Manager autentificado.
	 *   Restricciones infringidas:
	 *   	- Añadir una Task que no puede formar parte del Work Plan.
	 *   	- Fecha executionPeriodEnd anterior a executionPeriodStart.
	 *   	- Spam.
	 *   	- Publicar un Work Plan con tareas en privado.
	 *   	- Intentar añadir una Task que no es propiedad del Manager al Work Plan.
	 *   Se espera que se capturen los errores y no se actualice el plan de trabajo.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/workplan/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(20)
	public void updateNegative(final int recordIndex, final String title, final String executionPeriodStart, final String executionPeriodEnd,
		final String modelTasks, final String isPublic) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Workplans list");

		super.clickOnListingRecord(recordIndex);
		
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("executionPeriodStart", executionPeriodStart);
		super.fillInputBoxIn("executionPeriodEnd", executionPeriodEnd);
		super.fillInputBoxIn("modelTasks", modelTasks);
		super.fillInputBoxIn("isPublic", isPublic);
		
		super.clickOnSubmitButton("Update");
		
		super.checkErrorsExist();
		
		super.signOut();
	}
	
	
	
	
}