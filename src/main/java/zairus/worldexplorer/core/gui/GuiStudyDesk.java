package zairus.worldexplorer.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.inventory.ContainerStudyDesk;
import zairus.worldexplorer.core.tileentity.TileEntityDesk;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiStudyDesk
	extends GuiContainer
{
	private static final ResourceLocation guiStudyDeskTextures = new ResourceLocation(WEConstants.TEXTURES_PATH, "textures/gui/study_desk_crafting.png");
	
	public GuiStudyDesk(InventoryPlayer playerInv, TileEntityDesk deskInv, World world)
	{
		super(new ContainerStudyDesk(playerInv, deskInv, world));
		
		this.xSize = 256;
		this.ySize = 240;
		
		this.allowUserInput = false;
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
		//
	}
	
	protected void actionPerformed(GuiButton button)
	{
		//
	}
	
	protected void keyTyped(char keyChar, int p_73869_2_)
	{
		super.keyTyped(keyChar, p_73869_2_);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiStudyDeskTextures);
		
		int left = (this.width - this.xSize) / 2;
		int top = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
		
		String s = "Study Desk";
		this.fontRendererObj.drawString(s, left + 9, top + 15, 0);
	}
	
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}
