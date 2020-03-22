/*
 * MIT License
 *
 * Copyright (c) 2019-2020 i509VCB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.i509.fabric.bulkyshulkies;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import me.i509.fabric.bulkyshulkies.api.item.ShulkerHelmetItem;
import me.i509.fabric.bulkyshulkies.extension.ShulkerHooks;
import me.i509.fabric.bulkyshulkies.recipe.BulkyRecipeSerializers;
import me.i509.fabric.bulkyshulkies.registry.ShulkerBlockEntities;
import me.i509.fabric.bulkyshulkies.registry.ShulkerBlocks;
import me.i509.fabric.bulkyshulkies.registry.ShulkerNetworking;
import me.i509.fabric.bulkyshulkies.registry.ShulkerScreenHandlers;
import me.i509.fabric.bulkyshulkies.registry.ShulkerDispenserBehaviors;
import me.i509.fabric.bulkyshulkies.registry.ShulkerItemGroups;
import me.i509.fabric.bulkyshulkies.registry.ShulkerItems;
import me.i509.fabric.bulkyshulkies.screen.ScreenHandlerKeys;

public class BulkyShulkiesMod implements ModInitializer {
	public static final String MODID = "bulkyshulkies";

	@Override
	public void onInitialize() {
		BulkyShulkies.getInstance();
		BulkyRecipeSerializers.ABSTRACT_SHULKER_COLORING.getClass(); // Register the colorizer recipe type
		BulkyDataTrackerHandlers.SHULKER_ANIMATION_STAGE.getClass(); // Load the DataTrackers
		ShulkerBlocks.init();
		ShulkerDispenserBehaviors.init();
		ShulkerBlockEntities.init();
		ShulkerItems.init();
		ShulkerItemGroups.init();
		ShulkerHooks.init();

		ContainerProviderRegistry.INSTANCE.registerFactory(ScreenHandlerKeys.ENDER_SLAB, ShulkerScreenHandlers::createEnderSlab);
		ContainerProviderRegistry.INSTANCE.registerFactory(ScreenHandlerKeys.SHULKER_SCROLLABLE_CONTAINER, (ShulkerScreenHandlers::createScrollable));
		ContainerProviderRegistry.INSTANCE.registerFactory(ScreenHandlerKeys.SHULKER_9x7_CONTAINER, ShulkerScreenHandlers::create9x7);
		ContainerProviderRegistry.INSTANCE.registerFactory(ScreenHandlerKeys.SHULKER_11x7_CONTAINER, ShulkerScreenHandlers::create11x7);
		ContainerProviderRegistry.INSTANCE.registerFactory(ScreenHandlerKeys.SHULKER_13x7_CONTAINER, ShulkerScreenHandlers::create13x7);
		ContainerProviderRegistry.INSTANCE.registerFactory(ScreenHandlerKeys.SHULKER_HELMET, ShulkerScreenHandlers::createShulkerHelmet);

		ServerSidePacketRegistry.INSTANCE.register(ShulkerNetworking.HELMET_OPEN, this::onOpenHelmet);
	}

	private void onOpenHelmet(PacketContext context, PacketByteBuf packetByteBuf) {
		PlayerEntity playerEntity = context.getPlayer();

		context.getTaskQueue().execute(() -> {
			if (playerEntity.isSpectator()) {
				return;
			}

			ItemStack stack = playerEntity.getEquippedStack(EquipmentSlot.HEAD);

			if (stack.getItem() != ShulkerItems.SHULKER_HELMET) {
				return;
			}

			ShulkerHelmetItem.open(playerEntity);
		});
	}
}
