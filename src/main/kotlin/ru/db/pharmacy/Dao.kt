package ru.db.pharmacy

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

object Dao {
    private lateinit var sessionFactory: EntityManagerFactory

    fun init() {
        sessionFactory = Persistence.createEntityManagerFactory("Postgres")
    }

    fun stop() = sessionFactory.close()

    fun run(code: (EntityManager) -> Unit) {
        val entityManager = sessionFactory.createEntityManager()
        val tx = entityManager.transaction
        tx.begin()
        try {
            code(entityManager)
            tx.commit()
        } catch (e: Exception) {
            tx.rollback()
            throw RuntimeException(e)
        } finally {
            entityManager.close()
        }
    }

}