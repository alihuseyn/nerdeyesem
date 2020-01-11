package me.alihuseyn.nerdeyesem.zomato.model

import org.json.JSONObject
import java.io.Serializable

/**
 * <h1>Abstract Model class</h1>
 * <p>
 * Keep track of data used for polymorphism.
 * </p>
 */
abstract class Model: Serializable {

    /**
     * Convert given json object to required
     * model object according to the defined
     * annotation <code>JsonContent</code>
     *
     * @param json JSONObject Json object
     *
     * @return Unit
     */
    open fun  toModel(json: JSONObject) {
        for (field in this::class.java.declaredFields) {
            val annotation =  field.getAnnotation(JsonContent::class.java)
            annotation?.let {
                field.isAccessible = true
                field.set(this, json.opt(if(it.key.isNotEmpty()) it.key else field.name)?.toString())
            }
        }
    }
}