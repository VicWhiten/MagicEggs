package com.bukkit.vicwhiten.magiceggs;

import java.util.HashMap;
import java.util.logging.Logger;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;



public class MagicEggs extends JavaPlugin
{
	public static final Logger log = Logger.getLogger("Minecraft");
	private final MagicEggsPlayerListener playerListener = new MagicEggsPlayerListener(this);
	GroupManager gm;
	WorldsHolder wd;
    public HashMap<Location, Material> isModified = new HashMap<Location, Material>();
    public HashMap<String, Boolean> canThrow = new HashMap<String, Boolean>();
	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName()+" version "+pdfFile.getVersion()+" is disabled!");
	}

	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		Plugin p = this.getServer().getPluginManager().getPlugin("GroupManager");
        if (p != null) {
            if (!this.getServer().getPluginManager().isPluginEnabled(p)) {
                this.getServer().getPluginManager().enablePlugin(p);
            }
            gm = (GroupManager) p;
            wd = gm.getWorldsHolder();
        } else {
            this.getPluginLoader().disablePlugin(this);
        }
		pm.registerEvent(Event.Type.PLAYER_EGG_THROW, this.playerListener, Event.Priority.Normal, this);

        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName()+" version "+pdfFile.getVersion()+" is enabled!");
	}
}