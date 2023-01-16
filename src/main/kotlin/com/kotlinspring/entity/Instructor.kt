package com.kotlinspring.entity

import jakarta.persistence.*

@Entity
@Table(name = "instructors")
data class Instructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int? = null,
    var name: String? = null,
    @OneToMany(
        mappedBy = "instructor", // one instructor can be mapped to multiple courses
        cascade = [CascadeType.ALL], // cascade changes to all to keep db clean. If an instructor is removed, all the courses associated with him would be removed
        orphanRemoval = true
    )
    var courses: List<Course> = mutableListOf() // initially when instructor is created, he might not have any course under him, so we are creating empty list
)