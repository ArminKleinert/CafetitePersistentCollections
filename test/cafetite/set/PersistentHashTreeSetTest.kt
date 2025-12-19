package cafetite.set

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions

class PersistentHashTreeSetTest {

    @Test
    fun getSize() {
        Assertions.assertEquals(0, PersistentHashTreeSet<Int>().size)

        Assertions.assertEquals(0, PersistentHashTreeSet(setOf<Int>()).size)
        Assertions.assertEquals(0, PersistentHashTreeSet(setOf<Int>() as Iterable<Int>).size)
        Assertions.assertEquals(0, PersistentHashTreeSet(setOf<Int>() as Collection<Int>).size)

        Assertions.assertEquals(1, PersistentHashTreeSet(setOf(1)).size)
        Assertions.assertEquals(1, PersistentHashTreeSet(setOf(1) as Iterable<Int>).size)
        Assertions.assertEquals(1, PersistentHashTreeSet(setOf(1) as Collection<Int>).size)

        Assertions.assertEquals(3, PersistentHashTreeSet(setOf(1, 2, 3)).size)
        Assertions.assertEquals(3, PersistentHashTreeSet(setOf(1, 2, 3) as Iterable<Int>).size)
        Assertions.assertEquals(3, PersistentHashTreeSet(setOf(1, 2, 3) as Collection<Int>).size)

        Assertions.assertEquals(1, PersistentHashTreeSet(setOf(1, 1, 1)).size)
        Assertions.assertEquals(1, PersistentHashTreeSet(setOf(1, 1, 1) as Iterable<Int>).size)
        Assertions.assertEquals(1, PersistentHashTreeSet(setOf(1, 1, 1) as Collection<Int>).size)
    }

    @Test
    operator fun iterator() {
        run {
            var size = 0
            for (e in PersistentHashTreeSet(setOf<Int>()))
                size++
            Assertions.assertEquals(0, size)
        }
        run {
            var size = 0
            for (e in PersistentHashTreeSet(setOf(1, 1, 2, 3, 3)))
                size++
            Assertions.assertEquals(3, size)
        }
    }

    @Test
    fun contains() {
        Assertions.assertFalse(PersistentHashTreeSet<Int>().contains(1))
        Assertions.assertFalse(PersistentHashTreeSet(setOf<Int>()).contains(1))
        Assertions.assertTrue(PersistentHashTreeSet(setOf(1, 2, 3)).contains(1))
        Assertions.assertFalse(PersistentHashTreeSet(setOf(2, 3)).contains(1))
    }

