package pages

fun calculatePercentage(a: Int, b: Int): Double {
    if (b == 0) {
        throw IllegalArgumentException("Cannot divide by zero")
    }

    val result = (a.toDouble() / b.toDouble()) * 100
    return String.format("%.2f", result).toDouble()
}