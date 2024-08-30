/*package net.thelars.wishmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.thelars.wishmod.WishMod;
import net.thelars.wishmod.block.custom.StoneWishOreBlock;
import net.thelars.wishmod.blockentity.StoneWishOreBlockEntity;

public class StoneWishOreRenderer implements BlockEntityRenderer<StoneWishOreBlockEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(WishMod.MOD_ID, "block/stonewishoreblock");
    private final StoneWishOreBlock block;

    public StoneWishOreRenderer(BlockEntityRendererProvider.Context context) {
        this.block = (StoneWishOreBlock) WishMod.STONE_WISH_ORE_BLOCK.get();
    }

    @Override
    public void render(StoneWishOreBlockEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        VoxelShape shape = block.calculateShape(blockEntity);
        renderShape(matrixStack, buffer, shape, combinedLight, combinedOverlay);
    }

    private void renderShape(PoseStack matrixStack, MultiBufferSource buffer, VoxelShape shape, int combinedLight, int combinedOverlay) {
        VertexConsumer builder = buffer.getBuffer(RenderType.cutout());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(TEXTURE);

        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            renderBox(matrixStack, builder, sprite, minX, minY, minZ, maxX, maxY, maxZ, combinedLight, combinedOverlay);
        });
    }

    private void renderBox(PoseStack matrixStack, VertexConsumer builder, TextureAtlasSprite sprite,
                           double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
                           int combinedLight, int combinedOverlay) {
        // Render each face of the box
        for (Direction direction : Direction.values()) {
            renderFace(matrixStack, builder, sprite, direction, minX, minY, minZ, maxX, maxY, maxZ, combinedLight, combinedOverlay);
        }
    }

    private void renderFace(PoseStack matrixStack, VertexConsumer builder, TextureAtlasSprite sprite, Direction direction,
                            double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
                            int combinedLight, int combinedOverlay) {
        // This is a simplified rendering. You might need to adjust UV coordinates based on your texture layout.
        switch (direction) {
            case DOWN:
                addVertex(builder, matrixStack, minX, minY, minZ, sprite.getU0(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, minY, minZ, sprite.getU1(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, minY, maxZ, sprite.getU1(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, minX, minY, maxZ, sprite.getU0(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                break;
            case UP:
                addVertex(builder, matrixStack, minX, maxY, minZ, sprite.getU0(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, minX, maxY, maxZ, sprite.getU0(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, maxY, maxZ, sprite.getU1(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, maxY, minZ, sprite.getU1(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                break;
            case NORTH:
                addVertex(builder, matrixStack, minX, minY, minZ, sprite.getU0(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, minX, maxY, minZ, sprite.getU0(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, maxY, minZ, sprite.getU1(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, minY, minZ, sprite.getU1(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                break;
            case SOUTH:
                addVertex(builder, matrixStack, minX, minY, maxZ, sprite.getU0(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, minY, maxZ, sprite.getU1(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, maxY, maxZ, sprite.getU1(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, minX, maxY, maxZ, sprite.getU0(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                break;
            case WEST:
                addVertex(builder, matrixStack, minX, minY, minZ, sprite.getU0(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, minX, minY, maxZ, sprite.getU1(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, minX, maxY, maxZ, sprite.getU1(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, minX, maxY, minZ, sprite.getU0(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                break;
            case EAST:
                addVertex(builder, matrixStack, maxX, minY, minZ, sprite.getU0(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, maxY, minZ, sprite.getU0(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, maxY, maxZ, sprite.getU1(), sprite.getV0(), direction, combinedLight, combinedOverlay);
                addVertex(builder, matrixStack, maxX, minY, maxZ, sprite.getU1(), sprite.getV1(), direction, combinedLight, combinedOverlay);
                break;
        }
    }

    private void addVertex(VertexConsumer builder, PoseStack matrixStack, double x, double y, double z, float u, float v, Direction direction, int combinedLight, int combinedOverlay) {
        builder.vertex(matrixStack.last().pose(), (float)x, (float)y, (float)z)
                .color(1.0f, 1.0f, 1.0f, 1.0f)
                .uv(u, v)
                .overlayCoords(combinedOverlay)
                .uv2(combinedLight)
                .normal(matrixStack.last().normal(), direction.getStepX(), direction.getStepY(), direction.getStepZ())
                .endVertex();
    }
}
*/  this is a test