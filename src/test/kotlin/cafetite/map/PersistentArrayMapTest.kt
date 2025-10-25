package cafetite.map

import cafetite.map.PersistentArrayMap
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

class PersistentArrayMapTest {
    @Test
    fun getSize() {
        Assertions.assertEquals(0, PersistentArrayMap<Int, Int>(listOf()).size)
        Assertions.assertEquals(0, PersistentArrayMap<Int, Int>(mapOf()).size)

        Assertions.assertEquals(3, PersistentArrayMap(listOf(1 to 2, 3 to 4, 5 to 6)).size)
        Assertions.assertEquals(3, PersistentArrayMap(mapOf(1 to 2, 3 to 4, 5 to 6)).size)

        Assertions.assertEquals(1, PersistentArrayMap(listOf(1 to 2, 1 to 3, 1 to 6)).size)
        Assertions.assertEquals(1, PersistentArrayMap(mapOf(1 to 2, 1 to 3, 1 to 6)).size)
    }

    @Test
    fun iterator() {
        run {
            var size = 0
            for (e in PersistentArrayMap<Int, Int>(mapOf()))
                size++
            Assertions.assertEquals(0, size)
        }
        run {
            var size = 0
            for (e in PersistentArrayMap(mapOf(1 to 2, 1 to 4, 1 to 6)))
                size++
            Assertions.assertEquals(1, size)
        }
    }

    @Test
    fun ordered() {
        val r = Random(0xC0FFEEBEE) // A bee in my coffee
        val list = (0..128).map {r.nextInt() to 0}
        val m = PersistentArrayMap(list)

        // Order is preserved.
        for ((index, entry) in m.withIndex()) {
            Assertions.assertEquals(list[index].first , entry.key)
        }

        // Even when adding entries in a new order, the old order is preserved.
        for ((index, entry) in (m + list.sortedBy { it.first }).withIndex()) {
            Assertions.assertEquals(list[index].first , entry.key)
        }

        // After removing an element, the old order is preserved, without that element.
        val list1 = list - list[55]
        val m1 = m.dissoc(list[55].first)
        for ((index, entry) in m1.withIndex()) {
            Assertions.assertEquals(list1[index].first , entry.key)
        }
    }

