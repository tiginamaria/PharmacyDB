package ru.db.pharmasy.entities

import javax.persistence.*

@Entity(name = "Pharmacy")
data class PharmacyEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name="address", nullable = false)
    val address: String
)