package cafetite.map

class PersistentArrayMap<K, V> : PersistentMap<K, V> {
    private val arr: Array<Any?>

    companion object {
        fun <K, V> of(vararg pairs: Pair<K, V>) = PersistentArrayMap(pairs.toList())
        fun <K, V> from(coll: Collection<Pair<K, V>>) = PersistentArrayMap(coll)
        fun <K, V> from(coll: Map<K, V>) = if (coll is PersistentArrayMap) coll else PersistentArrayMap(coll)
    }

    constructor(entries: Collection<Pair<K, V>>) {
        val temp = ArrayList<Any?>(entries.size * 2)
        val registeredKeys = mutableSetOf<K>()
        for ((k, v) in entries) {
            if (registeredKeys.contains(k))
                continue
            registeredKeys.add(k)
            temp.add(k)
            temp.add(v)
        }
        this.arr = Array(temp.size) { temp[it] }
    }

    constructor(m: Map<K, V>) : this(m.toList())

    override val size: Int
        get() = arr.size / 2

    override fun entryList(): List<Map.Entry<K, V>> {
        val res = mutableListOf<Map.Entry<K, V>>()
        for (entry in this)
            res.add(entry)
        return res
    }

    override fun getEntry(key: K): Map.Entry<K, V>? {
        var index = 0
        while (index < arr.size) {
            @Suppress("UNCHECKED_CAST")
            if (arr[index] == key) return Entry(arr[index] as K, arr[index + 1] as V)
            index += 2
        }
        return null
    }

    override fun get(key: K): V? {
        var index = 0
        while (index < arr.size) {
            @Suppress("UNCHECKED_CAST")
            if (key == arr[index]) return arr[index + 1] as V
            index += 2
        }
        return null
    }

    override fun containsValue(value: V): Boolean {
        var index = 1
        while (index < arr.size) {
            if (value == arr[index]) return true
            index += 2
        }
        return false
    }

    override fun containsKey(key: K): Boolean {
        var index = 0
        while (index < arr.size) {
            if (key == arr[index]) return true
            index += 2
        }
        return false
    }

    override fun assocAll(coll: Collection<Pair<K, V>>): PersistentArrayMap<K, V> {
        val lst = mutableListOf<Pair<K, V>>()
        for ((k, v) in this)
            lst.add(k to v)
        for ((k, v) in coll)
            lst.add(k to v)
        return PersistentArrayMap(lst)
    }

    override fun dissocAll(ks: Collection<K>): PersistentArrayMap<K, V> {
        val s = ks.toSet()
        val lst = mutableListOf<Pair<K, V>>()
        for ((k, v) in this)
            if (!s.contains(k))
                lst.add(k to v)
        return PersistentArrayMap(lst)
    }

    override fun iterator(): Iterator<Map.Entry<K, V>> = object : Iterator<Map.Entry<K, V>> {
        var index: Int = 0

        override fun hasNext(): Boolean =
            index < arr.size

        override fun next(): Map.Entry<K, V> {
            @Suppress("UNCHECKED_CAST")
            val e = Entry(arr[index] as K, arr[index + 1] as V)
            index += 2
            return e
        }
    }

    override fun equals(other: Any?): Boolean =
        super.equals(other)

    override fun hashCode(): Int =
        arr.contentHashCode()
}