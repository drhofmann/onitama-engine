package de.bogin.onitamaengine.domain.movement

import de.bogin.onitamaengine.domain.board.PlayerColor

data class Move (val startSquare:Pair<Int,Int>, val destinationSquare:Pair<Int,Int>, val activePlayer: PlayerColor, val movementCard: MovementCard)