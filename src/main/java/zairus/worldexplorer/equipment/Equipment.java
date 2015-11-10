package zairus.worldexplorer.equipment;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import zairus.worldexplorer.core.IWEAddonEntityManager;
import zairus.worldexplorer.core.IWEAddonMod;
import zairus.worldexplorer.core.IWEAddonMonsterManager;
import zairus.worldexplorer.core.IWEAddonRenderManager;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.equipment.client.renderer.EquipmentRenderManager;
import zairus.worldexplorer.equipment.entity.EquipmentEntityManager;
import zairus.worldexplorer.equipment.entity.monster.EquipmentMonsterManager;
import zairus.worldexplorer.equipment.items.WEEquipmentItems;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=WEConstants.EQUIPMENT_MODID, name=WEConstants.EQUIPMENT_NAME, version=WEConstants.EQUIPMENT_VERSION, dependencies="required-before:" + WEConstants.CORE_MODID)
public class Equipment
	implements IWEAddonMod
{
	@Mod.Instance(WEConstants.EQUIPMENT_MODID)
	public static Equipment instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		WorldExplorer.registerWEAddonMod(this);
		
		WEEquipmentItems.init();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		WEEquipmentItems.register();
		
		addRecipes();
	}
	
	private void addRecipes()
	{
		GameRegistry.addShapedRecipe(
				new ItemStack(WEEquipmentItems.whiphandle)
				, new Object[] {
					 "lll"
					,"lpl"
					,"lpl"
					,'l'
					,Items.leather
					,'p'
					,Blocks.planks
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEEquipmentItems.leatherstrap)
				, new Object[] {
					"lll"
					,'l'
					,Items.leather
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEEquipmentItems.whiptip)
				, new Object[] {
					 "l l"
					," l "
					,'l'
					,Items.leather
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEEquipmentItems.whip)
				, new Object[] {
					 "  t"
					," s "
					,"h  "
					,'t'
					,WEEquipmentItems.whiptip
					,'s'
					,WEEquipmentItems.leatherstrap
					,'h'
					,WEEquipmentItems.whiphandle
				});
	}
	
	@Override
	public IWEAddonEntityManager getEntityManager()
	{
		return new EquipmentEntityManager();
	}
	
	@Override
	public IWEAddonMonsterManager getMonsterManager()
	{
		return new EquipmentMonsterManager();
	}
	
	@Override
	public IWEAddonRenderManager getRenderManager()
	{
		return new EquipmentRenderManager();
	}
}
