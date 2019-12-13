package de.bogin.onitamaengine.movement

class MantisCard: MovementCard("Ox",
    listOf(
        MoveVector(Pair(-1,1)),
        MoveVector(Pair(1,1)),
        MoveVector(Pair(0,-1))
    )
)