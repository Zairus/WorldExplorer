package zairus.worldexplorer.archery.client.renderer.item;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import zairus.worldexplorer.archery.client.model.ModelCrossBow;
import zairus.worldexplorer.archery.items.CrossBow;
import zairus.worldexplorer.archery.items.WEArcheryItems;
import zairus.worldexplorer.archery.items.WEItemRanged;
import zairus.worldexplorer.core.ClientProxy;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.helpers.ColorHelper;

@SideOnly(Side.CLIENT)
public class ItemCrossBowRenderer
	implements IItemRenderer
{
	private static final ResourceLocation blowPipeTextures = new ResourceLocation(WEConstants.CORE_PREFIX, "textures/model/crossbow_textures.png");
	private ModelCrossBow crossBowModel = new ModelCrossBow();
	
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
			
			float angleY = 0.0F;
			float angleZ = 210.0F;
			
			boolean flag = false;
			float stagePercent = 0.0f;
			
			ItemStack arrowStack = null;
			boolean pulling = false;
			boolean hasArrow = false;
			
			if (entity != null && entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)entity;
				
				arrowStack = WEItemRanged.getAmmo(stack, player);
				hasArrow = ((CrossBow)stack.getItem()).getBowHasAwwor(stack);
				pulling = player.getItemInUse() != null || hasArrow;
				
				if (player.getItemInUse() != null)
				{
					flag = true;
					
					float maxUse = 20.0f;
					int useCount = stack.getItem().getMaxItemUseDuration(stack) - player.getItemInUseCount();
					
					stagePercent = (float)useCount / maxUse;
					if (stagePercent > 1.0f)
						stagePercent = 1.0f;
				}
				
				if (hasArrow)
					stagePercent = 1.0f;
			}
			
			if ((type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) && flag)
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
			
			crossBowModel.setStage(stagePercent);
			crossBowModel.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			
			GL11.glTranslatef(offsetX * -1, offsetY * -1, offsetZ * -1);
			GL11.glRotatef(angleY * -1, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(angleZ * -1, 0.0F, 0.0F, 1.0F);
			
			GL11.glPopMatrix();
			
			if (pulling)
			{
				if (arrowStack == null)
					arrowStack = new ItemStack(WEArcheryItems.specialarrow, 1, 1);
				else
					arrowStack = new ItemStack(arrowStack.getItem(), 1, arrowStack.getItemDamage());
				
				GL11.glPushMatrix();
				
				float arrowScale = 2.0f;
				float arrowAngleZ = 75.0f;
				offsetX = 0.42f;
				offsetY = -0.43f;
				offsetZ = 0.0f;
				
				if ((type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) && flag)
				{
					arrowAngleZ -= 90.0f;
					offsetX -= 0.31f;
					offsetY += 0.44f;
				}
				
				EntityItem arrow = new EntityItem(mc.theWorld, 0.0d, 0.0d, 0.0d, arrowStack);
				arrow.hoverStart = 0.0f;
				RenderItem.renderInFrame = true;
				
				GL11.glRotatef(arrowAngleZ, 0.0f, 0.0f, 1.0f);
				GL11.glScalef(arrowScale, arrowScale, arrowScale);
				GL11.glTranslatef(offsetX, offsetY, offsetZ);
				RenderManager.instance.renderEntityWithPosYaw(arrow, 0.0d, 0.0d, 0.0d, 0.0f, 0.0f);
				GL11.glTranslatef(-offsetX, -offsetY, -offsetZ);
				GL11.glScalef(1.0f / arrowScale, 1.0f / arrowScale, 1.0f / arrowScale);
				GL11.glRotatef(-arrowAngleZ, 0.0f, 0.0f, 1.0f);
				
				RenderItem.renderInFrame = false;
				
				GL11.glPopMatrix();
			}
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
