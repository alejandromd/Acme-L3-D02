
package acme.features.administrator.banner;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Banner;
import acme.framework.components.accounts.Administrator;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorBannerRepository extends AbstractRepository {

	@Query("select b from Banner b where b.id = :id")
	Banner findBannerById(int id);

	@Query("select b from Banner b")
	Collection<Banner> findAllBanners();

	@Query("select sc.threshold from SpamConfig sc")
	Double findThreshold();

	@Query("select a from Administrator a where a.userAccount.id = :id")
	Administrator findAdminById(int id);

}
