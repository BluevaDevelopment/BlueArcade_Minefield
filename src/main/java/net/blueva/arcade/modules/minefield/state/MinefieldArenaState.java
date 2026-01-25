package net.blueva.arcade.modules.minefield.state;

import net.blueva.arcade.api.game.GameContext;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MinefieldArenaState {

    private final GameContext<org.bukkit.entity.Player, Location, World, Material, ItemStack, Sound, Block, Entity> context;
    private final Set<Location> mines = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private volatile boolean ended;
    private volatile UUID winner;
    private volatile String timerTaskId;

    public MinefieldArenaState(GameContext<org.bukkit.entity.Player, Location, World, Material, ItemStack, Sound, Block, Entity> context) {
        this.context = context;
    }

    public GameContext<org.bukkit.entity.Player, Location, World, Material, ItemStack, Sound, Block, Entity> getContext() {
        return context;
    }

    public Set<Location> getMines() {
        return mines;
    }

    public boolean isEnded() {
        return ended;
    }

    public boolean markEnded() {
        if (ended) {
            return false;
        }
        ended = true;
        return true;
    }

    public UUID getWinner() {
        return winner;
    }

    public void setWinner(UUID winner) {
        this.winner = winner;
    }

    public String getTimerTaskId() {
        return timerTaskId;
    }

    public void setTimerTaskId(String timerTaskId) {
        this.timerTaskId = timerTaskId;
    }
}
