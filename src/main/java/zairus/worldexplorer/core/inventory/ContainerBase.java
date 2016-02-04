package zairus.worldexplorer.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase
	extends Container
{
	public int bindPlayerInventory(InventoryPlayer playerInv)
	{
		int iIndex = 0;
		
		//hotbar :from 0 to 8
		int gridX = 7;
		int gridY = 149;
		int gridCols = 9;
		int gridRows = 1;
		iIndex = placeSlotGrid(playerInv, iIndex, gridX, gridY, gridCols, gridRows);
		
		//player's :from 9 to 35
		gridX = 7;
		gridY = 91;
		gridCols = 9;
		gridRows = 3;
		iIndex = placeSlotGrid(playerInv, iIndex, gridX, gridY, gridCols, gridRows);
		
		return iIndex;
	}
	
	public int placeSlotGrid(IInventory inv, int iIndex, int gridX, int gridY, int gridCols, int gridRows)
	{
		gridDone:
			for (int i = 0; i < gridRows; ++i)
			{
				for (int j = 0; j < gridCols; ++j)
				{
					if (iIndex > inv.getSizeInventory())
					{
						break gridDone;
					}
					
					this.addSlotToContainer(new Slot(inv, iIndex, gridX + (j * 18), gridY + (i * 18)));
					++iIndex;
				}
			}
		
		return iIndex;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ItemStack slotClick(int slot, int slotX, int slotY, EntityPlayer player)
	{
		ItemStack stack = super.slotClick(slot, slotX, slotY, player);
		
		return stack;
	}
	
	@Override
	protected void retrySlotClick(int slotNumber, int p_75133_2_, boolean p_75133_3_, EntityPlayer player)
    {
		if (slotNumber < 3*9+9)
			this.slotClick(slotNumber, p_75133_2_, 1, player);
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
		;
	}
	
	@Override
	public void putStackInSlot(int slot, ItemStack stack)
	{
		this.getSlot(slot).putStack(stack);
	}
}
