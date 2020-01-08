package de.bogin.onitamaengine

import io.javalin.Javalin
import io.javalin.plugin.rendering.vue.JavalinVue
import io.javalin.plugin.rendering.vue.VueComponent

fun main() {
    val app = Javalin.create {config ->
        config.enableWebjars()
    }.start(7000)
    app.get("/game", VueComponent("<wholepage></wholepage>"))

    app.get("/api/position", GameController::getGamePosition)
    app.post("/api/doMove", GameController::postMove)

    JavalinVue.stateFunction
}