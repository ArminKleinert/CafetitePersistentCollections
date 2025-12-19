//package cafetite.cons
//
//sealed class FingerTree<T> : AbstractPersistentList<T>() {
//    override val car: T
//        get() = TODO("Not yet implemented")
//    override val cdr: PersistentList<T>
//        get() = TODO("Not yet implemented")
//    override val size: Int
//        get() = TODO("Not yet implemented")
//
//    companion object {
//        fun <T>toVList(iterable: Iterable<T>) = when (iterable) {
//            is FingerTree<T> -> iterable
//            else -> TODO()
//        }
//
//        fun <T> of(vararg elements: T) =
//            if (elements.isEmpty()) @Suppress("UNCHECKED_CAST") (Empty as FingerTree<T>)
//            else if (elements.size == 1) Single(elements.first())
//            else TODO()
//    }
//
//    override fun cons(element:T): FingerTree<T> = TODO()
//
//    override fun iterator(): Iterator<T> {
//        TODO()
//    }
//
//override    fun asSequence(): Sequence<T> = TODO()
//
//    override fun reversed(): PersistentList<T> {TODO()
//    }
//
//    class Single<T>(override val car:T) : FingerTree<T>() {
//        override val cdr: PersistentList<T>
//            get() = @Suppress("UNCHECKED_CAST") (Empty as FingerTree<T>)
//        override val size: Int
//            get() = 1
//        override fun isEmpty(): Boolean = false
//    }
//
//    sealed class Node<T> {
//
//    }
//
//     object Empty : FingerTree<Any?>() {
//        @get:Throws(NoSuchElementException::class)
//        override val car: Any get() = throw NoSuchElementException("")
//        override val cdr: EmptyList get() = EmptyList
//        override val size: Int get() = 0
//        override fun isEmpty(): Boolean = true
//        override fun cleared(): Empty = this
//         override fun toString(): String = commonToString()
//         override fun hashCode(): Int { return javaClass.hashCode() }
//    }
//}