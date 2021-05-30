package acme.testing.manager.workplan;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class ManagerWorkplanShowTest extends AcmePlannerTest {

	/* showPositive
	 *   Caso positivo de mostrar un Work Plan como Manager autentificado y navegar a sus Tasks.
	 *   No se infringe ninguna restricción.
	 *   Se espera que el Work Plan se muestre correctamente y se comprueben los atributos.
	 *   Se comprueba la navegabilidad a las Tasks asociadas al plan de trabajo
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/workplan/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void showPositive(final int recordIndex, final String title, final String suggestion, final String executionPeriodStart,
		final String executionPeriodEnd, final String workload, final String validTaskIds, final String modelTasks, final String isPublic) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Workplans list");
			
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, executionPeriodStart);
		super.checkColumnHasValue(recordIndex, 2, executionPeriodEnd);
		super.checkColumnHasValue(recordIndex, 3, workload); 
		
		super.clickOnListingRecord(recordIndex);
		
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("suggestion", suggestion);
		super.checkInputBoxHasValue("executionPeriodStart", executionPeriodStart);
		super.checkInputBoxHasValue("executionPeriodEnd", executionPeriodEnd);
		super.checkInputBoxHasValue("workload", workload);
		super.checkInputBoxHasValue("validTaskIds", validTaskIds);
		super.checkInputBoxHasValue("modelTasks", modelTasks);
		super.checkInputBoxHasValue("isPublic", isPublic);
		
		//Comprobamos que podemos visualizar las tareas del plan de trabajo
		super.clickOnReturnButton("Tasks");
		super.clickOnListingRecord(0); //visualizamos una de ellas
		
		super.signOut();
	}
	
	/* listNegative
	 *   Caso negativo de acceso a la lista de Work Plans sin autentificarse.
	 *   La restricción que se infringe es la de acceso no autorizado al ser un usuario Anonymous el que intenta acceder.
	 *   Se espera que salte un panic de acceso no autorizado y que sea capturado.
	 * */
	@Test
	@Order(20)
	public void showNegative() {
		super.signIn("manager01", "manager01");
		super.clickOnMenu("Manager", "Workplans list");
		
		super.clickOnListingRecord(0);
		
		final String currentUrl = super.getCurrentUrl();
		
		super.signOut(); // Deslogueamos para hacer saltar el panic al acceder al listado desde anónimo
		
		super.navigate(currentUrl,null);

		super.checkPanicExists(); // Acceso no autorizado		
	}
	
	
}