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
import lv.venta.model.Course;
import lv.venta.model.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseControllerIntegrationTest {

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

		/*
		 * no need to save the user to db because AUTH does not check for that, it only
		 * checks if the JWT is generated with a specific secret key
		 */
		jwtToken = jwtConfig.generateToken(email, role.name(), name);
	}

	@Test
	public void testCRUD() throws Exception {
		Course course = new Course("Test Course", "TC", "Test Description", 5);

		int courseId = -1;
		try {
			// CREATE /course/insert
			Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken)
					.contentType(ContentType.JSON).body(objectMapper.writeValueAsString(course)).when()
					.post("/course/insert").then().statusCode(200).extract().response();

			courseId = response.jsonPath().getInt("courseId");

			// GET BY ID /course/get/{id}
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/course/get/" + courseId)
					.then().statusCode(200).body("courseId", equalTo(courseId)).body("name", equalTo("Test Course"));

			// GET LIST /course/all
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/course/all").then()
					.statusCode(200).body("size()", greaterThan(0));

			Course updatedCourse = new Course("Updated Course", "UC", "Updated Description", 7);

			// UPDATE /course/update/{id}
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(updatedCourse)).when().put("/course/update/" + courseId)
					.then().statusCode(200);

			// VERIFY UPDATE /course/get/{id}
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/course/get/" + courseId)
					.then().statusCode(200).body("name", equalTo("Updated Course")).body("creditPoints", equalTo(7))
					.body("description", equalTo("Updated Description")).body("shortName", equalTo("UC"));
		} finally {
			// DELETE /course/delete/{id}
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/course/delete/" + courseId).then().statusCode(200);

			// VERIFY DELETE /course/get/{id}
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/course/get/" + courseId)
					.then().statusCode(500);
		}
	}
}
