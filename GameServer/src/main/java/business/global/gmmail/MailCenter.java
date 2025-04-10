package business.global.gmmail;

import BaseTask.SyncTask.SyncTaskManager;
import BaseThread.BaseMutexObject;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.RobotManager;
import business.player.feature.features.MailFeature;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.google.common.collect.Lists;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.StringUtils;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefMail;
import core.config.refdata.ref.RefReward;
import core.database.game.bo.GlobalMailBO;
import core.database.game.bo.PlayerMailBO;
import core.logger.flow.FlowLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MailCenter
{
public MailCenter() {
this.allBOs = Lists.newArrayList();

this.offLineMailCached = new HashMap<>();

this.m_mutex = new BaseMutexObject();
} private static MailCenter instance = new MailCenter(); public List<GlobalMailBO> allBOs;
public void lock() {
this.m_mutex.lock();
} public HashMap<Long, List<PlayerMailBO>> offLineMailCached; protected BaseMutexObject m_mutex; public static MailCenter getInstance() {
return instance;
} public void unlock() {
this.m_mutex.unlock();
}

public void init() {
this.allBOs = BM.getBM(GlobalMailBO.class).findAll();

Collections.sort(this.allBOs, new Comparator<GlobalMailBO>()
{
public int compare(GlobalMailBO o1, GlobalMailBO o2) {
return (int)(o1.getId() - o2.getId());
}
});

SyncTaskManager.schedule(600000, () -> {
clearGlobalMail();
return true;
});
}

public List<PlayerMailBO> loadPrivateMail(long cid) {
List<PlayerMailBO> mails = BM.getBM(PlayerMailBO.class).findAll("pid", Long.valueOf(cid));
synchronized (this.offLineMailCached) {
List<PlayerMailBO> cached = this.offLineMailCached.get(Long.valueOf(cid));
if (cached != null && cached.size() > 0) {
mails.addAll(cached);
}
return mails;
} 
}

public void clearGlobalMail() {
if (this.allBOs.size() <= 0) {
return;
}
lock();
List<GlobalMailBO> removeList = new ArrayList<>();
int curTime = CommTime.nowSecond();
for (GlobalMailBO bo : this.allBOs.subList(0, this.allBOs.size() - 1)) {
if (bo.getCreateTime() + bo.getExistTime() < curTime) {
removeList.add(bo);
}
} 
for (GlobalMailBO bo : removeList) {
this.allBOs.remove(bo);
bo.del();
} 
unlock();
}

public List<GlobalMailBO> getGlobalMailList() {
List<GlobalMailBO> list = null;
lock();
list = Lists.newArrayList(this.allBOs);
unlock();
return list;
}

public void sendGlobalMail(String senderName, String title, String content, int existTime, int pickUpExistTime, String uniformIDList, String uniformCountList) {
if (existTime == 0) {
existTime = RefDataMgr.getFactor("MailDefaultExistTime", 604800);
}
GlobalMailBO bo = new GlobalMailBO();
bo.setSenderName(senderName);
bo.setTitle(title);
bo.setContent(content);
bo.setCreateTime(CommTime.nowSecond());
bo.setExistTime(existTime);
bo.setPickUpExistTime(pickUpExistTime);
bo.setUniformIDList(uniformIDList);
bo.setUniformCountList(uniformCountList);

lock();
bo.insert();
this.allBOs.add(bo);
unlock();

Collection<Player> players = PlayerMgr.getInstance().getOnlinePlayers();
for (Player p : players) {
SyncTaskManager.task(() -> ((MailFeature)paramPlayer.getFeature(MailFeature.class)).achieveGlobalMail());
}
}

public void sendMail(long cid, String senderName, String title, String content, Reward prize, String... param) {
for (int i = 0; i < param.length; i++) {
content = content.replace("{" + i + "}", param[i]);
}

String uniformIds = "", counts = "";
for (UniformItem item : prize) {
uniformIds = String.valueOf(uniformIds) + item.getUniformId() + ";";
counts = String.valueOf(counts) + item.getCount() + ";";
} 
sendMail(cid, senderName, title, content, uniformIds, counts);
}

public void sendMail(long cid, String senderName, String title, String content, List<Integer> uniformIdList, List<Integer> countList) {
int existTime = RefDataMgr.getFactor("MailDefaultExistTime", 604800);
int pickUpExistTime = 0;
if (uniformIdList == null || uniformIdList.size() == 0) {
pickUpExistTime = RefDataMgr.getFactor("MailDefaultPickExistTime", 259200);
}
sendMail(cid, senderName, title, content, existTime, pickUpExistTime, uniformIdList, countList);
}

public void sendMail(long cid, String senderName, String title, String content, String uniformIds, String counts) {
int existTime = RefDataMgr.getFactor("MailDefaultExistTime", 604800);
int pickUpExistTime = 0;

pickUpExistTime = RefDataMgr.getFactor("MailDefaultPickExistTime", 259200);

sendMail(cid, senderName, title, content, existTime, pickUpExistTime, uniformIds, counts);
}

public void sendMail(long cid, int mailId, String... param) {
RefMail refMail = (RefMail)RefDataMgr.get(RefMail.class, Integer.valueOf(mailId));
RefReward refReward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refMail.RewardId));
String content = refMail.Content;
for (int i = 0; i < param.length; i++) {
content = content.replace("{" + i + "}", param[i]);
}
List<Integer> uniformIdList = null, countList = null;
if (refReward != null) {
uniformIdList = Lists.newArrayList();
countList = Lists.newArrayList();
for (UniformItem item : refReward.genReward()) {
uniformIdList.add(Integer.valueOf(item.getUniformId()));
countList.add(Integer.valueOf(item.getCount()));
} 
} 
sendMail(cid, "", refMail.Title, content, refMail.ExistTime, refMail.PickUpExistTime, uniformIdList, countList);
}

