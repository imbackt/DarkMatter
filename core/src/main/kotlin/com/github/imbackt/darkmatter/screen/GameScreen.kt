package com.github.imbackt.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.imbackt.darkmatter.DarkMatter
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<GameScreen>()

class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {
    private val texture = Texture(Gdx.files.internal("graphics/ship_base.png"))
    private val sprite = Sprite(texture)

    override fun show() {
        LOG.debug { "Game screen is shown" }
        sprite.setPosition(1f, 1f)
    }

    override fun render(delta: Float) {
        batch.use {
            sprite.draw(it)
        }
    }

    override fun dispose() {
        texture.dispose()
        batch.dispose()
    }
}