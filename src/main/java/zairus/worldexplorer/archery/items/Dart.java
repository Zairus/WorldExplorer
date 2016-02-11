package zairus.worldexplorer.archery.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import zairus.worldexplorer.core.WorldExplorer;
import zairus.worldexplorer.core.items.WEItem;

/**
 * History
 * 20150115: BlowPipe Suggested by Tshallacka
 * */

public class Dart
	extends WEItem
{
	public static final String[] dart_types = new String[] {"dart"};
	public static String KEY_HASEFFECT = "dartHasEffect";
	public static String KEY_EFFECTID = "dartEffectId";
	public static String KEY_EFFECTNAME = "dartEffectName";
	
	private IIcon[] dartIcons;
	
	public Dart()
	{
		super();
		
		setUnlocalizedName("dart");
		setTextureName("worldexplorer:dart");
		setCreativeTab(WorldExplorer.tabWorldExplorer);
		setHasSubtypes(true);
		
		this.maxStackSize = 64;
		//RenderItem
		//damage Nausea:9, blind:15, hunger:17, wither:20
		// 1: regen id:10
		// 2: swiftness id:1
		// 3: fire resist id:12
		// 4: poison id:19
		// 5: healing id:6
		// 6: night vision id:16
		// 7: clear
		// 8: weakness id:18
		// 9: strength id:5
		// 10: slowness id:2
		// 11: diffuse
		// 12: harming id:7
		// 13: water breathing id:13
		// 14: invisibility id:14
		// 15: thin
		// 16: awkward
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
		int j = MathHelper.clamp_int(damage, 0, dart_types.length - 1);;
		
		return dartIcons[j];
    }
	
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass)
    {
		boolean hasEffect = super.hasEffect(stack, pass);
		
		if (stack.hasTagCompound())
		{
			if (stack.getTagCompound().hasKey(Dart.KEY_HASEFFECT))
			{
				hasEffect = stack.getTagCompound().getBoolean(Dart.KEY_HASEFFECT);
			}
		}
		
		return hasEffect;
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		int i = MathHelper.clamp_int(stack.getItemDamage(), 0, dart_types.length - 1);
		return super.getUnlocalizedName() + "." + dart_types[i];
	}
	
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, @SuppressWarnings("rawtypes") List list)
    {
		for (int i = 0; i < dart_types.length; ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
		dartIcons = new IIcon[dart_types.length];
		
		for (int i = 0; i < dart_types.length; ++i)
		{
			this.dartIcons[i] = iconRegister.registerIcon(this.getIconString());
		}
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound tag = stack.getTagCompound();
			
			if (tag.hasKey(Dart.KEY_EFFECTNAME))
				list.add("Effect: " + tag.getString(Dart.KEY_EFFECTNAME));
			
			if (tag.hasKey(Dart.KEY_EFFECTID))
				list.add("Id: " + tag.getString(Dart.KEY_EFFECTID));
		}
	}
}
