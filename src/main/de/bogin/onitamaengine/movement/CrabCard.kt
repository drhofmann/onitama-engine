package de.bogin.onitamaengine.movement

class CrabCard: MovementCard("Crab",
    listOf(
        MoveVector(Pair(-2,0)),
        MoveVector(Pair(2,0)),
        MoveVector(Pair(0,1))
    )
)