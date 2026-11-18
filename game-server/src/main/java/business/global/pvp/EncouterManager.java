package business.global.pvp;

import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EncouterManager {
    private static EncouterManager instacne = null;
    private LinkedList<EncouterManager.EncouterNews> news = new LinkedList<>();

    public static EncouterManager getInstance() {
        if (instacne == null) {
            instacne = new EncouterManager();
        }

        return instacne;
    }

    private EncouterManager() {
    }

    public void addNews(EncouterManager.NewsType type, String player, int treasureId) {
        EncouterManager.EncouterNews news = new EncouterManager.EncouterNews();
        news.type = type;
        news.player = player;
        news.treasureId = treasureId;
        news.time = CommTime.nowSecond();
        this.news.add(news);
        if (this.news.size() > RefDataMgr.getFactor("Encouter_NewsSize", 30)) {
            this.news.poll();
        }
    }

    public List<EncouterManager.EncouterNews> getEncouterNews() {
        return new ArrayList<>(this.news);
    }

    public static class EncouterNews {
        public EncouterManager.NewsType type;
        public String player;
        public int treasureId;
        public int time;
    }

    public static enum NewsType {
        None,
        Drop,
        Rob,
        Open;
    }
}
