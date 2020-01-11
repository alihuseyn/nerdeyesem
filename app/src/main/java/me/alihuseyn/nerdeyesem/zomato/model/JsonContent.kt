package me.alihuseyn.nerdeyesem.zomato.model

/**
 * <h1>JsonContent Annotation</h1>
 * <p>
 * Is used to define json content key.
 * According to this key the value will be selected
 * from json content and be set directly
 * </p>
 * <p>Usage:</p>
 * <code>
 *  ...
 *  @JsonContent("res_id") var res_id: String?
 *  ...
 * </code>
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonContent(val key:String = "")