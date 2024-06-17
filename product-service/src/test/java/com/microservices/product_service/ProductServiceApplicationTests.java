package com.microservices.product_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.product_service.dto.ProductRequest;
import com.microservices.product_service.dto.ProductResponse;
import com.microservices.product_service.model.Product;
import com.microservices.product_service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;



	@Container
	static MySQLContainer mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34"))
			.withDatabaseName("db_product")
			.withUsername("root")
			.withPassword("");


	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.datasource.url",mysqlContainer::getJdbcUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
        ProductRequest product = getProductRequest();
		String productString = objectMapper.writeValueAsString(product);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productString))
				.andExpect(status().isCreated());
	}


	@Test
	void shouldGetAllProducts() throws Exception {
		// Create a product first
		ProductRequest product = getProductRequest();

		//Insert it in the database
		shouldCreateProduct();

		// Perform GET request to fetch all products
		String responseString = mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		// Deserialize the response to a list of ProductResponse
		List<ProductResponse> products = objectMapper.readValue(responseString,
				objectMapper.getTypeFactory().constructCollectionType(List.class, ProductResponse.class));

		// Verify the product is in the response
		assertThat(products).isNotEmpty();
		assertThat(products.get(0).getName()).isEqualTo(product.getName());
		assertThat(products.get(0).getDescription()).isEqualTo(product.getDescription());

		// We use compareTo for asserting equality of BigDecimal Values cuz asserThat gives 900 != 900.00
		assertThat(products.get(0).getPrice().compareTo(product.getPrice())).isEqualTo(0);
	}


	@Test
	void contextLoads() {
	}





	private ProductRequest getProductRequest(){

		ProductRequest productRequest = ProductRequest.builder()
				.name("iohone 13")
				.description("new release")
				.price(BigDecimal.valueOf(900))
				.build();

		return productRequest;

	}


}
