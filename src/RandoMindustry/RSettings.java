package RandoMindustry;

import arc.Core;
import arc.scene.ui.ImageButton;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.Setting;

public final class RSettings {
  public static int g, s, b, q, w;

  public static void load() {
    Vars.ui.settings.addCategory(Core.bundle.get("settingShow"), "randomindustry-Rand", S -> {
      S.sliderPref("RandomSeed5", 0, 0, 9, 1, i -> {
        w = i;
        return "[gold]" + w + "[white]    ";
      });
      S.sliderPref("RandomSeed4", 0, 0, 9, 1, i -> {
        q = i;
        return " [gold]" + q + "[white]   ";
      });
      S.sliderPref("RandomSeed3", 0, 0, 9, 1, i -> {
        b = i;
        return "  [gold]" + b + "[white]  ";
      });
      S.sliderPref("RandomSeed2", 0, 0, 9, 1, i -> {
        s = i;
        return "   [gold]" + s + "[white] ";
      });
      S.sliderPref("RandomSeed1", 0, 0, 9, 1, i -> {
        g = i;
        return "    [gold]" + g + "[white]";
      });
      S.row();
      S.add("[accent]" + "@text.fragBullet");
      S.checkPref("addFragBullet", false);
      S.checkPref("allBulletFragBulletRandom", false);
      S.checkPref("allBulletAddFragBullet", false);
      S.sliderPref("defaultFragBullets", 1, 0, 5, i -> {
        if (i > 0 && i < 5)
          return "    [gold]" + i + "[white]";
        else if (i <= 0)
          return "     [gold]" + Core.bundle.get("text.none") + "[white]";
        return "     [red]" + i + "[white]";
      });

      S.sliderPref("maxFragBulletDepth", 5, 0, 10, i -> {
        if (i <= 5 && i > 0)
          return "    [red]" + i + "[white]";
        else if (i <= 0)
          return "     [gold]" + Core.bundle.get("text.none") + "[white]";
        return "     [red]" + i + "[white]";
      });

      S.sliderPref("minFragBulletDepth", 1, 0, 10, i -> {
        if (i <= 5 && i > 0)
          return "    [gold]" + i + "[white]";
        else if (i <= 0)
          return "     [gold]" + Core.bundle.get("text.none") + "[white]";
        return "     [red]" + i + "[white]";
      });

      S.pref(new LoadButton());
    });
  }

  public static boolean addFragBullet() {
    return Core.settings.getBool("addFragBullet", false);
  }

  public static boolean allBulletFragBulletRandom() {
    return Core.settings.getBool("allBulletFragBulletRandom", false);
  }

  public static boolean allBulletAddFragBullet() {
    return Core.settings.getBool("allBulletAddFragBullet");
  }

  static class LoadButton extends Setting {
    public LoadButton() {
      super("load");
      title = "setting.load.name";
    }

    @Override
    public void add(SettingsTable table) {
      ImageButton ib = table.button(Icon.refresh, () -> {
        ROverride.load();
      }).get();
      ib.label(() -> Core.bundle.get(title));
      table.row();
      addDesc(ib);
    }
  }
}
