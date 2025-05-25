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
import lv.venta.model.Semester;
import lv.venta.model.SemesterStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SemesterControllerIntegrationTest {

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
	void testSemesterCRUD() throws Exception {
		int semesterId = -1;

		try {
			Semester semester = new Semester("Test Semester", SemesterStatus.PLANNED);

			Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken)
					.contentType(ContentType.JSON).body(objectMapper.writeValueAsString(semester)).when()
					.post("/semester/insert").then().statusCode(200).extract().response();

			semesterId = response.jsonPath().getInt("semesterId");

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/semester/get/" + semesterId)
					.then().statusCode(200).body("semesterId", equalTo(semesterId))
					.body("name", equalTo("Test Semester")).body("semesterStatus", equalTo("PLANNED"));

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/semester/all").then()
					.statusCode(200).body("size()", greaterThan(0));

			Semester updated = new Semester("Updated Test Semester", SemesterStatus.ONGOING);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(updated)).when().put("/semester/update/" + semesterId).then()
					.statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/semester/get/" + semesterId)
					.then().statusCode(200).body("name", equalTo("Updated Test Semester"))
					.body("semesterStatus", equalTo("ONGOING"));

		} finally {
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/semester/delete/" + semesterId).then().statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/semester/get/" + semesterId)
					.then().statusCode(500);
		}
	}
}
