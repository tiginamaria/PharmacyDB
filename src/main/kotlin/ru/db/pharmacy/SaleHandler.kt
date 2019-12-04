package ru.db.pharmacy

class SaleHandler {
    fun handle(medicineId: Long, discount: Double): Int {
        var result = 0
        Dao.run { em ->
            result = em.createQuery("""
                |UPDATE MedicineInPharmacies m 
                |SET m.price = m.price / 100 * :discount 
                |WHERE m.id.medicine.id = :medicineId
                |""".trimMargin())
                .setParameter("discount", discount)
                .setParameter("medicineId", medicineId)
                .executeUpdate()
        }
        return result
    }
}
