
package acme.features.administrator.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {
	
	@Query("select 1.0*sum(case when s.patata.patataBoolean = true then 1 else 0 end)/count(s) from Shout s")
	Double ratioOfFlaggedPatata();
	
	// select count(s) from Shout s where s.patata.patataMoment <= CURRENT_TIMESTAMP
	@Query("select 1.0*sum(case when s.patata.patataMoment <> null then 1 else 0 end)/count(s) from Shout s")
	Double ratioOfPatataWithXXX();
	
	@Query("select avg(s.patata.patataValue.amount), stddev(s.patata.patataValue.amount), s.patata.patataValue.currency from Shout s group by s.patata.patataValue.currency")
	Collection<Object[]> patataValueStats();
	

	@Query("select count(t) from Task t where t.isPublic = false")
	int numberOfPrivateTasks();

	@Query("select count(t) from Task t where t.isPublic = true")
	int numberOfPublicTasks();

	@Query("select count(t) from Task t where t.endMoment <= CURRENT_TIMESTAMP")
	int numberOfFinishedTasks();

	@Query("select count(t) from Task t where t.endMoment > CURRENT_TIMESTAMP")
	int numberOfUnfinishedTasks();

	@Query("select min(datediff(t.endMoment, t.startMoment)), max(datediff(t.endMoment, t.startMoment)), avg(datediff(t.endMoment, t.startMoment)), stddev(datediff(t.endMoment, t.startMoment)) from Task t")
	Collection<Object[]> taskPeriodsStats();

	@Query("select min(t.workloadHours*60+t.workloadFraction), max(t.workloadHours*60+t.workloadFraction), avg(t.workloadHours*60+t.workloadFraction), stddev(t.workloadHours*60+t.workloadFraction) from Task t")
	Collection<Object[]> taskWorkloadsStats();

	@Query("select count(wp) from Workplan wp where wp.isPublic = false")
	int numberOfPrivateWorkPlans();

	@Query("select count(wp) from Workplan wp where wp.isPublic = true")
	int numberOfPublicWorkPlans();

	@Query("select count(wp) from Workplan wp where wp.executionPeriodEnd <= CURRENT_TIMESTAMP")
	int numberOfFinishedWorkPlans();
	
	@Query("select count(wp) from Workplan wp where wp.executionPeriodEnd > CURRENT_TIMESTAMP")
	int numberOfUnfinishedWorkPlans();

	@Query("select min(datediff(wp.executionPeriodEnd, wp.executionPeriodStart)), max(datediff(wp.executionPeriodEnd, wp.executionPeriodStart)), avg(datediff(wp.executionPeriodEnd, wp.executionPeriodStart)), stddev(datediff(wp.executionPeriodEnd, wp.executionPeriodStart)) from Workplan wp")
	Collection<Object[]> workPlanPeriodsStats();

	@Query("select min(t.workloadHours*60+t.workloadFraction), max(t.workloadHours*60+t.workloadFraction), avg(t.workloadHours*60+t.workloadFraction), stddev(t.workloadHours*60+t.workloadFraction) from Workplan wp join wp.tasks as t")
	Collection<Object[]> workPlanWorkloadsStats();
	
	@Query("select count(wp) from Workplan wp")
	int numberOfWorkPlans();

	@Query("select count(wp) from Workplan wp where wp.isPublic = true")
	int numberOfPublishedWorkPlans();

	@Query("select count(wp) from Workplan wp where wp.isPublic = false")
	int numberOfUnpublishedWorkPlans();
}
