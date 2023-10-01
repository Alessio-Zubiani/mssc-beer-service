package guru.springframework.msscbeerservice.services;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;

public interface BeerService {
	
	BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);
	
	BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand);
	
	BeerDto saveBeer(BeerDto beerDto);
	
	BeerDto updateBeer(UUID beerId, BeerDto beerDto);

}
