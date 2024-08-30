package net.thelars.wishmod.block;

import net.minecraft.world.level.material.PushReaction;
import net.thelars.wishmod.forge.WishModForge;
import net.thelars.wishmod.block.custom.StoneWishOreBlock;
import net.thelars.wishmod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;



public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, WishModForge.MOD_ID);

    public static final RegistryObject<Block> STONEWISHOREBLOCK = registerBlock("stonewishore",
        () -> new StoneWishOreBlock(BlockBehaviour.Properties.copy(Blocks.STONE)

                .pushReaction(PushReaction.PUSH_ONLY)//i know this is not the best but hey
                .forceSolidOn()
                .strength(0.5f)
                .sound(SoundType.AMETHYST)
                .noOcclusion()
                .requiresCorrectToolForDrops())); // This makes it require a shovel




// this is a diffrent way to make the effect might not need to use
    /*


    public static final RegistryObject<Block> FLOW_CONTROL_BLOCK = BLOCK//import net.thelars.wishmod.block.custom.FlowControlBlock;S.register("flow_control_block",
                 () -> new FlowControlBlock(Block.Properties.copy(Blocks.STONE)));  */


    /*this will add a new block to the registry just copy and uncomment */
    /*
    public static final RegistryObject<Block> thisiswhereyouputyournewblocknamenospacesnocaps = registerBlock("thisiswhereyouputyourne//import net.thelars.wishmod.block.custom.FlowControlBlock;wblocknamenospacesnocaps",
        ()-> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.AMETHYST)));
        */


    //evey thing down form here should not chang I think
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}