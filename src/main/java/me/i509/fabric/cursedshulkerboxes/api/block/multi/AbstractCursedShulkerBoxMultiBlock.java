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

package me.i509.fabric.cursedshulkerboxes.api.block.multi;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import me.i509.fabric.cursedshulkerboxes.api.block.base.AbstractShulkerBoxBlock;
import me.i509.fabric.cursedshulkerboxes.api.block.base.AbstractShulkerBoxBlockEntity;
import me.i509.fabric.cursedshulkerboxes.api.block.base.BaseShulkerBlockEntity;

public abstract class AbstractCursedShulkerBoxMultiBlock extends AbstractShulkerBoxBlock implements MultiShulkerBoxBlock {
	/*
	 * TODO:
	 *
	 * <p>Synchonized inventories breaks collision boxes.
	 *
	 * <p>Cannot place on a wall in midair yet.
	 *
	 * <p>When block is placed sideways or vertically, verify an entity isn't in the way before going letting the place occur.
	 *
	 * <p>Fix issue where placing the box will overwrite the block above it.
	 *
	 * <p>Breaking either side where an item is present will not break the other side.
	 *
	 * <p>When opening from bottom, also expand the upper halve's box shape.
	 *
	 * <p>Drop the shulker box when broken. (Requires all color types to be registered) [[Fine]]
	 *
	 * <p>Item Texture should be the tall box (custom item renderer needed)
	 *
	 * <p>Piston pushing is broken, when one half is pushed and destroyed, make sure the other half is removed.
	 */
	public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

	protected AbstractCursedShulkerBoxMultiBlock(Settings settings, int slotCount, @Nullable DyeColor color) {
		super(settings, slotCount, color);
		this.setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.UP).with(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
		if (blockState.get(HALF) == DoubleBlockHalf.LOWER) {
			return VoxelShapes.fullCube();
		}

		BlockEntity blockEntity = blockView.getBlockEntity(blockPos);
		return blockEntity instanceof BaseShulkerBlockEntity ? VoxelShapes.cuboid(((BaseShulkerBlockEntity) blockEntity).getBoundingBox(blockState)) : VoxelShapes.fullCube();
	}

	@Override
	public boolean isBottom(BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public boolean canPlaceAt(BlockState blockState, WorldView view, BlockPos blockPos) { // TODO for bug relating to overwriting block above when it shouldn't place at all
		BlockPos down = new BlockPos(blockPos.down());
		BlockState belowState = view.getBlockState(down);
		Direction facing = blockState.get(FACING);

		return blockState.get(HALF) == DoubleBlockHalf.LOWER ? belowState.isSideSolidFullSquare(view, down, facing) : belowState.getBlock() == this;
	}

	@Override
	public boolean canSuffocate(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return false;
	}

	@Override
	public boolean isObstructionFree(BaseShulkerBlockEntity blockEntity, Direction facing, World world, BlockPos blockPos) {
		if (blockEntity.getAnimationStage() == BaseShulkerBlockEntity.AnimationStatus.CLOSED) {
			Box openBox = getOpenBox(facing, world, blockPos);

			if (isBottom(world.getBlockState(blockPos))) {
				return world.doesNotCollide(openBox.offset(new BlockPos(blockPos.offset(facing, 2))));
			}

			return world.doesNotCollide(openBox.offset(blockPos.offset(facing)));
		} else {
			return true;
		}
	}

	public final Box getOpenBox(Direction facing) {
		return VoxelShapes.fullCube().getBoundingBox();
	}

	protected abstract Box getOpenBox(Direction facing, World world, BlockPos blockPos);

	@Override
	public BlockState getPlacementState(ItemPlacementContext placementContext) {
		return this.getDefaultState().with(FACING, placementContext.getSide()).with(HALF, DoubleBlockHalf.LOWER);
	}

	@Override
	public void onPlaced(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
		world.setBlockState(blockPos.offset(blockState.get(FACING)), blockState.with(HALF, DoubleBlockHalf.UPPER), 3); // Directionality of place checks here.

		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos.up());

			if (blockEntity instanceof AbstractShulkerBoxBlockEntity) {
				((AbstractShulkerBoxBlockEntity) blockEntity).setCustomName(itemStack.getName());
			}
		}

		super.onPlaced(world, blockPos, blockState, livingEntity, itemStack);
	}

	@Override
	public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
		DoubleBlockHalf brokeBlockHalf = blockState.get(HALF);
		Direction facing = blockState.get(FACING);

		BlockPos otherHalf = brokeBlockHalf == DoubleBlockHalf.LOWER ? blockPos.offset(facing) : blockPos.offset(facing.getOpposite());
		BlockState otherBlockState = world.getBlockState(otherHalf);

		if (otherBlockState.getBlock() == this && otherBlockState.get(HALF) != brokeBlockHalf) {
			world.setBlockState(otherHalf, Blocks.AIR.getDefaultState(), 35);
			world.playLevelEvent(playerEntity, 2001, otherHalf, Block.getRawIdFromState(otherBlockState));
			ItemStack mainHandStack = playerEntity.getMainHandStack();

			if (!world.isClient && !playerEntity.isCreative()) {
				Block.dropStacks(blockState, world, blockPos, null, playerEntity, mainHandStack);
				Block.dropStacks(otherBlockState, world, otherHalf, null, playerEntity, mainHandStack);
			}
		}

		super.onBreak(world, blockPos, blockState, playerEntity);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateFactory) {
		super.appendProperties(stateFactory);
		stateFactory.add(HALF);
	}
}