    @Test
    fun conjAll() {
        Assertions.assertEquals(0, PersistentHashTreeSet<Int>().size)
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).conjAll(setOf())))

        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).conjAll(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).conjAll(setOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).conjAll(setOf(1))))
        Assertions.assertEquals(setOf(1, 2, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).conjAll(setOf(2, 4))))

        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).conjAll(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).conjAll(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).conjAll(listOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).conjAll(listOf(1))))
        Assertions.assertEquals(setOf(1, 2, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).conjAll(listOf(2, 4))))
    }

    @Test
    fun withoutAll() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).withoutAll(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).withoutAll(setOf())))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).withoutAll(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).withoutAll(setOf(1))))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).withoutAll(setOf(2, 4))))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).withoutAll(setOf(4, 5, 6))))

        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).withoutAll(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).withoutAll(listOf())))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).withoutAll(listOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).withoutAll(listOf(1))))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).withoutAll(listOf(2, 4))))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).withoutAll(listOf(4, 5, 6))))
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(PersistentHashTreeSet(setOf<Int>()).isEmpty())
        Assertions.assertFalse(PersistentHashTreeSet(setOf(1, 2, 3)).isEmpty())
    }

    @Test
    fun containsAll() {
        Assertions.assertFalse(PersistentHashTreeSet(setOf<Int>()).containsAll(listOf(1)))
        Assertions.assertTrue(PersistentHashTreeSet(setOf(1, 2, 3)).containsAll(listOf(1)))
        Assertions.assertTrue(PersistentHashTreeSet(setOf(1, 2, 3)).containsAll(listOf(1, 2, 3)))
        Assertions.assertFalse(PersistentHashTreeSet(setOf(2, 3)).containsAll(listOf(1, 2)))
    }

    @Test
    fun conj() {
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).conj(1)))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).conj(1)))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).conj(2)))
        Assertions.assertEquals(setOf(1, 2, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).conj(2).conj(4)))
    }

    @Test
    fun without() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).without(1)))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).without(1)))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).without(2)))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).without(2).without(4)))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).without(4)))
    }

    @Test
    fun plusSingle() {
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).plus(1)))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).plus(1)))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).plus(2)))
        Assertions.assertEquals(setOf(1, 2, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).plus(2).plus(4)))
    }

    @Test
    fun plusMulti() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).plus(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).plus(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).plus(setOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).plus(setOf(1))))
        Assertions.assertEquals(setOf(1, 2, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).plus(setOf(2, 4))))

        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).plus(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).plus(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).plus(listOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).plus(listOf(1))))
        Assertions.assertEquals(setOf(1, 2, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).plus(listOf(2, 4))))
    }

    @Test
    fun minusSingle() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).minus(1)))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).minus(1)))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).minus(2)))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).minus(2).minus(4)))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).minus(4)))
    }

    @Test
    fun minusMulti() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).minus(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).minus(setOf())))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).minus(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).minus(setOf(1))))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).minus(setOf(2, 4))))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).minus(setOf(4, 5, 6))))

        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).minus(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).minus(listOf())))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).minus(listOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).minus(listOf(1))))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).minus(listOf(2, 4))))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).minus(listOf(4, 5, 6))))
    }

    @Test
    fun union() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).union(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).union(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).union(setOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).union(setOf(1))))
        Assertions.assertEquals(setOf(1, 2, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).union(setOf(2, 4))))

        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).union(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).union(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).union(listOf(1))))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).union(listOf(1))))
        Assertions.assertEquals(setOf(1, 2, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).union(listOf(2, 4))))
    }

    @Test
    fun intersection() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).intersection(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).intersection(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).intersection(setOf(1))))
        Assertions.assertEquals(setOf(2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).intersection(setOf(2, 3, 4))))
    }

    @Test
    fun and() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).and(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).and(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).and(setOf(1))))
        Assertions.assertEquals(setOf(2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).and(setOf(2, 3, 4))))
    }

    @Test
    fun difference() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).difference(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).difference(setOf())))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).difference(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).difference(setOf(1))))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).difference(setOf(2, 4))))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).difference(setOf(4, 5, 6))))

        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).difference(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).difference(listOf())))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).difference(listOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).difference(listOf(1))))
        Assertions.assertEquals(setOf(1, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).difference(listOf(2, 4))))
        Assertions.assertEquals(setOf(1, 2, 3), (PersistentHashTreeSet(setOf(1, 2, 3)).difference(listOf(4, 5, 6))))
    }

    @Test
    fun symmetricDifference() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).symmetricDifference(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).symmetricDifference(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).symmetricDifference(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).symmetricDifference(setOf(1))))
        Assertions.assertEquals(
            setOf(1, 3, 4),
            (PersistentHashTreeSet(setOf(1, 2, 3)).symmetricDifference(setOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3, 4, 5, 6),
            (PersistentHashTreeSet(setOf(1, 2, 3)).symmetricDifference(setOf(4, 5, 6)))
        )

        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).symmetricDifference(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).symmetricDifference(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).symmetricDifference(listOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).symmetricDifference(listOf(1))))
        Assertions.assertEquals(
            setOf(1, 3, 4),
            (PersistentHashTreeSet(setOf(1, 2, 3)).symmetricDifference(listOf(2, 4)))
        )
        Assertions.assertEquals(
            setOf(1, 2, 3, 4, 5, 6),
            (PersistentHashTreeSet(setOf(1, 2, 3)).symmetricDifference(listOf(4, 5, 6)))
        )
    }

    @Test
    fun xor() {
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).xor(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).xor(setOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).xor(setOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).xor(setOf(1))))
        Assertions.assertEquals(setOf(1, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).xor(setOf(2, 4))))
        Assertions.assertEquals(setOf(1, 2, 3, 4, 5, 6), (PersistentHashTreeSet(setOf(1, 2, 3)).xor(setOf(4, 5, 6))))

        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf<Int>()).xor(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf(1)).xor(listOf())))
        Assertions.assertEquals(setOf(1), (PersistentHashTreeSet(setOf<Int>()).xor(listOf(1))))
        Assertions.assertEquals(setOf<Int>(), (PersistentHashTreeSet(setOf(1)).xor(listOf(1))))
        Assertions.assertEquals(setOf(1, 3, 4), (PersistentHashTreeSet(setOf(1, 2, 3)).xor(listOf(2, 4))))
        Assertions.assertEquals(setOf(1, 2, 3, 4, 5, 6), (PersistentHashTreeSet(setOf(1, 2, 3)).xor(listOf(4, 5, 6))))
    }

    @Test
    fun testEquals() {
        Assertions.assertEquals(PersistentHashTreeSet(setOf<Int>()), setOf<Int>())
        Assertions.assertEquals(setOf(1), PersistentHashTreeSet(setOf(1)))
        Assertions.assertEquals(PersistentHashTreeSet(setOf(1, 2, 3, 4, 5, 6)), setOf(1, 2, 3, 4, 5, 6))
        Assertions.assertEquals(PersistentHashTreeSet(setOf(1, 1, 1, 1, 1, 1)), setOf(1))

        Assertions.assertNotEquals(PersistentHashTreeSet(setOf<Int>()), setOf(7))
        Assertions.assertNotEquals(PersistentHashTreeSet(setOf(1)), setOf(1, 7))
        Assertions.assertNotEquals(PersistentHashTreeSet(setOf(1, 2, 3, 4, 5, 6)), setOf(1, 2, 3, 4, 5, 6, 7))
        Assertions.assertNotEquals(PersistentHashTreeSet(setOf(1, 1, 1, 1, 1, 1)), setOf(1, 7))
    }
}