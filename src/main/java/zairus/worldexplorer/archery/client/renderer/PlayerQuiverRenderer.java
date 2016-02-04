package zairus.worldexplorer.archery.client.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import zairus.worldexplorer.archery.client.model.ModelQuiver;
import zairus.worldexplorer.archery.items.WEArcheryItems;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.client.IPlayerRenderer;
import zairus.worldexplorer.core.helpers.ColorHelper;
import zairus.worldexplorer.core.player.CorePlayerManager;

@SideOnly(Side.CLIENT)
public class PlayerQuiverRenderer
	implements IPlayerRenderer
{
	private ResourceLocation quiverTextures = new ResourceLocation(WEConstants.CORE_PREFIX, "textures/model/quiver.png");
	private ModelQuiver quiverModel = new ModelQuiver();
	
	private static final Tessellator tessellator = Tessellator.instance;
	
	public void render(EntityPlayer player)
	{
		if (!CorePlayerManager.getPlayerEquipmentInventory(player).hasQuiver())
			return;
		
		TextureManager renderManager = Minecraft.getMinecraft().renderEngine;
		
		float offsetX = 0.15f;
		float offsetY = 0.1f;
		float offsetZ = -0.38f;
		
		float rotationY = -(player.renderYawOffset);
		float rotationZ = 200f;
		
		renderManager.bindTexture(quiverTextures);
		
		GL11.glRotatef(rotationY, 0.0f, 1.0f, 0.0F);
		GL11.glRotatef(rotationZ, 0.0F, 0.0F, 1.0F);
		
		GL11.glTranslatef(offsetX, offsetY, offsetZ);
		quiverModel.render(player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GL11.glTranslatef(-offsetX, -offsetY, -offsetZ);
		
		GL11.glRotatef(-rotationZ, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(-rotationY, 0.0f, 1.0f, 0.0F);
		
		ItemStack arrow = new ItemStack(WEArcheryItems.specialarrow, 1, 1);
		IIcon icon = arrow.getItem().getIcon(arrow, 0);
		
		if (icon != null)
		{
			GL11.glPushMatrix();
			
			int color = arrow.getItem().getColorFromItemStack(arrow, 0);
			ColorHelper.glSetColor(color, 1.0F);
			
			offsetX = -0.30f;
			offsetY = -1.0f;
			offsetZ = 0.5f;
			
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
			drawItem(icon, 0.9f);
			GL11.glTranslatef(-offsetX, -offsetY, -offsetZ);
			
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_LIGHTING);
			
			GL11.glPopMatrix();
		}
	}
	
	private void drawItem(IIcon icon, float thickness)
	{
		float xStart = icon.getMinU();
		float xEnd = icon.getMaxU();
		float yStart = icon.getMinV();
		float yEnd = icon.getMaxV();
		int height = icon.getIconHeight();
		int width = icon.getIconWidth();
		
		ItemRenderer.renderItemIn2D(tessellator, xEnd, yStart, xStart, yEnd, width, height, thickness);
	}
}
