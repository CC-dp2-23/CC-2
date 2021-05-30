package acme.testing.anonymous.workplan;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class AnonymousWorkplanListTest extends AcmePlannerTest {
	
	/* listPositive
	 *   Caso positivo de listar Work Plans.
	 *   No se infringe ninguna restricción.
	 *   Se espera que se muestre el listado, se comprueban los valores de las columnas y se navega al show, 
	 *   	comprobando los atributos correctamente. Se comprueba la navegabilidad a las Tasks asociadas de cada Work Plan.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/anonymous/workplan/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void listPositive(final int recordIndex, final String title, final String executionPeriodStart, final String executionPeriodEnd, 
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
	
	/* listNegative
	 *   Caso negativo de listar Work Plans.
	 *   Se infringe restricción de acceso no autorizado.
	 *   Se espera que se recoja el panic de acceso no autorizado tras loguearnos como Authenticated e intentar acceder al listado.
	 * */
    @Test
    @Order(20)
    public void listNegative() {
        super.clickOnMenu("Anonymous", "Workplan list");
        
        final String currentUrl = super.getCurrentUrl();
        
        super.signIn("administrator","administrator"); //Logueamos como authenticated para hacer saltar el panic de acceso no autorizado.
        
        super.navigate(currentUrl,null);

        super.checkPanicExists();
        
        super.signOut();
    }

}
