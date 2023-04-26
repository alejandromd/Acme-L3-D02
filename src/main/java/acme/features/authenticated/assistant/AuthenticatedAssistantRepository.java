
package acme.features.authenticated.assistant;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.components.accounts.UserAccount;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AuthenticatedAssistantRepository extends AbstractRepository {

	@Query("SELECT u FROM UserAccount u WHERE u.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("SELECT a FROM Assistant a WHERE a.userAccount.id = :id")
	Assistant findOneAssistantByUserAccountId(int id);

}
