// sanity check to make sure junit working
// todo: for mocks check out https://github.com/mockito/mockito-kotlin (useful when testing controller behaviour relying on db i would think)
class Sample() {

    fun add(a: Int, b: Int): Int {
        return a + b
    }

    fun sub(a: Int, b: Int): Int {
        return a - b
    }
}
