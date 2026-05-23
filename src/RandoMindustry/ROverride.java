package RandoMindustry;

import arc.Core;
import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.ObjectIntMap;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.entities.bullet.*;
import mindustry.world.blocks.defense.turrets.*;

import static java.lang.Math.max;

public class ROverride {
    public static int size;
    public static Rand rand = new Rand();
    public static float maxRange;
    public static Seq<BulletType> b = new Seq();
    public static Seq<BulletType> fb = new Seq();
    public static ObjectIntMap<BulletType> hfb = new ObjectIntMap();

    public static void load() {
        int n = Core.settings.getInt("RandomSeed1") + Core.settings.getInt("RandomSeed2") * 10
                + Core.settings.getInt("RandomSeed3") * 100 + Core.settings.getInt("RandomSeed4") * 1000
                + Core.settings.getInt("RandomSeed5") * 10000;
        Mathf.rand.setSeed(n);
        rand.setSeed(n);
        Vars.content.units().each(u -> {
            u.weapons.each(w -> {
                b.add(w.bullet.copy());
                size++;
            });
        });
        Vars.content.blocks().each(B -> {
            if (B instanceof Turret) {
                if (B instanceof ItemTurret) {
                    ((ItemTurret) B).ammoTypes.each((item, ammo) -> {
                        b.add(ammo.copy());
                        size++;
                    });
                } else if (B instanceof LiquidTurret) {
                    ((LiquidTurret) B).ammoTypes.each((item, ammo) -> {
                        b.add(ammo.copy());
                        size++;
                    });
                } else if (B instanceof LaserTurret) {
                    b.add(((LaserTurret) B).shootType.copy());
                    size++;
                } else if (B instanceof ContinuousLiquidTurret) {
                    b.add(((ContinuousLiquidTurret) B).shootType.copy());
                    size++;
                } else if (B instanceof ContinuousTurret) {
                    b.add(((ContinuousTurret) B).shootType.copy());
                    size++;
                } else if (B instanceof PowerTurret) {
                    b.add(((PowerTurret) B).shootType.copy());
                    size++;
                }
            }
        });
        Mathf.random();
        if (RSettings.addFragBullet()) {
            b.each(bt -> bt.fragBullet != null, bu -> {
                getfragBullet(bu);
            });

            int maxDepth = Core.settings.getInt("maxFragBulletDepth");
            int minDepth = Core.settings.getInt("minFragBulletDepth");
            b.each(bt -> bt.fragBullet != null || RSettings.allBulletFragBulletRandom(), bu -> {
                int targetDepth = hfb.get(bu,
                        RSettings.allBulletFragBulletRandom() ? Core.settings.getInt("defaultFragBullets", 0) : 0);
                targetDepth = Mathf.clamp(targetDepth, minDepth, maxDepth);

                Seq<BulletType> pool = new Seq<>();
                pool.addAll(fb);
                if (RSettings.allBulletAddFragBullet())
                    pool.addAll(b);
                pool.remove(bu);

                BulletType inner = null;
                ObjectSet<BulletType> used = new ObjectSet<>();
                used.add(bu);

                int depth = 0;
                while (depth < targetDepth && pool.size > used.size - 1) {
                    BulletType candidate;
                    do {
                        candidate = pool.get(rand.random(pool.size - 1));
                    } while (used.contains(candidate));

                    candidate.fragBullet = inner;
                    if (candidate.fragBullets == 9)
                        candidate.fragBullets = Core.settings.getInt("defaultFragBullets");
                    inner = candidate;
                    used.add(candidate);
                    depth++;
                }
                bu.fragBullet = inner;
            });

        }
        Vars.content.units().each(u -> {
            maxRange = 0f;
            u.weapons.each(w -> {
                w.bullet = b.get((int) (Mathf.random() * size));
                maxRange = max(w.bullet.range, maxRange);
            });
            u.range = u.maxRange = maxRange;
        });
        Vars.content.blocks().each(B -> {
            if (B instanceof Turret) {
                if (B instanceof ItemTurret) {
                    ((ItemTurret) B).ammoTypes.each((item, ammo) -> {
                        ((ItemTurret) B).ammoTypes.put(item, b.get((int) (Mathf.random() * size)));
                    });
                } else if (B instanceof LaserTurret) {
                    ((LaserTurret) B).shootType = b.get((int) (Mathf.random() * size));
                } else if (B instanceof LiquidTurret) {
                    ((LiquidTurret) B).ammoTypes.each((item, ammo) -> {
                        ((LiquidTurret) B).ammoTypes.put(item, b.get((int) (Mathf.random() * size)));
                    });
                } else if (B instanceof ContinuousLiquidTurret) {
                    ((ContinuousLiquidTurret) B).shootType = b.get((int) (Mathf.random() * size));
                } else if (B instanceof ContinuousTurret) {
                    ((ContinuousTurret) B).shootType = b.get((int) (Mathf.random() * size));
                } else if (B instanceof PowerTurret) {
                    ((PowerTurret) B).shootType = b.get((int) (Mathf.random() * size));
                }
            }
        });
    }

    public static void getfragBullet(BulletType bt) {
        int i = 1;
        BulletType bt2 = bt.fragBullet;
        fb.add(bt2);

        while (i < 6 && bt2.fragBullet != null) {
            if (i == 1) {
                i++;
                continue;
            }
            fb.add(bt2);
            bt2 = bt2.fragBullet;
            i++;
        }

        hfb.put(bt, i);
    }
}
