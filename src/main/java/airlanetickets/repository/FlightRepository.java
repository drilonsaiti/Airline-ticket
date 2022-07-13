package airlanetickets.repository;

import airlanetickets.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight,Long> , PagingAndSortingRepository<Flight,Long> {

    List<Flight> findAllByFromLocationLikeAndToLocationLikeAndDeparatureTimeLike(String fromSearch, String toSearch, String deptSearch);

    List<Flight> findAllByFromLocationLikeAndToLocationLike(String fromSearch,String toSearch);

    List<Flight> findAllByFromLocationLikeAndDeparatureTimeLike(String fromSearch,String toSearch );

    List<Flight> findAllByToLocationLikeAndDeparatureTimeLike(String toSearch,String deptSearch);

    List<Flight> findAllByFromLocationLike(String fromSearch);

    List<Flight> findAllByToLocationLike(String deptSearch );

    List<Flight> findAllByDeparatureTimeLike(String deptSearch );

    Optional<Flight> findById(Long id);
}
