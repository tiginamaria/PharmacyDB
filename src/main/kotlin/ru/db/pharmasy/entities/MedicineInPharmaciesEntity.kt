package ru.db.pharmasy.entities

import org.hibernate.annotations.Check
import javax.persistence.*

@Entity(name = "MedicineInPharmacies")
data class MedicineInPharmaciesEntity(

    @EmbeddedId
    val id: MedicineInPharmaciesId,

    @Column(name = "price", nullable = false)
    @Check(constraints = "price >= 0")
    val price: Double,

    @Column(name = "amount", nullable = false)
    @Check(constraints = "amount >= 0")
    val amount: Int
)

@Embeddable
data class MedicineInPharmaciesId(
    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    @Column(nullable = false)
    val pharmacy: PharmacyEntity,

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    @Column(nullable = false)
    val medicine: MedicineEntity
)