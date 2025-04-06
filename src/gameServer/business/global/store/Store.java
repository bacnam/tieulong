package business.global.store;

import com.zhonglian.server.common.enums.StoreType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Store {
  StoreType[] value();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/store/Store.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */