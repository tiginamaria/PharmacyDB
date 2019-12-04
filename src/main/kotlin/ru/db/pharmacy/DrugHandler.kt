package ru.db.pharmacy

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import ru.db.pharmacy.entities.MedicineInPharmaciesEntity

class DrugHandler {

    private val mapper = ObjectMapper()

    fun handle(medicineId: Long?): String? {
        //TODO: FIX ME(WRONG OUTPUT ORDER)
        val result = mapper.createArrayNode()

        val medicinesInfo = getMedicineInfo(medicineId)
        for (medicineInfo in medicinesInfo) {
            mapMedicineInfo(result, medicineInfo)
        }

        val summariesInfo = getMedicineSummaryInfo(medicineId)
        for (summaryInfo in summariesInfo) {
            mapSummaryMedicineInfo(result, summaryInfo)
        }

        try {
            return mapper.writeValueAsString(result)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            throw RuntimeException(e)
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


    private fun mapMedicineInfo(out: ArrayNode, medicine: MedicineInPharmaciesEntity) {
        val medicineNode = mapper.createObjectNode()
        medicineNode.put("medicine_name", medicine.id.medicine.medicineDescription.tradeName)
        medicineNode.put("pharmacy_id", medicine.id.pharmacy.id.toString())
        medicineNode.put("amount", medicine.amount.toString())
        medicineNode.put("price", medicine.price.toString())
        out.add(medicineNode)
    }


    private fun mapSummaryMedicineInfo(out: ArrayNode, summaryInfo: Any?) {
        val summary = summaryInfo as Array<*>
        val summaryNode = mapper.createObjectNode()
        summaryNode.put("medicine_id", summary[0].toString())
        summaryNode.put("sum_amount", summary[1].toString())
        summaryNode.put("avg_price", summary[2].toString())
        out.add(summaryNode)
    }
}
