package zairus.worldexplorer.core.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.block.WorldExplorerBlocks;
import zairus.worldexplorer.core.helpers.RenderHelper;
import zairus.worldexplorer.core.items.WorldExplorerItems;
import zairus.worldexplorer.core.journal.IJournalSection;
import zairus.worldexplorer.core.journal.JournalSectionMain;
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
	private int currPage = 0;
	//private journalSections curSection = journalSections.Main;
	private NBTTagList bookPages;
	private String bookTitle = Minecraft.getMinecraft().thePlayer.getDisplayName() + "'s Journal";
	
	private GuiScreenJournal.JournalPageButton buttonNextPage;
	private GuiScreenJournal.JournalPageButton buttonPreviousPage;
	private GuiScreenJournal.JournalPageButton buttonCancel;
	private GuiScreenJournal.JournalPageButton buttonHome;
	
	private float sheetScaleX = 1.4f;
	private float sheetScaleY = 1.4f;
	
	private List<IJournalSection> sections = new ArrayList<IJournalSection>();
	
	/*private enum journalSections {
		Main
		,Items
	}*/
	
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
		WorldExplorer.packetPipeline.sendToServer(new JournalPacket());
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
		
		List<ItemStack> tabIcons = new ArrayList<ItemStack>();
		
		tabIcons.add(new ItemStack(WorldExplorerItems.journal));
		tabIcons.add(new ItemStack(WorldExplorerBlocks.studydesk));
		tabIcons.add(new ItemStack(Blocks.red_flower));
		tabIcons.add(new ItemStack(Items.map));
		
		for (int i = 0; i < tabIcons.size(); ++i)
			buttonList.add(new GuiScreenJournal.JournalPageButton(i + 4, 0, 0, 18, 26, sheetScaleX, sheetScaleY, 7, tabIcons.get(i)));
		
		updateButtons();
		
		sections.add(new JournalSectionMain().setTitle(bookTitle));
	}
	
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
	
	private void updateButtons()
	{
		int left = (int)(((float)width - scaleWidthF()) / 2.0f);
		int top = 13;
		
		for (int i = 0; i < 4; ++i)
		{
			((JournalPageButton)buttonList.get(i + 4)).soundType = 2;
			((JournalPageButton)buttonList.get(i + 4)).width = 18;
			((JournalPageButton)buttonList.get(i + 4)).height = 26;
			((JournalPageButton)buttonList.get(i + 4)).xPosition = left - 10;
			((JournalPageButton)buttonList.get(i + 4)).yPosition = top + (24 * i);
		}
		
		buttonCancel.xPosition = left + ((int)scaleWidthF() - buttonCancel.width) - 10;
		buttonCancel.yPosition = top + ((int)scaleHeightF() - buttonCancel.height) - 7;
		buttonCancel.visible = true;
		
		buttonNextPage.soundType = 1;
		buttonNextPage.visible= true;
		buttonNextPage.xPosition = left + (int)scaleWidthF() - buttonCancel.width - buttonNextPage.width - 10;
		buttonNextPage.yPosition = buttonCancel.yPosition;
		
		buttonPreviousPage.soundType = 1;
		buttonPreviousPage.visible = true;
		buttonPreviousPage.xPosition = buttonNextPage.xPosition - buttonPreviousPage.width;
		buttonPreviousPage.yPosition = buttonNextPage.yPosition;
		
		buttonHome.visible = false;
	}
	
	protected void actionPerformed(GuiButton button)
	{
		if (button.enabled)
		{
			switch(button.id)
			{
    			case 1:
    				currPage -= 1;
    				
    				if (currPage < 0)
    					currPage = 0;
    				
    				break;
    			case 2:
    				currPage += 1;
    				if (currPage > bookTotalPages)
    					currPage = bookTotalPages;
    				break;
    			case 3:
    				//this used to be home
    				currPage = 0;
    				break;
    			case 4:
    			case 5:
    			case 6:
    			case 7:
    				break;
    			default:
    				this.mc.displayGuiScreen(new GuiScreenEquipment(this.editingPlayer, this.editingPlayer.worldObj));
    				break;
    		}
			
			updateButtons();
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
		
		GL11.glScalef(1.0F * sheetScaleX, 1.0F * sheetScaleY, 0.0F);
		this.drawTexturedModalRect((int)((float)left / sheetScaleX), (int)((float)top / sheetScaleY), 3, 1, this.bookImageWidth, this.bookImageHeight);
		GL11.glScalef(1.0F / sheetScaleX, 1.0F / sheetScaleY, 0.0F);
		
		String s1 = "";
		
		super.drawScreen(screenX, screenY, f1);
		
		int leftMargin;
		int topMargin;
		int lineHeight;
		
		leftMargin = 30;
		topMargin = 30;
		lineHeight = 14;
		
		IJournalSection sec = sections.get(0);
		
		s1 = sec.getTitle();
		fontRendererObj.drawString(s1, left + leftMargin, top + topMargin, 0);
		fontRendererObj.drawString(s1, left + leftMargin + 1, top + topMargin, 0);
		
		int lineX = 0;
		int lineY = 0;
		
		List<List<String>> pages = new ArrayList<List<String>>();
		int curPage = 0;
		
		s1 = "";
		for (int i = 0; i < sec.getContent().length(); ++i)
		{
			s1 += sec.getContent().substring(i, i + 1);
			++lineX;
			
			if (lineX > 23 || i == sec.getContent().length() - 1)
			{
				if (pages.size() == 0)
					pages.add(new ArrayList<String>());
				
				pages.get(curPage).add(s1);
				lineX = 0;
				s1 = "";
				++lineY;
			}
			
			if (lineY > 9)
			{
				++curPage;
				pages.add(new ArrayList<String>());
				lineY = 0;
				lineX = 0;
			}
		}
		
		int textX = left + leftMargin ;
		int textY = top + topMargin + lineHeight + 5;
		
		bookTotalPages = MathHelper.ceiling_float_int((float)pages.size() / 2.0f) - 1;
		
		int pageIndex = currPage * 2;
		
		if (pageIndex < pages.size())
		{
			for (int j = 0; j < pages.get(pageIndex).size(); ++j)
			{
				fontRendererObj.drawString(pages.get(pageIndex).get(j), textX, textY + (lineHeight * j), 0);
			}
		}
		
		++pageIndex;
		
		if (pageIndex < pages.size())
		{
			for (int j = 0; j < pages.get(pageIndex).size(); ++j)
			{
				fontRendererObj.drawString(pages.get(pageIndex).get(j), textX + 155, textY + (lineHeight * j), 0);
			}
		}
		
		leftMargin = 40;
		
		int bottomRowTop = (int)((float)top + ((float)this.bookImageHeight * sheetScaleY) - 22);
		
		this.drawItemStack(new ItemStack(Items.compass), left + leftMargin, bottomRowTop, "");
		this.drawItemStack(new ItemStack(Items.clock), left + leftMargin + 20, bottomRowTop, "");
	}
	
	public int getCurrentPage()
	{
		return currPage;
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
		public int soundType = 0;
		public ItemStack iconStack = null;
		
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
		
		public JournalPageButton(int buttonId, int posX, int posY, int w, int h, float scaleW, float scaleH, int buttonType, ItemStack icon)
		{
			this(buttonId, posX, posY, w, h, scaleW, scaleH, buttonType, "");
			iconStack = icon;
		}
		
		public void drawButton(Minecraft minecraft, int mouseX, int mouseY)
		{
			if (visible)
			{
				boolean flag = 
						mouseX >= (int)((float)xPosition * scaleWidth) 
						&& mouseY >= (int)((float)yPosition * scaleHeight) 
						&& mouseX < (int)((float)(xPosition + width) * scaleWidth)
						&& mouseY < (int)((float)(yPosition + height) * scaleHeight);
						
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				minecraft.getTextureManager().bindTexture(GuiScreenJournal.bookGuiTextures);
				
				int k = 0;
				int l = 0;
				
				if (type < 7)
				{
					k = 0;
					l = 160 + (type * height);
				}
				else
				{
					k = 86;
					l = 160;
				}
				
				if (flag)
					k += this.width;
				
				GL11.glScalef(1.0F * scaleWidth, 1.0F * scaleHeight, 0.0F);
				drawTexturedModalRect(xPosition, yPosition, k, l, width, height);
				GL11.glScalef(1.0F / scaleWidth, 1.0F / scaleHeight, 0.0F);
				
				if (iconStack != null)
					RenderHelper.drawItemStack(iconStack, (int)((xPosition * scaleWidth) + 6), (int)((yPosition * scaleHeight) + 10), "");
				
				this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, this.bText, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xffffff);
			}
		}
		
		@Override
		public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY)
		{
			boolean pressed = 
					enabled 
					&& visible 
					&& mouseX >= (int)((float)xPosition * scaleWidth) 
					&& mouseY >= (int)((float)yPosition * scaleHeight) 
					&& mouseX < (int)((float)(xPosition + width) * scaleWidth) 
					&& mouseY < (int)((float)(yPosition + height) * scaleHeight);
			
			return pressed;
		}
		
		@Override
		public void func_146113_a(SoundHandler soundHandler)
		{
			switch (soundType)
			{
			case 1:
				soundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation(WEConstants.CORE_PREFIX, "book_page"), 1.0F));
				break;
			case 2:
				soundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation(WEConstants.CORE_PREFIX, "book_tab"), 1.0F));
				break;
			default:
				soundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation(WEConstants.CORE_PREFIX, "book_close"), 1.0F));
				break;
			}
		}
	}
}
