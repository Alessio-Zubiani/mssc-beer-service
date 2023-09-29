package guru.springframework.msscbeerservice.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.mappers.BeerMapper;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.exception.NotFoundException;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper;
	
	
	@Override
	public BeerDto getBeerById(UUID beerId) {
		
		Optional<Beer> o = this.beerRepository.findById(beerId);
		if(o.isEmpty()) {
			throw new NotFoundException(new StringBuilder("No beer found with UUID: ").append(beerId).toString());
		}
		
		return this.beerMapper.beerToBeerDto(o.get());
	}

	@Override
	public BeerDto saveBeer(BeerDto beerDto) {
		
		return this.beerMapper.beerToBeerDto(this.beerRepository.save(this.beerMapper.beerDtoToBeer(beerDto)));
	}

	@Override
	public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
		
		Optional<Beer> o = this.beerRepository.findById(beerId);
		if(o.isEmpty()) {
			throw new NotFoundException(new StringBuilder("No beer found with UUID: ").append(beerId).toString());
		}
		
		Beer beer = o.get();
		beer.setBeerName(beerDto.getBeerName());
		beer.setBeerStyle(beerDto.getBeerStyle().name());
		beer.setPrice(beerDto.getPrice());
		beer.setUpc(beerDto.getUpc());
		
		return this.beerMapper.beerToBeerDto(this.beerRepository.save(beer));
	}

}
