package net.blueva.arcade.modules.minefield.game;

import net.blueva.arcade.api.game.GameContext;
import net.blueva.arcade.modules.minefield.state.MinefieldArenaState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MinefieldArenaRegistry {

    private final Map<Integer, MinefieldArenaState> arenas = new ConcurrentHashMap<>();
    private final Map<Player, Integer> playerArenas = new ConcurrentHashMap<>();

    public MinefieldArenaState createArenaState(GameContext<Player, Location, World, Material, ItemStack, Sound, Block, Entity> context) {
        int arenaId = context.getArenaId();
        MinefieldArenaState state = new MinefieldArenaState(context);
        arenas.put(arenaId, state);

        for (Player player : context.getPlayers()) {
            playerArenas.put(player, arenaId);
        }

        return state;
    }

    public MinefieldArenaState getArenaState(int arenaId) {
        return arenas.get(arenaId);
    }

    public Integer getArenaId(Player player) {
        return playerArenas.get(player);
    }

    public Collection<MinefieldArenaState> getArenaStates() {
        return arenas.values();
    }

    public void removeArenaState(int arenaId) {
        arenas.remove(arenaId);
    }

    public void removePlayersForArena(int arenaId) {
        playerArenas.entrySet().removeIf(entry -> entry.getValue().equals(arenaId));
    }

    public void clear() {
        arenas.clear();
        playerArenas.clear();
    }
}
