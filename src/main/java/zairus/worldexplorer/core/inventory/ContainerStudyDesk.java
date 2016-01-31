package zairus.worldexplorer.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import zairus.worldexplorer.core.items.WEItem;
import zairus.worldexplorer.core.items.WEItem.Improvement;
import zairus.worldexplorer.core.tileentity.TileEntityDesk;

public class ContainerStudyDesk
	extends ContainerBase
{
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	public InventoryCraftResult craftAdditional = new InventoryCraftResult();
	
	private final TileEntityDesk inventory;
	private World worldObj;
	
	public ContainerStudyDesk(InventoryPlayer playerinv, TileEntityDesk inv, World world)
	{
		this.inventory = inv;
		this.worldObj = world;
		
		inv.openInventory();
		
		int iIndex = 0;
		
		//hotbar :from 0 to 8
		int gridX = 82;
		int gridY = 214;
		int gridCols = 9;
		int gridRows = 1;
		iIndex = placeSlotGrid(playerinv, iIndex, gridX, gridY, gridCols, gridRows);
		
		//player's :from 9 to 35
		gridX = 82;
		gridY = 156;
		gridCols = 9;
		gridRows = 3;
		iIndex = placeSlotGrid(playerinv, iIndex, gridX, gridY, gridCols, gridRows);
		
		//Chest :from 0 to 26
		iIndex = 0;
		gridX = 15;
		gridY = 30;
		gridCols = 3;
		gridRows = 9;
		iIndex = placeSlotGrid(inv, iIndex, gridX, gridY, gridCols, gridRows);
		
		//Improvement material :27
		gridX = 85;
		gridY = 49;
		this.addSlotToContainer(new SlotImprovement(inv, iIndex, gridX, gridY));
		++iIndex;
		
		//Crafting grid 1 :from 28 to 36
		gridX = 136;
		gridY = 30;
		gridCols = 3;
		gridRows = 3;
		iIndex = placeSlotGrid(inv, iIndex, gridX*2048, gridY*2048, gridCols, gridRows);
		
		//Additional result
		gridX = 155;
		gridY = 121;
		this.addSlotToContainer(new SlotCraftingAdditional(playerinv.player, inv, 27, this.craftMatrix, this.craftAdditional, 0, gridX, gridY));
		
		//Crafting grid 2
		iIndex = 0;
		gridX = 136;
		gridY = 30;
		gridCols = 3;
		gridRows = 3;
		iIndex = placeSlotGrid(this.craftMatrix, iIndex, gridX, gridY, gridCols, gridRows);
		
		//Crafting result
		gridX = 221;
		gridY = 49;
		this.addSlotToContainer(new SlotCrafting(playerinv.player, this.craftMatrix, this.craftResult, 0, gridX, gridY));
		
		this.onCraftMatrixChanged(this.craftMatrix);
		
		for (int i = 0; i < 9; ++i)
		{
			this.craftMatrix.setInventorySlotContents(i, this.inventory.getStackInSlot(i + 28));
		}
	}
	
	public void onCraftMatrixChanged(IInventory inventory)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
		
		this.craftAdditional.setInventorySlotContents(63, findImprovementResult());
	}
	
	private ItemStack findImprovementResult()
	{
		ItemStack improved = null;
		ItemStack subject = null;
		ItemStack material = this.inventory.getStackInSlot(27);
		Item subjectItem = null;
		
		for (int i = 0; i < 9; ++i)
		{
			if (subject == null)
				subject = this.craftMatrix.getStackInSlot(i);
			else if (this.craftMatrix.getStackInSlot(i) != null)
			{
				subject = null;
				i = 9;
			}
		}
		
		if (material != null && subject != null)
		{
			subjectItem = subject.getItem();
			
			if (subjectItem instanceof WEItem)
			{
				if (((WEItem)subjectItem).hasImprovements())
				{
					improved = subject.copy();
					
					Improvement imp = ((WEItem)subjectItem).getImprovementFromMaterial(material.getItem());
					
					if (imp != null)
						addImprovement(improved, imp.improvementType.getKey(), imp.valuePerUnit, imp.improvementType.getMaxValue());
				}
			}
		}
		return improved;
	}
	
	private ItemStack addImprovement(ItemStack improved, String improvement, float valueAdded, float valueMax)
	{
		if (improved.getTagCompound() == null)
			improved.setTagCompound(new NBTTagCompound());
		
		if (!improved.getTagCompound().hasKey(improvement))
			improved.getTagCompound().setFloat(improvement, 0.0F);
		
		float curValue = improved.getTagCompound().getFloat(improvement);
		curValue += valueAdded;
		
		if (curValue > valueMax)
			curValue = valueMax;
		
		improved.getTagCompound().setFloat(improvement, curValue);
		
		return improved;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.inventory.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack slotClick(int slot, int slotX, int slotY, EntityPlayer player)
	{
		ItemStack stack = super.slotClick(slot, slotX, slotY, player);
		
		this.onCraftMatrixChanged(this.craftMatrix);
		
		return stack;
	}
	
	@Override
	protected void retrySlotClick(int slotNumber, int p_75133_2_, boolean p_75133_3_, EntityPlayer player)
    {
		if (slotNumber < 3*9+9+9*3)
			this.slotClick(slotNumber, p_75133_2_, 1, player);
    }
	
	@Override
	public void addCraftingToCrafters(ICrafting crafting)
	{
		super.addCraftingToCrafters(crafting);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void detectAndSendChanges()
	{
		for (int i = 0; i < this.inventorySlots.size(); ++i)
		{
			ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
			ItemStack itemstack1 = (ItemStack)this.inventoryItemStacks.get(i);
			
			if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
			{
				itemstack1 = itemstack == null ? null : itemstack.copy();
				this.inventoryItemStacks.set(i, itemstack1);
				
				for (int j = 0; j < this.crafters.size(); ++j)
				{
					((ICrafting)this.crafters.get(j)).sendSlotContents(this, i, itemstack1);
				}
			}
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotNumber);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			// Player inventory
			if (slotNumber < 36)
			{
				if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory() - 10, 27*2 + 9, true))
					return null;
			}
			
			// Chest grid
			if (slotNumber > 35 && slotNumber < 63)
			{
				if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory() - 10, false))
					return null;
			}
			
			// Improvement material slot
			if (slotNumber == 63)
			{
				if (!this.mergeItemStack(itemstack1, 0, 63,false))
					return null;
			}
			
			// Additional result slot
			/*if (slotNumber == 73)
			{
				if (!this.mergeItemStack(itemstack1, 74, 82,false))
					return null;
			}*/
			
			// Crafting grid
			if (slotNumber > 73 && slotNumber < 83)
			{
				if (!this.mergeItemStack(itemstack1, 0, 63,false))
					return null;
			}
			
			// Crafting result
			if (slotNumber == 83)
			{
				while(slot.getStack() != null && slot.getStack().stackSize > 0)
				{
					itemstack1 = slot.getStack();
					itemstack = itemstack1.copy();
					
					if (!this.mergeItemStack(itemstack1, 0, 63, false))
						return null;
					
					slot.onSlotChange(itemstack1, itemstack);
					
					if (itemstack1.stackSize == itemstack.stackSize)
						return null;
					
					slot.onPickupFromSlot(player, itemstack1);
					
					this.onCraftMatrixChanged(this.craftMatrix);
				}
			}
			
			if (itemstack1.stackSize == 0)
				slot.putStack((ItemStack)null);
			else
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		for (int i = 0; i < 9; ++i)
		{
			this.inventory.setInventorySlotContents(i + 28, this.craftMatrix.getStackInSlot(i));
		}
		this.inventory.closeInventory();
	}
	
	@Override
	public void putStackInSlot(int slot, ItemStack stack)
	{
		this.getSlot(slot).putStack(stack);
	}
	
	private class SlotImprovement
		extends Slot
	{
		public SlotImprovement(IInventory inventory, int slotNumber, int x, int y)
		{
			super(inventory, slotNumber, x, y);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			if (!(
					stack.getItem() == Items.redstone
					|| stack.getItem() == Item.getItemFromBlock(Blocks.redstone_block)
					|| stack.getItem() == Items.glowstone_dust
					|| stack.getItem() == Item.getItemFromBlock(Blocks.glowstone)
					|| stack.getItem() == Items.gunpowder
					|| stack.getItem() == Items.slime_ball
					|| stack.getItem() == Items.ender_eye
					|| stack.getItem() == Items.ender_pearl
					|| stack.getItem() == Items.blaze_powder))
			{
				return false;
			}
			
			return true;
		}
	}
	
	private class SlotCraftingAdditional
		extends SlotCrafting
	{
		private IInventory improvementInv;
		private int improvementInvSlot;
		
		public SlotCraftingAdditional(EntityPlayer player, IInventory inventoryImprovement, int slotNumberImprovement, IInventory inventoryMatrix, IInventory inventoryResult, int index, int x, int y)
		{
			super(player, inventoryMatrix, inventoryResult, index, x, y);
			
			this.improvementInv = inventoryImprovement;
			this.improvementInvSlot = slotNumberImprovement;
		}
		
		@Override
		public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
		{
			super.onPickupFromSlot(player, stack);
			this.improvementInv.decrStackSize(this.improvementInvSlot, 1);
		}
	}
}
