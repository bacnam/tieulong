-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 20, 2023 at 05:43 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `longnu_dbgamemaster`
--

-- --------------------------------------------------------

--
-- Table structure for table `game_activity`
--

CREATE TABLE `game_activity` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `activity_type_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'µ┤╗σè¿τ▒╗σ₧ïid',
  `activate` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'µÿ»σÉªµ┐Çµ┤╗',
  `activitity_info` text COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'µ┤╗σè¿σÅéµò░,JSON:{name:''µ╡ïΦ»òτÜä"}',
  `isdeleted` varchar(1) COLLATE utf8_unicode_ci DEFAULT '0' COMMENT 'µÿ»σÉªσêáΘÖñ',
  `create_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'σê¢σ╗║µù╢Θù┤',
  `creater` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `config` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game_activity_logs`
--

CREATE TABLE `game_activity_logs` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ID',
  `activity` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'µ┤╗σè¿σÉìτº░',
  `activityid` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'µ┤╗σè¿ID',
  `server_ids` text COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'µ£ìσèíσÖ¿σêùΦí¿',
  `isall` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'µÿ»σÉªσà¿Θâ¿µ£ìσèíσÖ¿',
  `begintime` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT 'σ╝Çσºïµù╢Θù┤',
  `endtime` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT 'τ╗ôµ¥ƒµù╢Θù┤',
  `closetime` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT 'ΘóåσÅûτ╗ôµ¥ƒµù╢Θù┤',
  `config` text COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Θàìτ╜«',
  `isdeleted` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'µÿ»σÉªσêáΘÖñ',
  `sender` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'σÅæσ╕âΣ║║',
  `createtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'σÅæσ╕âµù╢Θù┤',
  `activate` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `result` text COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game_activity_type`
--

CREATE TABLE `game_activity_type` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `description` varchar(300) DEFAULT NULL COMMENT 'µ┤╗σè¿τ▒╗σ₧ïµÅÅΦ┐░',
  `activity` text DEFAULT NULL COMMENT 'µ┤╗σè¿σÅéµò░∩╝îJSONµá╝σ╝Å∩╝Ü{name:"µÿ╛τñ║τÜäσÉìτº░","descripttion":"µÅÅΦ┐░","count":"µò░ΘçÅ"}'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `game_chat`
--

CREATE TABLE `game_chat` (
  `id` int(100) NOT NULL,
  `sender_id` bigint(255) DEFAULT 0,
  `content` varchar(500) DEFAULT NULL,
  `send_time` int(255) DEFAULT 0,
  `server_id` varchar(255) DEFAULT NULL,
  `world_id` varchar(255) DEFAULT NULL,
  `sender_name` varchar(255) DEFAULT NULL,
  `receive_id` bigint(255) DEFAULT 0,
  `receive_name` varchar(255) DEFAULT NULL,
  `vip` int(10) DEFAULT NULL,
  `total_recharge` int(50) DEFAULT NULL,
  `today_recharge` int(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `game_configs`
--

CREATE TABLE `game_configs` (
  `server_id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT 'µ£ìσèíσÖ¿id',
  `server_key` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'σÅéµò░Σ╕╗Θö«',
  `server_value` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'σÅéµò░σÇ╝'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game_default`
--

CREATE TABLE `game_default` (
  `code` int(2) NOT NULL COMMENT '0 σà¿σ▒Ç 1 σî║σƒƒ  2 µ╕╕µêÅ',
  `game_key` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'σÅéµò░Σ╕╗Θö«',
  `game_value` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'σÅéµò░σÇ╝',
  `remark` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'σñçµ│¿/Φ»┤µÿÄ'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game_gamelist`
--

