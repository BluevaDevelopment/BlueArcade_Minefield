package net.blueva.arcade.modules.minefield.support;

import net.blueva.arcade.api.module.ModuleInfo;
import net.blueva.arcade.api.stats.StatDefinition;
import net.blueva.arcade.api.stats.StatScope;
import net.blueva.arcade.api.stats.StatsAPI;
import org.bukkit.entity.Player;

import java.util.Collection;

public class MinefieldStatsService {

    private final StatsAPI statsAPI;
    private final ModuleInfo moduleInfo;

    public MinefieldStatsService(StatsAPI statsAPI, ModuleInfo moduleInfo) {
        this.statsAPI = statsAPI;
        this.moduleInfo = moduleInfo;
    }

    public void registerStats() {
        if (statsAPI == null) {
            return;
        }

        statsAPI.registerModuleStat(moduleInfo.getId(),
                new StatDefinition("wins", "Wins", "Minefield wins", StatScope.MODULE));
        statsAPI.registerModuleStat(moduleInfo.getId(),
                new StatDefinition("games_played", "Games Played", "Minefield games played", StatScope.MODULE));
        statsAPI.registerModuleStat(moduleInfo.getId(),
                new StatDefinition("finish_line_crosses", "Finish line crosses", "Times you reached the safe zone", StatScope.MODULE));
        statsAPI.registerModuleStat(moduleInfo.getId(),
                new StatDefinition("mines_triggered", "Mines triggered", "Mines you have stepped on", StatScope.MODULE));
    }

    public boolean isEnabled() {
        return statsAPI != null;
    }

    public void recordGamesPlayed(Collection<Player> players) {
        if (statsAPI == null) {
            return;
        }

        for (Player player : players) {
            statsAPI.addModuleStat(player, moduleInfo.getId(), "games_played", 1);
        }
    }

    public void recordWin(Player player) {
        if (statsAPI == null) {
            return;
        }

        statsAPI.addModuleStat(player, moduleInfo.getId(), "wins", 1);
        statsAPI.addGlobalStat(player, "wins", 1);
    }

    public void recordFinishLine(Player player) {
        if (statsAPI == null) {
            return;
        }

        statsAPI.addModuleStat(player, moduleInfo.getId(), "finish_line_crosses", 1);
    }

    public void recordMineTriggered(Player player) {
        if (statsAPI == null) {
            return;
        }

        statsAPI.addModuleStat(player, moduleInfo.getId(), "mines_triggered", 1);
    }
}
