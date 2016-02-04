package zairus.worldexplorer.equipment.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.gui.IGuiOverlay;

@SideOnly(Side.CLIENT)
public class GuiScreenSpyGlass
	extends Gui
	implements IGuiOverlay
{
	private ResourceLocation spyglassOverlayTexture = new ResourceLocation(WEConstants.CORE_PREFIX, "textures/misc/spyglass_overlay.png");
	private Minecraft mc = Minecraft.getMinecraft();
	
	public void draw()
	{
		ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		
		mc.entityRenderer.setupOverlayRendering();
		mc.renderEngine.bindTexture(spyglassOverlayTexture);
		
		float scaleX = ((float)res.getScaledWidth_double() / 256.0f);
		float scaleY = ((float)res.getScaledHeight_double() / 256.0f);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glScalef(scaleX, scaleY, 0.0f);
		this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
		GL11.glScalef(1.0f / scaleX, 1.0f / scaleY, 0.0f);
	}
}
