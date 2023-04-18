
package acme.components;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Banner;
import acme.framework.helpers.MomentHelper;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface BannerRepository extends AbstractRepository {

	@Query("select count(b) from Banner b where b.displayPeriodBegin <= ?1 and b.displayPeriodFinish > ?1")
	int countBanner(Date date);

	@Query("select b from Banner b")
	List<Banner> findManyBanners(PageRequest pageRequest);

	default Banner findRandomBanner() {
		Banner result;
		int count, index;
		ThreadLocalRandom random;
		PageRequest page;
		List<Banner> list;

		count = this.countBanner(MomentHelper.getCurrentMoment());
		if (count == 0)
			result = null;
		else {
			random = ThreadLocalRandom.current();
			index = random.nextInt(0, count);

			page = PageRequest.of(index, 1);
			list = this.findManyBanners(page);
			result = list.isEmpty() ? null : list.get(0);
		}

		return result;
	}

}
