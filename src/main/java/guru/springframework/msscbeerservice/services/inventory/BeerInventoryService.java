package guru.springframework.msscbeerservice.services.inventory;

import java.util.UUID;

public interface BeerInventoryService {
	
	Integer getQuantityOnHand(UUID beerId);

}
