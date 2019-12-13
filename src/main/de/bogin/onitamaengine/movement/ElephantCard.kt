package de.bogin.onitamaengine.movement

class ElephantCard: MovementCard("Elephant",
    listOf(
        MoveVector(Pair(-1,1)),
        MoveVector(Pair(1,1)),
        MoveVector(Pair(-1,0)),
        MoveVector(Pair(1,0))
    )
)