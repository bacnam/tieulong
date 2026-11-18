-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 20, 2023 at 05:42 AM
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
-- Database: `longnu_game_server1_log`
--

-- --------------------------------------------------------

--
-- Table structure for table `activityrequest`
--

CREATE TABLE `activityrequest` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `activity` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿τ▒╗σ₧ï',
  `isActive` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªσÉ»τö¿',
  `gm_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'GMσÉÄσÅ░σ»╣σ║öτÜäid',
  `beginTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿σ╝ÇσÉ»µù╢Θù┤',
  `endTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿τ╗ôµ¥ƒµù╢Θù┤',
  `closeTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿σà│Θù¡µù╢Θù┤',
  `json` text DEFAULT NULL COMMENT 'µ┤╗σè¿σà╖Σ╜ôΘàìτ╜«',
  `type` varchar(500) NOT NULL DEFAULT '' COMMENT 'µôìΣ╜£τ▒╗σ₧ï:σó₧σêáµö╣',
  `operator` varchar(500) NOT NULL DEFAULT '' COMMENT 'µôìΣ╜£σæÿ'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='User Info µ┤╗σè¿µôìΣ╜£µùÑσ┐ùΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `arenatokenchargelog`
--

CREATE TABLE `arenatokenchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='ArenaToken Charge τ½₧µèÇσ╕üµ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `artificematerialchargelog`
--

CREATE TABLE `artificematerialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='ArtificeMaterial Charge τé╝σîûτƒ│µ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `createrolelog`
--

CREATE TABLE `createrolelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `openid` varchar(500) NOT NULL DEFAULT '' COMMENT 'τÄ⌐σ«╢openid',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σê¢ΦºÆµù╢Θù┤',
  `channel` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ╕áΘüô',
  `serverid` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿id'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Create Role σê¢ΦºÆµùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `crystalchargelog`
--

CREATE TABLE `crystalchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Coin Charge µ░┤µÖ╢µ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `equipinstancematerialchargelog`
--

CREATE TABLE `equipinstancematerialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='EquipInstanceMaterial Charge Φúàσñçσë»µ£¼µ¥ÉµûÖµ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `expchargelog`
--

CREATE TABLE `expchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Exp Charge τ╗ÅΘ¬îµ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `geminstancematerialchargelog`
--

CREATE TABLE `geminstancematerialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='GemInstanceMaterial Charge σ«¥τƒ│σë»µ£¼µ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `gemmaterialchargelog`
--

CREATE TABLE `gemmaterialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='GemMaterial Charge σ«¥τƒ│µ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `goldchargelog`
--

CREATE TABLE `goldchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Coin Charge Θçæσ╕üµ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `guilddonatechargelog`
--

CREATE TABLE `guilddonatechargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='GuildDonate Charge τÑ₧ΦúàτóÄτëçµ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `itemlog`
--

CREATE TABLE `itemlog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `itemId` int(11) NOT NULL DEFAULT 0 COMMENT 'Θüôσà╖Id',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Θüôσà╖µ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `lotterychargelog`
--

CREATE TABLE `lotterychargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='lottery Charge τÑ₧ΦúàτóÄτëçµ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `mail`
--

CREATE TABLE `mail` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `cid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢cid',
  `open_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φ┤ªσÅ╖open_id',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `mail_id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θé«Σ╗╢σ«₧Σ╛ïID',
  `title` varchar(500) NOT NULL DEFAULT '' COMMENT 'Θé«Σ╗╢µáçΘóÿ',
  `item_ids` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüIDσêùΦí¿',
  `item_nums` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüµò░ΘçÅσêùΦí¿',
  `sender` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÅæΘÇüΦÇà',
  `type` varchar(500) NOT NULL DEFAULT '' COMMENT 'τ▒╗σ₧ï∩╝Üµû░σó₧/ΘóåσÅû'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='User Info τÄ⌐σ«╢Θé«Σ╗╢µùÑσ┐ùΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `meridianinstancematerialchargelog`
