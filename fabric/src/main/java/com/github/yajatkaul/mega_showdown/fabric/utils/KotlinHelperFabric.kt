package com.github.yajatkaul.mega_showdown.fabric.utils

import com.bedrockk.molang.runtime.MoLangRuntime
import com.cobblemon.mod.common.util.asExpressionLike
import com.cobblemon.mod.common.util.resolve

object KotlinHelperFabric {
    fun playParticleEffect(particleId: String, locator: String, runtime: MoLangRuntime) {
        runtime.resolve("q.particle('$particleId', '$locator')".asExpressionLike())
    }
}