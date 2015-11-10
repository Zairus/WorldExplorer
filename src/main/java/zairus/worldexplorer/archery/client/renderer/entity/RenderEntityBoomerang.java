package zairus.worldexplorer.archery.client.renderer.entity;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.ENTITY;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.ENTITY_BOBBING;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.ENTITY_ROTATION;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import zairus.worldexplorer.archery.entity.EntityBoomerang;
import zairus.worldexplorer.archery.items.WEArcheryItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityBoomerang
	extends Render
{
	private static final ResourceLocation boomerangTextures = new ResourceLocation("worldexplorer", "textures/entity/entity_boomerang.png");
	private RenderBlocks renderBlocksRi = new RenderBlocks();
	private Random random = new Random();
    public boolean renderWithColor = true;
    public static boolean renderInFrame = false;
    private float boomerangRotate = 1.0F;
    
	public void doRender(EntityBoomerang boomerang, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		ItemStack itemstack = (boomerang.getThrownBoomerang() == null)? new ItemStack(WEArcheryItems.boomerang) : boomerang.getThrownBoomerang();
		
        if (itemstack.getItem() != null)
        {
            this.bindEntityTexture(boomerang);
            TextureUtil.func_152777_a(false, false, 1.0F);
            this.random.setSeed(187L);
            GL11.glPushMatrix();
            float f2 = 0F;
            float f3 = (((float)0 + p_76986_9_) / 20.0F + 0) * (180F / (float)Math.PI);
            byte b0 = 1;
            
            GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_ + f2, (float)p_76986_6_);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float f6;
            float f7;
            int k;
            
            GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
            
            boomerangRotate -= 20.0F;
            GL11.glRotatef(boomerangRotate, 0.0F, 0.0F, 1.0F);
            
            if (renderEBoomerang(boomerang, itemstack, f2, f3, random, renderManager.renderEngine, field_147909_c, b0))
            {
                ;
            }
            else // Code Style break here to prevent the patch from editing this line
            if (itemstack.getItemSpriteNumber() == 0 && itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType()))
            {
                Block block = Block.getBlockFromItem(itemstack.getItem());
                GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
                
                if (renderInFrame)
                {
                    GL11.glScalef(1.25F, 1.25F, 1.25F);
                    GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }
                
                float f9 = 0.25F;
                k = block.getRenderType();
                
                if (k == 1 || k == 19 || k == 12 || k == 2)
                {
                    f9 = 0.5F;
                }
                
                if (block.getRenderBlockPass() > 0)
                {
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                }
                
                GL11.glScalef(f9, f9, f9);
                
                for (int l = 0; l < b0; ++l)
                {
                    GL11.glPushMatrix();

                    if (l > 0)
                    {
                        f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
                        f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
                        float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
                        GL11.glTranslatef(f6, f7, f8);
                    }

                    this.renderBlocksRi.renderBlockAsItem(block, itemstack.getItemDamage(), 1.0F);
                    GL11.glPopMatrix();
                }
                
                if (block.getRenderBlockPass() > 0)
                {
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
            else
            {
                float f5;

                if (/*itemstack.getItemSpriteNumber() == 1 &&*/ itemstack.getItem().requiresMultipleRenderPasses())
                {
                    if (renderInFrame)
                    {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    }
                    else
                    {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }
                    
                    for (int j = 0; j < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++j)
                    {
                        this.random.setSeed(187L);
                        IIcon iicon1 = itemstack.getItem().getIcon(itemstack, j);
                        
                        if (this.renderWithColor)
                        {
                            k = itemstack.getItem().getColorFromItemStack(itemstack, j);
                            f5 = (float)(k >> 16 & 255) / 255.0F;
                            f6 = (float)(k >> 8 & 255) / 255.0F;
                            f7 = (float)(k & 255) / 255.0F;
                            GL11.glColor4f(f5, f6, f7, 1.0F);
                            this.renderDroppedItem(boomerang, iicon1, b0, p_76986_9_, f5, f6, f7, j);
                        }
                        else
                        {
                            this.renderDroppedItem(boomerang, iicon1, b0, p_76986_9_, 1.0F, 1.0F, 1.0F,  j);
                        }
                    }
                }
                else
                {
                    if (itemstack != null && itemstack.getItem() instanceof ItemCloth)
                    {
                        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                        GL11.glEnable(GL11.GL_BLEND);
                        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    }
                    
                    if (renderInFrame)
                    {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    }
                    else
                    {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }
                    
                    IIcon iicon = itemstack.getIconIndex();
                    
                    if (this.renderWithColor)
                    {
                        int i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
                        float f4 = (float)(i >> 16 & 255) / 255.0F;
                        f5 = (float)(i >> 8 & 255) / 255.0F;
                        f6 = (float)(i & 255) / 255.0F;
                        this.renderDroppedItem(boomerang, iicon, b0, p_76986_9_, f4, f5, f6);
                    }
                    else
                    {
                        this.renderDroppedItem(boomerang, iicon, b0, p_76986_9_, 1.0F, 1.0F, 1.0F);
                    }

                    if (itemstack != null && itemstack.getItem() instanceof ItemCloth)
                    {
                        GL11.glDisable(GL11.GL_BLEND);
                    }
                }
            }
            
            if (boomerang.getInventory().size() > 0)
            {
            	Tessellator tessellator = Tessellator.instance;
            	
            	float f14;
                float f15;
                float f4;
                float f5;
                float f9 = 0.0625F;
                
            	for (int i = 0; i < boomerang.getInventory().size(); i++)
            	{
            		IIcon stackIcon = boomerang.getInventory().get(i).getItem().getIconFromDamage(0);
            		
            		f14 = ((IIcon)stackIcon).getMinU();
                    f15 = ((IIcon)stackIcon).getMaxU();
                    f4 = ((IIcon)stackIcon).getMinV();
                    f5 = ((IIcon)stackIcon).getMaxV();
                    
            		ItemRenderer.renderItemIn2D(
            				tessellator
            				, f15
            				, f4
            				, f14
            				, f5
            				, ((IIcon)stackIcon).getIconWidth()
            				, ((IIcon)stackIcon).getIconHeight()
            				, f9);
            	}
            }
            
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            this.bindEntityTexture(boomerang);
            TextureUtil.func_147945_b();
        }
	}
	
	@Override
	public void doRender(Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		doRender((EntityBoomerang)entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
	
	protected ResourceLocation getEntityTexture(EntityBoomerang boomerang)
	{
		return boomerangTextures;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return this.getEntityTexture((EntityBoomerang)entity);
	}
	
	private void renderDroppedItem(EntityBoomerang boomerang, IIcon icon, int renderPass, float angle, float colorRed, float colorGreen, float colorBlue)
    {
        this.renderDroppedItem(boomerang, icon, renderPass, angle, colorRed, colorGreen, colorBlue, 0);
    }
	
	private void renderDroppedItem(EntityBoomerang boomerang, IIcon icon, int renderPasses, float angle, float colorRed, float colorGreen, float colorBlue, int pass)
	{
		renderDroppedItem(icon, renderPasses, angle, colorRed, colorGreen, colorBlue, pass);
	}
	
	private void renderDroppedItem(IIcon icon, int renderPasses, float angle, float colorRed, float colorGreen, float colorBlue, int pass)
	{
		Tessellator tessellator = Tessellator.instance;

        if (icon == null)
        {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = boomerangTextures;
            icon = ((TextureMap)texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        float f14 = ((IIcon)icon).getMinU();
        float f15 = ((IIcon)icon).getMaxU();
        float f4 = ((IIcon)icon).getMinV();
        float f5 = ((IIcon)icon).getMaxV();
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        float f10;

        if (this.renderManager.options.fancyGraphics)
        {
            GL11.glPushMatrix();

            if (renderInFrame)
            {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GL11.glRotatef((((float)0F + angle) / 20.0F + 0F) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            float f9 = 0.0625F;
            f10 = 0.021875F;
            ItemStack itemstack = new ItemStack(WEArcheryItems.boomerang);
            byte b0 = 1;
            
            GL11.glTranslatef(-f7, -f8, -((f9 + f10) * (float)b0 / 2.0F));

            for (int k = 0; k < b0; ++k)
            {
                // Makes items offset when in 3D, like when in 2D, looks much better. Considered a vanilla bug...
                if (k > 0)
                {
                    float x = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float y = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    //float z = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    GL11.glTranslatef(x, y, f9 + f10);
                }
                else
                {
                    GL11.glTranslatef(0f, 0f, f9 + f10);
                }

                if (itemstack.getItemSpriteNumber() == 0)
                {
                    this.bindTexture(TextureMap.locationBlocksTexture);
                }
                else
                {
                    this.bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(colorRed, colorGreen, colorBlue, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, f15, f4, f14, f5, ((IIcon)icon).getIconWidth(), ((IIcon)icon).getIconHeight(), f9);

                if (itemstack.hasEffect(pass))
                {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    this.renderManager.renderEngine.bindTexture(boomerangTextures);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    float f11 = 0.76F;
                    GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    float f12 = 0.125F;
                    GL11.glScalef(f12, f12, f12);
                    float f13 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(f13, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(f12, f12, f12);
                    f13 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-f13, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }

            GL11.glPopMatrix();
        }
        else
        {
            for (int l = 0; l < renderPasses; ++l)
            {
                GL11.glPushMatrix();

                if (l > 0)
                {
                    f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f10, f16, f17);
                }

                if (!renderInFrame)
                {
                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(colorRed, colorGreen, colorBlue, 1.0F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV((double)(0.0F - f7), (double)(0.0F - f8), 0.0D, (double)f14, (double)f5);
                tessellator.addVertexWithUV((double)(f6 - f7), (double)(0.0F - f8), 0.0D, (double)f15, (double)f5);
                tessellator.addVertexWithUV((double)(f6 - f7), (double)(1.0F - f8), 0.0D, (double)f15, (double)f4);
                tessellator.addVertexWithUV((double)(0.0F - f7), (double)(1.0F - f8), 0.0D, (double)f14, (double)f4);
                tessellator.draw();
                GL11.glPopMatrix();
            }
        }
	}
	
	public static boolean renderEBoomerang(EntityBoomerang entity, ItemStack item, float bobing, float rotation, Random random, TextureManager engine, RenderBlocks renderBlocks, int count)
	{
		IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer((entity.getThrownBoomerang() == null)? item : entity.getThrownBoomerang(), ENTITY);
        if (customRenderer == null)
        {
            return false;
        }

        if (customRenderer.shouldUseRenderHelper(ENTITY, item, ENTITY_ROTATION))
        {
            GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
        }
        if (!customRenderer.shouldUseRenderHelper(ENTITY, item, ENTITY_BOBBING))
        {
            GL11.glTranslatef(0.0F, -bobing, 0.0F);
        }
        boolean is3D = customRenderer.shouldUseRenderHelper(ENTITY, item, BLOCK_3D);

        engine.bindTexture(item.getItemSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
        Block block = item.getItem() instanceof ItemBlock ? Block.getBlockFromItem(item.getItem()) : null;
        if (is3D || (block != null && RenderBlocks.renderItemIn3d(block.getRenderType())))
        {
            int renderType = (block != null ? block.getRenderType() : 1);
            float scale = (renderType == 1 || renderType == 19 || renderType == 12 || renderType == 2 ? 0.5F : 0.25F);
            boolean blend = block != null && block.getRenderBlockPass() > 0;

            if (RenderItem.renderInFrame)
            {
                GL11.glScalef(1.25F, 1.25F, 1.25F);
                GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            if (blend)
            {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            }

            GL11.glScalef(scale, scale, scale);

            for(int j = 0; j < count; j++)
            {
                GL11.glPushMatrix();
                if (j > 0)
                {
                    GL11.glTranslatef(
                        ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / scale,
                        ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / scale,
                        ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / scale);
                }
                customRenderer.renderItem(ENTITY, item, renderBlocks, entity);
                GL11.glPopMatrix();
            }

            if (blend)
            {
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
        else
        {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            customRenderer.renderItem(ENTITY, item, renderBlocks, entity);
        }
        return true;
	}
}
