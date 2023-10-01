package guru.springframework.msscbeerservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.mappers.BeerMapper;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.exception.NotFoundException;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

	private final BeerRepository beerRepository;
	private final BeerInventoryService beerInventoryService;
	private final BeerMapper beerMapper;
	
	
	@Override
	public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {
		
		BeerPagedList beerPagedList;
		Page<Beer> beerPage;
		
		if(StringUtils.hasText(beerName) && beerStyle != null) {
			beerPage = this.beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
		} else if (StringUtils.hasText(beerName) && beerStyle == null) {
			beerPage = this.beerRepository.findAllByBeerName(beerName, pageRequest);
		} else if (!StringUtils.hasText(beerName) && beerStyle != null) {
			beerPage = this.beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
		} else {
			beerPage = this.beerRepository.findAll(pageRequest);
		}
		
		if(showInventoryOnHand) {
			List<BeerDto> dtos = new ArrayList<>();
			beerPage.getContent().forEach(b -> {
				BeerDto dto = this.beerMapper.beerToBeerDto(b);
				dto.setQuantityOnHand(this.beerInventoryService.getQuantityOnHand(b.getId()));
				
				dtos.add(dto);
			});
			
			beerPagedList = new BeerPagedList(dtos, 
					PageRequest
	                	.of(beerPage.getPageable().getPageNumber(),
	                        beerPage.getPageable().getPageSize()),
	            	beerPage.getTotalElements());
		} else {			
			beerPagedList = new BeerPagedList(beerPage.getContent()
					.stream()
					.map(this.beerMapper::beerToBeerDto)
					.collect(Collectors.toList()), 
					PageRequest
	                	.of(beerPage.getPageable().getPageNumber(),
	                        beerPage.getPageable().getPageSize()),
	            	beerPage.getTotalElements());
		}
		
		return beerPagedList;
	}
	
	@Override
	public BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand) {
		
		Optional<Beer> o = this.beerRepository.findById(beerId);
		if(o.isEmpty()) {
			throw new NotFoundException(new StringBuilder("No beer found with UUID: ").append(beerId).toString());
		}
		
		BeerDto dto;
		if(showInventoryOnHand) {
			dto = this.beerMapper.beerToBeerDto(o.get());
			dto.setQuantityOnHand(this.beerInventoryService.getQuantityOnHand(beerId));
		} else {
			dto = this.beerMapper.beerToBeerDto(o.get());
		}
		
		return dto;
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
