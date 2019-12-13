package de.bogin.onitamaengine.movement

class DragonCard: MovementCard("Dragon",
    listOf(
        MoveVector(Pair(-2,1)),
        MoveVector(Pair(2,1)),
        MoveVector(Pair(-1,-1)),
        MoveVector(Pair(1,-1))
    )
)