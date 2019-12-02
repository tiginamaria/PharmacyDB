package ru.db.pharmacy.entities

import java.io.Serializable
import javax.persistence.*
import java.sql.Date

@Entity(name = "Certificate")
data class CertificateEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "validity", nullable = false)
    val validity: Date,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "laboratory_id")
    val laboratory: LaboratoryEntity
):Serializable