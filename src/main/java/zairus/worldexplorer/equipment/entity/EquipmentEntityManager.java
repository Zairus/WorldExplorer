package zairus.worldexplorer.equipment.entity;

import zairus.worldexplorer.core.IWEAddonEntityManager;
import zairus.worldexplorer.core.entity.WEEntityRegistry;
import zairus.worldexplorer.equipment.Equipment;

public class EquipmentEntityManager
	implements IWEAddonEntityManager
{
	public EquipmentEntityManager()
	{
		//
	}
	
	@Override
	public void registerEntities()
	{
		WEEntityRegistry.registerEntity(EntityWhipTip.class, "enitity_whiptip", Equipment.instance, 1024, 1, true);
	}
}
