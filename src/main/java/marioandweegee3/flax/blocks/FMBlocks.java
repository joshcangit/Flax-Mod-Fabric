package marioandweegee3.flax.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class FMBlocks {
    public static final Block flaxCrop = new FlaxCrop(
        FabricBlockSettings.copy(Blocks.WHEAT)
        .build()
    );
}