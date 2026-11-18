package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.common.utils.Random;
import core.config.refdata.RefDataMgr;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RefRobot extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public int Count;
    public int VIP;
    public int Level;
    public int DungeonLevel;
    public List<Integer> Characters1;
    public List<Integer> Characters2;
    public List<Integer> Characters3;
    public List<Integer> Characters4;
    public List<Integer> Characters5;

    public List<Integer> randTeam() {
        List<List<Integer>> teams = new ArrayList<>();
        if (!this.Characters1.isEmpty()) {
            teams.add(this.Characters1);
        }

        if (!this.Characters2.isEmpty()) {
            teams.add(this.Characters2);
        }

        if (!this.Characters3.isEmpty()) {
            teams.add(this.Characters3);
        }

        if (!this.Characters4.isEmpty()) {
            teams.add(this.Characters4);
        }

        if (!this.Characters5.isEmpty()) {
            teams.add(this.Characters5);
        }

        return teams.get(Random.nextInt(teams.size()));
    }

    @Override
    public boolean Assert() {
        if (!this.assertCharacters(this.Characters1)) {
            return false;
        } else if (!this.assertCharacters(this.Characters2)) {
            return false;
        } else if (!this.assertCharacters(this.Characters3)) {
            return false;
        } else if (!this.assertCharacters(this.Characters4)) {
            return false;
        } else if (!this.assertCharacters(this.Characters5)) {
            return false;
        } else if (this.Characters1.size() == 0
            && this.Characters2.size() == 0
            && this.Characters3.size() == 0
            && this.Characters4.size() == 0
            && this.Characters5.size() == 0) {
            CommLog.error("至少需要留一支队伍给机器人");
            return false;
        } else {
            return true;
        }
    }

    private boolean assertCharacters(List<Integer> characters) {
        if (characters.size() == 0) {
            return true;
        } else if (!RefAssert.inRef(characters, RefRobotCharacter.class, 0)) {
            return false;
        } else {
            UnlockType type = UnlockType.valueOf("Character" + characters.size());
            if (type != UnlockType.Character1 && !RefUnlockFunction.checkUnlockSave(this.Level, this.VIP, type)) {
                CommLog.error("玩家 {}级 VIP:{} 不能拥有{}个角色", this.Level, this.VIP, characters.size());
                return false;
            } else {
                Set<Integer> charidSet = new HashSet<>();

                for (Integer simulateId : characters) {
                    RefRobotCharacter character = RefDataMgr.get(RefRobotCharacter.class, simulateId);
                    charidSet.add(character.CharId);

                    for (Integer equipid : character.Equip) {
                        if (equipid != 0 && RefDataMgr.get(RefEquip.class, equipid).Level > this.Level) {
                            CommLog.error("玩家 {}级 配置的角色:{}, 不能穿戴装备:{}", this.Level, simulateId, equipid);
                            return false;
                        }
                    }
                }

                if (charidSet.size() != characters.size()) {
                    CommLog.error("配置了相同的角色 ID[" + characters.toString() + "]");
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
