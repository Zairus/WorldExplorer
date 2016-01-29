package zairus.worldexplorer.core.journal;

import net.minecraft.item.ItemStack;
import zairus.worldexplorer.core.items.WorldExplorerItems;

public class JournalSectionMain
	implements IJournalSection
{
	private String title = "";
	String content = "";
	
	@Override
	public String getContent()
	{
		content = "";
		
		content += "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor ";
		content += "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis ";
		content += "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ";
		content += "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore ";
		content += "eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, ";
		content += "sunt in culpa qui officia deserunt mollit anim id est laborum.";
		content += "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor ";
		content += "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis ";
		content += "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ";
		content += "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore ";
		content += "eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, ";
		content += "sunt in culpa qui officia deserunt mollit anim id est laborum. END";
		
		content += "";
		
		return content;
	}
	
	@Override
	public void setContent(String content)
	{
		;
	}
	
	@Override
	public String getTitle()
	{
		return title;
	}
	
	@Override
	public ItemStack getIconStack()
	{
		return new ItemStack(WorldExplorerItems.journal);
	}
	
	@Override
	public IJournalSection setTitle(String sTitle)
	{
		title = sTitle;
		return this;
	}
}
