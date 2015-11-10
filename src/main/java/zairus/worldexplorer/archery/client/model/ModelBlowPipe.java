package zairus.worldexplorer.archery.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBlowPipe
	extends ModelBase
{
	ModelRenderer MainStraw;
	ModelRenderer Binding1;
	ModelRenderer Binding2;
	
	public ModelBlowPipe()
	{
		textureWidth = 32;
		textureHeight = 32;
		
		MainStraw = new ModelRenderer(this, 0, 3);
		MainStraw.addBox(0F, 0F, 0F, 1, 16, 1);
		MainStraw.setRotationPoint(0F, 8F, 0F);
		MainStraw.setTextureSize(32, 32);
		setRotation(MainStraw, 0F, 0F, 0F);
		
		Binding1 = new ModelRenderer(this, 0, 0);
		Binding1.addBox(0F, 0F, 0F, 2, 1, 2);
		Binding1.setRotationPoint(-0.5F, 21F, -0.5F);
		Binding1.setTextureSize(32, 32);
		setRotation(Binding1, 0F, 0F, 0F);
		
		Binding2 = new ModelRenderer(this, 0, 0);
		Binding2.addBox(0F, 0F, 0F, 2, 1, 2);
		Binding2.setRotationPoint(-0.5F, 17F, -0.5F);
		Binding2.setTextureSize(32, 32);
		setRotation(Binding2, 0F, 0F, 0F);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		MainStraw.render(f5);
		Binding1.render(f5);
		Binding2.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}
