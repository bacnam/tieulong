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
-- Database: `longnu_dbhunkftalk`
--

-- --------------------------------------------------------

--
-- Table structure for table `activity`
--

CREATE TABLE `activity` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `activity` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿τ▒╗σ₧ï',
  `isActive` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªσÉ»τö¿',
  `lastStatus` varchar(500) NOT NULL DEFAULT '' COMMENT 'Σ╕èµ¼íµúÇµ╡ïτÜäµù╢σÇÖτÜäτè╢µÇü',
  `gm_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'PHPσÉÄσÅ░σ»╣σ║öτÜäid',
  `beginTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿σ╝ÇσÉ»µù╢Θù┤',
  `endTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿τ╗ôµ¥ƒµù╢Θù┤-σÅ»Φâ╜σ«óµê╖τ½»Φ┐ÿΘ£ÇΦªüµÿ╛τñ║',
  `closeTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿σà│Θù¡µù╢Θù┤-µ┤╗σè¿σ╜╗σ║òσà│Θù¡∩╝îσ«óµê╖τ½»Σ╕ìσåìµÿ╛τñ║',
  `json` text DEFAULT NULL COMMENT 'µ┤╗σè¿σà╖Σ╜ôΘàìτ╜«',
  `joinIn` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿σÅéΣ╕ÄΣ║║µò░',
  `winnerCid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿Φ╡óσ«╢cid',
  `extInt_0` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extInt_1` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extInt_2` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extInt_3` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extInt_4` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extInt_5` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extInt_6` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extInt_7` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extInt_8` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extInt_9` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿intσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_0` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_1` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_2` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_3` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_4` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_5` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_6` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_7` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_8` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»',
  `extStr_9` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿stringσ₧ïµë⌐σ▒òσ¡ùµ«╡Σ┐íµü»'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µ┤╗σè¿Σ┐íµü»Φí¿∩╝îΦ«░σ╜òµ»ÅΣ╕¬µ┤╗σè¿σƒ║µ£¼Σ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `activityrecord`
--

CREATE TABLE `activityrecord` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢pid',
  `aid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σ»╣σ║öµ┤╗σè¿Σ╕╗Θö«Id',
  `serverId` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿id',
  `activity` varchar(500) NOT NULL DEFAULT '' COMMENT 'µëÇσ▒₧µ┤╗σè¿',
  `extInt_0` int(11) NOT NULL DEFAULT 0 COMMENT 'intσ₧ïµë⌐σ▒òσ¡ùµ«╡',
  `extInt_1` int(11) NOT NULL DEFAULT 0 COMMENT 'intσ₧ïµë⌐σ▒òσ¡ùµ«╡',
  `extInt_2` int(11) NOT NULL DEFAULT 0 COMMENT 'intσ₧ïµë⌐σ▒òσ¡ùµ«╡',
  `extInt_3` int(11) NOT NULL DEFAULT 0 COMMENT 'intσ₧ïµë⌐σ▒òσ¡ùµ«╡',
  `extInt_4` int(11) NOT NULL DEFAULT 0 COMMENT 'intσ₧ïµë⌐σ▒òσ¡ùµ«╡',
  `extStr_0` varchar(500) NOT NULL DEFAULT '' COMMENT 'stringσ₧ïµë⌐σ▒òσ¡ùµ«╡',
  `extStr_1` varchar(500) NOT NULL DEFAULT '' COMMENT 'stringσ₧ïµë⌐σ▒òσ¡ùµ«╡',
  `extStr_2` varchar(500) NOT NULL DEFAULT '' COMMENT 'stringσ₧ïµë⌐σ▒òσ¡ùµ«╡',
  `extStr_3` varchar(500) NOT NULL DEFAULT '' COMMENT 'stringσ₧ïµë⌐σ▒òσ¡ùµ«╡'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µ┤╗σè¿σÅéΣ╕ÄΦ«░σ╜òΣ┐íµü»∩╝îΦ«░σ╜òµ»ÅΣ╕¬τÄ⌐σ«╢σÅéσèáµ┤╗σè¿τÜäσÅéΣ╕ÄΦ«░σ╜ò';

-- --------------------------------------------------------

--
-- Table structure for table `chatmessage`
--

