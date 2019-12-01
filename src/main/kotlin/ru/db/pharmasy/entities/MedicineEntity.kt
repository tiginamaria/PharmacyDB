package ru.db.pharmasy.entities

import javax.persistence.*

@Entity(name = "Medicine")
@Table(uniqueConstraints = [
    UniqueConstraint(columnNames = ["medicine_description_id", "manufacturer", "form_id"])
])
data class MedicineEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne
    @JoinColumn(name = "medicine_description_id")
    @Column(nullable = false)
    val medicineDescription: MedicineDescriptionEntity,

    @Column(name = "manufacturer", nullable = false)
    val manufacturer: String,

    @ManyToOne
    @JoinColumn(name = "form_id")
    @Column(nullable = false)
    val form: FormEntity,

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    @Column(nullable = false)
    val certificate: CertificateEntity
)
