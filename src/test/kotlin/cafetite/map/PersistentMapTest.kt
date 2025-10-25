package cafetite.map

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

class PersistentMapTest {

    @Test
    fun testOf() {
        Assertions.assertInstanceOf(PersistentArrayMap::class.java, PersistentMap.of<Int, Int>())
        Assertions.assertInstanceOf(PersistentArrayMap::class.java, PersistentMap.of(1 to 2, 3 to 4, 5 to 6))
        Assertions.assertInstanceOf(
            PersistentHashMap::class.java,
            PersistentMap.of(
                0 to 0, 1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5, 6 to 6, 7 to 7, 8 to 8, 9 to 9, 10 to 10,
                11 to 11, 12 to 12, 13 to 13, 14 to 14, 15 to 15, 16 to 16, 17 to 17, 18 to 18, 19 to 19, 20 to 20,
                21 to 21, 22 to 22, 23 to 23, 24 to 24, 25 to 25, 26 to 26, 27 to 27, 28 to 28, 29 to 29, 30 to 30,
                31 to 31, 32 to 32, 33 to 33, 34 to 34, 35 to 35, 36 to 36, 37 to 37, 38 to 38, 39 to 39, 40 to 40,
                41 to 41, 42 to 42, 43 to 43, 44 to 44, 45 to 45, 46 to 46, 47 to 47, 48 to 48, 49 to 49, 50 to 50,
                51 to 51, 52 to 52, 53 to 53, 54 to 54, 55 to 55, 56 to 56, 57 to 57, 58 to 58, 59 to 59, 60 to 60,
                61 to 61, 62 to 62, 63 to 63, 64 to 64, 65 to 65, 66 to 66, 67 to 67, 68 to 68, 69 to 69, 70 to 70,
                71 to 71, 72 to 72, 73 to 73, 74 to 74, 75 to 75, 76 to 76, 77 to 77, 78 to 78, 79 to 79, 80 to 80,
                81 to 81, 82 to 82, 83 to 83, 84 to 84, 85 to 85, 86 to 86, 87 to 87, 88 to 88, 89 to 89, 90 to 90,
                91 to 91, 92 to 92, 93 to 93, 94 to 94, 95 to 95, 96 to 96, 97 to 97, 98 to 98, 99 to 99, 100 to 100
            )
        )
    }

    @Test
    fun testFrom() {
        Assertions.assertInstanceOf(PersistentArrayMap::class.java, PersistentMap.from<Int, Int>(setOf()))
        Assertions.assertInstanceOf(PersistentArrayMap::class.java, PersistentMap.from(setOf(1 to 2, 3 to 4, 5 to 6)))

        val lotsOfPairs = (0..100).zip(0..100)
        Assertions.assertInstanceOf(PersistentHashMap::class.java, PersistentMap.from(lotsOfPairs))
    }
}