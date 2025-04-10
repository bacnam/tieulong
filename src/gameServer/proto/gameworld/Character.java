package proto.gameworld;

import java.util.ArrayList;
import java.util.List;

public class Character {
public static class CharInfo {
public long sid;
public int charId;
public int level;
public int meridian;
public int wing;
public int wingExp;
public int rebirth;
public int rebirthExp;
public List<Integer> strengthen = new ArrayList<>();
public List<Integer> gem = new ArrayList<>();
public List<Integer> star = new ArrayList<>();
public List<Integer> skill = new ArrayList<>();
public List<Integer> artifice = new ArrayList<>();
public List<Integer> artificeMax = new ArrayList<>();
public List<EquipMessage> equips = new ArrayList<>();
public List<DressInfo> dresses = new ArrayList<>();
}
}

