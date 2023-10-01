package guru.springframework.msscbeerservice.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;



@WebMvcTest(value = BeerController.class)
@ComponentScan(basePackages = "guru.springframework.msscbeerservice.mappers")
class BeerControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private BeerService beerService;
	
	
	@Test
	void testGetBeerById() throws Exception {
		
		Mockito.when(this.beerService.getBeerById(Mockito.any(UUID.class), Mockito.anyBoolean()))
			.thenReturn(this.getValidBeerDto());
		
		this.mockMvc.perform(get ("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void testSaveBeer() throws Exception {
		
		Mockito.when(this.beerService.saveBeer(Mockito.any(BeerDto.class)))
			.thenReturn(this.getValidBeerDto());
		
		BeerDto beerDto = this.getValidBeerDto();
		String beerDtoJson = this.objectMapper.writeValueAsString(beerDto);
		
		this.mockMvc.perform(post("/api/v1/beer/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(beerDtoJson))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	void testUpdateBeerById() throws Exception {
		
		Mockito.when(this.beerService.updateBeer(Mockito.any(UUID.class), Mockito.any(BeerDto.class)))
			.thenReturn(this.getValidBeerDto());
		
		BeerDto beerDto = this.getValidBeerDto();
		String beerDtoJson = this.objectMapper.writeValueAsString(beerDto);
		
		this.mockMvc.perform(put("/api/v1/beer/".concat(UUID.randomUUID().toString()))
				.contentType(MediaType.APPLICATION_JSON)
				.content(beerDtoJson))
				.andExpect(status().isNoContent())
				.andDo(print());
	}
	
	private BeerDto getValidBeerDto() {
		
		return BeerDto.builder()
				.beerName("My beer")
				.beerStyle(BeerStyleEnum.ALE)
				.price(new BigDecimal(19.96))
				.upc(4L)
				.build();
	}

}
