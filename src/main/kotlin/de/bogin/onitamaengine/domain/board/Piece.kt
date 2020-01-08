package de.bogin.onitamaengine.domain.board

data class Piece (var square: Pair<Int,Int>, val owner: PlayerColor, val type: PieceType)