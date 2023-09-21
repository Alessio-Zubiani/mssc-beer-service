package guru.springframework.msscbeerservice.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import guru.springframework.msscbeerservice.web.model.BeerDto;

@WebMvcTest(value = BeerController.class)
class BeerControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Test
	void testGetBeerById() throws Exception {
		this.mockMvc.perform(get("/api/v1/beer/".concat(UUID.randomUUID().toString()))
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void testSaveBeer() throws Exception {
		
		BeerDto beerDto = BeerDto.builder().build();
		String beerDtoJson = this.objectMapper.writeValueAsString(beerDto);
		
		this.mockMvc.perform(post("/api/v1/beer/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(beerDtoJson))
				.andExpect(status().isCreated());
	}

	@Test
	void testUpdateBeerById() throws Exception {
		
		BeerDto beerDto = BeerDto.builder().build();
		String beerDtoJson = this.objectMapper.writeValueAsString(beerDto);
		
		this.mockMvc.perform(put("/api/v1/beer/".concat(UUID.randomUUID().toString()))
				.contentType(MediaType.APPLICATION_JSON)
				.content(beerDtoJson))
				.andExpect(status().isNoContent());
	}

}
