package core.network.client2game;

import java.util.HashSet;

public class ProtoForward {
    public static HashSet<String> ZoneForwardList = new HashSet<>();
    public static HashSet<String> WorldForwardList = new HashSet<>();

    static {
        ZoneForwardList.add("zforward.client.test");

        WorldForwardList.add("wforward.client.test");
    }
}

