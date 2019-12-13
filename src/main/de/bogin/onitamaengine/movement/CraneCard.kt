package de.bogin.onitamaengine.movement

class CraneCard: MovementCard("Crane",
    listOf(
        MoveVector(Pair(0,1)),
        MoveVector(Pair(-1,-1)),
        MoveVector(Pair(1,-1))
    )
)