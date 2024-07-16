package imagineBreaker.system;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import imagineBreaker.data.Data;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ImagineBreaker extends JavaPlugin {
    public static Plugin plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        onData();
        onPacket();
    }

    public void onData() {
        Data.loadAll();
    }

    public void onPacket() {
        var pm = ProtocolLibrary.getProtocolManager();
        pm.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Status.Server.SERVER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                var packet = event.getPacket();
                var json = Data.serverInfo(packet.getServerPings().read(0).toJson());
                var wrappedServerPing = WrappedServerPing.fromJson(json);
                packet.getServerPings().write(0, wrappedServerPing);
            }
        });
    }
}
