package teamrtg.highlands.generator.layer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Biomes;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import static net.minecraftforge.common.BiomeManager.BiomeType.*;

public class GenLayerBiomeHighlands extends GenLayer {

    private static final String __OBFID = "CL_00000555";
    private final ChunkProviderSettings field_175973_g;
    private List<BiomeEntry>[] biomes = new ArrayList[BiomeManager.BiomeType.values().length];

    public GenLayerBiomeHighlands(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, String p_i45560_5_) {

        super(p_i45560_1_);
        this.parent = p_i45560_3_;

        for (BiomeManager.BiomeType type : BiomeManager.BiomeType.values()) {
            com.google.common.collect.ImmutableList<BiomeEntry> biomesToAdd = BiomeManager.getBiomes(type);
            int idx = type.ordinal();

            if (biomes[idx] == null) {
                biomes[idx] = new ArrayList<BiomeEntry>();
            }
            if (biomesToAdd != null) {
                biomes[idx].addAll(biomesToAdd);
            }
        }

        this.field_175973_g = null;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {

        int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i1 = 0; i1 < areaHeight; ++i1) {
            for (int j1 = 0; j1 < areaWidth; ++j1) {
                this.initChunkSeed((long) (j1 + areaX), (long) (i1 + areaY));
                int k1 = aint[j1 + i1 * areaWidth];
                int l1 = (k1 & 3840) >> 8;
                k1 &= -3841;

                if (isBiomeOceanic(k1)) {
                    aint1[j1 + i1 * areaWidth] = k1;
                }
                else if (k1 == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
                    aint1[j1 + i1 * areaWidth] = k1;
                }
                else if (k1 == 1) {
                    aint1[j1 + i1 * areaWidth] = Biome.getIdForBiome(getWeightedBiomeEntry(DESERT).biome);
                }
                else if (k1 == 2) {
                    aint1[j1 + i1 * areaWidth] = Biome.getIdForBiome(getWeightedBiomeEntry(WARM).biome);
                }
                else if (k1 == 3) {
                    aint1[j1 + i1 * areaWidth] = Biome.getIdForBiome(getWeightedBiomeEntry(COOL).biome);
                }
                else if (k1 == 4) {
                    aint1[j1 + i1 * areaWidth] = Biome.getIdForBiome(getWeightedBiomeEntry(ICY).biome);
                }
                else {
                    aint1[j1 + i1 * areaWidth] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
                }
            }
        }

        return aint1;
    }

    protected BiomeEntry getWeightedBiomeEntry(BiomeManager.BiomeType type) {

        List<BiomeEntry> biomeList = biomes[type.ordinal()];
        int totalWeight = WeightedRandom.getTotalWeight(biomeList);
        int weight = BiomeManager.isTypeListModded(type) ? nextInt(totalWeight) : nextInt(totalWeight / 10) * 10;
        return (BiomeEntry) WeightedRandom.getRandomItem(biomeList, weight);
    }
}