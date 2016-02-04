package zairus.worldexplorer.equipment.client.render.item;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import zairus.worldexplorer.core.ClientProxy;
import zairus.worldexplorer.core.helpers.ColorHelper;
import zairus.worldexplorer.equipment.client.model.ModelSpyGlass;

@SideOnly(Side.CLIENT)
public class ItemSpyGlassRenderer
	implements IItemRenderer
{
	private static final ResourceLocation textures = new ResourceLocation("worldexplorer", "textures/model/spyglass.png");
	private ModelSpyGlass spyglassModel = new ModelSpyGlass();
	
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
		EntityLivingBase entity = null;
		
		if(data.length > 1)
		{
			if (data[1] instanceof EntityLivingBase)
				entity = (EntityLivingBase) data[1];
		}
		
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
			
			float angle = 210.0F;
			
			if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)
			{
				boolean flag = false;
				
				if (entity instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)entity;
					if (player.getItemInUse() != null)
					{
						flag = true;
					}
					
					if (flag && type== ItemRenderType.EQUIPPED_FIRST_PERSON)
					{
						return;
					}
				}
				
				if (flag)
				{
					angle -= 90.0F;
					offsetY -= 0.5;
					
					if (type == ItemRenderType.EQUIPPED)
					{
						offsetX += 1.05;
						offsetZ += 0.12;
					}
					else
					{
						offsetX += 0.8;
						offsetZ -= 0.4;
					}
				}
			}
			
			GL11.glPushMatrix();
			
			mc.renderEngine.bindTexture(textures);
			
			GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
			spyglassModel.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GL11.glTranslatef(offsetX * -1, offsetY * -1, offsetZ * -1);
			GL11.glRotatef(angle * -1, 0.0F, 0.0F, 1.0F);
			
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
