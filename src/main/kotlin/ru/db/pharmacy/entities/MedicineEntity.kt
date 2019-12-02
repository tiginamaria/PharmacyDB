package ru.db.pharmacy.entities

import java.io.Serializable
import javax.persistence.*

@Entity(name = "Medicine")
@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["medicine_description_id", "manufacturer", "form_id"])
    ]
)
data class MedicineEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "medicine_description_id")
    val medicineDescription: MedicineDescriptionEntity,

    @Column(name = "manufacturer", nullable = false)
    val manufacturer: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "form_id")
    val form: FormEntity,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "certificate_id")
    val certificate: CertificateEntity
): Serializable
