package com.kotlinspring.entity

import jakarta.persistence.*


@Entity
@Table(name = "courses")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int? = null,
    var name: String? = null,
    var category: String? = null,
    @ManyToOne(fetch = FetchType.LAZY) //one instructor mapped to multiple courses, fetch = FetchType.LAZY : usually fetch is eager, but in this case instructor data is invoked only if needed
    @JoinColumn(
        name = "INSTRUCTOR_ID",
        nullable = false
    ) //The join column connects courses entity to instructors entity, created on start of the application, nullable = false means it's mandatory field, should be present whenever course is created, if not it throws RUnTimeException
    val instructor: Instructor? = null
){
    override fun toString(): String {
        return "Course(id=$id, name='$name', category='$category', instructor=${instructor!!.id})"
    }
}
