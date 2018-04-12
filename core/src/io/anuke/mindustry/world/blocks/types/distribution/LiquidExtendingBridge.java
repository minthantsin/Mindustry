package io.anuke.mindustry.world.blocks.types.distribution;

import io.anuke.mindustry.resource.Item;
import io.anuke.mindustry.world.Tile;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.util.Mathf;

import static io.anuke.mindustry.Vars.world;

public class LiquidExtendingBridge extends ExtendingItemBridge {

    public LiquidExtendingBridge(String name) {
        super(name);
        hasInventory = false;
        hasLiquids = true;
    }

    @Override
    public void update(Tile tile) {
        ItemBridgeEntity entity = tile.entity();

        entity.time += entity.cycleSpeed* Timers.delta();
        entity.time2 += (entity.cycleSpeed-1f)*Timers.delta();

        Tile other = world.tile(entity.link);
        if(!linkValid(tile, other)){
            tryDumpLiquid(tile);
        }else{
            float use = Math.min(powerCapacity, powerUse * Timers.delta());

            if(!hasPower || entity.power.amount >= use){
                entity.uptime = Mathf.lerpDelta(entity.uptime, 1f, 0.04f);
                if(hasPower) entity.power.amount -= use;
            }else{
                entity.uptime = Mathf.lerpDelta(entity.uptime, 0f, 0.02f);
            }

            if(entity.uptime >= 0.5f){

                if(tryMoveLiquid(tile, other) > 0.1f){
                    entity.cycleSpeed = Mathf.lerpDelta(entity.cycleSpeed, 4f, 0.05f);
                }else{
                    entity.cycleSpeed = Mathf.lerpDelta(entity.cycleSpeed, 1f, 0.01f);
                }
            }
        }
    }

    @Override
    public boolean acceptItem(Item item, Tile tile, Tile source) {
        return false;
    }
}
