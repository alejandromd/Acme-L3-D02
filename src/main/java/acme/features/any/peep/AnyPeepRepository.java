
package acme.features.any.peep;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Peep;
import acme.framework.components.accounts.UserAccount;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnyPeepRepository extends AbstractRepository {

	@Query("select p from Peep p")
	Collection<Peep> findAllPeeps();

	@Query("select p from Peep p where p.id = :id")
	Peep findPeepById(int id);

	@Query("select u from UserAccount u where u.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select sc.threshold from SpamConfig sc")
	Double findThreshold();

}
