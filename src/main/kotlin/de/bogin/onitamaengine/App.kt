package de.bogin.onitamaengine

import de.bogin.onitamaengine.domain.board.PlayerColor
import de.bogin.onitamaengine.domain.game.Game
import de.bogin.onitamaengine.domain.game.StrategyRandomMove
import de.bogin.onitamaengine.domain.movement.MovementCard


fun main(args: Array<String>) {
    val strategy = StrategyRandomMove()
    val blueMovementCards = setOf(MovementCard.MANTIS, MovementCard.MONKEY)
    val redMovementCards = setOf(MovementCard.COBRA, MovementCard.CRANE)
    val neutralMovementCard = setOf(MovementCard.EEL)
    val movementCards = mapOf(PlayerColor.RED to redMovementCards, PlayerColor.NEUTRAL to neutralMovementCard, PlayerColor.BLUE to blueMovementCards)

    val game = Game(strategy, movementCards)
    game.play()
}


