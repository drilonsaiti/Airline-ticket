package airlanetickets.service;

import airlanetickets.model.Agency;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AgencyService {


    List<Agency> listAll();

    Agency findById(Long id);

    Agency delete(Long id);

    Agency create(String nameOfAgency,String city,String country,int yearOfCreated);

    Agency update(Long id,String nameOfAgency,String city,String country,int yearOfCreated);

    Page<Agency> findPaginated(int pageNo, int pageSize, String sortField, String sortDir);

}
