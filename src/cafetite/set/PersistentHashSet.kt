package cafetite.set

import kotlin.math.max
import kotlin.math.min

class PersistentHashSet<T> : PersistentSet<T> {
    companion object {
        private fun nextPowerOfTwo(m: Int): Int {
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

    private val nodes: Array<Node<T>?>

    constructor(elements: Collection<T>) : this(
        arrayOfNulls(min(max(nextPowerOfTwo(elements.size), 8), 512)), elements
    )

    constructor(elements: Iterable<T>) : this(elements.toSet())

    constructor(elements: Set<T>) : this(
        arrayOfNulls(min(max(nextPowerOfTwo(elements.size), 8), 512)), elements
    )

    private constructor(existingNodes: Array<Node<T>?>, additionalElements: Collection<T>) {
        nodes = existingNodes.copyOf()
        for (element in additionalElements) {
            val h = hash(element)
            var nodeAt = nodes[h % nodes.size]
            var newNode = Node(element, null)
            while (nodeAt != null) {
                if (element != nodeAt.element) newNode = Node(element, newNode)
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
                    var currentNode: Node<T>? = node
                    while (currentNode != null) {
                        bufferedSize++
                        currentNode = currentNode.next
                    }
                }
            }
            return bufferedSize
        }

    override fun isEmpty(): Boolean = nodes.all { it == null }

    private fun hash(element: T): Int {
        if (element == null) return 0
        val h = element.hashCode()
        return h xor (h shr 16)
    }

    override fun iterator(): Iterator<T> = iterator {
        for (node in nodes) {
            var currentNode: Node<T>? = node
            while (currentNode != null) {
                yield(currentNode.element)
                currentNode = currentNode.next
            }
        }
    }

    override fun withoutAll(elements: Collection<T>): PersistentSet<T> {
        val s = elements.toSet()
        val lst = mutableListOf<T>()
        for (e in this)
            if (!s.contains(e))
                lst.add(e)
        return PersistentHashSet(lst)
    }

    override fun conjAll(elements: Collection<T>): PersistentSet<T> {
        if (elements.isEmpty()) return this
        if (containsAll(elements)) return this
        return PersistentHashSet(nodes, elements)
    }

    override fun contains(element: T): Boolean {
        var nodeAt = nodes[hash(element) % nodes.size]
        while (nodeAt != null) {
            if (nodeAt.element == element) return true
            nodeAt = nodeAt.next
        }
        return false
    }

    internal data class Node<T>(val element: T, val next: Node<T>?)
}