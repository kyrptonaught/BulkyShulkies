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

package me.i509.fabric.bulkyshulkies.api.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import me.i509.fabric.bulkyshulkies.api.block.base.AbstractShulkerBoxBE;
import me.i509.fabric.bulkyshulkies.api.block.base.AbstractShulkerBoxBlock;
import me.i509.fabric.bulkyshulkies.api.block.base.BasicShulkerBlockEntity;

public abstract class HorizontalFacingShulkerBoxBlock extends AbstractShulkerBoxBlock {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	protected HorizontalFacingShulkerBoxBlock(Settings settings, int slotCount, @Nullable DyeColor color) {
		super(settings, slotCount, color);
		this.setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.UP));
	}

	@Override
	public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else if (player.isSpectator()) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);

			if (blockEntity instanceof AbstractShulkerBoxBE) {
				Direction facing = blockState.get(FACING);
				AbstractShulkerBoxBE cursedBlockEntity = (AbstractShulkerBoxBE) blockEntity;
				boolean shouldOpen;

				if (cursedBlockEntity.getAnimationStage() == BasicShulkerBlockEntity.AnimationStatus.CLOSED) {
					Box openBox = getOpenBox(facing);
					shouldOpen = world.doesNotCollide(openBox.offset(blockPos.offset(facing)));
				} else {
					shouldOpen = true;
				}

				if (shouldOpen) {
					if (cursedBlockEntity.checkUnlocked(player)) {
						cursedBlockEntity.checkLootInteraction(player);
						openContainer(blockPos, player, cursedBlockEntity.getDisplayName());
						player.incrementStat(Stats.OPEN_SHULKER_BOX);
					}
				}

				return ActionResult.SUCCESS;
			} else {
				return ActionResult.PASS;
			}
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext placementContext) {
		return this.getDefaultState().with(FACING, placementContext.getSide());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManagerBuilder) {
		super.appendProperties(stateManagerBuilder);
		stateManagerBuilder.add(FACING);
	}

	@Override
	public BlockState rotate(BlockState blockState, BlockRotation blockRotation) {
		return blockState.with(FACING, blockRotation.rotate(blockState.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, BlockMirror blockMirror) {
		return blockState.rotate(blockMirror.getRotation(blockState.get(FACING)));
	}
}
