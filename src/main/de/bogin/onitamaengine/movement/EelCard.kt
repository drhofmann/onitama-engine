package de.bogin.onitamaengine.movement

class EelCard: MovementCard("Eel",
    listOf(
        MoveVector(Pair(-1,1)),
        MoveVector(Pair(1,1)),
        MoveVector(Pair(1,0))
    )
)