package cafetite.cons

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LazyListTest {
    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T> from(coll: Array<T>) = LazyList.from(coll)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T> from(coll: Iterable<T>) = LazyList.from(coll)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T> from(coll: Collection<T>) = LazyList.from(coll)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T> of(vararg xs: T) = LazyList.of(*xs)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T> plOf(vararg xs: T) = PersistentList.of(*xs)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T> from(coll: Sequence<T>) = LazyList.from(coll)

    @Test
    fun testConstructor() {
        Assertions.assertEquals(of<Int>(), from(listOf<Int>()))
        Assertions.assertEquals(from<Int>(arrayOf()), from(listOf<Int>()))
        Assertions.assertEquals(from(listOf<Int>().asIterable()), from(listOf<Int>()))
        Assertions.assertEquals(from(of<Int>()), from(listOf<Int>()))
        Assertions.assertEquals(from(sequenceOf<Int>()), from(listOf<Int>()))

        Assertions.assertEquals(of(1), from(listOf(1)))
        Assertions.assertEquals(from(arrayOf(1)), from(listOf(1)))
        Assertions.assertEquals(from(listOf(1).asIterable()), from(listOf(1)))
        Assertions.assertEquals(from(of(1)), from(listOf(1)))
        Assertions.assertEquals(from(sequenceOf(1)), from(listOf(1)))

        Assertions.assertEquals(of(1, 2, 3, 4, 5), from(listOf(1, 2, 3, 4, 5)))
        Assertions.assertEquals(from(arrayOf(1, 2, 3, 4, 5)), from(listOf(1, 2, 3, 4, 5)))
        Assertions.assertEquals(from(listOf(1, 2, 3, 4, 5).asIterable()), from(listOf(1, 2, 3, 4, 5)))
        Assertions.assertEquals(from(of(1, 2, 3, 4, 5)), from(listOf(1, 2, 3, 4, 5)))
        Assertions.assertEquals(from(sequenceOf(1, 2, 3, 4, 5)), from(listOf(1, 2, 3, 4, 5)))
    }

    @Test
    fun of() {
        Assertions.assertEquals(plOf<Int>(), of<Int>())
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3))
    }

    @Test
    fun testFrom() {
        Assertions.assertEquals(plOf<Int>(), from<Int>(listOf()))
        Assertions.assertEquals(plOf(1, 2, 3), from(listOf(1, 2, 3)))
        Assertions.assertEquals(plOf<Int>(), from<Int>(listOf()))
        Assertions.assertEquals(plOf(1, 2, 3), from(listOf(1, 2, 3)))

        Assertions.assertEquals(plOf<Int>(), from(listOf<Int>().asIterable()))
        Assertions.assertEquals(plOf(1, 2, 3), from(listOf(1, 2, 3).asIterable()))
        Assertions.assertEquals(plOf<Int>(), from(listOf<Int>().asIterable()))
        Assertions.assertEquals(plOf(1, 2, 3), from(listOf(1, 2, 3).asIterable()))

        Assertions.assertEquals(plOf<Int>(), from(arrayOf<Int>()))
        Assertions.assertEquals(plOf(1, 2, 3), from(arrayOf(1, 2, 3)))
        Assertions.assertEquals(plOf<Int>(), from(arrayOf<Int>()))
        Assertions.assertEquals(plOf(1, 2, 3), from(arrayOf(1, 2, 3)))

        Assertions.assertEquals(plOf<Int>(), from(sequenceOf<Int>()))
        Assertions.assertEquals(plOf(1, 2, 3), from(sequenceOf(1, 2, 3)))
        Assertions.assertEquals(plOf<Int>(), from(sequenceOf<Int>()))
        Assertions.assertEquals(plOf(1, 2, 3), from(sequenceOf(1, 2, 3)))
    }

    @Test
    fun testToString() {
        Assertions.assertEquals("[]", of<Int>().toString())
        Assertions.assertEquals("[1, 2, 3]", of(1, 2, 3).toString())
        Assertions.assertEquals("[1, 2, 3, 4]", of(1, 2, 3, 4).toString(limit = 4))
        Assertions.assertEquals("[1, 2, 3, ...]", of(1, 2, 3, 4).toString(limit = 3))
        Assertions.assertEquals("[0, 0, 0, 0, 0, ...]", LazyList.repeat(0).toString(limit = 5))
    }

    @Test
    fun getCar() {
        Assertions.assertThrows(NoSuchElementException::class.java) { of<Int>().car }
        Assertions.assertEquals(1, of(1, 2, 3, 4, 5).car)
    }

    @Test
    fun first() {
        Assertions.assertThrows(NoSuchElementException::class.java) { of<Int>().car }
        Assertions.assertEquals(1, of(1, 2, 3, 4, 5).first())
    }

    @Test
    fun head() {
        Assertions.assertThrows(NoSuchElementException::class.java) { of<Int>().car }
        Assertions.assertEquals(1, of(1, 2, 3, 4, 5).head())
    }

    @Test
    fun getCadr() {
        Assertions.assertThrows(NoSuchElementException::class.java) { of<Int>().cadr }
        Assertions.assertThrows(NoSuchElementException::class.java) { of(1).cadr }
        Assertions.assertEquals(2, of(1, 2, 3, 4, 5).cadr)
    }

    @Test
    fun getCaddr() {
        Assertions.assertThrows(NoSuchElementException::class.java) { of<Int>().caddr }
        Assertions.assertThrows(NoSuchElementException::class.java) { of(1).caddr }
        Assertions.assertThrows(NoSuchElementException::class.java) { of(1, 2).caddr }
        Assertions.assertEquals(3, of(1, 2, 3, 4, 5).caddr)
    }

    @Test
    fun getCadddr() {
        Assertions.assertThrows(NoSuchElementException::class.java) { of<Int>().cadddr }
        Assertions.assertThrows(NoSuchElementException::class.java) { of(1).cadddr }
        Assertions.assertThrows(NoSuchElementException::class.java) { of(1, 2).cadddr }
        Assertions.assertThrows(NoSuchElementException::class.java) { of(1, 2, 3).cadddr }
        Assertions.assertEquals(4, of(1, 2, 3, 4, 5).cadddr)
    }

    @Test
    fun getCdr() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().cdr)
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 4, 5).cdr)
        Assertions.assertEquals(plOf<Int>(), of<Int>().cdr)
        Assertions.assertEquals(plOf<Int>(), of(1).cdr)
        Assertions.assertEquals(plOf(2), of(1, 2).cdr)
        Assertions.assertEquals(plOf(2, 3), of(1, 2, 3).cdr)
        Assertions.assertEquals(plOf(2, 3, 4), of(1, 2, 3, 4).cdr)
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(1, 2, 3, 4, 5).cdr)
    }

    @Test
    fun butfirst() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().butfirst())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 4, 5).butfirst())
        Assertions.assertEquals(plOf<Int>(), of<Int>().butfirst())
        Assertions.assertEquals(plOf<Int>(), of(1).butfirst())
        Assertions.assertEquals(plOf(2), of(1, 2).butfirst())
        Assertions.assertEquals(plOf(2, 3), of(1, 2, 3).butfirst())
        Assertions.assertEquals(plOf(2, 3, 4), of(1, 2, 3, 4).butfirst())
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(1, 2, 3, 4, 5).butfirst())
    }

    @Test
    fun tail() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().tail())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 4, 5).tail())
        Assertions.assertEquals(plOf<Int>(), of<Int>().tail())
        Assertions.assertEquals(plOf<Int>(), of(1).tail())
        Assertions.assertEquals(plOf(2), of(1, 2).tail())
        Assertions.assertEquals(plOf(2, 3), of(1, 2, 3).tail())
        Assertions.assertEquals(plOf(2, 3, 4), of(1, 2, 3, 4).tail())
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(1, 2, 3, 4, 5).tail())
    }

    @Test
    fun rest() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().rest())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 4, 5).rest())
        Assertions.assertEquals(plOf<Int>(), of<Int>().rest())
        Assertions.assertEquals(plOf<Int>(), of(1).rest())
        Assertions.assertEquals(plOf(2), of(1, 2).rest())
        Assertions.assertEquals(plOf(2, 3), of(1, 2, 3).rest())
        Assertions.assertEquals(plOf(2, 3, 4), of(1, 2, 3, 4).rest())
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(1, 2, 3, 4, 5).rest())
    }

    @Test
    fun next() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 4, 5).next())
        Assertions.assertEquals(null, of<Int>().next())
        Assertions.assertEquals(null, of(1).next())
        Assertions.assertEquals(plOf(2), of(1, 2).next())
        Assertions.assertEquals(plOf(2, 3), of(1, 2, 3).next())
        Assertions.assertEquals(plOf(2, 3, 4), of(1, 2, 3, 4).next())
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(1, 2, 3, 4, 5).next())
    }

    @Test
    fun getCddr() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().cddr)
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 4, 5).cddr)
        Assertions.assertEquals(plOf<Int>(), of<Int>().cddr)
        Assertions.assertEquals(plOf<Int>(), of(1).cddr)
        Assertions.assertEquals(plOf<Int>(), of(1, 2).cddr)
        Assertions.assertEquals(plOf(3), of(1, 2, 3).cddr)
        Assertions.assertEquals(plOf(3, 4), of(1, 2, 3, 4).cddr)
        Assertions.assertEquals(plOf(3, 4, 5), of(1, 2, 3, 4, 5).cddr)
    }

    @Test
    fun getCdddr() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().cdddr)
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 4, 5).cdddr)
        Assertions.assertEquals(plOf<Int>(), of<Int>().cdddr)
        Assertions.assertEquals(plOf<Int>(), of(1).cdddr)
        Assertions.assertEquals(plOf<Int>(), of(1, 2).cdddr)
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3).cdddr)
        Assertions.assertEquals(plOf(4), of(1, 2, 3, 4).cdddr)
        Assertions.assertEquals(plOf(4, 5), of(1, 2, 3, 4, 5).cdddr)
    }

    @Test
    fun getCddddr() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().cddddr)
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 4, 5).cddddr)
        Assertions.assertEquals(plOf<Int>(), of<Int>().cddddr)
        Assertions.assertEquals(plOf<Int>(), of(1).cddddr)
        Assertions.assertEquals(plOf<Int>(), of(1, 2).cddddr)
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3).cddddr)
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3, 4).cddddr)
        Assertions.assertEquals(plOf(5), of(1, 2, 3, 4, 5).cddddr)
    }

    @Test
    fun cons() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().cons(1))
        Assertions.assertInstanceOf(PersistentList::class.java, of(2).cons(1))
        Assertions.assertEquals(plOf(1), of<Int>().cons(1))
        Assertions.assertEquals(plOf(1, 2), of(2).cons(1))
        Assertions.assertEquals(plOf(1, 2, 3), of(2, 3).cons(1))
    }

    @Test
    fun concat() {
        Assertions.assertInstanceOf(PersistentList::class.java, PersistentList.concat(of<Int>(), of<Int>()))
        Assertions.assertInstanceOf(PersistentList::class.java, PersistentList.concat(of(1, 2, 3), of(4, 5, 6)))
        Assertions.assertEquals(plOf<Int>(), PersistentList.concat(of<Int>(), of<Int>()))
        Assertions.assertEquals(plOf(1, 2, 3), PersistentList.concat(of(1, 2, 3), of<Int>()))
        Assertions.assertEquals(plOf(1, 2, 3), PersistentList.concat(of<Int>(), of(1, 2, 3)))
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5, 6), PersistentList.concat(of(1, 2, 3), of(4, 5, 6)))
    }

    @Test
    fun getSize() {
        Assertions.assertEquals(0, of<Int>().size)
        Assertions.assertEquals(1, of<Int>().cons(1).size)
        Assertions.assertEquals(1, of(1).size)
        Assertions.assertEquals(2, of(1, 3).size)
    }

    @Test
    fun all() {
        Assertions.assertEquals(true, of<Int>().all { it > 0 })
        Assertions.assertEquals(true, of(1).all { it > 0 })
        Assertions.assertEquals(false, of(0).all { it > 0 })
        Assertions.assertEquals(false, of(-1).all { it > 0 })
        Assertions.assertEquals(false, of(-1, 0, 1).all { it > 0 })
        Assertions.assertEquals(true, of(1, 2, 3, 4, 5, 6).all { it > 0 })
    }

    @Test
    fun any() {
        Assertions.assertEquals(false, of<Int>().any { it > 0 })
        Assertions.assertEquals(true, of(1).any { it > 0 })
        Assertions.assertEquals(false, of(0).any { it > 0 })
        Assertions.assertEquals(false, of(-1).any { it > 0 })
        Assertions.assertEquals(true, of(-1, 0, 1).any { it > 0 })
        Assertions.assertEquals(true, of(1, 2, 3, 4, 5, 6).any { it > 0 })
    }

    @Test
    fun asIterable() {
        @Suppress("UNUSED_PARAMETER")
        fun <T> isA(l: List<T>) = 1

        @Suppress("UNUSED_PARAMETER")
        fun <T> isA(l: Iterable<T>) = 2
        Assertions.assertEquals(1, isA(of<Int>()))
        Assertions.assertEquals(2, isA(of<Int>().asIterable()))
        Assertions.assertEquals(1, isA(of(1, 2, 3)))
        Assertions.assertEquals(2, isA(of(1, 2, 3).asIterable()))
    }

    @Test
    fun asSequence() {
        Assertions.assertFalse(of<Int>() is Sequence<*>)
        Assertions.assertFalse(of(1, 2, 3) is Sequence<*>)
        Assertions.assertInstanceOf(Sequence::class.java, of<Int>().asSequence())
        Assertions.assertInstanceOf(Sequence::class.java, of(1, 2, 3).asSequence())
    }

    @Test
    fun chunked() {
        Assertions.assertEquals(plOf<PersistentList<Int>>(), of<Int>().chunked(2))

        val seq = PersistentList.of(1, 2, 3, 4)
        Assertions.assertEquals(plOf(of(1, 2), of(3, 4)), seq.chunked(2))
        Assertions.assertEquals(plOf(of(1, 2, 3), of(4)), seq.chunked(3))
        Assertions.assertEquals(plOf(3), seq.windowed(3, step = 3, transform = { it.size }))
    }

    @Test
    fun cleared() {
        Assertions.assertEquals(plOf<Int>(), of<Int>().cleared())
        Assertions.assertEquals(plOf<Int>(), of(0).cleared())
        Assertions.assertEquals(plOf<Int>(), of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).cleared())
    }

    @Test
    fun contains() {
        Assertions.assertEquals(false, of<Int>().contains(1))
        Assertions.assertEquals(false, of(0).contains(1))
        Assertions.assertEquals(true, of(1).contains(1))
        Assertions.assertEquals(true, of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).contains(1))
    }

    @Test
    fun containsAll() {
        Assertions.assertEquals(false, of<Int>().containsAll(setOf(1)))
        Assertions.assertEquals(false, of(0).containsAll(setOf(1)))
        Assertions.assertEquals(false, of(1).containsAll(setOf(1, 2)))
        Assertions.assertEquals(true, of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).containsAll(setOf(1, 2)))
        Assertions.assertEquals(true, of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).containsAll(setOf(0, 1, 2)))
        Assertions.assertEquals(false, of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).containsAll(setOf(-10, 1, 2)))
    }

    @Test
    fun count() {
        Assertions.assertEquals(0, of<Int>().count())
        Assertions.assertEquals(1, of(0).count())
        Assertions.assertEquals(10, of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).count())

        Assertions.assertEquals(0, of<Int>().count { it == 0 })
        Assertions.assertEquals(1, of(0).count { it == 0 })
        Assertions.assertEquals(9, of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).count { it > 0 })
        Assertions.assertEquals(1, of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).count { it == 0 })
    }

    @Test
    fun cycle() {
        Assertions.assertTrue(of<Int>().cycle().isEmpty())
        Assertions.assertFalse(of(1, 2, 3).cycle().isEmpty())
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3, 1), of(1, 2, 3).cycle().take(7))
    }

    @Test
    fun distinct() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().distinct())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).distinct())
        Assertions.assertEquals(plOf<Int>(), of<Int>().distinct())
        Assertions.assertEquals(plOf(0), of(0).distinct())
        Assertions.assertEquals(plOf(0), of(0, 0, 0).distinct())
        Assertions.assertEquals(plOf(0, 1, 2), of(0, 1, 2).distinct())
        Assertions.assertEquals(plOf(0, 1, 2), of(0, 0, 1, 1, 2, 2).distinct())
        Assertions.assertEquals(plOf(0, 1, 2), of(0, 1, 2, 0, 1, 2).distinct())
    }

    @Test
    fun drop() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().drop(2))
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).drop(2))
        Assertions.assertThrows(IllegalArgumentException::class.java) { of<Int>().drop(-1) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { of(1, 2, 3).drop(-1) }
        Assertions.assertEquals(plOf<Int>(), of<Int>().drop(0))
        Assertions.assertEquals(plOf<Int>(), of<Int>().drop(2))
        Assertions.assertEquals(plOf(0), of(0).drop(0))
        Assertions.assertEquals(plOf<Int>(), of(0).drop(1))
        Assertions.assertEquals(plOf<Int>(), of(0).drop(2))
        Assertions.assertEquals(plOf(0, 1, 2), of(0, 1, 2).drop(0))
        Assertions.assertEquals(plOf(1, 2), of(0, 1, 2).drop(1))
        Assertions.assertEquals(plOf(2), of(0, 1, 2).drop(2))
        Assertions.assertEquals(plOf<Int>(), of(0, 1, 2).drop(3))
        Assertions.assertEquals(plOf(0, 1, 2), of(0, 1, 2, 0, 1, 2).drop(3))
    }

    @Test
    fun dropWhile() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().dropWhile { it > 0 })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).dropWhile { it > 0 })
        Assertions.assertEquals(plOf<Int>(), of<Int>().dropWhile { it > 0 })
        Assertions.assertEquals(plOf(0), of(0).dropWhile { it > 0 })
        Assertions.assertEquals(plOf<Int>(), of(0).dropWhile { it == 0 })
        Assertions.assertEquals(plOf(0, 1, 2, 3, 4, 5, 6), of(0, 1, 2, 3, 4, 5, 6).dropWhile { it > 0 })
        Assertions.assertEquals(plOf<Int>(), of(0, 1, 2, 3, 4, 5, 6).dropWhile { it >= 0 })
    }

    @Test
    fun filter() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filter { true })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filter { false })
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(1, 2, 3, 1, 2, 3).filter { true })
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3, 1, 2, 3).filter { false })
        Assertions.assertEquals(plOf(1, 3, 1, 3), of(1, 2, 3, 1, 2, 3).filter { it % 2 == 1 })
    }

    @Test
    fun filterIndexed() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filterIndexed { _, _ -> true })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filterIndexed { _, _ -> false })
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(1, 2, 3, 1, 2, 3).filterIndexed { _, _ -> true })
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3, 1, 2, 3).filterIndexed { _, _ -> false })
        Assertions.assertEquals(plOf(1, 3, 1, 3), of(1, 2, 3, 1, 2, 3).filterIndexed { _, e -> e % 2 == 1 })
        Assertions.assertEquals(true, of(1, 2, 3, 1, 2, 3).filterIndexed { _, e -> e % 2 == 1 }.isLazyType())
    }

    @Test
    fun filterNot() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filterNot { true })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filterNot { false })
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3, 1, 2, 3).filterNot { true })
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(1, 2, 3, 1, 2, 3).filterNot { false })
        Assertions.assertEquals(plOf(2, 2), of(1, 2, 3, 1, 2, 3).filterNot { it % 2 == 1 })
        Assertions.assertEquals(true, of(1, 2, 3, 1, 2, 3).filterNot { it % 2 == 1 }.isLazyType())
    }

    @Test
    fun filterNotNull() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filterNotNull())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, null, 2, null).filterNotNull())
        Assertions.assertEquals(plOf(1, 2), of(1, null, 2, null).filterNotNull())
        Assertions.assertEquals(plOf<Int>(), of<Int?>(null, null, null).filterNotNull())
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(1, 2, 3, 1, 2, 3).filterNotNull())
    }

    @Test
    fun filterv() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filterv { true })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filterv { false })
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(1, 2, 3, 1, 2, 3).filterv { true })
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3, 1, 2, 3).filterv { false })
        Assertions.assertEquals(plOf(1, 3, 1, 3), of(1, 2, 3, 1, 2, 3).filterv { it % 2 == 1 })
        Assertions.assertEquals(false, of(1, 2, 3, 1, 2, 3).filterv { it % 2 == 1 }.isLazyType())
    }

    @Test
    fun filtervNot() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filtervNot { true })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 1, 2, 3).filtervNot { false })
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3, 1, 2, 3).filtervNot { true })
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(1, 2, 3, 1, 2, 3).filtervNot { false })
        Assertions.assertEquals(plOf(2, 2), of(1, 2, 3, 1, 2, 3).filtervNot { it % 2 == 1 })
        Assertions.assertEquals(false, of(1, 2, 3, 1, 2, 3).filtervNot { it % 2 == 1 }.isLazyType())
    }

    @Test
    fun flatMap() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().flatMap { listOf(it) })
        Assertions.assertEquals(plOf<Int>(), of<Int>().flatMap { listOf(it) })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).flatMap { listOf(it) })
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3).flatMap { listOf(it) })
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(1, 2), listOf(3)).flatMap { it })
    }

    @Test
    fun flatMapIndexed() {
        Assertions.assertEquals(plOf<Int>(), of<Int>().flatMapIndexed { _, it -> listOf(it) })
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3).flatMapIndexed { _, it -> listOf(it) })
        Assertions.assertEquals(plOf(1, 3, 5), of(1, 2, 3).flatMapIndexed { i, it -> listOf(i + it) })
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(1, 2), listOf(3)).flatMapIndexed { _, it -> it })
    }

    @Test
    fun fold() {
        Assertions.assertEquals(0, of<Int>().fold(0, Int::plus))
        Assertions.assertEquals(15, of(1, 2, 3, 4, 5).fold(0, Int::plus))
        Assertions.assertEquals(listOf(1, 2), of(1, 2).fold(listOf(), List<Int>::plus))
        Assertions.assertEquals(listOf(0, 1, 2), of(1, 2).fold(listOf(0), List<Int>::plus))
    }

    @Test
    fun foldIndexed() {
        Assertions.assertEquals(0, of<Int>().foldIndexed(0) { i, acc, e -> acc + e + i })
        Assertions.assertEquals(25, of(1, 2, 3, 4, 5).foldIndexed(0) { i, acc, e -> acc + e + i })
        Assertions.assertEquals(
            listOf(1, 2),
            of(listOf(1), listOf(2)).foldIndexed(listOf<Int>()) { _, acc, e -> acc + e })
        Assertions.assertEquals(
            listOf(0, 1, 2),
            of(listOf(1), listOf(2)).foldIndexed(listOf(0)) { _, acc, e -> acc + e })
    }

    @Test
    fun foldRight() {
        Assertions.assertEquals(0, of<Int>().foldRight(0, Int::plus))
        Assertions.assertEquals(15, of(1, 2, 3, 4, 5).foldRight(0, Int::plus))
        Assertions.assertEquals(listOf(1, 2), of(listOf(1), listOf(2)).foldRight(listOf(), List<Int>::plus))
        Assertions.assertEquals(listOf(1, 2, 0), of(listOf(1), listOf(2)).foldRight(listOf(0), List<Int>::plus))
    }

    @Test
    fun foldRightIndexed() {
        Assertions.assertEquals(0, of<Int>().foldRightIndexed(0) { i, acc, e -> acc + e + i })
        Assertions.assertEquals(25, of(1, 2, 3, 4, 5).foldRightIndexed(0) { i, acc, e -> acc + e + i })
        Assertions.assertEquals(
            listOf(1, 2),
            of(listOf(1), listOf(2)).foldRightIndexed(listOf<Int>()) { _, acc, e -> acc + e })
        Assertions.assertEquals(
            listOf(1, 2, 0),
            of(listOf(1), listOf(2)).foldRightIndexed(listOf(0)) { _, acc, e -> acc + e })
    }

    @Test
    fun get() {
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { of<Int>()[-1] }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { of<Int>()[0] }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { of<Int>()[1] }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { of(1, 2, 3)[3] }
        Assertions.assertEquals(1, of(1, 2, 3)[0])
        Assertions.assertEquals(2, of(1, 2, 3)[1])
    }

    @Test
    fun ifEmpty() {
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3).ifEmpty { of(4, 5, 6) })
        Assertions.assertEquals(plOf(4, 5, 6), of<Int>().ifEmpty { of(4, 5, 6) })
    }

    @Test
    fun indexOf() {
        Assertions.assertEquals(-1, of<Int>().indexOf(0))
        Assertions.assertEquals(-1, of(3, 1, 2, 3, 1).indexOf(0))
        Assertions.assertEquals(1, of(3, 1, 2, 3, 1).indexOf(1))
        Assertions.assertEquals(2, of(3, 1, 2, 3, 1).indexOf(2))
        Assertions.assertEquals(0, of(3, 1, 2, 3, 1).indexOf(3))
    }

    @Test
    fun isEmpty() {
        Assertions.assertEquals(true, of<Int>().isEmpty())
        Assertions.assertEquals(false, of(1).isEmpty())
        Assertions.assertEquals(false, of<Int>().isNotEmpty())
        Assertions.assertEquals(true, of(1).isNotEmpty())
    }

    @Test
    fun isLazyType() {
        Assertions.assertEquals(true, of<Int>().isLazyType())
    }

    @Test
    fun isSingleton() {
        Assertions.assertEquals(false, of<Int>().isSingleton())
        Assertions.assertEquals(true, of(1).isSingleton())
        Assertions.assertEquals(false, of(1, 2).isSingleton())
    }

    @Test
    fun iterator() {
        run {
            var counter = 0
            for (e in VList.of<Int>()) counter++
            Assertions.assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in VList.from(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))) counter++
            Assertions.assertEquals(10, counter)
        }
        run {
            var counter = 0
            VList.from(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).forEach { _ -> counter++ }
            Assertions.assertEquals(10, counter)
        }

        run {
            val iterator = VList.of(1, 2, 3, 4, 5).iterator()
            var sum = 0
            while (iterator.hasNext()) sum += iterator.next()
            Assertions.assertEquals(15, sum)
        }
    }

    @Test
    fun lastIndexOf() {
        Assertions.assertEquals(-1, of<Int>().lastIndexOf(0))
        Assertions.assertEquals(-1, of(3, 1, 2, 3, 1).lastIndexOf(0))
        Assertions.assertEquals(4, of(3, 1, 2, 3, 1).lastIndexOf(1))
        Assertions.assertEquals(2, of(3, 1, 2, 3, 1).lastIndexOf(2))
        Assertions.assertEquals(3, of(3, 1, 2, 3, 1).lastIndexOf(3))
    }

    @Test
    fun listIterator() {
        run {
            var counter = 0
            for (e in of<Int>().listIterator()) counter++
            Assertions.assertEquals(0, counter)
        }
        run {
            var counter = 0
            for (e in from(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))) counter++
            Assertions.assertEquals(10, counter)
        }
        run {
            var counter = 0
            from(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).forEach { _ -> counter++ }
            Assertions.assertEquals(10, counter)
        }
        run {
            val iterator = of(1, 2, 3, 4, 5).listIterator()
            var sum = 0
            while (iterator.hasNext()) sum += iterator.next()
            Assertions.assertEquals(15, sum)
        }
        run {
            val iterator = of(1, 2, 3, 4, 5).listIterator(0)
            var sum = 0
            while (iterator.hasNext()) sum += iterator.next()
            Assertions.assertEquals(15, sum)
        }
        run {
            val iterator = of(0, 1, 2, 3, 4, 5).listIterator(1)
            var sum = 0
            while (iterator.hasNext()) sum += iterator.next()
            Assertions.assertEquals(15, sum)
        }
    }

    @Test
    fun map() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().map { it })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).map { it })
        Assertions.assertEquals(true, of(1).map { it }.isLazyType())
        Assertions.assertEquals(plOf<Int>(), of<Int>().map { it })
        Assertions.assertEquals(plOf(1), of(1).map { it })
        Assertions.assertEquals(plOf(1, 2), of(1, 2).map { it })
        Assertions.assertEquals(plOf(2, 3), of(1, 2).map { it + 1 })
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3).map { it })
        Assertions.assertEquals(plOf(2, 3, 4), of(1, 2, 3).map { it + 1 })
    }

    @Test
    fun mapIndexed() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().mapIndexedNotNull { _, elem -> elem })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).mapIndexedNotNull { _, elem -> elem })
        Assertions.assertEquals(plOf<Int>(), of<Int>().mapIndexed { _, it -> it })
        Assertions.assertEquals(plOf(0), of(1).mapIndexed { index, _ -> index })
        Assertions.assertEquals(plOf(1, 2), of(1, 2).mapIndexed { _, it -> it })
        Assertions.assertEquals(plOf(1, 3), of(1, 2).mapIndexed { index, it -> it + index })
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3).mapIndexed { _, it -> it })
        Assertions.assertEquals(plOf(1, 3, 5), of(1, 2, 3).mapIndexed { index, it -> it + index })
    }

    @Test
    fun mapIndexedNotNull() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().mapIndexedNotNull { _, elem -> elem })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).mapIndexedNotNull { _, elem -> elem })
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().mapIndexedNotNull { _, elem -> elem })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).mapIndexedNotNull { _, elem -> elem })
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3, 4).mapIndexedNotNull { _, _ -> null })
        Assertions.assertEquals(
            of(1, 3), of(1, 2, 3, 4).mapIndexedNotNull { _, elem -> if (elem % 2 == 0) null else elem })
    }

    @Test
    fun mapNotNull() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().mapNotNull { it })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).mapNotNull { it })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, null).mapNotNull { it })
        Assertions.assertEquals(true, of(1).mapNotNull { it }.isLazyType())
        Assertions.assertEquals(plOf<Int>(), of<Int>().mapNotNull { it })
        Assertions.assertEquals(plOf(2), of(1, 2, 5).mapNotNull { if (it % 2 == 0) it else null })
        Assertions.assertEquals(plOf(1, 5), of(1, 2, 5).mapNotNull { if (it % 2 != 0) it else null })
        Assertions.assertEquals(plOf<Int>(), of<Int?>(null, null, null, null).mapNotNull { it })
    }

    @Test
    fun mapv() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().mapv { it })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).mapv { it })

        Assertions.assertEquals(false, of(1).mapv { it }.isLazyType())

        Assertions.assertEquals(plOf(1), of(1).mapv { it })
        Assertions.assertEquals(plOf(1, 2), of(1, 2).mapv { it })
        Assertions.assertEquals(plOf(2, 3), of(1, 2).mapv { it + 1 })
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3).mapv { it })
        Assertions.assertEquals(plOf(2, 3, 4), of(1, 2, 3).mapv { it + 1 })
    }

    @Test
    fun minus() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1) - 1)
        Assertions.assertInstanceOf(PersistentList::class.java, of(1) - listOf(1))
        Assertions.assertInstanceOf(PersistentList::class.java, of(1) - setOf(1))

        Assertions.assertEquals(plOf<Int>(), of<Int>() - 1)
        Assertions.assertEquals(plOf<Int>(), of<Int>() - listOf(1))
        Assertions.assertEquals(plOf<Int>(), of<Int>() - setOf(1))

        Assertions.assertEquals(plOf<Int>(), of(1) - 1)
        Assertions.assertEquals(plOf<Int>(), of(1) - listOf(1))
        Assertions.assertEquals(plOf<Int>(), of(1) - setOf(1))

        Assertions.assertEquals(plOf<Int>(), of(1, 1, 1, 1) - 1)
        Assertions.assertEquals(plOf<Int>(), of(1, 1, 1, 1) - listOf(1))
        Assertions.assertEquals(plOf<Int>(), of(1, 1, 1, 1) - setOf(1))

        Assertions.assertEquals(plOf(2, 4), of(1, 2, 3, 4) - 1 - 3)
        Assertions.assertEquals(plOf(2, 4), of(1, 2, 3, 4) - listOf(1, 3))
        Assertions.assertEquals(plOf(2, 4), of(1, 2, 3, 4) - setOf(1, 3))

        Assertions.assertEquals(plOf(1, 2, 3, 4), of(1, 2, 3, 4) - 55)
        Assertions.assertEquals(plOf(1, 2, 3, 4), of(1, 2, 3, 4) - listOf(55))
        Assertions.assertEquals(plOf(1, 2, 3, 4), of(1, 2, 3, 4) - setOf(55))
    }

    @Test
    fun none() {
        Assertions.assertEquals(true, of<Int>().none { it > 0 })
        Assertions.assertEquals(false, of(1).none { it > 0 })
        Assertions.assertEquals(true, of(0).none { it > 0 })
        Assertions.assertEquals(true, of(-1).none { it > 0 })
        Assertions.assertEquals(false, of(-1, 0, 1).none { it > 0 })
        Assertions.assertEquals(false, of(1, 2, 3, 4, 5, 6).none { it > 0 })
    }

    @Test
    fun onEach() {
        Assertions.assertEquals(plOf<Int>(), of<Int>().onEach { throw IllegalStateException() })
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().onEach {})
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).onEach {})

        var counter = 0
        of(0, 1, 2, 3).onEach { counter++ }
        Assertions.assertEquals(4, counter)
    }

    @Test
    fun onEachIndexed() {
        Assertions.assertEquals(plOf<Int>(), of<Int>().onEachIndexed { _, _ -> throw IllegalStateException() })

        var counter = 0
        of(0, 1, 2, 3).onEachIndexed { i, _ -> counter += i }
        Assertions.assertEquals(6, counter)
    }

    @Test
    fun plus() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>() + 1)
        Assertions.assertInstanceOf(PersistentList::class.java, of(1) + 2)
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>() + listOf(1))
        Assertions.assertInstanceOf(PersistentList::class.java, of(1) + listOf(2, 3))

        Assertions.assertEquals(plOf(1), of<Int>() + 1)
        Assertions.assertEquals(plOf(1, 2), of(1) + 2)

        Assertions.assertEquals(plOf<Int>(), of<Int>() + listOf())
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3) + listOf())

        Assertions.assertEquals(plOf(1), of<Int>() + listOf(1))
        Assertions.assertEquals(plOf(1, 2, 3), of(1) + listOf(2, 3))

        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of<Int>() + (1..5))
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of<Int>() + (1..<6))
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(1) + (2..5))
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(1) + sequenceOf(2, 3, 4, 5))

        Assertions.assertEquals(plOf<Int>(), of<Int>() + of<Int>())
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(1) + of(2, 3, 4, 5))
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(1) + listOf(2, 3, 4, 5))
    }

    @Test
    fun requireNoNulls() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().requireNoNulls())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).requireNoNulls())
        Assertions.assertThrows(IllegalArgumentException::class.java) { of(1, null, 2).requireNoNulls() }
        Assertions.assertThrows(IllegalArgumentException::class.java) { of<Int?>(null).requireNoNulls() }
        Assertions.assertEquals(plOf<Int>(), of<Int>().requireNoNulls())
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3).requireNoNulls())
    }

    @Test
    fun reversed() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().reversed())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).reversed())
        Assertions.assertEquals(plOf<Int>(), of<Int>().reversed())
        Assertions.assertEquals(plOf(3, 2, 1), of(1, 2, 3).reversed())
    }

    @Test
    fun runningFold() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).runningFold(0) { acc, it -> acc + it })
        Assertions.assertEquals(plOf(0, 0, 0, 0), of(1, 2, 3).runningFold(0) { acc, _ -> acc })
        Assertions.assertEquals(plOf(0, 1, 3, 6), of(1, 2, 3).runningFold(0) { acc, it -> acc + it })
        Assertions.assertEquals(plOf("", "a", "ab"), of("a", "b").runningFold("") { acc, it -> acc + it })
    }

    @Test
    fun runningFoldIndexed() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(1, 2, 3).runningFoldIndexed(0) { _, acc, it -> acc + it })
        Assertions.assertEquals(plOf(0, 0, 0, 0), of(1, 2, 3).runningFoldIndexed(0) { _, acc, _ -> acc })
        Assertions.assertEquals(plOf(0, 1, 3, 6), of(1, 2, 3).runningFoldIndexed(0) { _, acc, it -> acc + it })
        Assertions.assertEquals(plOf("", "a", "ab"), of("a", "b").runningFoldIndexed("") { _, acc, it -> acc + it })
    }

    @Test
    fun runningReduce() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).runningReduce { acc, it -> acc + it })
        Assertions.assertEquals(plOf(1, 1, 1), of(1, 2, 3).runningReduce { acc, _ -> acc })
        Assertions.assertEquals(plOf(1, 3, 6), of(1, 2, 3).runningReduce { acc, it -> acc + it })
        Assertions.assertEquals(plOf("a", "ab"), of("a", "b").runningReduce { acc, it -> acc + it })
    }

    @Test
    fun runningReduceIndexed() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(1, 2, 3).runningReduceIndexed { _, acc, it -> acc + it })
        Assertions.assertEquals(plOf(1, 1, 1), of(1, 2, 3).runningReduceIndexed { _, acc, _ -> acc })
        Assertions.assertEquals(plOf(1, 3, 6), of(1, 2, 3).runningReduceIndexed { _, acc, it -> acc + it })
        Assertions.assertEquals(plOf("a", "ab"), of("a", "b").runningReduceIndexed { _, acc, it -> acc + it })
    }

    @Test
    fun scan() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).scan(0) { acc, it -> acc + it })
        Assertions.assertEquals(plOf("d", "d", "d", "d"), of("a", "b", "c").scan("d") { acc, _ -> acc })
        Assertions.assertEquals(plOf("d", "da", "dab", "dabc"), of("a", "b", "c").scan("d") { acc, it -> acc + it })
    }

    @Test
    fun scanIndexed() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).scanIndexed(0) { _, acc, it -> acc + it })
        Assertions.assertEquals(plOf("d", "d", "d", "d"), of("a", "b", "c").scanIndexed("d") { _, acc, _ -> acc })
        Assertions.assertEquals(
            plOf("d", "da", "dab", "dabc"),
            of("a", "b", "c").scanIndexed("d") { _, acc, it -> acc + it })
    }

    @Test
    fun shuffled() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().shuffled())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).shuffled())
    }

    @Test
    fun sortedBy() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().sortedBy { it })
        Assertions.assertInstanceOf(PersistentList::class.java, of(3, 2, 1).sortedBy { it })
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3).sortedBy { it })
        Assertions.assertEquals(plOf(1, 2, 3), of(3, 2, 1).sortedBy { it })
    }

    @Test
    fun sortedByDescending() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().sortedByDescending { it })
        Assertions.assertInstanceOf(PersistentList::class.java, of(3, 2, 1).sortedByDescending { it })
        Assertions.assertEquals(plOf(3, 2, 1), of(1, 2, 3).sortedByDescending { it })
        Assertions.assertEquals(plOf(3, 2, 1), of(3, 2, 1).sortedByDescending { it })
    }

    @Test
    fun sortedWith() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().sortedWith { a, b -> a.compareTo(b) })
        Assertions.assertInstanceOf(PersistentList::class.java, of(3, 2, 1).sortedWith { a, b -> a.compareTo(b) })
        Assertions.assertEquals(plOf(1, 2, 3), of(1, 2, 3).sortedWith { a, b -> a.compareTo(b) })
        Assertions.assertEquals(plOf(1, 2, 3), of(3, 2, 1).sortedWith { a, b -> a.compareTo(b) })
    }

    @Test
    fun splitAt() {
        Assertions.assertInstanceOf(Pair::class.java, of<Int>().splitAt(3))
        Assertions.assertInstanceOf(Pair::class.java, of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).splitAt(3))
        Assertions.assertEquals(plOf(1) to of<Int>(), of(1).splitAt(3))
        Assertions.assertEquals(plOf(1, 2) to of(3, 4, 5), of(1, 2, 3, 4, 5).splitAt(2))
    }

    @Test
    fun subList() {
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { of<Int>().subList(3, 6).size }
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6))
        Assertions.assertEquals(listOf<Int>(), of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(0, 0))
        Assertions.assertEquals(listOf(4, 5, 6), of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6))
        Assertions.assertEquals(3, of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(3, 6).size)
    }

    @Test
    fun take() {
        Assertions.assertEquals(of<Int>(), of<Int>().take(1))
        Assertions.assertEquals(of<Int>(), of(1, 2).take(0))
        Assertions.assertEquals(of(1), of(1, 2).take(1))
        Assertions.assertEquals(of(1, 2), of(1, 2).take(5))
        Assertions.assertEquals(of(0, 1, 2, 3, 4), of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).take(5))
    }

    @Test
    fun takeWhile() {
        Assertions.assertEquals(of<Int>(), of<Int>().takeWhile { true })
        Assertions.assertEquals(of<Int>(), of(1, 2).takeWhile { false })
        Assertions.assertEquals(of(1, 2), of(1, 2).takeWhile { true })
        Assertions.assertEquals(of(1), of(1, 2, 1).takeWhile { it < 2 })
        Assertions.assertEquals(of(1, 2), of(1, 2).takeWhile { true })
        Assertions.assertEquals(of(0, 1, 2, 3, 4), of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).takeWhile { it < 5 })
    }

    @Test
    fun toList() {
        Assertions.assertInstanceOf(List::class.java, of<Int>().toList())
        Assertions.assertInstanceOf(List::class.java, of(1, 2, 3).toList())
        Assertions.assertEquals(listOf<Int>(), of<Int>().toList())
        Assertions.assertEquals(listOf(3, 2, 1), of(3, 2, 1).toList())
    }

    @Test
    fun toMutableList() {
        Assertions.assertInstanceOf(MutableList::class.java, of<Int>().toMutableList())
        Assertions.assertInstanceOf(MutableList::class.java, of(1, 2, 3).toMutableList())
        Assertions.assertEquals(mutableListOf(1, 2, 3), of(1, 2, 3).toMutableList())
        Assertions.assertEquals(mutableListOf(3, 2, 1), of(3, 2, 1).toMutableList())
    }

    @Test
    fun windowed() {
        Assertions.assertEquals(plOf<PersistentList<Int>>(), of<Int>().windowed(2))
        Assertions.assertEquals(plOf<PersistentList<Int>>(), of<Int>().windowed(2, partialWindows = true))

        val seq = PersistentList.of(1, 2, 3, 4)
        Assertions.assertEquals(plOf(of(1, 2), of(2, 3), of(3, 4)), seq.windowed(2))
        Assertions.assertEquals(plOf(of(1, 2), of(3, 4)), seq.windowed(2, step = 2))
        Assertions.assertEquals(plOf(of(1, 2, 3)), seq.windowed(3, step = 3))
        Assertions.assertEquals(plOf(of(1, 2, 3), of(4)), seq.windowed(3, step = 3, partialWindows = true))
        Assertions.assertEquals(plOf(3), seq.windowed(3, step = 3, transform = { it.size }))
        Assertions.assertEquals(plOf(3, 1), seq.windowed(3, step = 3, partialWindows = true, transform = { it.size }))
    }

    @Test
    fun withIndex() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().withIndex())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).withIndex())
        Assertions.assertEquals(plOf<IndexedValue<Int>>(), of<Int>().withIndex())
        Assertions.assertEquals(plOf(IndexedValue(0, 2), IndexedValue(1, 4)), of(2, 4).withIndex())
    }

    @Test
    fun zip() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().zip(of<Int>()))
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().zip(of(4, 5, 6)))
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).zip(of<Int>()))
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).zip(of(4, 5, 6)))

        Assertions.assertEquals(plOf<Pair<Int, Int>>(), of<Int>().zip(of<Int>()))
        Assertions.assertEquals(plOf<Pair<Int, Int>>(), of<Int>().zip(of(4, 5, 6)))
        Assertions.assertEquals(plOf<Pair<Int, Int>>(), of(1, 2, 3).zip(of<Int>()))

        Assertions.assertEquals(plOf(1 to 4, 2 to 5), of(1, 2).zip(of(4, 5, 6)))
        Assertions.assertEquals(plOf(1 to 4, 2 to 5), of(1, 2, 3).zip(of(4, 5)))
        Assertions.assertEquals(plOf(1 to 4, 2 to 5, 3 to 6), of(1, 2, 3).zip(of(4, 5, 6)))

        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().zip(of<Int>()) { a, b -> a + b })
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().zip(of(4, 5, 6)) { a, b -> a + b })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).zip(of<Int>()) { a, b -> a + b })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1, 2, 3).zip(of(4, 5, 6)) { a, b -> a + b })

        Assertions.assertEquals(plOf<Int>(), of<Int>().zip(of<Int>()) { a, b -> a + b })
        Assertions.assertEquals(plOf<Int>(), of<Int>().zip(of(4, 5, 6)) { a, b -> a + b })
        Assertions.assertEquals(plOf<Int>(), of(1, 2, 3).zip(of<Int>()) { a, b -> a + b })

        Assertions.assertEquals(plOf(5, 7), of(1, 2).zip(of(4, 5, 6)) { a, b -> a + b })
        Assertions.assertEquals(plOf(5, 7), of(1, 2, 3).zip(of(4, 5)) { a, b -> a + b })
        Assertions.assertEquals(plOf(5, 7, 9), of(1, 2, 3).zip(of(4, 5, 6)) { a, b -> a + b })
    }

    @Test
    fun zipWithNext() {
        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().zipWithNext())
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).zipWithNext())
        Assertions.assertEquals(plOf<Pair<Int, Int>>(), of<Int>().zipWithNext())
        Assertions.assertEquals(plOf<Pair<Int, Int>>(), of(1).zipWithNext())
        Assertions.assertEquals(plOf(1 to 2, 2 to 3), of(1, 2, 3).zipWithNext())

        Assertions.assertInstanceOf(PersistentList::class.java, of<Int>().zipWithNext { a, b -> a + b })
        Assertions.assertInstanceOf(PersistentList::class.java, of(1).zipWithNext { a, b -> a + b })
        Assertions.assertEquals(plOf<Int>(), of<Int>().zipWithNext { a, b -> a + b })
        Assertions.assertEquals(plOf<Int>(), of(1).zipWithNext { a, b -> a + b })
        Assertions.assertEquals(plOf(3, 5), of(1, 2, 3).zipWithNext { a, b -> a + b })
    }
}