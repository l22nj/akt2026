package week1

object HelloKotlin {
    @JvmStatic
    fun main(args: Array<String>) {
        val objs = listOf("Tere", false, "Kotlin", 2.3, "hüüumärk")
        println(objs)

        val answer = objs.sumOf(::convert)
        println(answer)
    }

    private fun convert(obj: Any): Int =
        when (obj) {
            "Tere" -> -12
            "Kotlin" -> 23
            is String -> obj.length
            is Double -> (obj * 10).toInt()
            else -> 0
        }
}
