package de.bogin.onitamaengine.movement

class MonkeyCard: MovementCard("Monkey",
    listOf(
        MoveVector(Pair(-1,1)),
        MoveVector(Pair(1,1)),
        MoveVector(Pair(-1,-1)),
        MoveVector(Pair(1,-1))
    )
)