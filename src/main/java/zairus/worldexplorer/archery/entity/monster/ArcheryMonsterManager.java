package zairus.worldexplorer.archery.entity.monster;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import zairus.worldexplorer.archery.Archery;
import zairus.worldexplorer.core.IWEAddonMonsterManager;
import zairus.worldexplorer.core.entity.WEEntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ArcheryMonsterManager
	implements IWEAddonMonsterManager
{
	public ArcheryMonsterManager()
	{
		//
	}
	
	@Override
	public void registerMobs()
	{
		WEEntityRegistry.registerEntity(EntitySkeletonExplorer.class, "entity_skeleton_explorer", Archery.instance, 64, 1, true, 0xffffff, 0x00ff00);
		
		for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++)
		{
			if (
					BiomeGenBase.getBiome(i) != null
					&& BiomeGenBase.getBiome(i) != BiomeGenBase.mushroomIsland
					&& BiomeGenBase.getBiome(i) != BiomeGenBase.mushroomIslandShore
					&& BiomeGenBase.getBiome(i) != BiomeGenBase.sky)
			{
				EntityRegistry.addSpawn(EntitySkeletonExplorer.class, 15, 1, 4, EnumCreatureType.monster, BiomeGenBase.getBiome(i));
			}
		}
	}
}
