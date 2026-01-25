package net.blueva.arcade.modules.minefield.listener;

import net.blueva.arcade.api.game.GameContext;
import net.blueva.arcade.api.game.GamePhase;
import net.blueva.arcade.modules.minefield.game.MinefieldGameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class MinefieldListener implements Listener {

    private final MinefieldGameManager gameManager;

    public MinefieldListener(MinefieldGameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        GameContext<Player, Location, World, Material, ItemStack, Sound, Block, Entity> context = gameManager.getGameContext(player);
        if (context == null) {
            return;
        }

        if (!context.isPlayerPlaying(player)) {
            return;
        }

        if (to == null || (from.getBlockX() == to.getBlockX() &&
                from.getBlockY() == to.getBlockY() &&
                from.getBlockZ() == to.getBlockZ())) {
            return;
        }

        if (context.getPhase() == GamePhase.COUNTDOWN) {
            event.getPlayer().teleport(event.getFrom());
            return;
        }

        if (!context.isInsideBounds(to)) {
            gameManager.handlePlayerOutOfBounds(context, player, false);
            return;
        }

        Location blockBelow = to.clone().subtract(0, 1, 0);
        Material blockBelowType = blockBelow.getBlock().getType();

        Material deathBlock = getDeathBlock(context);
        if (blockBelowType == deathBlock) {
            gameManager.handlePlayerOutOfBounds(context, player, true);
            return;
        }

        if (isInsideFinishLine(context, to)) {
            gameManager.handleFinishLineCross(context, player);
        }
    }

    @EventHandler
    public void onMineTrigger(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block == null) {
            return;
        }

        GameContext<Player, Location, World, Material, ItemStack, Sound, Block, Entity> context = gameManager.getGameContext(player);
        if (context == null || !context.isPlayerPlaying(player)) {
            return;
        }

        if (context.getPhase() != GamePhase.PLAYING) {
            return;
        }

        if (!gameManager.isMineMaterial(block.getType())) {
            return;
        }

        event.setCancelled(true);
        block.setType(Material.AIR);

        gameManager.removeMineLocation(context.getArenaId(), block.getLocation());
        gameManager.handleMineTrigger(player, context, block.getLocation());
    }

    private Material getDeathBlock(GameContext<Player, Location, World, Material, ItemStack, Sound, Block, Entity> context) {
        try {
            String deathBlockName = context.getDataAccess().getGameData("basic.death_block", String.class);
            if (deathBlockName != null) {
                return Material.valueOf(deathBlockName.toUpperCase());
            }
        } catch (Exception e) {
            // Fallback
        }
        return Material.BARRIER;
    }

    private boolean isInsideFinishLine(GameContext<Player, Location, World, Material, ItemStack, Sound, Block, Entity> context,
                                       Location location) {
        try {
            Location finishMin = context.getDataAccess().getGameLocation("game.finish_line.bounds.min");
            Location finishMax = context.getDataAccess().getGameLocation("game.finish_line.bounds.max");

            if (finishMin == null || finishMax == null) {
                return false;
            }

            return location.getX() >= Math.min(finishMin.getX(), finishMax.getX()) &&
                    location.getX() <= Math.max(finishMin.getX(), finishMax.getX()) &&
                    location.getY() >= Math.min(finishMin.getY(), finishMax.getY()) &&
                    location.getY() <= Math.max(finishMin.getY(), finishMax.getY()) &&
                    location.getZ() >= Math.min(finishMin.getZ(), finishMax.getZ()) &&
                    location.getZ() <= Math.max(finishMin.getZ(), finishMax.getZ());

        } catch (Exception e) {
            return false;
        }
    }
}
