package de.bogin.onitamaengine.movement

class RabbitCard: MovementCard("Rabbit",
    listOf(
        MoveVector(Pair(-1,-1)),
        MoveVector(Pair(1,1)),
        MoveVector(Pair(2,0))
    )
)