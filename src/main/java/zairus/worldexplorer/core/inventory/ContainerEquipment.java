package zairus.worldexplorer.core.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zairus.worldexplorer.core.player.CorePlayerManager;

public class ContainerEquipment
	extends ContainerBase
{
	public final InventoryPlayerEquipment playerEquipment;
	
	private final EntityPlayer player;
	
	@SideOnly(Side.CLIENT)
	public ContainerEquipment(World world, double x, double y, double z)
	{
		this.player = Minecraft.getMinecraft().thePlayer;
		this.playerEquipment = CorePlayerManager.getPlayerEquipmentInventory(this.player);
		
		initContainer();
	}
	
	public ContainerEquipment(InventoryPlayer playerInv, World world)
	{
		this.player = playerInv.player;
		this.playerEquipment = CorePlayerManager.getPlayerEquipmentInventory(this.player);
		
		initContainer();
	}
	
	private void initContainer()
	{
		int iIndex = 0;
		
		iIndex = bindPlayerInventory(this.player.inventory);
		
		playerEquipment.openInventory();
		
		iIndex = 0;
		
		iIndex = placeSlotGrid(playerEquipment, iIndex, 43, 10, 1, 2);
		
		iIndex = placeSlotGrid(playerEquipment, iIndex, 115, 10, 1, 2);
		
		iIndex = placeSlotGrid(playerEquipment, iIndex, 25, 46, 2, 1);
		
		iIndex = placeSlotGrid(playerEquipment, iIndex, 115, 46, 2, 1);
		
		iIndex = placeSlotGrid(playerEquipment, iIndex, 43, 64, 1, 1);
		
		iIndex = placeSlotGrid(playerEquipment, iIndex, 115, 64, 1, 1);
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
		CorePlayerManager.savePlayerEquipmentInventory(this.playerEquipment, this.player);
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
		
		CorePlayerManager.savePlayerEquipmentInventory(this.playerEquipment, this.player);
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
		playerEquipment.closeInventory();
	}
	
	@Override
	public void putStackInSlot(int slot, ItemStack stack)
	{
		this.getSlot(slot).putStack(stack);
	}
}
