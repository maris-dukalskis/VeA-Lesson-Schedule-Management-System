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
import lv.venta.model.StudyProgramme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudyProgrammeControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private JwtConfig jwtConfig;

	private String jwtToken;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setup() {
		RestAssured.port = port;

		String email = "admin@test.com";
		Role role = Role.ADMINISTRATOR;
		String name = "Admin Test";

		jwtToken = jwtConfig.generateToken(email, role.name(), name);
	}

	@Test
	public void testStudyProgrammeCRUD() throws Exception {
		int studyProgrammeId = -1;

		try {
			// CREATE
			StudyProgramme studyProgramme = new StudyProgramme("TestShort", "Test Name", 1);

			Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken)
					.contentType(ContentType.JSON).body(objectMapper.writeValueAsString(studyProgramme)).when()
					.post("/studyprogramme/insert").then().statusCode(200).extract().response();

			studyProgrammeId = response.jsonPath().getInt("studyProgrammeId");

			// GET BY ID
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.get("/studyprogramme/get/" + studyProgrammeId).then().statusCode(200)
					.body("studyProgrammeId", equalTo(studyProgrammeId)).body("shortName", equalTo("TestShort"))
					.body("name", equalTo("Test Name")).body("year", equalTo(1));

			// GET ALL
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/studyprogramme/all").then()
					.statusCode(200).body("size()", greaterThan(0));

			// UPDATE
			StudyProgramme updated = new StudyProgramme("TestShortUpdated", "Test Name Updated", 2);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(updated)).when()
					.put("/studyprogramme/update/" + studyProgrammeId).then().statusCode(200);

			// VERIFY UPDATE
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.get("/studyprogramme/get/" + studyProgrammeId).then().statusCode(200)
					.body("shortName", equalTo("TestShortUpdated")).body("name", equalTo("Test Name Updated"))
					.body("year", equalTo(2));
		} finally {
			// DELETE
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/studyprogramme/delete/" + studyProgrammeId).then().statusCode(200);

			// VERIFY DELETE
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.get("/studyprogramme/get/" + studyProgrammeId).then().statusCode(500);
		}
	}
}
