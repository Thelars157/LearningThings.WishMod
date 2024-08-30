package net.thelars.wishmod;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.thelars.wishmod.block.ModBlocks;
import net.thelars.wishmod.blockentity.ModBlockEntities;
import net.thelars.wishmod.item.ModCreativeModTabs;
import net.thelars.wishmod.item.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class WishModCommon {
    public static final String MOD_ID = "wishmod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static boolean showHitboxes = true;

    public static void init(IEventBus modEventBus) {
        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        modEventBus.addListener(WishModCommon::commonSetup);
        modEventBus.addListener(WishModCommon::addCreative);
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        // Common setup code here
    }

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.LAMP_SHARD);
            event.accept(ModItems.LAMP_ASSEMBLY);
        }
    }
}
