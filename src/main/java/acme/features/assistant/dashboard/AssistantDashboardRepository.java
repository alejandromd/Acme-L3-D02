
package acme.features.assistant.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantDashboardRepository extends AbstractRepository {

	@Query("SELECT count(t) FROM Tutorial t WHERE t.assistant.id = :assistantId")
	Integer findNumberOfTutorialsByAssistantId(int assistantId);

	@Query("SELECT a FROM Assistant a WHERE a.userAccount.id = :userAccountId")
	Assistant findOneAssistantByUserAccountId(int userAccountId);

	@Query(value = "SELECT avg(timestampdiff(hour, s.start_timestamp, s.end_timestamp)) FROM tutorial_session s left join tutorial t on s.tutorial_id = t.id WHERE t.assistant_id = :assistantId", nativeQuery = true)
	Double findAverageTimeOfSessionsByAssistantId(int assistantId);

	@Query(value = "SELECT stddev(timestampdiff(hour, s.start_timestamp, s.end_timestamp)) FROM tutorial_session s left join tutorial t on s.tutorial_id = t.id WHERE t.assistant_id = :assistantId", nativeQuery = true)
	Double findStdDevTimeOfSessionsByAssistantId(int assistantId);

	@Query(value = "SELECT max(timestampdiff(hour, s.start_timestamp, s.end_timestamp)) FROM tutorial_session s left join tutorial t on s.tutorial_id = t.id WHERE t.assistant_id = :assistantId", nativeQuery = true)
	Double findMaxTimeOfSessionsByAssistantId(int assistantId);

	@Query(value = "SELECT min(timestampdiff(hour, s.start_timestamp, s.end_timestamp)) FROM tutorial_session s left join tutorial t on s.tutorial_id = t.id WHERE t.assistant_id = :assistantId", nativeQuery = true)
	Double findMinTimeOfSessionsByAssistantId(int assistantId);

	@Query(
		value = "SELECT avg(ts.aggregated_duration) FROM (select sum(timestampdiff(hour, s.start_timestamp, s.end_timestamp)) as aggregated_duration from tutorial_session s left join tutorial t on s.tutorial_id = t.id where t.assistant_id = :assistantId group by t.id) as ts",
		nativeQuery = true)
	Double findAverageTimeOfTutorialsByAssistantId(int assistantId);

	@Query(
		value = "SELECT stddev(ts.aggregated_duration) FROM (select sum(timestampdiff(hour, s.start_timestamp, s.end_timestamp)) as aggregated_duration from tutorial_session s left join tutorial t on s.tutorial_id = t.id where t.assistant_id = :assistantId group by t.id) as ts",
		nativeQuery = true)
	Double findStdDevTimeOfTutorialsByAssistantId(int assistantId);

	@Query(
		value = "SELECT max(ts.aggregated_duration) FROM (select sum(timestampdiff(hour, s.start_timestamp, s.end_timestamp)) as aggregated_duration from tutorial_session s left join tutorial t on s.tutorial_id = t.id where t.assistant_id = :assistantId group by t.id) as ts",
		nativeQuery = true)
	Double findMaxTimeOfTutorialsByAssistantId(int assistantId);

	@Query(
		value = "SELECT min(ts.aggregated_duration) FROM (select sum(timestampdiff(hour, s.start_timestamp, s.end_timestamp)) as aggregated_duration from tutorial_session s left join tutorial t on s.tutorial_id = t.id where t.assistant_id = :assistantId group by t.id) as ts",
		nativeQuery = true)
	Double findMinTimeOfTutorialsByAssistantId(int assistantId);
}
