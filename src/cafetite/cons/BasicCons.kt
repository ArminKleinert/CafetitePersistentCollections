package cafetite.cons

object EmptyList : AbstractPersistentList<Any?>() {
    @get:Throws(NoSuchElementException::class)
    override val car: Any
        get() = throw NoSuchElementException("")

    override val cdr: EmptyList
        get() = this

    override val size: Int
        get() = 0

    override fun isEmpty(): Boolean = true

    override fun cleared(): PersistentList<Any?> = this

    override fun <R> sameTypeFromList(list: List<R>): PersistentList<R> {
        if (list.isEmpty()) return nullCons()
        return PersistentWrapper(list)
    }

    //override fun toString(): String = commonToString()
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> nullCons(): PersistentList<T> = EmptyList as PersistentList<T>

object Cons {
    fun <T> of(vararg elements: T): PersistentList<T> =
        if (elements.isEmpty()) nullCons()
        else PersistentListHead(elements.asIterable())

    fun <T> from(elements: Array<T>): PersistentList<T> =
        if (elements.isEmpty()) nullCons()
        else PersistentListHead(elements.asIterable())

    fun <T> from(elements: Iterable<T>): PersistentList<T> =
        if (!elements.iterator().hasNext()) nullCons()
        else PersistentListHead(elements)

    fun <T> from(elements: Collection<T>): PersistentList<T> =
        if (elements.isEmpty()) nullCons()
        else PersistentListHead(elements.asIterable())
}

class PersistentListHead<T> : AbstractPersistentList<T> {
    companion object {
//        fun <T> of(vararg elements: T) =
//            if (elements.isEmpty()) nullCons()
//            else PersistentListHead(elements.asIterable())
//        fun <T> from(elements: Array<T>) =
//            if (elements.isEmpty()) nullCons()
//            else PersistentListHead(elements.asIterable())
//        fun <T> from(elements: Iterable<T>) =
//            if (!elements.iterator().hasNext()) nullCons()
//            else PersistentListHead(elements)
//        fun <T> from(elements: Collection<T>) =
//            if (elements.isEmpty()) nullCons()
//            else PersistentListHead(elements.asIterable())
    }

    override val car: T

    override val cdr: PersistentList<T>

    private var lazySize: Int = -1

    override val size: Int
        get() {
            if (lazySize < 0) lazySize = cdr.size + 1
            return lazySize
        }

    constructor(car: T, cdr: PersistentList<T>) {
        this.car = car
        this.cdr = cdr
    }

    @Throws(NoSuchElementException::class)
    constructor(iter: Iterable<T>) {
        val iterator = iter.iterator()
        this.car = iterator.next()

        val tail = mutableListOf<T>()
        for (it in iterator)
            tail.add(it)
        this.cdr = PersistentWrapper(tail)
    }

    override fun isEmpty(): Boolean = false

    //override fun toString(): String = commonToString()

    override fun hashCode(): Int {
        var result = car?.hashCode() ?: 0
        result = 31 * result + cdr.hashCode()
        return result
    }

    override fun cleared(): PersistentList<T> = nullCons()
}