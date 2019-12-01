package ru.db.pharmasy.entities

import java.time.LocalDate
import javax.persistence.*

data class MedicineInternationalDescriptionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "international_name", nullable = false)
    val validity: LocalDate,

    @ManyToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "active_ingredient_id")
    @Column(nullable = false)
    val activeIngredient: IngredientEntity
)