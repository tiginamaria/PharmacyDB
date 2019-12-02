package ru.db.pharmacy.entities

import org.hibernate.annotations.Cascade
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
    val amount: Long
):Serializable

@Embeddable
data class MedicineInPharmaciesId(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "pharmacy_id")
    val pharmacy: PharmacyEntity,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "medicine_id")
    val medicine: MedicineEntity
) : Serializable