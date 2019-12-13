package de.bogin.onitamaengine.movement

import de.bogin.onitamaengine.board.PlayerColor

data class Move (val startSquare:Pair<Int,Int>, val destinationSquare:Pair<Int,Int>, val activePlayer: PlayerColor)