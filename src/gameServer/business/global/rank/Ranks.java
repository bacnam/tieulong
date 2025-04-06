package business.global.rank;

import com.zhonglian.server.common.enums.RankType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ranks {
  RankType[] value();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/rank/Ranks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */