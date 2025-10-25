package cafetite.map

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

class PersistentSortedMapTest {
    @Test
    fun getSize() {
        Assertions.assertEquals(0, PersistentSortedMap<Int, Int>(listOf(), Int::compareTo).size)
        Assertions.assertEquals(0, PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).size)

        Assertions.assertEquals(3, PersistentSortedMap(1 to 2, 3 to 4, 5 to 6, comparator=Int::compareTo).size)
        Assertions.assertEquals(3, PersistentSortedMap(listOf(1 to 2, 3 to 4, 5 to 6), Int::compareTo).size)
        Assertions.assertEquals(3, PersistentSortedMap(mapOf(1 to 2, 3 to 4, 5 to 6), Int::compareTo).size)

        Assertions.assertEquals(1, PersistentSortedMap(1 to 2, 1 to 3, 1 to 6, comparator=Int::compareTo).size)
        Assertions.assertEquals(1, PersistentSortedMap(listOf(1 to 2, 1 to 3, 1 to 6), Int::compareTo).size)
        Assertions.assertEquals(1, PersistentSortedMap(mapOf(1 to 2, 1 to 3, 1 to 6), Int::compareTo).size)
    }

    @Test
     fun iterator() {
        run {
            var size = 0
            for (e in PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo))
                size++
            Assertions.assertEquals(0, size)
        }
        run {
            var size = 0
            for (e in PersistentSortedMap(mapOf(1 to 2, 1 to 4, 1 to 6), Int::compareTo))
                size++
            Assertions.assertEquals(1, size)
        }
    }

    @Test
    fun sorted() {
        val r = Random(0xC0FFEEBEE) // A bee in my coffee
        val list = (0..128).map {it to 0}.shuffled(r)
        val m = PersistentSortedMap(list, Int::compareTo)
        val listSorted = list.sortedBy { it.first }

        // Order is preserved.
        for ((index, entry) in m.withIndex()) {
            Assertions.assertEquals(listSorted[index].first , entry.key)
        }

        // Even when adding entries in a new order, the old order is preserved.
        for ((index, entry) in (m + list).withIndex()) {
            Assertions.assertEquals(listSorted[index].first , entry.key)
        }

        // After removing an element, the old order is preserved, without that element.
        val list1 = (list - list[55]).sortedBy { it.first }
        val m1 = m.dissoc(list[55].first)
        for ((index, entry) in m1.withIndex()) {
            Assertions.assertEquals(list1[index].first , entry.key)
        }
    }

    @Test
    fun getEntries() {
        Assertions.assertEquals(mapOf<Int, Int>().entries, PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo).entries)
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4).entries, PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).entries)
    }

    @Test
    fun getKeys() {
        Assertions.assertEquals(setOf<Int>(), PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo).keys)
        Assertions.assertEquals(setOf(1), PersistentSortedMap(mapOf(1 to 2, 1 to 3, 1 to 4), Int::compareTo).keys)
        Assertions.assertEquals(setOf(1, 2, 3), PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).keys)
    }

    @Test
    fun getValues() {
        Assertions.assertEquals(listOf<Int>(), PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo).values)
        Assertions.assertEquals(listOf(2, 3, 4), PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).values)
        Assertions.assertEquals(listOf(2, 2, 2), PersistentSortedMap(mapOf(1 to 2, 2 to 2, 3 to 2), Int::compareTo).values)
    }

    @Test
    fun isEmpty() {
        Assertions.assertTrue(PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo).isEmpty())
        Assertions.assertFalse(PersistentSortedMap(mapOf(1 to 2, 1 to 3, 1 to 4), Int::compareTo).isEmpty())
    }

    @Test
    fun get() {
        Assertions.assertEquals(null, PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo)[1])
        Assertions.assertEquals(2, PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo)[1])
        Assertions.assertEquals(null, PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo)[7])
    }

    @Test
    fun selectKeys() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo).selectKeys(setOf(1, 2, 3)))
        Assertions.assertEquals(mapOf(1 to 2, 2 to 3), PersistentSortedMap(mapOf(1 to 2, 2 to 3), Int::compareTo).selectKeys(setOf(1, 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3), PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).selectKeys(setOf(1, 2))
        )
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).selectKeys(setOf(1, 4)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap(mapOf(1 to 2, 2 to 3), Int::compareTo).selectKeys(setOf(3, 4)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap(mapOf(1 to 2, 2 to 3), Int::compareTo).selectKeys(setOf()))
    }

    @Test
    fun containsValue() {
        Assertions.assertFalse(PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo).containsValue(1))
        Assertions.assertTrue(PersistentSortedMap(mapOf(1 to 1), Int::compareTo).containsValue(1))
        Assertions.assertTrue(PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).containsValue(3))
        Assertions.assertFalse(PersistentSortedMap(mapOf(1 to 2, 1 to 3, 1 to 4), Int::compareTo).containsValue(1))
    }

    @Test
    fun containsKey() {
        Assertions.assertFalse(PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo).containsKey(1))
        Assertions.assertTrue(PersistentSortedMap(mapOf(1 to 1), Int::compareTo).containsKey(1))
        Assertions.assertTrue(PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).containsKey(2))
        Assertions.assertFalse(PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).containsKey(99))
    }

    @Test
    fun contains() {
        Assertions.assertFalse(PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo).contains(1))
        Assertions.assertTrue(PersistentSortedMap(mapOf(1 to 1), Int::compareTo).contains(1))
        Assertions.assertTrue(PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).contains(2))
        Assertions.assertFalse(PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).contains(99))
    }

    @Test
    fun getOrDefault() {
        Assertions.assertEquals(88, PersistentSortedMap(mapOf<Int, Int>(), Int::compareTo).getOrDefault(1, 88))
        Assertions.assertEquals(2, PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).getOrDefault(1, 88))
        Assertions.assertEquals(88, PersistentSortedMap(mapOf(1 to 2, 2 to 3, 3 to 4), Int::compareTo).getOrDefault(7, 88))
    }

    @Test
    fun assocAll() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).assocAll(listOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).assocAll(listOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap(mapOf(1 to 2), Int::compareTo).assocAll(listOf(2 to 3, 3 to 4))
        )

        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).assocAll(mapOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).assocAll(mapOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap(mapOf(1 to 2), Int::compareTo).assocAll(mapOf(2 to 3, 3 to 4))
        )
    }

    @Test
    fun dissocAll() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).dissocAll(listOf(1)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).dissocAll(listOf(1)))
        Assertions.assertEquals(mapOf(3 to 4), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).dissocAll(listOf(1, 4)))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).dissocAll(listOf(4)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).dissocAll(listOf(3)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).dissocAll(listOf(1, 2, 3)))
    }

    @Test
    fun assoc() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).assoc(1, 2))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).assoc(1, 2))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap(mapOf(1 to 2), Int::compareTo).assoc(2, 3).assoc(3, 4)
        )

        val oldMap = PersistentSortedMap(1 to 2, 2 to 3, 3 to 4, comparator=Int::compareTo)
        val newMap = oldMap.assoc(5, 6)
        Assertions.assertNotSame(oldMap,newMap)
        Assertions.assertEquals(mapOf(1 to 2, 2 to 3, 3 to 4, 5 to 6),newMap)
        Assertions.assertEquals(mapOf(1 to 2, 2 to 3, 3 to 4),oldMap)

        val newMap2 = oldMap.assoc(1, 3)
        Assertions.assertNotSame(oldMap,newMap2)
        Assertions.assertEquals(mapOf(1 to 3, 2 to 3, 3 to 4),newMap2)
        Assertions.assertEquals(mapOf(1 to 2, 2 to 3, 3 to 4),oldMap)
    }

    @Test
    fun plusSingle() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).plus(1 to 2))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).plus(1 to 2))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap(mapOf(1 to 2), Int::compareTo).plus(2 to 3).plus(3 to 4)
        )
    }

    @Test
    fun plusMulti() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).plus(listOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).plus(listOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap(mapOf(1 to 2), Int::compareTo).plus(listOf(2 to 3, 3 to 4))
        )

        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).plus(mapOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).plus(mapOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap(mapOf(1 to 2), Int::compareTo).plus(mapOf(2 to 3, 3 to 4))
        )
    }

    @Test
    fun dissoc() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).dissoc(1))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).dissoc(1))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).dissoc(5))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).dissoc(4))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).dissoc(3))
    }

    @Test
    fun minusSingle() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).minus(1))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).minus(1))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).minus(5))
        Assertions.assertEquals(mapOf(3 to 4), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).minus(1).minus(4))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).minus(3))
    }

    @Test
    fun minusMulti() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo).minus(listOf(1)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap(mapOf(1 to 2), Int::compareTo).minus(listOf(1)))
        Assertions.assertEquals(mapOf(3 to 4), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).minus(listOf(1, 4)))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).minus(listOf(4)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).minus(listOf(3)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo).minus(listOf(1, 2, 3)))
    }

    @Test
    fun testEquals() {
        Assertions.assertEquals(PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo), mapOf<Int, Int>())
        Assertions.assertEquals(PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo), mapOf(1 to 2, 3 to 4))

        Assertions.assertNotEquals(PersistentSortedMap<Int, Int>(mapOf(), Int::compareTo), mapOf(1 to 2, 3 to 4))
        Assertions.assertNotEquals(PersistentSortedMap(mapOf(1 to 2, 3 to 4), Int::compareTo), mapOf<Int, Int>())
    }
    @Test
    fun getSizeWithoutComparator() {
        Assertions.assertEquals(0, PersistentSortedMap.from<Int, Int>(listOf()).size)
        Assertions.assertEquals(0, PersistentSortedMap.from<Int, Int>(mapOf()).size)

        Assertions.assertEquals(3, PersistentSortedMap.of(1 to 2, 3 to 4, 5 to 6).size)
        Assertions.assertEquals(3, PersistentSortedMap.from(listOf(1 to 2, 3 to 4, 5 to 6)).size)
        Assertions.assertEquals(3, PersistentSortedMap.from(mapOf(1 to 2, 3 to 4, 5 to 6)).size)

        Assertions.assertEquals(1, PersistentSortedMap.of(1 to 2, 1 to 3, 1 to 6).size)
        Assertions.assertEquals(1, PersistentSortedMap.from(listOf(1 to 2, 1 to 3, 1 to 6)).size)
        Assertions.assertEquals(1, PersistentSortedMap.from(mapOf(1 to 2, 1 to 3, 1 to 6)).size)
    }

    @Test
    fun iteratorWithoutComparator() {
        run {
            var size = 0
            for (e in PersistentSortedMap.from<Int, Int>(mapOf()))
                size++
            Assertions.assertEquals(0, size)
        }
        run {
            var size = 0
            for (e in PersistentSortedMap.from(mapOf(1 to 2, 1 to 4, 1 to 6)))
                size++
            Assertions.assertEquals(1, size)
        }
    }

    @Test
    fun sortedWithoutComparator() {
        val r = Random(0xC0FFEEBEE) // A bee in my coffee
        val list = (0..128).map {it to 0}.shuffled(r)
        val m = PersistentSortedMap.from(list)
        val listSorted = list.sortedBy { it.first }

        // Order is preserved.
        for ((index, entry) in m.withIndex()) {
            Assertions.assertEquals(listSorted[index].first , entry.key)
        }

        // Even when adding entries in a new order, the old order is preserved.
        for ((index, entry) in (m + list).withIndex()) {
            Assertions.assertEquals(listSorted[index].first , entry.key)
        }

        // After removing an element, the old order is preserved, without that element.
        val list1 = (list - list[55]).sortedBy { it.first }
        val m1 = m.dissoc(list[55].first)
        for ((index, entry) in m1.withIndex()) {
            Assertions.assertEquals(list1[index].first , entry.key)
        }
    }

    @Test
    fun getEntriesWithoutComparator() {
        Assertions.assertEquals(mapOf<Int, Int>().entries, PersistentSortedMap.from(mapOf<Int, Int>()).entries)
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4).entries, PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).entries)
    }

    @Test
    fun getKeysWithoutComparator() {
        Assertions.assertEquals(setOf<Int>(), PersistentSortedMap.from(mapOf<Int, Int>()).keys)
        Assertions.assertEquals(setOf(1), PersistentSortedMap.from(mapOf(1 to 2, 1 to 3, 1 to 4)).keys)
        Assertions.assertEquals(setOf(1, 2, 3), PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).keys)
    }

    @Test
    fun getValuesWithoutComparator() {
        Assertions.assertEquals(listOf<Int>(), PersistentSortedMap.from(mapOf<Int, Int>()).values)
        Assertions.assertEquals(listOf(2, 3, 4), PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).values)
        Assertions.assertEquals(listOf(2, 2, 2), PersistentSortedMap.from(mapOf(1 to 2, 2 to 2, 3 to 2)).values)
    }

    @Test
    fun isEmptyWithoutComparator() {
        Assertions.assertTrue(PersistentSortedMap.from(mapOf<Int, Int>()).isEmpty())
        Assertions.assertFalse(PersistentSortedMap.from(mapOf(1 to 2, 1 to 3, 1 to 4)).isEmpty())
    }

    @Test
    fun getWithoutComparator() {
        Assertions.assertEquals(null, PersistentSortedMap.from(mapOf<Int, Int>())[1])
        Assertions.assertEquals(2, PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4))[1])
        Assertions.assertEquals(null, PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4))[7])
    }

    @Test
    fun selectKeysWithoutComparator() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from(mapOf<Int, Int>()).selectKeys(setOf(1, 2, 3)))
        Assertions.assertEquals(mapOf(1 to 2, 2 to 3), PersistentSortedMap.from(mapOf(1 to 2, 2 to 3)).selectKeys(setOf(1, 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3), PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).selectKeys(setOf(1, 2))
        )
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).selectKeys(setOf(1, 4)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from(mapOf(1 to 2, 2 to 3)).selectKeys(setOf(3, 4)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from(mapOf(1 to 2, 2 to 3)).selectKeys(setOf()))
    }

    @Test
    fun containsValueWithoutComparator() {
        Assertions.assertFalse(PersistentSortedMap.from(mapOf<Int, Int>()).containsValue(1))
        Assertions.assertTrue(PersistentSortedMap.from(mapOf(1 to 1)).containsValue(1))
        Assertions.assertTrue(PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).containsValue(3))
        Assertions.assertFalse(PersistentSortedMap.from(mapOf(1 to 2, 1 to 3, 1 to 4)).containsValue(1))
    }

    @Test
    fun containsKeyWithoutComparator() {
        Assertions.assertFalse(PersistentSortedMap.from(mapOf<Int, Int>()).containsKey(1))
        Assertions.assertTrue(PersistentSortedMap.from(mapOf(1 to 1)).containsKey(1))
        Assertions.assertTrue(PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).containsKey(2))
        Assertions.assertFalse(PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).containsKey(99))
    }

    @Test
    fun containsWithoutComparator() {
        Assertions.assertFalse(PersistentSortedMap.from(mapOf<Int, Int>()).contains(1))
        Assertions.assertTrue(PersistentSortedMap.from(mapOf(1 to 1)).contains(1))
        Assertions.assertTrue(PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).contains(2))
        Assertions.assertFalse(PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).contains(99))
    }

    @Test
    fun getOrDefaultWithoutComparator() {
        Assertions.assertEquals(88, PersistentSortedMap.from(mapOf<Int, Int>()).getOrDefault(1, 88))
        Assertions.assertEquals(2, PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).getOrDefault(1, 88))
        Assertions.assertEquals(88, PersistentSortedMap.from(mapOf(1 to 2, 2 to 3, 3 to 4)).getOrDefault(7, 88))
    }

    @Test
    fun assocAllWithoutComparator() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from<Int, Int>(mapOf()).assocAll(listOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2)).assocAll(listOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap.from(mapOf(1 to 2)).assocAll(listOf(2 to 3, 3 to 4))
        )

        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from<Int, Int>(mapOf()).assocAll(mapOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2)).assocAll(mapOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap.from(mapOf(1 to 2)).assocAll(mapOf(2 to 3, 3 to 4))
        )
    }

    @Test
    fun dissocAllWithoutComparator() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from<Int, Int>(mapOf()).dissocAll(listOf(1)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from(mapOf(1 to 2)).dissocAll(listOf(1)))
        Assertions.assertEquals(mapOf(3 to 4), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).dissocAll(listOf(1, 4)))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).dissocAll(listOf(4)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).dissocAll(listOf(3)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).dissocAll(listOf(1, 2, 3)))
    }

    @Test
    fun assocWithoutComparator() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from<Int, Int>(mapOf()).assoc(1, 2))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2)).assoc(1, 2))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap.from(mapOf(1 to 2)).assoc(2, 3).assoc(3, 4)
        )

        val oldMap = PersistentSortedMap.from(listOf(1 to 2, 2 to 3, 3 to 4))
        val newMap = oldMap.assoc(5, 6)
        Assertions.assertNotSame(oldMap,newMap)
        Assertions.assertEquals(mapOf(1 to 2, 2 to 3, 3 to 4, 5 to 6),newMap)
        Assertions.assertEquals(mapOf(1 to 2, 2 to 3, 3 to 4),oldMap)

        val newMap2 = oldMap.assoc(1, 3)
        Assertions.assertNotSame(oldMap,newMap2)
        Assertions.assertEquals(mapOf(1 to 3, 2 to 3, 3 to 4),newMap2)
        Assertions.assertEquals(mapOf(1 to 2, 2 to 3, 3 to 4),oldMap)
    }

    @Test
    fun plusSingleWithoutComparator() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from<Int, Int>(mapOf()).plus(1 to 2))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2)).plus(1 to 2))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap.from(mapOf(1 to 2)).plus(2 to 3).plus(3 to 4)
        )
    }

    @Test
    fun plusMultiWithoutComparator() {
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from<Int, Int>(mapOf()).plus(listOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2)).plus(listOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap.from(mapOf(1 to 2)).plus(listOf(2 to 3, 3 to 4))
        )

        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from<Int, Int>(mapOf()).plus(mapOf(1 to 2)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2)).plus(mapOf(1 to 2)))
        Assertions.assertEquals(
            mapOf(1 to 2, 2 to 3, 3 to 4),
            PersistentSortedMap.from(mapOf(1 to 2)).plus(mapOf(2 to 3, 3 to 4))
        )
    }

    @Test
    fun dissocWithoutComparator() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from<Int, Int>(mapOf()).dissoc(1))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from(mapOf(1 to 2)).dissoc(1))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).dissoc(5))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).dissoc(4))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).dissoc(3))
    }

    @Test
    fun minusSingleWithoutComparator() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from<Int, Int>(mapOf()).minus(1))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from(mapOf(1 to 2)).minus(1))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).minus(5))
        Assertions.assertEquals(mapOf(3 to 4), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).minus(1).minus(4))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).minus(3))
    }

    @Test
    fun minusMultiWithoutComparator() {
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from<Int, Int>(mapOf()).minus(listOf(1)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from(mapOf(1 to 2)).minus(listOf(1)))
        Assertions.assertEquals(mapOf(3 to 4), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).minus(listOf(1, 4)))
        Assertions.assertEquals(mapOf(1 to 2, 3 to 4), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).minus(listOf(4)))
        Assertions.assertEquals(mapOf(1 to 2), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).minus(listOf(3)))
        Assertions.assertEquals(mapOf<Int, Int>(), PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)).minus(listOf(1, 2, 3)))
    }

    @Test
    fun testEqualsWithoutComparator() {
        Assertions.assertEquals(PersistentSortedMap.from<Int, Int>(mapOf()), mapOf<Int, Int>())
        Assertions.assertEquals(PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)), mapOf(1 to 2, 3 to 4))

        Assertions.assertNotEquals(PersistentSortedMap.from<Int, Int>(mapOf()), mapOf(1 to 2, 3 to 4))
        Assertions.assertNotEquals(PersistentSortedMap.from(mapOf(1 to 2, 3 to 4)), mapOf<Int, Int>())
    }
}
