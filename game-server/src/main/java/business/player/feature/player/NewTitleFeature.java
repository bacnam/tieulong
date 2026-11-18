package business.player.feature.player;

import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerItem;
import business.player.feature.character.CharFeature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefNewTitle;
import core.config.refdata.ref.RefNewTitleLevel;
import core.database.game.bo.NewtitleBO;
import core.network.proto.TitleInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewTitleFeature extends Feature {
    public Map<Integer, NewtitleBO> titleMap = Maps.newConcurrentMap();
    private NewtitleBO usingTitle;

    public NewTitleFeature(Player player) {
        super(player);
    }

    @Override
    public void loadDB() {
        for (NewtitleBO bo : BM.getBM(NewtitleBO.class).findAll("pid", this.player.getPid())) {
            Map<Integer, RefNewTitleLevel> refData = RefNewTitleLevel.getTitleByType(bo.getTitleId());
            if (refData == null) {
                bo.del();
            } else {
                this.titleMap.put(bo.getTitleId(), bo);
                if (bo.getIsUsing()) {
                    this.usingTitle = bo;
                }
            }
        }
    }

    public TitleInfo upgradeTitle(int titleId) throws WSException {
        NewtitleBO bo = this.titleMap.get(titleId);
        if (bo == null) {
            throw new WSException(ErrorCode.Title_NotFound, "称号没找到");
        } else {
            Map<Integer, RefNewTitleLevel> refmap = RefNewTitleLevel.getTitleByType(bo.getTitleId());
            if (refmap == null) {
                throw new WSException(ErrorCode.Title_NotFound, "未找到称号");
            } else if (bo.getLevel() >= refmap.values().size()) {
                throw new WSException(ErrorCode.Title_AlreadyFull, "称号已满级");
            } else {
                RefNewTitleLevel ref = refmap.get(bo.getLevel() + 1);
                if (!this.player.getFeature(PlayerItem.class).checkAndConsume(ref.UniformIdList, ref.UniformCountList, ItemFlow.TitleUpgrade)) {
                    throw new WSException(ErrorCode.NotEnough_Currency, "材料不足");
                } else {
                    bo.saveLevel(bo.getLevel() + 1);
                    this.player.getFeature(CharFeature.class).updateCharPower();
                    return new TitleInfo(bo);
                }
            }
        }
    }

    public TitleInfo activeTitle(int titleId) throws WSException {
        if (this.titleMap.get(titleId) != null) {
            throw new WSException(ErrorCode.Title_AlreadyActive, "称号已激活");
        } else {
            RefNewTitle ref = RefDataMgr.get(RefNewTitle.class, titleId);
            if (ref == null) {
                throw new WSException(ErrorCode.Title_NotFound, "称号没找到");
            } else if (this.player.getLv() < ref.level) {
                throw new WSException(ErrorCode.Title_NotFound, "激活等级不足");
            } else if (!this.player.getFeature(PlayerItem.class).checkAndConsume(ref.ActiveId, ref.ActiveCount, ItemFlow.TitleActive)) {
                throw new WSException(ErrorCode.NotEnough_Currency, "激活材料不足");
            } else {
                NewtitleBO newbo = new NewtitleBO();
                newbo.setPid(this.player.getPid());
                newbo.setTitleId(ref.id);
                newbo.setLevel(1);
                newbo.setActiveTime(CommTime.nowSecond());
                newbo.insert();
                this.titleMap.put(ref.id, newbo);
                this.player.getFeature(CharFeature.class).updateCharPower();
                return new TitleInfo(newbo);
            }
        }
    }

    public TitleInfo useTitle(int titleId) throws WSException {
        NewtitleBO bo = this.titleMap.get(titleId);
        if (bo == null) {
            throw new WSException(ErrorCode.Title_NotFound, "称号没找到");
        } else {
            NewtitleBO old = this.usingTitle;
            if (old != null) {
                old.saveIsUsing(false);
            }

            this.usingTitle = bo;
            bo.saveIsUsing(true);
            return new TitleInfo(bo);
        }
    }

    public List<TitleInfo> getAllTitleInfo() {
        List<TitleInfo> list = new ArrayList<>();

        for (NewtitleBO bo : this.titleMap.values()) {
            list.add(new TitleInfo(bo));
        }

        return list;
    }
}
