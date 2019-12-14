package de.bogin.onitamaengine.board

import de.bogin.onitamaengine.extensions.minus
import de.bogin.onitamaengine.extensions.plus
import de.bogin.onitamaengine.movement.Move
import de.bogin.onitamaengine.movement.MoveVector
import de.bogin.onitamaengine.movement.MovementCard
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.AnnotationSpec


class PositionTest: AnnotationSpec() {
    private lateinit var standardPosition:Position

    @Test
    fun testDestinationFieldOfMoveNotOnBoardUpperBound() {
        standardPosition.isMoveAllowed(createMove(Pair(6,5))) shouldBe false
    }

    @Test
    fun testDestinationFieldOfMoveNotOnBoardLowerBound() {
        standardPosition.isMoveAllowed(createMove(Pair(0,-1))) shouldBe false
    }

    @Test
    fun testDestinationFieldOfMoveOnBoard() {
        standardPosition.isMoveAllowed(createMove(Pair(3,4))) shouldBe true
    }

    @Test
    fun testDestinationFieldOccupiedByOwnBlueMaster() {
        standardPosition.isMoveAllowed(createMove(Pair(3,5), PlayerColor.BLUE)) shouldBe false
    }

    @Test
    fun testDestinationFieldOccupiedByOwnRedStudent() {
        standardPosition.isMoveAllowed(createMove(Pair(4,1), PlayerColor.RED)) shouldBe false
    }

    @Test
    fun testDestinationFieldOccupiedByOpposingRedMaster() {
        standardPosition.isMoveAllowed(createMove(Pair(3,1), PlayerColor.BLUE)) shouldBe true
    }

    @Test
    fun testDestinationFieldOccupiedByOpposingBlueStudent() {
        standardPosition.isMoveAllowed(createMove(Pair(2,5), PlayerColor.RED)) shouldBe true
    }

    @Test
    fun testDestinationFieldIsEmpty() {
        standardPosition.isMoveAllowed(createMove(Pair(4,3), PlayerColor.RED)) shouldBe true
    }

    @Test
    fun testCreateMoveConsideringPlayerColorForBLuePlayer() {
        val piece = Piece(square=Pair(3,3), owner = PlayerColor.BLUE, type = PieceType.MASTER)
        val coordinateIncrement = Pair(1,2)
        val move = standardPosition.createMoveConsideringPlayerColor(
            piece,
            MoveVector(relativeTargetCoordinates = coordinateIncrement), MovementCard.COBRA
        )
        move shouldBe Move(startSquare = piece.square, destinationSquare = piece.square.minus(coordinateIncrement), activePlayer = piece.owner, movementCard = MovementCard.COBRA)
    }

    @Test
    fun testGenerateMovesForRedMaster() {
        val moves = mutableListOf<Move>()
        val redMaster =
            standardPosition.pieces[PlayerColor.RED]!!.first { it.owner == PlayerColor.RED && it.type == PieceType.MASTER }
        moves.add(Move(startSquare = redMaster.square, destinationSquare = redMaster.square.plus(Pair(-1,0)), activePlayer = redMaster.owner, movementCard = MovementCard.COBRA))
        moves.add(Move(startSquare = redMaster.square, destinationSquare = redMaster.square.plus(Pair(1,-1)), activePlayer = redMaster.owner, movementCard = MovementCard.COBRA))
        moves.add(Move(startSquare = redMaster.square, destinationSquare = redMaster.square.plus(Pair(1,1)), activePlayer = redMaster.owner, movementCard = MovementCard.COBRA))
        moves.add(Move(startSquare = redMaster.square, destinationSquare = redMaster.square.plus(Pair(0,1)), activePlayer = redMaster.owner, movementCard = MovementCard.CRANE))
        moves.add(Move(startSquare = redMaster.square, destinationSquare = redMaster.square.plus(Pair(-1,-1)), activePlayer = redMaster.owner, movementCard = MovementCard.CRANE))
        moves.add(Move(startSquare = redMaster.square, destinationSquare = redMaster.square.plus(Pair(1,-1)), activePlayer = redMaster.owner, movementCard = MovementCard.CRANE))
        val expectedMoves = moves.toSet()
        val createdMoves = standardPosition.generateMovesForOnePiece(redMaster)
        createdMoves shouldBe expectedMoves
    }

    @Test
    fun testGenerateValidMoveForBluePlayer() {
        standardPosition.activePlayer = PlayerColor.BLUE
        val moves = mutableSetOf<Move>()
        standardPosition.pieces[PlayerColor.BLUE]!!.forEach {
            moves.add(Move(startSquare = it.square, destinationSquare = it.square.minus(Pair(1,1)), activePlayer = it.owner, movementCard = MovementCard.MANTIS))
            moves.add(Move(startSquare = it.square, destinationSquare = it.square.minus(Pair(-1,1)), activePlayer = it.owner, movementCard = MovementCard.MANTIS))
            moves.add(Move(startSquare = it.square, destinationSquare = it.square.minus(Pair(1,1)), activePlayer = it.owner, movementCard = MovementCard.MONKEY))
            moves.add(Move(startSquare = it.square, destinationSquare = it.square.minus(Pair(-1,1)), activePlayer = it.owner, movementCard = MovementCard.MONKEY))
        }
        val expectedMoves = moves.filter { it.destinationSquare.first in 1..5 }.toSet()
        val createdMoves = standardPosition.generateValidMoves()
        createdMoves shouldBe expectedMoves
    }

