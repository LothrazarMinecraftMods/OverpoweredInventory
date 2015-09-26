package com.lothrazar.powerinventory.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.inventory.slot.SlotBottle;
import com.lothrazar.powerinventory.inventory.slot.SlotClock;
import com.lothrazar.powerinventory.inventory.slot.SlotCompass;
import com.lothrazar.powerinventory.inventory.slot.SlotEnderChest;
import com.lothrazar.powerinventory.inventory.slot.SlotEnderPearl;

public class OverpoweredContainerPlayer extends ContainerPlayer implements IOverpoweredContainer
{	
	@Override
	public void addSlot(Slot s)
	{
        super.addSlotToContainer(s);
	}
	@Override
	public int getSlotCount()
	{
		return inventorySlots.size();
	}
	
	
	private final int craftSize = 3;//did not exist before, was magic'd as 2 everywhere
    private final EntityPlayer thePlayer;
 
	public OverpoweredInventoryPlayer invo;
    public boolean isLocalWorld;

	//these get used here for actual slot, and in GUI for texture
    //ender pearl is in the far bottom right corner, and the others move left relative to this


	
//store slot numbers  (not indexes) as we go. so that transferStack.. is actually readable
	 
	int S_RESULT;
	int S_CRAFT_START;
	int S_CRAFT_END;
	int S_ARMOR_START;
	int S_ARMOR_END;
	int S_BAR_START;
	int S_BAR_END;
	int S_MAIN_START;
	int S_MAIN_END;
	int S_ECHEST;
	int S_PEARL;
	int S_CLOCK;
	int S_COMPASS;
	int S_BOTTLE;
	int S_UNCRAFT;
	
	public OverpoweredContainerPlayer(OverpoweredInventoryPlayer playerInventory, boolean isLocal, EntityPlayer player)
	{
		super(playerInventory, isLocal, player);
        this.thePlayer = player;
		inventorySlots = Lists.newArrayList();//undo everything done by super()
		craftMatrix = new InventoryCrafting(this, craftSize, craftSize);
 
		
		
        int i,j,cx,cy;//rows and cols of vanilla, not extra
        S_RESULT = this.getSlotCount();
        this.addSlot(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 
        		200,  
        		40));
        
        S_CRAFT_START = this.getSlotCount();
        for (i = 0; i < craftSize; ++i)
        { 
            for (j = 0; j < craftSize; ++j)
            {  
    			cx = 114 + j * Const.square ; 
    			cy = 20 + i * Const.square ;

    			this.addSlot(new Slot(this.craftMatrix, j + i * this.craftSize, cx , cy)); 
            }
        }
        S_CRAFT_END = this.getSlotCount() - 1;
        
        
        //InventoryBuilder.setupContainer(this);
		

		OverpoweredContainerPlayer self = this;
        
		
		
		self.S_ARMOR_START = self.getSlotCount();
        for (i = 0; i < Const.armorSize; ++i)
        {
        	cx = 8;
        	cy = 8 + i * Const.square;
            final int k = i;
 
            self.addSlot(new Slot(playerInventory,  playerInventory.getSizeInventory() - 1 - i, cx, cy)
            { 
            	public int getSlotStackLimit()
	            {
	                return 1;
	            }
	            public boolean isItemValid(ItemStack stack)
	            {
	                if (stack == null) return false;
	                return stack.getItem().isValidArmor(stack, k, thePlayer);
	            }
	            @SideOnly(Side.CLIENT)
	            public String getSlotTexture()
	            {
	                return ItemArmor.EMPTY_SLOT_NAMES[k];
	            }
            }); 
        }
        S_ARMOR_END = self.getSlotCount() - 1;
        
        S_BAR_START = self.getSlotCount();
        for (i = 0; i < Const.hotbarSize; ++i)
        { 
        	cx = 8 + i * Const.square;
        	cy = 142 + (Const.square * Const.MORE_ROWS);
 
        	self.addSlot(new Slot(playerInventory, i, cx, cy));
        }
        S_BAR_END = self.getSlotCount() - 1;
        
        
        S_MAIN_START = self.getSlotCount();
        int slotIndex = Const.hotbarSize;
        
        for( i = 0; i < Const.ALL_ROWS; i++)
		{
            for ( j = 0; j < Const.ALL_COLS; ++j)
            { 
            	cx = 8 + j * Const.square;
            	cy = 84 + i * Const.square;
            	self.addSlot(new Slot(playerInventory, slotIndex, cx, cy));
            	slotIndex++;
            }
        }
        S_MAIN_END = self.getSlotCount() - 1;
        
        
        S_PEARL =  self.getSlotCount() ;
        self.addSlot(new SlotEnderPearl(playerInventory, Const.enderPearlSlot, InventoryBuilder.pearlX, InventoryBuilder.pearlY));

        S_ECHEST =  self.getSlotCount() ;
        self.addSlot(new SlotEnderChest(playerInventory, Const.enderChestSlot, InventoryBuilder.echestX, InventoryBuilder.echestY)); 

        S_CLOCK =  self.getSlotCount();
        self.addSlot(new SlotClock(playerInventory, Const.clockSlot, InventoryBuilder.clockX, InventoryBuilder.clockY)); 

        S_COMPASS = self.getSlotCount() ;
        self.addSlot(new SlotCompass(playerInventory, Const.compassSlot, InventoryBuilder.compassX, InventoryBuilder.compassY)); 
        
        S_BOTTLE = self.getSlotCount() ;
        self.addSlot(new SlotBottle(playerInventory, Const.bottleSlot, InventoryBuilder.bottleX, InventoryBuilder.bottleY)); 
        
        S_UNCRAFT =self.getSlotCount() ;
        self.addSlot(new Slot(playerInventory, Const.uncraftSlot, InventoryBuilder.uncraftX, InventoryBuilder.uncraftY)); 

        
        
		this.invo = playerInventory; 
        this.onCraftMatrixChanged(this.craftMatrix);
	}
  
	@Override
	public Slot getSlotFromInventory(IInventory invo, int id)
	{
		Slot slot = super.getSlotFromInventory(invo, id);
	
		return slot;
	}

	@Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
// we COULD not empty it BUT then it gets erased on logout, etc
        for (int i = 0; i < craftSize*craftSize; ++i) // was 4
        {
            ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

            if (itemstack != null)
            {
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }

        this.craftResult.setInventorySlotContents(0, (ItemStack)null);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
	@Override
    public ItemStack transferStackInSlot(EntityPlayer p, int slotNumber)
    {  
		if(p.capabilities.isCreativeMode)
		{
			return super.transferStackInSlot(p, slotNumber);
		}
		 
		//Thanks to coolAlias on the forums : 
		//http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571051-custom-container-how-to-properly-override-shift
		//above is from 2013 but still relevant
        ItemStack stackCopy = null;
        Slot slot = (Slot)this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack())
        {
            ItemStack stackOrig = slot.getStack();
            stackCopy = stackOrig.copy();
            if (slotNumber == S_RESULT)  
            { 
            	//System.out.printf("\ntest result %d %d ___  ",S_MAIN_START,S_MAIN_END);
                if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))//was starting at S_BAR_START
                {
                    return null;
                }

                slot.onSlotChange(stackOrig, stackCopy);
            }
            else if (slotNumber >= S_CRAFT_START && slotNumber <= S_CRAFT_END) 
            { 
                if (!this.mergeItemStack(stackOrig,  S_BAR_START, S_MAIN_END, false))//was 9,45
                {
                    return null;
                }
            }
            else if (slotNumber >= S_ARMOR_START && slotNumber <= S_ARMOR_END) 
            { 
                if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))
                {
                    return null;
                }
            }
            else if (stackCopy.getItem() instanceof ItemArmor 
            		&& !((Slot)this.inventorySlots.get(S_ARMOR_START + ((ItemArmor)stackCopy.getItem()).armorType)).getHasStack()) // Inventory to armor
            { 
            	int j = S_ARMOR_START + ((ItemArmor)stackCopy.getItem()).armorType;
           
            	if (!this.mergeItemStack(stackOrig, j, j+1, false))
                {
                    return null;
                } 
            }
            else if (slotNumber >= S_MAIN_START && slotNumber <= S_MAIN_END) // main inv grid
            { 
            	//only from here are we doing the special items
            	
            	if(stackCopy.getItem() == Items.ender_pearl && 
            		(
        			p.inventory.getStackInSlot(Const.enderPearlSlot) == null || 
        			p.inventory.getStackInSlot(Const.enderPearlSlot).stackSize < Items.ender_pearl.getItemStackLimit(stackCopy))
        			)
        		{
            		 
            		if (!this.mergeItemStack(stackOrig, S_PEARL, S_PEARL+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Item.getItemFromBlock(Blocks.ender_chest) && 
            		(
        			p.inventory.getStackInSlot(Const.enderChestSlot) == null || 
        			p.inventory.getStackInSlot(Const.enderChestSlot).stackSize < 1)
        			)
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_ECHEST, S_ECHEST+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Items.compass && 
            		(
        			p.inventory.getStackInSlot(Const.compassSlot) == null || 
        			p.inventory.getStackInSlot(Const.compassSlot).stackSize < 1)
        			)
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_COMPASS, S_COMPASS+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Items.clock && 
            		(
        			p.inventory.getStackInSlot(Const.clockSlot) == null || 
        			p.inventory.getStackInSlot(Const.clockSlot).stackSize < 1)
        			)
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_CLOCK, S_CLOCK+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Items.glass_bottle )
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_BOTTLE, S_BOTTLE+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if (!this.mergeItemStack(stackOrig, S_BAR_START, S_BAR_END+1, false)            			)
            	{
            		
                    return null;
                }
            }
            else if (slotNumber >= S_BAR_START && slotNumber <= S_BAR_END) // Hotbar
            { 
            	if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))
            	{
                    return null;
                }
            }
            else if(slotNumber == S_PEARL || slotNumber == S_ECHEST  || slotNumber == S_COMPASS  || slotNumber == S_CLOCK || slotNumber == S_BOTTLE
            		|| slotNumber == S_UNCRAFT)
            { 
            	if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))
            	{
                    return null;
                }
            }
            else if (!this.mergeItemStack(stackOrig, Const.hotbarSize, invo.getSlotsNotArmor() + Const.hotbarSize, false)) // Full range
            {
                return null;
            }
            if (stackOrig.stackSize == 0)
            { 
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (stackOrig.stackSize == stackCopy.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(p, stackOrig);
        }

        return stackCopy;
    }
}
