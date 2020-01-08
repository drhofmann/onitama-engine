package de.bogin.onitamaengine.domain.game

import de.bogin.onitamaengine.domain.board.Position
import de.bogin.onitamaengine.domain.movement.Move

class StrategyRandomMove :Strategy {
    override fun getMove(position: Position): Move {
        val validMoves = position.generateValidMoves()
        return validMoves.random()
    }

}