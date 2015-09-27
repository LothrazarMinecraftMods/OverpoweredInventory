package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.network.EnderChestPacket;
import com.lothrazar.powerinventory.ModInv;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiOpenEnder extends GuiButton 
{ 
    public GuiOpenEnder(int buttonId, int x, int y, int w,int h)
    {
    	super(buttonId, x, y, w,h,"I"); 
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);

    	if(pressed)
    	{
			ModInv.instance.network.sendToServer(new EnderChestPacket( new NBTTagCompound()));
    		  
    	}
    	
    	return pressed;
    }
}
