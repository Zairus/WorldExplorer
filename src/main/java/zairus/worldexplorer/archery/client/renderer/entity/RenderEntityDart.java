package zairus.worldexplorer.archery.client.renderer.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import zairus.worldexplorer.archery.entity.EntityDart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityDart
	extends Render
{
	private static final ResourceLocation arrowTextures = new ResourceLocation("worldexplorer", "textures/model/dart_textures.png");
    
    public void doRender(EntityDart entity, double x, double y, double z, float p_76986_8_, float p_76986_9_)
    {
    	GL11.glPushMatrix();
    	
    	this.bindTexture(arrowTextures);
    	GL11.glTranslatef((float)x, (float)y, (float)z);
    	GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    	float var10 = 0.5f;
    	GL11.glScalef(var10 / 1.0F, var10 / 1.0F, var10 / 1.0F);
    	
    	GL11.glRotatef(((entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * p_76986_9_) - 90.0F), 0.0F, 1.0F, 0.0F);
    	GL11.glRotatef(((entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * p_76986_9_) - 45.0F) * -1.0F, 0.0F, 0.0F, 1.0F);
    	
    	GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
    	renderFace();
    	
    	GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    	GL11.glTranslatef(0.5F, 0, -0.5F);
    	renderFace();
    	
    	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    	GL11.glPopMatrix();
    }
    
    private void renderFace()
    {
    	Tessellator var12 = Tessellator.instance;
    	
    	float var13 = 0F;
    	float var14 = 1F;
    	float var15 = 0F;
    	float var16 = 1F;
    	float var17 = 1.0F;
    	float var18 = 1F;
    	float var19 = 0.25F;
    	
    	var12.startDrawingQuads();
    	var12.setNormal(0.0F, 1.0F, 0.0F);
    	var12.addVertexWithUV((double)(0.0F - var18), (double)(0.0F - var19), 0.0D, (double)var13, (double)var16);
    	var12.addVertexWithUV((double)(var17 - var18), (double)(0.0F - var19), 0.0D, (double)var14, (double)var16);
    	var12.addVertexWithUV((double)(var17 - var18), (double)(1.0F - var19), 0.0D, (double)var14, (double)var15);
    	var12.addVertexWithUV((double)(0.0F - var18), (double)(1.0F - var19), 0.0D, (double)var13, (double)var15);
    	var12.draw();
    	
    	var12.startDrawingQuads();
    	var12.setNormal(0.0F, 1.0F, 0.0F);
    	var12.addVertexWithUV((double)(var17 - var18), (double)(0.0F - var19), 0.0D, (double)var14, (double)var16);
    	var12.addVertexWithUV((double)(0.0F - var18), (double)(0.0F - var19), 0.0D, (double)var13, (double)var16);
    	var12.addVertexWithUV((double)(0.0F - var18), (double)(1.0F - var19), 0.0D, (double)var13, (double)var15);
    	var12.addVertexWithUV((double)(var17 - var18), (double)(1.0F - var19), 0.0D, (double)var14, (double)var15);
    	var12.draw();
    }
    
    protected ResourceLocation getEntityTexture(EntityDart p_110775_1_)
    {
        return arrowTextures;
    }
    
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityDart)p_110775_1_);
    }
    
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityDart)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}