package de.bogin.onitamaengine.board

data class BoardConfiguration(
    val width:Int,
    val height:Int,
    val blueTemple:Pair<Int, Int>,
    val redTemple:Pair<Int, Int>
)
