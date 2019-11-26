/*
 * MIT License
 *
 * Copyright (c) 2019 i509VCB
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

package me.i509.fabric.cursedshulkerboxes.block.material.copper;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.world.BlockView;

import me.i509.fabric.cursedshulkerboxes.abstraction.DefaultReturnHashMap;
import me.i509.fabric.cursedshulkerboxes.api.block.material.AbstractMaterialBasedShulkerBoxBlock;
import me.i509.fabric.cursedshulkerboxes.registry.ShulkerBlocks;

public class CopperShulkerBoxBlock extends AbstractMaterialBasedShulkerBoxBlock {
	private static final Map<DyeColor, Block> COLOR_BLOCK_MAP = Util.create(new DefaultReturnHashMap<>(ShulkerBlocks.COPPER_SHULKER_BOX), (map) -> {
		map.put(DyeColor.WHITE, ShulkerBlocks.WHITE_COPPER_SHULKER_BOX);
		map.put(DyeColor.ORANGE, ShulkerBlocks.ORANGE_COPPER_SHULKER_BOX);
		map.put(DyeColor.MAGENTA, ShulkerBlocks.MAGENTA_COPPER_SHULKER_BOX);
		map.put(DyeColor.LIGHT_BLUE, ShulkerBlocks.LIGHT_BLUE_COPPER_SHULKER_BOX);
		map.put(DyeColor.YELLOW, ShulkerBlocks.YELLOW_COPPER_SHULKER_BOX);
		map.put(DyeColor.LIME, ShulkerBlocks.LIME_COPPER_SHULKER_BOX);
		map.put(DyeColor.PINK, ShulkerBlocks.PINK_COPPER_SHULKER_BOX);
		map.put(DyeColor.GRAY, ShulkerBlocks.GRAY_COPPER_SHULKER_BOX);
		map.put(DyeColor.LIGHT_GRAY, ShulkerBlocks.LIGHT_GRAY_COPPER_SHULKER_BOX);
		map.put(DyeColor.CYAN, ShulkerBlocks.CYAN_COPPER_SHULKER_BOX);
		map.put(DyeColor.PURPLE, ShulkerBlocks.PURPLE_COPPER_SHULKER_BOX);
		map.put(DyeColor.BLUE, ShulkerBlocks.BLUE_COPPER_SHULKER_BOX);
		map.put(DyeColor.BROWN, ShulkerBlocks.BROWN_COPPER_SHULKER_BOX);
		map.put(DyeColor.GREEN, ShulkerBlocks.GREEN_COPPER_SHULKER_BOX);
		map.put(DyeColor.RED, ShulkerBlocks.RED_COPPER_SHULKER_BOX);
		map.put(DyeColor.BLACK, ShulkerBlocks.BLACK_COPPER_SHULKER_BOX);
	});

	public CopperShulkerBoxBlock(Settings settings, @Nullable DyeColor color) {
		super(settings, 36, color);
	}

	@Override
	public ItemStack getItemStack(@Nullable DyeColor color) {
		return new ItemStack(COLOR_BLOCK_MAP.get(color));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new CopperShulkerBoxBlockEntity(this.getColor());
	}
}