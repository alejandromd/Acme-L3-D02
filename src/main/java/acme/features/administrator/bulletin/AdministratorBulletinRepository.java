
package acme.features.administrator.bulletin;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorBulletinRepository extends AbstractRepository {

	@Query("select b from Bulletin b where b.id = :id")
	Offer findBulletinById(int id);

	@Query("select b from Bulletin b")
	Collection<Offer> findAllBulletins();

	@Query("select a from Administrator a where a.userAccount.id = :id")
	Administrator findAdminById(int id);

	@Query("select sc.threshold from SpamConfig sc")
	Double findThreshold();
}
