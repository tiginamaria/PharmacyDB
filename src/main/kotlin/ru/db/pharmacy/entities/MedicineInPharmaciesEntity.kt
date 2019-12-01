package ru.db.pharmacy.entities

import org.hibernate.annotations.Check
import java.io.Serializable
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
    val pharmacy: PharmacyEntity,

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    val medicine: MedicineEntity
) : Serializable