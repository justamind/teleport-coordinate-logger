package packets.builder;

import game.Game;
import game.data.Coordinate3D;
import game.data.DoubleCoordiante3D;
import proxy.auth.NameRetriever;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientBoundGamePacketBuilder extends PacketBuilder {
    private HashMap<Integer, String> players = new HashMap<>();
    private HashMap<String, DoubleCoordiante3D> playerPositions = new HashMap<>();
    private ConcurrentHashMap<String, String> playerNames = new ConcurrentHashMap<>();

    private HashMap<String, PacketOperator> operations = new HashMap<>();
    public ClientBoundGamePacketBuilder() {
        PacketOperator updatePlayerPosition = provider -> {
            double x = provider.readDouble();
            double y = provider.readDouble();
            double z = provider.readDouble();

            Coordinate3D playerPos = new Coordinate3D(x, y, z);
            Game.setPlayerPosition(playerPos);

            return true;
        };

        operations.put("player_position_look", updatePlayerPosition);
        operations.put("player_vehicle_move", updatePlayerPosition);


        operations.put("spawn_player", (provider) -> {
            int entId = provider.readVarInt();
            String uuid = provider.readUUID();
            players.put(entId, uuid);
            new NameRetriever(playerNames, uuid).start();

            playerPositions.put(uuid, new DoubleCoordiante3D(provider.readDouble(), provider.readDouble(), provider.readDouble()));
            return true;
        });

        operations.put("entity_teleport", provider -> {
            int entId = provider.readVarInt();
            DoubleCoordiante3D dest = new DoubleCoordiante3D(provider.readDouble(), provider.readDouble(), provider.readDouble());
            if (players.containsKey(entId)) {
                DoubleCoordiante3D oldPosition = playerPositions.get(players.get(entId));
                playerPositions.put(players.get(entId), dest);

                // if the teleport distance is very short, don't bother printing it
                if (dest.isInRange(oldPosition, 32)) {
                    return true;
                }

                System.out.println("Player " + playerNames.get(players.get(entId)) + " moved to " + dest);
            }
            return true;
        });
        operations.put("entity_move", provider -> {
            int entId = provider.readVarInt();
            if (!players.containsKey(entId)) {
                 return true;
            }

            DoubleCoordiante3D pos = playerPositions.getOrDefault(players.get(entId), new DoubleCoordiante3D(0d, 0d, 0d));
            pos.x += provider.readShort() / (128 * 32d);
            pos.y += provider.readShort() / (128 * 32d);
            pos.z += provider.readShort() / (128 * 32d);

            return true;
        });
    }

    @Override
    public Map<String, PacketOperator> getOperators() {
        return operations;
    }

    @Override
    public boolean isClientBound() {
        return true;
    }
}
