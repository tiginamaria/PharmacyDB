package ru.db.pharmacy

class DrugHandler {

    private val builder = StringBuilder()

    fun handle(medicineId: Long?): String? {

        val summariesInfo = getMedicineSummaryInfo(medicineId)
        builder.append("<table border=1>\n")
        builder.append("<tr>\n")
        builder.append("<th>Name</th>")
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

    private fun getMedicineInfo(medicineId: Long?): List<Any?> {
        var medicineEntities = emptyList<Any?>()
        Dao.run { em ->
            if (medicineId != null) {
                medicineEntities = em
                    .createQuery("""
                            |SELECT
                            |m.id.medicine.medicineDescription.tradeName, 
                            |m.id.pharmacy.id,
                            |m.amount,
                            |m.price
                            |FROM MedicineInPharmacies m 
                            |WHERE m.id.medicine.id=:medicineId 
                            |ORDER BY m.id.medicine.medicineDescription.tradeName ASC, 
                            |m.id.pharmacy.id ASC, 
                            |m.amount DESC
                            |""".trimMargin())
                    .setParameter("medicineId", medicineId)
                    .resultList
            } else {
                medicineEntities = em
                    .createQuery("""
                            |SELECT
                            |m.id.medicine.medicineDescription.tradeName, 
                            |m.id.pharmacy.id,
                            |m.amount,
                            |m.price
                            |FROM MedicineInPharmacies m 
                            |ORDER BY m.id.medicine.medicineDescription.tradeName ASC, 
                            |m.id.pharmacy.id ASC, 
                            |m.amount DESC
                            |""".trimMargin())
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
                            |m.id.medicine.medicineDescription.tradeName AS name, 
                            |SUM(m.amount) AS sum_amount, 
                            |AVG(m.price) AS avg_price 
                            |FROM MedicineInPharmacies m 
                            |WHERE m.id.medicine.id=:medicineId 
                            |GROUP BY m.id.medicine.medicineDescription.tradeName
                            |ORDER BY name
                            |""".trimMargin())
                    .setParameter("medicineId", medicineId)
                    .resultList
            } else {
                summaryMedicineEntities = em
                    .createQuery("""
                            |SELECT 
                            |m.id.medicine.medicineDescription.tradeName AS name, 
                            |SUM(m.amount) AS sum_amount, 
                            |AVG(m.price) AS avg_price 
                            |FROM MedicineInPharmacies m 
                            |GROUP BY m.id.medicine.medicineDescription.tradeName
                            |ORDER BY name
                            |""".trimMargin())
                    .resultList
            }
        }
        return summaryMedicineEntities
    }

    private fun mapInfo(info: Any?, count: Int) {
        val data = info as Array<*>
        for (i in 0 until count) {
            builder.append("<th>${data[i].toString()}</th>")
        }
    }

    private fun mapMedicineInfo(medicineInfo: Any?) = mapInfo(medicineInfo, 4)
    private fun mapSummaryMedicineInfo(summaryInfo: Any?) = mapInfo(summaryInfo, 3)
}
