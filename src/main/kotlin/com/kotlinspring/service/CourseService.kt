package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository) {

    private val logger = KotlinLogging.logger {}

    fun addCourse(courseDTO: CourseDTO): CourseDTO {

        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category)
        }

        courseRepository.save(courseEntity)

        logger.info { "Saved Course is: $courseEntity" }

        return courseEntity.let {
            CourseDTO(it.id, it.name!!, it.category!!)
        }

    }

    fun retrieveAllCourse(): List<CourseDTO> {
        return courseRepository.findAll()
            .map {
                CourseDTO(it.id, it.name!!, it.category!!)
            }
    }

}
