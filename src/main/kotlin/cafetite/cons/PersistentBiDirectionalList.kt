package cafetite.cons

class PersistentBiDirectionalList<T> : PersistentList<T>() {

override    val car:T
        get()= TODO()

    override val cdr: PersistentList<T>
        get() = TODO("Not yet implemented")

    override val size: Int
        get() = TODO("Not yet implemented")

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }
}