package ru.db.pharmacy.entities

import javax.persistence.*

@Entity(name = "MedicineInternationalDescription")
data class MedicineInternationalDescriptionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "international_name", nullable = false, unique = true)
    val internationalName: String,

    @ManyToOne
    @JoinColumn(name = "active_ingredient_id")
    val activeIngredient: IngredientEntity
)