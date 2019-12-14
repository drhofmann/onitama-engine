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

    fun doMove(move: Move):Position {
        val newMovementOptions = updateMovementCards(move)
        val newPieceConfiguration = updatePieces(move)
        return Position(boardConfiguration, newMovementOptions, swapActivePlayer(), newPieceConfiguration)
    }


    private fun updateMovementCards(move: Move): Map<PlayerColor, List<MovementCard>> {
        val result = mutableMapOf<PlayerColor, List<MovementCard>>()
        result[PlayerColor.NEUTRAL] = listOf(move.movementCard)
        result[swapActivePlayer()] = movementOptions[swapActivePlayer()]!!
        result[activePlayer] = movementOptions[activePlayer]?.minus(move.movementCard)?.plus(movementOptions[PlayerColor.NEUTRAL]!!)!!
        return result
    }

    private fun updatePieces(move: Move): Map<PlayerColor, List<Piece>> {
        val result = mutableMapOf<PlayerColor, List<Piece>>()
        result[swapActivePlayer()] = removePossibleOpposingPieceOnDestinationSquare(move.destinationSquare)
        result[activePlayer] = updatePieceThatMoved(move)
        return result
    }


    private fun removePossibleOpposingPieceOnDestinationSquare(destinationSquare: Pair<Int, Int>): List<Piece> {
        val opposingPieces = pieces[swapActivePlayer()]!!
        return opposingPieces.stream()
            .filter { it.square != destinationSquare}.collect(Collectors.toList())
    }

    private fun updatePieceThatMoved(move: Move): List<Piece> {
        val activePlayerPieces = pieces[activePlayer]!!
        activePlayerPieces.stream().filter{it.square==move.startSquare}.findFirst().get().square = move.destinationSquare
        return activePlayerPieces
    }

    private fun swapActivePlayer():PlayerColor {
        return if (activePlayer == PlayerColor.BLUE) PlayerColor.RED
        else PlayerColor.BLUE
    }

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
                movesForOnePiece.add(createMoveConsideringPlayerColor(piece, moveVector, movementCard))
            }
        }
        return movesForOnePiece
    }

    fun createMoveConsideringPlayerColor(piece: Piece, moveVector: MoveVector, movementCard: MovementCard):Move {
        val startSquare = piece.square
        val destinationSquare:Pair<Int,Int> = if (piece.owner == PlayerColor.RED) {
            piece.square.plus(moveVector.relativeTargetCoordinates)
        } else {
            piece.square.minus(moveVector.relativeTargetCoordinates)
        }
        return Move(startSquare, destinationSquare, piece.owner, movementCard)
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

