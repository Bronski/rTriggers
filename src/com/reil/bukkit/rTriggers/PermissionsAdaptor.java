package com.reil.bukkit.rTriggers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsAdaptor implements Listener {
	Object permPlugin;
	rTriggers plugin;
	PermissionType pluginType = PermissionType.NONE;
	enum PermissionType {
		NONE,
		PEX,
		NIJIKOKUN;
	}
	
	public PermissionsAdaptor(rTriggers plugin){
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
			plugin.log.info("[rTriggers] Attached to PermissionsEx.");
			permPlugin = PermissionsEx.getPermissionManager();
			pluginType = PermissionType.PEX;
		} else if (plugin.pluginManager.getPlugin("Permissions") != null){
			permPlugin = Permissions.Security;
			pluginType = PermissionType.NIJIKOKUN;
			plugin.log.info("[rTriggers] Attached to Permissions.");
		}
	}
	
	public List<String> getGroups(Player player){
		LinkedList<String> returnMe = new LinkedList<String>();
		switch (pluginType) {
		case PEX:
			PermissionUser check = ((PermissionManager) permPlugin).getUser(player.getName());
			for(PermissionGroup group : check.getGroups()){
				returnMe.add(group.getName());
			}
			return returnMe;
		case NIJIKOKUN:
			return Arrays.asList(((PermissionHandler) permPlugin).getGroups(player.getWorld().getName(),player.getName()));
		default:
			return returnMe;
		}
	}
	
	public boolean isInGroup(Player player, String Group) {
		if (permPlugin == null) return false;
		if (pluginType == PermissionType.PEX){
			PermissionUser check = ((PermissionManager) permPlugin).getUser(player.getName());
			return check.inGroup(Group, player.getWorld().getName(), false);
		} else if (pluginType == PermissionType.NIJIKOKUN){
			return ((PermissionHandler) permPlugin).inSingleGroup(player.getWorld().getName(), player.getName(), Group);
		}
		return false;
	}
	
	public boolean hasPermission(Player player, String Perm){
		switch (pluginType){
		case PEX:
			PermissionUser check = ((PermissionManager) permPlugin).getUser(player.getName());
			return check.has(Perm);
		case NIJIKOKUN:
			return ((PermissionHandler) permPlugin).has(player, Perm);
		default:
			return player.hasPermission(Perm);
		}
	}
}
