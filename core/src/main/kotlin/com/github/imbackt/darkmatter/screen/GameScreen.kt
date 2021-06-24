package com.github.imbackt.darkmatter.screen

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.imbackt.darkmatter.DarkMatter
import com.github.imbackt.darkmatter.V_WIDTH
import com.github.imbackt.darkmatter.ecs.component.*
import com.github.imbackt.darkmatter.ecs.system.DAMAGE_AREA_HEIGHT
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import kotlin.math.min

private val LOG = logger<GameScreen>()
private const val MAX_DELTA_TIME = 1 / 20f

class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {

    override fun show() {
        LOG.debug { "Game screen is shown" }

        engine.entity {
            with<TransformComponent> {
                setInitialPosition(4.5f, 8f, 0f)
            }
            with<MoveComponent>()
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }

        engine.entity {
            with<TransformComponent>() {
                size.set(
                    V_WIDTH.toFloat(),
                    DAMAGE_AREA_HEIGHT
                )
            }
            with<AnimationComponent> { type = AnimationType.DARK_MATTER }
            with<GraphicComponent>()
        }
    }

    override fun render(delta: Float) {
        (game.batch as SpriteBatch).renderCalls = 0
        engine.update(min(MAX_DELTA_TIME, delta))
        LOG.debug { "RenderCalls: ${(game.batch as SpriteBatch).renderCalls}" }
    }
}