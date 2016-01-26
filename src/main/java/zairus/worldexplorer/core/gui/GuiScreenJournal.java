package zairus.worldexplorer.core.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.block.WorldExplorerBlocks;
import zairus.worldexplorer.core.items.WorldExplorerItems;
import zairus.worldexplorer.core.player.CorePlayerManager;
import zairus.worldexplorer.core.util.network.JournalPacket;

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
	private GuiScreenJournal.JournalPageButton buttonItems;
	
	private GuiScreenJournal.JournalPageButton buttonItemJournal;
	private GuiScreenJournal.JournalPageButton buttonItemStudyDesk;
	
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
			WorldExplorer.packetPipeline.sendToServer(new JournalPacket());
			this.bookObj.getTagCompound().setString("JournalTitle", this.bookTitle);
		}
		
		CorePlayerManager.checkInitialize(player);
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
		
		int left = (int)(((float)this.width - ((float)this.bookImageWidth * sheetScaleX)) / 2.0F);
		int top = 10;
		int scaledWidth = (int)((float)this.bookImageWidth * sheetScaleX) + left;
		int scaledHeight = 10 + (int)((float)this.bookImageHeight * sheetScaleY);
		
		this.buttonList.add(this.buttonCancel = new GuiScreenJournal.JournalPageButton(0, scaledWidth - rightMargin - buttonSpacing - (buttonWidth * 3), scaledHeight - bottomMargin, 2));
		this.buttonList.add(this.buttonPreviousPage = new GuiScreenJournal.JournalPageButton(1, scaledWidth - rightMargin - buttonSpacing - (buttonWidth * 2), scaledHeight - bottomMargin, 1));
		this.buttonList.add(this.buttonNextPage = new GuiScreenJournal.JournalPageButton(2, scaledWidth - rightMargin - buttonWidth, scaledHeight - bottomMargin, 0));
		
		this.buttonList.add(this.buttonHome = new GuiScreenJournal.JournalPageButton(3, (this.width / 2) + 5, scaledHeight - bottomMargin, 63, 9, 1.0F, 1.0F, 6, "Home"));
		this.buttonList.add(this.buttonItems = new GuiScreenJournal.JournalPageButton(4, (this.bookImageWidth / 2), 30, 53, 9, 1.0F, 1.0F, 7, "Items"));
		
		this.buttonList.add(this.buttonItemJournal = new GuiScreenJournal.JournalPageButton(5, left + 15, top + 18 + (14 * 2), 100, 18, 1.0F, 1.0F, 4, ""));
		this.buttonList.add(this.buttonItemStudyDesk = new GuiScreenJournal.JournalPageButton(6, left + 15, top + 18 + (14 * 3), 100, 18, 1.0F, 1.0F, 4, ""));
		
		this.updateButtons();
	}
	
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
	
	private void updateButtons()
	{
		switch(this.curSection)
		{
			case Items:
				this.buttonCancel.visible = true;
				this.buttonPreviousPage.visible = false;
				this.buttonNextPage.visible = false;
				
				this.buttonHome.visible = true;
				
				this.buttonItems.visible = false;
				
				this.buttonItemJournal.visible = true;
				this.buttonItemStudyDesk.visible = true;
				
				break;
			default:
				this.buttonCancel.visible = true;
				this.buttonPreviousPage.visible = false;
				this.buttonNextPage.visible = false;
				
				this.buttonHome.visible = false;
				
				this.buttonItems.visible = true;
				this.buttonItems.xPosition = (this.width / 2) + (this.width / 4) - (this.buttonItems.width / 2);
				this.buttonItems.yPosition = 100;
				
				this.buttonItemJournal.visible = false;
				this.buttonItemStudyDesk.visible = false;
				
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
	
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(bookGuiTextures);
		
		float sheetScaleX = 1.4F;
		float sheetScaleY = 1.4F;
		
		int left = (int)(((float)this.width - ((float)this.bookImageWidth * sheetScaleX)) / 2.0F);
		int top = 10;
		
		GL11.glScalef(1.0F * sheetScaleX, 1.0F * sheetScaleY, 0.0F);
		this.drawTexturedModalRect((int)((float)left / sheetScaleX), (int)((float)top / sheetScaleY), 3, 1, this.bookImageWidth, this.bookImageHeight);
		GL11.glScalef(1.0F / sheetScaleX, 1.0F / sheetScaleY, 0.0F);
		
		String sectionTitle;
		
		String s1 = "";
		
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		
		int leftMargin;
		int topMargin;
		int lineHeight;
		
		int secondPageLeft;
		
		leftMargin = 5;
		topMargin = 18;
		lineHeight = 14;
		
		secondPageLeft = (int)((float)left + (((float)this.bookImageWidth * sheetScaleX) / 2.0F));
		
		switch(this.curSection)
		{
			case Items:
				sectionTitle = "Items";
				this.fontRendererObj.drawString(sectionTitle, left + leftMargin, top + topMargin, 0);
				
				s1 = "Here's a simple list of some items.";
				this.fontRendererObj.drawSplitString(s1, left + leftMargin, top + topMargin + lineHeight, 180, 0);
				
				s1 = "This Journal.";
				this.fontRendererObj.drawSplitString(s1, left + leftMargin + 33, top + topMargin + (lineHeight * 2), 180, 0);
				s1 = "Study Desk.";
				this.fontRendererObj.drawSplitString(s1, left + leftMargin + 33, top + topMargin + (lineHeight * 3), 180, 0);
				
				switch(this.currPage)
				{
					case 1:
						s1 = "By putting your journal in a desk, with enough space to craft and store materials, you can now discover new ways to improve your items. Just use some planks, a chest, a crafting surface and your journal.";
						this.fontRendererObj.drawSplitString(s1, secondPageLeft + leftMargin, top + topMargin + lineHeight, 160, 0);
						
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						Minecraft.getMinecraft().getTextureManager().bindTexture(bookGuiTextures);
						this.drawTexturedModalRect(secondPageLeft + leftMargin, top + topMargin + (lineHeight * 7), 129, 165, 58, 58);
						
						this.drawItemStack(new ItemStack(WorldExplorerItems.journal), secondPageLeft + leftMargin + 21, top + topMargin + (lineHeight * 7) + 2, "");
						
						this.drawItemStack(new ItemStack(Blocks.planks), secondPageLeft + leftMargin + 2, top + topMargin + (lineHeight * 7) + 21, "");
						this.drawItemStack(new ItemStack(Blocks.crafting_table), secondPageLeft + leftMargin + 21, top + topMargin + (lineHeight * 7) + 21, "");
						this.drawItemStack(new ItemStack(Blocks.planks), secondPageLeft + leftMargin + 41, top + topMargin + (lineHeight * 7) + 21, "");
						
						this.drawItemStack(new ItemStack(Blocks.planks), secondPageLeft + leftMargin + 2, top + topMargin + (lineHeight * 7) + 41, "");
						this.drawItemStack(new ItemStack(Blocks.chest), secondPageLeft + leftMargin + 21, top + topMargin + (lineHeight * 7) + 41, "");
						this.drawItemStack(new ItemStack(Blocks.planks), secondPageLeft + leftMargin + 41, top + topMargin + (lineHeight * 7) + 41, "");
						break;
					default:
						s1 = "Your Journal is your personal record, where you'll be keeping track of your progres, discoveries and clues. In case you loose it, there's a way to get it back, by crafting it using the following materials.";
						this.fontRendererObj.drawSplitString(s1, secondPageLeft + leftMargin, top + topMargin + lineHeight, 160, 0);
						
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						Minecraft.getMinecraft().getTextureManager().bindTexture(bookGuiTextures);
						this.drawTexturedModalRect(secondPageLeft + leftMargin, top + topMargin + (lineHeight * 6), 129, 165, 58, 58);
						
						this.drawItemStack(new ItemStack(Items.writable_book), secondPageLeft + leftMargin + 2, top + topMargin + (lineHeight * 6) + 1, "");
						this.drawItemStack(new ItemStack(Items.compass), secondPageLeft + leftMargin + 1, top + topMargin + (lineHeight * 6) + 21, "");
						this.drawItemStack(new ItemStack(Items.clock), secondPageLeft + leftMargin + 20, top + topMargin + (lineHeight * 6) + 21, "");
						break;
				}
				
				this.drawItemStack(new ItemStack(WorldExplorerItems.journal), left + leftMargin + 10, top + topMargin + (lineHeight * 2) - 2, "");
				this.drawItemStack(new ItemStack(WorldExplorerBlocks.studydesk), left + leftMargin + 10, top + topMargin + (lineHeight * 3) - 2, "");
				
				break;
			default:
				sectionTitle = this.bookTitle;
				this.fontRendererObj.drawString(sectionTitle, left + leftMargin, top + topMargin, 0);
				
				s1 = "You are learning from the world around you. By mixing different materials, you found a way to create objects that you can use to improve the way you interact with the environment.";
				this.fontRendererObj.drawSplitString(s1, left + leftMargin, top + topMargin + lineHeight, 180, 0);
				
				s1 = "Use this book to keep track of your discoveries and find new ways to learn.";
				this.fontRendererObj.drawSplitString(s1, left + leftMargin, top + topMargin + (lineHeight * 6), 180, 0);
				
				s1 = "Here's the record of whay you've discovered so far. This book is still under development, for now it just shows a few things. But soon will be helpful.";
				this.fontRendererObj.drawSplitString(s1, secondPageLeft + leftMargin, top + topMargin + lineHeight, 170, 0);
				break;
		}
		
		leftMargin = 12;
		
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
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        
        float zl = this.zLevel;
        this.zLevel = 200.0F;
        
        itemRender.zLevel = 200.0F;
        
        FontRenderer font = null;
        if (stack != null) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRendererObj;
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), stack, x, y);
        itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager(), stack, x, y, text);
        
        this.zLevel = zl;
        
        itemRender.zLevel = 0.0F;
    }
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
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
			if (this.visible)
			{
				boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
				
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				
				minecraft.getTextureManager().bindTexture(GuiScreenJournal.bookGuiTextures);
				
				int k = 0;
				int l = 160 + (type * this.height);
				
				if (flag)
				{
					k += this.width;
				}
				
				GL11.glScalef(1.0F * this.scaleWidth, 1.0F * this.scaleHeight, 0.0F);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, this.width, this.height);
				GL11.glScalef(1.0F / this.scaleWidth, 1.0F / this.scaleHeight, 0.0F);
				this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, this.bText, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xffffff);
			}
		}
	}
}
