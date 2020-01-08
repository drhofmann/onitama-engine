package de.bogin.onitamaengine.domain.game

import de.bogin.onitamaengine.domain.board.Position
import de.bogin.onitamaengine.domain.movement.Move

interface Strategy {
    fun getMove(position: Position): Move
}