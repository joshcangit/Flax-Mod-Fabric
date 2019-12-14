package marioandweegee3.flax;

import marioandweegee3.flax.blocks.FMBlocks;
import marioandweegee3.flax.items.FMItems;
import marioandweegee3.ml3api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;

public class FlaxMod implements ModInitializer {
    public static final String modid = "flax";
    public static final RegistryHelper helper = new RegistryHelper(modid);

    @Override
    public void onInitialize() {
        helper.registerItem("flax_seeds", FMItems.flaxSeeds);
        helper.registerItem("roasted_flax_seeds", FMItems.roastedFlaxSeeds);

        registerCrop("flax", FMBlocks.flaxCrop);

        LootTableLoadingCallback.EVENT.register(
            (resourceManager, lootManager, id, supplier, setter) -> {
                if(isGrass(id)){
                    FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder
                        .builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(0.125f))
                        .withEntry(ItemEntry.builder(FMItems.flaxSeeds));
                    
                    supplier.withPool(poolBuilder);
                }
            }
        );
    }

    public void registerCrop(String name, Block crop){
        Registry.BLOCK.add(helper.makeId(name), crop);
    }

    public boolean isGrass(Identifier id){
        if(id.equals(new Identifier("blocks/grass"))) return true;
        if(id.equals(new Identifier("blocks/fern"))) return true;
        if(id.equals(new Identifier("blocks/tall_grass"))) return true;
        return false;
    }

}