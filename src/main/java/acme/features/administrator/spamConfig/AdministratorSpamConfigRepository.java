
package acme.features.administrator.spamConfig;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.SpamConfig;
import acme.framework.components.accounts.Administrator;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorSpamConfigRepository extends AbstractRepository {

	@Query("select sc from SpamConfig sc")
	SpamConfig findOneSpamConfig();

	@Query("select sc from SpamConfig sc where id = :id")
	SpamConfig findOneSpamConfigById(int id);

	@Query("select a from Administrator a where a.userAccount.id = :id")
	Administrator findAdminById(int id);

}
