package cafetite.map

import cafetite.map.PersistentArrayMap

abstract class PersistentMap<K, V> : Map<K, V>, Iterable<Map.Entry<K, V>> {
    companion object {
        fun <K, V> of(vararg elements: Pair<K, V>): PersistentMap<K, V> {
            if (elements.size <= 16) return PersistentArrayMap(elements.toList())
            return PersistentHashMap(elements.toList())
        }

        fun <K, V> from(elements: Collection<Pair<K, V>>): PersistentMap<K, V> {
            if (elements.size <= 16) return PersistentArrayMap(elements)
            return PersistentHashMap(elements)
        }

        fun <K, V> from(elements: Map<K, V>): PersistentMap<K, V> {
            if (elements is PersistentMap) return elements
            if (elements.size <= 16) return PersistentArrayMap(elements)
            return PersistentHashMap(elements)
        }

        internal fun nextPowerOfTwo(m: Int): Int {
            var n = m
            n--
            n = n or (n shr 1)
            n = n or (n shr 2)
            n = n or (n shr 4)
            n = n or (n shr 8)
            n = n or (n shr 16)
            n++
            return n
        }
    }

    abstract override val size: Int
    internal abstract fun entryList(): List<Map.Entry<K, V>>
    internal abstract fun getEntry(key: K): Map.Entry<K, V>?
    abstract override fun iterator(): Iterator<Map.Entry<K, V>>
    abstract fun assocAll(coll: Collection<Pair<K, V>>): PersistentMap<K, V>
    abstract fun dissocAll(ks: Collection<K>): PersistentMap<K, V>

    override val entries: Set<Map.Entry<K, V>>
        get() = toSet()
    override val keys: Set<K>
        get() = entryList().mapTo(mutableSetOf()) { it.key }
    override val values: Collection<V>
        get() = entryList().map { it.value }

    fun assocAll(coll: Map<K, V>): PersistentMap<K, V> {
        if (coll.isEmpty()) return this
        return assocAll(coll.map { (k, v) -> k to v })
    }

    override fun isEmpty(): Boolean = size == 0

    override fun get(key: K): V? = getEntry(key)?.value

    fun selectKeys(keys: Iterable<K>): PersistentMap<K, V> {
        val res = mutableListOf<Pair<K, V>>()
        for (k in keys){
            val (key, value) = getEntry(k) ?: continue
            res.add(key to value)
        }
        return from(res)
    }

    override fun containsValue(value: V): Boolean = entryList().map { it.value }.contains(value)
    override fun containsKey(key: K): Boolean = getEntry(key) != null
    fun contains(key: K): Boolean = getEntry(key) != null

    override fun getOrDefault(key: K, defaultValue: V): V = getEntry(key)?.value ?: defaultValue

    fun assoc(k: K, v: V): PersistentMap<K, V> = assocAll(listOf(k to v))
    operator fun plus(entry: Pair<K, V>): PersistentMap<K, V> = assocAll(listOf(entry))
    operator fun plus(entries: Collection<Pair<K, V>>): PersistentMap<K, V> = assocAll(entries)
    operator fun plus(entries: Map<K, V>): PersistentMap<K, V> = assocAll(entries)

    fun dissoc(key: K): PersistentMap<K, V> {
        if (!containsKey(key)) return this
        return dissocAll(listOf(key))
    }

    operator fun minus(entry: K): PersistentMap<K, V> = dissocAll(listOf(entry))
    operator fun minus(keys: Collection<K>): PersistentMap<K, V> = dissocAll(keys)

    override fun toString(): String = joinToString(", ", prefix = "{", postfix = "}") { (k, v) -> "${k}=${v}" }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Map<*, *>) return false
        if (size != other.size) return false

        for ((k, v) in this)
            if (!other.contains(k) || v != other[k])
                return false

        return true
    }

    override fun hashCode(): Int {
        var hash = 0
        for (e in this) hash = hash * 31 + e.hashCode()
        return hash
    }
}