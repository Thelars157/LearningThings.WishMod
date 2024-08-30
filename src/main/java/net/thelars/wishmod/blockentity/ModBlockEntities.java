package net.thelars.wishmod.blockentity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.thelars.wishmod.WishModCommon;
import net.thelars.wishmod.block.ModBlocks;
import net.thelars.wishmod.blockentity.StoneWishOreBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WishModCommon.MOD_ID);

    public static final RegistryObject<BlockEntityType<StoneWishOreBlockEntity>> STONE_WISH_ORE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("stone_wish_ore_block_entity",
                    () -> BlockEntityType.Builder.of(StoneWishOreBlockEntity::new,
                            ModBlocks.STONEWISHOREBLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
