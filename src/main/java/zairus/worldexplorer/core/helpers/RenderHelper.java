package zairus.worldexplorer.core.helpers;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class RenderHelper
{
	public static void drawItemStack(ItemStack stack, int x, int y, String text)
	{
		Minecraft mc = Minecraft.getMinecraft();
		RenderItem r = RenderItem.getInstance();
		
		float zLev = r.zLevel;
		r.zLevel = 200.0f;
		
		FontRenderer font = null;
		if (stack != null) font = stack.getItem().getFontRenderer(stack);
		if (font == null) font = mc.fontRenderer;
		
		GL11.glTranslatef(0.0f, 0.0f, 32.0f);
		
		r.renderItemIntoGUI(font, mc.getTextureManager(), stack, x, y, false);
		
		r.zLevel = zLev;
		
		GL11.glDisable(GL11.GL_LIGHTING);
	}
}
