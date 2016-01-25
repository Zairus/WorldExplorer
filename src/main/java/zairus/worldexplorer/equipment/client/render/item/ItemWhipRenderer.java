package zairus.worldexplorer.equipment.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import zairus.worldexplorer.core.ClientProxy;
import zairus.worldexplorer.core.helpers.ColorHelper;
import zairus.worldexplorer.equipment.client.model.ModelWhip;
import zairus.worldexplorer.equipment.items.Whip;

@SideOnly(Side.CLIENT)
public class ItemWhipRenderer
	implements IItemRenderer
{
	private static final ResourceLocation whipTextures = new ResourceLocation("worldexplorer", "textures/model/whip_textures.png");
	private ModelWhip whipModel = new ModelWhip();
	private ModelWhip whipBody = new ModelWhip(ModelWhip.WhipPart.partBody);
	private Tessellator tessellator = Tessellator.instance;
	
	private static Minecraft mc = null;
	private static final RenderItem renderItem = new RenderItem();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return (
				type.equals(IItemRenderer.ItemRenderType.EQUIPPED)) 
				|| (type.equals(IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)) 
				|| (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) 
				|| (type.equals(IItemRenderer.ItemRenderType.ENTITY));
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return (
				helper.equals(IItemRenderer.ItemRendererHelper.ENTITY_BOBBING)) 
				|| (helper.equals(IItemRenderer.ItemRendererHelper.ENTITY_ROTATION));
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
	{
		if (mc == null)
		{
			mc = ClientProxy.mc;
		}
		
		IIcon icon = stack.getItem().getIconFromDamage(0);
		int color = stack.getItem().getColorFromItemStack(stack, 0);
		
		switch (type)
		{
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
		case ENTITY:
			float scaleOffsetX = -0.52F;
			float scaleOffsetY = 0.35F;
			float scaleOffsetZ = 0.0F;
			
			if (!((Whip)stack.getItem()).unleashed)
			{
				GL11.glPushMatrix();
				
				icon = ((Whip)stack.getItem()).getIconFromUseTick();
				
				GL11.glTranslatef(scaleOffsetX, scaleOffsetY, scaleOffsetZ);
				
				drawItem(icon, 0.09375F);
				
				GL11.glTranslatef(-scaleOffsetX, -scaleOffsetY, -scaleOffsetZ);
				
				GL11.glPopMatrix();
			}
			
			GL11.glPushMatrix();
			
			mc.renderEngine.bindTexture(whipTextures);
			
			scaleOffsetX = 0.77F;
			scaleOffsetY = 0.08F;
			scaleOffsetZ = -0.105F;
			
			GL11.glTranslatef(scaleOffsetX, scaleOffsetY, scaleOffsetZ);
			GL11.glRotatef(45.0F, 0.0F, 0.0F, 1.0F);
			whipModel.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GL11.glRotatef(-45.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-scaleOffsetX, -scaleOffsetY, -scaleOffsetZ);
			
			GL11.glPopMatrix();
			
			if (((Whip)stack.getItem()).unleashed)
			{
				GL11.glPushMatrix();
				
				Whip usedWhip = ((Whip)stack.getItem());
				mc.renderEngine.bindTexture(whipTextures);
				
				scaleOffsetX = 0.4F;
				scaleOffsetY = 0.50F;
				scaleOffsetZ = -0.08F;
				
				GL11.glTranslatef(scaleOffsetX, scaleOffsetY, scaleOffsetZ);
				
				float rPitch = usedWhip.getCurPitch() - usedWhip.getThrowPitch();
				float rYaw = usedWhip.getCurYaw() - usedWhip.getThrowYaw();
				
				if (type == ItemRenderType.EQUIPPED)
				{
					float swing_perc = (float)usedWhip.getShooter().getItemInUseDuration() / 2.0F;
					if (swing_perc > 1.0F)
						swing_perc = 1;
					
					GL11.glRotatef(rYaw + 5, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(rPitch - 50.0F + (35.0F * (1.0F - usedWhip.getShooter().getSwingProgress(1.0F))), 0.0F, 0.0F, 1.0F);
				}
				else
				{
					float swing_perc = (float)usedWhip.getShooter().getItemInUseDuration() / 5.0F;
					if (swing_perc > 1.0F)
						swing_perc = 1;
					
					GL11.glRotatef(-rYaw - 9.0F - (5 * (1.0F - usedWhip.getPercentaje())), 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(rPitch - 47.0F + (120.0F * (1.0F - usedWhip.getShooter().getSwingProgress(1.0F))), 0.0F, 0.0F, 1.0F); // - (13.0F * (1.0F - usedWhip.getPercentaje()))
				}
				
				GL11.glScaled(1.0F, ((Whip)stack.getItem()).getWhipTipDistance(), 1.0F);
				
				whipBody.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
				
				GL11.glPopMatrix();
			}
			break;
		case INVENTORY:
			icon = stack.getItem().getIconFromDamage(0);
			
			ColorHelper.glSetColor(color, 1.0F);
			
			GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            
            renderItem.renderIcon(0, 0, icon, 16, 16);
            
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_LIGHTING);
			break;
		default:
			break;
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
