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

		request.unbind(entity, model, "author", "text", "info" );
		
		model.setAttribute("patataTicker", entity.getPatata().getPatataTicker());
		
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
			final String[] aux = patataTicker.split("-");
			if(aux.length==3) {
				final Date now = new Date(System.currentTimeMillis()-1);

				final Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				final Integer year = calendar.get(Calendar.YEAR);
				final Integer month = calendar.get(Calendar.MONTH);
				final Integer day = calendar.get(Calendar.DAY_OF_MONTH);
				
				final boolean errorDate = year.equals(Integer.getInteger(aux[0]))&&
								month.equals(Integer.getInteger(aux[1]))&&
								day.equals(Integer.getInteger(aux[2]));
				
				errors.state(request, errorDate, "patataTicker", "anonymous.shout.form.error.patata-ticker-date");
			} else {
				errors.state(request, false, "patataTicker", "anonymous.shout.form.error.patata-ticker-malformed");
			}	
		}
		
		if(!errors.hasErrors("patataMoment")) {
			final Date now = new Date(System.currentTimeMillis()-1);
			if(!((String) request.getModel().getAttribute("patataMoment")).equals("")) {
				final Date patataMoment = request.getModel().getDate("patataMoment");
				errors.state(request, patataMoment.after(now), "patataMoment", "anonymous.shout.form.error.patata-moment");
			} else {
				errors.state(request, false, "patataMoment", "anonymous.shout.form.error.patata-moment-vacio");
			}
		}
		
		if(!errors.hasErrors("patataValue")) {
			final String EURO = "€"; // A CAMBIAR
			final String LIBRA = "£"; // A CAMBIAR
			final Money money = (Money) request.getModel().getAttribute("patataValue");
			final String currency = money.getCurrency();
			errors.state(request, currency.equals(EURO)||currency.equals(LIBRA), "patataValue", "anonymous.shout.form.error.patata-currency");
		}
		
		
	}
	
	@Override
	public void create(final Request<Shout> request, final Shout entity) {
		assert request != null;
		assert entity != null;
		
		final String patataTicker = (String) request.getModel().getAttribute("patataTicker");
		final Date patataMoment = request.getModel().getDate("patataMoment");
		final String aux = (String) request.getModel().getAttribute("patataValue");
		final String [] aux2 = aux.split(" ");
		final Money money = new Money();
		money.setAmount(Double.valueOf(aux2[1]));
		money.setCurrency(aux2[0]);
		final Boolean patataBoolean = request.getModel().getBoolean("patataBoolean");
		
		final Patata patata = new Patata();
		patata.setPatataTicker(patataTicker);
		patata.setPatataMoment(patataMoment);
		patata.setPatataValue(money);
		patata.setPatataBoolean(patataBoolean);
		
		entity.setPatata(patata);
		this.repository.save(patata);

		Date moment;
		
		moment = new Date(System.currentTimeMillis()-1);
		entity.setMoment(moment);
		this.repository.save(entity);
	}

}
