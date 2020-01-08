package de.bogin.onitamaengine.domain.game

import de.bogin.onitamaengine.domain.board.*
import de.bogin.onitamaengine.domain.movement.Move
import de.bogin.onitamaengine.domain.movement.MovementCard

class Game (private val strategy: Strategy, movementCards: Map<PlayerColor, Set<MovementCard>>){

    var position = initializeGame(movementCards)

    fun play() {
        while(true) {
            val move = getMove(position)
            printInformationAboutMove(position, move)
            position = position.doMove(move)
            if (position.activePlayerHasLost()) {
                break
            }
        }
        println("Player ${position.opposingPlayer()} has won!")
    }

    private fun getMove(position: Position): Move {
        return if (position.activePlayer == PlayerColor.RED) {
            getMoveByHumanPlayer(position)
        } else {
            getMoveByComputerPlayer(position)
        }
    }

    private fun getMoveByHumanPlayer(position: Position): Move {
        position.print()
        val input = readLine()!!
        return parseMove(input)
    }

    private fun getMoveByComputerPlayer(position: Position): Move {
        return strategy.getMove(position)
    }

    private fun parseMove(input: String): Move {
        val split = input.split(",")
        val startSquare = Pair(split[0].toInt(),split[1].toInt())
        val destinationSquare = Pair(split[2].toInt(),split[3].toInt())
        val movementCard = MovementCard.valueOf(split[4])
        return Move(startSquare = startSquare, destinationSquare = destinationSquare, movementCard = movementCard, activePlayer = PlayerColor.RED)
    }

    private fun printInformationAboutMove(position: Position, move: Move) {
        println("${position.activePlayer} plays the move ${move.movementCard} : ${move.startSquare} -> ${move.destinationSquare}")
        if (position.pieces[position.opposingPlayer()]!!.map{it.square}.contains(move.destinationSquare)) {
            println("A ${position.opposingPlayer()} piece has been captured")
        }
    }

    private fun initializeGame(movementCards: Map<PlayerColor, Set<MovementCard>>): Position {
        val boardConfiguration = BoardConfiguration(5, 5, mapOf(PlayerColor.RED to Pair(3, 1), PlayerColor.BLUE to Pair(3, 5)))
        val bluePieces = setOf(
                Piece(Pair(3, 5), PlayerColor.BLUE, PieceType.MASTER),
                Piece(Pair(1, 5), PlayerColor.BLUE, PieceType.STUDENT),
                Piece(Pair(2, 5), PlayerColor.BLUE, PieceType.STUDENT),
                Piece(Pair(4, 5), PlayerColor.BLUE, PieceType.STUDENT),
                Piece(Pair(5, 5), PlayerColor.BLUE, PieceType.STUDENT)
        )
        val redPieces = setOf(
                Piece(Pair(3, 1), PlayerColor.RED, PieceType.MASTER),
                Piece(Pair(1, 1), PlayerColor.RED, PieceType.STUDENT),
                Piece(Pair(2, 1), PlayerColor.RED, PieceType.STUDENT),
                Piece(Pair(4, 1), PlayerColor.RED, PieceType.STUDENT),
                Piece(Pair(5, 1), PlayerColor.RED, PieceType.STUDENT)
        )
        return Position(
                boardConfiguration = boardConfiguration,
                movementOptions = movementCards,
                activePlayer = PlayerColor.RED,
                pieces = mapOf(PlayerColor.BLUE to bluePieces, PlayerColor.RED to redPieces)
        )
    }
}