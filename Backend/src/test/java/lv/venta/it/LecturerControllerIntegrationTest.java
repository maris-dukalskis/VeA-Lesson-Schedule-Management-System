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
import lv.venta.model.Lecturer;
import lv.venta.model.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LecturerControllerIntegrationTest {

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
	void testCRUD() throws Exception {
		Lecturer lecturer = new Lecturer("Test Lecturer", "lecturer.test@test.com", Role.LECTURER, 20, "Test Notes",
				"Test Seniority");

		int lecturerId = -1;
		try {
			Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken)
					.contentType(ContentType.JSON).body(objectMapper.writeValueAsString(lecturer)).when()
					.post("/lecturer/insert").then().statusCode(200).extract().response();

			lecturerId = response.jsonPath().getInt("userId");

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/lecturer/get/" + lecturerId)
					.then().statusCode(200).body("userId", equalTo(lecturerId))
					.body("fullName", equalTo("Test Lecturer"));

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/lecturer/all").then()
					.statusCode(200).body("size()", greaterThan(0));

			Lecturer updatedLecturer = new Lecturer("Updated Test Lecturer", "lecturer.test.updated@test.com",
					Role.LECTURER, 25, "Updated Test Notes", "Updated Test Seniority");

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(updatedLecturer)).when().put("/lecturer/update/" + lecturerId)
					.then().statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/lecturer/get/" + lecturerId)
					.then().statusCode(200).body("fullName", equalTo("Updated Test Lecturer"))
					.body("hours", equalTo(25)).body("notes", equalTo("Updated Test Notes"))
					.body("seniority", equalTo("Updated Test Seniority"));

		} finally {
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/lecturer/delete/" + lecturerId).then().statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/lecturer/get/" + lecturerId)
					.then().statusCode(500);
		}
	}
}
