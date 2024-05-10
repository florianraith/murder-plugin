package com.florianraith.murder.phase;

import com.florianraith.murder.BossBars;
import com.florianraith.murder.Disguises;
import com.florianraith.murder.config.Messages;
import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.PlayerRole;
import com.florianraith.murder.item.ItemManager;
import com.florianraith.murder.item.KnifeItem;
import com.florianraith.murder.util.Attributes;
import com.florianraith.murder.util.Worlds;
import com.google.inject.Inject;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
    @Inject private Messages messages;
    @Inject private BossBars bossBars;

    private final Map<Player, PlayerRole> roles = new HashMap<>();
    private Disguises disguises;

    @Override
    public void onEnable(WorldPhase previous) {
        itemManager.register(KnifeItem.class);

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Worlds.ensurePlayersAreInWorld(players, world);
        Collections.shuffle(players);

        if (previous instanceof PreparingPhase preparing) {
            disguises = preparing.getDisguises();
        } else {
            disguises = Disguises.create(players);
        }

        assignMurderer(players.removeFirst());
        players.forEach(this::assignBystander);

    }

    @Override
    public void onDisable(WorldPhase next) {
        bossBars.clearAll();
        itemManager.unregisterAll();
        roles.clear();
        disguises.undisguiseAll();
    }

    @Override
    public void onJoin(Player player) {
        player.teleport(world.getSpawnLocation());
        assignSpectator(player);
        player.sendMessage(messages.prefixed("game.role.spectator"));
    }

    @Override
    public void onQuit(Player player) {
        roles.remove(player);

        List<Player> murderers = getPlayers(PlayerRole.MURDERER);
        List<Player> bystanders = getPlayers(PlayerRole.BYSTANDER);

        if (murderers.isEmpty()) {
            Bukkit.broadcast(messages.prefixed("game.left.murderer"));
            plugin.setPhase(new EndPhase());
        }

        if (bystanders.isEmpty()) {
            Bukkit.broadcast(messages.prefixed("game.left.last_bystander"));
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
        player.sendMessage(messages.prefixed("game.role.murderer"));
        player.getInventory().addItem(itemManager.get(KnifeItem.class));

        bossBars.show(player, bossBars.create(messages.get("roles.murderer"), BossBar.Color.RED));
    }

    public void assignBystander(Player player) {
        roles.put(player, PlayerRole.BYSTANDER);
        player.sendMessage(messages.prefixed("game.role.bystander"));

        bossBars.show(player, bossBars.create(messages.get("roles.bystander"), BossBar.Color.YELLOW));
    }

    public void assignSpectator(Player player) {
        roles.put(player, PlayerRole.SPECTATOR);
        player.setGameMode(GameMode.SPECTATOR);

        bossBars.show(player, bossBars.create(messages.get("roles.spectator"), BossBar.Color.WHITE));
    }

    public void killPlayer(Player player, Player killer) {
        disguises.undisguise(player);
        assignSpectator(player);
        player.sendMessage(messages.prefixed("game.killed", Placeholder.unparsed("killer", killer.getName())));
    }

    public void checkEnd() {
        List<Player> murderers = getPlayers(PlayerRole.MURDERER);
        List<Player> bystanders = getPlayers(PlayerRole.BYSTANDER);
        List<Player> spectators = getPlayers(PlayerRole.SPECTATOR);

        if (murderers.isEmpty()) {
            Bukkit.broadcast(messages.prefixed("game.win.murderer"));
            plugin.setPhase(new EndPhase(spectators));
            return;
        }

        if (bystanders.isEmpty()) {
            Bukkit.broadcast(messages.prefixed("game.win.bystander"));
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
