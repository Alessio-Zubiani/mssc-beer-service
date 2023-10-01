package guru.springframework.msscbeerservice.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;

@Repository
public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID> {

	Page<Beer> findAllByBeerNameAndBeerStyle(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest);

	Page<Beer> findAllByBeerName(String beerName, PageRequest pageRequest);

	Page<Beer> findAllByBeerStyle(BeerStyleEnum beerStyle, PageRequest pageRequest);

}