CREATE TABLE `chatmessage` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `chatType` int(11) NOT NULL DEFAULT 0 COMMENT 'Φüèσñ⌐ΘóæΘüô[1πÇüΣ╕ûτòîΘóæΘüô 2πÇüσà¼Σ╝ÜΘóæΘüô 3πÇüτºüΦüèΘóæΘüô 4πÇüτ│╗τ╗ƒΘóæΘüô]',
  `senderPid` bigint(20) NOT NULL DEFAULT 0 COMMENT ']',
  `senderName` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φüèσñ⌐ΘóæΘüô[1πÇüΣ╕ûτòîΘóæΘüô 2πÇüσà¼Σ╝ÜΘóæΘüô 3πÇüτºüΦüèΘóæΘüô 4πÇüτ│╗τ╗ƒΘóæΘüô]',
  `senderLv` int(11) NOT NULL DEFAULT 0 COMMENT 'σà╢Σ╜ÖΦí¿τñ║τÄ⌐σ«╢pid[τÄ░σ£¿τÜäτÄ⌐σ«╢pidΘâ╜µÿ»σñºµò░σ¡ù]',
  `senderVipLv` int(11) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüΦÇàCID',
  `senderIcon` int(11) NOT NULL DEFAULT 0 COMMENT 'Φüèσñ⌐σåàσ«╣',
  `senderServerId` int(11) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüΦÇàµ£ìσèíσÖ¿Id',
  `message` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÅæΘÇüµù╢Θù┤',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÅæΘÇüµù╢Θù┤',
  `sendTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüµù╢Θù┤',
  `receivePid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüµù╢Θù┤',
  `receiveName` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÅæΘÇüµù╢Θù┤',
  `MonthCard` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüµù╢Θù┤',
  `YearCard` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüµù╢Θù┤',
  `is_married` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Φüèσñ⌐Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `dailyworldrefresh`
--

CREATE TABLE `dailyworldrefresh` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `serverStart` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿Θªûµ¼íσÉ»σè¿',
  `indexLastTime_0` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_1` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_2` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_3` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_4` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_5` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_6` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_7` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_8` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_9` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_10` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_11` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_12` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_13` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_14` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_15` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_16` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_17` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_18` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_19` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_20` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_21` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_22` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_23` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_24` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_25` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_26` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_27` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_28` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_29` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_30` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_31` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_32` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_33` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_34` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_35` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_36` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_37` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_38` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_39` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µ»ÅµùÑσê╖µû░';

-- --------------------------------------------------------

--
-- Table structure for table `rank_record`
--

CREATE TABLE `rank_record` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'µÄÆΦíîµª£τ▒╗σ₧ï',
  `owner` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧ΦÇàid',
  `serverId` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿id',
  `value` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σÇ╝',
  `updateTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ¢┤µû░µù╢Θù┤',
  `ext1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡1',
  `ext2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡2',
  `ext3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡3',
  `ext4` varchar(200) NOT NULL DEFAULT '' COMMENT 'µë⌐σ▒òσ¡ùµ«╡4',
  `ext5` varchar(200) NOT NULL DEFAULT '' COMMENT 'µë⌐σ▒òσ¡ùµ«╡5',
  `name` varchar(500) NOT NULL DEFAULT '' COMMENT 'σºôσÉì',
  `lv` int(11) NOT NULL DEFAULT 0 COMMENT 'τ¡ëτ║º',
  `icon` int(11) NOT NULL DEFAULT 0 COMMENT 'σñ┤σâÅ',
  `vipLv` int(11) NOT NULL DEFAULT 0 COMMENT 'vip',
  `maxPower` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùσè¢'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µÄÆΦíîΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `serverconfig`
--

CREATE TABLE `serverconfig` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `key` varchar(500) NOT NULL DEFAULT '' COMMENT 'Θö«',
  `value` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÇ╝'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µ£ìσèíσÖ¿σè¿µÇüΘàìτ╜«';

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
-- Table structure for table `worldmail`
--

CREATE TABLE `worldmail` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `cid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢cid',
  `mailID` int(11) NOT NULL DEFAULT 0 COMMENT 'Θé«Σ╗╢Θ¥ÖµÇüId',
  `uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüIdσêùΦí¿',
  `uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüµò░ΘçÅσêùΦí¿',
  `params` varchar(500) NOT NULL DEFAULT '' COMMENT 'Θó¥σñûτÜäσÅéµò░σêùΦí¿'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Zoneσ╗╢Φ┐ƒΘé«Σ╗╢Σ┐íµü»';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activity`
--
ALTER TABLE `activity`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `activityrecord`
--
ALTER TABLE `activityrecord`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `chatmessage`
--
ALTER TABLE `chatmessage`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dailyworldrefresh`
--
ALTER TABLE `dailyworldrefresh`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rank_record`
--
ALTER TABLE `rank_record`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `serverconfig`
--
ALTER TABLE `serverconfig`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tableversioninfo`
--
ALTER TABLE `tableversioninfo`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `worldmail`
--
ALTER TABLE `worldmail`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activity`
--
ALTER TABLE `activity`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `activityrecord`
--
ALTER TABLE `activityrecord`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `chatmessage`
--
ALTER TABLE `chatmessage`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `dailyworldrefresh`
--
ALTER TABLE `dailyworldrefresh`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `rank_record`
--
ALTER TABLE `rank_record`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `serverconfig`
--
ALTER TABLE `serverconfig`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `worldmail`
--
ALTER TABLE `worldmail`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
