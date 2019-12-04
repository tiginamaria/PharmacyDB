package ru.db.pharmacy

class UpdateRemainderHandler {
    fun handle(medicineId: Long, pharmacyId: Long, remainder: String): Int {
        var result = 0
        Dao.run { em ->
            when {
                remainder.matches(Regex("[1-9][0-9]*")) ->
                    result = em.createQuery("""
                        |UPDATE MedicineInPharmacies m 
                        |SET m.amount = :remainder 
                        |WHERE m.id.medicine.id = :medicineId AND m.id.pharmacy.id = :pharmacyId
                        |""".trimMargin())
                        .setParameter("remainder", remainder.toLong())
                        .setParameter("medicineId", medicineId)
                        .setParameter("pharmacyId", pharmacyId)
                        .executeUpdate()
                remainder.matches(Regex("[+|-][1-9][0-9]*")) ->
                    result = em.createQuery("""
                        |UPDATE MedicineInPharmacies m 
                        |SET m.amount = m.amount + :remainder 
                        |WHERE m.id.medicine.id = :medicineId AND m.id.pharmacy.id = :pharmacyId
                        |""".trimMargin())
                        .setParameter("remainder", remainder.toLong())
                        .setParameter("medicineId", medicineId)
                        .setParameter("pharmacyId", pharmacyId)
                        .executeUpdate()
                else -> {
                    println("Parameter remainder should match [+|-]?[1-9][0-9]*")
                    result = -1
                }
            }
        }
        return result
    }
}
