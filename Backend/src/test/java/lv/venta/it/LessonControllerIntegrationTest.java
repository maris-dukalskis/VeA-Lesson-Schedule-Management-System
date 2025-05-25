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
import lv.venta.model.Course;
import lv.venta.model.Lecturer;
import lv.venta.model.Lesson;
import lv.venta.model.Role;
import lv.venta.model.Semester;
import lv.venta.model.SemesterStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LessonControllerIntegrationTest {

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
	void testLessonCRUD() throws Exception {
		int lessonId = -1;
		Course course = new Course("Test Course", "TC", "Test Description", 5);
		Classroom classroom = new Classroom("Test Building", 101, "Test Equipment", 30);
		Lecturer lecturer = new Lecturer("Test", "lecturer@test.com", Role.LECTURER, 20, "Test Notes",
				"Test Seniority");
		Semester semester = new Semester("Test Name", SemesterStatus.PLANNED);
		try {
			course = RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(course)).when().post("/course/insert").then().statusCode(200)
					.extract().as(Course.class);

			classroom = RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(classroom)).when().post("/classroom/insert").then()
					.statusCode(200).extract().as(Classroom.class);

			lecturer = RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(lecturer)).when().post("/lecturer/insert").then()
					.statusCode(200).extract().as(Lecturer.class);

			semester = RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(semester)).when().post("/semester/insert").then()
					.statusCode(200).extract().as(Semester.class);

			Lesson lesson = new Lesson(course, lecturer, classroom, semester, 1, true, "Test Online Info");

			Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken)
					.contentType(ContentType.JSON).body(objectMapper.writeValueAsString(lesson)).when()
					.post("/lesson/insert").then().statusCode(200).extract().response();

			lessonId = response.jsonPath().getInt("lessonId");

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/lesson/get/" + lessonId)
					.then().statusCode(200).body("lessonId", equalTo(lessonId)).body("lessonGroup", equalTo(1))
					.body("online", equalTo(true)).body("onlineInformation", equalTo("Test Online Info"));

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/lesson/all").then()
					.statusCode(200).body("size()", greaterThan(0));

			Lesson updatedLesson = new Lesson(course, lecturer, classroom, semester, 2, false, "Updated Test Info");

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(updatedLesson)).when().put("/lesson/update/" + lessonId)
					.then().statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/lesson/get/" + lessonId)
					.then().statusCode(200).body("lessonGroup", equalTo(2)).body("online", equalTo(false))
					.body("onlineInformation", equalTo("Updated Test Info"));

		} finally {
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/lesson/delete/" + lessonId).then().statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/course/delete/" + course.getCourseId()).then().statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/classroom/delete/" + classroom.getClassroomId()).then().statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/lecturer/delete/" + lecturer.getUserId()).then().statusCode(200);

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/semester/delete/" + semester.getSemesterId()).then().statusCode(200);
		}
	}
}
