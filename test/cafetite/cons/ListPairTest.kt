package cafetite.cons

import kotlin.random.Random
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ListPairTest {
    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T> of(left: List<T>, right: List<T>) =
        ListPair.concat(PersistentList.from(left), PersistentList.from(right))

    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T> plOf(vararg elems: T) = PersistentList.of(*elems)

    @Test
    fun testConstructorEmpty() {
        Assertions.assertTrue(ListPair.concat(nullCons<Int>(), nullCons()).isEmpty())
        Assertions.assertEquals(nullCons<Int>(), ListPair.concat(nullCons<Int>(), nullCons()))
    }

    @Test
    fun testConstructorOneEmpty() {
        val lst = nullCons<Int>().cons(1).cons(2).cons(3)
        Assertions.assertSame(lst, ListPair.concat(lst, nullCons()))
        Assertions.assertSame(lst, ListPair.concat(nullCons(), lst))
        Assertions.assertSame(lst, ListPair.concat(lst, plOf()))
        Assertions.assertSame(lst, ListPair.concat(plOf(), lst))
    }

    @Test
    fun testConstructorNormal() {
        val lst = nullCons<Int>().cons(1).cons(2).cons(3)
        Assertions.assertEquals(lst + lst, ListPair.concat(lst, lst))
    }

    @Test
    fun testToString() {
        Assertions.assertEquals("[1, 2, 3]", of(listOf(1, 2), listOf(3)).toString())
        Assertions.assertEquals("[1, 2, 3, 4]", of(listOf(1, 2), listOf(3, 4)).toString(limit = 4))
        Assertions.assertEquals("[1, 2, 3, ...]", of(listOf(1, 2), listOf(3, 4)).toString(limit = 3))
    }

    @Test
    fun getCar() {
        Assertions.assertEquals(1, of(listOf(1, 2, 3), listOf(4, 5)).car)
    }

    @Test
    fun first() {
        Assertions.assertEquals(1, of(listOf(1, 2, 3), listOf(4, 5)).first())
    }

    @Test
    fun head() {
        Assertions.assertEquals(1, of(listOf(1, 2, 3), listOf(4, 5)).head())
    }

    @Test
    fun getCadr() {
        Assertions.assertEquals(2, of(listOf(1, 2, 3), listOf(4, 5)).cadr)
    }

    @Test
    fun getCaddr() {
        Assertions.assertThrows(NoSuchElementException::class.java) { of(listOf(1), listOf(2)).caddr }
        Assertions.assertEquals(3, of(listOf(1, 2, 3), listOf(4, 5)).caddr)
    }

    @Test
    fun getCadddr() {
        Assertions.assertThrows(NoSuchElementException::class.java) { of(listOf(1), listOf(2)).cadddr }
        Assertions.assertThrows(NoSuchElementException::class.java) { of(listOf(1), listOf(2, 3)).cadddr }
        Assertions.assertEquals(4, of(listOf(1, 2, 3), listOf(4, 5)).cadddr)
        Assertions.assertEquals(4, of(listOf(1, 2, 3, 4), listOf(5)).cadddr)
    }

    @Test
    fun getCdr() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).cdr)
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2, 3)).cdr)
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2)).cdr)
        Assertions.assertEquals(plOf(2), of(listOf(1), listOf(2)).cdr)
        Assertions.assertEquals(plOf(2, 3), of(listOf(1), listOf(2, 3)).cdr)
        Assertions.assertEquals(plOf(2, 3), of(listOf(1, 2), listOf(3)).cdr)
        Assertions.assertEquals(plOf(2, 3, 4), of(listOf(1, 2, 3), listOf(4)).cdr)
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).cdr)
    }

    @Test
    fun butfirst() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).butfirst())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2, 3)).butfirst())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2)).butfirst())
        Assertions.assertEquals(plOf(2), of(listOf(1), listOf(2)).butfirst())
        Assertions.assertEquals(plOf(2, 3), of(listOf(1), listOf(2, 3)).butfirst())
        Assertions.assertEquals(plOf(2, 3), of(listOf(1, 2), listOf(3)).butfirst())
        Assertions.assertEquals(plOf(2, 3, 4), of(listOf(1, 2, 3), listOf(4)).butfirst())
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).butfirst())
    }

    @Test
    fun tail() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).tail())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2, 3)).tail())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2)).tail())
        Assertions.assertEquals(plOf(2), of(listOf(1), listOf(2)).tail())
        Assertions.assertEquals(plOf(2, 3), of(listOf(1), listOf(2, 3)).tail())
        Assertions.assertEquals(plOf(2, 3), of(listOf(1, 2), listOf(3)).tail())
        Assertions.assertEquals(plOf(2, 3, 4), of(listOf(1, 2, 3), listOf(4)).tail())
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).tail())
    }

    @Test
    fun rest() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).rest())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2, 3)).rest())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2)).rest())
        Assertions.assertEquals(plOf(2), of(listOf(1), listOf(2)).rest())
        Assertions.assertEquals(plOf(2, 3), of(listOf(1), listOf(2, 3)).rest())
        Assertions.assertEquals(plOf(2, 3), of(listOf(1, 2), listOf(3)).rest())
        Assertions.assertEquals(plOf(2, 3, 4), of(listOf(1, 2, 3), listOf(4)).rest())
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).rest())
    }

    @Test
    fun next() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).next())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2, 3)).next())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2)).next())
        Assertions.assertEquals(null, of(listOf(1), listOf(2)).next()!!.next())
        Assertions.assertEquals(null, of(listOf(1), listOf(2, 3)).next()!!.next()!!.next())
        Assertions.assertEquals(plOf(2), of(listOf(1), listOf(2)).next())
        Assertions.assertEquals(plOf(2, 3), of(listOf(1), listOf(2, 3)).next())
        Assertions.assertEquals(plOf(2, 3, 4), of(listOf(1, 2), listOf(3, 4)).next())
        Assertions.assertEquals(plOf(2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).next())
    }

    @Test
    fun getCddr() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).cddr)
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2)).cddr)
        Assertions.assertEquals(plOf(3), of(listOf(1), listOf(2, 3)).cddr)
        Assertions.assertEquals(plOf(3, 4), of(listOf(1, 2), listOf(3, 4)).cddr)
        Assertions.assertEquals(plOf(3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).cddr)
    }

    @Test
    fun getCdddr() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).cdddr)
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2)).cdddr)
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2, 3)).cdddr)
        Assertions.assertEquals(plOf(4), of(listOf(1, 2), listOf(3, 4)).cdddr)
        Assertions.assertEquals(plOf(4, 5), of(listOf(1, 2, 3), listOf(4, 5)).cdddr)
    }

    @Test
    fun getCddddr() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).cddddr)
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2)).cddddr)
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2, 3)).cddddr)
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2), listOf(3, 4)).cddddr)
        Assertions.assertEquals(plOf(5), of(listOf(1, 2, 3), listOf(4, 5)).cddddr)
    }

    @Test
    fun cons() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(2), listOf(3)).cons(1))
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(2), listOf(3)).cons(1))
        Assertions.assertEquals(plOf(0, 1, 2, 3), of(listOf(2), listOf(3)).cons(1).cons(0))
    }

    @Test
    fun concat() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            PersistentList.concat(of(listOf(1, 2), listOf(3, 4)), plOf())
        )
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            PersistentList.concat(of(listOf(1, 2), listOf(3, 4)), plOf(5, 6))
        )
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            PersistentList.concat(plOf(), of(listOf(5, 6), listOf(7, 8)))
        )
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            PersistentList.concat(plOf(1, 2), of(listOf(3, 4), listOf(5, 6)))
        )
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            PersistentList.concat(of(listOf(1), listOf(2, 3)), of(listOf(4), listOf(6)))
        )

        Assertions.assertEquals(
            plOf(1, 2, 3, 4),
            PersistentList.concat(of(listOf(1, 2), listOf(3, 4)), plOf())
        )
        Assertions.assertEquals(
            plOf(1, 2, 3, 4, 5, 6),
            PersistentList.concat(of(listOf(1, 2), listOf(3, 4)), plOf(5, 6))
        )
        Assertions.assertEquals(
            plOf(5, 6, 7, 8),
            PersistentList.concat(plOf(), of(listOf(5, 6), listOf(7, 8)))
        )
        Assertions.assertEquals(
            plOf(1, 2, 3, 4, 5, 6),
            PersistentList.concat(plOf(1, 2), of(listOf(3, 4), listOf(5, 6)))
        )
        Assertions.assertEquals(
            plOf(1, 2, 3, 4, 5, 6),
            PersistentList.concat(of(listOf(1), listOf(2, 3)), of(listOf(4, 5), listOf(6)))
        )
    }

    @Test
    fun getSize() {
        Assertions.assertEquals(4, of(listOf(1, 2), listOf(3, 4)).size)
        Assertions.assertEquals(5, of(listOf(1, 2), listOf(3, 4)).cons(1).size)
        Assertions.assertEquals(2, of(listOf(1), listOf(3)).size)
        Assertions.assertEquals(3, of(listOf(1, 2), listOf(3, 4)).cdr.size)
        Assertions.assertEquals(2, of(listOf(1, 2), listOf(3, 4)).cddr.size)
        Assertions.assertEquals(1, of(listOf(1, 2), listOf(3, 4)).cdddr.size)
        Assertions.assertEquals(0, of(listOf(1, 2), listOf(3, 4)).cddddr.size)
    }

    @Test
    fun all() {
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).all { it < 0 })
        Assertions.assertEquals(false, of(listOf(0, 1, 2), listOf(3, 4)).all { it < 0 })
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).all { it > 0 })
        Assertions.assertEquals(false, of(listOf(0, 1, 2), listOf(3, 4)).all { it > 0 })
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).all { it >= 0 })
        Assertions.assertEquals(true, of(listOf(0, 1, 2), listOf(3, 4)).all { it >= 0 })
    }

    @Test
    fun any() {
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).any { it < 0 })
        Assertions.assertEquals(false, of(listOf(0, 1, 2), listOf(3, 4)).any { it < 0 })
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).any { it > 0 })
        Assertions.assertEquals(true, of(listOf(0, 1, 2), listOf(3, 4)).any { it > 0 })
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).any { it >= 0 })
        Assertions.assertEquals(true, of(listOf(0, 1, 2), listOf(3, 4)).any { it >= 0 })
    }

    @Test
    fun asIterable() {
        @Suppress("UNUSED_PARAMETER")
        fun <T> isA(l: List<T>) = 1

        @Suppress("UNUSED_PARAMETER")
        fun <T> isA(l: Iterable<T>) = 2
        Assertions.assertEquals(1, isA(of(listOf(1, 2), listOf(3, 4))))
        Assertions.assertEquals(2, isA(of(listOf(1, 2), listOf(3, 4)).asIterable()))
    }

    @Test
    fun asSequence() {
        Assertions.assertFalse(of(listOf(1, 2), listOf(3, 4)) is Sequence<*>)
        Assertions.assertInstanceOf(Sequence::class.java, of(listOf(1, 2), listOf(3, 4)).asSequence())
    }

    @Test
    fun chunked() {
        val seq = of(listOf(1, 2, 3), listOf(4, 5, 6))
        Assertions.assertEquals(plOf(plOf(1, 2), plOf(3, 4), plOf(5, 6)), seq.chunked(2))
        Assertions.assertEquals(plOf(plOf(1, 2, 3), plOf(4, 5, 6)), seq.chunked(3))
        Assertions.assertEquals(plOf(plOf(1, 2, 3, 4), plOf(5, 6)), seq.chunked(4))
        Assertions.assertEquals(plOf(3, 3), seq.windowed(3, step = 3, transform = { it.size }))
        Assertions.assertEquals(plOf(2, 2, 2), seq.windowed(2, step = 2, transform = { it.size }))
    }

    @Test
    fun cleared() {
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2)).cleared())
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2, 3)).cleared())
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2), listOf(3)).cleared())
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2), listOf(3, 4)).cleared())
    }

    @Test
    fun contains() {
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).contains(0))
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).contains(5))
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).contains(1))
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).contains(2))
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).contains(3))
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).contains(4))
    }

    @Test
    fun containsAll() {
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).containsAll(setOf(0)))
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).containsAll(setOf(5)))
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).containsAll(setOf(1)))
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).containsAll(setOf(0, 2)))
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).containsAll(setOf()))
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).containsAll(setOf(1, 2, 3, 4)))
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).containsAll(setOf(1, 2, 3, 4, 5)))
    }

    @Test
    fun count() {
        Assertions.assertEquals(10, of(listOf(0, 1, 2, 3), listOf(4, 5, 6, 7, 8, 9)).count())
        Assertions.assertEquals(9, of(listOf(0, 1, 2, 3), listOf(4, 5, 6, 7, 8, 9)).count { it > 0 })
        Assertions.assertEquals(1, of(listOf(0, 1, 2, 3), listOf(4, 5, 6, 7, 8, 9)).count { it == 0 })
        Assertions.assertEquals(0, of(listOf(0, 1, 2, 3), listOf(4, 5, 6, 7, 8, 9)).count { it < 0 })
    }

    @Test
    fun cycle() {
        Assertions.assertFalse(of(listOf(1), listOf(2, 3)).cycle().isEmpty())
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3, 1), of(listOf(1), listOf(2, 3)).cycle().take(7))
    }

    @Test
    fun distinct() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(0, 0), listOf(0, 0)).distinct())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(2, 3)).distinct())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(0, 1, 1), listOf(2, 2)).distinct())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(0, 1, 1, 2), listOf(2, 2)).distinct())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(0, 1, 2), listOf(0, 1, 2)).distinct())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(0, 1, 2, 0), listOf(1, 2)).distinct())

        Assertions.assertEquals(plOf(0), of(listOf(0, 0), listOf(0, 0)).distinct())
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(1, 2), listOf(2, 3)).distinct())
        Assertions.assertEquals(plOf(0, 1, 2), of(listOf(0, 1, 1), listOf(2, 2)).distinct())
        Assertions.assertEquals(plOf(0, 1, 2), of(listOf(0, 1, 1, 2), listOf(2, 2)).distinct())
        Assertions.assertEquals(plOf(0, 1, 2), of(listOf(0, 1, 2), listOf(0, 1, 2)).distinct())
        Assertions.assertEquals(plOf(0, 1, 2), of(listOf(0, 1, 2, 0), listOf(1, 2)).distinct())
    }

    @Test
    fun drop() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(3, 4)).drop(0))
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(3, 4)).drop(1))
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(3, 4)).drop(2))
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(3, 4)).drop(3))
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(3, 4)).drop(4))

        Assertions.assertThrows(IllegalArgumentException::class.java) { of(listOf(1, 2), listOf(3, 4)).drop(-1) }

        Assertions.assertEquals(plOf(1, 2, 3, 4), of(listOf(1, 2), listOf(3, 4)).drop(0))
        Assertions.assertEquals(plOf(2, 3, 4), of(listOf(1, 2), listOf(3, 4)).drop(1))
        Assertions.assertEquals(plOf(3, 4), of(listOf(1, 2), listOf(3, 4)).drop(2))
        Assertions.assertEquals(plOf(4), of(listOf(1, 2), listOf(3, 4)).drop(3))
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2), listOf(3, 4)).drop(4))
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2), listOf(3, 4)).drop(5))
    }

    @Test
    fun dropWhile() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3), listOf(4, 6, 7, 8)).dropWhile { it > 0 })
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3), listOf(4, 6, 7, 8)).dropWhile { it < 0 })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2, 3), listOf(4, 6, 7, 8)).dropWhile { it > 0 })
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(listOf(0, 1, 2, 3), listOf(4, 5)).dropWhile { it == 0 })
        Assertions.assertEquals(plOf(0, 1, 2, 3, 4, 5, 6), of(listOf(0, 1, 2, 3), listOf(4, 5, 6)).dropWhile { it > 0 })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2, 3), listOf(4, 6, 7, 8)).dropWhile { it >= 0 })
    }

    @Test
    fun filter() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(1, 2, 3)).filter { true })
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(1, 2, 3)).filter { false })
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(listOf(1, 2, 3), listOf(1, 2, 3)).filter { true })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2, 3), listOf(1, 2, 3)).filter { false })
        Assertions.assertEquals(plOf(1, 3, 1, 3), of(listOf(1, 2, 3), listOf(1, 2, 3)).filter { it % 2 == 1 })
    }

    @Test
    fun filterIndexed() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3), listOf(1, 2, 3)).filterIndexed { _, _ -> true })
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3), listOf(1, 2, 3)).filterIndexed { _, _ -> false })
        Assertions.assertEquals(
            plOf(1, 2, 3, 1, 2, 3),
            of(listOf(1, 2, 3), listOf(1, 2, 3)).filterIndexed { _, _ -> true })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2, 3), listOf(1, 2, 3)).filterIndexed { _, _ -> false })
        Assertions.assertEquals(
            plOf(1, 3, 1, 3),
            of(listOf(1, 2, 3), listOf(1, 2, 3)).filterIndexed { _, e -> e % 2 == 1 })
        Assertions.assertEquals(
            true,
            of(listOf(1, 2, 3), listOf(1, 2, 3)).filterIndexed { _, e -> e % 2 == 1 }.isLazyType()
        )
    }

    @Test
    fun filterNot() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(1, 2, 3)).filterNot { true })
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3), listOf(1, 2, 3)).filterNot { false })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2, 3), listOf(1, 2, 3)).filterNot { true })
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(listOf(1, 2, 3), listOf(1, 2, 3)).filterNot { false })
        Assertions.assertEquals(plOf(2, 2), of(listOf(1, 2, 3), listOf(1, 2, 3)).filterNot { it % 2 == 1 })
        Assertions.assertEquals(true, of(listOf(1, 2, 3), listOf(1, 2, 3)).filterNot { it % 2 == 1 }.isLazyType())
    }

    @Test
    fun filterNotNull() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(1, 2, 3)).filterNotNull())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, null), listOf(2, null)).filterNotNull())
        Assertions.assertEquals(plOf(1, 2), of(listOf(1, null), listOf(2, null)).filterNotNull())
        Assertions.assertEquals(plOf<Int>(), of<Int?>(listOf(null), listOf(null, null)).filterNotNull())
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(listOf(1, 2, 3), listOf(1, 2, 3)).filterNotNull())
    }

    @Test
    fun filterv() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(1, 2, 3)).filterv { true })
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(1, 2, 3)).filterv { false })
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(listOf(1, 2, 3), listOf(1, 2, 3)).filterv { true })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2, 3), listOf(1, 2, 3)).filterv { false })
        Assertions.assertEquals(plOf(1, 3, 1, 3), of(listOf(1, 2, 3), listOf(1, 2, 3)).filterv { it % 2 == 1 })
        Assertions.assertEquals(false, of(listOf(1, 2, 3), listOf(1, 2, 3)).filterv { it % 2 == 1 }.isLazyType())
    }

    @Test
    fun filtervNot() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3), listOf(1, 2, 3)).filtervNot { true })
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3), listOf(1, 2, 3)).filtervNot { false })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2, 3), listOf(1, 2, 3)).filtervNot { true })
        Assertions.assertEquals(plOf(1, 2, 3, 1, 2, 3), of(listOf(1, 2, 3), listOf(1, 2, 3)).filtervNot { false })
        Assertions.assertEquals(plOf(2, 2), of(listOf(1, 2, 3), listOf(1, 2, 3)).filtervNot { it % 2 == 1 })
        Assertions.assertEquals(false, of(listOf(1, 2, 3), listOf(1, 2, 3)).filtervNot { it % 2 == 1 }.isLazyType())
    }

    @Test
    fun flatMap() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2, 3)).flatMap { listOf(it) })
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(1), listOf(2, 3)).flatMap { listOf(it) })

        Assertions.assertEquals(
            plOf(1, 2, 3),
            of(listOf(of(listOf(1), plOf(2))), plOf(plOf(3))).flatMap { it })
    }

    @Test
    fun flatMapIndexed() {
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(1), listOf(2, 3)).flatMapIndexed { _, it -> listOf(it) })
        Assertions.assertEquals(plOf(1, 3, 5), of(listOf(1), listOf(2, 3)).flatMapIndexed { i, it -> listOf(i + it) })

        Assertions.assertEquals(
            plOf(1, 2, 3),
            of(listOf(of(listOf(1), plOf(2))), plOf(plOf(3))).flatMapIndexed { _, it -> it })
    }

    @Test
    fun fold() {
        Assertions.assertEquals(15, of(listOf(1, 2, 3), listOf(4, 5)).fold(0, Int::plus))
        Assertions.assertEquals(listOf(1, 2), of(listOf(1), listOf(2)).fold(listOf(), List<Int>::plus))
        Assertions.assertEquals(listOf(0, 1, 2), of(listOf(1), listOf(2)).fold(listOf(0), List<Int>::plus))
    }

    @Test
    fun foldIndexed() {
        Assertions.assertEquals(25, of(listOf(1, 2, 3), listOf(4, 5)).foldIndexed(0) { i, acc, e -> acc + e + i })
        Assertions.assertEquals(
            listOf(1, 2),
            of(listOf(1), listOf(2)).foldIndexed(listOf<Int>()) { _, acc, e -> acc + e })
        Assertions.assertEquals(
            listOf(0, 1, 2),
            of(listOf(1), listOf(2)).foldIndexed(listOf(0)) { _, acc, e -> acc + e })
    }

    @Test
    fun foldRight() {
        Assertions.assertEquals(15, of(listOf(1, 2, 3), listOf(4, 5)).foldRight(0, Int::plus))
        Assertions.assertEquals(
            listOf(1, 2),
            of(listOf(listOf(1)), listOf(listOf(2))).foldRight(listOf(), List<Int>::plus)
        )
        Assertions.assertEquals(
            listOf(1, 2, 0),
            of(listOf(listOf(1)), listOf(listOf(2))).foldRight(listOf(0), List<Int>::plus)
        )
    }

    @Test
    fun foldRightIndexed() {
        Assertions.assertEquals(25, of(listOf(1, 2, 3), listOf(4, 5)).foldRightIndexed(0) { i, acc, e -> acc + e + i })
        Assertions.assertEquals(
            listOf(1, 2),
            of(listOf(listOf(1)), listOf(listOf(2))).foldRightIndexed(listOf<Int>()) { _, acc, e -> acc + e })
        Assertions.assertEquals(
            listOf(1, 2, 0),
            of(listOf(listOf(1)), listOf(listOf(2))).foldRightIndexed(listOf(0)) { _, acc, e -> acc + e })
    }

    @Test
    fun get() {
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { of(listOf(1, 2), listOf(3, 4, 5, 6, 7))[-1] }
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) { of(listOf(1, 2), listOf(3, 4, 5, 6, 7))[9] }
        Assertions.assertEquals(1, of(listOf(1, 2), listOf(3, 5, 4, 6, 7, 8))[0])
        Assertions.assertEquals(2, of(listOf(1, 2), listOf(3, 5, 4, 6, 7, 8))[1])
        Assertions.assertEquals(8, of(listOf(1, 2), listOf(3, 5, 4, 6, 7, 8))[7])
    }

    @Test
    fun ifEmpty() {
        of(listOf(1, 2, 3), listOf(4, 5, 6))
        Assertions.assertEquals(plOf(1, 2, 3, 4), of(listOf(1, 2), listOf(3, 4)).ifEmpty { plOf(9, 9, 9) })

        val lazyL = of(plOf(1, 2, 3).filter { it > 0 }, plOf(4, 7, 8).filter { it > 0 })
        Assertions.assertEquals(plOf(1, 2, 3, 4, 7, 8), lazyL.ifEmpty { plOf(9, 9, 9) })

        val lazyLEmpty = of(plOf(1, 2, 3).filter { it < 0 }, plOf(4, 7, 8).filter { it < 0 })
        Assertions.assertEquals(plOf(9, 9, 9), lazyLEmpty.ifEmpty { plOf(9, 9, 9) })
    }

    @Test
    fun indexOf() {
        Assertions.assertEquals(-1, of(listOf(3, 1), listOf(2, 3, 1)).indexOf(0))
        Assertions.assertEquals(1, of(listOf(3, 1), listOf(2, 3, 1)).indexOf(1))
        Assertions.assertEquals(2, of(listOf(3, 1), listOf(2, 3, 1)).indexOf(2))
        Assertions.assertEquals(0, of(listOf(3, 1), listOf(2, 3, 1)).indexOf(3))
    }

    @Test
    fun isEmpty() {
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).isEmpty())
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).isNotEmpty())

        val lazyL = of(plOf(1, 2, 3).filter { it > 0 }, plOf(4, 7, 8).filter { it > 0 })
        Assertions.assertEquals(false, lazyL.isEmpty())
        Assertions.assertEquals(true, lazyL.isNotEmpty())

        val lazyLEmpty = of(plOf(1, 2, 3).filter { it < 0 }, plOf(4, 7, 8).filter { it < 0 })
        Assertions.assertEquals(true, lazyLEmpty.isEmpty())
        Assertions.assertEquals(false, lazyLEmpty.isNotEmpty())
    }

    @Test
    fun isLazyType() {
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).isLazyType())

        Assertions.assertEquals(true, of(plOf(1, 2).filter { it > 0 }, plOf(3, 4)).isLazyType())
        Assertions.assertEquals(true, of(plOf(1, 2), plOf(3, 4).filter { it > 0 }).isLazyType())
        Assertions.assertEquals(true, of(plOf(1, 2).filter { it > 0 }, plOf(3, 4).filter { it > 0 }).isLazyType())
    }

    @Test
    fun iterator() {
        run {
            var counter = 0
            for (e in of(listOf(1, 2, 3), listOf(4, 5, 6, 7, 8, 9, 10))) counter++
            Assertions.assertEquals(10, counter)
        }
        run {
            var counter = 0
            of(listOf(1, 2, 3), listOf(4, 5, 6, 7, 8, 9, 10)).forEach { _ -> counter++ }
            Assertions.assertEquals(10, counter)
        }

        run {
            val iterator = of(listOf(1, 2, 3), listOf(4, 5)).iterator()
            var sum = 0
            while (iterator.hasNext()) sum += iterator.next()
            Assertions.assertEquals(15, sum)
        }
    }

    @Test
    fun lastIndexOf() {
        of(listOf(3, 1, 2), listOf(3, 1))
        Assertions.assertEquals(-1, of(listOf(3, 1, 2), listOf(3, 1)).lastIndexOf(0))
        Assertions.assertEquals(4, of(listOf(3, 1, 2), listOf(3, 1)).lastIndexOf(1))
        Assertions.assertEquals(2, of(listOf(3, 1, 2), listOf(3, 1)).lastIndexOf(2))
        Assertions.assertEquals(3, of(listOf(3, 1, 2), listOf(3, 1)).lastIndexOf(3))
    }

    @Test
    fun listIterator() {
        run {
            var counter = 0
            for (e in of(listOf(1, 2, 3), listOf(4, 5, 6, 7, 8, 9, 10)).listIterator()) counter++
            Assertions.assertEquals(10, counter)
        }
        run {
            var counter = 0
            of(listOf(1, 2, 3), listOf(4, 5, 6, 7, 8, 9, 10)).listIterator().forEach { _ -> counter++ }
            Assertions.assertEquals(10, counter)
        }

        run {
            val iterator = of(listOf(1, 2, 3), listOf(4, 5)).listIterator().iterator()
            var sum = 0
            while (iterator.hasNext()) sum += iterator.next()
            Assertions.assertEquals(15, sum)
        }
    }

    @Test
    fun map() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).map { it })
        Assertions.assertEquals(true, of(listOf(1, 2, 3), listOf(4, 5)).map { it }.isLazyType())
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).map { it })
        Assertions.assertEquals(plOf(2, 3, 4, 5, 6), of(listOf(1, 2, 3), listOf(4, 5)).map { it + 1 })
    }

    @Test
    fun mapIndexed() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3), listOf(4, 5)).mapIndexed { _, e -> e })
        Assertions.assertEquals(true, of(listOf(1, 2, 3), listOf(4, 5)).mapIndexed { _, e -> e }.isLazyType())
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).mapIndexed { _, e -> e })
        Assertions.assertEquals(plOf(1, 3, 5, 7, 9), of(listOf(1, 2, 3), listOf(4, 5)).mapIndexed { i, e -> e + i })
    }

    @Test
    fun mapIndexedNotNull() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3), listOf(4, 5)).mapIndexedNotNull { _, e -> e })
        Assertions.assertEquals(true, of(listOf(1, 2, 3), listOf(4, 5)).mapIndexedNotNull { _, e -> e }.isLazyType())
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).mapIndexedNotNull { _, e -> e })
        Assertions.assertEquals(
            plOf(1, 3, 5, 7, 9),
            of(listOf(1, 2, 3), listOf(4, 5)).mapIndexedNotNull { i, e -> e + i })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2, 3), listOf(4, 5)).mapIndexedNotNull { _, _ -> null })
        Assertions.assertEquals(
            plOf(1, 3, 5),
            of(listOf(1, 2, 3), listOf(4, 5)).mapIndexedNotNull { _, e -> if (e % 2 == 0) null else e })
    }

    @Test
    fun mapNotNull() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).mapNotNull { it })
        Assertions.assertEquals(true, of(listOf(1, 2, 3), listOf(4, 5)).mapNotNull { it }.isLazyType())
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).mapNotNull { it })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 2, 3), listOf(4, 5)).mapNotNull { null })
        Assertions.assertEquals(
            plOf(1, 3, 5),
            of(listOf(1, 0, 3), listOf(0, 5)).mapNotNull { if (it == 0) null else it })
    }

    @Test
    fun mapv() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)).mapv { it })
        Assertions.assertEquals(false, of(listOf(1, 2, 3), listOf(4, 5)).mapv { it }.isLazyType())
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5), of(listOf(1, 2, 3), listOf(4, 5)).mapv { it })
        Assertions.assertEquals(plOf(2, 3, 4, 5, 6), of(listOf(1, 2, 3), listOf(4, 5)).mapv { it + 1 })
    }

    @Test
    fun minus() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)) - 1)
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)) - listOf(1))
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2, 3), listOf(4, 5)) - setOf(1))

        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 1, 1), listOf(1, 1)) - 1)
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 1, 1), listOf(1, 1)) - listOf(1))
        Assertions.assertEquals(plOf<Int>(), of(listOf(1, 1, 1), listOf(1, 1)) - setOf(1))

        Assertions.assertEquals(plOf(5), of(listOf(1, 1, 1), listOf(1, 5)) - 1)
        Assertions.assertEquals(plOf(5), of(listOf(1, 1, 1), listOf(1, 5)) - listOf(1))
        Assertions.assertEquals(plOf(5), of(listOf(1, 1, 1), listOf(1, 5)) - setOf(1))

        Assertions.assertEquals(plOf(2, 4), of(listOf(1, 2), listOf(3, 4)) - 1 - 3)
        Assertions.assertEquals(plOf(2, 4), of(listOf(1, 2), listOf(3, 4)) - listOf(1, 3))
        Assertions.assertEquals(plOf(2, 4), of(listOf(1, 2), listOf(3, 4)) - setOf(1, 3))

        Assertions.assertEquals(plOf(1, 2, 3, 4), of(listOf(1, 2), listOf(3, 4)) - 55)
        Assertions.assertEquals(plOf(1, 2, 3, 4), of(listOf(1, 2), listOf(3, 4)) - listOf(55))
        Assertions.assertEquals(plOf(1, 2, 3, 4), of(listOf(1, 2), listOf(3, 4)) - setOf(55))
    }

    @Test
    fun none() {
        Assertions.assertEquals(true, of(listOf(1, 2), listOf(3, 4)).none { it < 0 })
        Assertions.assertEquals(true, of(listOf(0, 1, 2), listOf(3, 4)).none { it < 0 })
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).none { it > 0 })
        Assertions.assertEquals(false, of(listOf(0, 1, 2), listOf(3, 4)).none { it > 0 })
        Assertions.assertEquals(false, of(listOf(1, 2), listOf(3, 4)).none { it >= 0 })
        Assertions.assertEquals(false, of(listOf(0, 1, 2), listOf(3, 4)).none { it >= 0 })
    }

    @Test
    fun onEach() {
        Assertions.assertEquals(plOf(1, 2, 3, 4), of(listOf(1, 2), listOf(3, 4)).onEach { })
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(3, 4)).onEach {})
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(3, 4)).onEach {})

        var counter = 0
        of(listOf(1, 2), listOf(3, 4)).onEach { counter++ }
        Assertions.assertEquals(4, counter)
    }

    @Test
    fun onEachIndexed() {
        Assertions.assertEquals(plOf(1, 2, 3, 4), of(listOf(1, 2), listOf(3, 4)).onEachIndexed { _, _ -> })
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2), listOf(3, 4)).onEachIndexed { i, e -> e + i })
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2), listOf(3, 4)).onEachIndexed { _, _ -> })

        var counter = 0
        of(listOf(1, 2), listOf(3, 4)).onEachIndexed { _, _ -> counter++ }
        Assertions.assertEquals(4, counter)
    }

    @Test
    fun plus() {
        Assertions.assertEquals(plOf(1, 2, 3, 4), of(listOf(1, 2), listOf(3, 4)) + listOf())

        Assertions.assertEquals(plOf(1, 2, 3, 4, 5, 6), of(listOf(1, 2), listOf(3, 4)) + (5..6))
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5, 6), of(listOf(1, 2), listOf(3, 4)) + sequenceOf(5, 6))

        Assertions.assertEquals(plOf(1, 2, 3, 4, 5, 6), of(listOf(1, 2), listOf(3, 4)) + plOf(5, 6))
        Assertions.assertEquals(plOf(1, 2, 3, 4, 5, 6), of(listOf(1, 2), listOf(3, 4)) + listOf(5, 6))
    }


    @Test
    fun requireNoNulls() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(3, 4)).requireNoNulls())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(4)).requireNoNulls())
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(3, 4)).requireNoNulls())
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            of(
                listOf(null, null),
                listOf(3, 4)
            ).requireNoNulls()
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            of(
                listOf(1, 2),
                listOf(null, null)
            ).requireNoNulls()
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            of(
                listOf(null, 2),
                listOf(null, 4)
            ).requireNoNulls()
        }
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(1), listOf(2, 3)).requireNoNulls())
    }

    @Test
    fun reversed() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2, 3)).reversed())
        Assertions.assertEquals(plOf(3, 2, 1), of(listOf(1), listOf(2, 3)).reversed())
    }

    @Test
    fun runningFold() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1), listOf(2, 3)).runningFold(0) { acc, it -> acc + it })
        Assertions.assertEquals(plOf(0, 0, 0, 0), of(listOf(1), listOf(2, 3)).runningFold(0) { acc, _ -> acc })
        Assertions.assertEquals(plOf(0, 1, 3, 6), of(listOf(1), listOf(2, 3)).runningFold(0) { acc, it -> acc + it })
        Assertions.assertEquals(
            plOf("", "a", "ab"),
            of(listOf("a"), listOf("b")).runningFold("") { acc, it -> acc + it })
    }

    @Test
    fun runningFoldIndexed() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1), listOf(2, 3)).runningFoldIndexed(0) { _, acc, it -> acc + it })
        Assertions.assertEquals(
            plOf(0, 0, 0, 0),
            of(listOf(1), listOf(2, 3)).runningFoldIndexed(0) { _, acc, _ -> acc })
        Assertions.assertEquals(
            plOf(0, 1, 3, 6),
            of(listOf(1), listOf(2, 3)).runningFoldIndexed(0) { _, acc, it -> acc + it })
        Assertions.assertEquals(
            plOf("", "a", "ab"),
            of(listOf("a"), listOf("b")).runningFoldIndexed("") { _, acc, it -> acc + it })
    }

    @Test
    fun runningReduce() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1), listOf(2, 3)).runningReduce { acc, it -> acc + it })
        Assertions.assertEquals(plOf(1, 1, 1), of(listOf(1), listOf(2, 3)).runningReduce { acc, _ -> acc })
        Assertions.assertEquals(plOf(1, 3, 6), of(listOf(1), listOf(2, 3)).runningReduce { acc, it -> acc + it })
        Assertions.assertEquals(plOf("a", "ab"), of(listOf("a"), listOf("b")).runningReduce { acc, it -> acc + it })
    }

    @Test
    fun runningReduceIndexed() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1), listOf(2, 3)).runningReduceIndexed { _, acc, it -> acc + it })
        Assertions.assertEquals(plOf(1, 1, 1), of(listOf(1), listOf(2, 3)).runningReduceIndexed { _, acc, _ -> acc })
        Assertions.assertEquals(
            plOf(1, 3, 6),
            of(listOf(1), listOf(2, 3)).runningReduceIndexed { _, acc, it -> acc + it })
        Assertions.assertEquals(
            plOf("a", "ab"),
            of(listOf("a"), listOf("b")).runningReduceIndexed { _, acc, it -> acc + it })
    }

    @Test
    fun scan() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1), listOf(2, 3)).scan(0) { acc, it -> acc + it })
        Assertions.assertEquals(plOf("d", "d", "d", "d"), of(listOf("a"), listOf("b", "c")).scan("d") { acc, _ -> acc })
        Assertions.assertEquals(
            plOf("d", "da", "dab", "dabc"),
            of(listOf("a"), listOf("b", "c")).scan("d") { acc, it -> acc + it })
    }

    @Test
    fun scanIndexed() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1), listOf(2, 3)).scanIndexed(0) { _, acc, it -> acc + it })
        Assertions.assertEquals(
            plOf("d", "d", "d", "d"),
            of(listOf("a"), listOf("b", "c")).scanIndexed("d") { _, acc, _ -> acc })
        Assertions.assertEquals(
            plOf("d", "da", "dab", "dabc"),
            of(listOf("a"), listOf("b", "c")).scanIndexed("d") { _, acc, it -> acc + it })
    }

    @Test
    fun shuffled() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1, 2), listOf(3, 4)).shuffled())

        val l = of(listOf(1, 2), listOf(3, 4, 6, 7, 8))
        Assertions.assertNotEquals(l, l.shuffled(Random(0xCAFEBEEF)))
    }

    @Test
    fun sortedBy() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(3), listOf(2, 1)).sortedBy { it })
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(1), listOf(2, 3)).sortedBy { it })
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(3), listOf(2, 1)).sortedBy { it })
    }

    @Test
    fun sortedByDescending() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(3), listOf(2, 1)).sortedByDescending { it })
        Assertions.assertEquals(plOf(3, 2, 1), of(listOf(1), listOf(2, 3)).sortedByDescending { it })
        Assertions.assertEquals(plOf(3, 2, 1), of(listOf(3), listOf(2, 1)).sortedByDescending { it })
    }

    @Test
    fun sortedWith() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(3), listOf(2, 1)).sortedWith { a, b -> a.compareTo(b) })
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(1), listOf(2, 3)).sortedWith { a, b -> a.compareTo(b) })
        Assertions.assertEquals(plOf(1, 2, 3), of(listOf(3), listOf(2, 1)).sortedWith { a, b -> a.compareTo(b) })
    }

    @Test
    fun splitAt() {
        Assertions.assertInstanceOf(Pair::class.java, of(listOf(1, 2, 3, 4, 5, 6), listOf(7, 8, 9, 10)).splitAt(3))
        Assertions.assertEquals(plOf(1, 2) to of(listOf(3, 4), listOf(5)), of(listOf(1, 2, 3), listOf(4, 5)).splitAt(2))
    }

    @Test
    fun subList() {
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1, 2, 3, 4, 5, 6), listOf(7, 8, 9, 10)).subList(3, 6)
        )
        Assertions.assertEquals(listOf<Int>(), of(listOf(1, 2, 3, 4, 5, 6), listOf(7, 8, 9, 10)).subList(0, 0))
        Assertions.assertEquals(listOf(4, 5, 6), of(listOf(1, 2, 3, 4, 5, 6), listOf(7, 8, 9, 10)).subList(3, 6))
        Assertions.assertEquals(3, of(listOf(1, 2, 3, 4, 5, 6), listOf(7, 8, 9, 10)).subList(3, 6).size)
    }

    @Test
    fun take() {
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2)).take(0))
        Assertions.assertEquals(plOf(1), of(listOf(1), listOf(2)).take(1))
        Assertions.assertEquals(plOf(1, 2), of(listOf(1), listOf(2)).take(5))
        Assertions.assertEquals(plOf(0, 1, 2, 3, 4), of(listOf(0, 1, 2, 3, 4, 5, 6), listOf(7, 8, 9)).take(5))
    }

    @Test
    fun takeWhile() {
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2)).takeWhile { false })
        Assertions.assertEquals(plOf(1, 2), of(listOf(1), listOf(2)).takeWhile { true })
        Assertions.assertEquals(plOf(1), of(listOf(1), listOf(2, 1)).takeWhile { it < 2 })
        Assertions.assertEquals(plOf(1, 2), of(listOf(1), listOf(2)).takeWhile { true })
        Assertions.assertEquals(
            plOf(0, 1, 2, 3, 4),
            of(listOf(0, 1, 2, 3, 4, 5, 6), listOf(7, 8, 9)).takeWhile { it < 5 })
    }

    @Test
    fun toList() {
        Assertions.assertInstanceOf(List::class.java, of(listOf(1), listOf(2, 3)).toList())
        Assertions.assertEquals(listOf(1, 2, 3), of(listOf(1), listOf(2, 3)).toList())
        Assertions.assertEquals(listOf(3, 2, 1), of(listOf(3), listOf(2, 1)).toList())
        Assertions.assertEquals(listOf(3, 2, 1), of(listOf(3, 2), listOf(1)).toList())
    }

    @Test
    fun toMutableList() {
        Assertions.assertInstanceOf(MutableList::class.java, of(listOf(1), listOf(2, 3)).toMutableList())
        Assertions.assertEquals(mutableListOf(1, 2, 3), of(listOf(1), listOf(2, 3)).toMutableList())
        Assertions.assertEquals(mutableListOf(3, 2, 1), of(listOf(3), listOf(2, 1)).toMutableList())
        Assertions.assertEquals(mutableListOf(3, 2, 1), of(listOf(3, 2), listOf(1)).toMutableList())
    }

    @Test
    fun windowed() {
        val seq = PersistentList.of(1, 2, 3, 4)
        Assertions.assertEquals(plOf(plOf(1, 2), plOf(2, 3), plOf(3, 4)), seq.windowed(2))
        Assertions.assertEquals(plOf(plOf(1, 2), plOf(3, 4)), seq.windowed(2, step = 2))
        Assertions.assertEquals(plOf(plOf(1, 2, 3)), seq.windowed(3, step = 3))
        Assertions.assertEquals(plOf(plOf(1, 2, 3), plOf(4)), seq.windowed(3, step = 3, partialWindows = true))
        Assertions.assertEquals(plOf(3), seq.windowed(3, step = 3, transform = { it.size }))
        Assertions.assertEquals(
            plOf(3, 1),
            seq.windowed(3, step = 3, partialWindows = true, transform = { it.size })
        )
    }

    @Test
    fun withIndex() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2, 3)).withIndex())
        Assertions.assertEquals(plOf(IndexedValue(0, 2), IndexedValue(1, 4)), of(listOf(2), listOf(4)).withIndex())
    }

    @Test
    fun zip() {
        Assertions.assertInstanceOf(PersistentList::class.java, of(listOf(1), listOf(2, 3)).zip(plOf<Int>()))
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1), listOf(2, 3)).zip(of(listOf(4), listOf(5, 6)))
        )

        Assertions.assertEquals(plOf<Pair<Int, Int>>(), of(listOf(1), listOf(2, 3)).zip(plOf<Int>()))

        Assertions.assertEquals(plOf(1 to 4, 2 to 5), of(listOf(1), listOf(2)).zip(of(listOf(4), listOf(5, 6))))
        Assertions.assertEquals(plOf(1 to 4, 2 to 5), of(listOf(1), listOf(2, 3)).zip(of(listOf(4), listOf(5))))
        Assertions.assertEquals(
            plOf(1 to 4, 2 to 5, 3 to 6),
            of(listOf(1), listOf(2, 3)).zip(of(listOf(4), listOf(5, 6)))
        )

        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1), listOf(2, 3)).zip(plOf<Int>()) { a, b -> a + b })
        Assertions.assertInstanceOf(
            PersistentList::class.java,
            of(listOf(1), listOf(2, 3)).zip(of(listOf(4), listOf(5, 6))) { a, b -> a + b })
        Assertions.assertEquals(plOf<Int>(), of(listOf(1), listOf(2, 3)).zip(plOf<Int>()) { a, b -> a + b })

        Assertions.assertEquals(plOf(5, 7), of(listOf(1), listOf(2)).zip(plOf(4, 5, 6)) { a, b -> a + b })
        Assertions.assertEquals(plOf(5, 7), of(listOf(1), listOf(2, 3)).zip(plOf(4, 5)) { a, b -> a + b })
        Assertions.assertEquals(plOf(5, 7, 9), of(listOf(1), listOf(2, 3)).zip(plOf(4, 5, 6)) { a, b -> a + b })
    }

    @Test
    fun zipWithNext() {
        Assertions.assertEquals(plOf(1 to 2, 2 to 3), of(listOf(1), listOf(2, 3)).zipWithNext())
        Assertions.assertEquals(plOf(3, 5), of(listOf(1), listOf(2, 3)).zipWithNext { a, b -> a + b })
    }
}