package cafetite.map

class PersistentSortedMap<K, V> : PersistentMap<K, V> {
    override val size: Int
        get() = entryArray.size

    override val entries: Set<Map.Entry<K, V>>
        get() = entryArray.toSortedSet { e1, e2 -> comparator(e1.key, e2.key) }
    override val keys: Set<K>
        get() = entryList().mapTo(sortedSetOf()) { it.key }

    private val entryArray: Array<Entry<K, V>>
    private val comparator: (K, K) -> Int

    companion object {
        fun <K : Comparable<K>, V> of(vararg elements: Pair<K, V>) =
            PersistentSortedMap(elements.toList()) { k1, k2 -> k1.compareTo(k2) }

        fun <K : Comparable<K>, V> from(elements: Map<K, V>) =
            PersistentSortedMap(elements.toList()) { k1, k2 -> k1.compareTo(k2) }

        fun <K : Comparable<K>, V> from(elements: Collection<Pair<K, V>>) =
            PersistentSortedMap(elements.toList()) { k1, k2 -> k1.compareTo(k2) }

        fun <K, V> of(vararg elements: Pair<K, V>, comparator: (K, K) -> Int) =
            PersistentSortedMap(elements.toSet(), comparator)

        fun <K, V> from(elements: Map<K, V>, comparator: (K, K) -> Int) =
            PersistentSortedMap(elements.toList(), comparator)

        fun <K, V> from(elements: Collection<Pair<K, V>>, comparator: (K, K) -> Int) =
            PersistentSortedMap(elements.toList(), comparator)
    }

    constructor(vararg elements: Pair<K, V>, comparator: (K, K) -> Int) :
            this(elements.distinctBy(Pair<K, V>::first), comparator)

    constructor(elements: Map<K, V>, comparator: (K, K) -> Int) :
            this(elements.toList(), comparator)

    constructor(elements: Collection<Pair<K, V>>, comparator: (K, K) -> Int) {
        val entries = mutableListOf<Entry<K, V>>()// elements.distinctBy(Pair<K,V>::first).sortedBy(Pair<K,V>::first)
        val keys = mutableSetOf<K>()
        for ((k, v) in elements) {
            if (k in keys) continue
            keys.add(k)
            entries.add(Entry(k, v))
        }
        this.comparator = comparator
        entries.sortWith { e1, e2 -> comparator(e1.key, e2.key) }
        entryArray = entries.toTypedArray()
    }

    override fun entryList(): List<Map.Entry<K, V>> = entryArray.toList()

    override fun iterator(): Iterator<Map.Entry<K, V>> = entryArray.iterator()

    override fun dissocAll(ks: Collection<K>): PersistentMap<K, V> {
        val discard = ks.toSet()
        return PersistentSortedMap(entryArray.filterNot { it.key in discard }.map { (k, v) -> k to v }, comparator)
    }

    override fun assocAll(coll: Collection<Pair<K, V>>): PersistentMap<K, V> {
        val pairs = mutableListOf<Pair<K, V>>()
        pairs.addAll(coll)
        pairs.addAll(entries.map { (k, v) -> k to v })
        return PersistentSortedMap(pairs, comparator)
    }

    override fun getEntry(key: K): Map.Entry<K, V>? {
        var lower = 0
        var upper = size - 1

        do {
            if (upper < lower)
                return null

            val mid = lower + (upper - lower) / 2
            val currentCompared = comparator(entryArray[mid].key, key)

            if (currentCompared == 0) return entryArray[mid]
            if (currentCompared < 0) lower = mid + 1
            else upper = mid - 1
        } while (true)
    }

    internal data class Entry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V> {
        override fun toString(): String = "($key, $value)"
    }
}