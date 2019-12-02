package ru.db.pharmacy.entities

import java.io.Serializable
import javax.persistence.*

@Entity(name = "MedicineDescription")
data class MedicineDescriptionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "trade_name", nullable = false, unique = true)
    val tradeName: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "medicine_international_id")
    val medicineInternationalDescription: MedicineInternationalDescriptionEntity
): Serializable