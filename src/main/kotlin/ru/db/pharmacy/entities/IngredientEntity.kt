package ru.db.pharmacy.entities

import javax.persistence.*

@Entity(name = "Ingredient")
data class IngredientEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @Column(name = "formula", nullable = false)
    val formula: String
)