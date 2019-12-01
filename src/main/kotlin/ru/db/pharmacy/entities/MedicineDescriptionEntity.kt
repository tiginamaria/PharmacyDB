package ru.db.pharmacy.entities

import javax.persistence.*

@Entity(name = "MedicineDescription")
data class MedicineDescriptionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "trade_name", nullable = false, unique = true)
    val tradeName: String,

    @ManyToOne
    @JoinColumn(name = "medicine_international_id")
    val medicineInternationalDescription: MedicineInternationalDescriptionEntity
)