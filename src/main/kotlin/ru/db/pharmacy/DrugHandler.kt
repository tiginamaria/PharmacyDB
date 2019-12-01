package ru.db.pharmacy

import ru.db.pharmacy.entities.FormEntity

class DrugHandler {
    fun handle(drugId: Long?): FormEntity? {
        var form: FormEntity? = null
        if (drugId != null) {
            Dao.run { em ->
                form = em.find(FormEntity::class.java, 1L)
            }
        }
        return form
    }
}
