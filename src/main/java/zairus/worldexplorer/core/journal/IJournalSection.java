package zairus.worldexplorer.core.journal;

import net.minecraft.item.ItemStack;

public interface IJournalSection
{
	public IJournalSection setTitle(String title);
	public String getTitle();
	public ItemStack getIconStack();
	public void setContent(String content);
	public String getContent();
}
