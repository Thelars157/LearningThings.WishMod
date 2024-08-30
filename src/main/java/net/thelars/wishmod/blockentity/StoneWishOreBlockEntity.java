package net.thelars.wishmod.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class StoneWishOreBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final int DEFAULT_INSET = 5;
    private static final int DEFAULT_EXTENSION = 0;
    private static final int MIN_INSET = 0;
    private static final int MAX_INSET = 15;
    private static final int MIN_EXTENSION = 0;
    private static final int MAX_EXTENSION = 16;

    private final Map<Direction, Integer> insets = new EnumMap<>(Direction.class);
    private final Map<Direction, Integer> extensions = new EnumMap<>(Direction.class);

    public StoneWishOreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STONE_WISH_ORE_BLOCK_ENTITY.get(), pos, state);
        resetToDefault();
    }

    public void resetToDefault() {
        for (Direction dir : Direction.values()) {
            insets.put(dir, DEFAULT_INSET);
            extensions.put(dir, DEFAULT_EXTENSION);
        }
        setChanged();
    }

    public int getInset(Direction dir) {
        return insets.getOrDefault(dir, DEFAULT_INSET);
    }

    public int getExtension(Direction dir) {
        return extensions.getOrDefault(dir, DEFAULT_EXTENSION);
    }

    public void increaseInsetOnAdjacentFaces(Direction clickedFace) {
        boolean changed = false;
        for (Direction dir : Direction.values()) {
            if (dir != clickedFace && dir != clickedFace.getOpposite()) {
                int currentInset = getInset(dir);
                if (currentInset < MAX_INSET && currentInset < DEFAULT_INSET) {
                    insets.put(dir, currentInset + 1);
                    changed = true;
                }
            }
        }
        if (changed) {
            setChanged();
        }
    }

    public void decreaseInsetOnAdjacentFaces(Direction clickedFace) {
        boolean changed = false;
        for (Direction dir : Direction.values()) {
            if (dir != clickedFace && dir != clickedFace.getOpposite()) {
                int currentInset = getInset(dir);
                if (currentInset > MIN_INSET) {
                    insets.put(dir, currentInset - 1);
                    changed = true;
                }
            }
        }
        if (changed) {
            setChanged();
        }
    }

    public void increaseExtension(Direction dir) {
        int currentExtension = getExtension(dir);
        if (currentExtension < MAX_EXTENSION) {
            extensions.put(dir, currentExtension + 1);
            setChanged();
        }
    }

    public void decreaseExtension(Direction dir) {
        int currentExtension = getExtension(dir);
        if (currentExtension > MIN_EXTENSION) {
            extensions.put(dir, currentExtension - 1);
            setChanged();
        }
    }

    private boolean isStateValid() {
        for (Direction dir : Direction.values()) {
            Integer inset = insets.get(dir);
            Integer extension = extensions.get(dir);
            if (inset == null || extension == null ||
                    inset < MIN_INSET || inset > MAX_INSET ||
                    extension < MIN_EXTENSION || extension > MAX_EXTENSION) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag insetsTag = tag.getCompound("Insets");
        CompoundTag extensionsTag = tag.getCompound("Extensions");
        for (Direction dir : Direction.values()) {
            insets.put(dir, insetsTag.getInt(dir.getName()));
            extensions.put(dir, extensionsTag.getInt(dir.getName()));
        }
        if (!isStateValid()) {
            LOGGER.warn("Invalid state detected in StoneWishOreBlockEntity at {}. Resetting to default.", worldPosition);
            resetToDefault();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag insetsTag = new CompoundTag();
        CompoundTag extensionsTag = new CompoundTag();
        for (Direction dir : Direction.values()) {
            insetsTag.putInt(dir.getName(), insets.get(dir));
            extensionsTag.putInt(dir.getName(), extensions.get(dir));
        }
        tag.put("Insets", insetsTag);
        tag.put("Extensions", extensionsTag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    public boolean isEmpty() {
        for (Direction dir : Direction.values()) {
            if (getInset(dir) != DEFAULT_INSET || getExtension(dir) != DEFAULT_EXTENSION) {
                return false;
            }
        }
        return true;
    }
}
