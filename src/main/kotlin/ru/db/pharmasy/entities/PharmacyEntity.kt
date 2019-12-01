package ru.db.pharmasy.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "Pharmacy")
data class PharmacyEntity(

    @Id
    val id: Long,

    @Column(name = "address", nullable = false)
    val address: String
)