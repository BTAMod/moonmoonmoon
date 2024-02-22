package useless.moonsteel;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import turniplabs.halplibe.helper.TextureHelper;

public class ItemConnectedStar extends Item {
	public ItemConnectedStar(String name, int id) {
		super(name, id);
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (itemstack.getData().getBoolean("moonsteel$has_location")){
			int destX = itemstack.getData().getInteger("moonsteel$x");
			int destY = itemstack.getData().getInteger("moonsteel$y");
			int destZ = itemstack.getData().getInteger("moonsteel$z");
			MoonSteel.forceChunkLoads = true;
			Chunk chunk = world.getChunkProvider().provideChunk(destX >> 4, destZ >> 4);
			MoonSteel.forceChunkLoads = false;
			TileEntity te = chunk.getTileEntity(destX &0xF, destY, destZ &0xF);
			if (te instanceof TileEntityStellarRewinder && ((TileEntityStellarRewinder) te).canTeleport(itemstack)){
				Side side = ((TileEntityStellarRewinder) te).side;
				entityplayer.setPos(destX + side.getOffsetX() + 0.5f, destY + side.getOffsetY() + 2, destZ + side.getOffsetZ() + 0.5f);
				((TileEntityStellarRewinder) te).setInUse(false);
			} else {
				entityplayer.addChatMessage("moonsteel.teleport.fail");
			}
			itemstack.getData().putBoolean("moonsteel$has_location", false);
		}
		return itemstack;
	}
	public int getIconIndex(ItemStack itemstack) {
		if (itemstack.getData().getBoolean("moonsteel$has_location")){
			return TextureHelper.getOrCreateItemTextureIndex(MoonSteel.MOD_ID, "connected_star.png");
		}
		return this.getIconFromDamage(itemstack.getMetadata());
	}
}