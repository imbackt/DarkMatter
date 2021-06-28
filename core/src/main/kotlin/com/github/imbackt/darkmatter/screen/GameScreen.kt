package com.github.imbackt.darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.imbackt.darkmatter.DarkMatter
import com.github.imbackt.darkmatter.UNIT_SCALE
import com.github.imbackt.darkmatter.V_WIDTH
import com.github.imbackt.darkmatter.asset.MusicAsset
import com.github.imbackt.darkmatter.ecs.component.*
import com.github.imbackt.darkmatter.ecs.system.DAMAGE_AREA_HEIGHT
import com.github.imbackt.darkmatter.event.GameEvent
import com.github.imbackt.darkmatter.event.GameEventListener
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import kotlin.math.min

private val LOG = logger<GameScreen>()
private const val MAX_DELTA_TIME = 1 / 20f

class GameScreen(
    game: DarkMatter,
    val engine: Engine = game.engine,
) : DarkMatterScreen(game), GameEventListener {

    override fun show() {
        LOG.debug { "Game screen is shown" }
        LOG.debug { "${preferences["highscore", 0f]}" }
        gameEventManager.addListener(GameEvent.PlayerDeath::class, this)

        audioService.play(MusicAsset.GAME)
        spawnPlayer()

        engine.entity {
            with<TransformComponent> {
                size.set(
                    V_WIDTH.toFloat(),
                    DAMAGE_AREA_HEIGHT
                )
            }
            with<AnimationComponent> { type = AnimationType.DARK_MATTER }
            with<GraphicComponent>()
        }
    }

    override fun hide() {
        super.hide()
        gameEventManager.removeListener(this)
    }

    private fun spawnPlayer() {
        val playerShip = engine.entity {
            with<TransformComponent> {
                setInitialPosition(4.5f, 8f, -1f)
            }
            with<MoveComponent>()
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }

        engine.entity {
            with<TransformComponent>()
            with<AttachComponent> {
                entity = playerShip
                offset.set(1f * UNIT_SCALE, -6f * UNIT_SCALE)
            }
            with<GraphicComponent>()
            with<AnimationComponent> { type = AnimationType.FIRE }
        }
    }

    override fun render(delta: Float) {
        (game.batch as SpriteBatch).renderCalls = 0
        engine.update(min(MAX_DELTA_TIME, delta))
        audioService.update()
        LOG.debug { "RenderCalls: ${(game.batch as SpriteBatch).renderCalls}" }
    }

    override fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.PlayerDeath -> {
                LOG.debug { "Player died with a distance of ${event.distance}" }
                preferences.flush {
                    this["highscore"] = event.distance
                }
                spawnPlayer()
            }
            GameEvent.CollectPowerUp -> TODO()
            GameEvent.PlayerHit -> TODO()
        }
    }
}