--

CREATE TABLE `meridianinstancematerialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MeridianInstanceMaterial Charge τ╗ÅΦäëµ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `mermaterialchargelog`
--

CREATE TABLE `mermaterialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='MerMaterial Charge τ╗ÅΦäëΣ╕╣µ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `offlinerewards`
--

CREATE TABLE `offlinerewards` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `team_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `dungeon_level` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìτ¡ëτ║º',
  `waves` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ôτ«ùµ│óµò░',
  `offline_time` int(11) NOT NULL DEFAULT 0 COMMENT 'τª╗τ║┐µù╢Θù┤',
  `calc_time` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ôτ«ùΣ╜┐τö¿τÜäµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Offline Rewards τª╗τ║┐σÑûσè▒ΘóåσÅûµùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `rechargelog`
--

CREATE TABLE `rechargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `cid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢cid',
  `open_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φ┤ªσÅ╖open_id',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `order_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φ«óσìòσÅ╖',
  `tp_order_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'τ¼¼Σ╕ëµû╣Φ«óσìòσÅ╖',
  `goods_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'σààσÇ╝Φ┤ºτë⌐ID',
  `money` int(11) NOT NULL DEFAULT 0 COMMENT 'σààσÇ╝ΘçæΘó¥',
  `coin_num` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┤ºσ╕üµò░ΘçÅ',
  `status` varchar(500) NOT NULL DEFAULT '' COMMENT 'σààσÇ╝τè╢µÇü',
  `pay_time` int(11) NOT NULL DEFAULT 0 COMMENT 'σààσÇ╝µù╢Θù┤',
  `pay_time_date` varchar(500) NOT NULL DEFAULT '' COMMENT 'σààσÇ╝µù╢Θù┤date',
  `order_time` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕ïσìòµù╢Θù┤',
  `order_time_date` varchar(500) NOT NULL DEFAULT '' COMMENT 'Σ╕ïσìòµù╢Θù┤date',
  `adfrom` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ╕áΘüô',
  `adfrom2` varchar(500) NOT NULL DEFAULT '' COMMENT 'Σ║îτ║ºµ╕áΘüô',
  `ar_ip` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φ┤ªσÅ╖µ│¿σåîIP',
  `ar_time` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┤ªσÅ╖µ│¿σåîµù╢Θù┤',
  `ar_time_date` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φ┤ªσÅ╖µ│¿σåîµù╢Θù┤date',
  `adid` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φ«╛σñçID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Recharge σààσÇ╝Φ«óσìòΦ«░σ╜òµùÑσ┐ùΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `redpiecechargelog`
--

CREATE TABLE `redpiecechargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='RedPiece Charge τÑ₧ΦúàτóÄτëçµ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `starmaterialchargelog`
--

CREATE TABLE `starmaterialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='StarMaterial Charge σìçµÿƒτƒ│µ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `strengthenmaterialchargelog`
--

CREATE TABLE `strengthenmaterialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='StrengthenMaterial Charge σ╝║σîûµ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `tableversioninfo`
--

CREATE TABLE `tableversioninfo` (
  `ID` int(11) NOT NULL,
  `Version` varchar(64) NOT NULL,
  `Info` varchar(64) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `warspirittalentmaterialchargelog`
--

CREATE TABLE `warspirittalentmaterialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='WarspiritTalentMaterial Charge µêÿτü╡σñ⌐Φ╡ïΣ╕╣µ╢êΦ┤╣µùÑσ┐ù';

-- --------------------------------------------------------

--
-- Table structure for table `wingmaterialchargelog`
--

