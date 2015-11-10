package zairus.worldexplorer.archery.entity;

import zairus.worldexplorer.archery.Archery;
import zairus.worldexplorer.core.IWEAddonEntityManager;
import zairus.worldexplorer.core.entity.WEEntityRegistry;

public final class ArcheryEntityManager
	implements IWEAddonEntityManager
{
	public ArcheryEntityManager()
	{
		//
	}
	
	@Override
	public void registerEntities()
	{
		WEEntityRegistry.registerEntity(EntityPebble.class, "entity_pebble", Archery.instance, 1024, 1, true);
		WEEntityRegistry.registerEntity(EntityBoomerang.class, "entity_boomerang", Archery.instance, 1024, 1, true);
		WEEntityRegistry.registerEntity(EntityDart.class, "entity_dart", Archery.instance, 1024, 1, true);
	}
}
