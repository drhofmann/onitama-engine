package de.bogin.onitamaengine.board

import de.bogin.onitamaengine.movement.Move
import main.de.bogin.onitamaengine.board.BoardConfiguration
import main.de.bogin.onitamaengine.extensions.plus
import main.de.bogin.onitamaengine.movement.MoveVector
import main.de.bogin.onitamaengine.movement.MovementCard

class Board (
    val blueMaster:Pair<Int,Int>,
    val blueStudents:List<Pair<Int,Int>>,
    val redMaster:Pair<Int,Int>,
    val redStudents:List<Pair<Int,Int>>,
    val boardConfiguration: BoardConfiguration,
    val movementOptions: Map<PlayerColor, List<MovementCard>>,
    val activePlayer: PlayerColor
) {


    fun createMove(startSquare:Pair<Int,Int>, moveVector: MoveVector, activePlayer: PlayerColor):Move {
        val destinationSquare = startSquare.plus(moveVector.relativeTargetCoordinates)
        return Move(startSquare, destinationSquare, activePlayer)
    }

    fun isMoveAllowed(move: Move):Boolean {
        return isDestinationFieldOfMoveOnBoard(move.destinationSquare)
                && !isDestinationFieldOccupiedByOwnPiece(move.destinationSquare, move.activePlayer)
    }

    fun isDestinationFieldOfMoveOnBoard(destinationSquare: Pair<Int, Int>):Boolean {
        return destinationSquare.first in IntRange(1,boardConfiguration.width) && destinationSquare.second in IntRange(1,boardConfiguration.height)
    }

    fun isDestinationFieldOccupiedByOwnPiece(destinationSquare: Pair<Int, Int>, activePlayer: PlayerColor):Boolean {
        if (activePlayer == PlayerColor.BLUE) {
            return blueMaster == destinationSquare || blueStudents.contains(destinationSquare)
        }
        return redMaster == destinationSquare || redStudents.contains(destinationSquare)
    }

}

