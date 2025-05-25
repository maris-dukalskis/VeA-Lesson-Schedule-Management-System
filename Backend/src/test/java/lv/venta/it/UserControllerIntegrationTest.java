package lv.venta.it;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lv.venta.config.JwtConfig;
import lv.venta.model.Role;
import lv.venta.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private JwtConfig jwtConfig;

	private String jwtToken;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setup() {
		RestAssured.port = port;

		String email = "admin@test.com";
		Role role = Role.ADMINISTRATOR;
		String name = "Admin Test";

		jwtToken = jwtConfig.generateToken(email, role.name(), name);
	}

	@Test
	void testUserCRUD() throws Exception {
		int userId = -1;

		User regularUser = new User("Test User Regular", "userregtest@test.com", Role.USER);

		try {
			Response userResponse = RestAssured.given().header("Authorization", "Bearer " + jwtToken)
					.contentType(ContentType.JSON).body(objectMapper.writeValueAsString(regularUser)).when()
					.post("/user/insert").then().statusCode(200).extract().response();

			userId = userResponse.jsonPath().getInt("userId");

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/user/get/" + userId).then()
					.statusCode(200).body("userId", equalTo(userId)).body("fullName", equalTo("Test User Regular"))
					.body("email", equalTo("userregtest@test.com")).body("role", equalTo("USER"));

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/user/all").then()
					.statusCode(200).body("size()", greaterThan(0));

			User updatedUser = new User("Updated User Test", "updateduser@test.com", Role.USER);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(updatedUser)).when().put("/user/update/" + userId).then()
					.statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/user/get/" + userId).then()
					.statusCode(200).body("fullName", equalTo("Updated User Test"))
					.body("email", equalTo("updateduser@test.com")).body("role", equalTo("USER"));

		} finally {
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().delete("/user/delete/" + userId)
					.then().statusCode(200);
		}
	}
}
