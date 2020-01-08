package de.bogin.onitamaengine.domain.board

import de.bogin.onitamaengine.domain.extensions.minus
import de.bogin.onitamaengine.domain.movement.Move
import de.bogin.onitamaengine.domain.extensions.plus
import de.bogin.onitamaengine.domain.movement.MoveVector
import de.bogin.onitamaengine.domain.movement.MovementCard
import java.util.stream.Collectors

class Position(
        val boardConfiguration: BoardConfiguration,
        val movementOptions: Map<PlayerColor, Set<MovementCard>>,
        var activePlayer: PlayerColor,
        val pieces: Map<PlayerColor, Set<Piece>>
) {

    fun doMove(move: Move): Position {
        validateMove(move)
        val newMovementOptions = updateMovementCards(move)
        val newPieceConfiguration = updatePieces(move)
        return Position(boardConfiguration, newMovementOptions, opposingPlayer(), newPieceConfiguration)
    }

    private fun validateMove(move: Move) {
        if (move.activePlayer != activePlayer) {
            throw IncorrectBoardStateException("A move by ${move.activePlayer} would be made but it is $activePlayer's turn")
        }
        if (!movementOptions[activePlayer]!!.contains(move.movementCard)) {
            throw IncorrectBoardStateException("A move with ${move.movementCard} would be made but active player $activePlayer has following cards: ${movementOptions[move.activePlayer]}")
        }
        if (!pieces[activePlayer]!!.map{it.square}.contains(move.startSquare)) {
            throw IncorrectBoardStateException("A move with a ${move.activePlayer} piece would be made but there is no piece of that player on square ${move.startSquare}. There are pieces on: ${pieces[activePlayer]!!.map { it.square }}"
            )
        }
        if (pieces[activePlayer]!!.map{it.square}.contains(move.destinationSquare)) {
            throw IncorrectBoardStateException("A move to ${move.destinationSquare} would be made but there is already a piece of player ${activePlayer}. There are pieces on: ${pieces[move.activePlayer]!!.map { it.square }}")
        }
    }


    private fun updateMovementCards(move: Move): Map<PlayerColor, Set<MovementCard>> {
        val result = mutableMapOf<PlayerColor, Set<MovementCard>>()
        result[PlayerColor.NEUTRAL] = setOf(move.movementCard)
        result[opposingPlayer()] = movementOptions[opposingPlayer()]!!
        result[activePlayer] = movementOptions[activePlayer]?.minus(move.movementCard)?.plus(movementOptions[PlayerColor.NEUTRAL]!!)!!
        return result
    }

    private fun updatePieces(move: Move): Map<PlayerColor, Set<Piece>> {
        val result = mutableMapOf<PlayerColor, Set<Piece>>()
        result[opposingPlayer()] = removePossibleOpposingPieceOnDestinationSquare(move.destinationSquare)
        result[activePlayer] = updatePieceThatMoved(move)
        return result
    }


    private fun removePossibleOpposingPieceOnDestinationSquare(destinationSquare: Pair<Int, Int>): Set<Piece> {
        val opposingPieces = pieces[opposingPlayer()]!!
        return opposingPieces.stream()
            .filter { it.square != destinationSquare}.collect(Collectors.toSet())
    }

    private fun updatePieceThatMoved(move: Move): Set<Piece> {
        val activePlayerPieces = pieces[activePlayer]!!
        activePlayerPieces.stream().filter{it.square==move.startSquare}.findFirst().get().square = move.destinationSquare
        return activePlayerPieces
    }

    fun opposingPlayer(): PlayerColor {
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

    fun createMoveConsideringPlayerColor(piece: Piece, moveVector: MoveVector, movementCard: MovementCard): Move {
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

    fun activePlayerHasLost(): Boolean {
        return ownTempleIsLost() || ownMasterIsLost()
    }

    private fun ownTempleIsLost():Boolean {
        return pieces[opposingPlayer()]!!.map{it.square}.contains(boardConfiguration.temples[activePlayer])
    }

    private fun ownMasterIsLost():Boolean {
        return pieces[activePlayer]!!.filter { it.type == PieceType.MASTER }.isEmpty()
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

    fun print() {
        var board = getEmptyBoard()
        board = addPieces(board)
        println(" BLUE: ${movementOptions[PlayerColor.BLUE]}")
        printBoard(board)
        println(" RED: ${movementOptions[PlayerColor.RED]}")
        println(" NEUTRAL: ${movementOptions[PlayerColor.NEUTRAL]}")
    }

    fun getBoardWithPieces(): Array<Array<String>> {
        var board = getEmptyBoard()
        board = addPieces(board)
        return board
    }

    private fun getEmptyBoard(): Array<Array<String>> {
        return Array(5){Array(5) {" "} }
    }

    private fun addPieces(board: Array<Array<String>>): Array<Array<String>> {
        pieces[PlayerColor.RED]!!.filter { it.type== PieceType.STUDENT }.forEach {
            board[5-it.square.second][it.square.first-1] = "S"
        }
        pieces[PlayerColor.RED]!!.filter { it.type== PieceType.MASTER }.forEach {
            board[5-it.square.second][it.square.first-1] = "M"
        }
        pieces[PlayerColor.BLUE]!!.filter { it.type== PieceType.STUDENT }.forEach {
            board[5-it.square.second][it.square.first-1] = "s"
        }
        pieces[PlayerColor.BLUE]!!.filter { it.type== PieceType.MASTER }.forEach {
            board[5-it.square.second][it.square.first-1] = "m"
        }
        return board
    }

    private fun printBoard(board: Array<Array<String>>) {
        println("+---------+")
        board.forEach { row ->
            var rowString = "|"
            row.forEach {char ->
                rowString += char
                rowString += " "
            }
            rowString = rowString.subSequence(0, rowString.length-1).toString() + "|"
            println(rowString)
        }
        println("+---------+")
    }

}

