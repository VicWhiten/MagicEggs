package com.bukkit.vicwhiten.magiceggs;

import com.bukkit.vicwhiten.magiceggs.MagicEggsPlayerListener;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
       	String player = event.getPlayer().getName();
       	boolean canThrow;
       	try{
       	canThrow = plugin.canThrow.get(player);
       	if(canThrow !=true && canThrow!= false)
       	{
       		plugin.canThrow.put(player, true);
       	}}catch(Exception e)
       	{
       		plugin.canThrow.put(player, true);
       	}
       	if(plugin.canThrow.get(player))
       	{
       		try{
       		plugin.canThrow.put(player, false);
        	String group = plugin.wd.getDefaultWorld().getUser(player).getGroupName();
        	int groupIndex = nations.indexOf(group);
        	Block block = event.getEgg().getLocation().getBlock();
        	if(groupIndex == 0)
        	{
        	createCactiRing(block);
        	}
        	if(groupIndex == 1)
        	{
        	createPond(block);
        	}
        	if(groupIndex == 2)
        	{
        	createSlowPatch(block);
        	}
        	if(groupIndex == 3)
        	{
        	createBouncePad(block);
        	}
        	if(groupIndex == 4)
        	{
        		createCage(block);
        	}
        	Timer t = new Timer();
        	t.schedule(new ResetPlayerCooldownTimerTask(event.getPlayer()),  30000);
       		}catch(Exception e)
       		{
       			event.getPlayer().sendMessage("Spell Fizzled!");
       			plugin.canThrow.put(player, true);
       		}
        	
        }else
        {
        	event.getPlayer().sendMessage(ChatColor.YELLOW + "Charging Magic, cannot use now!");
        	PlayerInventory inv = event.getPlayer().getInventory();
        	inv.addItem(new ItemStack(Material.EGG, 1));
        }
    }
    
    public void createFireCage(Block source)
    {
    	setBlockRing(source, Material.FIRE, 4000, 4, true);
    }
    
    public void createBouncePad(Block source)
    {
    	setBlockCircle(source.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN),Material.DIRT, 4020,4,true);
    	setBlockCircle(source.getRelative(BlockFace.DOWN), Material.SIGN_POST, 4010, 4, true);
    	setBlockCircle(source, Material.STEP, 4000, 4, true);
    }
    
    public void createCactiRing(Block source)
    {
    	setBlockCircle(source.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN), Material.DIRT, 4020, 4, false);
    	setBlockCircle(source.getRelative(BlockFace.DOWN), Material.SAND, 4010, 4, false);
    	setBlockRing(source, Material.CACTUS, 4000, 4, true);
    }
    public void createPond(Block source)
    {
    	setBlockCircle(source.getRelative(BlockFace.DOWN), Material.WATER, 4000, 4, false);
    	setBlockCircle(source.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN), Material.DIRT,4000,4,false);
    	
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
    
    public void createIcePatch(Block source)
    {
    	setBlockCircle(source.getRelative(BlockFace.DOWN), Material.ICE, 4000, 4, false);
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
    	setBlockCircle(source.getRelative(BlockFace.DOWN), Material.SNOW_BLOCK, 30000, 4, true);
    	setBlockRing(source, Material.SNOW_BLOCK,30000, 4, true);
    	setBlockRing(source.getRelative(BlockFace.UP), Material.SNOW_BLOCK,30000, 4, true);
    	setBlockCircle(source.getRelative(BlockFace.UP).getRelative(BlockFace.UP), Material.SNOW_BLOCK,30000, 4, true);
    	setBlockCircle(source.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP), Material.SNOW_BLOCK,30000, 2, true);
    	setBlockCircle(source.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP), Material.SNOW_BLOCK,30000, 1, true);
    }
    
    public void setBlockCircle(Block block, Material mat, int time, int count, boolean allowAir)
    { 
    	setBlockCircleHelper(block,mat,time,count,allowAir);
    	Timer t = new Timer();
    	t.schedule(new RevertBlockCircleTimerTask(block, count), time);
    }
    public void setBlockCircleHelper(Block block, Material mat, int time, int count, boolean allowAir)
    {
    	
    	
    	setBlock(block, mat, allowAir);

        	if(count >= 1)
        	{
        		setBlockCircleHelper(block.getRelative(BlockFace.SOUTH), mat, time, count -1, allowAir);
        		setBlockCircleHelper(block.getRelative(BlockFace.NORTH), mat, time, count -1, allowAir);
        		setBlockCircleHelper(block.getRelative(BlockFace.EAST), mat, time, count -1, allowAir);
        		setBlockCircleHelper(block.getRelative(BlockFace.WEST),mat, time, count -1, allowAir);
        	}
    }
    
    public void restoreBlockCircle(Block block,int count)
    {

    	restoreBlock(block);

    	
    	if(count >= 1)
    	{
    		restoreBlockCircle(block.getRelative(BlockFace.SOUTH),count - 1);
    		restoreBlockCircle(block.getRelative(BlockFace.NORTH),count - 1);
    		restoreBlockCircle(block.getRelative(BlockFace.EAST),count - 1);
    		restoreBlockCircle(block.getRelative(BlockFace.WEST),count - 1);
    	}
    	
    }
    
    public void setBlockRing(Block block, Material mat, int time, int count, boolean allowAir)
    {
    	
    	
    	setRingQuad(block, mat, BlockFace.NORTH, BlockFace.EAST, count, time, allowAir);
    	setRingQuad(block, mat, BlockFace.EAST, BlockFace.SOUTH, count, time, allowAir);
    	setRingQuad(block, mat, BlockFace.SOUTH, BlockFace.WEST, count, time, allowAir);
    	setRingQuad(block, mat, BlockFace.WEST, BlockFace.NORTH, count, time, allowAir);
    	
    	Timer t = new Timer();
    	t.schedule(new RevertBlockRingTimerTask(block, count), time);
    }
    
    public void restoreBlockRing(Block block,int count)
    {
    	restoreRingQuad(block, BlockFace.NORTH, BlockFace.EAST, count);
    	restoreRingQuad(block, BlockFace.EAST, BlockFace.SOUTH, count);
    	restoreRingQuad(block, BlockFace.SOUTH, BlockFace.WEST, count);
    	restoreRingQuad(block, BlockFace.WEST, BlockFace.NORTH, count);
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
    		setBlock(block, mat, allowAir);
    		
    		x--;
    		y++;
    	}
    }
    
    public void restoreRingQuad(Block source, BlockFace dir1, BlockFace dir2, int size)
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

    		restoreBlock(block);

    		x--;
    		y++;
    	}
    }
     
    public void setBlock(Block block, Material mat, boolean allowAir)
    {
    	if (!plugin.isModified.containsKey(block.getLocation()) && checkSensativeBlock(block) && block.getType() != mat &&
    			(block.getType() != Material.AIR || allowAir))
		{
    		if((mat != Material.LAVA && mat != Material.WATER && mat != Material.STATIONARY_WATER && mat != Material.STATIONARY_LAVA)||
    				(block.getRelative(BlockFace.NORTH).getType() != Material.AIR &&
        			block.getRelative(BlockFace.EAST).getType() != Material.AIR &&
        			block.getRelative(BlockFace.SOUTH).getType() != Material.AIR &&
        			block.getRelative(BlockFace.WEST).getType() != Material.AIR ))
        	{
        		plugin.isModified.put(block.getLocation(), block.getType());
        		
        		//special case
        		if(mat == Material.STEP)
        		{
        		block.setData((byte)2);
        		}
        		
        		block.setType(mat);	
        		
        		
        		//special cases
        		if(mat == Material.SIGN_POST)
        		{	
        		Sign sign = (Sign)block.getState();
            	sign.setLine(0, "20");
        		}
        		
        		if(mat == Material.STEP)
        		{
        		block.setData((byte)2);
        		}
        		
        		

        	}
		}

    }
    
    public void restoreBlock(Block block)
    {
    	Material original = plugin.isModified.get(block.getLocation());
    	if(original != null)
    	{
    		block.setType(plugin.isModified.get(block.getLocation()));
			plugin.isModified.remove(block.getLocation());
    	}
    }
    public boolean checkSensativeBlock(Block block)
    {
    	if (block.getType() != Material.CHEST && block.getType() != Material.SIGN_POST && block.getType() != Material.WORKBENCH && block.getType() != Material.FURNACE && block.getType() != Material.BURNING_FURNACE && block.getType() != Material.WOODEN_DOOR && block.getType() != Material.LADDER && block.getType() != Material.WALL_SIGN && block.getType() != Material.IRON_DOOR_BLOCK && block.getType() != Material.SAPLING && block.getType() != Material.REDSTONE_TORCH_OFF && block.getType() != Material.REDSTONE_TORCH_ON && block.getType() != Material.MOB_SPAWNER && block.getType() != Material.DIODE_BLOCK_OFF && block.getType() != Material.DIODE_BLOCK_ON && block.getType() != Material.PAINTING && block.getType() != Material.WOOL && block.getType() != Material.SPONGE && block.getType() != Material.REDSTONE_WIRE && block.getType() != Material.LEVER && block.getType() != Material.STONE_PLATE && block.getType() != Material.WOOD_PLATE && block.getType() != Material.STONE_BUTTON && block.getType() != Material.RAILS && block.getType() != Material.TORCH && block.getType() != Material.FENCE && block.getType() != Material.WOOD_STAIRS && block.getType() != Material.COBBLESTONE_STAIRS && block.getType() != Material.WATER && block.getType() != Material.LAVA && block.getType() != Material.STATIONARY_WATER && block.getType() != Material.STATIONARY_LAVA)
        {
    		return true;
        }else return false;
    }
  
    private class RevertBlockCircleTimerTask extends TimerTask
    {
    	Block block;
    	int size;
		public RevertBlockCircleTimerTask(Block b, int count)
		{
			block = b;
			size = count;
			
		}
		public void run() {
			System.out.println("Reverting Circle");
			restoreBlockCircle(block, size);
			
		}
    	
    }
    
    private class RevertBlockRingTimerTask extends TimerTask
    {
    	Block block;
    	int size;
		public RevertBlockRingTimerTask(Block b, int count)
		{
			block = b;
			size = count;
			
		}
		public void run() {
			System.out.println("Reverting Ring");
			restoreBlockRing(block, size);

			
		}
    	
    }
    
    private class ResetPlayerCooldownTimerTask extends TimerTask
    {
    	Player player;
    	
    	public ResetPlayerCooldownTimerTask(Player name)
    	{
    		player = name;
    		
    	}
    	
    	public void run()
    	{
    		plugin.canThrow.put(player.getName(), true);
    		player.sendMessage(ChatColor.YELLOW + "Your magic is ready!");
    	}
    }
}
