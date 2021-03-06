package de.bogin.onitamaengine.domain.movement

enum class MovementCard (
    val movementVectors:List<MoveVector>
) {
    BOAR(listOf(
            MoveVector(Pair(-1, 0)),
            MoveVector(Pair(1, 0)),
            MoveVector(Pair(0, 1))
    )),
    COBRA(listOf(
            MoveVector(Pair(-1, 0)),
            MoveVector(Pair(1, -1)),
            MoveVector(Pair(1, 1))
    )),
    CRAB(listOf(
            MoveVector(Pair(-2, 0)),
            MoveVector(Pair(2, 0)),
            MoveVector(Pair(0, 1))
    )),
    CRANE(listOf(
            MoveVector(Pair(0, 1)),
            MoveVector(Pair(-1, -1)),
            MoveVector(Pair(1, -1))
    )),
    DRAGON(listOf(
            MoveVector(Pair(-2, 1)),
            MoveVector(Pair(2, 1)),
            MoveVector(Pair(-1, -1)),
            MoveVector(Pair(1, -1))
    )),
    EEL(listOf(
            MoveVector(Pair(-1, 1)),
            MoveVector(Pair(1, 1)),
            MoveVector(Pair(1, 0))
    )),
    ELEPHANT(listOf(
            MoveVector(Pair(-1, 1)),
            MoveVector(Pair(1, 1)),
            MoveVector(Pair(-1, 0)),
            MoveVector(Pair(1, 0))
    )),
    FROG(listOf(
            MoveVector(Pair(-2, 0)),
            MoveVector(Pair(-1, 1)),
            MoveVector(Pair(1, -1))
    )),
    GOOSE(listOf(
            MoveVector(Pair(-1, 1)),
            MoveVector(Pair(-1, 0)),
            MoveVector(Pair(1, 0)),
            MoveVector(Pair(1, -1))
    )),
    HORSE(listOf(
            MoveVector(Pair(-1, 0)),
            MoveVector(Pair(0, -1)),
            MoveVector(Pair(0, 1))
    )),
    MANTIS(listOf(
            MoveVector(Pair(-1, 1)),
            MoveVector(Pair(1, 1)),
            MoveVector(Pair(0, -1))
    )),
    MONKEY(listOf(
            MoveVector(Pair(-1, 1)),
            MoveVector(Pair(1, 1)),
            MoveVector(Pair(-1, -1)),
            MoveVector(Pair(1, -1))
    )),
    OX(listOf(
            MoveVector(Pair(1, 0)),
            MoveVector(Pair(0, -1)),
            MoveVector(Pair(0, 1))
    )),
    RABBIT(listOf(
            MoveVector(Pair(-1, -1)),
            MoveVector(Pair(1, 1)),
            MoveVector(Pair(2, 0))
    )),
    ROOSTER(listOf(
            MoveVector(Pair(-1, 0)),
            MoveVector(Pair(-1, -1)),
            MoveVector(Pair(1, 0)),
            MoveVector(Pair(1, 1))
    )),
    TIGER(listOf(
            MoveVector(Pair(0, 2)),
            MoveVector(Pair(0, -1))
    ))
}