package ru.db.pharmacy.entities

import java.time.LocalDate
import javax.persistence.*

@Entity(name = "Certificate")
data class CertificateEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "validity", nullable = false)
    val validity: LocalDate,

    @ManyToOne
    @JoinColumn(name = "laboratory_id")
    val laboratory: LaboratoryEntity
)