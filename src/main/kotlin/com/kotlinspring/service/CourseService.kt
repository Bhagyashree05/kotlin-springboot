package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository,
    val instructorService: InstructorService
) {

    private val logger = KotlinLogging.logger {}

    fun addCourse(courseDTO: CourseDTO): CourseDTO {

        val instructor = instructorService.findByInstructorId(courseDTO.instructorId!!)

        if(!instructor.isPresent){
            throw InstructorNotValidException("Instructor not valid for the id: ${courseDTO.instructorId}")
        }

        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, instructor.get())
        }

        courseRepository.save(courseEntity)

        logger.info { "Saved Course is: $courseEntity" }

        return courseEntity.let {
            CourseDTO(it.id, it.name!!, it.category!!, it.instructor!!.id)
        }

    }

    fun retrieveAllCourse(courseName: String?): List<CourseDTO> {

        // if courseName is not null findCoursesByName() is executed
        // if courseName is null findAll() is executed
        val courses = courseName?.let {
            courseRepository.findCoursesByName(courseName)
        } ?: courseRepository.findAll()

        return courses
            .map {
                CourseDTO(it.id, it.name!!, it.category!!)
            }

    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    courseRepository.save(it)
                    CourseDTO(it.id, it.name!!, it.category!!)
                }
        } else {
            throw CourseNotFoundException("No course found for the passed in Id: $courseId")
        }

    }

    fun deleteCourse(courseId: Int) {
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    courseRepository.deleteById(courseId)
                }
        } else {
            throw CourseNotFoundException("No course found for the passed in Id: $courseId")
        }

    }

}
