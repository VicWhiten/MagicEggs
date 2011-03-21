package com.bukkit.vicwhiten.magiceggs;

import com.bukkit.vicwhiten.magiceggs.MagicEggsPlayerListener;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerListener;

public class MagicEggsPlayerListener extends PlayerListener
{
    public final List<String> nations =Arrays.asList("Fire", "Water", "Earth", "Air", "Admins");
    public static MagicEggs plugin;
    public MagicEggsPlayerListener(MagicEggs instance)
    {
        plugin = instance;
    }

    public void onPlayerEggThrow(PlayerEggThrowEvent event)
    {
        event.setHatching(false);
        String group = plugin.wd.getDefaultWorld().getUser(event.getPlayer().getName()).getGroupName();
        int groupIndex = nations.indexOf(group);
        Block block = event.getEgg().getLocation().getBlock();
        if(groupIndex == 0)
        {
        	createFireCage(block);
        }
        if(groupIndex == 1)
        {
        	createIgloo(block);
        }
        if(groupIndex == 2)
        {
        	createSlowPatch(block);
        }
        if(groupIndex == 3)
        {
        	createPitfall(block);
        }
        if(groupIndex == 4)
        {
        	createCage(block);
        	
        }
    }
    
    public void createFireCage(Block source)
    {
    	setBlockRing(source, Material.FIRE, 4000, 4, true);
    }
    public void createPond(Block source)
    {
    	setBlockCircle(source.getRelative(BlockFace.DOWN), Material.WATER, 4000, 4, false);
    }
    public void createPitfall(Block source)
    {
    	setBlockCircle(source.getRelative(BlockFace.DOWN),Material.AIR, 4000, 4, false);
    	setBlockCircle(source.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN),Material.AIR, 4000, 4, false);
    }
    public void createSlowPatch(Block source)
    {
    	setBlockCircle(source.getRelative(BlockFace.DOWN), Material.SOUL_SAND, 4000, 4, false);
    }
    
    public void createCage(Block source)
    {
    	setBlockCircle(source.getRelative(BlockFace.DOWN), Material.BEDROCK, 30000, 4, true);
    	setBlockRing(source, Material.BEDROCK, 30000, 4, true);
    	setBlockRing(source.getRelative(BlockFace.UP), Material.GLASS, 30000, 4, true);
    	setBlockCircle(source.getRelative(BlockFace.UP).getRelative(BlockFace.UP), Material.BEDROCK, 30000, 4, true);
    }
    public void createIgloo(Block source)
    {
    	setBlockCircle(source, Material.SNOW, 30000, 4, true);
    	setBlockRing(source, Material.SNOW_BLOCK,30000, 4, true);
    	setBlockRing(source.getRelative(BlockFace.UP), Material.SNOW_BLOCK,30000, 4, true);
    	setBlockCircle(source.getRelative(BlockFace.UP).getRelative(BlockFace.UP), Material.SNOW_BLOCK,30000, 4, true);
    	setBlockCircle(source.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP), Material.SNOW_BLOCK,30000, 2, true);
    	setBlockCircle(source.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP), Material.SNOW_BLOCK,30000, 1, true);
    }
    
    public void setBlockCircle(Block block, Material mat, int time, int count, boolean allowAir)
    {
    	
    	
    	setBlock(block, mat, time, allowAir);

        	if(count >= 1)
        	{
        		setBlockCircle(block.getRelative(BlockFace.SOUTH), mat, time, count -1, allowAir);
        		setBlockCircle(block.getRelative(BlockFace.NORTH), mat, time, count -1, allowAir);
        		setBlockCircle(block.getRelative(BlockFace.EAST), mat, time, count -1, allowAir);
        		setBlockCircle(block.getRelative(BlockFace.WEST),mat, time, count -1, allowAir);
        	}
    }
    
    public void setBlockRing(Block block, Material mat, int time, int count, boolean allowAir)
    {
    	
    	
    	setRingQuad(block, mat, BlockFace.NORTH, BlockFace.EAST, count, time, allowAir);
    	setRingQuad(block, mat, BlockFace.EAST, BlockFace.SOUTH, count, time, allowAir);
    	setRingQuad(block, mat, BlockFace.SOUTH, BlockFace.WEST, count, time, allowAir);
    	setRingQuad(block, mat, BlockFace.WEST, BlockFace.NORTH, count, time, allowAir);
    }
    
    
    public void setRingQuad(Block source, Material mat, BlockFace dir1, BlockFace dir2, int size, int time, boolean allowAir)
    {
    	int x = size;
    	int y = 0;
    	while(x >= 0)
    	{
    		Block block = source;
    		for(int i=0; i<x; i++)
    		{
    			block = block.getRelative(dir1);
    		}
    		for(int i=0; i<y; i++)
    		{
    			block = block.getRelative(dir2);
    		}
    		setBlock(block, mat, time, allowAir);
    		x--;
    		y++;
    	}
    }
     
    public void setBlock(Block block, Material mat, int time, boolean allowAir)
    {
    	if (!plugin.isModified.containsKey(block) && checkSensativeBlock(block) && block.getType() != mat &&
    			(block.getType() != Material.AIR || allowAir))
		{
    		if((mat != Material.LAVA && mat != Material.WATER) ||
    				(block.getRelative(BlockFace.NORTH).getType() != Material.AIR &&
        			block.getRelative(BlockFace.EAST).getType() != Material.AIR &&
        			block.getRelative(BlockFace.SOUTH).getType() != Material.AIR &&
        			block.getRelative(BlockFace.WEST).getType() != Material.AIR ))
        	{
        		Timer t = new Timer();
        		t.schedule(new revertBlockTimerTask(plugin,block, block.getType()), time);
        		block.setType(mat);	
        		plugin.isModified.put(block, true);
        	}
		}

    }
    public boolean checkSensativeBlock(Block block)
    {
    	if (block.getType() != Material.CHEST && block.getType() != Material.SIGN_POST && block.getType() != Material.WORKBENCH && block.getType() != Material.FURNACE && block.getType() != Material.BURNING_FURNACE && block.getType() != Material.WOODEN_DOOR && block.getType() != Material.LADDER && block.getType() != Material.WALL_SIGN && block.getType() != Material.IRON_DOOR_BLOCK && block.getType() != Material.SAPLING && block.getType() != Material.REDSTONE_TORCH_OFF && block.getType() != Material.REDSTONE_TORCH_ON && block.getType() != Material.MOB_SPAWNER && block.getType() != Material.DIODE_BLOCK_OFF && block.getType() != Material.DIODE_BLOCK_ON && block.getType() != Material.PAINTING && block.getType() != Material.WOOL && block.getType() != Material.SPONGE && block.getType() != Material.REDSTONE_WIRE && block.getType() != Material.LEVER && block.getType() != Material.STONE_PLATE && block.getType() != Material.WOOD_PLATE && block.getType() != Material.STONE_BUTTON && block.getType() != Material.RAILS && block.getType() != Material.TORCH && block.getType() != Material.FENCE && block.getType() != Material.WOOD_STAIRS && block.getType() != Material.COBBLESTONE_STAIRS)
        {
    		return true;
        }else return false;
    }
}   
    class revertBlockTimerTask extends TimerTask
    {
    	Block block;
    	Material origType;
    	MagicEggs plugin;
		public revertBlockTimerTask(MagicEggs plug, Block b, Material originalType)
		{
			block = b;
			origType = originalType;
			plugin = plug;
			
		}
		public void run() {
			block.setType(origType);
			plugin.isModified.remove(block);
			
		}
    	
    }
