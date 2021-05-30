package acme.testing.manager.workplan;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class ManagerWorkplanCreateTest extends AcmePlannerTest {

	/* createPositive
	 *   Caso positivo de crear un Work Plan como Manager autentificado.
	 *   No se infringe ninguna restricción.
	 *   Se espera que el Work Plan se cree correctamente y se comprueben los atributos.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/workplan/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void createPositive(final int recordIndex, final String title, final String executionPeriodStart, final String executionPeriodEnd, final String isPublic) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Create workplan");
		
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("executionPeriodStart", executionPeriodStart);
		super.fillInputBoxIn("executionPeriodEnd", executionPeriodEnd);
		
		super.clickOnSubmitButton("Create");
		
		super.clickOnMenu("Manager", "Workplans list");
		
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, executionPeriodStart);
		super.checkColumnHasValue(recordIndex, 2, executionPeriodEnd);

		super.clickOnListingRecord(recordIndex);
		
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("executionPeriodStart", executionPeriodStart);
		super.checkInputBoxHasValue("executionPeriodEnd", executionPeriodEnd);
		super.checkInputBoxHasValue("isPublic", isPublic);
		
		super.signOut();
	}
	
	/* createNegative
	 *   Caso negativo de crear un Work Plan como Manager autentificado.
	 *   Restricciones infringidas:
	 *   	- Parámetros vacíos (NotBlank).
	 *   	- Fecha executionPeriodEnd anterior a executionPeriodStart.
	 *   	- Fechas con formato no adecuado.
	 *   Se espera que se capturen los errores y no se cree el Work Plan.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/manager/workplan/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(20)
	public void createNegative(final int recordIndex, final String title, final String executionPeriodStart, final String executionPeriodEnd) {
		super.signIn("manager01", "manager01");
		
		super.clickOnMenu("Manager", "Create workplan");
		
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("executionPeriodStart", executionPeriodStart);
		super.fillInputBoxIn("executionPeriodEnd", executionPeriodEnd);
		
		super.clickOnSubmitButton("Create");
		
		super.checkErrorsExist();
		
		super.signOut();
	}
}