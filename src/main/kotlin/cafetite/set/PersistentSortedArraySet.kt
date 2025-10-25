package cafetite.set

class PersistentSortedArraySet<T> : PersistentSet<T> {
    private val arr: Array<Any?>
    private val comparator: Comparator<T>

    override val size: Int
        get() = arr.size

    constructor(elements: Collection<T>, sortingFunction: Comparator<T>) {
        val temp = ArrayList<Any?>(elements.size)
        var index = 0
        for (e in elements) {
            if (temp.contains(e))
                continue
            temp.add(e)
            index++
        }
        this.arr = temp.toTypedArray()

        @Suppress("UNCHECKED_CAST")
        (arr as Array<T>).sortWith(sortingFunction)

        this.comparator = sortingFunction
    }

    constructor(elements: Iterable<T>, sortingFunction: Comparator<T>) {
        val temp = ArrayList<Any?>()
        var index = 0
        for (e in elements) {
            if (temp.contains(e))
                continue
            temp.add(e)
            index++
        }
        this.arr = temp.toTypedArray()

        @Suppress("UNCHECKED_CAST")
        (arr as Array<T>).sortWith(sortingFunction)

        this.comparator = sortingFunction
    }

    constructor(elements: Set<T>, sortingFunction: Comparator<T>) {
        this.arr = arrayOfNulls(elements.size)
        for ((index, e) in elements.withIndex()) arr[index] = e

        @Suppress("UNCHECKED_CAST")
        (arr as Array<T>).sortWith(sortingFunction)

        this.comparator = sortingFunction
    }

    companion object {
        inline fun <reified T : Comparable<T>> of(vararg elements: T) =
            PersistentSortedArraySet(elements.asIterable()) { a: T, b: T -> a.compareTo(b) }
        inline fun <reified T : Comparable<T>> from(elements: Iterable<T>) =
            PersistentSortedArraySet(elements) { a: T, b: T -> a.compareTo(b) }
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
        return PersistentSortedArraySet(newElements, comparator)
    }

    override fun conjAll(elements: Collection<T>): PersistentSet<T> {
        if (elements.isEmpty()) return this
        val temp = ArrayList<T>()

        @Suppress("UNCHECKED_CAST")
        temp.addAll(arr as Array<T>)

        temp.addAll(elements)
        return from(temp) // May become PersistentHashSet
    }

    override fun contains(element: T): Boolean {
        if (size == 0)
            return false

        val indexOfElement = binarySearch(element)
        return indexOfElement >= 0
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun binarySearch(key: T): Int {
        var low = 0
        var high = size - 1
        while (low <= high) {
            val mid = low + high ushr 1
            val midVal = arr[mid]

            @Suppress("UNCHECKED_CAST")
            val compareResult: Int = comparator.compare(midVal as T, key)

            if (compareResult < 0) low = mid + 1
            else if (compareResult > 0) high = mid - 1
            else return mid // key found
        }
        return -(low + 1) // key not found.
    }
}
