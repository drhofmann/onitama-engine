package de.bogin.onitamaengine.domain.board

data class BoardConfiguration(
    val width:Int,
    val height:Int,
    val temples: Map<PlayerColor, Pair<Int,Int>>
)
