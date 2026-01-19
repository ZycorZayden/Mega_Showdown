package com.github.yajatkaul.mega_showdown.render;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.render.renderTypes.MSDRenderTypes;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TeraMapLoader implements ResourceManagerReloadListener {
    public static final HashMap<String, ShaderInstance> REGISTRY = new HashMap<>();
    private static final String DIRECTORY = "mega_showdown/tera_map";

    public record TeraMap(Map<String, String> colorMap) {
        public static Codec<TeraMap> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("aspectShaderMap").forGetter(TeraMap::colorMap)
        ).apply(instance, TeraMap::new));
    }

    public static void load() {
        ResourceManager rm = Minecraft.getInstance().getResourceManager();

        REGISTRY.clear();

        Collection<ResourceLocation> resources =
                rm.listResources(DIRECTORY, path -> path.getPath().endsWith(".json")).keySet();

        for (ResourceLocation id : resources) {
            try (var stream = rm.getResource(id).get().open()) {
                TeraMap codec = TeraMap.CODEC.parse(
                        JsonOps.INSTANCE,
                        JsonParser.parseReader(new InputStreamReader(stream))
                ).result().orElseThrow();

                for (Map.Entry<String, String> entrySet : codec.colorMap.entrySet()) {
                    REGISTRY.put(entrySet.getKey(), getColorShaderMap().get(entrySet.getValue()));
                }
            } catch (Exception e) {
                MegaShowdown.LOGGER.error("Failed loading tera map JSON: {}", id, e);
            }
        }

        MegaShowdown.LOGGER.info("Loaded {} custom tera map", REGISTRY.size());
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
        load();
        return ResourceManagerReloadListener.super.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public @NotNull String getName() {
        return "mega_showdown";
    }

    private static final Map<String, ShaderInstance> COLOR_SHADER_MAP = new HashMap<>();

    private static Map<String, ShaderInstance> getColorShaderMap() {
        if (COLOR_SHADER_MAP.isEmpty()) {
            COLOR_SHADER_MAP.put("red", MSDRenderTypes.teraFire);
            COLOR_SHADER_MAP.put("blue", MSDRenderTypes.teraWater);
            COLOR_SHADER_MAP.put("green", MSDRenderTypes.teraGrass);
            COLOR_SHADER_MAP.put("yellow", MSDRenderTypes.teraElectric);
            COLOR_SHADER_MAP.put("brown", MSDRenderTypes.teraGround);
            COLOR_SHADER_MAP.put("light_blue", MSDRenderTypes.teraFlying);
            COLOR_SHADER_MAP.put("purple", MSDRenderTypes.teraDragon);
            COLOR_SHADER_MAP.put("pink", MSDRenderTypes.teraFairy);
            COLOR_SHADER_MAP.put("black", MSDRenderTypes.teraDark);
            COLOR_SHADER_MAP.put("gray", MSDRenderTypes.teraSteel);
            COLOR_SHADER_MAP.put("light_grey", MSDRenderTypes.teraIce);
            COLOR_SHADER_MAP.put("orange", MSDRenderTypes.teraFighting);
            COLOR_SHADER_MAP.put("lime", MSDRenderTypes.teraBug);
            COLOR_SHADER_MAP.put("teal", MSDRenderTypes.teraPoison);
            COLOR_SHADER_MAP.put("indigo", MSDRenderTypes.teraGhost);
            COLOR_SHADER_MAP.put("magenta", MSDRenderTypes.teraPsychic);
            COLOR_SHADER_MAP.put("tan", MSDRenderTypes.teraRock);
            COLOR_SHADER_MAP.put("navy", MSDRenderTypes.teraNormal);
            COLOR_SHADER_MAP.put("white", MSDRenderTypes.teraStellar);
        }
        return COLOR_SHADER_MAP;
    }
}