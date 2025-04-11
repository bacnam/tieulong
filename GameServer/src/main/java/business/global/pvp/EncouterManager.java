package business.global.pvp;

import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EncouterManager {
    private static EncouterManager instacne = null;
    private LinkedList<EncouterNews> news;

    private EncouterManager() {
        this.news = new LinkedList<>();
    }

    public static EncouterManager getInstance() {
        if (instacne == null) {
            instacne = new EncouterManager();
        }
        return instacne;
    }

    public void addNews(NewsType type, String player, int treasureId) {
        EncouterNews news = new EncouterNews();
        news.type = type;
        news.player = player;
        news.treasureId = treasureId;
        news.time = CommTime.nowSecond();

        this.news.add(news);
        if (this.news.size() > RefDataMgr.getFactor("Encouter_NewsSize", 30)) {
            this.news.poll();
        }
    }

    public List<EncouterNews> getEncouterNews() {
        return new ArrayList<>(this.news);
    }

    public enum NewsType {
        None,
        Drop,
        Rob,
        Open;
    }

    public static class EncouterNews {
        public EncouterManager.NewsType type;
        public String player;
        public int treasureId;
        public int time;
    }
}

