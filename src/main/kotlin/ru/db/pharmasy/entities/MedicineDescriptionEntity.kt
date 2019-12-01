package ru.db.pharmasy.entities

import javax.persistence.*

data class MedicineDescriptionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "trade_name", nullable = false)
    val tradeName: String,

    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "medicine_international_id")
    @Column(nullable = false)
    val medicineInternationalDescription: MedicineInternationalDescriptionEntity
)