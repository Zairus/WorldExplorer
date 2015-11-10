package zairus.worldexplorer.equipment.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWhip
	extends ModelBase
{
	public ModelRenderer modelRenderer;
	
	public static enum WhipPart
	{
		partHandle,
		partBody
	}
	
	public ModelWhip()
	{
		this(0, 0, 32, 32);
	}
	
	public ModelWhip(int textureOffsetX, int textureOffsetY, int textureWidth, int textureHeight)
	{
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.modelRenderer = new ModelRenderer(this, textureOffsetX, textureOffsetY);
		this.modelRenderer.addBox(0.0F, 0.0F, 0.0F, 2, 10, 2, 0.0F);
		this.modelRenderer.setRotationPoint(0.0F, 0.0F, 0.0F);
	}
	
	public ModelWhip(WhipPart part)
	{
		switch (part)
		{
		case partBody:
			this.textureWidth = 32;
			this.textureHeight = 32;
			this.modelRenderer = new ModelRenderer(this, 16, 0);
			this.modelRenderer.addBox(0.0F, 0.0F, 0.0F, 1, 10, 1, 0.0F);
			this.modelRenderer.setRotationPoint(0.0F, 0.0F, 0.0F);
			break;
		default:
			this.textureWidth = 32;
			this.textureHeight = 32;
			this.modelRenderer = new ModelRenderer(this, 0, 0);
			this.modelRenderer.addBox(0.0F, 0.0F, 0.0F, 2, 10, 2, 0.0F);
			this.modelRenderer.setRotationPoint(0.0F, 0.0F, 0.0F);
			break;
		}
	}
	
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		this.modelRenderer.render(p_78088_7_);
	}
	
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
		this.modelRenderer.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
		this.modelRenderer.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
	}
}
