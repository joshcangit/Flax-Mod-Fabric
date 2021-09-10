package marioandweegee3.flax;

import java.util.HashSet;
import java.util.Collections;

import java.io.IOException;
import java.io.Writer;
import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import marioandweegee3.flax.blocks.FMBlocks;
import marioandweegee3.flax.items.FMItems;
import marioandweegee3.flax.config.FMConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;

@SuppressWarnings("unused")
public class FlaxMod implements ModInitializer {
    public static final String modid = "flax";
    public static final Logger logger = LogManager.getLogger(modid);

    public void registerCrop(String name, Block crop){
        Registry.register(Registry.BLOCK, new Identifier(modid, name), crop);
    }

    public void registerItem(String name, Item item){
        Registry.register(Registry.ITEM, new Identifier(modid, name), item);
    }

    public static Charset charset = java.nio.charset.StandardCharsets.UTF_8;
    public static final JsonGrammar HJSON = JsonGrammar.builder()
        .withComments(true)
        .printCommas(false)
        .bareSpecialNumerics(true)
        .printUnquotedKeys(true)
        .build();
    public static Jankson jankson = Jankson.builder().build();
    public Float dropChance = 0.0f;

    @Override
    public void onInitialize() {
        registerItem("flax_seeds", FMItems.flaxSeeds);
        registerItem("roasted_flax_seeds", FMItems.roastedFlaxSeeds);

        registerCrop("flax", FMBlocks.flaxCrop);

        try {
            Path configDir = FabricLoader.getInstance().getConfigDir();
            if(!Files.exists(configDir)) Files.createDirectories(configDir);
            Path configFile = configDir.resolve(Paths.get(modid.concat(".hjson"))).normalize();
            configWriter(configFile);
            JsonObject configObject = configReader(configFile);
            dropChance = chanceToDrop(configObject, "grassFlaxSeedDropChance");
        } catch (Exception e) {
            e.getStackTrace();
        }

        LootTableLoadingCallback.EVENT.register(
            (resourceManager, lootManager, id, supplier, setter) -> {
                HashSet<String> hs = new HashSet<String>();
                Collections.addAll(hs, "blocks/grass", "blocks/tall_grass", "blocks/fern", "blocks/large_fern");
                if (hs.contains(id.getPath())) {
                    FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder
                        .builder()
                        .rolls(UniformLootNumberProvider.create(0, 1))
                        .withCondition(RandomChanceLootCondition.builder(dropChance).build())
                        .withEntry(ItemEntry.builder(FMItems.flaxSeeds).build());
                    supplier.withPool(poolBuilder.build());
                }
            }
        );
    }

    public void configWriter(Path filePath) throws IOException {
        if(!Files.exists(filePath) || Files.size(filePath) == 0) {
            JsonElement element = jankson.toJson(new FMConfig());
            try(Writer writer = Files.newBufferedWriter(filePath, charset, CREATE, WRITE)) {
                element.toJson(writer, HJSON, 0);
                writer.flush(); writer.close();
            } catch(Exception e) {
                e.getStackTrace();
            }
        }
    }

    public JsonObject configReader(Path filePath) throws IOException {
        JsonObject hjson = null;
        if (Files.size(filePath) > 0) {
            try(BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(filePath))) {
                hjson = jankson.load(bis);
                return hjson;
            } catch(Exception e) {
                e.getStackTrace();
            }
        }
        return hjson;
    }

    public Float chanceToDrop(JsonObject json, String key) {
        Float chance = json.get(Float.class, key);
        if (chance > 1.0f) {
            return 1.0f;
        } else if (chance < 0.0f) {
            return 0.0f;
        } else return chance;
    }
}
