package de.bogin.onitamaengine

import de.bogin.onitamaengine.domain.board.*
import de.bogin.onitamaengine.domain.game.Game
import de.bogin.onitamaengine.domain.game.StrategyRandomMove
import de.bogin.onitamaengine.domain.movement.Move
import de.bogin.onitamaengine.domain.movement.MovementCard
import io.javalin.http.Context

object GameController {
    private val strategy = StrategyRandomMove()
    private val blueMovementCards = setOf(MovementCard.MANTIS, MovementCard.MONKEY)
    private val redMovementCards = setOf(MovementCard.COBRA, MovementCard.CRANE)
    private val neutralMovementCard = setOf(MovementCard.EEL)
    private val movementCards = mapOf(PlayerColor.RED to redMovementCards, PlayerColor.NEUTRAL to neutralMovementCard, PlayerColor.BLUE to blueMovementCards)
    private val game = Game(strategy, movementCards)

    fun getGamePosition(ctx:Context) {
        ctx.json(game.position.getBoardWithPieces())
    }

    fun postMove(ctx: Context) {
        game.position.doMove(Move(startSquare = Pair(1,1),activePlayer = PlayerColor.RED, movementCard = MovementCard.OX, destinationSquare = Pair(2,2)))
        ctx.json(game.position.pieces)
    }

}