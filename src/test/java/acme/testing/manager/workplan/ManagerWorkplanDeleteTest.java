package acme.testing.manager.workplan;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class ManagerWorkplanDeleteTest extends AcmePlannerTest {

	/* deletePositive
	 *   Caso positivo de borrar un Work Plan como Manager autentificado.
	 *   No se infringe ninguna restricción.
	 *   Se espera que el Work Plan se elimine correctamente.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/workplan/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void deletePositive(final int recordIndex, final String title, final String suggestion, final String executionPeriodStart, final String executionPeriodEnd,
		final String workload, final String validTaskIds, final String modelTasks, final String isPublic) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Workplans list");
		
		super.clickOnListingRecord(recordIndex);
		
		super.clickOnSubmitButton("Delete");
				
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, executionPeriodStart);
		super.checkColumnHasValue(recordIndex, 2, executionPeriodEnd); 
		
		super.clickOnListingRecord(recordIndex); 
		
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("suggestion", suggestion);
		super.checkInputBoxHasValue("executionPeriodStart", executionPeriodStart);
		super.checkInputBoxHasValue("executionPeriodEnd", executionPeriodEnd);
		super.checkInputBoxHasValue("workload", workload);
		super.checkInputBoxHasValue("validTaskIds", validTaskIds);
		super.checkInputBoxHasValue("modelTasks", modelTasks);
		super.checkInputBoxHasValue("isPublic", isPublic);
		
		super.signOut();
	
	}
	
	/* deleteNegative
	 *   Caso negativo de eliminar un Work Plan.
	 *   La restricción que se infringe es intentar acceder al Work Plan siendo Anonymous
	 *   para borrarlo, siendo solo el propietario el que lo puede borrar.
	 *   Se espera que salte un error de acceso no permitido y que no se pueda acceder.
	 * */
	@Test
	@Order(20)
	public void deleteNegative() {
		super.signIn("manager02", "manager02");
		
		super.clickOnMenu("Manager", "Workplans list");
		 
		super.clickOnListingRecord(0);
		
		final String currentURL = super.getCurrentUrl();
		
		super.signOut();
		
		super.navigate(currentURL, null);
		
		super.checkPanicExists();		
	}

}