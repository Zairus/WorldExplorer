package zairus.worldexplorer.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryBase
	implements IInventory
{
	protected final int inventorySize = 1;
	
	protected ItemStack[] contents = new ItemStack[inventorySize];
	protected String inventoryName;
	protected boolean inventoryChanged = false;
	
	@Override
	public int getSizeInventory()
	{
		return this.contents.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return (slot < this.contents.length)? this.contents[slot] : null;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int count)
	{
		if (this.contents[slot] != null)
		{
			ItemStack itemstack;
			
			if (this.contents[slot].stackSize <= count)
			{
				itemstack = this.contents[slot];
				this.contents[slot] = null;
				this.markDirty();
				return itemstack;
			}
			else
			{
				itemstack = this.contents[slot].splitStack(count);
				
				if (this.contents[slot].stackSize == 0)
				{
					this.contents[slot] = null;
				}
				
				this.markDirty();
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (this.contents[slot] != null)
		{
			ItemStack itemstack = this.contents[slot];
			this.contents[slot] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.contents[slot] = stack;
		
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
		
        this.markDirty();
	}
	
	@Override
	public String getInventoryName()
	{
		return this.hasCustomInventoryName()? this.inventoryName : "inventory.name";
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		return this.inventoryName != null && this.inventoryName.length() > 0;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public void markDirty()
	{
		this.inventoryChanged = true;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public void openInventory()
	{
		;
	}
	
	@Override
	public void closeInventory()
	{
		;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack syack)
	{
		return true;
	}
	
	public NBTTagList writeToNBT(NBTTagList tagList)
	{
		int i;
		NBTTagCompound nbttagcompound;
		
		for (i = 0; i < this.contents.length; ++i)
		{
			if (this.contents[i] != null)
			{
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte)i);
				this.contents[i].writeToNBT(nbttagcompound);
				tagList.appendTag(nbttagcompound);
			}
		}
		
		return tagList;
	}
	
	public void readFromNBT(NBTTagList tagList)
	{
		this.contents = new ItemStack[inventorySize];
		
		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
			
			if (itemstack != null)
			{
				if (j >= 0 && j < this.contents.length)
				{
					this.contents[j] = itemstack;
				}
			}
		}
	}
}
