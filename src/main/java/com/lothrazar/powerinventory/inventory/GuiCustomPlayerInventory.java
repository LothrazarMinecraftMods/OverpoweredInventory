package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.UtilTextureRender;
import com.lothrazar.powerinventory.inventory.client.GuiButtonRotate;
import com.lothrazar.powerinventory.inventory.slot.*;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer; 
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiCustomPlayerInventory extends GuiContainer
{
	private ResourceLocation bkg = new ResourceLocation(Const.MODID,  "textures/gui/inventory.png");
	public static boolean SHOW_DEBUG_NUMS = true;
	private final InventoryCustomPlayer inventory;
	private ContainerCustomPlayer container;
	
	public GuiCustomPlayerInventory(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryCustomPlayer inventoryCustom)
	{
		//the player.inventory gets passed in here
		super(new ContainerCustomPlayer(player, inventoryPlayer, inventoryCustom));
		container = (ContainerCustomPlayer)this.inventorySlots;
		inventory = inventoryCustom;
		
		//fixed numbers from the .png resource size
		this.xSize = 338;
		this.ySize = 221;
	}
	
	@Override
	public void initGui()
    { 
		super.initGui();
		int button_id = 99;
		final int height = 20;
		int width = 26;
		final int padding = 6;
		int offset = 0;
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
				this.guiLeft + this.xSize - width - padding + offset, 
				this.guiTop + padding, 1));
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
				this.guiLeft + this.xSize - width - padding + offset, 
				this.guiTop + padding+height, 2));
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
				this.guiLeft + this.xSize - width - padding + offset, 
				this.guiTop + 2*(padding+height), 3));
    }
	
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		
		if(SHOW_DEBUG_NUMS){ 
			for(Slot s : this.container.inventorySlots)
			{
				//each slot has two different numbers. the slotNumber is UNIQUE, the index is not
				this.drawString(this.fontRendererObj, "" + s.getSlotIndex(), 
						this.guiLeft + s.xDisplayPosition,
						this.guiTop + s.yDisplayPosition +  4, 
						16777120);//font color
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{ 
		this.checkSlotsEmpty();
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	private void checkSlotsEmpty()
	{
		final int s = 16;

		if(inventory.getStackInSlot(Const.enderPearlSlot) == null)
		{  
			UtilTextureRender.drawTextureSimple(SlotEnderPearl.background,SlotEnderPearl.posX, SlotEnderPearl.posY,s,s);
		}

		if(inventory.getStackInSlot(Const.enderChestSlot) == null)
		{  
			UtilTextureRender.drawTextureSimple(SlotEnderChest.background,SlotEnderChest.posX, SlotEnderChest.posY,s,s);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{ 
		UtilTextureRender.drawTextureSimple(bkg, this.guiLeft, this.guiTop,this.xSize,this.ySize);
	 
        drawSlotAt(SlotEnderChest.posX, SlotEnderChest.posY);
    	drawSlotAt(SlotEnderPearl.posX, SlotEnderPearl.posY);
	}
	
	private void drawSlotAt(int x, int y)
	{
        UtilTextureRender.drawTextureSimple(Const.slot,this.guiLeft + x - 1, this.guiTop + y - 1, Const.SQ, Const.SQ);
	}
}
