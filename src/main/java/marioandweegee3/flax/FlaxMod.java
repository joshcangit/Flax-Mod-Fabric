package marioandweegee3.flax;

import java.util.HashSet;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import marioandweegee3.flax.blocks.FMBlocks;
import marioandweegee3.flax.items.FMItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;

public class FlaxMod implements ModInitializer {
    public static final String modid = "flax";
    public static final Logger logger = LogManager.getLogger(modid);

    public void registerCrop(String name, Block crop){
        Registry.register(Registry.BLOCK, new Identifier(modid, name), crop);
    }

    public void registerItem(String name, Item item){
        Registry.register(Registry.ITEM, new Identifier(modid, name), item);
    }

    @Override
    public void onInitialize() {
        registerItem("flax_seeds", FMItems.flaxSeeds);
        registerItem("roasted_flax_seeds", FMItems.roastedFlaxSeeds);

        registerCrop("flax", FMBlocks.flaxCrop);

        LootTableLoadingCallback.EVENT.register(
            (resourceManager, lootManager, id, supplier, setter) -> {
                HashSet<String> hs = new HashSet<String>();
                Collections.addAll(hs, "blocks/grass", "blocks/tall_grass", "blocks/fern", "blocks/large_fern");
                if (hs.contains(id.getPath())) {
                    FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder
                        .builder()
                        .rolls(UniformLootNumberProvider.create(0, 1))
                        .withCondition(RandomChanceLootCondition.builder(FMItems.chanceToDrop).build())
                        .withEntry(ItemEntry.builder(FMItems.flaxSeeds).build());
                    supplier.withPool(poolBuilder.build());
                }
            }
        );
    }
}
