package zairus.worldexplorer.core.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDesk
	extends TileEntity
	implements IInventory
{
	private ItemStack[] contents = new ItemStack[totalInventory()];
	
	private String customName;
	private int numPlayersUsing;
	
	@Override
	public int getSizeInventory()
	{
		return totalInventory();
	}
	
	private final int totalInventory()
	{
		return 
				(9 * 3)
				+(3 * 3)
				+1
				;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.contents[slot];
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
		return this.hasCustomInventoryName() ? this.customName : "container.studydesk";
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		return this.customName != null && this.customName.length() > 0;
	}
	
	public void setCustomInventoryName(String name)
	{
		this.customName = name;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
	
	@Override
	public void openInventory() {
		if (this.numPlayersUsing < 0)
		{
			this.numPlayersUsing = 0;
        }
		
		++this.numPlayersUsing;
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
	}
	
	@Override
	public void closeInventory() {
		--this.numPlayersUsing;
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		NBTTagList nbttaglist = new NBTTagList();
		
		for (int i = 0; i < this.contents.length; ++i)
		{
			if (this.contents[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.contents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		
        tag.setTag("Items", nbttaglist);
        
        tag.setInteger("renderFacing", this.blockMetadata);
        
        if (this.hasCustomInventoryName())
        {
        	tag.setString("CustomName", this.customName);
        }
	}
	
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		NBTTagList nbttaglist = tag.getTagList("Items", 10);
		
		this.contents = new ItemStack[this.getSizeInventory()];
		
		this.blockMetadata = tag.getInteger("renderFacing");
		
		if (tag.hasKey("CustomName", 8))
		{
			this.customName = tag.getString("CustomName");
		}
		
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			
			if (j >= 0 && j < this.contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}
}
