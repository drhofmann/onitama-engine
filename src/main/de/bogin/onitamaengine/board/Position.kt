package de.bogin.onitamaengine.board

import de.bogin.onitamaengine.extensions.minus
import de.bogin.onitamaengine.movement.Move
import de.bogin.onitamaengine.extensions.plus
import de.bogin.onitamaengine.movement.MoveVector
import de.bogin.onitamaengine.movement.MovementCard
import java.util.stream.Collectors

class Position (
    val boardConfiguration: BoardConfiguration,
    val movementOptions: Map<PlayerColor, List<MovementCard>>,
    var activePlayer: PlayerColor,
    val pieces: Map<PlayerColor, List<Piece>>
) {

    fun generateValidMoves():List<Move> {
        val activePlayerPieces = pieces[activePlayer]
        val moves = mutableListOf<Move>()
        activePlayerPieces?.forEach {
            moves.addAll(generateMovesForOnePiece(it))
        }
        return moves.filter { isMoveAllowed(it) }
    }

    fun generateMovesForOnePiece(piece: Piece):List<Move> {
        val movesForOnePiece = mutableListOf<Move>()
        movementOptions[piece.owner]?.forEach{movementCard ->
            movementCard.movementVectors.forEach{moveVector ->
                movesForOnePiece.add(createMoveConsideringPlayerColor(piece, moveVector))
            }
        }
        return movesForOnePiece
    }

    fun createMoveConsideringPlayerColor(piece: Piece, moveVector: MoveVector):Move {
        val startSquare = piece.square
        val destinationSquare:Pair<Int,Int> = if (piece.owner == PlayerColor.RED) {
            piece.square.plus(moveVector.relativeTargetCoordinates)
        } else {
            piece.square.minus(moveVector.relativeTargetCoordinates)
        }
        return Move(startSquare, destinationSquare, piece.owner)
    }

    fun isMoveAllowed(move: Move):Boolean {
        return isDestinationFieldOfMoveOnBoard(move.destinationSquare)
                && !isDestinationFieldOccupiedByOwnPiece(move.destinationSquare, move.activePlayer)
    }

    private fun isDestinationFieldOfMoveOnBoard(destinationSquare: Pair<Int, Int>):Boolean {
        return destinationSquare.first in IntRange(1,boardConfiguration.width) && destinationSquare.second in IntRange(1,boardConfiguration.height)
    }

    private fun isDestinationFieldOccupiedByOwnPiece(destinationSquare: Pair<Int, Int>, activePlayer: PlayerColor):Boolean {
        val activePlayerPieces = pieces[activePlayer]
        if (activePlayerPieces != null) {
            return activePlayerPieces.stream().map { it.square }.collect(Collectors.toSet())
                .contains(destinationSquare)
        }
        return false
    }

}

