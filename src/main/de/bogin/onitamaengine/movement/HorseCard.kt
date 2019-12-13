package de.bogin.onitamaengine.movement

class HorseCard: MovementCard("Horse",
    listOf(
        MoveVector(Pair(-1,0)),
        MoveVector(Pair(0,-1)),
        MoveVector(Pair(0,1))
    )
)