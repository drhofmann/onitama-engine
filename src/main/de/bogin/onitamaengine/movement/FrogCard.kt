package de.bogin.onitamaengine.movement

class FrogCard: MovementCard("Frog",
    listOf(
        MoveVector(Pair(-2,0)),
        MoveVector(Pair(-1,1)),
        MoveVector(Pair(1,-1))
    )
)