package ru.db.pharmasy

import spark.Spark.exception
import spark.Spark.get
import spark.Spark.port
import spark.Spark.staticFiles

object App {
    private val drugHandler = DrugHandler()
    private val updateRemainderHandler = UpdateRemainderHandler()
    private val saleHandler = SaleHandler()

    @JvmStatic
    fun main(args: Array<String>) {
        exception(Exception::class.java) { e, _, _ -> e.printStackTrace() }
        staticFiles.location("/public")
        port(8080)

        get("/drugs") { req, _ ->
            drugHandler.handle(req.queryParams("drug_id")?.toLong())
        }

        get("/update_remainder") { req, _ ->
            val drugId = req.queryParams("drug_id")
            val pharmacyId = req.queryParams("pharmacy_id")
            val remainder = req.queryParams("remainder")
            //TODO: check nullability
            updateRemainderHandler.handle(drugId.toLong(), pharmacyId.toLong(), remainder)
        }

        get("/sale") { req, _ ->
            val drugId: String = req.queryParams("drug_id")
            val discount: String = req.queryParams("discount")
            //TODO: check nullability
            saleHandler.handle(drugId.toLong(), discount.toDouble())
        }
    }
}
