package main.de.bogin.onitamaengine.extensions

fun Pair<Int,Int>.plus(other:Pair<Int,Int>): Pair<Int, Int> {
    return Pair(this.first + other.first, this.second + other.second)
}