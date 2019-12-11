package me.i509.fabric.bulkyshulkies.client.block.entity.renderer;

import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import me.i509.fabric.bulkyshulkies.block.material.platinum.PlatinumShulkerBoxBE;
import me.i509.fabric.bulkyshulkies.ShulkerBoxKeys;

@Environment(EnvType.CLIENT)
public class PlatinumShulkerBoxBERenderer extends Abstract1x1ShulkerBERenderer<PlatinumShulkerBoxBE> {
	public PlatinumShulkerBoxBERenderer(BlockEntityRenderDispatcher ber) {
		super(ber, ShulkerBoxKeys.PLATINUM);
	}
}
