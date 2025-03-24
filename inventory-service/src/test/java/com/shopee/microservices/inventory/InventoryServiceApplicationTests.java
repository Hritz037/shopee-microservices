package com.shopee.microservices.inventory;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.hamcrest.Matchers;
import org.testcontainers.utility.DockerImageName;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"));

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port=port;
	}

	static{
		mysqlContainer.start();
	}
	@Test
	void contextLoads() {
		String skuCode = "iphone_15";
		String quantity="101";
		RestAssured.given().contentType("application/json").body("")
				.when().get("/api/inventory?skuCode="+skuCode+"&quantity="+quantity)
				.then().statusCode(200).body("",Matchers.is(false));


	}

}
