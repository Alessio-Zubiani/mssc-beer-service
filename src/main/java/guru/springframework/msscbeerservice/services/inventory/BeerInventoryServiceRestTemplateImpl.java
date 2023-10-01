package guru.springframework.msscbeerservice.services.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter

@Slf4j
@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Service
public class BeerInventoryServiceRestTemplateImpl implements BeerInventoryService {
	
	private final String INVENTORY_PATH = "/api/v1/inventories/beer/{beerId}/inventory";
	private final RestTemplate restTemplate;
	
	public BeerInventoryServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
	private String beerInventoryServiceHost;
	

	@Override
	public Integer getQuantityOnHand(UUID beerId) {
		
		log.debug("Calling InventoryService to get quantityOnHand value");
		
		Map<String, UUID> uriParam = new HashMap<>();
	    uriParam.put("beerId", beerId);
        
	    ParameterizedTypeReference<List<BeerInventoryDto>> responseType = new ParameterizedTypeReference<List<BeerInventoryDto>>() {};
	    
		ResponseEntity<List<BeerInventoryDto>> response = this.restTemplate
				.exchange(this.beerInventoryServiceHost.concat(INVENTORY_PATH), HttpMethod.GET, null, responseType, uriParam);
		
		log.debug("Response: [{}]", response.getBody());
		Integer quantityOnHand = Objects.requireNonNull(response.getBody())
				.stream()
				.mapToInt(BeerInventoryDto::getQuantityOnHand)
				.sum();
		
		return quantityOnHand;
	}

}
