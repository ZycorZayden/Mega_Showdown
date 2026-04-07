package com.github.yajatkaul.mega_showdown.plugin;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.block.custom.ReassemblyUnitBlock;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    public static final ResourceLocation JADE =
            ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "jade");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(ReassemblyComponentProvider.INSTANCE, ReassemblyUnitBlock.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ReassemblyComponentProvider.INSTANCE, ReassemblyUnitBlock.class);
    }
}
