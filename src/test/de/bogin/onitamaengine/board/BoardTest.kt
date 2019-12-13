package de.bogin.onitamaengine.board

import de.bogin.onitamaengine.movement.Move
import main.de.bogin.onitamaengine.board.BoardConfiguration
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class BoardTest {
    private lateinit var standardBoard:Board

    @Test
    fun testDestinationFieldOfMoveNotOnBoardUpperBound() {
        initBoard()
        createMove(Pair(6,5))
        assertFalse(standardBoard.isDestinationFieldOfMoveOnBoard(Pair(6,5)))
    }

    @Test
    fun testDestinationFieldOfMoveNotOnBoardLowerBound() {
        initBoard()
        assertFalse(standardBoard.isDestinationFieldOfMoveOnBoard(Pair(0,-1)))
    }

    @Test
    fun testDestinationFieldOfMoveOnBoard() {
        initBoard()
        assertTrue(standardBoard.isDestinationFieldOfMoveOnBoard(Pair(3,4)))
    }

    @Test
    fun testDestinationFieldOccupiedByOwnBlueMaster() {
        initBoard()
        assertTrue(standardBoard.isDestinationFieldOccupiedByOwnPiece(Pair(3,5), PlayerColor.BLUE))
    }

    @Test
    fun testDestinationFieldOccupiedByOwnRedStudent() {
        initBoard()
        assertTrue(standardBoard.isDestinationFieldOccupiedByOwnPiece(Pair(4,1), PlayerColor.RED))
    }

    @Test
    fun testDestinationFieldOccupiedByOpposingRedMaster() {
        initBoard()
        assertFalse(standardBoard.isDestinationFieldOccupiedByOwnPiece(Pair(3,1), PlayerColor.BLUE))
    }

    @Test
    fun testDestinationFieldOccupiedByOpposingBlueStudent() {
        initBoard()
        assertFalse(standardBoard.isDestinationFieldOccupiedByOwnPiece(Pair(2,5), PlayerColor.RED))
    }

    @Test
    fun testDestinationFieldIsEmpty() {
        initBoard()
        assertFalse(standardBoard.isDestinationFieldOccupiedByOwnPiece(Pair(4,3), PlayerColor.RED))
    }


    private fun initBoard() {
        var boardConfiguration = BoardConfiguration(5,5,Pair(3,5),Pair(3,1))
        standardBoard =  Board (
            blueMaster = boardConfiguration.blueTemple,
            blueStudents = listOf(Pair(1,5), Pair(2,5), Pair(4,5), Pair(5,5)),
            redMaster = boardConfiguration.redTemple,
            redStudents = listOf(Pair(1,1), Pair(2,1), Pair(4,1), Pair(5,1)),
            boardConfiguration = boardConfiguration,
            movementOptions = mapOf(),
            activePlayer = PlayerColor.RED
        )
    }

    private fun createMove(destinationSquare:Pair<Int,Int>, activePlayer:PlayerColor = PlayerColor.BLUE):Move {
        return Move(Pair(3,3), destinationSquare, activePlayer)
    }

}