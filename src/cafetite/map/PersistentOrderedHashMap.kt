package cafetite.map

import kotlin.math.max
import kotlin.math.min

class PersistentOrderedHashMap<K, V> : PersistentMap<K, V> {
    private val nodes: Array<Node<K, V>?>
    private val orderedKeys: List<K>

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

        fun <K, V> of(vararg pairs: Pair<K, V>) = PersistentOrderedHashMap(pairs.toList())
        fun <K, V> from(coll: Collection<Pair<K, V>>) = PersistentOrderedHashMap(coll)
        fun <K, V> from(coll: Map<K, V>) =
            if (coll is PersistentOrderedHashMap) coll else PersistentOrderedHashMap(coll)
    }

    constructor(elements: Collection<Pair<K, V>>) : this(
        arrayOfNulls(min(max(nextPowerOfTwo(elements.size / 2), 8), 512)),
        listOf(),
        elements.map { (k, v) -> Entry(k, v) }
    )

    constructor(elements: Map<K, V>) : this(
        arrayOfNulls(min(max(nextPowerOfTwo(elements.size / 2), 8), 512)), listOf(), elements.entries
    )

    private constructor(
        existingNodes: Array<Node<K, V>?>,
        oldOrderedKeys: List<K>,
        additionalElements: Collection<Map.Entry<K, V>>
    ) {
//        nodes = existingNodes.copyOf()
//        val mutableKeys = oldOrderedKeys.toMutableList()
//        for ((key, value) in additionalElements) {
//            val h = hash(key)
//            var nodeAt = nodes[h % nodes.size]
//            var newNode = Node(key, value, null)
//            while (nodeAt != null) {
//                if (key != nodeAt.key) newNode = Node(nodeAt.key, nodeAt.value, newNode)
//                nodeAt = nodeAt.next
//            }
//            nodes[h % nodes.size] = newNode
//        }
//        orderedKeys = mutableKeys.toList()
        nodes = existingNodes.copyOf()
        val mutableKeys = oldOrderedKeys.toMutableList()

        for ((key, value) in additionalElements) {
            val h = hash(key)
            var nodeAt = nodes[h % nodes.size]
            while (nodeAt != null && key != nodeAt.key)
                nodeAt = nodeAt.next

            // If the key did not exist.
            if (nodeAt == null) {
                nodes[h % nodes.size] = Node(key, value, nodes[h % nodes.size])
                mutableKeys += key
                continue
            }

            nodeAt = nodes[h % nodes.size]
            var newNode = Node(key, value, null)
            while (nodeAt != null) {
                if (key != nodeAt.key) newNode = Node(nodeAt.key, nodeAt.value, newNode)
                nodeAt = nodeAt.next
            }
            nodes[h % nodes.size] = newNode
        }

        orderedKeys = mutableKeys.toList()
    }

    val keySeq: List<K>
        get() = orderedKeys

    override val keys: Set<K>
        get() = orderedKeys.toSet()

    override val size: Int
        get() = orderedKeys.size

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
        for (orderedKey in orderedKeys) {
            yield(Entry(orderedKey, get(orderedKey)!!))
        }
    }

    override fun assocAll(coll: Collection<Pair<K, V>>): PersistentOrderedHashMap<K, V> {
        if (coll.isEmpty())
            return this
        return PersistentOrderedHashMap(nodes, orderedKeys, coll.map { (k, v) -> Entry(k, v) })
    }

    override fun dissocAll(ks: Collection<K>): PersistentOrderedHashMap<K, V> {
        val s = ks.toSet()
        val lst = mutableListOf<Pair<K, V>>()
        for ((k, v) in this)
            if (!s.contains(k))
                lst.add(k to v)
        if (lst.size == size)
            return this
        return PersistentOrderedHashMap(lst)
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