CREATE TABLE `game_gamelist` (
  `id` varchar(32) NOT NULL,
  `app_id` varchar(32) NOT NULL,
  `app_key` varchar(32) DEFAULT NULL,
  `open_id` varchar(32) DEFAULT NULL,
  `callBackUrl` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `game_giftcodes`
--

CREATE TABLE `game_giftcodes` (
  `giftcode` varchar(32) NOT NULL,
  `code` varchar(32) DEFAULT NULL,
  `usecount` int(11) DEFAULT NULL,
  `state` char(1) DEFAULT NULL COMMENT 'µ£¬σê░µ£ƒσëìτè╢µÇü 1 σ╖▓τ╗ÅΣ╜┐τö¿  0 µ£¬Σ╜┐τö¿',
  `exptime` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `game_gifts`
--

CREATE TABLE `game_gifts` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `code` varchar(32) NOT NULL COMMENT 'τñ╝σîàID',
  `name` varchar(300) DEFAULT NULL COMMENT 'τñ╝σîàσÉìτº░',
  `activation` int(11) DEFAULT NULL COMMENT 'µ┐Çµ┤╗µ¼íµò░',
  `exclude` varchar(32) DEFAULT NULL COMMENT 'Σ║ÆΦ»ëτ▒╗σ₧ï',
  `ids` varchar(2000) DEFAULT NULL COMMENT 'τë⌐σôüIDσêùΦí¿',
  `nums` varchar(2000) DEFAULT NULL COMMENT 'τë⌐σôüµò░ΘçÅσêùΦí¿',
  `mailtitle` varchar(32) DEFAULT NULL COMMENT 'Θé«Σ╗╢µáçΘóÿ',
  `mailcontext` varchar(2000) DEFAULT NULL COMMENT 'Θé«Σ╗╢σåàσ«╣',
  `mailsender` varchar(32) DEFAULT NULL COMMENT 'Θé«Σ╗╢σÅæΘÇüΦÇà',
  `serverid` varchar(32) DEFAULT NULL COMMENT 'µ£ìσèíσÖ¿ID  -1 µëÇµ£ëµ£ìσèíσÖ¿',
  `create_time` timestamp NULL DEFAULT NULL COMMENT 'σê¢σ╗║µù╢Θù┤'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `game_gituselog`
--

CREATE TABLE `game_gituselog` (
  `id` varchar(32) DEFAULT NULL COMMENT 'IDσ║ÅσÅ╖',
  `giftid` varchar(32) DEFAULT NULL COMMENT 'τñ╝σîàID',
  `platform` varchar(300) DEFAULT NULL COMMENT 'Φ┐ÉΦÉÑσòå',
  `uid` varchar(32) DEFAULT NULL COMMENT 'τö¿µê╖ID',
  `cid` varchar(32) DEFAULT NULL COMMENT 'ΦºÆΦë▓ID',
  `code` varchar(32) DEFAULT NULL COMMENT 'σàæµìóτáü',
  `gameid` varchar(32) DEFAULT NULL COMMENT 'µ╕╕µêÅID',
  `serverid` varchar(32) DEFAULT NULL COMMENT 'µ£ìσèíσÖ¿',
  `usetime` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Σ╜┐τö¿µù╢Θù┤'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `game_notice`
--

CREATE TABLE `game_notice` (
  `id` varchar(32) DEFAULT NULL COMMENT 'ID',
  `gameid` varchar(32) DEFAULT NULL COMMENT 'µ╕╕µêÅID',
  `server_id` varchar(32) DEFAULT NULL COMMENT 'µ£ìσèíσÖ¿ID',
  `notice_type` int(11) DEFAULT NULL COMMENT 'σà¼σæèτ▒╗σ₧ï',
  `starttime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp() COMMENT 'σ╝Çσºïµù╢Θù┤',
  `endtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'τ╗ôµ¥ƒµù╢Θù┤',
  `title` varchar(300) DEFAULT NULL COMMENT 'Σ╕╗Θóÿ',
  `contenttype` int(11) DEFAULT NULL COMMENT 'σåàσ«╣τ▒╗σ₧ï',
  `content` text DEFAULT NULL COMMENT 'σåàσ«╣',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'σê¢σ╗║µù╢Θù┤'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='µ╕╕µêÅσà¼σæè';

-- --------------------------------------------------------

--
-- Table structure for table `game_order`
--

CREATE TABLE `game_order` (
  `id` varchar(32) NOT NULL,
  `order_no` varchar(32) NOT NULL COMMENT 'Φ«óσìòID',
  `carrier` varchar(500) DEFAULT NULL COMMENT 'Φ┐ÉΦÉÑσòå',
  `platform` varchar(300) DEFAULT NULL COMMENT 'σ╣│σÅ░ios∩╝îandriod∩╝îΦ╢èτï▒',
  `adfrom` varchar(30) DEFAULT NULL,
  `adfrom2` varchar(30) DEFAULT NULL,
  `gameid` varchar(32) DEFAULT NULL COMMENT 'µ╕╕µêÅid',
  `app_id` varchar(32) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `uid` varchar(32) DEFAULT NULL,
  `cid` varchar(32) DEFAULT NULL,
  `crystal` decimal(10,0) DEFAULT NULL,
  `server_id` varchar(32) DEFAULT NULL,
  `zone_id` varchar(32) DEFAULT NULL,
  `world_id` varchar(32) DEFAULT NULL,
  `quantity` decimal(10,0) DEFAULT NULL COMMENT 'Φ«óσìòΣ║ºσôüµò░ΘçÅ',
  `productid` varchar(32) DEFAULT NULL COMMENT 'Σ║ºσôüid',
  `status` int(2) DEFAULT NULL COMMENT 'τè╢µÇü 0 µ£¬Σ╗ÿµ¼╛  1 σ╖▓Σ╗ÿµ¼╛ 2 Σ╗ÿµ¼╛σñ▒Φ┤Ñ  3 Φ«óσìòσñ▒µòê 4ΘÇÇµ¼╛',
  `create_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `udpate_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `game_servers`
--

CREATE TABLE `game_servers` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT 'id',
  `code` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'τ╝ûτáü',
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'σÉìτº░',
  `pid` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT 'τê╢Φèéτé╣',
  `gametype` int(2) NOT NULL COMMENT '0 σà¿σ▒Ç 1 σî║σƒƒ  2 µ╕╕µêÅ',
  `server_ip` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'IP',
  `intranet_ip` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'σåàτ╜æIP',
  `server_status` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'τè╢µÇü 0 τªüτö¿ 1 σÉ»τö¿',
  `opentime` timestamp NULL DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `whitelist` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'τÖ╜σÉìσìò',
  `starttime` timestamp NULL DEFAULT NULL COMMENT 'τ╗┤µèñσ╝Çσºïµù╢Θù┤',
  `endtime` timestamp NULL DEFAULT NULL COMMENT 'τ╗┤µèñτ╗ôµ¥ƒµù╢Θù┤',
  `mergeserver` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Triggers `game_servers`
--
DELIMITER $$
CREATE TRIGGER `serverdel` BEFORE DELETE ON `game_servers` FOR EACH ROW BEGIN

	DELETE FROM game_configs WHERE server_id = old.id;

    END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `game_userinfo`
--

CREATE TABLE `game_userinfo` (
  `open_id` varchar(255) DEFAULT NULL,
  `server_id` varchar(255) DEFAULT NULL,
  `world_id` varchar(255) DEFAULT NULL,
  `icon` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `vip` int(11) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `logintime` int(11) DEFAULT NULL,
  `gameKey` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `game_userlogin`
--

CREATE TABLE `game_userlogin` (
  `open_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'τö¿µê╖ID',
  `world_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'τÖ╗σ╜òWORLD',
  `server_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'τÖ╗σ╜òµ£ìσèíσÖ¿',
  `login_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'τÖ╗σ╜òµù╢Θù┤'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sys_config_tab`
--

CREATE TABLE `sys_config_tab` (
  `name` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `value` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `info` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sys_group`
--

CREATE TABLE `sys_group` (
  `group_id` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `group_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `group_info` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `islocked` tinyint(1) DEFAULT 0,
  `isdefault` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sys_group_menu_rule`
--

CREATE TABLE `sys_group_menu_rule` (
  `group_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `menu_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sys_group_user`
--

CREATE TABLE `sys_group_user` (
  `group_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sys_login_log`
--

CREATE TABLE `sys_login_log` (
  `userid` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `login_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_name` varchar(400) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ipaddr` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `session` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `login_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `logout_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `sys_menu`
--

CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL,
  `menu_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_id` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `menu_link` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `menu_order` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Triggers `sys_menu`
--
DELIMITER $$
CREATE TRIGGER `adminmenurule` AFTER INSERT ON `sys_menu` FOR EACH ROW BEGIN

	insert into sys_menu_user_rule values (-9527,NEW.menu_id);

    END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `adminmenuruledel` AFTER DELETE ON `sys_menu` FOR EACH ROW BEGIN

	DELETE FROM sys_menu_user_rule WHERE menu_id = OLD.menu_id;

	DELETE FROM sys_group_menu_rule WHERE menu_id = OLD.menu_id;

    END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `sys_menu_user_rule`
--

CREATE TABLE `sys_menu_user_rule` (
  `user_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `menu_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sys_operation_logs`
--

CREATE TABLE `sys_operation_logs` (
  `id` int(11) NOT NULL COMMENT 'ID',
  `dealtype` varchar(20) DEFAULT NULL COMMENT 'τ▒╗σ₧ï',
  `title` varchar(300) DEFAULT NULL COMMENT 'µáçΘóÿ',
  `info` blob DEFAULT NULL COMMENT 'Σ┐íµü»',
  `resultInfo` blob DEFAULT NULL,
  `dealtime` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'µù╢Θù┤',
  `user` varchar(300) DEFAULT NULL COMMENT 'τö¿µê╖ID/σÉìτº░',
  `state` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `sys_users`
--

CREATE TABLE `sys_users` (
  `user_id` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `user_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_truename` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `login_id` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `invalidtime` date DEFAULT NULL COMMENT 'σñ▒µòêµù╢Θù┤',
  `create_time` timestamp NULL DEFAULT current_timestamp(),
  `islock` tinyint(1) DEFAULT 0 COMMENT 'µÿ»σÉªΦó½Θöüσ«Ü',
  `disabled` tinyint(1) DEFAULT 0 COMMENT 'µÿ»σÉªσÅ»τö¿',
  `isproxyadmin` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isadmin` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isproxy` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `proxyid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Triggers `sys_users`
--
DELIMITER $$
CREATE TRIGGER `after_del` AFTER DELETE ON `sys_users` FOR EACH ROW delete from sys_group_user where user_id = OLD.user_id
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `sys_visit_log`
--

CREATE TABLE `sys_visit_log` (
  `uri` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userid` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `login_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ipaddr` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hostname` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `session` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `visit_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `parameter` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_base64code`
-- (See below for the actual view)
--
CREATE TABLE `view_base64code` (
`before` varchar(1)
,`after` varchar(1)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_group_user`
-- (See below for the actual view)
--
CREATE TABLE `view_group_user` (
`group_id` varchar(32)
,`group_name` varchar(30)
,`group_info` varchar(500)
,`user_id` varchar(32)
,`user_name` varchar(30)
,`user_truename` varchar(30)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_menu_root`
-- (See below for the actual view)
--
CREATE TABLE `view_menu_root` (
`parent_id` int(11)
,`parent_name` varchar(30)
,`parent_link` varchar(4000)
);

-- --------------------------------------------------------

--
-- Structure for view `view_base64code`
--
DROP TABLE IF EXISTS `view_base64code`;

CREATE ALGORITHM=UNDEFINED DEFINER=`dbgamemaster`@`%` SQL SECURITY DEFINER VIEW `view_base64code`  AS SELECT 'A' AS `before`, '0' AS `after` ;

-- --------------------------------------------------------

--
-- Structure for view `view_group_user`
--
DROP TABLE IF EXISTS `view_group_user`;

CREATE ALGORITHM=UNDEFINED DEFINER=`dbgamemaster`@`%` SQL SECURITY DEFINER VIEW `view_group_user`  AS   (select `g`.`group_id` AS `group_id`,`g`.`group_name` AS `group_name`,`g`.`group_info` AS `group_info`,`u`.`user_id` AS `user_id`,`u`.`user_name` AS `user_name`,`u`.`user_truename` AS `user_truename` from ((`sys_group` `g` join `sys_users` `u`) join `sys_group_user` `gu`) where `g`.`group_id` = `gu`.`group_id` and `u`.`user_id` = `gu`.`user_id`)  ;

-- --------------------------------------------------------

--
-- Structure for view `view_menu_root`
--
DROP TABLE IF EXISTS `view_menu_root`;

CREATE ALGORITHM=UNDEFINED DEFINER=`dbgamemaster`@`%` SQL SECURITY DEFINER VIEW `view_menu_root`  AS   (select distinct `t`.`menu_id` AS `parent_id`,`t`.`menu_name` AS `parent_name`,`t`.`menu_link` AS `parent_link` from `sys_menu` `t` where `t`.`parent_id` = '00')  ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `game_activity`
--
ALTER TABLE `game_activity`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `game_activity_logs`
--
ALTER TABLE `game_activity_logs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `game_activity_type`
--
ALTER TABLE `game_activity_type`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `game_chat`
--
ALTER TABLE `game_chat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `game_configs`
--
ALTER TABLE `game_configs`
  ADD KEY `serverid` (`server_id`);

--
-- Indexes for table `game_gamelist`
--
ALTER TABLE `game_gamelist`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `game_giftcodes`
--
ALTER TABLE `game_giftcodes`
  ADD KEY `giftcode` (`code`),
  ADD KEY `code` (`giftcode`,`exptime`),
  ADD KEY `date` (`exptime`);

--
-- Indexes for table `game_gifts`
--
ALTER TABLE `game_gifts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pk_code` (`code`),
  ADD KEY `game_id` (`serverid`);

--
-- Indexes for table `game_gituselog`
--
ALTER TABLE `game_gituselog`
  ADD KEY `code` (`code`),
  ADD KEY `serverid` (`serverid`),
  ADD KEY `giftid` (`giftid`);

--
-- Indexes for table `game_order`
--
ALTER TABLE `game_order`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `game_servers`
--
ALTER TABLE `game_servers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `game_userlogin`
--
ALTER TABLE `game_userlogin`
  ADD KEY `open_id` (`open_id`,`world_id`) USING BTREE,
  ADD KEY `login_time` (`login_time`) USING BTREE;

--
-- Indexes for table `sys_group`
--
ALTER TABLE `sys_group`
  ADD PRIMARY KEY (`group_id`);

--
-- Indexes for table `sys_menu`
--
ALTER TABLE `sys_menu`
  ADD PRIMARY KEY (`menu_id`);

--
-- Indexes for table `sys_operation_logs`
--
ALTER TABLE `sys_operation_logs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sys_users`
--
ALTER TABLE `sys_users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `game_chat`
--
ALTER TABLE `game_chat`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sys_menu`
--
ALTER TABLE `sys_menu`
  MODIFY `menu_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sys_operation_logs`
--
ALTER TABLE `sys_operation_logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID';
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
