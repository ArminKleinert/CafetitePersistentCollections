package cafetite.set

import kotlin.math.log2
import kotlin.math.max
import kotlin.random.Random

class IntSet : Iterable<Int> {
    private var inner: IntArray

    var size = 0
        private set

    companion object {
        private const val RESIZE_FACTOR = 0.75

        fun of(vararg ints: Int) = IntSet().let {
            it.addAll(ints)
            it
        }

        fun from(iterable: Iterable<Int>): IntSet {
            val res = IntSet()
            res.addAll(iterable)
            return res
        }
    }

    constructor(initialSize: Int) {
        inner = IntArray(max(log2(initialSize.toDouble()).toInt() + 1, 32))
    }

    constructor() {
        inner = IntArray(32)
    }

    constructor(iarr: IntArray) : this() {
        addAll(iarr)
    }

    constructor(icoll: Collection<Int>) : this() {
        addAll(icoll)
    }

    constructor(i: IntSet) : this() {
        addAll(i)
    }

    constructor(i: IntProgression) : this() {
        addAll(i)
    }

    private fun internalSort() {
        inner.sort(0, size)
    }

    operator fun contains(i: Int) = size != 0 && inner.binarySearch(i, toIndex = size) >= 0
    operator fun contains(i: Any?) = i is Int && contains(i)

    private fun resizeIfThresholdReached(minSize: Int = inner.size) {
        if (inner.size < minSize)
            inner = inner.copyOf(1 shl (log2(minSize.toDouble()).toInt() + 1))
        else if (inner.size * RESIZE_FACTOR < size)
            inner = inner.copyOf(size shl 1)
    }

    private fun compress() {
        inner = inner.copyOf((size * 1.5).toInt())
    }

    fun validate(): Boolean {
        if (size == 0)
            return true

        var prev = inner[0]
        var idx = 1
        while (idx < size) {
            val temp = inner[idx]
            if (prev >= temp)
                return false
            prev = temp
            idx++
        }

        return true
    }

    fun add(i: Int): Boolean {
        if (contains(i))
            return false
        inner[size] = i
        size++
        internalSort()
        resizeIfThresholdReached()
        return true
    }

    fun addAll(ix: IntProgression): Boolean {
        resizeIfThresholdReached(size + ix.count())
        var result = false
        for (i in ix) {
            val temp = add(i)
            result = temp || result
        }
        return result
    }

    fun addAll(ix: IntArray): Boolean {
        resizeIfThresholdReached(size + ix.size)
        var result = false
        for (i in ix) {
            val temp = add(i)
            result = temp || result
        }
        return result
    }

    fun addAll(ix: IntSet): Boolean {
        resizeIfThresholdReached(size + ix.size)
        var result = false
        for (idx in 0..<ix.size) {
            val temp = add(ix.elementAt(idx))
            result = temp || result
        }
        return result
    }

    fun addAll(ix: Collection<Int>): Boolean {
        return addAll(ix.asIterable())
    }

    fun addAll(ix: Iterable<Int>): Boolean {
        var result = false
        for (i in ix) {
            val temp = add(i)
            result = temp || result
        }
        return result
    }

    fun remove(i: Int): Boolean {
        val idx = inner.binarySearch(i)
        if (idx < 0) return false
        inner[idx] = Int.MAX_VALUE
        internalSort()
        inner[size] = 0
        size--
        return true
    }

    fun removeAll(ix: IntRange): Boolean {
        var result = false
        for (i in ix) {
            val temp = remove(i)
            result = temp || result
        }
        return result
    }

    fun removeAll(ix: IntArray): Boolean {
        var result = false
        for (i in ix) {
            val temp = remove(i)
            result = temp || result
        }
        return result
    }

    fun removeAll(ix: IntSet): Boolean {
        var result = false
        for (idx in 0..<ix.size) {
            val temp = remove(ix.elementAt(idx))
            result = temp || result
        }
        return result
    }

    fun copyOf(minSize: Int = size) = IntSet().also {
        it.resizeIfThresholdReached(minSize)
        it.addAll(this)
    }

    operator fun plus(other: IntProgression) = copyOf(size + other.count()).also {
        it.addAll(other)
        it.compress()
    }

    operator fun plusAssign(other: IntProgression) {
        addAll(other)
    }

    operator fun plus(other: IntSet) = copyOf(size + other.count()).also {
        it.addAll(other)
        it.compress()
    }

    operator fun plusAssign(other: IntSet) {
        addAll(other)
    }

    operator fun plus(i: Int) = copyOf().also {
        it.add(i)
    }

    operator fun plusAssign(other: Int) {
        add(other)
    }

    operator fun minus(other: IntSet) = copyOf().also {
        for (idx in 0..<other.size) it.remove(other.elementAt(idx))
        it.compress()
    }

    operator fun minus(other: IntProgression) = copyOf().also {
        for (i in other) it.remove(i)
        it.compress()
    }

    operator fun minus(i: Int) = copyOf().also {
        it.remove(i)
    }

    infix fun and(other: IntSet) = filter { it in other }

    infix fun or(other: IntSet) = plus(other)

    inline fun map(f: (Int) -> Int) = asIterable().map(f)

    inline fun map(target: IntSet, f: (Int) -> Int) = target.also {
        for (idx in 0..<size)
            it.add(f(elementAt(idx)))
    }

    inline fun map(target: IntArray, f: (Int) -> Int) = target.also {
        require(target.size >= size)
        for (idx in 0..<size)
            target[idx] = f(elementAt(idx))
    }

    inline fun mapToIntSet(f: (Int) -> Int) = map(IntSet(size * 2), f)

    inline fun mapToArray(f: (Int) -> Int) = toIntArray().also {
        for (idx in 0..<size)
            it[idx] = f(it[idx])
    }

    inline fun filter(f: (Int) -> Boolean) = IntSet().also {
        for (idx in 0..<size) {
            val i = elementAt(idx)
            if (f(i))
                it.add(i)
        }
    }

    inline fun filterNot(f: (Int) -> Boolean) = IntSet().also {
        for (idx in 0..<size) {
            val i = elementAt(idx)
            if (!f(i))
                it.add(i)
        }
    }

    fun toList(): List<Int> {
        val res = mutableListOf<Int>()
        for (idx in 0..<size) res.add(inner[idx])
        return res
    }

    override operator fun iterator() = inner.copyOf(size).iterator()

    inline fun forEach(action: (Int) -> Unit) {
        for (idx in 0..<size) action(elementAt(idx))
    }

    fun toIntArray() = inner.copyOf(size)

    fun random() = inner[Random.nextInt(0, size - 1)]

    operator fun get(i: Int) = contains(i)

    operator fun set(i: Int, value: Boolean) = if (value) add(i) else remove(i)

    override fun toString() = buildString {
        append('[')

        sequenceOf(1, 2, 3).shuffled()

        if (size > 0) {
            for (idx in 0..<size - 1) {
                append(inner[idx])
                append(", ")
            }
            append(inner[size - 1])
        }

        append(']')
    }

    fun isEmpty() = size == 0

    override fun equals(other: Any?): Boolean {
        if (other !is IntSet) return false
        if (other.size != size) return false

        for (idx in 0..<size)
            if (other.inner[idx] != inner[idx])
                return false

        return true
    }

    override fun hashCode(): Int = inner.contentHashCode()

    fun elementAt(idx: Int) = inner[idx]
}
