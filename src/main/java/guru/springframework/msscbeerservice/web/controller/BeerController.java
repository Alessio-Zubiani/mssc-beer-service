package guru.springframework.msscbeerservice.web.controller;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/beer")
@RestController
@RequiredArgsConstructor
public class BeerController {
	
	private static final Integer DEFAULT_PAGE_NUMBER = 0;
	private static final Integer DEFAULT_PAGE_SIZE = 25;
	
	private final BeerService beerService;
	
	
	@GetMapping
	public ResponseEntity<BeerPagedList> getBeers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber, 
			@RequestParam(value = "pageSize", required = false) Integer pageSize, 
			@RequestParam(value = "beerName", required = false) String beerName, 
			@RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle, 
			@RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
		
		if(pageNumber == null || pageNumber < 0) {
			pageNumber = DEFAULT_PAGE_NUMBER;
		}
		
		if(pageSize == null || pageSize < 1) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		
		if(showInventoryOnHand == null) {
			showInventoryOnHand = false;
		}
		
		BeerPagedList beerList = this.beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);
		
		return new ResponseEntity<BeerPagedList>(beerList, HttpStatus.OK);
	}
	
	@GetMapping("/{beerId}")
	public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId, 
			@RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
		
		if(showInventoryOnHand == null) {
			showInventoryOnHand = false;
		}
		
		return new ResponseEntity<BeerDto>(this.beerService.getBeerById(beerId, showInventoryOnHand), HttpStatus.OK);
	}
	
	@GetMapping("/upc/{upc}")
	public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc) {
		
		return new ResponseEntity<BeerDto>(this.beerService.getBeerByUpc(upc), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Object> saveBeer(@Validated @RequestBody BeerDto beerDto) {
				
		return new ResponseEntity<Object>(this.beerService.saveBeer(beerDto), HttpStatus.CREATED);
	}
	
	@PutMapping("/{beerId}")
	public ResponseEntity<Object> updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) {
		
		return new ResponseEntity<Object>(this.beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT);
	}
	
}
