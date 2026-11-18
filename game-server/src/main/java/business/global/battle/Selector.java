package business.global.battle;

import com.google.gson.Gson;
import com.zhonglian.server.common.enums.Attribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Selector {
    private Selector.ITarget targetSelect;
    private Selector.ITarget castSelect;
    private Selector.IStrategy strategy;
    private Selector.IArea area;
    private Selector.AreaParam areaParam;

    public Selector(String selectTarget, String strategy, String castTarget, String area, String param) {
        this.targetSelect = target(selectTarget);
        this.castSelect = target(castTarget);
        this.strategy = strategy(strategy);
        this.area = area(area);
        this.areaParam = new Gson().fromJson(param, Selector.AreaParam.class);
    }

    public Selector.Result select(Creature me, List<Creature> all) {
        List<Creature> toSelectCast = new ArrayList<>();
        Creature target = null;

        for (Creature e : all) {
            if (!e.isDead()) {
                if (this.castSelect.apply(me, e)) {
                    toSelectCast.add(e);
                }

                if (this.targetSelect.apply(me, e) && me.inRange(e)) {
                    target = target == null ? e : this.strategy.apply(me, target, e);
                }
            }
        }

        return new Selector.Result(target, this.area.apply(target, toSelectCast, this.areaParam));
    }

    private static Selector.ITarget target(String name) {
        switch (name.hashCode()) {
            case 2573164:
                if (name.equals("Self")) {
                    return (me, e) -> me == e;
                }
                break;
            case 63350320:
                if (name.equals("Alias")) {
                    return (me, e) -> me != e && me.isFriend(e);
                }
                break;
            case 67100520:
                if (!name.equals("Enemy")) {
                }
                break;
            case 69076575:
                if (name.equals("Group")) {
                    return (me, e) -> me == e || me.group == e.group;
                }
                break;
            case 681064005:
                if (name.equals("SelfAndAlias")) {
                    return (me, e) -> me == e || me.isFriend(e);
                }
        }

        return (me, e) -> me != e && !me.isFriend(e);
    }

    private static Selector.IStrategy strategy(String name) {
        switch (name.hashCode()) {
            case -1763776967:
                if (!name.equals("Closest")) {
                }
                break;
            case 69907466:
                if (name.equals("HpMin")) {
                    return (me, tar1, tar2) -> tar1.hp / tar1.attr(Attribute.MaxHP) < tar2.hp / tar2.attr(Attribute.MaxHP) ? tar1 : tar2;
                }
        }

        return (me, tar1, tar2) -> distance0(me, tar1) < distance0(me, tar2) ? tar1 : tar2;
    }

    private static Selector.IArea area(String name) {
        switch (name.hashCode()) {
            case -1818398616:
                if (!name.equals("Single")) {
                }
                break;
            case 65921:
                if (name.equals("All")) {
                    return (target, all, param) -> all;
                }
                break;
            case 2018617584:
                if (name.equals("Circle")) {
                    return (target, all, param) -> {
                        double d = param.radius * param.radius;
                        return all.stream().filter(c -> distance0(target, c) <= d).collect(Collectors.toList());
                    };
                }
        }

        return (target, all, param) -> {
            List<Creature> rtn = new ArrayList<>();
            rtn.add(target);
            return rtn;
        };
    }

    private static double distance0(Creature c1, Creature c2) {
        double dx = c1.x - c2.x;
        double dy = c1.y - c2.y;
        return dx * dx + dy * dy;
    }

    static class AreaParam {
        double radius;
    }

    private interface IArea {
        default List<Creature> apply(Creature me, List<Creature> all, Selector.AreaParam param) {
            return me == null ? null : this.apply0(me, all, param);
        }

        List<Creature> apply0(Creature var1, List<Creature> var2, Selector.AreaParam var3);
    }

    private interface IStrategy {
        Creature apply(Creature var1, Creature var2, Creature var3);
    }

    private interface ITarget {
        boolean apply(Creature var1, Creature var2);
    }

    static class Result {
        Creature selected;
        List<Creature> castOn;

        Result(Creature selected, List<Creature> castOn) {
            this.selected = selected;
            this.castOn = castOn;
        }
    }
}
