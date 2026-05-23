package RandoMindustry;

import arc.Core;
import arc.Events;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Scaling;
import arc.util.Strings;
import arc.util.Time;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.ClassMap;
import mindustry.mod.Mod;
import mindustry.mod.Mods;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.mods;
import static mindustry.Vars.ui;

@SuppressWarnings("unused")
public class RandoMindustry extends Mod {

    public static Mods.LoadedMod RandomAmmo;

    public RandoMindustry() {
        Log.info("RandoMindustry");
    }

    @Override
    public void init() {
        RSettings.load();
        Events.on(ClientLoadEvent.class, e -> {
            ROverride.load();
        });
    }
}
