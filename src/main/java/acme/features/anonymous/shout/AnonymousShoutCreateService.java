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

import java.util.Calendar;
import java.util.Date;

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

		request.unbind(entity, model, "author", "text", "info", "patata.patataTicker","patata.patataMoment","patata.patataValue","patata.patataBoolean");
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
		
		if(!errors.hasErrors("patata.patataTicker")) {
			final String patataTicker = (String) request.getModel().getAttribute("patata.patataTicker");

			if(!patataTicker.matches("^\\d{4}-\\d{2}-\\d{2}$")) { // "^"+author+"-\\d{4}-\\d{2}-\\d{2}$"
				errors.state(request, false, "patata.patataTicker"	, "anonymous.shout.form.error.patata-ticker-pattern");
			} else {
			/*  authorYYYYMMDD 
				
				String dd = patataTicker.substring(patataTicker.length()-2, patataTicker.length()); //DD
				String mm = patataTicker.substring(patataTicker.length()-4, patataTicker.length()-2); //MM
				String yyyy = patataTicker.substring(patataTicker.length()-8, patataTicker.length()-4); //YYYY
				String author = patataTicker.substring(0, patataTicker.length()-8); //author
			*/
				final String[] aux = patataTicker.split("-"); 
				
				final Date now = new Date(System.currentTimeMillis()-1);

				final Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				final Integer year = calendar.get(Calendar.YEAR);
				final Integer month = calendar.get(Calendar.MONTH)+1;
				final Integer day = calendar.get(Calendar.DAY_OF_MONTH);
				
				final boolean errorDate = year.equals(Integer.parseInt(aux[0]))&&
								month.equals(Integer.parseInt(aux[1]))&&
								day.equals(Integer.parseInt(aux[2]));
				
				errors.state(request, errorDate, "patata.patataTicker", "anonymous.shout.form.error.patata-ticker-pattern");
				
				if(this.repository.isPatataTickerExist(patataTicker)) {
					errors.state(request, false, "patata.patataTicker", "anonymous.shout.form.error.patata-ticker-unique");
				}
			}
			
			if(!errors.hasErrors("patata.patataMoment")) {
				final Date now = new Date(System.currentTimeMillis()-1);
				final Date fecha = request.getModel().getDate("patata.patataMoment");
				errors.state(request, fecha.after(now), "patata.patataMoment", "anonymous.shout.form.error.patata-moment");
			}
			
			if(!errors.hasErrors("patata.patataValue")) {
				final Money patataValue = request.getModel().getAttribute("patata.patataValue", Money.class);
				final boolean validCurrency = patataValue.getCurrency().equalsIgnoreCase("EUR")||patataValue.getCurrency().equalsIgnoreCase("GBP");
				errors.state(request, validCurrency, "patata.patataValue", "anonymous.shout.form.error.patata-value-currency");
			}
		}
	}
	
	@Override
	public void create(final Request<Shout> request, final Shout entity) {
		assert request != null;
		assert entity != null;

		Date moment;
		
		moment = new Date(System.currentTimeMillis()-1);
		entity.setMoment(moment);
		
		
		final String patataTicker = (String) request.getModel().getAttribute("patata.patataTicker");
		final Date patataMoment = request.getModel().getDate("patata.patataMoment"); 
		final Boolean patataBoolean = request.getModel().getBoolean("patata.patataBoolean");
		final Money patataValue = request.getModel().getAttribute("patata.patataValue", Money.class);
		
		final Patata patata = new Patata();
		
		patata.setPatataTicker(patataTicker);
		patata.setPatataMoment(patataMoment);
		patata.setPatataValue(patataValue);
		patata.setPatataBoolean(patataBoolean);
		
		entity.setPatata(patata);
		this.repository.save(patata);
		this.repository.save(entity);
		
	}

}
