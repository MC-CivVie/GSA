package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.snitches.*;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.world.GSLChunk;
import me.zombie_striker.gsl.world.GSLCube;
import me.zombie_striker.gsl.world.GSLWorld;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class SnitchEvents implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreakSnitch(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.JUKEBOX) {
            GSLWorld gslWorld = GSLWorld.getWorld(event.getBlock().getWorld());
            for (Snitch snitch : new ArrayList<>(gslWorld.getSnitches())) {
                if (snitch.getLocation().equals(event.getBlock().getLocation())) {
                    event.getPlayer().sendMessage(new ComponentBuilder("This snitch was owned by ",ComponentBuilder.GRAY).append(snitch.getNameLayer().getName(),ComponentBuilder.WHITE).build());
                    gslWorld.unregisterSnitch(snitch);
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlaceSnitch(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.JUKEBOX) {
            GSLChunk gslChunk = GSLChunk.getGSLChunk(event.getBlock().getChunk());
            GSLCube gslCube = gslChunk.getCubes()[(event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
            if (gslCube != null) {
                int x = event.getBlock().getX() % 16;
                if (event.getBlock().getX() < 0)
                    x = Math.abs((-event.getBlock().getX()) % 16 - 15);
                int z = event.getBlock().getZ() % 16;
                if (event.getBlock().getZ() < 0)
                    z = Math.abs((-event.getBlock().getZ()) % 16 - 15);

                int y = (event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET)%16;

                if (gslCube.getNamelayers()[x][y][z] != null) {
                    Snitch snitch = new Snitch(event.getBlock().getLocation(), Snitch.SNITCHLOG_SIZE);
                    GSLWorld.getWorld(event.getBlock().getWorld()).registerSnitch(snitch);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlaceAnything(BlockPlaceEvent event) {
        GSLWorld gslWorld = GSLWorld.getWorld(event.getBlockAgainst().getWorld());
        SnitchLogPlace placelog = new SnitchLogPlace(event.getBlock().getType(), System.currentTimeMillis(), event.getPlayer().getUniqueId(), event.getBlock().getLocation());
        for (Snitch snitch : gslWorld.getSnitches()) {
            if (snitch.canDetect(event.getBlock().getLocation(), event.getPlayer())) {
                snitch.broadcast(new ComponentBuilder(event.getPlayer().getName(),ComponentBuilder.WHITE)
                        .append(" placed a block at ",ComponentBuilder.BLUE)
                                .append(event.getBlock().getLocation().getBlockX()+", "+event.getBlock().getLocation().getBlockY()+", "+event.getBlock().getLocation().getBlockZ(),ComponentBuilder.WHITE)
                        .build());
                snitch.addToLog(placelog);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreakAnything(BlockPlaceEvent event) {
        GSLWorld gslWorld = GSLWorld.getWorld(event.getBlockAgainst().getWorld());
        SnitchLogBreak placelog = new SnitchLogBreak(event.getBlock().getType(), System.currentTimeMillis(), event.getPlayer().getUniqueId(), event.getBlock().getLocation());
        for (Snitch snitch : gslWorld.getSnitches()) {
            if (snitch.canDetect(event.getBlock().getLocation(), event.getPlayer())) {
                snitch.broadcast(new ComponentBuilder(event.getPlayer().getName(),ComponentBuilder.WHITE)
                        .append(" broke a block at ",ComponentBuilder.BLUE)
                        .append(event.getBlock().getLocation().getBlockX()+", "+event.getBlock().getLocation().getBlockY()+", "+event.getBlock().getLocation().getBlockZ(),ComponentBuilder.WHITE)
                        .build());
                snitch.addToLog(placelog);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        GSLWorld gslWorld = GSLWorld.getWorld(event.getPlayer().getWorld());
        for (Snitch snitch : gslWorld.getSnitches()) {
            if (snitch.canDetect(event.getFrom(), event.getPlayer()) && !snitch.canDetect(event.getTo(), event.getPlayer())) {
                SnitchLogUnauthorizedExit placelog = new SnitchLogUnauthorizedExit(Material.PLAYER_HEAD, System.currentTimeMillis(), event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
                snitch.addToLog(placelog);
                snitch.broadcast(new ComponentBuilder(event.getPlayer().getName(),ComponentBuilder.WHITE)
                        .append(" entered snitch at ",ComponentBuilder.BLUE)
                        .append(event.getTo().getBlockX()+", "+event.getTo().getBlockY()+", "+event.getTo().getBlockZ(),ComponentBuilder.WHITE)
                        .build());
            } else if (!snitch.canDetect(event.getFrom(), event.getPlayer()) && snitch.canDetect(event.getTo(), event.getPlayer())) {
                SnitchLogUnauthorizedEnter placelog = new SnitchLogUnauthorizedEnter(Material.PLAYER_HEAD, System.currentTimeMillis(), event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
                snitch.addToLog(placelog);
                snitch.broadcast(new ComponentBuilder(event.getPlayer().getName(),ComponentBuilder.WHITE)
                        .append(" exited snitch at ",ComponentBuilder.BLUE)
                        .append(event.getTo().getBlockX()+", "+event.getTo().getBlockY()+", "+event.getTo().getBlockZ(),ComponentBuilder.WHITE)
                        .build());
            }
        }
    }
}
