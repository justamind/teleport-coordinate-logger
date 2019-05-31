package packets.builder;

import game.Game;
import game.data.Coordinate2D;
import game.data.Coordinate3D;
import game.data.WorldManager;
import game.data.chunk.ChunkFactory;
import se.llbit.nbt.SpecificTag;

import java.util.HashMap;
import java.util.Map;

public class ClientBoundGamePacketBuilder extends PacketBuilder {
    private HashMap<Integer, String> players = new HashMap<>();

    private HashMap<String, PacketOperator> operations = new HashMap<>();
    public ClientBoundGamePacketBuilder() {
        operations.put("chunk_data", provider -> {
            try {
                ChunkFactory.getInstance().addChunk(provider);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        });
        operations.put("chunk_unload", provider -> {
            WorldManager.unloadChunk(new Coordinate2D(provider.readInt(), provider.readInt()));
            return true;
        });
        operations.put("chunk_update_light", provider -> {
            // TODO: update chunk light for 1.14
            return true;
        });
        operations.put("update_block_entity", provider -> {
            Coordinate3D position = provider.readCoordinates();
            byte action = provider.readNext();
            SpecificTag entityData = provider.readNbtTag();

            ChunkFactory.getInstance().updateTileEntity(position, entityData);
            return true;
        });
        PacketOperator updatePlayerPosition = provider -> {
            double x = provider.readDouble();
            double y = provider.readDouble();
            double z = provider.readDouble();

            Coordinate3D playerPos = new Coordinate3D(x, y, z);
            playerPos.offsetGlobal();
            Game.setPlayerPosition(playerPos);

            return true;
        };

        operations.put("player_position_look", updatePlayerPosition);
        operations.put("player_vehicle_move", updatePlayerPosition);


        operations.put("spawn_player", (provider) -> {
            int entId = provider.readVarInt();
            String uuid = provider.readUUID();
            players.put(entId, uuid);
            System.out.println("New player spawned with UUID " + uuid + " at " + new Coordinate3D(provider.readDouble(), provider.readDouble(), provider.readDouble()));
            return true;
        });

        operations.put("entity_teleport", provider -> {
            int entId = provider.readVarInt();
            Coordinate3D dest = new Coordinate3D(provider.readDouble(), provider.readDouble(), provider.readDouble());
            if (players.containsKey(entId)) {
                System.out.println("Player " + players.get(entId) + " moved to " + dest);
            }
            return true;
        });
        operations.put("entity_move", provider -> {
            int entId = provider.readVarInt();

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
