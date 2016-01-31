package zairus.worldexplorer.core;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;

public class WEKeyBindings
{
	public static KeyBinding explorergui;
	
	public static void init()
	{
		explorergui = new KeyBinding("key.explorergui",  WEConfig.KEY_EXPLORERGUI, "key.categories." + WEConstants.CORE_PREFIX);
		
		ClientRegistry.registerKeyBinding(explorergui);
	}
}
