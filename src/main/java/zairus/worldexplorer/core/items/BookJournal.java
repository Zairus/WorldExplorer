package zairus.worldexplorer.core.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zairus.worldexplorer.core.WEConstants;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.player.CorePlayerManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BookJournal
	extends WEItem
{
	public BookJournal ()
	{
		super();
		
		setUnlocalizedName("journal");
		setTextureName(WEConstants.CORE_PREFIX + ":journal");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		maxStackSize = 1;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		CorePlayerManager.unlockItem(player, this);
		
		world.playSoundAtEntity(
				player
				, WEConstants.CORE_PREFIX + ":book_tab"
				, 1.0F
				, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
		
		WorldExplorer.proxy.displayBookJournalGUI(player, stack);
		
		return stack;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_)
	{
		if (stack.hasTagCompound())
		{
			if (stack.getTagCompound().hasKey("JournalTitle"))
			{
				list.add(stack.getTagCompound().getString("JournalTitle"));
			}
		}
	}
}
