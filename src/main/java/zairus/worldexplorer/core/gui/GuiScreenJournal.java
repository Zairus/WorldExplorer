package zairus.worldexplorer.core.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.helpers.RenderHelper;
import zairus.worldexplorer.core.player.CorePlayerManager;
import zairus.worldexplorer.core.util.network.JournalPacket;
import zairus.worldexplorer.core.util.network.JournalPacket.JournalPacketAction;

@SideOnly(Side.CLIENT)
public class GuiScreenJournal
	extends GuiScreen
{
	private static final ResourceLocation bookGuiTextures = new ResourceLocation(WEConstants.TEXTURES_PATH, "textures/gui/zairus_charts.png");
	
	private final EntityPlayer editingPlayer;
	private final ItemStack bookObj;
	private int bookImageWidth = 251;
	private int bookImageHeight = 157;
	private int bookTotalPages = 1;
	private int currPage = 1;
	private journalSections curSection = journalSections.Main;
	private NBTTagList bookPages;
	private String bookTitle = Minecraft.getMinecraft().thePlayer.getDisplayName() + "'s Journal";
	
	private GuiScreenJournal.JournalPageButton buttonNextPage;
	private GuiScreenJournal.JournalPageButton buttonPreviousPage;
	private GuiScreenJournal.JournalPageButton buttonCancel;
	private GuiScreenJournal.JournalPageButton buttonHome;
	
	private float sheetScaleX = 1.4f;
	private float sheetScaleY = 1.4f;
	
	private enum journalSections {
		Main
		,Items
	}
	
	public GuiScreenJournal(EntityPlayer player, ItemStack stack)
	{
		this.editingPlayer = player;
		this.bookObj = stack;
		
		if (stack.hasTagCompound())
		{
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			this.bookPages = nbttagcompound.getTagList("pages", 8);
			
			if (this.bookPages != null)
			{
				this.bookPages = (NBTTagList)this.bookPages.copy();
				this.bookTotalPages = this.bookPages.tagCount();
				
				if (this.bookTotalPages < 1)
				{
					this.bookTotalPages = 1;
				}
			}
		}
		
		if (this.bookPages == null)
		{
			this.bookPages = new NBTTagList();
			this.bookPages.appendTag(new NBTTagString(""));
			this.bookTotalPages = 1;
		}
		
		if (!this.bookObj.hasTagCompound())
			this.bookObj.setTagCompound(new NBTTagCompound());
		
		if (this.bookObj.getTagCompound().hasKey("JournalTitle"))
		{
			this.bookTitle = this.bookObj.getTagCompound().getString("JournalTitle");
		}
		else
		{
			WorldExplorer.packetPipeline.sendToServer(new JournalPacket(JournalPacketAction.ACTION_JOURNAL_TITLE));
			this.bookObj.getTagCompound().setString("JournalTitle", this.bookTitle);
		}
		
		CorePlayerManager.checkInitialize(player);
		WorldExplorer.packetPipeline.sendToServer(new JournalPacket(JournalPacketAction.ACTION_PLAYER_INIT));
	}
	
	public void updateScreen()
	{
		super.updateScreen();
	}
	
	@SuppressWarnings("unchecked")
	public void initGui()
	{
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		
		int rightMargin = 13;
		int buttonSpacing = 5;
		int buttonWidth = 21;
		int bottomMargin = 31;
		
		//this.editingPlayer.getEntityData();
		
		float sheetScaleX = 1.4F;
		float sheetScaleY = 1.4F;
		
		int left = (int)(((float)width - scaleWidthF()) / 2.0F);
		
		int scaledWidth = (int)((float)this.bookImageWidth * sheetScaleX) + left;
		int scaledHeight = 10 + (int)((float)this.bookImageHeight * sheetScaleY);
		
		buttonList.add(buttonCancel = new GuiScreenJournal.JournalPageButton(0, scaledWidth - rightMargin - buttonSpacing - (buttonWidth * 3), scaledHeight - bottomMargin, 2));
		buttonList.add(buttonPreviousPage = new GuiScreenJournal.JournalPageButton(1, scaledWidth - rightMargin - buttonSpacing - (buttonWidth * 2), scaledHeight - bottomMargin, 1));
		buttonList.add(buttonNextPage = new GuiScreenJournal.JournalPageButton(2, scaledWidth - rightMargin - buttonWidth, scaledHeight - bottomMargin, 0));
		
		buttonList.add(buttonHome = new GuiScreenJournal.JournalPageButton(3, (this.width / 2) + 5, scaledHeight - bottomMargin, 63, 9, 1.0F, 1.0F, 6, "Home"));
		
		for (int i = 0; i < 4; ++i)
			buttonList.add(new GuiScreenJournal.JournalPageButton(i + 4, 0, 0, 18, 25, sheetScaleX, sheetScaleY, 7, ""));
		
		updateButtons();
	}
	
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
	
	private void updateButtons()
	{
		int left = (int)(((float)width - scaleWidthF()) / 2.0f);
		int top = 10;
		
		switch(this.curSection)
		{
			case Items:
				this.buttonCancel.xPosition = left + ((int)scaleWidthF() - buttonCancel.width) - 10;
				this.buttonCancel.yPosition = top + ((int)scaleHeightF() - buttonCancel.height) - 4;
				this.buttonCancel.visible = true;
				
				this.buttonPreviousPage.visible = false;
				this.buttonNextPage.visible = false;
				
				this.buttonHome.visible = true;
				break;
			default:
				this.buttonCancel.xPosition = left + ((int)scaleWidthF() - buttonCancel.width) - 10;
				this.buttonCancel.yPosition = top + ((int)scaleHeightF() - buttonCancel.height) - 4;
				this.buttonCancel.visible = true;
				
				this.buttonPreviousPage.visible = false;
				this.buttonNextPage.visible = false;
				
				this.buttonHome.visible = false;
				break;
		}
	}
	
	protected void actionPerformed(GuiButton button)
	{
		if (button.enabled)
		{
			switch(button.id)
			{
    			case 1:
    				this.currPage -= 1;
    				
    				if (this.currPage <= 0)
    					this.currPage = 0;
    				
    				break;
    			case 2:
    				this.currPage += 1;
    				break;
    			case 3:
    				this.curSection = journalSections.Main;
    				this.currPage = 0;
    				break;
    			case 4:
    				this.curSection = journalSections.Items;
    				this.currPage = 0;
    				break;
    			case 5:
    				this.currPage = 0;
    				break;
    			case 6:
    				this.currPage = 1;
    				break;
    			default:
    				this.mc.displayGuiScreen(new GuiScreenEquipment(this.editingPlayer, this.editingPlayer.worldObj));
    				break;
    		}
			
			this.updateButtons();
		}
	}
	
	protected void keyTyped(char keyChar, int p_73869_2_)
	{
		super.keyTyped(keyChar, p_73869_2_);
	}
	
	private float scaleWidthF()
	{
		return (float)bookImageWidth * sheetScaleX;
	}
	
	private float scaleHeightF()
	{
		return (float)bookImageHeight * sheetScaleY;
	}
	
	public void setScale(float xScale, float yScale)
	{
		sheetScaleX = xScale;
		sheetScaleY = yScale;
	}
	
	public void drawScreen(int screenX, int screenY, float f1)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(bookGuiTextures);
		
		int left = (int)(((float)width - scaleWidthF()) / 2.0f);
		int top = 10;
		
		((JournalPageButton)buttonList.get(4)).width = 18;
		((JournalPageButton)buttonList.get(4)).height = 25;
		((JournalPageButton)buttonList.get(4)).xPosition = left - 10;
		((JournalPageButton)buttonList.get(4)).yPosition = top;
		
		GL11.glScalef(1.0F * sheetScaleX, 1.0F * sheetScaleY, 0.0F);
		this.drawTexturedModalRect((int)((float)left / sheetScaleX), (int)((float)top / sheetScaleY), 3, 1, this.bookImageWidth, this.bookImageHeight);
		GL11.glScalef(1.0F / sheetScaleX, 1.0F / sheetScaleY, 0.0F);
		
		String sectionTitle;
		
		String s1 = "";
		
		super.drawScreen(screenX, screenY, f1);
		
		int leftMargin;
		int topMargin;
		int lineHeight;
		
		int secondPageLeft;
		
		secondPageLeft = (int)((float)left + (((float)this.bookImageWidth * sheetScaleX) / 2.0F));
		
		leftMargin = secondPageLeft;
		leftMargin = 30;
		topMargin = 30;
		lineHeight = 14;
		
		switch(this.curSection)
		{
			case Items:
				sectionTitle = "Items";
				this.fontRendererObj.drawString(sectionTitle, left + leftMargin, top + topMargin, 0);
				
				s1 = "";
				this.fontRendererObj.drawSplitString(s1, left + leftMargin, top + topMargin + lineHeight, 180, 0);
				break;
			default:
				sectionTitle = this.bookTitle;
				this.fontRendererObj.drawString(sectionTitle, left + leftMargin, top + topMargin, 0);
				
				s1 = "";
				this.fontRendererObj.drawSplitString(s1, left + leftMargin, top + topMargin + lineHeight, 180, 0);
				break;
		}
		
		leftMargin = 40;
		
		int bottomRowTop = (int)((float)top + ((float)this.bookImageHeight * sheetScaleY) - 22);
		
		this.drawItemStack(new ItemStack(Items.compass), left + leftMargin, bottomRowTop, "");
		this.drawItemStack(new ItemStack(Items.clock), left + leftMargin + 20, bottomRowTop, "");
	}
	
	public int getCurrentPage()
	{
		return this.currPage;
	}
	
	private void drawItemStack(ItemStack stack, int x, int y, String text)
	{
		RenderHelper.drawItemStack(stack, x, y, text);
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	static class JournalPageButton extends GuiButton
	{
		private final int type /**/;
		
		private int width = 0;
		private int height = 0;
		private float scaleWidth = 1.0F;
		private float scaleHeight = 1.0F;
		private String bText;
		
		public JournalPageButton(int buttonId, int posX, int posY, int buttonType)
		{
			super(buttonId, posX, posY, 21, 18, "");
			this.type = buttonType;
			this.width = 21;
			this.height = 18;
		}
		
		public JournalPageButton(int buttonId, int posX, int posY, int w, int h, float scaleW, float scaleH, int buttonType, String text)
		{
			super(buttonId, posX, posY, w, h, text);
			this.type = buttonType;
			this.width = w;
			this.height = h;
			this.scaleWidth = scaleW;
			this.scaleHeight = scaleH;
			this.bText = text;
		}
		
		public void drawButton(Minecraft minecraft, int mouseX, int mouseY)
		{
			if (visible)
			{
				boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				minecraft.getTextureManager().bindTexture(GuiScreenJournal.bookGuiTextures);
				
				int k = 0;
				int l = 0;
				
				if (type < 7)
				{
					k = 0;
					l = 160 + (type * this.height);
				}
				else
				{
					k = 86; //86 -- 104; w:18; 104 -- 122
					l = 160;
				}
				
				if (flag)
					k += this.width;
				
				GL11.glScalef(1.0F * this.scaleWidth, 1.0F * this.scaleHeight, 0.0F);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, this.width, this.height);
				GL11.glScalef(1.0F / this.scaleWidth, 1.0F / this.scaleHeight, 0.0F);
				this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, this.bText, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xffffff);
			}
		}
	}
}
