package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.*;
import com.lothrazar.powerinventory.CapabilityRegistry.IPlayerExtendedProperties;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EnderPearlPacket implements IMessage, IMessageHandler<EnderPearlPacket, IMessage> {
	public EnderPearlPacket() {
	}

	NBTTagCompound tags = new NBTTagCompound();

	public EnderPearlPacket(NBTTagCompound ptags) {
		tags = ptags;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.tags);
	}

	@Override
	public IMessage onMessage(EnderPearlPacket message, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;

    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(p);

		ItemStack pearls = prop.inventory.getStackInSlot(Const.SLOT_EPEARL);

		if (pearls != null) {
			p.worldObj.spawnEntityInWorld(new EntityEnderPearl(p.worldObj, p));

			ModInv.playSound(p, SoundEvents.ENTITY_ARROW_SHOOT);
			if (p.capabilities.isCreativeMode == false)
				prop.inventory.decrStackSize(Const.SLOT_EPEARL, 1);
		}

		return null;
	}
}
