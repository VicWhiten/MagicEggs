package com.bukkit.vicwhiten.magiceggs;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;



public class MagicEggsBlockListener extends BlockListener {

	private MagicEggs plugin;

	public MagicEggsBlockListener(MagicEggs plug) {
		plugin = plug;
	}
	
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
    	Block block = event.getBlock();
    	System.out.println("Type: " + block.getType());
    	System.out.println("Data: " + (int)block.getData());
	}
}