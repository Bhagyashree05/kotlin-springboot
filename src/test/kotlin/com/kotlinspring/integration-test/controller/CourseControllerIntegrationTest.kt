package com.kotlinspring.`integration-test`.controller

import com.kotlinspring.`integration-test`.util.courseEntityList
import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup() {

        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)


    }

    @Test
    fun addCourse() {

        val courseDTO = CourseDTO(null, "Build Restful APIs using Kotlin and SpringBoot", "DEVELOPMENT")

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }

    }

    @Test
    fun retrieveAllCourses() {
        val courseDTO = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(3, courseDTO.size)
    }

    @Test
    fun updateCourse() {

        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development"
        )
        courseRepository.save(course)

        val updateCourseDTO = CourseDTO(
            null,
            "Build RestFul APis using SpringBoot and Kotlin1", "Development"
        )

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
            .bodyValue(updateCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(updateCourseDTO.name, updatedCourse.name)

    }

    @Test
    fun deleteCourse() {

        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development"
        )
        courseRepository.save(course)

        val updatedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent

    }
}