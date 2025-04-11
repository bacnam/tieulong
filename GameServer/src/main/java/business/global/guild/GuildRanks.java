package business.global.guild;

import com.zhonglian.server.common.enums.GuildRankType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GuildRanks {
    GuildRankType[] value();
}

