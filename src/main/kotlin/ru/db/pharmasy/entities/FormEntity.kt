package ru.db.pharmasy.entities

import javax.persistence.*

data class FormEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "name", nullable = false, unique = true)
    val name: String
)