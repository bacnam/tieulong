package proto.gameworld;

import com.zhonglian.server.common.enums.EquipPos;
import java.util.List;

public class EquipMessage {
  int equipId;

  long sid;

  List<Integer> attrs;

  int charId;

  EquipPos pos;
}

