
package acme.features.manager.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Manager;
import acme.entities.tasks.Task;
import acme.entities.workplans.Workplan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractDeleteService;

@Service
public class ManagerTaskDeleteService implements AbstractDeleteService<Manager, Task> {

	// Internal state

	@Autowired
	protected ManagerTaskRepository repository;

	// AbstractDeleteService<Manager, Task> interface

	@Override
	public boolean authorise(final Request<Task> request) {
		assert request != null;
		
		boolean res;
		int taskId;
		final Task task;
		final Manager manager;
		Principal principal;

		taskId = request.getModel().getInteger("id");
		task = this.repository.findOneTaskById(taskId);
		manager = task.getOwner();
		principal = request.getPrincipal();
		res = manager.getUserAccount().getId() == principal.getAccountId();
		
		return res;
	}

	@Override
	public void bind(final Request<Task> request, final Task entity, final Errors errors) {
		// Este método es heredado y no se ejecuta en ningún momento.
		// Su contenido ha sido borrado para que no interfiera con la cobertura.
	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		// Este método es heredado y no se ejecuta en ningún momento.
		// Su contenido ha sido borrado para que no interfiera con la cobertura.
	}

	@Override
	public Task findOne(final Request<Task> request) {
		assert request != null;

		final int id = request.getModel().getInteger("id");

		return this.repository.findOneTaskById(id);
	}

	@Override
	public void validate(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
	}

	@Override
	public void delete(final Request<Task> request, final Task entity) {
		assert request != null;
		assert entity != null;
		
		final Collection<Workplan> workplans = this.repository.findWorkplansByTask(request.getModel().getInteger("id"));
		
		for(final Workplan w: workplans) {
			
			final Collection<Task> tasks = w.getTasks();
			tasks.remove(entity);
	
			w.setTasks(tasks);
			
			this.repository.save(w);
		}
		
		this.repository.delete(entity);
	}

}
