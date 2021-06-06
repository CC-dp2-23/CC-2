package acme.testing.anonymous.shout;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.AcmePlannerTest;


public class AnonymousShoutCreateTest extends AcmePlannerTest {

	/* createPositive
	 *   Casos positivos de crear Shouts y sus Patatas.
	 *   No se infringe ninguna restricción.
	 *   Se espera que se creen los Shouts y sus Patatas correctamente.
	 **/
	@ParameterizedTest
	@CsvFileSource(resources = "/anonymous/shout/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(20)
	public void createPositive(final int recordIndex, final String author, final String text, final String info,
		final String patataMoment, final String patataValue, final String patataBoolean) {
		super.clickOnMenu("Anonymous", "Shout!");
		super.fillInputBoxIn("author", author);
		super.fillInputBoxIn("text", text);
		super.fillInputBoxIn("info", info);
		
		final String patataTicker = this.getTickerFechaActual(); // yyyy-mm-dd
		
		super.fillInputBoxIn("patata.patataTicker", patataTicker);
		super.fillInputBoxIn("patata.patataMoment", patataMoment);
		super.fillInputBoxIn("patata.patataValue", patataValue);
		super.fillInputBoxIn("patata.patataBoolean", patataBoolean);
		
		super.clickOnSubmitButton("Shout!");
		
		super.clickOnMenu("Anonymous", "Shouts list");
		
		super.checkColumnHasValue(recordIndex, 1, author);
		super.checkColumnHasValue(recordIndex, 2, text);
		super.checkColumnHasValue(recordIndex, 3, patataTicker);
		
		super.clickOnListingRecord(recordIndex);
		
		super.checkInputBoxHasValue("author", author);
		super.checkInputBoxHasValue("text", text);
		super.checkInputBoxHasValue("info", info);
		super.checkInputBoxHasValue("patata.patataTicker", patataTicker);
		super.checkInputBoxHasValue("patata.patataMoment", patataMoment);
		super.checkInputBoxHasValue("patata.patataValue", patataValue);
		super.checkInputBoxHasValue("patata.patataBoolean", patataBoolean);
		
	}
	
	/* createNegative
	 *   Casos negativos de crear Shouts.
	 *   Las restricciones que se infringen son:
	 *   -	 Parámetros vacios.
	 *   - 	 Spam.
	 *   -	 Url con formato incorrecto.
	 *   -	 patataTicker de la Patata con formato incorrecto.
	 *   -	 patataMoment no futuro.
	 *   -	 Divisa de patataValue incorrecta.
	 *   Se espera que aparezcan los mensajes de error, y que no se creen los gritos.
	 * */
	@ParameterizedTest
	@CsvFileSource(resources = "/anonymous/shout/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void createNegative(final int recordIndex, final String author, final String text, final String info, final String patataTicker, 
		final String patataMoment, final String patataValue, final String patataBoolean) {
		super.clickOnMenu("Anonymous", "Shout!");
		
		super.fillInputBoxIn("author", author);
		super.fillInputBoxIn("text", text);
		super.fillInputBoxIn("info", info);
		
		final String patataTickerAux = this.getTickerFechaActual();
		if(recordIndex != 0 && recordIndex != 3) {
			super.fillInputBoxIn("patata.patataTicker", patataTickerAux);
		} else {
			super.fillInputBoxIn("patata.patataTicker", patataTicker);
		}
		super.fillInputBoxIn("patata.patataMoment", patataMoment);
		super.fillInputBoxIn("patata.patataValue", patataValue);
		super.fillInputBoxIn("patata.patataBoolean", patataBoolean);
		super.clickOnSubmitButton("Shout!");
		
		super.checkErrorsExist();
	}
	
	private String getTickerFechaActual() {
		
		final StringBuilder ticker = new StringBuilder();
		
		final Date now = new Date(System.currentTimeMillis()-1);
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		final Integer year = calendar.get(Calendar.YEAR);
		final Integer month = calendar.get(Calendar.MONTH)+1;
		final Integer day = calendar.get(Calendar.DAY_OF_MONTH);
		
		ticker.append(year);
		ticker.append("-");
		if(month>9) {
			ticker.append(month);
			ticker.append("-");
		}else {
			ticker.append("0");
			ticker.append(month);
			ticker.append("-");
		}
		if(day>9) {
			ticker.append(day);
		}else {
			ticker.append("0");
			ticker.append(day);			
		}
		return ticker.toString();
		
	}
	
	
}