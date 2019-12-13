package main.de.bogin.onitamaengine.board

data class GameConfiguration(
    val blueMaster:Pair<Int, Int>,
    val blueStudents:List<Pair<Int, Int>>,
    val redMaster:Pair<Int, Int>,
    val redStudents:List<Pair<Int, Int>>,
    val boardConfiguration:BoardConfiguration
)