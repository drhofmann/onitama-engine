package de.bogin.onitamaengine.board

import de.bogin.onitamaengine.extensions.minus
import de.bogin.onitamaengine.movement.Move
import de.bogin.onitamaengine.extensions.plus
import de.bogin.onitamaengine.movement.MoveVector
import de.bogin.onitamaengine.movement.MovementCard
import java.util.stream.Collectors

class Position(
    val boardConfiguration: BoardConfiguration,
    val movementOptions: Map<PlayerColor, Set<MovementCard>>,
    var activePlayer: PlayerColor,
    val pieces: Map<PlayerColor, Set<Piece>>
) {

    fun doMove(move: Move):Position {
        validateMove(move)
        val newMovementOptions = updateMovementCards(move)
        val newPieceConfiguration = updatePieces(move)
        return Position(boardConfiguration, newMovementOptions, swapActivePlayer(), newPieceConfiguration)
    }

    private fun validateMove(move: Move) {
        if (move.activePlayer != activePlayer) {
            throw IncorrectBoardStateException("A move by ${move.activePlayer} would be made but it is $activePlayer's turn")
        }
        if (!movementOptions[activePlayer]!!.contains(move.movementCard)) {
            throw IncorrectBoardStateException("A move with ${move.movementCard} would be made but active player $activePlayer has following cards: ${movementOptions[move.activePlayer]}")
        }
        if (!pieces[activePlayer]!!.map{it.square}.contains(move.startSquare)) {
            throw IncorrectBoardStateException("A move with a ${move.activePlayer} piece would be made but there is no piece of that player on square ${move.startSquare}. There are pieces on: ${pieces[activePlayer]!!.map{it.square}}"
            )
        }
        if (pieces[activePlayer]!!.map{it.square}.contains(move.destinationSquare)) {
            throw IncorrectBoardStateException("A move to ${move.destinationSquare} would be made but there is already a piece of player ${activePlayer}. There are pieces on: ${pieces[move.activePlayer]!!.map{it.square}}")
        }
    }


    private fun updateMovementCards(move: Move): Map<PlayerColor, Set<MovementCard>> {
        val result = mutableMapOf<PlayerColor, Set<MovementCard>>()
        result[PlayerColor.NEUTRAL] = setOf(move.movementCard)
        result[swapActivePlayer()] = movementOptions[swapActivePlayer()]!!
        result[activePlayer] = movementOptions[activePlayer]?.minus(move.movementCard)?.plus(movementOptions[PlayerColor.NEUTRAL]!!)!!
        return result
    }

    private fun updatePieces(move: Move): Map<PlayerColor, Set<Piece>> {
        val result = mutableMapOf<PlayerColor, Set<Piece>>()
        result[swapActivePlayer()] = removePossibleOpposingPieceOnDestinationSquare(move.destinationSquare)
        result[activePlayer] = updatePieceThatMoved(move)
        return result
    }


    private fun removePossibleOpposingPieceOnDestinationSquare(destinationSquare: Pair<Int, Int>): Set<Piece> {
        val opposingPieces = pieces[swapActivePlayer()]!!
        return opposingPieces.stream()
            .filter { it.square != destinationSquare}.collect(Collectors.toSet())
    }

    private fun updatePieceThatMoved(move: Move): Set<Piece> {
        val activePlayerPieces = pieces[activePlayer]!!
        activePlayerPieces.stream().filter{it.square==move.startSquare}.findFirst().get().square = move.destinationSquare
        return activePlayerPieces
    }

    private fun swapActivePlayer():PlayerColor {
        return if (activePlayer == PlayerColor.BLUE) PlayerColor.RED
        else PlayerColor.BLUE
    }

    fun generateValidMoves():Set<Move> {
        val activePlayerPieces = pieces[activePlayer]
        val moves = mutableSetOf<Move>()
        activePlayerPieces?.forEach {
            moves.addAll(generateMovesForOnePiece(it))
        }
        return moves.filter { isMoveAllowed(it) }.toSet()
    }

    fun generateMovesForOnePiece(piece: Piece):Set<Move> {
        val movesForOnePiece = mutableSetOf<Move>()
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

