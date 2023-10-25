import kotlin.test.Test
import kotlin.test.assertEquals

internal class SampleTest {
    private val testSample: Sample = Sample()

    @Test
    fun testAdd() {
        val expected = 10
        assertEquals(expected, testSample.add(4, 6))
    }

    @Test
    fun testSub() {
        val expected = -5
        assertEquals(expected, testSample.sub(7, 13))
    }
}
