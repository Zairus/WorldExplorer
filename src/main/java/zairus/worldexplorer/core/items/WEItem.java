package zairus.worldexplorer.core.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class WEItem
	extends Item
{
	protected List<Improvement> itemImprovements = new ArrayList<Improvement>();
	
	public WEItem()
	{
		addImprovements();
	}
	
	@Override
	public WEItem setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(name);
		return this;
	}
	
	@Override
	public WEItem setTextureName(String name)
	{
		this.iconString = name;
		return this;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return super.getUnlocalizedName();
	}
	
	@Override
	public WEItem setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
	@Override
	public WEItem setMaxStackSize(int size)
    {
        super.setMaxStackSize(size);
        return this;
    }
	
	protected void addImprovements() {}
	
	public List<Improvement> getItemImprovements()
	{
		return this.itemImprovements;
	}
	
	public boolean hasImprovements()
	{
		return this.itemImprovements.size() > 0;
	}
	
	public Improvement getImprovementFromMaterial(Item material)
	{
		Improvement imp = null;
		
		find_loop:
		for (int i = 0; i < this.itemImprovements.size(); ++i)
		{
			if (this.itemImprovements.get(i).material == material)
			{
				imp = this.itemImprovements.get(i);
				break find_loop;
			}
		}
		
		return imp;
	}
	
	/** Item improvement type */
	public static enum ImprovementType
	{
		/** Has an effect on impact of the tool/item */
		IMPACT("Gunpowder Modifier", 3.0f)
		/** Causes the item or tool to be stick or hold things */
		,ADHERENCE("Slime Modifier", 8.0f)
		/** Adds power or strengths the action of item/tool */
		,POWER("Glowstone Modifier", 3.0f)
		/** Accelerates function or speed */
		,ENERGETIC("Redstone Modifier", 48.0f)
		/** Supernatural modifier */
		,ENDER("Ender Modifier", 100.0f)
		/** Adds fire power: Heat, Melting */
		,BLAZE("Fire Modifier", 3.0f);
		
		private String improvementKey;
		private float maxValue;
		
		private ImprovementType(String key, float maxValue)
		{
			this.improvementKey = key;
			this.maxValue = maxValue;
		}
		
		public String getKey()
		{
			return this.improvementKey;
		}
		
		public float getMaxValue()
		{
			return this.maxValue;
		}
		
		@Override
		public String toString()
		{
			return super.toString();
		}
	}
	
	/** Represents improvement for item */
	public class Improvement
	{
		public final Item material;
		public final ImprovementType improvementType;
		public final float valuePerUnit;
		
		public Improvement(Item material, ImprovementType type, float value)
		{
			this.material = material;
			this.improvementType = type;
			this.valuePerUnit = value;
		}
	}
}