public void sendMail(long cid, int mailId, int uniformId, int count, String... param) {
List<Integer> uniformIdList = Lists.newArrayList(), countList = Lists.newArrayList();
uniformIdList.add(Integer.valueOf(uniformId));
countList.add(Integer.valueOf(count));
sendMail(cid, mailId, uniformIdList, countList, param);
}

public void sendMail(long cid, int mailId, List<Integer> extUniformIdList, List<Integer> extCountList, String... param) {
RefMail refMail = (RefMail)RefDataMgr.get(RefMail.class, Integer.valueOf(mailId));
RefReward refReward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refMail.RewardId));
String content = refMail.Content;
for (int i = 0; i < param.length; i++) {
content = content.replace("{" + i + "}", param[i]);
}
List<Integer> uniformIdList = Lists.newArrayList(), countList = Lists.newArrayList();
if (refReward != null) {
for (UniformItem uniform : refReward.genReward()) {
uniformIdList.add(Integer.valueOf(uniform.getUniformId()));
countList.add(Integer.valueOf(uniform.getCount()));
} 
}
if (extUniformIdList != null && extUniformIdList.size() > 0) {
uniformIdList.addAll(extUniformIdList);
countList.addAll(extCountList);
} 
sendMail(cid, "", refMail.Title, content, refMail.ExistTime, refMail.PickUpExistTime, uniformIdList, countList);
}

public void sendMail(long cid, int mailId, String extrasUniformIds, String extrasCounts, String params) {
RefMail refMail = (RefMail)RefDataMgr.get(RefMail.class, Integer.valueOf(mailId));

String content = refMail.Content;
if (params != null && params.length() > 0) {
String[] array = params.split(";");
for (int i = 0; i < array.length; i++) {
content = content.replace("{" + i + "}", array[i]);
}
} 

StringBuilder itemId = new StringBuilder();
StringBuilder counts = new StringBuilder();

RefReward reward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refMail.RewardId));
if (reward != null) {
for (UniformItem item : reward.genReward()) {
itemId.append(item.getUniformId()).append(';');
counts.append(item.getCount()).append(';');
} 
}

if (extrasUniformIds != "" && extrasUniformIds.length() > 0) {
itemId.append(extrasUniformIds);
counts.append(extrasCounts);
} 
if (itemId.toString().endsWith(";")) {
itemId.deleteCharAt(itemId.length() - 1);
}
if (counts.toString().endsWith(";")) {
counts.deleteCharAt(counts.length() - 1);
}
sendMail(cid, "", refMail.Title, content, refMail.ExistTime, refMail.PickUpExistTime, itemId.toString(), counts.toString());
}

private void addCachedPrivateMail(long cid, PlayerMailBO mail) {
synchronized (this.offLineMailCached) {
List<PlayerMailBO> mails = this.offLineMailCached.get(Long.valueOf(cid));
if (mails == null) {
this.offLineMailCached.put(Long.valueOf(cid), mails = new ArrayList<>());
}
mails.add(mail);
this.offLineMailCached.put(Long.valueOf(cid), mails);
} 
}

private void sendMail(long cid, String senderName, String title, String content, int existTime, int pickUpExistTime, List<Integer> uniformIdList, List<Integer> countList) {
String uniformIds = "", counts = "";
if (uniformIdList != null && uniformIdList.size() > 0) {
uniformIds = StringUtils.list2String(uniformIdList);
counts = StringUtils.list2String(countList);
} 
sendMail(cid, senderName, title, content, existTime, pickUpExistTime, uniformIds, counts);
}

private void sendMail(long cid, String senderName, String title, String content, int existTime, int pickUpExistTime, String uniformIDList, String uniformCountList) {
if (RobotManager.getInstance().isRobot(cid)) {
return;
}
PlayerMailBO bo = new PlayerMailBO();
bo.setPid(cid);
bo.setSenderName(senderName);
bo.setTitle(title);
bo.setContent(content);
bo.setCreateTime(CommTime.nowSecond());
bo.setExistTime(existTime);
bo.setPickUpExistTime(pickUpExistTime);
bo.setUniformIDList(uniformIDList);
bo.setUniformCountList(uniformCountList);
bo.insert();

Player player = PlayerMgr.getInstance().getPlayer(cid);
if (player == null) {
return;
}

if (player.isLoaded(MailFeature.class)) {
((MailFeature)player.getFeature(MailFeature.class)).addMail(bo);
}
else {

addCachedPrivateMail(cid, bo);
} 

FlowLogger.mail(player.getPid(), player.getOpenId(), player.getVipLevel(), player.getLv(), 
bo.getId(), title, uniformIDList, uniformCountList, senderName, "new");
}

public void delPrivateMail(long cid) {
synchronized (this.offLineMailCached) {
this.offLineMailCached.remove(Long.valueOf(cid));
} 
}
}

