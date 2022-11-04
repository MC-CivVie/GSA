package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.megabuilds.MegaBuild;
import me.zombie_striker.gsl.megabuilds.MegaBuildType;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.world.GSLWorld;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.LinkedList;
import java.util.List;

public class FactoryEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() != EquipmentSlot.OFF_HAND) {
            GSLWorld gslWorld = GSLWorld.getWorld(event.getClickedBlock().getWorld());
            for (MegaBuild megaBuild : gslWorld.getMegabuilds()) {
                int xoff =  megaBuild.getCenter().getBlockX() + megaBuild.getType().getOffsetX() -event.getClickedBlock().getX();
                int yoff =  megaBuild.getCenter().getBlockY() + megaBuild.getType().getOffsetY() -event.getClickedBlock().getY();
                int zoff =  megaBuild.getCenter().getBlockZ() + megaBuild.getType().getOffsetZ() -event.getClickedBlock().getZ();

                if(xoff<0||xoff>=megaBuild.getType().getActions().length)
                    continue;
                if(yoff<0||yoff>=megaBuild.getType().getActions()[xoff].length)
                    continue;
                if(zoff<0||zoff>=megaBuild.getType().getActions()[xoff][yoff].length)
                    continue;
                if (megaBuild.getType().getActions()[xoff][yoff][zoff] != null) {
                    megaBuild.getType().getActions()[xoff][yoff][zoff].onInteract(event.getPlayer(), event.getClickedBlock(), megaBuild, true);
                    event.setCancelled(true);
                }
                return;
            }
            for (MegaBuildType types : MegaBuildType.getBuildtypes()) {
                if (types.isValidStructure(event.getClickedBlock().getLocation())) {
                    event.setCancelled(true);
                    List<Location> l = new LinkedList<>();
                    for (int x = -types.getOffsetX(); x < types.getTypes().length - types.getOffsetX(); x++) {
                        for (int y = -types.getOffsetY(); y < types.getTypes()[x + types.getOffsetX()].length - types.getOffsetY(); y++) {
                            for (int z = -types.getOffsetZ(); z < types.getTypes()[x + types.getOffsetX()][y + types.getOffsetY()].length - types.getOffsetZ(); z++) {
                                l.add(event.getClickedBlock().getLocation().add(x, y, z));
                            }
                        }
                    }

                    MegaBuild megaBuild = new MegaBuild(types, event.getClickedBlock().getLocation(), l.toArray(new Location[l.size()]));
                    gslWorld.getMegabuilds().add(megaBuild);
                    event.getPlayer().sendMessage(new ComponentBuilder(megaBuild.getType().getDisplayname() + " created.", ComponentBuilder.GREEN).build());
                    return;
                }
            }
        }
    }
}
