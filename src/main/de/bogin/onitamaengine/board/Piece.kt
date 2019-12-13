package de.bogin.onitamaengine.board

data class Piece (var square: Pair<Int,Int>, val owner: PlayerColor, val type: PieceType)