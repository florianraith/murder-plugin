package com.florianraith.murder;

import com.comphenix.packetwrapper.wrappers.ResourceKey;
import com.comphenix.packetwrapper.wrappers.WrapperPlayServerPlayerInfo;
import com.comphenix.packetwrapper.wrappers.WrapperPlayServerPlayerInfoRemove;
import com.comphenix.packetwrapper.wrappers.WrapperPlayServerRespawn;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.*;
import com.florianraith.murder.config.Messages;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class DisguiseManager {

    @Inject private ProtocolManager protocolManager;
    @Inject private MurderPlugin plugin;
    @Inject private Messages messages;

    private PacketAdapter adapter;

    private final Map<UUID, PlayerInfoData> originalData = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerInfoData> fakedData = new ConcurrentHashMap<>();
    private final Map<UUID, String> requests = new ConcurrentHashMap<>();

    public void enable() {
        adapter = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO) {

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                Set<EnumWrappers.PlayerInfoAction> actions = packet.getPlayerInfoActions().read(0);
                PlayerInfoData playerData = packet.getPlayerInfoDataLists().read(1).getFirst();

                if (!actions.contains(EnumWrappers.PlayerInfoAction.ADD_PLAYER) || !actions.contains(EnumWrappers.PlayerInfoAction.INITIALIZE_CHAT)) {
                    return;
                }

                UUID playerUUID = playerData.getProfile().getUUID();

                if (!originalData.containsKey(playerUUID)) {
                    originalData.put(playerUUID, playerData);
                }

                if (requests.containsKey(playerUUID)) {
                    String name = requests.remove(playerUUID);
                    fakedData.put(playerUUID, fakePlayerData(playerData, name));
                }

                if (fakedData.containsKey(playerUUID)) {
                    String fakeName = fakedData.get(playerUUID).getProfile().getName();
                    PlayerInfoData fakeData = fakePlayerData(playerData, fakeName);
                    packet.getPlayerInfoDataLists().write(1, List.of(fakeData));
                }

            }
        };

        protocolManager.addPacketListener(adapter);

        protocolManager.addPacketListener(new PacketAdapter(
                plugin,
                WrapperPlayServerRespawn.TYPE
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == WrapperPlayServerPlayerInfo.TYPE) {
                    WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo(event.getPacket());
                    System.out.println("to " + event.getPlayer().getName() + ": " + packet);
                }
            }
        });
    }

    public void requestDisguise(Player player, String name) {
        if (originalData.containsKey(player.getUniqueId())) {
            disguise(player, name);
        } else {
            requests.put(player.getUniqueId(), name);
        }
    }

    private void disguise(Player player, String name) {
        PlayerInfoData original = originalData.get(player.getUniqueId());
        PlayerInfoData fake = fakePlayerData(original, name);

        sendPackets(player, fake);
        fakedData.put(player.getUniqueId(), fake);
    }

    public void reset(Player player) {
        fakedData.remove(player.getUniqueId());
        PlayerInfoData original = originalData.get(player.getUniqueId());
        sendPackets(player, original);
    }

    public void resetAll() {
        System.out.println(fakedData.keySet());
        fakedData.keySet().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(this::reset);

        fakedData.clear();
    }

    private PlayerInfoData fakePlayerData(PlayerInfoData original, String name) {
        WrappedGameProfile profile = original.getProfile().withName(name);
        return new PlayerInfoData(
                original.getProfileId(),
                original.getLatency(),
                original.isListed(),
                original.getGameMode(),
                profile,
                null,
                original.getRemoteChatSessionData()
        );
    }

    private void sendPackets(Player target, PlayerInfoData data) {
        Location location = target.getLocation();
        boolean flying = target.isFlying();

        WrapperPlayServerPlayerInfoRemove removePacket = new WrapperPlayServerPlayerInfoRemove();
        removePacket.setProfileIds(List.of(data.getProfileId()));

        PacketContainer respawnPacket = new PacketContainer(PacketType.Play.Server.RESPAWN);
        InternalStructure playerSpawnInfo = respawnPacket.getStructures().read(0);
        playerSpawnInfo.getModifier().withType(MinecraftReflection.getResourceKey(), ResourceKey.CONVERTER).write(0, new ResourceKey(new MinecraftKey("minecraft", "dimension_type"), new MinecraftKey("minecraft", "overworld")));
        playerSpawnInfo.getWorldKeys().write(0, target.getWorld());
        playerSpawnInfo.getLongs().write(0, Hashing.sha256().hashLong(target.getWorld().getSeed()).asLong());
        playerSpawnInfo.getGameModes().write(0, EnumWrappers.NativeGameMode.fromBukkit(target.getGameMode()));
        playerSpawnInfo.getGameModes().write(1, EnumWrappers.NativeGameMode.fromBukkit(target.getGameMode()));
        playerSpawnInfo.getBooleans().write(0, false);
        playerSpawnInfo.getBooleans().write(1, false);
        playerSpawnInfo.getOptionals(Converters.passthrough(Objects.class)).write(0, Optional.empty());
        playerSpawnInfo.getIntegers().write(0, 0);
        respawnPacket.getStructures().write(0, playerSpawnInfo);
        respawnPacket.getBytes().write(0, (byte) 3);

        System.out.println("sending disguise packets to " + target.getName());

        protocolManager.sendServerPacket(target, removePacket.getHandle());
        protocolManager.sendServerPacket(target, respawnPacket);
        protocolManager.sendServerPacket(target, infoPacket(EnumWrappers.PlayerInfoAction.ADD_PLAYER, data));
        protocolManager.sendServerPacket(target, infoPacket(EnumWrappers.PlayerInfoAction.UPDATE_LISTED, data));

        target.updateInventory();
        target.teleport(location);
        target.setFlying(flying);


        for (Player player : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(plugin, target);
            player.showPlayer(plugin, target);
        }
    }

    private PacketContainer infoPacket(EnumWrappers.PlayerInfoAction action, PlayerInfoData data) {
        WrapperPlayServerPlayerInfo addPacket = new WrapperPlayServerPlayerInfo();
        addPacket.setActions(Set.of(action));
        addPacket.setEntries(List.of(data));
        return addPacket.getHandle();
    }

    public void disable() {
        protocolManager.removePacketListener(adapter);
        resetAll();
        originalData.clear();
        requests.clear();
    }

}
