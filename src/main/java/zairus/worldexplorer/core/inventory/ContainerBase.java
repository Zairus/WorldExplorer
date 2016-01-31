package zairus.worldexplorer.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerBase
	extends Container
{ 
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
}
