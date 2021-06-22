package com.github.imbackt.darkmatter.screen

import com.badlogic.gdx.math.MathUtils
import com.github.imbackt.darkmatter.DarkMatter
import com.github.imbackt.darkmatter.ecs.component.FacingComponent
import com.github.imbackt.darkmatter.ecs.component.GraphicComponent
import com.github.imbackt.darkmatter.ecs.component.PlayerComponent
import com.github.imbackt.darkmatter.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<GameScreen>()

class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {

    override fun show() {
        LOG.debug { "Game screen is shown" }

        engine.entity {
            with<TransformComponent>() {
                position.set(MathUtils.random(0f, 9f), MathUtils.random(0f, 16f), 0f)
            }
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)

    }
}