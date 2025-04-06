package business.gmcmd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
  String command() default "";
  
  String comment();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/annotation/Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */