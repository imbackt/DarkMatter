package com.github.imbackt.darkmatter.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.GdxRuntimeException
import com.github.imbackt.darkmatter.ecs.component.Animation2D
import com.github.imbackt.darkmatter.ecs.component.AnimationComponent
import com.github.imbackt.darkmatter.ecs.component.AnimationType
import com.github.imbackt.darkmatter.ecs.component.GraphicComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import java.util.*

private val LOG = logger<AnimationSystem>()

class AnimationSystem(
    private val atlas: TextureAtlas
) : IteratingSystem(allOf(AnimationComponent::class, GraphicComponent::class).get()), EntityListener {
    private val animationCache = EnumMap<AnimationType, Animation2D>(AnimationType::class.java)

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        entity[AnimationComponent.mapper]?.let { animationComponent ->
            animationComponent.animation = getAnimation(animationComponent.type)
            val frame = animationComponent.animation.getKeyFrame(animationComponent.stateTime)
            entity[GraphicComponent.mapper]?.setSpriteRegion(frame)
        }
    }

    override fun entityRemoved(entity: Entity?) = Unit

    private fun getAnimation(type: AnimationType): Animation2D {
        var animation = animationCache[type]
        if (animation == null) {
            //load animation
            var regions = atlas.findRegions(type.atlasKey)
            if (regions.isEmpty) {
                LOG.error { "No regions found for ${type.atlasKey}" }
                regions = atlas.findRegions("error")
                if (regions.isEmpty) throw GdxRuntimeException("There is no error region in the atlas")
            } else {
                LOG.debug { "Adding animation of type $type with  ${regions.size} regions" }
            }
            animation = Animation2D(type, regions, type.playMode, type.speedRate)
            animationCache[type] = animation
        }
        return animation
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animationComponent = entity[AnimationComponent.mapper]
        require(animationComponent!=null) {"Entity |entity| must have an AnimationComponent. entity=$entity"}
        val graphic = entity[GraphicComponent.mapper]
        require(graphic!=null) {"Entity |entity| must have an GraphicComponent. entity=$entity"}

        if (animationComponent.type == AnimationType.NONE) {
            LOG.error { "No type specified for animation component $animationComponent for |entity| $entity" }
            return
        }

        if (animationComponent.type == animationComponent.animation.type) {
            // animation is correctly set -> update it
            animationComponent.stateTime += deltaTime
        } else {
            animationComponent.stateTime = 0f
            animationComponent.animation = getAnimation(animationComponent.type)
        }

        val frame = animationComponent.animation.getKeyFrame(animationComponent.stateTime)
        graphic.setSpriteRegion(frame)
    }
}