package net.thelars.wishmod.forge;

import com.mojang.logging.LogUtils;
import net.thelars.wishmod.WishModCommon;
import net.thelars.wishmod.block.ModBlocks;
import net.thelars.wishmod.blockentity.ModBlockEntities;
import net.thelars.wishmod.item.ModCreativeModTabs;
import net.thelars.wishmod.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.minecraftforge.event.TickEvent;
import net.minecraft.client.Minecraft;

@Mod(WishModCommon.MOD_ID)
public class WishModForge {
    public static final String MOD_ID = "wishmod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static boolean showHitboxes = true;

    public WishModForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Common setup code here
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.LAMP_SHARD);
            event.accept(ModItems.LAMP_ASSEMBLY);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Server starting code here
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Client-side setup code here
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientEventHandler {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.level != null) {
                    minecraft.getEntityRenderDispatcher().setRenderHitBoxes(showHitboxes);
                }
            }
        }
    }
}
