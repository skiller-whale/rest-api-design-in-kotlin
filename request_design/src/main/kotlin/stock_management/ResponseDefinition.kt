package stock_management

//TODO: do we add templates in responses everywhere? I don't think stock instances should get a template.
//TODO: For what error codes do we return this response? If only for success, as now, should we remove the errors object?


data class TemplateProperty(val name: String, val type: String, val prompt: String?)

/**
 * Helper methods for construction a response from a given template.
 * If we want to be really explicit, or only care about one router, we can move this into the router class.
 */
fun <T> response(item: T, itemFormatter: (T) -> Any, template: List<TemplateProperty>) = response(listOf(item), itemFormatter, template)

fun <T> response(items: List<T>, itemFormatter: (T) -> Any, template: List<TemplateProperty>): Any {
    return object {
        val data = items.map(itemFormatter)
        val template = template
    }
}