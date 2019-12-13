package de.bogin.onitamaengine.movement

class OxCard: MovementCard("Ox",
    listOf(
        MoveVector(Pair(1,0)),
        MoveVector(Pair(0,-1)),
        MoveVector(Pair(0,1))
    )
)