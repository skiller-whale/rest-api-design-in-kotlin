package stock_management

data class TemplateProperty(val name: String, val type: String, val prompt: String?)

fun <T> response(item: T, itemFormatter: (T) -> Any, template: List<TemplateProperty>) = response(listOf(item), itemFormatter, template)

fun <T> response(items: List<T>, itemFormatter: (T) -> Any, template: List<TemplateProperty>): Any {
    return object {
        val data = items.map(itemFormatter)
        val template = template
    }
}