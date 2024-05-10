package com.florianraith.murder;

import dev.iiahmed.disguise.Disguise;
import dev.iiahmed.disguise.DisguiseManager;
import org.bukkit.entity.Player;

import java.util.*;

public class Disguises {

    private static final String[] NAMES = {
            "Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf",
            "Hotel", "India", "Juliett", "Kilo", "Lima", "Mike", "November",
            "Oscar", "Papa", "Quebec", "Romeo", "Sierra", "Tango", "Uniform",
            "Victor", "Whiskey", "Xray", "Yankee", "Zulu"
    };

    private static final Texture[] SKINS = {
            new Texture("ewogICJ0aW1lc3RhbXAiIDogMTcxNTM2NDMyNjQ1NCwKICAicHJvZmlsZUlkIiA6ICI5OTFhMWEyYjZkZWQ0MmU0YWVlMDZlNTA4ZjZjYzY2ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCcmFpbm1peCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82ZTE0ZmM2ODYwNTllZDE1MmRiZmU1ODVhOWMyZTljMTllNjdkMGZjNjNmZDYzYzQ0N2Y5NThjZDI4NmRkMmJhIgogICAgfQogIH0KfQ==", "GITiVylmY0Flv7NEAIncUymgwFKDdLZ2/BnYjWUkKuM6XgKtksMmZ4j2WQmAJC8XONFee1AE3TawVIAUc5xk46OwMGbF8QNMncR+oI+6QfCtbKbhW1fV2gDPiuKoTts8zuM7VMydteUQcQvGYgCqEIz4n6AyZIGmgl41TM18UJg71fRUhsDXNrJEvt5ysrShjCK3m9WyIqfee3jRk9UAoL2940WOWkKBHSzNEGkf9j1erZw1AvFU1yfYkPD7hNAykWxD8VfGILAFWq9IOlFWnPu/jvOLbRhZ8pERVz47J//gtG1Z3+Ne1030sFMHWFl0P79lSH6JnB0YR49GrKEcReSCa5VNaNb32TidjaSEBhfa35xrSxTMaqbgP2b39C96vel0Uh+0udXue3n2/DqP9zjiYAVF7v/nugjMJejOKZ2qpmeCJvsz340aygk2TFx9h94t4/LX+Zi/XAS/Wt0glgmo9JEutZaQFcwmveiye13Z7j6eCQavR2dygfUEKcFdqtbXKh/bRxfSLsc90FxlibD26kHlBArgtfZxFGKqNM6dXYxvvmht3Vh4Omn+x7co2UW3uPaOAqnq7qWSC05SGA3gPjAjC/sqinmSRd7s53J4dxR/EMLjvyKnjkYHU9W9AIE2Re7gtUFLRyczGYL/Fki1Rk0bBx1L77ech68MdgY="),
            new Texture("ewogICJ0aW1lc3RhbXAiIDogMTcxNTM2NDU2NzE1OSwKICAicHJvZmlsZUlkIiA6ICJmYTFmYmIzMTAwMjQ0MDA0ODM0NTdlZDE1NjM2ODhkOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaWNldmljZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xNWJmYjBjYjc0NjM5MDk5NTFmNjNlZjE0Y2EzNGFmZDU5NTgyODZkMjE1NWNjMzdhZGY4YWNjMzk3MTcyMjc2IgogICAgfQogIH0KfQ==", "jF4aL5rd5TsEcDxmKaZCQM6gESJV7qe+mG2pkYmqsZqG8OjdF7hqYQCPaMoT6oQJ8tmVYILjRtDbk87iwrbjOrLrKqye6hRQD1YOP8CWWsZ1VVaV6VAi50RHSSWTGF8JwFulHX1gGLne8LjOE5xkwHewnkyvkKuWggI8qffkAUZVMXz/c8YoAzbIVx4icTOY5z3sIdntkrHGOY6k/e3UZ5Pn1uUOW5tvq5HkPawZzrVDlGWF0L2vzJ3VJpD4Hl1SuSroZ2jUXKs3L4BbHDvTXbE3ciENTXh1XxzMKbnk7wgGW64HjXBpd/4tk1iyh3jHkNYn6pdOeT5bOmNfl5WV+jiTW2p0s9MFFAutE8VB9v4a49LPQZFZyMjUblKKAqum2dp7kj3/ovzF080j90reu4fVcxLeiCESfzuINNLaWT9d4H0o7kQhAq2grAmHW0gE6Kh9nihfYN4I9h4wgAz/qyTBPHeD8HHarReStzAXy0PgyQyzXVozJ3vCM0nq5xXD6Lmp0TqUxSbjubwXJLwu5fhbxwmtdprtC6C9O2rQnaGqnSCNrOsxo2G632kDE/ZjZjWMgQCMNOHpst1fDI+0+g1opsgctH9KNF3guDHz6atMUTIJb1IUWXmoushgrfbL5HySuDmpMF9VGAlDuj2Ou3gW/SvXphs3aSs5j12pXfw="),
            new Texture("ewogICJ0aW1lc3RhbXAiIDogMTcxNTM2NDY0NzczMCwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==", "eA+g4sdrgziDV1mhksOgcETDf0V9ijwsofPYNIWEpDoRdicIBXlGJBmeCvQxTXUmAXe49R7rm4nDfosEH92usRMs4IfU86+JEorxPHH+YT4DjtflWrz3nHxa53h2hsScMZx19h/WhKJ/lr61/u4grPdgcIUsCHTflYTGd1WEp0A3t9HccPO4AUqCuqbzeGCbj09QLa98tLJD112osO6zMABks6YPfbDvYf+bFOSMTPicAYRrYBwXCc04a55jevxJGLJXuJ2YAfJ4bOMh7sOVY/cpbbPxdhvCvEP0CD7CTyJHnePNwHlIvs82b7UPK8gFwuME0LkiFeEcIRO+J+I8nLLdvEqA5mV4LMin1lXRx8a4MejV2JckWUzrdHPu9bOe7oMW0iQAJmvuaEkSXb5XZ64Zx+hAm9dk3JYnc20g3KnXtgs9oMtXxjLP+Bvt6qA9EJaGW+rCtoibyHcorMINpZMvVPSg6TSHtk86OSEcHFUO8Dx51yggBgPwDFmUakkG85M2GWAss80dWLzPGCWQYDRGKbApXAjEG+4aPaU9AhGzJio7NcLthLFQb7i/6qgf7AUaPeCFwqf11RKgoTMplymrVcZp3zwRUDwfv0MpSEVOCUFxnOpUBE1K2HgMyTLKPshXjHINWEJsCZxT6s9spq/fTB3P4QRYXOuZWVsJYog="),
            new Texture("ewogICJ0aW1lc3RhbXAiIDogMTcxNTM2NDc3NDU5MSwKICAicHJvZmlsZUlkIiA6ICI1MjRlOGFkNTdkZjI0ZGU3YjlmNGFlODlkOWExOTU5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb2FhYWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTIzNTZjM2U0ZGQ4MjA0MzAyZWY2YjY0ZGQ3ZjBmY2Q1ODNjZmE0NmVhNWNkM2FlZDA0YmQxNmI5ZmYxZDY3NiIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjYzA4ODQwNzAwNDQ3MzIyZDk1M2EwMmI5NjVmMWQ2NWExM2E2MDNiZjY0YjE3YzgwM2MyMTQ0NmZlMTYzNSIKICAgIH0KICB9Cn0=", "LSrlNyw+YDIZ2k+MssdzbN22keCMv103WU2BISiAyhUZeP0oZ7JLz0bUZbF/1MGvChjultkjs+JFLb8JuKColcpyxlcviyy7N+cqcUd661vV/QNu6OqInyI/PuMqvjuNC1Qpnt8QpcTaitDm+qok8S9aIPTZLpmHCl70rcDY6SC+oxh0l8Ycd8JoYqDgRHpICE9dwLRUrSxcVv67JlzN8FmGfPBvMLz2+y5TiQnXG5oS84t1zpVr+6Upueg6O6jjQw/TyNyFaxh/dhLnzfbTPNJTCUjQxmkVBStEmck95AwmgRiMz2B15oPp7jKUu3Py6e03wMMNCc54f4OFFcV04d5RAopSXJlROQwKrZImt+6p5/Myk994AD/rj0L3EiUbY9nac4cOIkBZ9eo+5sUahcyTHdj7mrvV9ThDLGA7Z/LjUeaMc1lxmX0w07ETz/yhkzXO3Qf83oLvqxiU17BhK3XNPE2IP6wTuFmTw1q/lKKQAvV3pJP9dKqH4fzFopW1xA1u/EiW+8970oZ3DACwOtZdZ7uXNWOxqh/pf6SrjQPqHaEClzAUzj7g2csrIRojClVjF3BfcaA1EB8DPUYORuCULYCn8HuwwmV1qZLWbLm57ocIlsR1whLRM1XNYCXwzP4AahwB+6nkIpUY9US4002wJJcUp4pcfI/gXjnIeQ0="),
            new Texture("ewogICJ0aW1lc3RhbXAiIDogMTcxNTM2NDg1NjEyMywKICAicHJvZmlsZUlkIiA6ICJlOTAxM2MyZmRhMDE0MjVmYTQ4YjUxNmY1NWU5NDM4NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJHb21tZUhEIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y2OTMyY2VjOTRjZjA5NGZmODM2YmU0ZjViMjQzMWJlNWRhMDE2YmE4OWZkMmFjM2FhMjJlOTNhZTRmZTU3NGUiCiAgICB9CiAgfQp9", "CTROHKrYbPCtsv3QrubmMYAOcjCrZxeag7Uqs/DQEAICtA5M76bzVG3g1vHRlkB0oclQboUpVapstprUj4QxYIe8Gjz/8gsEG/vGq5lKRFTKV3Ko/5cAsO/kNuoPndzNPISfLdVMnKmO/y7YAgg2LsXJmQAMYi52Rz9AR2/WI4wSMbPcO/tCVwUX19mAf81/mRUOzFDSp/n4bkYUtiQOgGEcfDIL/vc97De+a/0EhklY+B6+qVxv+tc3FLyHovYY2x8jVwfv3oIThNyomFzNcIY0wugX4RdaARDgCJa3gP4IuudVLVzUZYItS8QZHea3BFPImTYdhtxdnzKDwa0CIbNSfSydh8KqLM/6hHvOnOirbSlC6vTIq8YKn3M9NB78hBZC6c4nb49Vuh0XOnQY3D46Z6t6G9FVnoo+Xe5wNy+nDGMRL0P8hPYbG710ylwrshVDvYZcQuX1jAx4rjjCXPt337u1ZCrIZld+OCzYXV4hcBo1C9CYTO1IgpjFs0RerhJngF9Ucj3XgRNiQN4jmNxAz1Gmtbq4dfA36npWWYDVfsih+cWfPTHhRTO+Iy+VTWELGOXRjduZsL2SSrAQmrzL9WqKwbZZM9nzFuus8mWuafk21CE6sPVQnIpnv66iyXwSfsklIocEuL9M+XmT90yo1BKjfu6xvtCYqAsQsKE=")
    };

