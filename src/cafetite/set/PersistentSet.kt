package cafetite.set

abstract class PersistentSet<T> : Set<T> {
    companion object {
        fun <T> of(vararg elements: T): PersistentSet<T> {
            if (elements.size <= 16) return PersistentArraySet(elements.toList())
            return PersistentHashSet(elements.toList())
        }

        fun <T> from(elements: Collection<T>): PersistentSet<T> {
            if (elements.size <= 16) return PersistentArraySet(elements)
            return PersistentHashSet(elements)
        }
    }

    abstract override val size: Int
    abstract override fun iterator(): Iterator<T>
    abstract override fun contains(element: T): Boolean
    abstract fun conjAll(elements: Collection<T>): PersistentSet<T>
    abstract fun withoutAll(elements: Collection<T>): PersistentSet<T>

    override fun isEmpty(): Boolean = size == 0

    override fun containsAll(elements: Collection<T>): Boolean {
        for (e in elements)
            if (!contains(e)) return false
        return true
    }

    fun conj(element: T): PersistentSet<T> =
        conjAll(listOf(element))

    fun without(element: T): PersistentSet<T> {
        if (!contains(element)) return this
        return withoutAll(listOf(element))
    }

    operator fun plus(entry: T): PersistentSet<T> = conjAll(listOf(entry))
    operator fun plus(entries: Collection<T>): PersistentSet<T> = conjAll(entries)

    operator fun minus(entry: T): PersistentSet<T> = withoutAll(listOf(entry))
    operator fun minus(keys: Collection<T>): PersistentSet<T> = withoutAll(keys)

    fun union(elements: Collection<T>) = conjAll(elements)

    fun intersection(elements: Collection<T>): PersistentSet<T> {
        val other = elements.toSet()
        val res = mutableListOf<T>()
        for (e in this)
            if (e !in other) res.add(e)
        return withoutAll(res)
    }

    infix fun and(other: Collection<T>) = intersection(other)

    fun difference(keys: Collection<T>): PersistentSet<T> = withoutAll(keys)

    fun symmetricDifference(other: Collection<T>): PersistentSet<T> =
        (withoutAll(other)).union(PersistentHashSet(other).withoutAll(this))

    infix fun xor(other: Collection<T>): PersistentSet<T> =
        symmetricDifference(other)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Collection<*>) return false
        if (size != other.size) return false

        for (e in other)
            if (e !in this) return false

        return true
    }

    override fun toString(): String =
        joinToString(", ", "[", "]") { it.toString() }

    override fun hashCode(): Int {
        var hash = 0
        for (e in this) hash = hash * 31 + e.hashCode()
        return hash
    }
}