CREATE TABLE `wingmaterialchargelog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `vip_level` int(11) NOT NULL DEFAULT 0 COMMENT 'VIPτ¡ëτ║º',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ¡ëτ║º',
  `reason` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ║ºτöƒσÄƒσ¢áτ▒╗σ₧ï',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µò░ΘçÅ',
  `cur_remainder` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσë⌐Σ╜Ö',
  `pre_value` int(11) NOT NULL DEFAULT 0 COMMENT 'σëìσÇ╝',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τ▒╗σ₧ï(1:ΦÄ╖σ╛ù,2:µ╢êΦÇù)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='WingMaterial Charge τ╛╜µ»¢µ╢êΦ┤╣µùÑσ┐ù';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activityrequest`
--
ALTER TABLE `activityrequest`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `arenatokenchargelog`
--
ALTER TABLE `arenatokenchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `artificematerialchargelog`
--
ALTER TABLE `artificematerialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `createrolelog`
--
ALTER TABLE `createrolelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `crystalchargelog`
--
ALTER TABLE `crystalchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `equipinstancematerialchargelog`
--
ALTER TABLE `equipinstancematerialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `expchargelog`
--
ALTER TABLE `expchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `geminstancematerialchargelog`
--
ALTER TABLE `geminstancematerialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `gemmaterialchargelog`
--
ALTER TABLE `gemmaterialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `goldchargelog`
--
ALTER TABLE `goldchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guilddonatechargelog`
--
ALTER TABLE `guilddonatechargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `itemlog`
--
ALTER TABLE `itemlog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `lotterychargelog`
--
ALTER TABLE `lotterychargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mail`
--
ALTER TABLE `mail`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `meridianinstancematerialchargelog`
--
ALTER TABLE `meridianinstancematerialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mermaterialchargelog`
--
ALTER TABLE `mermaterialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `offlinerewards`
--
ALTER TABLE `offlinerewards`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rechargelog`
--
ALTER TABLE `rechargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `redpiecechargelog`
--
ALTER TABLE `redpiecechargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `starmaterialchargelog`
--
ALTER TABLE `starmaterialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `strengthenmaterialchargelog`
--
ALTER TABLE `strengthenmaterialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tableversioninfo`
--
ALTER TABLE `tableversioninfo`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `warspirittalentmaterialchargelog`
--
ALTER TABLE `warspirittalentmaterialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `wingmaterialchargelog`
--
ALTER TABLE `wingmaterialchargelog`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activityrequest`
--
ALTER TABLE `activityrequest`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `arenatokenchargelog`
--
ALTER TABLE `arenatokenchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `artificematerialchargelog`
--
ALTER TABLE `artificematerialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `createrolelog`
--
ALTER TABLE `createrolelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `crystalchargelog`
--
ALTER TABLE `crystalchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `equipinstancematerialchargelog`
--
ALTER TABLE `equipinstancematerialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `expchargelog`
--
ALTER TABLE `expchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `geminstancematerialchargelog`
--
ALTER TABLE `geminstancematerialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `gemmaterialchargelog`
--
ALTER TABLE `gemmaterialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `goldchargelog`
--
ALTER TABLE `goldchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guilddonatechargelog`
--
ALTER TABLE `guilddonatechargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `itemlog`
--
ALTER TABLE `itemlog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `lotterychargelog`
--
ALTER TABLE `lotterychargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `mail`
--
ALTER TABLE `mail`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `meridianinstancematerialchargelog`
--
ALTER TABLE `meridianinstancematerialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `mermaterialchargelog`
--
ALTER TABLE `mermaterialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `offlinerewards`
--
ALTER TABLE `offlinerewards`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `rechargelog`
--
ALTER TABLE `rechargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `redpiecechargelog`
--
ALTER TABLE `redpiecechargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `starmaterialchargelog`
--
ALTER TABLE `starmaterialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `strengthenmaterialchargelog`
--
ALTER TABLE `strengthenmaterialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `warspirittalentmaterialchargelog`
--
ALTER TABLE `warspirittalentmaterialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `wingmaterialchargelog`
--
ALTER TABLE `wingmaterialchargelog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
