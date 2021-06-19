package com.github.imbackt.darkmatter

import com.badlogic.gdx.Game

class DarkMatter : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}