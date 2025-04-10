package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Quality;

public class RefSmelt
extends RefBaseGame
{
@RefField(iskey = true)
public String id;
public int Level;
public Quality Quality;
public int Strengthen;
public int Gold;
public int RedPiece;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

