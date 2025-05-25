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
import lv.venta.model.Classroom;
import lv.venta.model.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClassroomControllerIntegrationTest {

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
	void testClassroomCRUD() throws Exception {
		Classroom classroom = new Classroom("Test Building", 101, "Test Equipment", 30);

		int classroomId = -1;
		try {
			Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken)
					.contentType(ContentType.JSON).body(objectMapper.writeValueAsString(classroom)).when()
					.post("/classroom/insert").then().statusCode(200).extract().response();

			classroomId = response.jsonPath().getInt("classroomId");

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.get("/classroom/get/" + classroomId).then().statusCode(200)
					.body("classroomId", equalTo(classroomId)).body("building", equalTo("Test Building"))
					.body("number", equalTo(101)).body("seats", equalTo(30))
					.body("equipmentDescription", equalTo("Test Equipment"));

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/classroom/all").then()
					.statusCode(200).body("size()", greaterThan(0));

			Classroom updatedClassroom = new Classroom("Updated Test Building", 202, "Updated Test Equipment", 25);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(updatedClassroom)).when()
					.put("/classroom/update/" + classroomId).then().statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.get("/classroom/get/" + classroomId).then().statusCode(200)
					.body("building", equalTo("Updated Test Building")).body("number", equalTo(202))
					.body("seats", equalTo(25)).body("equipmentDescription", equalTo("Updated Test Equipment"));
		} finally {
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/classroom/delete/" + classroomId).then().statusCode(200);
		}
	}
}
