package com.github.imbackt.darkmatter.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

class TransformComponent : Component, Poolable, Comparable<TransformComponent> {
    val position = Vector3()
    val previousPosition = Vector3()
    val interpolatedPosition = Vector3()
    val size = Vector2(1f, 1f)
    var rotation = 0f

    override fun reset() {
        position.set(Vector3.Zero)
        previousPosition.set(Vector3.Zero)
        interpolatedPosition.set(Vector3.Zero)
        size.set(1f, 1f)
        rotation = 0f
    }

    fun setInitialPosition(x: Float, y: Float, z: Float) {
        position.set(x, y, z)
        previousPosition.set(x, y, z)
        interpolatedPosition.set(x, y, z)
    }

    override fun compareTo(other: TransformComponent): Int {
        val zDiff = other.position.z.compareTo(position.z)
        return if (zDiff == 0) other.position.y.compareTo(position.y) else zDiff
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}