package ru.db.pharmacy

class UpdateRemainderHandler {
    fun handle(medicineId: Long, pharmacyId: Long, remainder: String): String {
        //TODO: FIX ME(DO NOT UPDATE)
        var queryStatus = "Ok"
        Dao.run { em ->
            when {
                remainder.matches(Regex("[1-9][0-9]+")) ->
                    em.createQuery("UPDATE MedicineInPharmacies m " +
                            "SET m.amount=:remainder " +
                            "WHERE m.id.medicine.id=:medicineId AND m.id.pharmacy.id=:pharmacyId")
                        .setParameter("remainder", remainder.toLong())
                        .setParameter("medicineId", medicineId)
                        .setParameter("pharmacyId", pharmacyId)
                remainder.matches(Regex("[+|-][1-9][0-9]+")) ->
                    em.createQuery("UPDATE MedicineInPharmacies m " +
                            "SET m.amount=m.amount+:remainder " +
                            "WHERE m.id.medicine.id=:medicineId AND m.id.pharmacy.id=:pharmacyId")
                        .setParameter("remainder", remainder.toLong())
                        .setParameter("medicineId", medicineId)
                        .setParameter("pharmacyId", pharmacyId)
                else -> {
                    print("Parameter remeinder should match [+|-]?[1-9][0-9]+")
                    queryStatus = "ERROR 400"
                }
            }
        }
        return queryStatus
    }
}
