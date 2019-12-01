package ru.db.pharmasy.entities

import org.hibernate.annotations.Check
import javax.persistence.*
import java.io.Serializable
import javax.persistence.Embeddable

@Entity(name = "MedicineInPharmacies")
data class MedicineInPharmaciesEntity(

    @EmbeddedId
    val id: MedicineInPharmaciesId,

    @Column(name = "price", nullable = false)
    val price: Double,

    @Column(name = "amount", nullable = false)
    @Check(constraints = "amount >= 0")
    val amount: Int
)

@Embeddable
class MedicineInPharmaciesId(
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "pharmacy_id")
    @Column(nullable = false)
    val pharmacy: PharmacyEntity,

    @OneToMany(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "medicine_id")
    @Column(nullable = false)
    val medicine: MedicineEntity
) : Serializable