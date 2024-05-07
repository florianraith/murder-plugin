package com.florianraith.murder.state;

import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.PlayerRole;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GamePhase extends WorldPhase {

    @Inject private MurderPlugin plugin;
    @Inject private World world;

    private final Map<Player, PlayerRole> roles = new HashMap<>();

    @Override
    public void onEnable() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        assignMurderer(players.removeFirst());
        players.forEach(this::assignBystander);
    }

    @Override
    public void onDisable() {
        roles.clear();
    }

    @Override
    public void onJoin(Player player) {
        player.teleport(world.getSpawnLocation());
        assignSpectator(player);
    }

    @Override
    public void onQuit(Player player) {
        roles.remove(player);

        List<Player> murderers = getPlayers(PlayerRole.MURDERER);
        List<Player> bystanders = getPlayers(PlayerRole.BYSTANDER);

        if (murderers.isEmpty()) {
            Bukkit.broadcastMessage("The murderer has left the game.");
            Bukkit.broadcastMessage("");
            plugin.switchWorldState(EndPhase.class);
        }

        if (bystanders.isEmpty()) {
            Bukkit.broadcastMessage("The last bystander has left the game.");
            Bukkit.broadcastMessage("");
            plugin.switchWorldState(EndPhase.class);
        }
    }

    private List<Player> getPlayers(PlayerRole role) {
        return roles.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == role)
                .map(Map.Entry::getKey)
                .toList();
    }

    private void assignMurderer(Player player) {
        roles.put(player, PlayerRole.MURDERER);
        player.sendMessage("");
        player.sendMessage("You are the murderer.");
        player.sendMessage("Try to secretly kill all players.");
        player.sendMessage("");

        player.getInventory().addItem(new ItemStack(Material.GOLDEN_SWORD));
    }

    private void assignBystander(Player player) {
        roles.put(player, PlayerRole.BYSTANDER);
        player.sendMessage("");
        player.sendMessage("You are a bystander.");
        player.sendMessage("There is a murderer on the lose. Survive!");
        player.sendMessage("");
    }

    private void assignSpectator(Player player) {
        roles.put(player, PlayerRole.SPECTATOR);
        player.sendMessage("");
        player.sendMessage("You joined an ongoing game.");
        player.sendMessage("");

        player.setGameMode(GameMode.SPECTATOR);
    }

}
