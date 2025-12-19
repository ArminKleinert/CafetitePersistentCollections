package cafetite.cons

import kotlin.random.Random

abstract class AbstractPersistentList<T> : PersistentList<T> {
    protected fun commonToString(
        separator: CharSequence = ", ",
        prefix: CharSequence = "[",
        postfix: CharSequence = "]",
        limit: Int = -1,
        truncated: CharSequence = "...",
        buffer: Appendable = StringBuilder(),
    ): String {
        buffer.append(prefix)
        var count = 0
        for (element in this) {
            if (++count > 1) buffer.append(separator)
            if (!(limit < 0 || count <= limit)) break

            when (element) {
                is CharSequence? -> buffer.append(element)
                is Char -> buffer.append(element)
                else -> buffer.append(element.toString())
            }

        }
        if (limit in 0..<count) buffer.append(truncated)
        buffer.append(postfix)
        return buffer.toString()
    }

    override fun toString(): String = commonToString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is List<Any?>) return false

        val iter = iterator()
        val otherIter = other.iterator()

        while (iter.hasNext()) {
            if (!otherIter.hasNext()) return false
            if (iter.next() != otherIter.next()) return false
        }

        return !otherIter.hasNext()
    }

    override fun hashCode(): Int {
        var result = car?.hashCode() ?: 0
        result = 31 * result + cdr.hashCode()
        return result
    }

    protected open fun <R> sameTypeFromList(list: List<R>): PersistentList<R> = PersistentList.from(list)

    override fun cons(element: T): PersistentList<T> = PersistentListHead(element, this)

    override fun reversedPersistent(): PersistentList<T> {
        if (isEmpty())
            return this
        var res: PersistentList<T> = cleared()
        for (it in this) {
            res = res.cons(it)
        }
        return res
    }

    override fun subList(fromIndex: Int, toIndex: Int): PersistentList<T> =
        sameTypeFromList(toList().subList(fromIndex, toIndex))

    override fun shuffled(random: Random): PersistentList<T> =
        sameTypeFromList(toList().shuffled(random))

    override fun chunked(size: Int): PersistentList<List<T>> =
        sameTypeFromList(asIterable().chunked(size))

    override fun dropWhile(predicate: (T) -> Boolean): PersistentList<T> =
        sameTypeFromList(asIterable().dropWhile(predicate))

    override fun filterv(predicate: (T) -> Boolean): PersistentList<T> =
        sameTypeFromList(asIterable().filter(predicate))

    override fun filtervNot(predicate: (T) -> Boolean): PersistentList<T> =
        sameTypeFromList(asIterable().filterNot(predicate))

    override fun <R> runningFold(initial: R, operation: (acc: R, T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().runningFold(initial, operation))

    override fun <R> runningFoldIndexed(initial: R, operation: (index: Int, acc: R, T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().runningFoldIndexed(initial, operation))

    override fun runningReduce(operation: (acc: T, T) -> T): PersistentList<T> =
        sameTypeFromList(asIterable().runningReduce(operation))

    override fun runningReduceIndexed(operation: (index: Int, acc: T, T) -> T): PersistentList<T> =
        sameTypeFromList(asIterable().runningReduceIndexed(operation))

    override fun <R> scan(initial: R, operation: (acc: R, T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().scan(initial, operation))

    override fun <R> scanIndexed(initial: R, operation: (index: Int, acc: R, T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().scanIndexed(initial, operation))

    override fun <R : Comparable<R>> sortedBy(selector: (T) -> R?): PersistentList<T> =
        sameTypeFromList(asIterable().sortedBy(selector))

    override fun <R : Comparable<R>> sortedByDescending(selector: (T) -> R?): PersistentList<T> =
        sameTypeFromList(asIterable().sortedByDescending(selector))

    override fun sortedWith(comparator: Comparator<in T>): PersistentList<T> =
        sameTypeFromList(asIterable().sortedWith(comparator))

    override fun <R> mapv(transform: (T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().map(transform))

    override open fun take(n: Int): PersistentList<T> =
        if (isLazyType()) LazyList.take(n, this)
        else sameTypeFromList(asIterable().take(n))

    override fun withIndex(): PersistentList<IndexedValue<T>> =
        sameTypeFromList(asIterable().withIndex().toList())

    override fun <R> zip(other: PersistentList<R>): PersistentList<Pair<T, R>> =
        sameTypeFromList(asIterable().zip(other.asIterable()))

    override fun <R, V> zip(other: Iterable<R>, transform: (a: T, b: R) -> V): PersistentList<V> =
        sameTypeFromList(asIterable().zip(other.asIterable(), transform))

    override fun zipWithNext(): PersistentList<Pair<T, T>> =
        sameTypeFromList(asIterable().zipWithNext())

    override fun <R> zipWithNext(transform: (a: T, b: T) -> R): PersistentList<R> =
        sameTypeFromList(asIterable().zipWithNext(transform))
}