    @Test
    fun getEntries() {
        Assertions.assertEquals(mapOf<Int, Int>().entries, PersistentArrayMap(mapOf<Int, Int>()).entries)
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4).entries, PersistentArrayMap(mapOf(1 to 2, 3 to 4)).entries)
    }

    @Test
    fun getKeys() {
        Assertions.assertEquals(setOf<Int>(), PersistentArrayMap(mapOf<Int, Int>()).keys)
        Assertions.assertEquals(setOf(1), PersistentArrayMap(mapOf(1 to 2, 1 to 3, 1 to 4)).keys)
        Assertions.assertEquals(setOf(1, 2, 3), PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).keys)
    }

    @Test
    fun getValues() {
        Assertions.assertEquals(listOf<Int>(), PersistentArrayMap(mapOf<Int, Int>()).values)
        Assertions.assertEquals(listOf(2, 3, 4), PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).values)
        Assertions.assertEquals(listOf(2, 2, 2), PersistentArrayMap(mapOf(1 to 2, 2 to 2, 3 to 2)).values)
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(PersistentArrayMap(mapOf<Int, Int>()).isEmpty())
        Assertions.assertFalse(PersistentArrayMap(mapOf(1 to 2, 1 to 3, 1 to 4)).isEmpty())
    }

    @Test
    fun get() {
        Assertions.assertEquals(null, PersistentArrayMap(mapOf<Int, Int>())[1])
        Assertions.assertEquals(2, PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4))[1])
        Assertions.assertEquals(null, PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4))[7])
    }

    @Test
    fun selectKeys() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap(mapOf<Int, Int>()).selectKeys(setOf(1, 2, 3)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3),
            PersistentArrayMap(mapOf(1 to 2, 2 to 3)).selectKeys(setOf(1, 2))
        )
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3), PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).selectKeys(setOf(1, 2))
        )
        Assertions.assertEquals(
            mapOf(1 to 2),
            PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).selectKeys(setOf(1, 4))
        )
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap(mapOf(1 to 2, 2 to 3)).selectKeys(setOf(3, 4)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap(mapOf(1 to 2, 2 to 3)).selectKeys(setOf()))
    }

    @Test
    fun containsValue() {
        Assertions.assertFalse(PersistentArrayMap(mapOf<Int, Int>()).containsValue(1))
        Assertions.assertTrue(PersistentArrayMap(mapOf(1 to 1)).containsValue(1))
        Assertions.assertTrue(PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).containsValue(3))
        Assertions.assertFalse(PersistentArrayMap(mapOf(1 to 2, 1 to 3, 1 to 4)).containsValue(1))
    }

    @Test
    fun containsKey() {
        Assertions.assertFalse(PersistentArrayMap(mapOf<Int, Int>()).containsKey(1))
        Assertions.assertTrue(PersistentArrayMap(mapOf(1 to 1)).containsKey(1))
        Assertions.assertTrue(PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).containsKey(2))
        Assertions.assertFalse(PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).containsKey(99))
    }

    @Test
    fun contains() {
        Assertions.assertFalse(PersistentArrayMap(mapOf<Int, Int>()).contains(1))
        Assertions.assertTrue(PersistentArrayMap(mapOf(1 to 1)).contains(1))
        Assertions.assertTrue(PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).contains(2))
        Assertions.assertFalse(PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).contains(99))
    }

    @Test
    fun getOrDefault() {
        Assertions.assertEquals(88, PersistentArrayMap(mapOf<Int, Int>()).getOrDefault(1, 88))
        Assertions.assertEquals(2, PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).getOrDefault(1, 88))
        Assertions.assertEquals(88, PersistentArrayMap(mapOf(1 to 2, 2 to 3, 3 to 4)).getOrDefault(7, 88))
    }

    @Test
    fun assocAll() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap<Int, Int>(mapOf()).assocAll(listOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2)).assocAll(listOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentArrayMap(mapOf(1 to 2)).assocAll(listOf(2 to 3, 3 to 4))
        )

        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap<Int, Int>(mapOf()).assocAll(mapOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2)).assocAll(mapOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentArrayMap(mapOf(1 to 2)).assocAll(mapOf(2 to 3, 3 to 4))
        )
    }

    @Test
    fun dissocAll() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap<Int, Int>(mapOf()).dissocAll(listOf(1)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap(mapOf(1 to 2)).dissocAll(listOf(1)))
        Assertions.assertEquals(mapOf(3 to 4), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).dissocAll(listOf(1, 4)))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).dissocAll(listOf(4)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).dissocAll(listOf(3)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).dissocAll(listOf(1, 2, 3)))
    }

    @Test
    fun assoc() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap<Int, Int>(mapOf()).assoc(1, 2))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2)).assoc(1, 2))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentArrayMap(mapOf(1 to 2)).assoc(2, 3).assoc(3, 4)
        )
    }

    @Test
    fun plusSingle() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap<Int, Int>(mapOf()).plus(1 to 2))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2)).plus(1 to 2))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentArrayMap(mapOf(1 to 2)).plus(2 to 3).plus(3 to 4)
        )
    }

    @Test
    fun plusMulti() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap<Int, Int>(mapOf()).plus(listOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2)).plus(listOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentArrayMap(mapOf(1 to 2)).plus(listOf(2 to 3, 3 to 4))
        )

        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap<Int, Int>(mapOf()).plus(mapOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2)).plus(mapOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentArrayMap(mapOf(1 to 2)).plus(mapOf(2 to 3, 3 to 4))
        )
    }

    @Test
    fun dissoc() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap<Int, Int>(mapOf()).dissoc(1))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap(mapOf(1 to 2)).dissoc(1))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).dissoc(5))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).dissoc(4))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).dissoc(3))
    }

    @Test
    fun minusSingle() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap<Int, Int>(mapOf()).minus(1))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap(mapOf(1 to 2)).minus(1))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).minus(5))
        Assertions.assertEquals(mapOf(3 to 4), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).minus(1).minus(4))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).minus(3))
    }

    @Test
    fun minusMulti() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap<Int, Int>(mapOf()).minus(listOf(1)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap(mapOf(1 to 2)).minus(listOf(1)))
        Assertions.assertEquals(mapOf(3 to 4), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).minus(listOf(1, 4)))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).minus(listOf(4)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).minus(listOf(3)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentArrayMap(mapOf(1 to 2, 3 to 4)).minus(listOf(1, 2, 3)))
    }

    @Test
    fun testEquals() {
        Assertions.assertEquals(PersistentArrayMap<Int, Int>(mapOf()), mapOf<Int, Int>())
        Assertions.assertEquals(PersistentArrayMap(mapOf(1 to 2, 3 to 4)), mapOf(1 to 2, 3 to 4))

        Assertions.assertNotEquals(PersistentArrayMap<Int, Int>(mapOf()), mapOf(1 to 2, 3 to 4))
        Assertions.assertNotEquals(PersistentArrayMap(mapOf(1 to 2, 3 to 4)), mapOf<Int, Int>())
    }
}