package acme.testing.administrator.dashboard;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class AdministratorDashboardShowTest extends AcmePlannerTest {
	
	/* showPositive
	 *   Caso positivo de mostrar el Dashboard.
	 *   No se infringe ninguna restricción.
	 *   Se espera que se muestre el Dashboard, comprobando los atributos correctamente.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/administrator/dashboard/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void showPositive(final String numberOfPrivateTasks, final String numberOfPublicTasks, 
		final String numberOfFinishedTasks, final String numberOfUnfinishedTasks, final String minTaskPeriodsDays, 
		final String maxTaskPeriodsDays, final String avgTaskPeriodsDays, final String stddevTaskPeriodsDays, 
		final String minTaskWorkloadsHoursMinutes, final String maxTaskWorkloadsHoursMinutes, 
		final String avgTaskWorkloadsHoursMinutes, final String stddevTaskWorkloadsHoursMinutes, final String numberOfPrivateWorkPlans, 
		final String numberOfPublicWorkPlans, final String numberOfFinishedWorkPlans, final String numberOfUnfinishedWorkPlans, 
		final String minWorkPlanPeriodsDays, final String maxWorkPlanPeriodsDays, final String avgWorkPlanPeriodsDays, 
		final String stddevWorkPlanPeriodsDays, final String minWorkPlanWorkloadsHoursMinutes, final String maxWorkPlanWorkloadsHoursMinutes, 
		final String avgWorkPlanWorkloadsHoursMinutes, final String stddevWorkPlanWorkloadsHoursMinutes) {
		super.signIn("administrator", "administrator");
		
		super.clickOnMenu("Administrator", "Dashboard");
		
		super.checkInputBoxHasValue("numberOfPrivateTasks",numberOfPrivateTasks);
		super.checkInputBoxHasValue("numberOfPublicTasks",numberOfPublicTasks);
		super.checkInputBoxHasValue("numberOfFinishedTasks",numberOfFinishedTasks);
		super.checkInputBoxHasValue("numberOfUnfinishedTasks",numberOfUnfinishedTasks);
		super.checkInputBoxHasValue("minTaskPeriodsDays",minTaskPeriodsDays);
		super.checkInputBoxHasValue("maxTaskPeriodsDays",maxTaskPeriodsDays);
		super.checkInputBoxHasValue("avgTaskPeriodsDays",avgTaskPeriodsDays);
		super.checkInputBoxHasValue("stddevTaskPeriodsDays",stddevTaskPeriodsDays);
		super.checkInputBoxHasValue("minTaskWorkloadsHoursMinutes",minTaskWorkloadsHoursMinutes);
		super.checkInputBoxHasValue("maxTaskWorkloadsHoursMinutes",maxTaskWorkloadsHoursMinutes);
		super.checkInputBoxHasValue("avgTaskWorkloadsHoursMinutes",avgTaskWorkloadsHoursMinutes);
		super.checkInputBoxHasValue("stddevTaskWorkloadsHoursMinutes",stddevTaskWorkloadsHoursMinutes);
		super.checkInputBoxHasValue("numberOfPrivateWorkPlans",numberOfPrivateWorkPlans);
		super.checkInputBoxHasValue("numberOfPublicWorkPlans",numberOfPublicWorkPlans);
		super.checkInputBoxHasValue("numberOfFinishedWorkPlans",numberOfFinishedWorkPlans);
		super.checkInputBoxHasValue("numberOfUnfinishedWorkPlans",numberOfUnfinishedWorkPlans);
		super.checkInputBoxHasValue("minWorkPlanPeriodsDays",minWorkPlanPeriodsDays);
		super.checkInputBoxHasValue("maxWorkPlanPeriodsDays",maxWorkPlanPeriodsDays);
		super.checkInputBoxHasValue("avgWorkPlanPeriodsDays",avgWorkPlanPeriodsDays);
		super.checkInputBoxHasValue("stddevWorkPlanPeriodsDays",stddevWorkPlanPeriodsDays);
		super.checkInputBoxHasValue("minWorkPlanWorkloadsHoursMinutes",minWorkPlanWorkloadsHoursMinutes);
		super.checkInputBoxHasValue("maxWorkPlanWorkloadsHoursMinutes",maxWorkPlanWorkloadsHoursMinutes);
		super.checkInputBoxHasValue("avgWorkPlanWorkloadsHoursMinutes",avgWorkPlanWorkloadsHoursMinutes);
		super.checkInputBoxHasValue("stddevWorkPlanWorkloadsHoursMinutes",stddevWorkPlanWorkloadsHoursMinutes);

		super.signOut();
	}
	
	/* showNegative
	 *   Caso negativo de mostrar el Dashboard.
	 *   Se infringe restricción de acceso no autorizado.
	 *   Se espera que se recoja el panic de acceso no autorizado al intentar acceder al Dashboard sin ser Administrator.
	 * */
    @Test
    @Order(20)
    public void showNegative() {
		super.signIn("administrator", "administrator");
		
		super.clickOnMenu("Administrator", "Dashboard");
        
        final String currentUrl = super.getCurrentUrl();
        
        super.signOut(); // Deslogueamos para hacer saltar el panic de acceso no autorizado.
        
        super.navigate(currentUrl,null);

        super.checkPanicExists();
        
    }

}
