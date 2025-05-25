package lv.venta.it;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.sql.Date;

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
import lv.venta.model.LessonDateTime;
import lv.venta.model.Role;
import lv.venta.model.Semester;
import lv.venta.model.SemesterStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LessonDateTimeControllerIntegrationTest {

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
	public void testLessonDateTimeCRUD() throws Exception {
		int lessonDateTimeId = -1;
		int lessonId = -1;

		Course course = new Course("Test Course", "TC", "Test Desc", 4);
		Classroom classroom = new Classroom("Test Building", 102, "Test Equipment", 20);
		Lecturer lecturer = new Lecturer("Lecturer Test", "lecturer2@test.com", Role.LECTURER, 15, "Test Notes",
				"Test Seniority");
		Semester semester = new Semester("Test Semester", SemesterStatus.PLANNED);

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
			lesson = RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(lesson)).when().post("/lesson/insert").then().statusCode(200)
					.extract().as(Lesson.class);
			lessonId = lesson.getLessonId();

			// CREATE /lessondatetime/insert
			LessonDateTime lessonDateTime = new LessonDateTime(lesson, true, Date.valueOf("2025-05-20"), "10:00",
					"11:30");

			Response response = RestAssured.given().header("Authorization", "Bearer " + jwtToken)
					.contentType(ContentType.JSON).body(objectMapper.writeValueAsString(lessonDateTime)).when()
					.post("/lessondatetime/insert").then().statusCode(200).extract().response();

			lessonDateTimeId = response.jsonPath().getInt("lessonDateTimeId");

			// GET BY ID /lessondatetime/get/{id}
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.get("/lessondatetime/get/" + lessonDateTimeId).then().statusCode(200)
					.body("lessonDateTimeId", equalTo(lessonDateTimeId)).body("custom", equalTo(true))
					.body("date", equalTo("2025-05-20")).body("timeFrom", equalTo("10:00"))
					.body("timeTo", equalTo("11:30"));

			// GET ALL /lessondatetime/all
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when().get("/lessondatetime/all").then()
					.statusCode(200).body("size()", greaterThan(0));

			// UPDATE /lessondatetime/update/{id}
			LessonDateTime updated = new LessonDateTime(lesson, false, Date.valueOf("2025-05-26"), "12:00", "13:30");

			RestAssured.given().header("Authorization", "Bearer " + jwtToken).contentType(ContentType.JSON)
					.body(objectMapper.writeValueAsString(updated)).when()
					.put("/lessondatetime/update/" + lessonDateTimeId).then().statusCode(200);

			// VERIFY UPDATE /lessondatetime/get/{id}
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.get("/lessondatetime/get/" + lessonDateTimeId).then().statusCode(200)
					.body("custom", equalTo(false)).body("date", equalTo("2025-05-26"))
					.body("timeFrom", equalTo("12:00")).body("timeTo", equalTo("13:30"));

		} finally {
			// DELETE /lessondatetime/delete/{id}
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.delete("/lessondatetime/delete/" + lessonDateTimeId).then().statusCode(200);

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

			// VERIFY DELETE /lessondatetime/get/{id}
			RestAssured.given().header("Authorization", "Bearer " + jwtToken).when()
					.get("/lessondatetime/get/" + lessonDateTimeId).then().statusCode(500);
		}
	}
}
