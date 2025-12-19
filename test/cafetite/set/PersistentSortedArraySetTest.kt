package cafetite.set

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions

class PersistentSortedArraySetTest {

    @Test
    fun getSize() {
        Assertions.assertEquals(0, PersistentSortedArraySet(setOf<Int>(), Int::compareTo).size)
        Assertions.assertEquals(0, PersistentSortedArraySet(setOf<Int>() as Iterable<Int>, Int::compareTo).size)
        Assertions.assertEquals(0, PersistentSortedArraySet(setOf<Int>() as Collection<Int>, Int::compareTo).size)

        Assertions.assertEquals(1, PersistentSortedArraySet(setOf(1), Int::compareTo).size)
        Assertions.assertEquals(1, PersistentSortedArraySet(setOf(1) as Iterable<Int>, Int::compareTo).size)
        Assertions.assertEquals(1, PersistentSortedArraySet(setOf(1) as Collection<Int>, Int::compareTo).size)

        Assertions.assertEquals(3, PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).size)
        Assertions.assertEquals(3, PersistentSortedArraySet(setOf(1, 2, 3) as Iterable<Int>, Int::compareTo).size)
        Assertions.assertEquals(3, PersistentSortedArraySet(setOf(1, 2, 3) as Collection<Int>, Int::compareTo).size)

