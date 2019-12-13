package de.bogin.onitamaengine.board

import de.bogin.onitamaengine.extensions.minus
import de.bogin.onitamaengine.extensions.plus
import de.bogin.onitamaengine.movement.Move
import de.bogin.onitamaengine.movement.CobraCard
import de.bogin.onitamaengine.movement.MantisCard
import de.bogin.onitamaengine.movement.MoveVector
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class PositionTest {
    private lateinit var standardPosition:Position

    @Test
    fun testDestinationFieldOfMoveNotOnBoardUpperBound() {
        initBoard()
        assertFalse(standardPosition.isMoveAllowed(createMove(Pair(6,5))))
    }

    @Test
    fun testDestinationFieldOfMoveNotOnBoardLowerBound() {
        initBoard()
        assertFalse(standardPosition.isMoveAllowed(createMove(Pair(0,-1))))
    }

    @Test
    fun testDestinationFieldOfMoveOnBoard() {
        initBoard()
        assertTrue(standardPosition.isMoveAllowed(createMove(Pair(3,4))))
    }

    @Test
    fun testDestinationFieldOccupiedByOwnBlueMaster() {
        initBoard()
        assertFalse(standardPosition.isMoveAllowed(createMove(Pair(3,5), PlayerColor.BLUE)))
    }

    @Test
    fun testDestinationFieldOccupiedByOwnRedStudent() {
        initBoard()
        assertFalse(standardPosition.isMoveAllowed(createMove(Pair(4,1), PlayerColor.RED)))
    }

    @Test
    fun testDestinationFieldOccupiedByOpposingRedMaster() {
        initBoard()
        assertTrue(standardPosition.isMoveAllowed(createMove(Pair(3,1), PlayerColor.BLUE)))
    }

    @Test
    fun testDestinationFieldOccupiedByOpposingBlueStudent() {
        initBoard()
        assertTrue(standardPosition.isMoveAllowed(createMove(Pair(2,5), PlayerColor.RED)))
    }

    @Test
    fun testDestinationFieldIsEmpty() {
        initBoard()
        assertTrue(standardPosition.isMoveAllowed(createMove(Pair(4,3), PlayerColor.RED)))
    }

    @Test
    fun testCreateMoveConsideringPlayerColorForBLuePlayer() {
        initBoard()
        val piece = Piece(square=Pair(3,3), owner = PlayerColor.BLUE, type = PieceType.MASTER)
        val coordinateIncrement = Pair(1,2)
        val move = standardPosition.createMoveConsideringPlayerColor(piece, MoveVector(relativeTargetCoordinates = coordinateIncrement))
        assertThat(move, equalTo(Move(startSquare = piece.square, destinationSquare = piece.square.minus(coordinateIncrement), activePlayer = piece.owner)))
    }

    @Test
    fun testGenerateCobraMovesForRedMaster() {
        initBoard()
        val moves = mutableListOf<Move>()
        val redMaster = standardPosition.pieces[PlayerColor.RED]!!.filter { it.owner==PlayerColor.RED && it.type==PieceType.MASTER}.first()
        moves.add(Move(startSquare = redMaster.square, destinationSquare = redMaster.square.plus(Pair(-1,0)), activePlayer = redMaster.owner))
        moves.add(Move(startSquare = redMaster.square, destinationSquare = redMaster.square.plus(Pair(1,-1)), activePlayer = redMaster.owner))
        moves.add(Move(startSquare = redMaster.square, destinationSquare = redMaster.square.plus(Pair(1,1)), activePlayer = redMaster.owner))
        val expectedMoves = moves.toList()
        val createdMoves = standardPosition.generateMovesForOnePiece(redMaster)
        assertThat(createdMoves, equalTo(expectedMoves))
    }

    @Test
    fun testGenerateValidMoveForBluePlayer() {
        initBoard()
        standardPosition.activePlayer = PlayerColor.BLUE
        val moves = mutableListOf<Move>()
        standardPosition.pieces[PlayerColor.BLUE]!!.forEach {
            moves.add(Move(startSquare = it.square, destinationSquare = it.square.minus(Pair(1,1)), activePlayer = it.owner))
            moves.add(Move(startSquare = it.square, destinationSquare = it.square.minus(Pair(-1,1)), activePlayer = it.owner))
        }
        val expectedMoves = moves.filter { it.destinationSquare.first in 1..5 }.toList()
        val createdMoves = standardPosition.generateValidMoves()
        assertTrue(createdMoves.containsAll(expectedMoves))
        assertTrue(expectedMoves.containsAll(createdMoves))
    }

    @Test
    fun testGenerateValidMoveForRedPlayer() {
        initBoard()
        val moves = mutableListOf<Move>()
        standardPosition.pieces[PlayerColor.RED]!!.forEach {
            moves.add(Move(startSquare = it.square, destinationSquare = it.square.plus(Pair(1,1)), activePlayer = it.owner))
        }
        val expectedMoves = moves.filter {it.destinationSquare.first<=5}.toList()
        val createdMoves = standardPosition.generateValidMoves()
        assertThat(createdMoves, equalTo(expectedMoves))
    }


    private fun initBoard() {
        var boardConfiguration = BoardConfiguration(width = 5,height = 5,blueTemple = Pair(3,5),redTemple = Pair(3,1))
        val bluePieces = listOf(
            Piece(Pair(3,5),PlayerColor.BLUE, PieceType.MASTER),
            Piece(Pair(1,5),PlayerColor.BLUE, PieceType.STUDENT),
            Piece(Pair(2,5),PlayerColor.BLUE, PieceType.STUDENT),
            Piece(Pair(4,5),PlayerColor.BLUE, PieceType.STUDENT),
            Piece(Pair(5,5),PlayerColor.BLUE, PieceType.STUDENT)
        )
        val redPieces = listOf(
            Piece(Pair(3,1),PlayerColor.RED, PieceType.MASTER),
            Piece(Pair(1,1),PlayerColor.RED, PieceType.STUDENT),
            Piece(Pair(2,1),PlayerColor.RED, PieceType.STUDENT),
            Piece(Pair(4,1),PlayerColor.RED, PieceType.STUDENT),
            Piece(Pair(5,1),PlayerColor.RED, PieceType.STUDENT)
        )
        standardPosition =  Position (
            boardConfiguration = boardConfiguration,
            movementOptions = mapOf(PlayerColor.RED to listOf(CobraCard()), PlayerColor.BLUE to listOf(MantisCard())),
            activePlayer = PlayerColor.RED,
            pieces = mapOf(PlayerColor.BLUE to bluePieces, PlayerColor.RED to redPieces)
        )
    }

    private fun createMove(destinationSquare:Pair<Int,Int>, activePlayer:PlayerColor = PlayerColor.BLUE):Move {
        return Move(Pair(3,3), destinationSquare, activePlayer)
    }

}