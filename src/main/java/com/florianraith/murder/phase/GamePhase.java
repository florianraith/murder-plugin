package com.florianraith.murder.phase;

import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.PlayerRole;
import com.florianraith.murder.item.ItemManager;
import com.florianraith.murder.item.KnifeItem;
import com.florianraith.murder.util.Attributes;
import com.florianraith.murder.util.Worlds;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.*;

public class GamePhase implements WorldPhase {

    @Inject private MurderPlugin plugin;
    @Inject private World world;
    @Inject private ItemManager itemManager;

    private final Map<Player, PlayerRole> roles = new HashMap<>();

    @Override
    public void onEnable() {
        itemManager.register(KnifeItem.class);

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Worlds.ensurePlayersAreInWorld(players, world);
        Collections.shuffle(players);

        assignMurderer(players.removeFirst());
        players.forEach(this::assignBystander);
    }

    @Override
    public void onDisable() {
        itemManager.unregisterAll();
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
            plugin.setPhase(new EndPhase());
        }

        if (bystanders.isEmpty()) {
            Bukkit.broadcastMessage("The last bystander has left the game.");
            Bukkit.broadcastMessage("");
            plugin.setPhase(new EndPhase());
        }
    }

    public List<Player> getPlayers(PlayerRole role) {
        return roles.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == role)
                .map(Map.Entry::getKey)
                .toList();
    }

    public boolean hasRole(Player player, PlayerRole role) {
        return roles.get(player) == role;
    }

    public void assignMurderer(Player player) {
        roles.put(player, PlayerRole.MURDERER);
        player.sendMessage("");
        player.sendMessage("You are the murderer.");
        player.sendMessage("Try to secretly kill all players.");
        player.sendMessage("");

        player.getInventory().addItem(itemManager.get(KnifeItem.class));
    }

    public void assignBystander(Player player) {
        roles.put(player, PlayerRole.BYSTANDER);
        player.sendMessage("");
        player.sendMessage("You are a bystander.");
        player.sendMessage("There is a murderer on the lose. Survive!");
        player.sendMessage("");
    }

    public void assignSpectator(Player player) {
        roles.put(player, PlayerRole.SPECTATOR);
        player.sendMessage("");
        player.sendMessage("You joined an ongoing game.");
        player.sendMessage("");

        player.setGameMode(GameMode.SPECTATOR);
    }

    public void checkEnd() {
        List<Player> murderers = getPlayers(PlayerRole.MURDERER);
        List<Player> bystanders = getPlayers(PlayerRole.BYSTANDER);
        List<Player> spectators = getPlayers(PlayerRole.SPECTATOR);

        if (murderers.isEmpty()) {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("Congratulations!");
            Bukkit.broadcastMessage("All murderers are dead");
            Bukkit.broadcastMessage("");
            plugin.setPhase(new EndPhase(spectators));
            return;
        }

        if (bystanders.isEmpty()) {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("No bystander survived");
            Bukkit.broadcastMessage("The murderer has won the game");
            Bukkit.broadcastMessage("");
            plugin.setPhase(new EndPhase(spectators));
            return;
        }
    }

    @Override
    public void preparePlayer(Player player) {
        WorldPhase.super.preparePlayer(player);
        player.setHealth(Attributes.set(player, Attribute.GENERIC_MAX_HEALTH, 2));
    }
}
