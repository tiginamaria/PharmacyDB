package ru.db.pharmacy

import spark.Spark.*

object App {
    private val drugHandler = DrugHandler()
    private val updateRemainderHandler = UpdateRemainderHandler()
    private val saleHandler = SaleHandler()

    @JvmStatic
    fun main(args: Array<String>) {
        Dao.init()
        exception(Exception::class.java) { e, _, _ -> e.printStackTrace() }
        port(8080)

        get("/drugs") { req, _ ->
            drugHandler.handle(req.queryParams("drug_id")?.toLong())
        }

        get("/update_remainder") { req, _ ->
            val drugId = req.queryParams("drug_id")
            val pharmacyId = req.queryParams("pharmacy_id")
            val remainder = req.queryParams("remainder")
            //TODO: check nullability
            val result = updateRemainderHandler.handle(drugId.toLong(), pharmacyId.toLong(), remainder)
            when {
                result < 0 -> halt(400, "Bad request!")
                result > 0 -> "OK. $result updated."
                else -> "No lines updated."
            }

        }

        get("/sale") { req, _ ->
            val drugId: String = req.queryParams("drug_id")
            val discount: String = req.queryParams("discount")
            //TODO: check nullability
            saleHandler.handle(drugId.toLong(), discount.toDouble())
        }
    }
}
