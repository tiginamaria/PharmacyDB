package ru.db.pharmacy

import ru.db.pharmacy.entities.MedicineInPharmaciesEntity

class DrugHandler {

    private val builder = StringBuilder()

    fun handle(medicineId: Long?): String? {

        val summariesInfo = getMedicineSummaryInfo(medicineId)
        builder.append("<table border=1>\n")
        builder.append("<tr>\n")
        builder.append("<th>Medicine id</th>")
        builder.append("<th>Total amount</th>")
        builder.append("<th>Average price</th>")
        builder.append("</tr>\n")
        for (summaryInfo in summariesInfo) {
            builder.append("<tr>\n")
            mapSummaryMedicineInfo(summaryInfo)
            builder.append("</tr>\n")
        }
        builder.append("</table>\n")
        builder.append("<br>\n")

        val medicinesInfo = getMedicineInfo(medicineId)
        builder.append("<table border=1>\n")
        builder.append("<tr>\n")
        builder.append("<th>Name</th>")
        builder.append("<th>Pharmacy id</th>")
        builder.append("<th>Amount</th>")
        builder.append("<th>Price</th>")
        builder.append("</tr>\n")
        for (medicineInfo in medicinesInfo) {
            builder.append("<tr>\n")
            mapMedicineInfo(medicineInfo)
            builder.append("</tr>\n")
        }
        builder.append("</table>\n")

        return builder.toString().also {
            builder.clear()
        }

    }

    private fun getMedicineInfo(medicineId: Long?): List<MedicineInPharmaciesEntity> {
        var medicineEntities: List<MedicineInPharmaciesEntity> = emptyList()
        Dao.run { em ->
            if (medicineId != null) {
                medicineEntities = em
                    .createQuery("""
                            |SELECT m FROM MedicineInPharmacies m 
                            |WHERE m.id.medicine.id=:medicineId 
                            |ORDER BY m.id.medicine.medicineDescription.tradeName ASC, 
                            |m.id.pharmacy.id ASC, 
                            |m.amount DESC
                            |""".trimMargin(),
                        MedicineInPharmaciesEntity::class.java)
                    .setParameter("medicineId", medicineId)
                    .resultList
            } else {
                medicineEntities = em
                    .createQuery("""
                            |FROM MedicineInPharmacies m 
                            |ORDER BY m.id.medicine.medicineDescription.tradeName ASC, 
                            |m.id.pharmacy.id ASC, 
                            |m.amount DESC
                            |""".trimMargin(),
                        MedicineInPharmaciesEntity::class.java)
                    .resultList
            }
        }
        return medicineEntities
    }

    private fun getMedicineSummaryInfo(medicineId: Long?): List<Any?> {
        var summaryMedicineEntities: List<Any?> = emptyList()
        Dao.run { em ->
            if (medicineId != null) {
                summaryMedicineEntities = em
                    .createQuery("""
                            |SELECT 
                            |m.id.medicine.id AS id, 
                            |SUM(m.amount) AS sum_amount, 
                            |AVG(m.price) AS avg_price 
                            |FROM MedicineInPharmacies m 
                            |WHERE m.id.medicine.id=:medicineId 
                            |GROUP BY medicine_id
                            |""".trimMargin())
                    .setParameter("medicineId", medicineId)
                    .resultList
            } else {
                summaryMedicineEntities = em
                    .createQuery("""
                            |SELECT 
                            |m.id.medicine.id AS id, 
                            |SUM(m.amount) AS sum_amount, 
                            |AVG(m.price) AS avg_price 
                            |FROM MedicineInPharmacies m 
                            |GROUP BY medicine_id
                            |""".trimMargin())
                    .resultList
            }
        }
        return summaryMedicineEntities
    }


    private fun mapMedicineInfo(medicine: MedicineInPharmaciesEntity) {
        builder.append("<th>${medicine.id.medicine.medicineDescription.tradeName}</th>")
        builder.append("<th>${medicine.id.pharmacy.id}</th>")
        builder.append("<th>${medicine.amount}</th>")
        builder.append("<th>${medicine.price}</th>")
    }


    private fun mapSummaryMedicineInfo(summaryInfo: Any?) {
        val summary = summaryInfo as Array<*>
        builder.append("<th>${summary[0].toString()}</th>")
        builder.append("<th>${summary[1].toString()}</th>")
        builder.append("<th>${summary[2].toString()}</th>")
    }
}
