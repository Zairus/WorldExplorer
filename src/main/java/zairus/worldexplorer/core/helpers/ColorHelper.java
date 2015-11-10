package zairus.worldexplorer.core.helpers;

import java.util.HashMap;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

public class ColorHelper
{
	private static HashMap<String, HashMap<Integer, Integer>> cachedStartEndBitMaps = new HashMap<String, HashMap<Integer, Integer>>();
	
	public static void glSetColor(int color)
	{
		glSetColor(color, 1.0F);
	}
	
	public static void glSetColor(int color, float alpha)
	{
		GL11.glColor4f(getRGB('r', color) / 255.0F, getRGB('g', color) / 255.0F, getRGB('b', color) / 255.0F, alpha);
	}
	
	private static int getRGB(char rgb, int color)
	{
		switch (rgb)
		{
			case 'r':
				return getInteger(color, 16, 23);
			case 'g':
				return getInteger(color, 8, 15);
			case 'b':
				return getInteger(color, 0, 7);
		}
		return 0;
	}
	
	private static int getInteger(int source, int start, int end)
	{
		return getInteger(source, getBitMap(start, end));
	}
	
	private static int getInteger(int source, HashMap<Integer, Integer> bitMap)
	{
		int output = 0;
		
		for (Iterator<Integer> i$ = bitMap.keySet().iterator(); i$.hasNext();)
		{
			int bitLocation = ((Integer)i$.next()).intValue();
			
			boolean bit = (source & 1 << bitLocation) != 0;
			if (bit) {
				output += (1 << ((Integer)bitMap.get(Integer.valueOf(bitLocation))).intValue());
			}
		}
		return output;
	}
	
	private static HashMap<Integer, Integer> getBitMap(int start, int end)
	{
		HashMap<Integer, Integer> bitMap = null;
		String cacheKey = start + "," + end;
		
		if (cachedStartEndBitMaps.containsKey(cacheKey))
		{
			bitMap = (HashMap<Integer, Integer>)cachedStartEndBitMaps.get(cacheKey);
		}
		else
		{
			bitMap = new HashMap<Integer, Integer>();
			for (int i = start; i <= end; i++) {
				bitMap.put(Integer.valueOf(i), Integer.valueOf(i - start));
			}
			cachedStartEndBitMaps.put(cacheKey, bitMap);
		}
		return bitMap;
	}
}
