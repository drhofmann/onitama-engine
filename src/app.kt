import main.de.bogin.onitamaengine.board.BoardConfiguration
import main.de.bogin.onitamaengine.board.GameConfiguration

fun main (args: Array<String>) {
    val config = initializeGame()
    val initialBoard=null
}

fun initializeGame(): GameConfiguration {
    val boardConfiguration = BoardConfiguration(5,5,Pair(3,5),Pair(3,1))

    return GameConfiguration(Pair(3,5),
        listOf(Pair(1,5), Pair(2,5), Pair(4,5), Pair(5,5)),
        Pair(3,0),
        listOf(Pair(1,1), Pair(2,1), Pair(4,1), Pair(5,1)),
        boardConfiguration
    )
}