    private final List<String> unusedNames = getShuffledNames();
    private final Map<Player, String> disguises = new HashMap<>();

    public static Disguises create(Collection<? extends Player> players) {
        Disguises disguises = new Disguises();
        players.forEach(disguises::disguise);
        return disguises;
    }

    private static List<String> getShuffledNames() {
        List<String> names = new ArrayList<>(List.of(NAMES));
        Collections.shuffle(names);
        return names;
    }

    private static Texture getRandomSkin() {
        return SKINS[new Random().nextInt(SKINS.length)];
    }

    public void disguise(Player player) {
        if (unusedNames.isEmpty()) {
            throw new IllegalStateException("There are not enough names to disguise all players");
        }

        String name = unusedNames.removeFirst();
        Texture skin = getRandomSkin();

        Disguise disguise = Disguise.builder()
                .setName(name)
                .setSkin(skin.value, skin.signature)
                .build();

        DisguiseManager.getProvider().disguise(player, disguise);
        disguises.put(player, name);
    }

    public String getDisguise(Player player) {
        return disguises.get(player);
    }

    public void undisguise(Player player) {
        DisguiseManager.getProvider().undisguise(player);
        String name = disguises.remove(player);
        unusedNames.add(name);
    }

    public void undisguiseAll() {
        disguises.keySet().forEach(DisguiseManager.getProvider()::undisguise);
        unusedNames.addAll(disguises.values());
        disguises.clear();
    }

    private record Texture(String value, String signature) {
    }

}
