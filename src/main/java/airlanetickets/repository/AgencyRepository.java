package airlanetickets.repository;

import airlanetickets.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AgencyRepository extends JpaRepository<Agency,Long>, PagingAndSortingRepository<Agency,Long> {

}
