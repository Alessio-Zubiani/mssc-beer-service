package guru.springframework.msscbeerservice.bootstrap;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;

//@Component
@RequiredArgsConstructor
public class BeerLoader implements CommandLineRunner {

	private final BeerRepository beerRepository;
	
	
	@Override
	public void run(String... args) throws Exception {
		this.loadBeerObjects();
	}
	
	private void loadBeerObjects() {
		if(this.beerRepository.count() == 0) {
			this.beerRepository.save(Beer.builder()
					.beerName("Mango Bobs")
					.beerStyle("IPA")
					.quantityToBrew(200)
					.minOnHand(12)
					.upc("337010000001")
					.price(new BigDecimal(12.95))
					.build());
			
			this.beerRepository.save(Beer.builder()
					.beerName("Galaxy Cat")
					.beerStyle("PALE_ALE")
					.quantityToBrew(200)
					.minOnHand(12)
					.upc("337010000002")
					.price(new BigDecimal(11.95))
					.build());
		}
	}

}
