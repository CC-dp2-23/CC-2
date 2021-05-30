package acme.testing.anonymous.workplan;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class AnonymousWorkplanShowTest extends AcmePlannerTest {
	
	/* showPositive
	 *   Caso positivo de mostrar un Work Plan.
	 *   No se infringe ninguna restricción.
	 *   Se espera que se muestre el plan de trabajo, comprobando los atributos correctamente.
	 *   Se comprueba la navegabilidad a las Tasks asociadas al Work Plan.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/anonymous/workplan/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void showPositive(final int recordIndex, final String title, final String executionPeriodStart, final String executionPeriodEnd, 
		final String workload, final String isPublic) {
		super.clickOnMenu("Anonymous", "Workplan list");
		
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, executionPeriodStart);
		super.checkColumnHasValue(recordIndex, 2, executionPeriodEnd);
		super.checkColumnHasValue(recordIndex, 3, workload);
		
		super.clickOnListingRecord(recordIndex);
		
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("executionPeriodStart", executionPeriodStart);
		super.checkInputBoxHasValue("executionPeriodEnd", executionPeriodEnd);
		super.checkInputBoxHasValue("workload", workload);
		super.checkInputBoxHasValue("isPublic", isPublic);
		
		//Comprobamos que podemos visualizar las tareas del plan de trabajo
		super.clickOnReturnButton("Tasks");
		super.clickOnListingRecord(0); //visualizamos una de ellas

	}
	
	/* showNegative
	 *   Caso negativo de mostrar un Work Plan.
	 *   Se infringe restricción de acceso no autorizado.
	 *   Se espera que se recoja el panic de acceso no autorizado tras loguearnos como Authenticated e intentar hacer show.
	 * */
    @Test
    @Order(20)
    public void showNegative() {
        super.clickOnMenu("Anonymous", "Workplan list");
        super.clickOnListingRecord(0);
        
        final String currentUrl = super.getCurrentUrl();
        
        super.signIn("administrator","administrator"); //Logueamos como authenticated para hacer saltar el panic de acceso no autorizado.
        
        super.navigate(currentUrl,null);

        super.checkPanicExists();
        
        super.signOut();
    }

}
