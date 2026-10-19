
package example.npcaura;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.mob.MobEntity;

public class NpcAuraClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) {
                return;
            }

            var player = client.player;

            MobEntity target = client.world
                .getEntitiesByClass(
                    MobEntity.class,
                    player.getBoundingBox().expand(3.5),
                    mob -> mob.isAlive()
                        && mob.getScoreboardTags()
                            .contains("training_npc")
                        && player.distanceTo(mob) <= 3.5
                )
                .stream()
                .min(java.util.Comparator.comparingDouble(
                    player::distanceTo
                ))
                .orElse(null);

            if (target != null) {
                player.sendMessage(
                    net.minecraft.text.Text.literal(
                        "Training target: "
                            + target.getName().getString()
                    ),
                    true
                );
            }
        });
    }
}