        Assertions.assertEquals(1, PersistentSortedArraySet(setOf(1, 1, 1), Int::compareTo).size)
        Assertions.assertEquals(1, PersistentSortedArraySet(setOf(1, 1, 1) as Iterable<Int>, Int::compareTo).size)
        Assertions.assertEquals(1, PersistentSortedArraySet(setOf(1, 1, 1) as Collection<Int>, Int::compareTo).size)
    }

    @Test
    operator fun iterator() {
        run {
            var size = 0
            for (e in PersistentSortedArraySet(setOf<Int>(), Int::compareTo))
                size++
            Assertions.assertEquals(0, size)
        }
        run {
            var size = 0
            for (e in PersistentSortedArraySet(setOf(1, 1, 2, 3, 3), Int::compareTo))
                size++
            Assertions.assertEquals(3, size)
        }
    }

    @Test
    fun contains() {
        Assertions.assertFalse(PersistentSortedArraySet(setOf<Int>(), Int::compareTo).contains(1))
        Assertions.assertTrue(PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).contains(1))
        Assertions.assertFalse(PersistentSortedArraySet(setOf(2, 3), Int::compareTo).contains(1))
    }

    @Test
    fun conjAll() {
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).conjAll(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).conjAll(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).conjAll(setOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).conjAll(setOf(1))))
        Assertions.assertEquals(
            setOf(1, 2, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).conjAll(setOf(2, 4)))
        )

        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).conjAll(listOf()))
        )
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).conjAll(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).conjAll(listOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).conjAll(listOf(1))))
        Assertions.assertEquals(
            setOf(1, 2, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).conjAll(listOf(2, 4)))
        )
    }

    @Test
    fun withoutAll() {
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).withoutAll(setOf()))
        )
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).withoutAll(setOf())))
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).withoutAll(setOf(1)))
        )
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf(1), Int::compareTo).withoutAll(setOf(1))))
        Assertions.assertEquals(
            setOf(1, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).withoutAll(setOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).withoutAll(setOf(4, 5, 6)))
        )

        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).withoutAll(listOf()))
        )
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).withoutAll(listOf())))
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).withoutAll(listOf(1)))
        )
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf(1), Int::compareTo).withoutAll(listOf(1)))
        )
        Assertions.assertEquals(
            setOf(1, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).withoutAll(listOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).withoutAll(listOf(4, 5, 6)))
        )
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(PersistentSortedArraySet(setOf<Int>(), Int::compareTo).isEmpty())
        Assertions.assertFalse(PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).isEmpty())
    }

    @Test
    fun containsAll() {
        Assertions.assertFalse(PersistentSortedArraySet(setOf<Int>(), Int::compareTo).containsAll(listOf(1)))
        Assertions.assertTrue(PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).containsAll(listOf(1)))
        Assertions.assertTrue(PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).containsAll(listOf(1, 2, 3)))
        Assertions.assertFalse(PersistentSortedArraySet(setOf(2, 3), Int::compareTo).containsAll(listOf(1, 2)))
    }

    @Test
    fun conj() {
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).conj(1)))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).conj(1)))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).conj(2)))
        Assertions.assertEquals(
            setOf(1, 2, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).conj(2).conj(4))
        )
    }

    @Test
    fun without() {
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).without(1)))
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf(1), Int::compareTo).without(1)))
        Assertions.assertEquals(setOf(1, 3), (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).without(2)))
        Assertions.assertEquals(
            setOf(1, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).without(2).without(4))
        )
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).without(4)))
    }

    @Test
    fun plusSingle() {
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).plus(1)))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).plus(1)))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).plus(2)))
        Assertions.assertEquals(
            setOf(1, 2, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).plus(2).plus(4))
        )
    }

    @Test
    fun plusMulti() {
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).plus(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).plus(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).plus(setOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).plus(setOf(1))))
        Assertions.assertEquals(
            setOf(1, 2, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).plus(setOf(2, 4)))
        )

        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).plus(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).plus(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).plus(listOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).plus(listOf(1))))
        Assertions.assertEquals(
            setOf(1, 2, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).plus(listOf(2, 4)))
        )
    }

    @Test
    fun minusSingle() {
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).minus(1)))
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf(1), Int::compareTo).minus(1)))
        Assertions.assertEquals(setOf(1, 3), (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).minus(2)))
        Assertions.assertEquals(
            setOf(1, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).minus(2).minus(4))
        )
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).minus(4)))
    }

    @Test
    fun minusMulti() {
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).minus(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).minus(setOf())))
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).minus(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf(1), Int::compareTo).minus(setOf(1))))
        Assertions.assertEquals(
            setOf(1, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).minus(setOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).minus(setOf(4, 5, 6)))
        )

        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).minus(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).minus(listOf())))
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).minus(listOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf(1), Int::compareTo).minus(listOf(1))))
        Assertions.assertEquals(
            setOf(1, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).minus(listOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).minus(listOf(4, 5, 6)))
        )
    }

    @Test
    fun union() {
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).union(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).union(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).union(setOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).union(setOf(1))))
        Assertions.assertEquals(
            setOf(1, 2, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).union(setOf(2, 4)))
        )

        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).union(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).union(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).union(listOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).union(listOf(1))))
        Assertions.assertEquals(
            setOf(1, 2, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).union(listOf(2, 4)))
        )
    }

    @Test
    fun intersection() {
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).intersection(setOf(1)))
        )
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf(1), Int::compareTo).intersection(setOf()))
        )
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).intersection(setOf(1))))
        Assertions.assertEquals(
            setOf(2, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).intersection(setOf(2, 3, 4)))
        )
    }

    @Test
    fun and() {
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).and(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf(1), Int::compareTo).and(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).and(setOf(1))))
        Assertions.assertEquals(
            setOf(2, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).and(setOf(2, 3, 4)))
        )
    }

    @Test
    fun difference() {
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).difference(setOf()))
        )
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).difference(setOf())))
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).difference(setOf(1)))
        )
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf(1), Int::compareTo).difference(setOf(1))))
        Assertions.assertEquals(
            setOf(1, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).difference(setOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).difference(setOf(4, 5, 6)))
        )

        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).difference(listOf()))
        )
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).difference(listOf())))
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).difference(listOf(1)))
        )
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf(1), Int::compareTo).difference(listOf(1)))
        )
        Assertions.assertEquals(
            setOf(1, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).difference(listOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).difference(listOf(4, 5, 6)))
        )
    }

    @Test
    fun symmetricDifference() {
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).symmetricDifference(setOf()))
        )
        Assertions.assertEquals(
            setOf(1),
            (PersistentSortedArraySet(setOf(1), Int::compareTo).symmetricDifference(setOf()))
        )
        Assertions.assertEquals(
            setOf(1),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).symmetricDifference(setOf(1)))
        )
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf(1), Int::compareTo).symmetricDifference(setOf(1)))
        )
        Assertions.assertEquals(
            setOf(1, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).symmetricDifference(setOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3, 4, 5, 6),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).symmetricDifference(setOf(4, 5, 6)))
        )

        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).symmetricDifference(listOf()))
        )
        Assertions.assertEquals(
            setOf(1),
            (PersistentSortedArraySet(setOf(1), Int::compareTo).symmetricDifference(listOf()))
        )
        Assertions.assertEquals(
            setOf(1),
            (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).symmetricDifference(listOf(1)))
        )
        Assertions.assertEquals(
            setOf<Int>(),
            (PersistentSortedArraySet(setOf(1), Int::compareTo).symmetricDifference(listOf(1)))
        )
        Assertions.assertEquals(
            setOf(1, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).symmetricDifference(listOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3, 4, 5, 6),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).symmetricDifference(listOf(4, 5, 6)))
        )
    }

    @Test
    fun xor() {
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).xor(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).xor(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).xor(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf(1), Int::compareTo).xor(setOf(1))))
        Assertions.assertEquals(
            setOf(1, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).xor(setOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3, 4, 5, 6),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).xor(setOf(4, 5, 6)))
        )

        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).xor(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf(1), Int::compareTo).xor(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentSortedArraySet(setOf<Int>(), Int::compareTo).xor(listOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentSortedArraySet(setOf(1), Int::compareTo).xor(listOf(1))))
        Assertions.assertEquals(
            setOf(1, 3, 4),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).xor(listOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3, 4, 5, 6),
            (PersistentSortedArraySet(setOf(1, 2, 3), Int::compareTo).xor(listOf(4, 5, 6)))
        )
    }

    @Test
    fun testEquals() {
        Assertions.assertEquals(PersistentSortedArraySet(setOf<Int>(), Int::compareTo), setOf<Int>())
        Assertions.assertEquals(setOf(1), PersistentSortedArraySet(setOf(1), Int::compareTo))
        Assertions.assertEquals(
            PersistentSortedArraySet(setOf(1, 2, 3, 4, 5, 6), Int::compareTo),
            setOf(1, 2, 3, 4, 5, 6)
        )
        Assertions.assertEquals(PersistentSortedArraySet(setOf(1, 1, 1, 1, 1, 1), Int::compareTo), setOf(1))

        Assertions.assertNotEquals(PersistentSortedArraySet(setOf<Int>(), Int::compareTo), setOf(7))
        Assertions.assertNotEquals(PersistentSortedArraySet(setOf(1), Int::compareTo), setOf(1, 7))
        Assertions.assertNotEquals(
            PersistentSortedArraySet(setOf(1, 2, 3, 4, 5, 6), Int::compareTo),
            setOf(1, 2, 3, 4, 5, 6, 7)
        )
        Assertions.assertNotEquals(PersistentSortedArraySet(setOf(1, 1, 1, 1, 1, 1), Int::compareTo), setOf(1, 7))
    }
}