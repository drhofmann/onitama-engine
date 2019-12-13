package de.bogin.onitamaengine.movement

class BoarCard: MovementCard("Boar",
    listOf(
        MoveVector(Pair(-1,0)),
        MoveVector(Pair(1,0)),
        MoveVector(Pair(0,1))
    )
)