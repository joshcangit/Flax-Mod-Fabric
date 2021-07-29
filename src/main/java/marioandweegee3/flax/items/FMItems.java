package marioandweegee3.flax.items;

import marioandweegee3.flax.blocks.FMBlocks;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class FMItems {
    public static final Item flaxSeeds = new AliasedBlockItem(FMBlocks.flaxCrop, new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item roastedFlaxSeeds = new Item(new Item.Settings().food(new FoodComponent.Builder().hunger(2).saturationModifier(0.45f).snack().alwaysEdible().build()).group(ItemGroup.FOOD));
    public static final float chanceToDrop = 0.25f;
}
