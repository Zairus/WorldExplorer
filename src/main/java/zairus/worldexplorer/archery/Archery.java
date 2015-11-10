package zairus.worldexplorer.archery;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zairus.worldexplorer.archery.client.renderer.ArcheryRenderManager;
import zairus.worldexplorer.archery.entity.ArcheryEntityManager;
import zairus.worldexplorer.archery.entity.monster.ArcheryMonsterManager;
import zairus.worldexplorer.archery.items.WEArcheryItems;
import zairus.worldexplorer.core.IWEAddonEntityManager;
import zairus.worldexplorer.core.IWEAddonMod;
import zairus.worldexplorer.core.IWEAddonMonsterManager;
import zairus.worldexplorer.core.IWEAddonRenderManager;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WorldExplorerItems;
import zairus.worldexplorer.equipment.items.WEEquipmentItems;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=WEConstants.ARCHERY_MODID, name=WEConstants.ARCHERY_NAME, version=WEConstants.ARCHERY_VERSION, dependencies="required-before:" + WEConstants.CORE_MODID)
public class Archery
	implements IWEAddonMod
{
	@Mod.Instance(WEConstants.ARCHERY_MODID)
	public static Archery instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		WorldExplorer.registerWEAddonMod(this);
		
		WEArcheryItems.init();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		WEArcheryItems.register();
		
		addRecipes();
	}
	
	private void addRecipes()
	{
		GameRegistry.addShapedRecipe(
				new ItemStack(WEArcheryItems.slingshot)
				,new Object[] {
					 "tst"
					," t "
					,'t'
					,Items.stick
					,'s'
					,Items.string});
		
		GameRegistry.addShapelessRecipe(
				new ItemStack(WEArcheryItems.pebble, 4)
				, new Object[] {
					Blocks.cobblestone
				});
		
		GameRegistry.addShapelessRecipe(
				new ItemStack(Blocks.cobblestone)
				, new Object[] {
					WEArcheryItems.pebble
					,WEArcheryItems.pebble
					,WEArcheryItems.pebble
					,WEArcheryItems.pebble
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEArcheryItems.boomerang)
				, new Object[] {
					 "ppl"
					,"slp"
					," sp"
					,'p'
					,Blocks.planks
					,'l'
					,Items.leather
					,'s'
					,Items.string
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEArcheryItems.specialarrow, 1, 1)
				, new Object[] {
					 "  p"
					," s "
					,"f  "
					,'p'
					,WEArcheryItems.pebble
					,'s'
					,new ItemStack(WEArcheryItems.specialarrow, 1, 0)
					,'f'
					,Items.feather
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEArcheryItems.blowpipe)
				, new Object[] {
					 " h "
					,"scs"
					,"scs"
					,'h'
					,new ItemStack(WEArcheryItems.specialarrow, 1, 0)
					,'s'
					,Items.string
					,'c'
					,Items.reeds
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEArcheryItems.dart, 2, 0)
				, new Object[] {
					 " n "
					," h "
					,"fsf"
					,'n'
					,WorldExplorerItems.needle
					,'h'
					,new ItemStack(WEArcheryItems.specialarrow, 1, 0)
					,'f'
					,Items.feather
					,'s'
					,Items.string
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEArcheryItems.longbow_handle)
				, new Object[] {
					 "stt"
					,"tl "
					,"stt"
					,'s'
					,Items.string
					,'t'
					,Items.stick
					,'l'
					,Items.leather
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEArcheryItems.longbow_string)
				, new Object[] {
					 " ss"
					," ss"
					," ss"
					,'s'
					,Items.string
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEArcheryItems.longbow)
				, new Object[] {
					"hs"
					,'h'
					,WEArcheryItems.longbow_handle
					,'s'
					,WEArcheryItems.longbow_string
				});
		
		GameRegistry.addShapedRecipe(
				new ItemStack(WEArcheryItems.crossbow)
				, new Object[] {
					 " s "
					,"sbs"
					,"lwh"
					,'s'
					,Items.stick
					,'b'
					,WEArcheryItems.longbow_string
					,'l'
					,Blocks.lever
					,'w'
					,WEEquipmentItems.whiphandle
					,'h'
					,Blocks.tripwire_hook
				});
		
		GameRegistry.addShapelessRecipe(
				new ItemStack(WEArcheryItems.specialarrow, 2, 0)
				, new Object[] {
					Items.stick
					,Items.stick
				});
	}
	
	public IWEAddonEntityManager getEntityManager()
	{
		return new ArcheryEntityManager();
	}
	
	public IWEAddonMonsterManager getMonsterManager()
	{
		return new ArcheryMonsterManager();
	}
	
	public IWEAddonRenderManager getRenderManager()
	{
		return new ArcheryRenderManager();
	}
	
	public static void onBowFOV(ItemStack stack, EntityPlayer player, int count, Item itemInUse)
	{
		float f = 1.0F;
		
		if (player.capabilities.isFlying)
		{
			f *= 1.1F;
		}
		
		float speedOnGround = ObfuscationReflectionHelper.getPrivateValue(EntityPlayer.class, player, "speedOnGround", "field_71108_cd");
		
		f *= (player.capabilities.getWalkSpeed() * 1 / speedOnGround + 1.0F) / 2.0F;
		
		//if (player.isUsingItem() && player.getItemInUse().getItem() == itemInUse)
		//{
			int i = player.getItemInUseDuration();
			float f1 = (float)i / 20.0F;
			
			if (f1 > 1.0F)
			{
				f1 = 1.0F;
			}
			else
			{
				f1 *= f1;
			}
			
			//f *= 1.0F - f1 * 0.15F;
		//}
		
		float fovModifierHand = ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, Minecraft.getMinecraft().entityRenderer, "fovModifierHand", "field_78507_R");
		
		fovModifierHand += (f - fovModifierHand) * 0.5F;
		
		if (fovModifierHand > 1.5F)
		{
			fovModifierHand = 1.5F;
		}
		
		if (fovModifierHand < 0.1F)
		{
			fovModifierHand = 0.1F;
		}
		
		ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, Minecraft.getMinecraft().entityRenderer, fovModifierHand, "fovModifierHand", "field_78507_R");
	}
}
