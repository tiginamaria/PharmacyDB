package ru.db.pharmacy.entities

import java.io.Serializable
import javax.persistence.*

@Entity(name = "Laboratory")
data class LaboratoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "leader_name", nullable = false)
    val leaderName: String
):Serializable