    @Test
    fun testGenerateValidMoveForRedPlayer() {
        val moves = mutableListOf<Move>()
        standardPosition.pieces[PlayerColor.RED]!!.forEach {
            moves.add(Move(startSquare = it.square, destinationSquare = it.square.plus(Pair(1,1)), activePlayer = it.owner, movementCard = MovementCard.COBRA))
            moves.add(Move(startSquare = it.square, destinationSquare = it.square.plus(Pair(0,1)), activePlayer = it.owner, movementCard = MovementCard.CRANE))
        }
        val expectedMoves = moves.filter {it.destinationSquare.first<=5}.toSet()
        val createdMoves = standardPosition.generateValidMoves()
        createdMoves shouldBe expectedMoves
    }

    @Test
    fun testDoValidMove() {
        val move = Move(movementCard = MovementCard.COBRA, activePlayer = PlayerColor.RED, startSquare = Pair(3,1), destinationSquare = Pair(4,2))
        val newPosition = standardPosition.doMove(move)
        newPosition.activePlayer shouldBe PlayerColor.BLUE
        newPosition.movementOptions[PlayerColor.NEUTRAL] shouldBe setOf(MovementCard.COBRA)
        newPosition.movementOptions[PlayerColor.BLUE] shouldBe standardPosition.movementOptions[PlayerColor.BLUE]
        newPosition.movementOptions[PlayerColor.RED] shouldBe setOf(MovementCard.EEL, MovementCard.CRANE)
    }

    @Test
    fun testDoMoveWithWrongColor() {
        val move = Move(movementCard = MovementCard.MANTIS, activePlayer = PlayerColor.BLUE, startSquare = Pair(3,5), destinationSquare = Pair(2,4))
        val exception = shouldThrowExactly<IncorrectBoardStateException> {
            standardPosition.doMove(move)
        }
        exception.message shouldBe "A move by ${move.activePlayer} would be made but it is RED's turn"
    }

    @Test
    fun testDoMoveWithInvalidMovementCard() {
        val move = Move(movementCard = MovementCard.EEL, activePlayer = PlayerColor.RED, startSquare = Pair(3,1), destinationSquare = Pair(4,2))
        val exception = shouldThrowExactly<IncorrectBoardStateException> {
            standardPosition.doMove(move)
        }
        exception.message shouldBe "A move with ${move.movementCard} would be made but active player ${move.activePlayer} has following cards: ${standardPosition.movementOptions[move.activePlayer]}"
    }

    @Test
    fun testDoMoveWithNoValidPieceOnStartSquare() {
        val move = Move(movementCard = MovementCard.COBRA, activePlayer = PlayerColor.RED, startSquare = Pair(3,2), destinationSquare = Pair(4,2))
        val exception = shouldThrowExactly<IncorrectBoardStateException> {
            standardPosition.doMove(move)
        }
        exception.message shouldBe "A move with a ${move.activePlayer} piece would be made but there is no piece of that player on square ${move.startSquare}. There are pieces on: ${standardPosition.pieces[move.activePlayer]!!.map{it.square}}"
    }

    @Test
    fun testDoMoveWithOwnPieceOnDestinationSquare() {
        val move = Move(movementCard = MovementCard.COBRA, activePlayer = PlayerColor.RED, startSquare = Pair(3,1), destinationSquare = Pair(4,1))
        val exception = shouldThrowExactly<IncorrectBoardStateException> {
            standardPosition.doMove(move)
        }
        exception.message shouldBe "A move to ${move.destinationSquare} would be made but there is already a piece of player ${move.activePlayer}. There are pieces on: ${standardPosition.pieces[move.activePlayer]!!.map{it.square}}"
    }

    @Before
    fun initBoard() {
        var boardConfiguration = BoardConfiguration(width = 5,height = 5,blueTemple = Pair(3,5),redTemple = Pair(3,1))
        val bluePieces = setOf(
            Piece(Pair(3,5),PlayerColor.BLUE, PieceType.MASTER),
            Piece(Pair(1,5),PlayerColor.BLUE, PieceType.STUDENT),
            Piece(Pair(2,5),PlayerColor.BLUE, PieceType.STUDENT),
            Piece(Pair(4,5),PlayerColor.BLUE, PieceType.STUDENT),
            Piece(Pair(5,5),PlayerColor.BLUE, PieceType.STUDENT)
        )
        val redPieces = setOf(
            Piece(Pair(3,1),PlayerColor.RED, PieceType.MASTER),
            Piece(Pair(1,1),PlayerColor.RED, PieceType.STUDENT),
            Piece(Pair(2,1),PlayerColor.RED, PieceType.STUDENT),
            Piece(Pair(4,1),PlayerColor.RED, PieceType.STUDENT),
            Piece(Pair(5,1),PlayerColor.RED, PieceType.STUDENT)
        )
        standardPosition =  Position (
            boardConfiguration = boardConfiguration,
            movementOptions = mapOf(PlayerColor.RED to setOf(MovementCard.COBRA, MovementCard.CRANE),
                PlayerColor.BLUE to setOf(MovementCard.MANTIS, MovementCard.MONKEY),
                PlayerColor.NEUTRAL to setOf(MovementCard.EEL)),
            activePlayer = PlayerColor.RED,
            pieces = mapOf(PlayerColor.BLUE to bluePieces, PlayerColor.RED to redPieces)
        )
    }

    private fun createMove(destinationSquare:Pair<Int,Int>, activePlayer:PlayerColor = PlayerColor.BLUE):Move {
        return Move(Pair(3,3), destinationSquare, activePlayer, MovementCard.MANTIS)
    }

}