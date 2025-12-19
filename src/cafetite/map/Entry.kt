package cafetite.map

internal data class Entry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V> {
    override fun toString(): String = "($key, $value)"
}