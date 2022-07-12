package com.lambda.client.module.modules.render

import com.lambda.client.module.Category
import com.lambda.client.module.Module
import com.lambda.client.setting.settings.impl.collection.CollectionSetting
import com.lambda.client.util.threads.onMainThread
import kotlinx.coroutines.runBlocking
import net.minecraft.block.state.IBlockState

object Xray : Module(
    name = "Xray",
    description = "Lets you see through blocks",
    category = Category.RENDER
) {
    private val defaultVisibleList = linkedSetOf("minecraft:diamond_ore", "minecraft:iron_ore", "minecraft:gold_ore", "minecraft:portal", "minecraft:cobblestone","minecraft:water","minecraft:flowing_water","minecraft:lava","minecraft:flowing_lava","minecraft:chest","minecraft:trapped_chest")

    val visibleList = setting(CollectionSetting("Visible List", defaultVisibleList, { false }))

    @JvmStatic
    fun shouldReplace(state: IBlockState): Boolean {
        return isEnabled && !visibleList.contains(state.block.registryName.toString())
    }

    init {
        onToggle {
            runBlocking {
                onMainThread {
                    mc.renderGlobal?.loadRenderers()
                }
            }
        }

        visibleList.editListeners.add {
            runBlocking {
                onMainThread {
                    mc.renderGlobal?.loadRenderers()
                }
            }
        }
    }
}
