package ru.db.pharmacy.entities

import java.io.Serializable
import javax.persistence.*

@Entity(name = "Form")
data class FormEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "name", nullable = false, unique = true)
    val name: String
): Serializable