package airlanetickets.service.impl;

import airlanetickets.model.Agency;
import airlanetickets.model.exceptions.InvalidAgencyIdException;
import airlanetickets.repository.AgencyRepository;
import airlanetickets.service.AgencyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AgencyImpl implements AgencyService {

    private final AgencyRepository agencyRepository;

    public AgencyImpl(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }


    @Override
    public List<Agency> listAll() {
        return this.agencyRepository.findAll();
    }

    @Override
    public Page<Agency> findPaginated(int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        return this.agencyRepository.findAll(pageable);
    }

    @Override
    public Agency findById(Long id) {
        Agency agency = this.agencyRepository.findById(id)
                .orElseThrow(InvalidAgencyIdException::new);

        return agency;
    }

    @Override
    public Agency delete(Long id) {
        Agency agency = this.findById(id);

        this.agencyRepository.delete(agency);

        return agency;
    }

    @Override
    public Agency create(String nameOfAgency, String city, String country, int yearOfCreated) {
        Agency agency = new Agency(nameOfAgency,city,country,yearOfCreated);

        return this.agencyRepository.save(agency);
    }

    @Override
    public Agency update(Long id, String nameOfAgency, String city, String country, int yearOfCreated) {
        Agency agency = this.findById(id);

        agency.setNameOfAgency(nameOfAgency);
        agency.setCity(city);
        agency.setCountry(country);
        agency.setYearOfCreated(yearOfCreated);

        return this.agencyRepository.save(agency);
    }
}
