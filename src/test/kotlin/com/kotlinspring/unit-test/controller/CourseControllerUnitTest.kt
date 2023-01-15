package com.kotlinspring.`unit-test`.controller

import com.kotlinspring.`integration-test`.util.courseDTO
import com.kotlinspring.controller.CourseController
import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @MockkBean
    lateinit var courseService: CourseService

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun addCourse() {

        val courseDTO = CourseDTO(null, "Build Restful APIs using Kotlin and SpringBoot", "DEVELOPMENT")

        every { courseService.addCourse(any()) } returns courseDTO(id = 1)

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
    fun addCourse_validation() {

        val courseDTO = CourseDTO(null, "", "")

        every { courseService.addCourse(any()) } returns courseDTO(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("courseDTO.category must not be blank, courseDTO.name must not be blank", response)

    }

    @Test
    fun addCourse_test_runtimeException() {

        val courseDTO = CourseDTO(null, "Build Restful APIs using Kotlin and SpringBoot", "DEVELOPMENT")

        val errorMessage = "Unexpected Error occurred"
        every { courseService.addCourse(any()) } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage, response)

    }

    @Test
    fun retrieveAllCourses() {
        every { courseService.retrieveAllCourse(any()) }.returnsMany(
            listOf(
                courseDTO(id = 1),
                courseDTO(id = 2, name = "Wiremock for Java Developers")
            )
        )

        val courseDTO = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, courseDTO.size)
    }

    @Test
    fun updateCourse() {

        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development"
        )

        every { courseService.updateCourse(any(), any()) } returns courseDTO(
            id = 100,
            name = "Build RestFul APis using SpringBoot and Kotlin1"
        )

        val updateCourseDTO = CourseDTO(
            null,
            "Build RestFul APis using SpringBoot and Kotlin1", "Development"
        )

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updateCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build RestFul APis using SpringBoot and Kotlin1", updatedCourse.name)

    }

    @Test
    fun deleteCourse() {

        every { courseService.deleteCourse(any()) } just runs

        val updatedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 2)
            .exchange()
            .expectStatus().isNoContent

    }


}