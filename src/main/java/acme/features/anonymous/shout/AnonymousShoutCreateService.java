/*
 * AnonymousShoutCreateService.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.anonymous.shout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.patatas.Patata;
import acme.entities.shouts.Shout;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.datatypes.Money;
import acme.framework.entities.Anonymous;
import acme.framework.services.AbstractCreateService;
import acme.utilities.SpamModule;
import acme.utilities.SpamModule.SpamModuleResult;
import acme.utilities.SpamRepository;

@Service
public class AnonymousShoutCreateService implements AbstractCreateService<Anonymous, Shout> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnonymousShoutRepository repository;
	
	@Autowired
	protected SpamRepository spamRepository;

	// AbstractCreateService<Anonymous, Shout> ---------------------------


	@Override
	public boolean authorise(final Request<Shout> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Shout> request, final Shout entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "author", "text", "info" );
		
	}

	@Override
	public Shout instantiate(final Request<Shout> request) {
		assert request != null;

		Shout result;
		Date moment;
		
		result = new Shout();
		moment = new Date(System.currentTimeMillis()-1);
		result.setMoment(moment);	
		
		return result;
	}

	@Override
	public void validate(final Request<Shout> request, final Shout entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		final SpamModule sm = new SpamModule(this.spamRepository);
		
		final SpamModuleResult spamResult = sm.checkSpam(entity);
		if(spamResult.isHasErrors()) {
			errors.state(request, false, "info", "anonymous.shout.form.error.spam.has-errors");
		} else if (spamResult.isSpam()){
			errors.state(request, false, "info", "anonymous.shout.form.error.spam.is-spam");
		}
		
		if(!errors.hasErrors("patataTicker")) {
			final String patataTicker = (String) request.getModel().getAttribute("patataTicker");
			if(patataTicker.equals("")) { 
				errors.state(request, false, "patataTicker", "anonymous.shout.form.error.patata-ticker-vacio");
			} else {
				if(!patataTicker.matches("^\\d{4}-\\d{2}-\\d{2}$")) { // "^"+author+"-\\d{4}-\\d{2}-\\d{2}$"
					errors.state(request, false, "patataTicker"	, "anonymous.shout.form.error.patata-ticker-pattern");
				} else {
					final String[] aux = patataTicker.split("-"); //YYYYMMDD
					final Date now = new Date(System.currentTimeMillis()-1);

					final Calendar calendar = Calendar.getInstance();
					calendar.setTime(now);
					final Integer year = calendar.get(Calendar.YEAR);
					final Integer month = calendar.get(Calendar.MONTH)+1;
					final Integer day = calendar.get(Calendar.DAY_OF_MONTH);
					
					final boolean errorDate = year.equals(Integer.parseInt(aux[0]))&&
									month.equals(Integer.parseInt(aux[1]))&&
									day.equals(Integer.parseInt(aux[2]));
					
					errors.state(request, errorDate, "patataTicker", "anonymous.shout.form.error.patata-ticker-pattern");
					
					if(this.repository.isPatataTickerExist(patataTicker)) {
						errors.state(request, false, "patataTicker", "anonymous.shout.form.error.patata-ticker-unique");
					}
				}
			}
		}
		
		if(!errors.hasErrors("patataMoment")) {
			final Date now = new Date(System.currentTimeMillis()-1);
			final String fecha = (String) request.getModel().getAttribute("patataMoment");
			if(!fecha.equals("")) {
				final String lang = request.getLocale().getLanguage();
				if(lang.equals("en")) {
					if(!fecha.matches("^\\s*\\d{4}\\/\\d{2}\\/\\d{2}\\s+\\d{2}\\:\\d{2}$")) {
						errors.state(request, false, "patataMoment", "anonymous.shout.form.error.patata-moment-malformed");
					} else {
						final Date patataMoment = request.getModel().getDate("patataMoment");
						errors.state(request, patataMoment.after(now), "patataMoment", "anonymous.shout.form.error.patata-moment");
					}
				} else if (lang.equals("es")) {
					if(!fecha.matches("^\\s*\\d{2}\\/\\d{2}\\/\\d{4}\\s+\\d{2}\\:\\d{2}$")) {
						errors.state(request, false, "patataMoment", "anonymous.shout.form.error.patata-moment-malformed");
					} else {
						final Date patataMoment = request.getModel().getDate("patataMoment");
						errors.state(request, patataMoment.after(now), "patataMoment", "anonymous.shout.form.error.patata-moment");
					}
				}
			} else {
				errors.state(request, false, "patataMoment", "anonymous.shout.form.error.patata-moment-vacio");
			}
		}
		
		if(!errors.hasErrors("patataValue")) {

			String patataValue = ((String) request.getModel().getAttribute("patataValue")).toLowerCase();
			
			if(!patataValue.equals("")) {
				if(!(patataValue.endsWith(" ") || patataValue.startsWith(" "))) {
					
					final List<String> currencies = Arrays.asList("eur","gbp");
					String currency = "";
					
					boolean multipleCurrency = false;
					for(final String s : currencies) {
						boolean repeatedCurrency = false;
						final Pattern pattern = Pattern.compile(s);
						final Matcher matcher = pattern.matcher(patataValue);
						if(matcher.find()) { 
							if(matcher.find()) {
								repeatedCurrency = true;
								errors.state(request, false, "patataValue", "anonymous.shout.form.error.patata-value-currency"); // The currencies available eur or gbd
							} 
						}
						if(patataValue.contains(s) && !repeatedCurrency) {
							if(multipleCurrency) {
								errors.state(request, false, "patataValue", "anonymous.shout.form.error.patata-value-currency"); // The currencies available eur or gbd
							}
							multipleCurrency = true;
							currency = s;
						}
	
					}
					final String lang = request.getLocale().getLanguage();
					if(!currency.equals("")) {
						patataValue = patataValue.replace(currency,"").trim();
						
						if(lang.equals("en")) {
							// 120,0.5
							final long countComma = patataValue.chars().filter(ch -> ch == ',').count();
							final long countPoint = patataValue.chars().filter(ch -> ch == '.').count();
							if(countComma > 1 || countPoint > 1) {
								errors.state(request, false, "patataValue", "anonymous.shout.form.error.patata-value-malformed");
							}
							// 1,900,000,000.44 -- 1,900,000,000
							if (!patataValue.contains(".")) {
								
							}
							
							patataValue = patataValue.replace(",","");
						} else if(lang.equals("es")) {
							patataValue = patataValue.replace(".", "");
							patataValue = patataValue.replace(",", ".");
						}
						
						if(!patataValue.equals("") && !patataValue.equals("-")) {
							if(patataValue.matches("^\\-?\\d*(\\.\\d{1,})?$")) { 
								final Double value = Double.valueOf(patataValue);
								errors.state(request, value>=0, "patataValue", "anonymous.shout.form.error.patata-value-negativo"); 
								if (value>=0 && !patataValue.matches("^\\d{0,10}(\\.\\d{1,2})?$")) {
									errors.state(request, false, "patataValue", "anonymous.shout.form.error.patata-value-range"); 
								}
							} else {
								errors.state(request, false, "patataValue", "anonymous.shout.form.error.patata-value-malformed");
							}
						} else {
							errors.state(request, false, "patataValue", "anonymous.shout.form.error.patata-value-malformed");
						}
					} else {
						errors.state(request, false, "patataValue", "anonymous.shout.form.error.patata-value-malformed"); 
					}
				} else {
					errors.state(request, false, "patataValue", "anonymous.shout.form.error.patata-value-malformed");
				}
			} else {
				errors.state(request, false, "patataValue", "anonymous.shout.form.error.patata-value-vacio"); 
			}
		}
	}
	
	@Override
	public void create(final Request<Shout> request, final Shout entity) {
		assert request != null;
		assert entity != null; 
		
		final String lang = request.getLocale().getLanguage();
		final String patataTicker = (String) request.getModel().getAttribute("patataTicker");
		final String patataMomentString = ((String) request.getModel().getAttribute("patataMoment")).trim().replaceAll("\\s+", " "); 
		SimpleDateFormat format = new SimpleDateFormat();
		if(lang.equals("en")) {
			format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		} else if (lang.equals("es")) {
			format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		}
		
		Date patataMoment = new Date();
		try {
			patataMoment = format.parse(patataMomentString);
		} catch (final ParseException e) {
		}
		
		final List<String> currencies = Arrays.asList("eur","gbp"); 
		String patataValue = ((String) request.getModel().getAttribute("patataValue")).toLowerCase();
		final Money money = new Money();
		for(final String s : currencies) {
			if(patataValue.contains(s)){
				money.setCurrency(s.toUpperCase());
				patataValue = patataValue.replace(s.toLowerCase(),"").trim();
				money.setAmount(Double.valueOf(patataValue));
			}
		}
		final Boolean patataBoolean = request.getModel().getBoolean("patataBoolean");
		
		final Patata patata = new Patata();
		patata.setPatataTicker(patataTicker);
		patata.setPatataMoment(patataMoment);
		patata.setPatataValue(money);
		patata.setPatataBoolean(patataBoolean);
		
		this.repository.save(patata);
		entity.setPatata(patata);

		Date moment;
		
		moment = new Date(System.currentTimeMillis()-1);
		entity.setMoment(moment);
		this.repository.save(entity);
	}

}
