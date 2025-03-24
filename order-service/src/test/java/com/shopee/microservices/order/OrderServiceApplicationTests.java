package com.shopee.microservices.order;

import com.shopee.microservices.order.stubs.InventoryClientStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port=0)
class OrderServiceApplicationTests {

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
	void shouldPlaceOrder() {
		String requestBody= """
		{
		    "skuCode":"iphone_15",
		                "price":1000,
		                "quantity":100
		})
		""";
		InventoryClientStub.stubInventoryCall("iphone_15", 100);
		RestAssured.given().contentType("application/json").body(requestBody)
				.when().post("/api/order")
				.then().statusCode(201)
				.body("message", Matchers.equalTo("Order placed successfully"));
	}

}
