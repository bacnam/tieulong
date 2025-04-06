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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/proto/gameworld/EquipMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */