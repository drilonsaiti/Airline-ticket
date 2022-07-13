package airlanetickets.repository;

import airlanetickets.model.Flight;
import airlanetickets.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAll();

    Order findByFlightId(Long id);

    List<Order> findAllByFlightId(Long id);

    Order findByFlight(Flight flight);

    void deleteByFlightId(Long id);

}
