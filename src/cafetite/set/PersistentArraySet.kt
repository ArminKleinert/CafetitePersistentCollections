package cafetite.set

class PersistentArraySet<T> : PersistentSet<T> {
    private val arr: Array<Any?>

    override val size: Int
        get() = arr.size

    constructor(elements: Collection<T>) {
        val temp = ArrayList<Any?>(elements.size)
        var index = 0
        for (e in elements) {
            if (temp.contains(e))
                continue
            temp.add(e)
            index++
        }
        this.arr = temp.toTypedArray()
    }

    constructor(elements: Iterable<T>) {
        val temp = ArrayList<Any?>()
        var index = 0
        for (e in elements) {
            if (temp.contains(e))
                continue
            temp.add(e)
            index++
        }
        this.arr = temp.toTypedArray()
    }

    constructor(elements: Set<T>) {
        this.arr = arrayOfNulls(elements.size)
        for ((index, e) in elements.withIndex()) arr[index] = e
    }

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var index: Int = 0

        override fun hasNext(): Boolean =
            index < arr.size

        override fun next(): T {
            @Suppress("UNCHECKED_CAST")
            val e = arr[index] as T
            index++
            return e
        }
    }

    override fun withoutAll(elements: Collection<T>): PersistentSet<T> {
        if (elements.isEmpty()) return this
        val other = elements.toSet()
        val newElements = mutableListOf<T>()
        for (element in this) {
            if (element !in other) newElements.add(element)
        }
        return PersistentArraySet(newElements)
    }

    override fun conjAll(elements: Collection<T>): PersistentSet<T> {
        if (elements.isEmpty()) return this
        val temp = ArrayList<T>()
        @Suppress("UNCHECKED_CAST")
        temp.addAll(arr as Array<T>)
        temp.addAll(elements)
        return from(temp) // May become PersistentHashSet
    }

    override fun contains(element: T): Boolean = arr.contains(element)
}