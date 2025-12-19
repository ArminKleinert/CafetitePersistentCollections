package cafetite.map

import kotlin.math.max
import kotlin.math.min

class PersistentHashMap<K, V> : PersistentMap<K, V> {
    private val nodes: Array<Node<K, V>?>

    companion object {
//        private fun nextPowerOfTwo(m: Int): Int {
//            var n = m
//            n--
//            n = n or (n shr 1)
//            n = n or (n shr 2)
//            n = n or (n shr 4)
//            n = n or (n shr 8)
//            n = n or (n shr 16)
//            n++
//            return n
//        }

        fun <K, V> of(vararg pairs: Pair<K, V>) = PersistentHashMap(pairs.toList())
        fun <K, V> from(coll: Collection<Pair<K, V>>) = PersistentHashMap(coll)
        fun <K, V> from(coll: Map<K, V>) = if (coll is PersistentHashMap) coll else PersistentHashMap(coll)
    }

    constructor(elements: Collection<Pair<K, V>>) : this(
        arrayOfNulls(min(max(nextPowerOfTwo(elements.size / 2), 8), 512)), elements.map { (k, v) -> Entry(k, v) }
    )

    constructor(elements: Map<K, V>) : this(
        arrayOfNulls(min(max(nextPowerOfTwo(elements.size / 2), 8), 512)), elements.entries
    )

    private constructor(existingNodes: Array<Node<K, V>?>, additionalElements: Collection<Map.Entry<K, V>>) {
        nodes = existingNodes.copyOf()
        for ((key, value) in additionalElements) {
            val h = hash(key)
            var nodeAt = nodes[h % nodes.size]
            var newNode = Node(key, value, null)
            while (nodeAt != null) {
                if (key != nodeAt.key) newNode = Node(nodeAt.key, nodeAt.value, newNode)
                nodeAt = nodeAt.next
            }
            nodes[h % nodes.size] = newNode
        }
    }

    private var bufferedSize: Int = -1

    override val size: Int
        get() {
            if (bufferedSize < 0) {
                bufferedSize = 0
                for (node in nodes) {
                    var currentNode: Node<K, V>? = node
                    while (currentNode != null) {
                        bufferedSize++
                        currentNode = currentNode.next
                    }
                }
            }
            return bufferedSize
        }

    override fun isEmpty(): Boolean = nodes.all { it == null }

    private fun hash(key: K): Int {
        if (key == null) return 0
        val h = key.hashCode()
        return h xor (h shr 16)
    }

    override fun getEntry(key: K): Map.Entry<K, V>? {
        var subNode = nodes[hash(key) % nodes.size]
        while (subNode != null) {
            if (subNode.key == key) return subNode
            subNode = subNode.next
        }
        return null
    }

    override fun iterator(): Iterator<Map.Entry<K, V>> = iterator {
        for (node in nodes) {
            var currentNode: Node<K, V>? = node
            while (currentNode != null) {
                yield(Entry(currentNode.key, currentNode.value))
                currentNode = currentNode.next
            }
        }
    }

    override fun assocAll(coll: Collection<Pair<K, V>>): PersistentHashMap<K, V> {
        if (coll.isEmpty())
            return this
        return PersistentHashMap(nodes, coll.map { (k, v) -> Entry(k, v) })
    }

    override fun dissocAll(ks: Collection<K>): PersistentHashMap<K, V> {
        val s = ks.toSet()
        val lst = mutableListOf<Pair<K, V>>()
        for ((k, v) in this)
            if (!s.contains(k))
                lst.add(k to v)
        if (lst.size == size)
            return this
        return PersistentHashMap(lst)
    }

    override fun entryList(): List<Map.Entry<K, V>> {
        val res = mutableListOf<Map.Entry<K, V>>()
        for (node in nodes) {
            var currentNode: Node<K, V>? = node
            while (currentNode != null) {
                res.add(Entry(currentNode.key, currentNode.value))
                currentNode = currentNode.next
            }
        }
        return res
    }


    internal data class Node<K, V>(override val key: K, override val value: V, val next: Node<K, V>?) :
        Map.Entry<K, V>
}