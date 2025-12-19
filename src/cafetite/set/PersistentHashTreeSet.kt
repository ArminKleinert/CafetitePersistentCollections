package cafetite.set

import cafetite.cons.VList

class PersistentHashTreeSet<T> : PersistentSet<T> {
    private val root: TreeNode<T>

    constructor() : this(TreeNode.leaf())

    @Suppress("UNCHECKED_CAST")
    constructor(elements: Iterable<T>) {
        root = conjAllToNode(TreeNode.Leaf as TreeNode<T>, elements.distinct())
    }

    constructor(elements: Collection<T>) : this(elements.asIterable())

    private constructor(root: TreeNode<T>) {
        this.root = root
    }

    companion object {
        fun <T> hash(e: T): Int = e.hashCode()

        fun <T> of(vararg elements: T): PersistentHashTreeSet<T> = PersistentHashTreeSet(elements.asIterable())

        private fun <T> conjAllToNode(node: TreeNode<T>, elements: Iterable<T>): TreeNode<T> {
            var newRoot = node
            for (e in elements) {
                val h = hash(e)
                newRoot = insert(h, e, newRoot)
            }

            if (newRoot === node) return node

            require(newRoot !== TreeNode.Leaf)

            return balance(toNodeList(newRoot))
        }

        private fun <T> insert(hash: Int, element: T, node: TreeNode<T>): TreeNode<T> =
            when (node) {
                TreeNode.Leaf ->
                    leaf(element)

                is TreeNode.Node<T> ->
                    if (hash < node.hash)
                        TreeNode.Node(node.hash, node.elements, insert(hash, element, node.left), node.right)
                    else if (hash > node.hash)
                        TreeNode.Node(node.hash, node.elements, node.left, insert(hash, element, node.right))
                    else if (element in node.elements) node
                    else leaf(node.elements.cons(element))
            }

        private fun <T> leaf(e: T) =
            TreeNode.Node(hash(e), VList.of(e), TreeNode.leaf(), TreeNode.leaf())

        private fun <T> leaf(elements: VList<T>) =
            TreeNode.Node(elements.first().hashCode(), elements, TreeNode.leaf(), TreeNode.leaf())

        private fun <T> toNodeList(node: TreeNode<T>): MutableList<TreeNode.Node<T>> {
            if (node === TreeNode.Leaf)
                return mutableListOf()

            val stack = mutableListOf<TreeNode.Node<T>>()
            val elements = mutableListOf<TreeNode.Node<T>>()

            require(node is TreeNode.Node<T>)
            stack.add(node)

            while (stack.isNotEmpty()) {
                val head = stack.last()
                stack.removeLast()

                if (head.left is TreeNode.Node<T>) stack.add(head.left)
                elements.add(head)
                if (head.right is TreeNode.Node<T>) stack.add(head.right)
            }

            elements.sortBy { it.hash }

            return elements
        }


        private fun <T> balance(elements: List<TreeNode.Node<T>>): TreeNode<T> {
            if (elements.isEmpty())
                return TreeNode.leaf()

            val halfLength = elements.size / 2
            val elementsLeft = balance(elements.take(halfLength))
            val elementMid = elements[halfLength]
            val elementRight = balance(elements.drop(halfLength + 1))

            return TreeNode.Node(elementMid.hash, elementMid.elements, elementsLeft, elementRight)
        }
    }

    private var bufferedSize = -1

    override val size: Int
        get() {
            if (bufferedSize != -1) return bufferedSize

            bufferedSize = when (root) {
                is TreeNode.Leaf -> 0
                is TreeNode.Node<T> -> root.deepSize()
            }

            return bufferedSize
        }

    override fun iterator(): Iterator<T> =
        toList().iterator()

    fun toList(): List<T> {
        return toNodeList(root).flatMap { it.elements }
    }

    override fun withoutAll(elements: Collection<T>): PersistentHashTreeSet<T> {
        if (root === TreeNode.Leaf)
            return this

        var newRoot: TreeNode<T> = root
        for (element in elements.distinct()) {
            newRoot = removed(hash(element), element, newRoot)
        }

        return PersistentHashTreeSet(newRoot)
    }

    override fun conjAll(elements: Collection<T>): PersistentHashTreeSet<T> {
        return PersistentHashTreeSet(conjAllToNode(root, elements))
    }

    private fun removed(hash: Int, element: T, node: TreeNode<T>): TreeNode<T> {
        if (node === TreeNode.Leaf) return TreeNode.leaf()
        require(node is TreeNode.Node<T>)

        if (hash < node.hash)
            return TreeNode.Node(node.hash, node.elements, removed(hash, element, node.left), node.right)

        if (hash > node.hash)
            return TreeNode.Node(node.hash, node.elements, node.left, removed(hash, element, node.right))

        if (element !in node.elements)
            return node

        val elements = node.elements - element

        if (elements.isNotEmpty())
            return TreeNode.Node(node.hash, VList.from(elements), node.left, node.right)

        val nodes = mutableListOf<TreeNode.Node<T>>()
        nodes.addAll(toNodeList(node.left))
        nodes.addAll(toNodeList(node.right))
        return balance(nodes)
    }

    override fun contains(element: T): Boolean =
        find(hash(element), element, root)

    private tailrec fun find(hash: Int, element: T, node: TreeNode<T>): Boolean =
        when (node) {
            TreeNode.Leaf ->
                false

            is TreeNode.Node<T> ->
                if (hash == node.hash) element in node.elements
                else if (hash < node.hash) find(hash, element, node.left)
                else find(hash, element, node.right)
        }

    override fun toString(): String =
        joinToString(", ", "[", "]") { it.toString() }

    internal sealed interface TreeNode<T> {
        data class Node<T>(
            val hash: Int, val elements: VList<T>, val left: TreeNode<T>, val right: TreeNode<T>
        ) : TreeNode<T> {
            override fun toString(): String {
                return "Node(hash=$hash, elements=$elements, left=$left, right=$right)"
            }

            fun deepSize(): Int {
                val stack = mutableListOf(this)
                var size = 0
                do {
                    val top = stack.removeLast()
                    size += top.elements.size
                    if (top.left !is Leaf) stack.add(top.left as Node<T>)
                    if (top.right !is Leaf) stack.add(top.right as Node<T>)
                } while (stack.isNotEmpty())
                return size
            }
        }

        data object Leaf : TreeNode<Any?>

        companion object {
            @Suppress("UNCHECKED_CAST")
            fun <T> leaf(): TreeNode<T> = Leaf as TreeNode<T>
        }
    }
}
