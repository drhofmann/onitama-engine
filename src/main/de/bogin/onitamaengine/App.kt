import de.bogin.onitamaengine.board.*
import de.bogin.onitamaengine.board.BoardConfiguration


fun main(args: Array<String>) {
    val initialPosition = initializeGame()
    println ("Hi")
    val input = readLine()
    println("You typed in $input")
}



fun initializeGame(): Position {
    val boardConfiguration = BoardConfiguration(5,5,Pair(3,5),Pair(3,1))
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
    return Position(
        boardConfiguration = boardConfiguration,
        movementOptions = mapOf(),
        activePlayer = PlayerColor.RED,
        pieces = mapOf(PlayerColor.BLUE to bluePieces, PlayerColor.RED to redPieces)
    )
}