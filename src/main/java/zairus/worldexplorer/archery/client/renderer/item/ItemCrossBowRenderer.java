package zairus.worldexplorer.archery.client.renderer.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import zairus.worldexplorer.archery.client.model.ModelCrossBow;
import zairus.worldexplorer.archery.items.CrossBow;
import zairus.worldexplorer.core.ClientProxy;
import zairus.worldexplorer.core.helpers.ColorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemCrossBowRenderer
	implements IItemRenderer
{
	private static final ResourceLocation blowPipeTextures = new ResourceLocation("worldexplorer", "textures/model/crossbow_textures.png");
	private ModelCrossBow crossBowModel = new ModelCrossBow();
	
	private static Minecraft mc = null;
	private static final RenderItem renderItem = new RenderItem();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return (
				type.equals(IItemRenderer.ItemRenderType.EQUIPPED)) 
				|| (type.equals(IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)) 
				|| (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) 
				|| (type.equals(IItemRenderer.ItemRenderType.ENTITY));
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return (
				helper.equals(IItemRenderer.ItemRendererHelper.ENTITY_BOBBING)) 
				|| (helper.equals(IItemRenderer.ItemRendererHelper.ENTITY_ROTATION));
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		if (mc == null)
		{
			mc = ClientProxy.mc;
		}
		
		switch (type)
		{
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
		case ENTITY:
			float offsetX = -0.90F;
			float offsetY = -1.10F;
			float offsetZ = 0.00F;
			
			float angleY = 0.0F;
			float angleZ = 210.0F;
			
			CrossBow crossBowItem = (CrossBow)stack.getItem();
			
			if ((type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) && (crossBowItem.getUseTick() > 0))
			{
				angleY -= 90.0F;
				angleZ -= 90.0F;
				offsetX += 1.0F;
				offsetY -= 0.6F;
			}
			
			GL11.glPushMatrix();
			
			mc.renderEngine.bindTexture(blowPipeTextures);
			
			GL11.glRotatef(angleZ, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(angleY, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
			crossBowModel.setStage(crossBowItem.getStagePercent(stack));
			crossBowModel.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GL11.glTranslatef(offsetX * -1, offsetY * -1, offsetZ * -1);
			GL11.glRotatef(angleY * -1, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(angleZ * -1, 0.0F, 0.0F, 1.0F);
			
			GL11.glPopMatrix();
			break;
		case INVENTORY:
			IIcon icon = stack.getItem().getIconFromDamage(0);
			int color = stack.getItem().getColorFromItemStack(stack, 0);
			
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
}
