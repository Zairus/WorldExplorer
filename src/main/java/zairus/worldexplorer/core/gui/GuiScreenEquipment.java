package zairus.worldexplorer.core.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.inventory.ContainerEquipment;

public class GuiScreenEquipment
	extends GuiContainer
{
	private static final ResourceLocation guiTextures = new ResourceLocation(WEConstants.CORE_PREFIX, "textures/gui/explorer_equipment.png");
	private float x1;
	private float y1;
	
	public GuiScreenEquipment(EntityPlayer player, World world)
	{
		super(new ContainerEquipment(player, world));
		
		this.mc = Minecraft.getMinecraft();
		this.xSize = 176;
		this.ySize = 174;
	}
	
	public void updateScreen()
	{
		super.updateScreen();
	}
	
	public void initGui()
	{
		super.initGui();
		
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		
		this.updateButtons();
	}
	
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
	
	private void updateButtons()
	{
		;
	}
	
	protected void actionPerformed(GuiButton button)
	{
		;
	}
	
	protected void keyTyped(char keyChar, int p_73869_2_)
	{
		super.keyTyped(keyChar, p_73869_2_);
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		x1 = (float)p_73863_1_;
		y1 = (float)p_73863_2_;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiTextures);
		
		int left = (this.width - this.xSize) / 2;
		int top = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
		
		int k = this.guiLeft;
		int l = this.guiTop;
		
		renderPlayer(k + 87, l + 75, 30, (float)(k + 51) - this.x1, (float)(l + 75 - 50) - this.y1, this.mc.thePlayer);
	}
	
	public static void renderPlayer(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)p_147046_0_, (float)p_147046_1_, 50.0F);
        GL11.glScalef((float)(-p_147046_2_), (float)p_147046_2_, (float)p_147046_2_);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = p_147046_5_.renderYawOffset;
        float f3 = p_147046_5_.rotationYaw;
        float f4 = p_147046_5_.rotationPitch;
        float f5 = p_147046_5_.prevRotationYawHead;
        float f6 = p_147046_5_.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_147046_5_.renderYawOffset = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 20.0F;
        p_147046_5_.rotationYaw = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 40.0F;
        p_147046_5_.rotationPitch = -((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GL11.glTranslatef(0.0F, p_147046_5_.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        p_147046_5_.renderYawOffset = f2;
        p_147046_5_.rotationYaw = f3;
        p_147046_5_.rotationPitch = f4;
        p_147046_5_.prevRotationYawHead = f5;
        p_147046_5_.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
