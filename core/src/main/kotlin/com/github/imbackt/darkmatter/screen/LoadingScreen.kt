package com.github.imbackt.darkmatter.screen

import com.github.imbackt.darkmatter.DarkMatter
import com.github.imbackt.darkmatter.asset.ShaderProgramAsset
import com.github.imbackt.darkmatter.asset.SoundAsset
import com.github.imbackt.darkmatter.asset.TextureAsset
import com.github.imbackt.darkmatter.asset.TextureAtlasAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<LoadingScreen>()

class LoadingScreen(game: DarkMatter) : DarkMatterScreen(game) {
    override fun show() {
        val old = System.currentTimeMillis()
        //queue asset loading
        val assetReferences = gdxArrayOf(
            TextureAsset.values().map { assets.loadAsync(it.descriptor) },
            TextureAtlasAsset.values().map { assets.loadAsync(it.descriptor) },
            SoundAsset.values().map { assets.loadAsync(it.descriptor) },
            ShaderProgramAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()

        //once assets are loaded -> change to GameScreen
        KtxAsync.launch {
            assetReferences.joinAll()
            LOG.debug { "Time for loading assets: ${System.currentTimeMillis() - old} ms" }
            assetsLoaded()
        }

        // ...
        //setup UI
    }

    private fun assetsLoaded() {
        game.addScreen(GameScreen(game))
        game.setScreen<GameScreen>()
        game.removeScreen<LoadingScreen>()
        dispose()
    }
}