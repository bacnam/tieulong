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
-- Database: `longnu_game_server1`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `open_id` varchar(50) NOT NULL DEFAULT '' COMMENT 'σ╣│σÅ░τö¿µê╖openid',
  `username` varchar(128) NOT NULL DEFAULT '' COMMENT 'Φ┤ªσÅ╖σÉì',
  `ip` varchar(32) NOT NULL DEFAULT '' COMMENT 'σ╜ôσëìτÖ╗σ╜òIP',
  `mac` varchar(50) NOT NULL DEFAULT '' COMMENT 'σ╜ôσëìτÖ╗σ╜òMacσ£░σ¥Ç',
  `loginTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìτÖ╗σ╜òµù╢Θù┤',
  `lastIp` varchar(32) NOT NULL DEFAULT '' COMMENT 'Σ╕èµ¼íτÖ╗σ╜òIP',
  `lastMac` varchar(50) NOT NULL DEFAULT '' COMMENT 'Σ╕èµ¼íτÖ╗σ╜òMac',
  `lastLoginTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íτÖ╗σ╜òµùÑµ£ƒ',
  `banExpiredTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σ░üσÅ╖Φ┐çµ£ƒµù╢Θù┤',
  `lastServerId` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇσÉÄτÖ╗σ╜òτÜäµ£ìσèíσÖ¿ID',
  `lastClientVersion` varchar(30) NOT NULL DEFAULT '' COMMENT 'µ£ÇσÉÄτÖ╗σ╜òτÜäσ«óµê╖τ½»τëêµ£¼',
  `adFrom` varchar(50) NOT NULL DEFAULT '' COMMENT 'τÄ⌐σ«╢µ╕áΘüô',
  `adFrom2` varchar(50) NOT NULL DEFAULT '' COMMENT 'τÄ⌐σ«╢Σ║îτ║ºµ╕áΘüô',
  `regIp` varchar(32) NOT NULL DEFAULT '' COMMENT 'µ│¿σåîIP',
  `regMac` varchar(50) NOT NULL DEFAULT '' COMMENT 'µ│¿σåîMacσ£░σ¥Ç',
  `regTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ│¿σåîµù╢Θù┤',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σê¢σ╗║µù╢Θù┤',
  `adid` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φ«╛σñçID',
  `nation` varchar(50) NOT NULL DEFAULT '' COMMENT 'σ¢╜σ«╢'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Φ┤ªσÅ╖Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `achievement`
--

CREATE TABLE `achievement` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '*τÄ⌐σ«╢ID',
  `achieveId` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÉσ░▒ID',
  `completeCount` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«îµêÉµ¼íµò░',
  `achieveCount` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ╛╛µêÉµêÉσ░▒µò░ΘçÅ(τ│╗σêùµêÉσ░▒)',
  `gainPrizeList` varchar(500) NOT NULL DEFAULT '' COMMENT 'ΘóåσÅûσêùΦí¿',
  `argument` bigint(20) NOT NULL DEFAULT 0 COMMENT 'ΘÖäσèáσÅéµò░',
  `argument2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'ΘÖäσèáσÅéµò░2',
  `argument3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'ΘÖäσèáσÅéµò░3'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µêÉσ░▒Σ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `activity`
--

CREATE TABLE `activity` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `activity` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ┤╗σè¿τ▒╗σ₧ï',
  `isActive` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªσÉ»τö¿',
  `lastStatus` varchar(500) NOT NULL DEFAULT '' COMMENT 'Σ╕èµ¼íµúÇµ╡ïτÜäµù╢σÇÖτÜäτè╢µÇü',
  `gm_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'GMσÉÄσÅ░σ»╣σ║öτÜäid',
  `beginTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿σ╝ÇσÉ»µù╢Θù┤',
  `endTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿τ╗ôµ¥ƒµù╢Θù┤-σÅ»Φâ╜σ«óµê╖τ½»Φ┐ÿΘ£ÇΦªüµÿ╛τñ║',
  `closeTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿σà│Θù¡µù╢Θù┤-µ┤╗σè¿σ╜╗σ║òσà│Θù¡∩╝îσ«óµê╖τ½»Σ╕ìσåìµÿ╛τñ║',
  `json` text DEFAULT NULL COMMENT 'µ┤╗σè¿σà╖Σ╜ôΘàìτ╜«',
  `joinIn` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿σÅéΣ╕ÄΣ║║µò░',
  `winnerPid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µ┤╗σè¿Φ╡óσ«╢pid',
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
-- Table structure for table `arena_competitor`
--

CREATE TABLE `arena_competitor` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢ID',
  `rank` int(11) NOT NULL DEFAULT 0 COMMENT 'µÄÆσÉì',
  `lastRankTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΦ┐æΣ╕Çµ¼íµÄÆσÉìσÅÿµ¢┤µù╢Θù┤',
  `lastFightTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íµêÿµûùΣ║ïΣ╗╢,σìòΣ╜ìτºÆ,τö¿Σ║Äσå╖σì┤',
  `highestRank` int(11) NOT NULL DEFAULT 0 COMMENT 'σÄåσÅ▓µ£ÇΘ½ÿµÄÆσÉì',
  `winning` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┐₧Φâ£'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `arena_fight_record`
--

CREATE TABLE `arena_fight_record` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `atkPid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿΦÇàτÄ⌐σ«╢ID',
  `defPid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θÿ▓σ╛íΦÇàτÄ⌐σ«╢ID',
  `atkRank` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿΦÇàτÄ⌐σ«╢µÄÆσÉì',
  `defRank` int(11) NOT NULL DEFAULT 0 COMMENT 'Θÿ▓σ╛íΦÇàτÄ⌐σ«╢µÄÆσÉì',
  `beginTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùσ╝Çσºïµù╢Θù┤',
  `endTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùτ╗ôµ¥ƒµù╢Θù┤',
  `result` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿτ╗ôµ₧£,FightResult'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µêÿµèÑΦ«░σ╜òΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `character`
--

CREATE TABLE `character` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `char_id` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦºÆΦë▓id',
  `strengthen_0` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûτ¡ëτ║º',
  `strengthen_1` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûτ¡ëτ║º',
  `strengthen_2` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûτ¡ëτ║º',
  `strengthen_3` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûτ¡ëτ║º',
  `strengthen_4` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûτ¡ëτ║º',
  `strengthen_5` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûτ¡ëτ║º',
  `strengthen_6` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûτ¡ëτ║º',
  `strengthen_7` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûτ¡ëτ║º',
  `strengthen_8` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûτ¡ëτ║º',
  `gem_0` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│',
  `gem_1` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│',
  `gem_2` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│',
  `gem_3` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│',
  `gem_4` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│',
  `gem_5` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│',
  `gem_6` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│',
  `gem_7` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│',
  `gem_8` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│',
  `star_0` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `star_1` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `star_2` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `star_3` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `star_4` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `star_5` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `star_6` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `star_7` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `star_8` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `meridian` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ÅΦäë',
  `wing` int(11) NOT NULL DEFAULT 0 COMMENT 'τ┐àΦåÇ',
  `wingExp` int(11) NOT NULL DEFAULT 0 COMMENT 'τ┐àΦåÇσ╜ôσëìτ╗ÅΘ¬î',
  `skill_0` int(11) NOT NULL DEFAULT 0 COMMENT 'µèÇΦâ╜τ¡ëτ║º',
  `skill_1` int(11) NOT NULL DEFAULT 0 COMMENT 'µèÇΦâ╜τ¡ëτ║º',
  `skill_2` int(11) NOT NULL DEFAULT 0 COMMENT 'µèÇΦâ╜τ¡ëτ║º',
  `skill_3` int(11) NOT NULL DEFAULT 0 COMMENT 'µèÇΦâ╜τ¡ëτ║º',
  `skill_4` int(11) NOT NULL DEFAULT 0 COMMENT 'µèÇΦâ╜τ¡ëτ║º',
  `rebirth` int(11) NOT NULL DEFAULT 0 COMMENT 'Θçìτöƒτ¡ëτ║º',
  `rebirthExp` int(11) NOT NULL DEFAULT 0 COMMENT 'Θçìτöƒτ╗ÅΘ¬î',
  `artifice_0` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûσ╜ôσëìτ¡ëτ║º',
  `artifice_1` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûσ╜ôσëìτ¡ëτ║º',
  `artifice_2` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûσ╜ôσëìτ¡ëτ║º',
  `artifice_3` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûσ╜ôσëìτ¡ëτ║º',
  `artifice_4` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûσ╜ôσëìτ¡ëτ║º',
  `artifice_5` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûσ╜ôσëìτ¡ëτ║º',
  `artifice_6` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûσ╜ôσëìτ¡ëτ║º',
  `artifice_7` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûσ╜ôσëìτ¡ëτ║º',
  `artifice_8` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûσ╜ôσëìτ¡ëτ║º',
  `artificeMax_0` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ£Çσñºτ¡ëτ║º',
  `artificeMax_1` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ£Çσñºτ¡ëτ║º',
  `artificeMax_2` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ£Çσñºτ¡ëτ║º',
  `artificeMax_3` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ£Çσñºτ¡ëτ║º',
  `artificeMax_4` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ£Çσñºτ¡ëτ║º',
  `artificeMax_5` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ£Çσñºτ¡ëτ║º',
  `artificeMax_6` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ£Çσñºτ¡ëτ║º',
  `artificeMax_7` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ£Çσñºτ¡ëτ║º',
  `artificeMax_8` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ£Çσñºτ¡ëτ║º',
  `artificeTimes_0` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ¼íµò░',
  `artificeTimes_1` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ¼íµò░',
  `artificeTimes_2` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ¼íµò░',
  `artificeTimes_3` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ¼íµò░',
  `artificeTimes_4` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ¼íµò░',
  `artificeTimes_5` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ¼íµò░',
  `artificeTimes_6` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ¼íµò░',
  `artificeTimes_7` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ¼íµò░',
  `artificeTimes_8` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûµ¼íµò░'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `chatmessage`
--

CREATE TABLE `chatmessage` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `chatType` int(11) NOT NULL DEFAULT 0 COMMENT 'Φüèσñ⌐ΘóæΘüô[1πÇüΣ╕ûτòîΘóæΘüô 2πÇüσà¼Σ╝ÜΘóæΘüô 3πÇüτºüΦüèΘóæΘüô 4πÇüτ│╗τ╗ƒΘóæΘüô]',
  `receiveCId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà╢Σ╜ÖΦí¿τñ║τÄ⌐σ«╢pid[τÄ░σ£¿τÜäτÄ⌐σ«╢pidΘâ╜µÿ»σñºµò░σ¡ù]',
  `senderCId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüΦÇàCID',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φüèσñ⌐σåàσ«╣',
  `sendTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Φüèσñ⌐Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `clientdata`
--

CREATE TABLE `clientdata` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ID',
  `vstr_0` text DEFAULT NULL COMMENT 'Φç¬σ«ÜΣ╣ëσ¡ùµ«╡',
  `vstr_1` text DEFAULT NULL COMMENT 'Φç¬σ«ÜΣ╣ëσ¡ùµ«╡',
  `vstr_2` text DEFAULT NULL COMMENT 'Φç¬σ«ÜΣ╣ëσ¡ùµ«╡',
  `vstr_3` text DEFAULT NULL COMMENT 'Φç¬σ«ÜΣ╣ëσ¡ùµ«╡',
  `vstr_4` text DEFAULT NULL COMMENT 'Φç¬σ«ÜΣ╣ëσ¡ùµ«╡',
  `vstr_5` text DEFAULT NULL COMMENT 'Φç¬σ«ÜΣ╣ëσ¡ùµ«╡',
  `vstr_6` text DEFAULT NULL COMMENT 'Φç¬σ«ÜΣ╣ëσ¡ùµ«╡',
  `vstr_7` text DEFAULT NULL COMMENT 'Φç¬σ«ÜΣ╣ëσ¡ùµ«╡'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τö¿µê╖Φç¬σ«ÜΣ╣ëµò░µ«╡Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `clientguide`
--

CREATE TABLE `clientguide` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ID',
  `groupId` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗äid',
  `finishList` varchar(2000) NOT NULL DEFAULT '' COMMENT 'σ«îµêÉτÜäσ╝òσ»╝τ╗äid∩╝îσ╗║Φ««Θççτö¿τ╗äidσêùΦí¿τÜäσ║ÅσÅ╖µîëΣ╜ìσ¡ÿµö╛∩╝îΣ╜┐τö¿longσÇ╝σ¡ÿσé¿πÇéΦ»Ñσ¡ùµ«╡τö▒σ«óµê╖τ½»Σ╜┐τö¿Σ╕ÄΦºúµ₧É',
  `triggerList` varchar(2000) NOT NULL DEFAULT '' COMMENT 'σ«îµêÉτÜäσ╝òσ»╝τ╗äid∩╝îσ╗║Φ««Θççτö¿τ╗äidσêùΦí¿τÜäσ║ÅσÅ╖µîëΣ╜ìσ¡ÿµö╛∩╝îΣ╜┐τö¿longσÇ╝σ¡ÿσé¿πÇéΦ»Ñσ¡ùµ«╡τö▒σ«óµê╖τ½»Σ╜┐τö¿Σ╕ÄΦºúµ₧É'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µû░µëïσ╝òσ»╝µò░µì«σ¡ÿσé¿Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `confreward`
--

CREATE TABLE `confreward` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `rewardID` int(11) NOT NULL DEFAULT 0 COMMENT 'τñ╝σîàID',
  `price` int(11) NOT NULL DEFAULT 0 COMMENT 'τñ╝σîàΣ╗╖µá╝',
  `iconID` varchar(64) NOT NULL DEFAULT '' COMMENT 'τñ╝σîàσ¢╛µáç',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'τñ╝σîàσÉìτº░',
  `itemID` varchar(128) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüID',
  `itemCount` varchar(128) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüµò░ΘçÅ',
  `rewardDescribe` varchar(512) NOT NULL DEFAULT '' COMMENT 'τñ╝σîàµÅÅΦ┐░',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σê¢σ╗║µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σÉÄσÅ░Θàìτ╜«τñ╝σîà';

-- --------------------------------------------------------

--
-- Table structure for table `dailyactive`
--

CREATE TABLE `dailyactive` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '*τÄ⌐σ«╢ID',
  `value` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìµ┤╗Φ╖âσÇ╝',
  `teamLevel` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐σê╖µû░µù╢τÜäτÄ⌐σ«╢τ¡ëτ║º',
  `fetchedTaskIndex` varchar(500) NOT NULL DEFAULT '' COMMENT 'σ╖▓τ╗ÅΘóåσÅûσÑûσè▒τÜäσ║ÅσÅ╖'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Σ╗╗σèíµ┤╗Φ╖âσ║ªΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `dailyplayerrefresh`
--

CREATE TABLE `dailyplayerrefresh` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'ΦºÆΦë▓ID',
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
  `indexLastTime_39` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_40` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_41` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_42` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_43` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_44` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_45` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_46` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_47` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_48` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_49` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_50` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_51` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_52` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_53` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_54` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_55` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_56` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_57` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_58` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_59` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_60` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_61` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_62` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_63` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_64` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_65` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_66` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_67` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_68` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤',
  `indexLastTime_69` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µ»ÅµùÑσê╖µû░';

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
-- Table structure for table `dress`
--

CREATE TABLE `dress` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `dress_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µù╢Φúàid',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'µù╢Φúàτ▒╗σ₧ï',
  `char_id` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦúàσñçτÜäΦºÆΦë▓',
  `active_time` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┐Çµ┤╗µù╢Θù┤',
  `equip_time` int(11) NOT NULL DEFAULT 0 COMMENT 'τ⌐┐µê┤µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `droiyan`
--

CREATE TABLE `droiyan` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `point` int(11) NOT NULL DEFAULT 0 COMMENT 'σå│µêÿτº»σêå',
  `red` int(11) NOT NULL DEFAULT 0 COMMENT 'τ║óσÉìσÇ╝',
  `win_times` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┐₧Φâ£µ¼íµò░',
  `fighters_0` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Φ╖»Σ║║',
  `fighters_1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Φ╖»Σ║║',
  `fighters_2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Φ╖»Σ║║',
  `last_search_time` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇσÉÄµúÇτ┤óµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σå│µêÿτ│╗τ╗ƒ';

-- --------------------------------------------------------

--
-- Table structure for table `droiyan_record`
--

CREATE TABLE `droiyan_record` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `target` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Φó½µö╗σç╗τÄ⌐σ«╢',
  `target_name` varchar(50) NOT NULL DEFAULT '' COMMENT 'Φó½µö╗σç╗τÄ⌐σ«╢σÉìτº░',
  `result` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿτ╗ôµ₧£',
  `time` int(11) NOT NULL DEFAULT 0 COMMENT 'σÅæτöƒµù╢Θù┤',
  `point` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦÄ╖σ╛ùτº»σêå',
  `gold` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦÄ╖σ╛ùΘçæσ╕ü',
  `exp` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦÄ╖σ╛ùτ╗ÅΘ¬î',
  `rob` int(11) NOT NULL DEFAULT 0 COMMENT 'µèóσè½ΦÄ╖σ╛ùτë⌐σôü',
  `treasure` int(11) NOT NULL DEFAULT 0 COMMENT 'σ»╗σ«¥ΦÄ╖σ╛ùτë⌐σôü',
  `revenged` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'σ╖▓τ╗ÅσñìΣ╗ç'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σå│µêÿΦ«░σ╜ò';

-- --------------------------------------------------------

--
-- Table structure for table `droiyan_treasure`
--

CREATE TABLE `droiyan_treasure` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `treasure_id` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦÄ╖σ╛ùσ»åΣ┐¥',
  `gain_time` int(11) NOT NULL DEFAULT 0 COMMENT 'σÅæτöƒµù╢Θù┤',
  `expire_time` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┐çµ£ƒµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σå│µêÿΦ«░σ╜ò';

-- --------------------------------------------------------

--
-- Table structure for table `equip`
--

CREATE TABLE `equip` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `equip_id` int(11) NOT NULL DEFAULT 0 COMMENT 'Φúàσñçid',
  `attr_0` int(11) NOT NULL DEFAULT 0 COMMENT 'µ╡«σè¿σ▒₧µÇº',
  `attr_1` int(11) NOT NULL DEFAULT 0 COMMENT 'µ╡«σè¿σ▒₧µÇº',
  `attr_2` int(11) NOT NULL DEFAULT 0 COMMENT 'µ╡«σè¿σ▒₧µÇº',
  `attr_3` int(11) NOT NULL DEFAULT 0 COMMENT 'µ╡«σè¿σ▒₧µÇº',
  `attr_4` int(11) NOT NULL DEFAULT 0 COMMENT 'µ╡«σè¿σ▒₧µÇº',
  `attr_5` int(11) NOT NULL DEFAULT 0 COMMENT 'µ╡«σè¿σ▒₧µÇº',
  `pos` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦúàσñçΣ╜ìτ╜«',
  `char_id` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦúàσñçτÜäΦºÆΦë▓',
  `gain_time` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦÄ╖σÅûµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `fixedtimemail`
--

CREATE TABLE `fixedtimemail` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `mailId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'phpΘé«Σ╗╢ID',
  `senderName` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÅæΘÇüΦÇà',
  `title` varchar(500) NOT NULL DEFAULT '' COMMENT 'Θé«Σ╗╢µáçΘóÿ',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT 'Θé«Σ╗╢µÅÅΦ┐░',
  `uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüIdσêùΦí¿',
  `uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüµò░ΘçÅσêùΦí¿',
  `beginTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝Çσºïµù╢Θù┤',
  `hasSendNum` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╖▓σÅæµ¼íµò░',
  `cyclesNum` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╛¬τÄ»µ¼íµò░'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σÉÄσÅ░µÄ¿ΘÇüσ«Üµù╢Θé«Σ╗╢';

-- --------------------------------------------------------

--
-- Table structure for table `friend`
--

CREATE TABLE `friend` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢pid',
  `friendCId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σÑ╜σÅïτÄ⌐σ«╢pid',
  `isBlack` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªΦó½σèáσàÑΘ╗æσÉìσìò',
  `isDelete` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªΦó½τÄ⌐σ«╢σêáΘÖñ',
  `lastGiveTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΦ┐æΦ╡áΘÇüσÑ╜σÅïΣ╜ôσè¢µù╢Θù┤µê│[τºÆ]',
  `giveTimes` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ╡áΘÇüσÑ╜σÅïΣ╜ôσè¢µ¼íµò░',
  `lastRecvTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΦ┐æΘóåσÅûσÑ╜σÅïΣ╜ôσè¢µù╢Θù┤µê│[τºÆ]',
  `recvTimes` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘóåσÅûσÑ╜σÅïΣ╜ôσè¢µ¼íµò░',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÉΣ╕║σÑ╜σÅïµù╢Θù┤',
  `blackTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µïëΘ╗æσÑ╜σÅïµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σÑ╜σÅïΣ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `friendapply`
--

CREATE TABLE `friendapply` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢pid',
  `friendCId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σÑ╜σÅïτÄ⌐σ«╢pid',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT 'ΘÖäσèáσåàσ«╣',
  `applyTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τö│Φ»╖µù╢Θù┤µê│[τºÆ]'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σÑ╜σÅïτö│Φ»╖σêùΦí¿[σ¡ÿσé¿σèáσÑ╜σÅïτö│Φ»╖]';

-- --------------------------------------------------------

--
-- Table structure for table `globalmail`
--

CREATE TABLE `globalmail` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `senderName` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÅæΘÇüΦÇàσÉìτº░',
  `title` varchar(200) NOT NULL DEFAULT '' COMMENT 'Θé«Σ╗╢µáçΘóÿ',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT 'Θé«Σ╗╢µÅÅΦ┐░',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Θé«Σ╗╢σÅæΘÇüµù╢Θù┤(σìòΣ╜ìs)',
  `existTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Θé«Σ╗╢τöƒσ¡ÿµù╢Θù┤(s)',
  `pickUpExistTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ»╗σÅûΘóåσÅûσÉÄτöƒσ¡ÿµù╢Θù┤(σìòΣ╜ìs)',
  `uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüIdσêùΦí¿',
  `uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüµò░ΘçÅσêùΦí¿'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Φ«░σ╜ògmσÅæΘÇüτÜäσà¿µ£ìΘé«Σ╗╢Σ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `globalrichrisk`
--

CREATE TABLE `globalrichrisk` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `mapId` int(11) NOT NULL DEFAULT 0 COMMENT 'σ£░σ¢╛Id-RichRiskMapΦí¿ID',
  `chapter` int(11) NOT NULL DEFAULT 0 COMMENT 'σ£░σ¢╛τ½áΦèé',
  `width` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«╜σ║ª',
  `height` int(11) NOT NULL DEFAULT 0 COMMENT 'Θ½ÿσ║ª',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σê¢σ╗║µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σñºσ»îτ┐üσ£░σ¢╛Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `guild`
--

CREATE TABLE `guild` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╕«Σ╝Üτ¡ëτ║º',
  `exp` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╕«Σ╝Üτ╗ÅΘ¬î',
  `historyExp` int(11) NOT NULL DEFAULT 0 COMMENT 'σÄåσÅ▓σ╕«Σ╝Üτ╗ÅΘ¬î',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT 'σÉìτº░',
  `icon` int(11) NOT NULL DEFAULT 0 COMMENT 'σñ┤σâÅ',
  `border` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ╛╣µíå',
  `notice` varchar(500) NOT NULL DEFAULT '' COMMENT 'σà¼σæè',
  `manifesto` varchar(500) NOT NULL DEFAULT '' COMMENT 'σ«úΦ¿Ç',
  `joinState` int(11) NOT NULL DEFAULT 0 COMMENT 'σèáσàÑτè╢µÇü',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σê¢σ╗║µù╢Θù┤',
  `lastLoginTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σà¼Σ╝ÜτÄ⌐σ«╢µ£ÇΦ┐æΣ╕Çµ¼íτÖ╗σ╜òµù╢Θù┤',
  `levelLimit` int(11) NOT NULL DEFAULT 0 COMMENT 'σèáσàÑτ¡ëτ║ºΘÖÉσê╢',
  `maxFightPower` int(11) NOT NULL DEFAULT 0 COMMENT 'σÄåσÅ▓µ£Çσñºµêÿµûùσè¢',
  `donate` int(11) NOT NULL DEFAULT 0 COMMENT 'σÄåσÅ▓µ£ÇσñºΦ┤íτî«σ║ª',
  `guildbossLevel` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇσñºΘÇÜσà│σ╕«µ┤╛σë»µ£¼τ¡ëτ║º',
  `guildbossOpenNum` int(11) NOT NULL DEFAULT 0 COMMENT 'µ»ÅµùÑσ╕«µ┤╛σë»µ£¼σ╝ÇσÉ»µò░',
  `lnlevel` int(11) NOT NULL DEFAULT 0 COMMENT 'Θ╛ÖσÑ│τ¡ëτ║º',
  `lnexp` int(11) NOT NULL DEFAULT 0 COMMENT 'Θ╛ÖσÑ│τ╗ÅΘ¬î',
  `lnwarLevel` int(11) NOT NULL DEFAULT 0 COMMENT 'Θ╛ÖσÑ│µêÿσè¢µíúµ¼í'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σà¼Σ╝ÜΣ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `guildapply`
--

CREATE TABLE `guildapply` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢ID',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢ID',
  `applyTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τö│Φ»╖µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σà¼Σ╝Üτö│Φ»╖σêùΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `guildboard`
--

CREATE TABLE `guildboard` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τòÖΦ¿ÇτÄ⌐σ«╢id',
  `name` varchar(500) NOT NULL DEFAULT '' COMMENT 'τÄ⌐σ«╢σÉìτº░',
  `job` int(11) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ΦüîΣ╜ì',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σ╕«Σ╝ÜID',
  `message` varchar(500) NOT NULL DEFAULT '' COMMENT 'τòÖΦ¿Çσåàσ«╣',
  `messageTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τòÖΦ¿Çµù╢Θù┤',
  `top` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'τ╜«Θí╢'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τ½₧µèÇσ£║Σ┐íµü»Φí¿,Φ«░σ╜òµ»ÅΣ╕¬τÄ⌐σ«╢τ½₧µèÇσ£║τ¢╕σà│Σ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `guildboss`
--

CREATE TABLE `guildboss` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σ╕«µ┤╛Id',
  `bossId` int(11) NOT NULL DEFAULT 0 COMMENT 'Bossσà│σìíID(GuildBossΘ¥ÖµÇüΦí¿ID)',
  `bossHp` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossσ╜ôσëìτöƒσæ╜(σñìµ┤╗µù╢σê╖µû░)',
  `bossMaxHp` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossµ£Çσñºτöƒσæ╜(σñìµ┤╗µù╢σê╖µû░)',
  `isDead` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Bossµÿ»σÉªσ╖▓µ¡╗Σ║í',
  `isOpen` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Bossµÿ»σÉªσ╖▓σ╝ÇσÉ»',
  `deadTime` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossµ¡╗Σ║íµù╢Θù┤',
  `reviveTime` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossσñìµ┤╗µù╢Θù┤',
  `lastKillCid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσç╗µ¥ÇΦÇàpid'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σ╕«µ┤╛bossΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `guildbosschallenge`
--

CREATE TABLE `guildbosschallenge` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `challengeTimes` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengeMaxTimes` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ£Çσñºµ¼íµò░',
  `challengeBuyTimes` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┤¡Σ╣░µîæµêÿµ¼íµò░',
  `totalDamage_0` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_4` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_5` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_6` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_7` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_8` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_9` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_10` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_11` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_12` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_13` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_14` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_15` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_16` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_17` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_18` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_19` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_20` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_21` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_22` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_23` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_24` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_25` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_26` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_27` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_28` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_29` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_0` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_4` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_5` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_6` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_7` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_8` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_9` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_10` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_11` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_12` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_13` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_14` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_15` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_16` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_17` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_18` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_19` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_20` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_21` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_22` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_23` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_24` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_25` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_26` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_27` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_28` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_29` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `beginFightTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢σ╝Çσºïµîæµêÿbossµù╢Θù┤,τö¿Σ║ÄΦ«íτ«ùτÄ⌐σ«╢µêÿµûùµù╢Θù┤',
  `leaveFightTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ΘÇÇσç║bossµù╢Θù┤,τö¿Σ║ÄΦ«íτ«ùτÄ⌐σ«╢µîæµêÿCDµù╢Θù┤',
  `attackTimes` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╗èµùÑµö╗σç╗µ¼íµò░'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢µîæµêÿσ╕«µ┤╛BossΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `guildlog`
--

CREATE TABLE `guildlog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σ╕«Σ╝Üid',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùτ▒╗σ₧ï',
  `param_0` varchar(50) NOT NULL DEFAULT '' COMMENT 'σÅéµò░',
  `param_1` varchar(50) NOT NULL DEFAULT '' COMMENT 'σÅéµò░',
  `param_2` varchar(50) NOT NULL DEFAULT '' COMMENT 'σÅéµò░',
  `param_3` varchar(50) NOT NULL DEFAULT '' COMMENT 'σÅéµò░',
  `param_4` varchar(50) NOT NULL DEFAULT '' COMMENT 'σÅéµò░',
  `time` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τ½₧µèÇσ£║Σ┐íµü»Φí¿,Φ«░σ╜òµ»ÅΣ╕¬τÄ⌐σ«╢τ½₧µèÇσ£║τ¢╕σà│Σ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `guildmember`
--

CREATE TABLE `guildmember` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σ╕«Σ╝ÜId',
  `donate` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┤íτî«σÇ╝',
  `weekDonate` int(11) NOT NULL DEFAULT 0 COMMENT 'σæ¿Φ┤íτî«σÇ╝',
  `historyDonate` int(11) NOT NULL DEFAULT 0 COMMENT 'σÄåσÅ▓Φ┤íτî«σÇ╝',
  `job` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╕«Σ╝ÜΦüîσèí',
  `lastLeaveTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èΣ╕Çµ¼íΘÇÇσç║µù╢Θù┤',
  `joinTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σèáσàÑµù╢Θù┤',
  `sacrificeDonate` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐τî«τÑ¡Φ┤íτî«σÇ╝',
  `sacrificeStatus` int(11) NOT NULL DEFAULT 0 COMMENT 'τÑ¡σñ⌐τè╢µÇü',
  `skillpoints` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╕«µ┤╛µèÇΦâ╜τé╣',
  `guildwarInspire` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╕«µ┤╛µêÿΘ╝ôΦê₧µ¼íµò░',
  `longnvPickReward` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«êσì½Θ╛ÖσÑ│ΘóåσÑûµ¼íµò░'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σ╕«Σ╝ÜµêÉσæÿΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `guildmemberskill`
--

CREATE TABLE `guildmemberskill` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `skillId` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╕«Σ╝ÜµèÇΦâ╜Θ¥ÖµÇüId',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╕«Σ╝ÜµèÇΦâ╜τ¡ëτ║º',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σê¢σ╗║µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σ╕«Σ╝ÜµêÉσæÿµèÇΦâ╜Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `guildtasklog`
--

CREATE TABLE `guildtasklog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `taskId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Σ╗╗σèíτÜäsid',
  `logTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤',
  `action` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╗╗σèíσè¿Σ╜£',
  `itemId` int(11) NOT NULL DEFAULT 0 COMMENT 'τë⌐σôüid',
  `itemCount` int(11) NOT NULL DEFAULT 0 COMMENT 'τë⌐σôüµò░ΘçÅ',
  `isDouble` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'σÅîσÇì'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τ½₧µèÇσ£║Σ┐íµü»Φí¿,Φ«░σ╜òµ»ÅΣ╕¬τÄ⌐σ«╢τ½₧µèÇσ£║τ¢╕σà│Σ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `guildwarapply`
--

CREATE TABLE `guildwarapply` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà¼Σ╝ÜID',
  `centerId` int(11) NOT NULL DEFAULT 0 COMMENT 'µì«τé╣id',
  `applyTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τö│Φ»╖µù╢Θù┤',
  `winCenterId` int(11) NOT NULL DEFAULT 0 COMMENT 'σìáΘóåµì«τé╣Id'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σà¼Σ╝ÜµèÑσÉìΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `guildwarguildresult`
--

CREATE TABLE `guildwarguildresult` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `atkGuildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà¼Σ╝ÜID',
  `defGuildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà¼Σ╝ÜID',
  `result` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ôµ₧£',
  `centerId` int(11) NOT NULL DEFAULT 0 COMMENT 'µì«τé╣id',
  `fightTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σà¼Σ╝Üµêÿ';

-- --------------------------------------------------------

--
-- Table structure for table `guildwarpuppet`
--

CREATE TABLE `guildwarpuppet` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ID',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà¼Σ╝ÜID',
  `puppetId` int(11) NOT NULL DEFAULT 0 COMMENT 'σéÇσäíID',
  `applyTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τö│Φ»╖µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢σéÇσäíΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `guildwarresult`
--

CREATE TABLE `guildwarresult` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `atkpid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà¼Σ╝ÜID',
  `defpid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà¼Σ╝ÜID',
  `result` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ôµ₧£',
  `centerId` int(11) NOT NULL DEFAULT 0 COMMENT 'µì«τé╣id',
  `fightTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σà¼Σ╝Üµêÿ';

-- --------------------------------------------------------

--
-- Table structure for table `guild_rank_record`
--

CREATE TABLE `guild_rank_record` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'µÄÆΦíîµª£τ▒╗σ₧ï',
  `guildid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σ╕«µ┤╛id',
  `owner` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧ΦÇàid',
  `value` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σÇ╝',
  `updateTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ¢┤µû░µù╢Θù┤',
  `ext1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡1',
  `ext2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡2',
  `ext3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡3',
  `ext4` varchar(200) NOT NULL DEFAULT '' COMMENT 'µë⌐σ▒òσ¡ùµ«╡4',
  `ext5` varchar(200) NOT NULL DEFAULT '' COMMENT 'µë⌐σ▒òσ¡ùµ«╡5'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µÄÆΦíîΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `instance_info`
--

CREATE TABLE `instance_info` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢ID',
  `instanceMaxLevel_0` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `instanceMaxLevel_1` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `instanceMaxLevel_2` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `instanceMaxLevel_3` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `instanceMaxLevel_4` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `instanceMaxLevel_5` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `instanceMaxLevel_6` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `instanceMaxLevel_7` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `instanceMaxLevel_8` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `instanceMaxLevel_9` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΘ½ÿσë»µ£¼σà│σìíτ¡ëτ║º',
  `challengTimes_0` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengTimes_1` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengTimes_2` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengTimes_3` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengTimes_4` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengTimes_5` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengTimes_6` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengTimes_7` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengTimes_8` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `challengTimes_9` int(11) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Φúàσñçσë»µ£¼Σ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢ID',
  `itemId` int(11) NOT NULL DEFAULT 0 COMMENT 'Θüôσà╖ID',
  `count` int(11) NOT NULL DEFAULT 0 COMMENT 'Θüôσà╖µò░ΘçÅ',
  `gainTime` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦÄ╖σ╛ùµù╢Θù┤,σìòΣ╜ìτºÆ'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Θüôσà╖Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `lingbao`
--

CREATE TABLE `lingbao` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'τü╡σ«¥τ¡ëτ║º',
  `exp` int(11) NOT NULL DEFAULT 0 COMMENT 'τü╡σ«¥τ╗ÅΘ¬î'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τü╡σ«¥Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `longnvapply`
--

CREATE TABLE `longnvapply` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σ╕«Σ╝ÜId',
  `applyTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τö│Φ»╖µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σ╕«Σ╝ÜµêÉσæÿΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `longnvresult`
--

CREATE TABLE `longnvresult` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `atkpid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µö╗σç╗ΦÇàID',
  `defpid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θÿ▓σ╛íΦÇàID',
  `result` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ôµ₧£',
  `fightTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σà¼Σ╝Üµêÿ';

-- --------------------------------------------------------

--
-- Table structure for table `longnvwarpuppet`
--

CREATE TABLE `longnvwarpuppet` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ID',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà¼Σ╝ÜID',
  `puppetId` int(11) NOT NULL DEFAULT 0 COMMENT 'σéÇσäíID',
  `applyTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τö│Φ»╖µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢σéÇσäíΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `longnvwarresult`
--

CREATE TABLE `longnvwarresult` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `guildId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà¼Σ╝ÜID',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'Θ╛ÖσÑ│τ¡ëτ║º',
  `result` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ôµ₧£',
  `challengeTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«úµêÿµù╢Θù┤',
  `fightTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σà¼Σ╝Üµêÿ';

-- --------------------------------------------------------

--
-- Table structure for table `lottery`
--

CREATE TABLE `lottery` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢pid',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ƒµò░',
  `number` int(11) NOT NULL DEFAULT 0 COMMENT 'σÅ╖τáü',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'τºìτ▒╗',
  `buyday` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┤¡Σ╣░µùÑµ£ƒ',
  `rewardday` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕¡σÑûµùÑµ£ƒ'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Σ╕ÇσàâΦ┤¡Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `marry`
--

CREATE TABLE `marry` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `sex` int(11) NOT NULL DEFAULT 0 COMMENT 'µÇºσê½',
  `married` int(11) NOT NULL DEFAULT 0 COMMENT 'σ⌐Üµüïτè╢µÇü',
  `lover_pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Σ╝┤Σ╛úpid',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'µü⌐τê▒τ¡ëτ║º',
  `exp` int(11) NOT NULL DEFAULT 0 COMMENT 'µü⌐τê▒τ╗ÅΘ¬î',
  `signin` int(11) NOT NULL DEFAULT 0 COMMENT 'τ¡╛σê░σñ⌐µò░',
  `isSign` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐µÿ»σÉªτ¡╛σê░',
  `signReward` varchar(500) NOT NULL DEFAULT '' COMMENT 'τ¡╛σê░σ╖▓ΘóåσÑûσè▒',
  `levelReward` varchar(500) NOT NULL DEFAULT '' COMMENT 'µü⌐τê▒τ¡ëτ║ºσ╖▓ΘóåσÑûσè▒'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σ⌐Üµüïτ│╗τ╗ƒ';

-- --------------------------------------------------------

--
-- Table structure for table `marry_apply`
--

CREATE TABLE `marry_apply` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `from_pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µ¥ÑΦç¬τÄ⌐σ«╢id',
  `apply_time` int(11) NOT NULL DEFAULT 0 COMMENT 'τö│Φ»╖µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σ⌐Üµüïτ│╗τ╗ƒ';

-- --------------------------------------------------------

--
-- Table structure for table `marry_divorce_apply`
--

CREATE TABLE `marry_divorce_apply` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `from_pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µ¥ÑΦç¬τÄ⌐σ«╢id',
  `apply_time` int(11) NOT NULL DEFAULT 0 COMMENT 'τö│Φ»╖µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τª╗σ⌐Üτö│Φ»╖';

-- --------------------------------------------------------

--
-- Table structure for table `newtitle`
--

CREATE TABLE `newtitle` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `title_id` int(11) NOT NULL DEFAULT 0 COMMENT 'τº░σÅ╖id',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'τ¡ëτ║º',
  `is_using` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµ¡úσ£¿Σ╜┐τö¿',
  `active_time` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┐Çµ┤╗µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

CREATE TABLE `player` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `open_id` varchar(50) NOT NULL DEFAULT '' COMMENT 'τÄ⌐σ«╢Φ┤ªσÅ╖ID',
  `sid` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿id',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT 'τÄ⌐σ«╢σÉìτº░/µÿ╡τº░',
  `icon` int(11) NOT NULL DEFAULT 0 COMMENT 'σñ┤σâÅ',
  `maxFightPower` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£Çσñºµêÿµûùσè¢',
  `gmLevel` int(11) NOT NULL DEFAULT 0 COMMENT 'gmµ¥âΘÖÉ',
  `cheatTimes` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜£σ╝èµ¼íµò░',
  `banned_ChatExpiredTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τªüΦ¿ÇΦ┐çµ£ƒµù╢Θù┤',
  `banned_LoginExpiredTime` int(11) NOT NULL DEFAULT 0 COMMENT 'τªüτÖ╗Φ┐çµ£ƒµù╢Θù┤',
  `bannedTimes` int(11) NOT NULL DEFAULT 0 COMMENT 'σ░üσÅ╖µ¼íµò░',
  `lastLogin` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΦ┐æΣ╕Çµ¼íτÖ╗ΘÖåµÖéΘûô',
  `lastLogout` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΦ┐æΣ╕Çµ¼íτÖ╗σç║µÖéΘûô',
  `online_update` int(11) NOT NULL DEFAULT 0 COMMENT 'σ£¿τ║┐µ¢┤µû░µù╢Θù┤',
  `totalRecharge` int(11) NOT NULL DEFAULT 10 COMMENT 'τ┤»Φ«íσààσÇ╝, τ╗ƒΦ«íτÄ⌐σ«╢σààσÇ╝RMB',
  `vipExp` int(11) NOT NULL DEFAULT 146 COMMENT 'vipτ╗ÅΘ¬î',
  `vipLevel` int(11) NOT NULL DEFAULT 3 COMMENT 'vipτ¡ëτ║º',
  `lv` int(11) NOT NULL DEFAULT 0 COMMENT 'τ¡ëτ║º',
  `exp` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ╗ÅΘ¬î',
  `crystal` int(11) NOT NULL DEFAULT 0 COMMENT 'µ░┤µÖ╢µò░ΘçÅ',
  `gold` bigint(20) NOT NULL DEFAULT 5000 COMMENT 'Θçæσ╕ü',
  `dungeon_lv` int(11) NOT NULL DEFAULT 0 COMMENT 'σë»µ£¼τ¡ëτ║º',
  `dungeon_time` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íµëôσë»µ£¼µù╢Θù┤',
  `ext_package` int(11) NOT NULL DEFAULT 0 COMMENT 'Θó¥σñûΦâîσîàσ«╣ΘçÅ',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┤ªσÅ╖σê¢σ╗║µù╢Θù┤,σìòΣ╜ìτºÆ',
  `GmMailCheckId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'GmΘé«Σ╗╢ΘóåσÅûτ╝ûσÅ╖',
  `strengthen_material` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╝║σîûµ¥ÉµûÖ',
  `gem_material` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│µ¥ÉµûÖ',
  `star_material` int(11) NOT NULL DEFAULT 0 COMMENT 'σìçµÿƒµ¥ÉµûÖ',
  `mer_material` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ÅΦäëµ¥ÉµûÖ',
  `wing_material` int(11) NOT NULL DEFAULT 0 COMMENT 'τ┐àΦåÇµ¥ÉµûÖ',
  `arena_token` int(11) NOT NULL DEFAULT 0 COMMENT 'τ½₧µèÇσ£║Σ╗úσ╕ü',
  `equip_instance_material` int(11) NOT NULL DEFAULT 0 COMMENT 'Φúàσñçσë»µ£¼Σ╗úσ╕ü',
  `gem_instance_material` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«¥τƒ│σë»µ£¼Σ╗úσ╕ü',
  `meridian_instance_material` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ÅΦäëσë»µ£¼Σ╗úσ╕ü',
  `red_piece` int(11) NOT NULL DEFAULT 0 COMMENT 'τ║óΦúàτóÄτëç',
  `artifice_material` int(11) NOT NULL DEFAULT 0 COMMENT 'τé╝σîûτƒ│',
  `lottery` int(11) NOT NULL DEFAULT 0 COMMENT 'σÑûσê╕',
  `warspirit_talent_material` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿτü╡σñ⌐Φ╡ïµ¥ÉµûÖ',
  `warspirit_lv_material` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿτü╡τ¡ëτ║ºµ¥ÉµûÖ',
  `warspirit_lv` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿτü╡τ¡ëτ║º',
  `warspirit_exp` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿτü╡τ╗ÅΘ¬î',
  `warspirit_talent` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿτü╡σñ⌐Φ╡ï',
  `dress_material` int(11) NOT NULL DEFAULT 0 COMMENT 'µù╢Φúàµ¥ÉµûÖ',
  `exp_material` int(11) NOT NULL DEFAULT 0 COMMENT 'τ╗ÅΘ¬îµ¥ÉµûÖ'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `playericon`
--

CREATE TABLE `playericon` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '*µëÇσ▒₧τÄ⌐σ«╢ID',
  `refCard` int(11) NOT NULL DEFAULT 0 COMMENT 'σìíτëîID',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT '2-Φï▒Θ¢äσñ┤σâÅ,3-ΦºëΘåÆσñ┤σâÅ'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢σñ┤σâÅΦí¿,µîëΦí¿τÜäidσ¡ùµ«╡µÄÆσ║Å';

-- --------------------------------------------------------

--
-- Table structure for table `playericonborder`
--

CREATE TABLE `playericonborder` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '*µëÇσ▒₧τÄ⌐σ«╢ID',
  `refID` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ╛╣µíåID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢σñ┤σâÅΦ╛╣µíå';

-- --------------------------------------------------------

--
-- Table structure for table `playermail`
--

CREATE TABLE `playermail` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ID',
  `senderName` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÅæΘÇüΦÇàσÉìτº░',
  `title` varchar(200) NOT NULL DEFAULT '' COMMENT 'Θé«Σ╗╢µáçΘóÿ',
  `content` varchar(1000) NOT NULL DEFAULT '' COMMENT 'Θé«Σ╗╢µÅÅΦ┐░',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Θé«Σ╗╢σÅæΘÇüµù╢Θù┤(σìòΣ╜ìs)',
  `existTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Θé«Σ╗╢τöƒσ¡ÿµù╢Θù┤(s)',
  `pickUpTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ»╗σÅûΘóåσÅûµù╢Θù┤(σìòΣ╜ìs)',
  `pickUpExistTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ»╗σÅûΘóåσÅûσÉÄτöƒσ¡ÿµù╢Θù┤(σìòΣ╜ìs)',
  `uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüIdσêùΦí¿',
  `uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT 'τë⌐σôüµò░ΘçÅσêùΦí¿',
  `globalMailID` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σà¿Σ╜ôΘé«Σ╗╢τÜäID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Θé«Σ╗╢Σ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `playerrechargeinfo`
--

CREATE TABLE `playerrechargeinfo` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ΦºÆΦë▓id',
  `monthCardRemains` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£êσìíσë⌐Σ╜Öσñ⌐µò░',
  `permantCardRemains` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╣┤σìíσë⌐Σ╜Öσñ⌐µò░'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σààσÇ╝Σ┐íµü»µ▒çµÇ╗';

-- --------------------------------------------------------

--
-- Table structure for table `playerrechargerecord`
--

CREATE TABLE `playerrechargerecord` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ΦºÆΦë▓id',
  `goodsID` varchar(500) NOT NULL DEFAULT '' COMMENT 'σòåσôüid',
  `lastBuyTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΦ┐æΣ╕Çµ¼íΦ┤¡Σ╣░µù╢Θù┤',
  `buyCount` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ┤¡Σ╣░µ¼íµò░',
  `resetSign` varchar(500) NOT NULL DEFAULT '' COMMENT 'Θçìτ╜«µáçΦ»å'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢σ£░σ«½Φ«░σ╜ò';

-- --------------------------------------------------------

--
-- Table structure for table `playerrecord`
--

CREATE TABLE `playerrecord` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'ΦºÆΦë▓ID',
  `value_0` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_1` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_2` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_3` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_4` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_5` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_6` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_7` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_8` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_9` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_10` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_11` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_12` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_13` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_14` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_15` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_16` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_17` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_18` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_19` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_20` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_21` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_22` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_23` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_24` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_25` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_26` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_27` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_28` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_29` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_30` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_31` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_32` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_33` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_34` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_35` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_36` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_37` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_38` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_39` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_40` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_41` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_42` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_43` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_44` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_45` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_46` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_47` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_48` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_49` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_50` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_51` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_52` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_53` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_54` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_55` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_56` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_57` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_58` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `value_59` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉäΘí╣Φ«░σ╜òσÇ╝',
  `useItemTimes_0` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_1` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_2` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_3` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_4` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_5` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_6` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_7` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_8` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_9` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_10` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_11` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_12` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_13` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_14` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_15` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_16` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_17` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_18` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_19` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_20` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_21` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_22` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_23` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_24` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_25` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_26` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_27` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_28` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `useItemTimes_29` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╜┐τö¿τë⌐σôüµ¼íµò░[µá╣µì«τë⌐σôüΣ╜┐τö¿τ▒╗σ₧ïµ¥Ñσî║σêå∩╝îΣ╕╗Φªüτö¿Σ║Äσ╝Çσ«¥τ«▒µù╢µá╣µì«Σ╕ìσÉîτÜäσ╝ÇσÉ»µ¼íµò░µ¥ÑσÅûΘÜÅµ£║σÇ╝]',
  `lastUseBoxTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΦ┐æσ╝ÇσÉ»σ«¥τ«▒µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µ»ÅµùÑσê╖µû░';

-- --------------------------------------------------------

--
-- Table structure for table `playersetting`
--

CREATE TABLE `playersetting` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '*µëÇσ▒₧τÄ⌐σ«╢ID',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ«╛τ╜«τ▒╗σ₧ï,σ»╣σ║öµ₧ÜΣ╕╛τ▒╗σ₧ïProtoEnum.SettingType',
  `value` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ«╛τ╜«σ»╣σ║öσÇ╝',
  `lastChangeTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇΦ┐æΣ╕Çµ¼íΣ┐«µö╣µù╢Θù┤,σìòΣ╜ìτºÆ'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Φ«╛τ╜«Φí¿,µîëΦí¿τÜäidσ¡ùµ«╡µÄÆσ║Å';

-- --------------------------------------------------------

--
-- Table structure for table `player_find_treasure`
--

CREATE TABLE `player_find_treasure` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `total` int(11) NOT NULL DEFAULT 0 COMMENT 'τ┤»Φ«íσ»╗σ«¥µ¼íµò░',
  `times` int(11) NOT NULL DEFAULT 0 COMMENT 'µ»ÅµùÑσìòµè╜σ»╗σ«¥µ¼íµò░',
  `tentimes` int(11) NOT NULL DEFAULT 0 COMMENT 'µ»ÅµùÑσìüΦ┐₧µè╜σ»╗σ«¥µ¼íµò░',
  `warspiritTotal` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿτü╡τ┤»Φ«íσ»╗σ«¥µ¼íµò░'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢σ»╗σ«¥Σ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `player_goods`
--

CREATE TABLE `player_goods` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ID',
  `goods_id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σòåσôüΦ╡äµ║ÉID(Σ╕ÇΦê¼σòåσôü:RefGoodsInfoΦí¿ID,ΘÖÉµù╢σòåσôü:LimitedGoodsΦí¿Σ╕╗Θö«,σà¼Σ╝Üσòåσôü:GuildGoodsΦí¿Σ╕╗Θö«)',
  `store_id` int(11) NOT NULL DEFAULT 0 COMMENT 'σòåσ║ùID',
  `store_type` int(11) NOT NULL DEFAULT 0 COMMENT 'σòåσ║ùτ▒╗σ₧ï',
  `cost_uniform_id` varchar(64) NOT NULL DEFAULT '' COMMENT 'µ╢êΦ┤╣Σ╗úσ╕üUniformId',
  `price` varchar(64) NOT NULL DEFAULT '' COMMENT 'σòåσôüσÄƒΣ╗╖',
  `discount` varchar(64) NOT NULL DEFAULT '' COMMENT 'µèÿσÉÄΣ╗╖µá╝',
  `amount` int(11) NOT NULL DEFAULT 0 COMMENT 'σìòµ¼íΦ┤¡Σ╣░µò░ΘçÅ',
  `buy_times` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╖▓Φ┤¡Σ╣░µ¼íµò░',
  `total_buy_times` int(11) NOT NULL DEFAULT 0 COMMENT 'µÇ╗Φ┤¡Σ╣░µ¼íµò░',
  `soldout` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªσ╖▓σìûσç║',
  `createTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σê¢σ╗║µù╢Θù┤',
  `level` int(11) NOT NULL DEFAULT 0 COMMENT 'σà│σìíID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢σòåσôüΣ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `rankinfo`
--

CREATE TABLE `rankinfo` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `aid` int(11) NOT NULL DEFAULT 0 COMMENT 'Φ«░σ╜òµò░µì«ID[σ»╣σ║öΣ║ÄRankSystemTypeµ₧ÜΣ╕╛σÇ╝]',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '*µëÇσ▒₧τÄ⌐σ«╢ID',
  `value` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µò░σÇ╝',
  `updateTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ¢┤µû░µù╢Θù┤',
  `ext1` int(11) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡1',
  `ext2` int(11) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡2',
  `ext3` varchar(200) NOT NULL DEFAULT '' COMMENT 'µë⌐σ▒òσ¡ùµ«╡3',
  `ext4` varchar(200) NOT NULL DEFAULT '' COMMENT 'µë⌐σ▒òσ¡ùµ«╡4',
  `ext5` int(11) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡5'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µÄÆΦíîΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `rank_record`
--

CREATE TABLE `rank_record` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT 'µÄÆΦíîµª£τ▒╗σ₧ï',
  `owner` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧ΦÇàid',
  `value` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σÇ╝',
  `updateTime` int(11) NOT NULL DEFAULT 0 COMMENT 'µ¢┤µû░µù╢Θù┤',
  `ext1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡1',
  `ext2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡2',
  `ext3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µë⌐σ▒òσ¡ùµ«╡3',
  `ext4` varchar(200) NOT NULL DEFAULT '' COMMENT 'µë⌐σ▒òσ¡ùµ«╡4',
  `ext5` varchar(200) NOT NULL DEFAULT '' COMMENT 'µë⌐σ▒òσ¡ùµ«╡5'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µÄÆΦíîΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `rechargeorder`
--

CREATE TABLE `rechargeorder` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ΦºÆΦë▓id',
  `open_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'σ╣│σÅ░µ£ìσèíσÖ¿Σ╕èτöƒµêÉτÜäopenid',
  `productid` varchar(500) NOT NULL DEFAULT '' COMMENT 'σòåσôüid',
  `quantity` int(11) NOT NULL DEFAULT 0 COMMENT 'σòåσôüid',
  `orderTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σê░µ╕╕µêÅµ£ìσèíσÖ¿Σ╕èµù╢Θù┤',
  `deliverTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σê░µ╕╕µêÅµ£ìσèíσÖ¿Σ╕èµù╢Θù┤',
  `cporderid` varchar(100) NOT NULL DEFAULT '' COMMENT 'cpσ«ÜσìòσÅ╖,phpµ¡úσ╝ÅτÜä∩╝îσö»Σ╕Ç',
  `adfromOrderid` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ╕áΘüôµö»Σ╗ÿσ«ÜσìòσÅ╖',
  `serverID` varchar(500) NOT NULL DEFAULT '' COMMENT 'µ£ìσèíσÖ¿ID',
  `carrier` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φ┐ÉΦÉÑσòå',
  `platform` varchar(500) NOT NULL DEFAULT '' COMMENT 'σ╣│σÅ░ios∩╝îandriod∩╝îΦ╢èτï▒',
  `adfrom` varchar(500) NOT NULL DEFAULT '' COMMENT 'pp∩╝î360∩╝îqq Σ╕╗µ¥Ñµ║É1',
  `adfrom2` varchar(500) NOT NULL DEFAULT '' COMMENT 'Σ║îτ║ºµ¥Ñµ║É',
  `gameid` varchar(500) NOT NULL DEFAULT '' COMMENT 'µóªσèáσåàΘâ¿µ╕╕µêÅid',
  `appID` varchar(500) NOT NULL DEFAULT '' COMMENT 'appID',
  `status` varchar(500) NOT NULL DEFAULT '' COMMENT 'Φ«óσìòτè╢µÇü-σ╖▓Σ╗ÿµ¼╛∩╝îσ╖▓ΘóåσÅû∩╝îσ╖▓σÅûµ╢ê'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢σ£░σ«½Φ«░σ╜ò';

-- --------------------------------------------------------

--
-- Table structure for table `rechargereset`
--

CREATE TABLE `rechargereset` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `goodsid` varchar(100) NOT NULL DEFAULT '' COMMENT 'σòåσôüid',
  `resetSign` varchar(500) NOT NULL DEFAULT '' COMMENT 'Θçìτ╜«τ¡╛σÉì'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢σñºσ»îτ┐üΣ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `red_packet`
--

CREATE TABLE `red_packet` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `left_money` int(11) NOT NULL DEFAULT 0 COMMENT 'σë⌐Σ╜ÖΘçæΘó¥',
  `max_money` int(11) NOT NULL DEFAULT 0 COMMENT 'µÇ╗ΘçæΘó¥',
  `already_pick` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╖▓ΘóåσÅûΣ║║µò░',
  `max_pick` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ÇσñºΘóåσÅûΣ║║µò░',
  `time` int(11) NOT NULL DEFAULT 0 COMMENT 'τ║óσîàτöƒµêÉµù╢Θù┤',
  `packet_type_id` int(11) NOT NULL DEFAULT 0 COMMENT 'τ║óσîàτºìτ▒╗id',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘóåσÅûτè╢µÇü'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τ║óσîàΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `red_packet_pick`
--

CREATE TABLE `red_packet_pick` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `packet_id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τ║óσîàid',
  `money` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘóåσÅûΘçæΘó¥',
  `time` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘóåσÅûµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τ║óσîàΘóåσÅûΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `richriskavatar`
--

CREATE TABLE `richriskavatar` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `avatarPid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Φ╖»Θ£╕τÄ⌐σ«╢pid',
  `x` int(11) NOT NULL DEFAULT 0 COMMENT 'µëÇσ£¿σ¥ÉµáçτÜäx',
  `y` int(11) NOT NULL DEFAULT 0 COMMENT 'µëÇσ£¿σ¥ÉµáçτÜäy',
  `isAlive` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Φ╖»Θ£╕µÿ»σÉªσ╖▓τ╗ÅΦó½σç╗µ¥Ç'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σñºσ»îτ┐üΦ╖»Θ£╕Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `richriskexchange`
--

CREATE TABLE `richriskexchange` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `x` int(11) NOT NULL DEFAULT 0 COMMENT 'σ¥ÉµáçτÜäX',
  `y` int(11) NOT NULL DEFAULT 0 COMMENT 'σ¥ÉµáçτÜäY',
  `exchangeId` int(11) NOT NULL DEFAULT 0 COMMENT 'σàæµìóId',
  `conditionId` int(11) NOT NULL DEFAULT 0 COMMENT 'µ¥íΣ╗╢τë⌐σôüId',
  `conditionAmount` int(11) NOT NULL DEFAULT 0 COMMENT 'µ¥íΣ╗╢τë⌐σôüµò░ΘçÅ',
  `goalId` int(11) NOT NULL DEFAULT 0 COMMENT 'σàæµìóτë⌐Id',
  `goalAmount` int(11) NOT NULL DEFAULT 0 COMMENT 'σàæµìóτë⌐µò░ΘçÅ',
  `score` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦÄ╖σ╛ùτº»σêå',
  `timesLimit` int(11) NOT NULL DEFAULT 0 COMMENT 'µ¼íµò░ΘÖÉσê╢'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='σñºσ»îτ┐üσòåσôüσàæµìóΦí¿';

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
-- Table structure for table `steal_gold`
--

CREATE TABLE `steal_gold` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `times` int(11) NOT NULL DEFAULT 0 COMMENT 'σü╖σÅûµ¼íµò░',
  `fighters_0` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Φ╖»Σ║║',
  `fighters_1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Φ╖»Σ║║',
  `fighters_2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Φ╖»Σ║║',
  `fighters_3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Φ╖»Σ║║'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µÄóΘçæµëïτ│╗τ╗ƒ';

-- --------------------------------------------------------

--
-- Table structure for table `steal_gold_news`
--

CREATE TABLE `steal_gold_news` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `atkid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σü╖σÅûΦÇàid',
  `money` int(11) NOT NULL DEFAULT 0 COMMENT 'σü╖σÅûµò░ΘçÅ',
  `time` int(11) NOT NULL DEFAULT 0 COMMENT 'µù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µÄóΘçæµëïΣ╝áΘù╗τ│╗τ╗ƒ';

-- --------------------------------------------------------

--
-- Table structure for table `storerecord`
--

CREATE TABLE `storerecord` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ID',
  `lastRefreshTime_0` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_1` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_2` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_3` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_4` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_5` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_6` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_7` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_8` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_9` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_10` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_11` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_12` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_13` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_14` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_15` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_16` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_17` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_18` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_19` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_20` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_21` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_22` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_23` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `lastRefreshTime_24` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσê╖µû░µù╢Θù┤,σìòΣ╜ìτºÆ',
  `freeRefreshTimes_0` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_1` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_2` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_3` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_4` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_5` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_6` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_7` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_8` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_9` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_10` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_11` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_12` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_13` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_14` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_15` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_16` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_17` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_18` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_19` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_20` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_21` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_22` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_23` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `freeRefreshTimes_24` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìσàìΦ┤╣σê╖µû░µ¼íµò░(Σ╕ñσ░Åµù╢τ┤»τº»Σ╕Çµ¼íµ£ÇσñºΣ╕║10)',
  `paidRefreshTimes_0` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_1` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_2` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_3` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_4` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_5` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_6` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_7` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_8` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_9` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_10` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_11` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_12` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_13` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_14` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_15` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_16` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_17` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_18` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_19` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_20` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_21` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_22` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_23` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `paidRefreshTimes_24` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìΣ╗ÿΦ┤╣σê╖µû░µ¼íµò░(µ¼íµò░Φ╢èσñÜΦè▒Φ┤╣Φ╢èΘ½ÿµ£ÇσñºΘàìτ╜«)',
  `buyTimes_0` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_1` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_2` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_3` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_4` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_5` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_6` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_7` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_8` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_9` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_10` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_11` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_12` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_13` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_14` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_15` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_16` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_17` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_18` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_19` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_20` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_21` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_22` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_23` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `buyTimes_24` int(11) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσñ⌐Φ┤¡Σ╣░µ¼íµò░',
  `flushIcon_0` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_1` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_2` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_3` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_4` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_5` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_6` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_7` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_8` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_9` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_10` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_11` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_12` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_13` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_14` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_15` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_16` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_17` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_18` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_19` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_20` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_21` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_22` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_23` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»',
  `flushIcon_24` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµÅÉτñ║σ░Åτ║óτé╣Σ┐íµü»'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Σ╕¬Σ║║σòåσ║ùσê╖µû░Φ«░σ╜òΦí¿';

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
-- Table structure for table `taskinfo`
--

CREATE TABLE `taskinfo` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `taskId` int(11) NOT NULL DEFAULT 0 COMMENT 'Σ╗╗σèíID',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '*τÄ⌐σ«╢ID',
  `taskName` varchar(30) NOT NULL DEFAULT '' COMMENT 'Σ╗╗σèíσÉìτº░',
  `completeCount` int(11) NOT NULL DEFAULT 0 COMMENT 'σ«îµêÉµ¼íµò░',
  `isCompleted` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªσ╖▓σ«îµêÉ(1µÿ»/0σÉª)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Σ╗╗σèíΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `team`
--

CREATE TABLE `team` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢ID',
  `teamType` int(11) NOT NULL DEFAULT 0 COMMENT 'ΘÿƒΣ╝ìτ▒╗σ₧ï',
  `fightPower` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùσè¢',
  `battleCard_0` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `battleCard_1` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `battleCard_2` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `battleCard_3` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `reserveCard_0` int(11) NOT NULL DEFAULT 0 COMMENT 'σ░ÅΣ╝ÖΣ╝┤σìíτëîΣ┐íµü»',
  `reserveCard_1` int(11) NOT NULL DEFAULT 0 COMMENT 'σ░ÅΣ╝ÖΣ╝┤σìíτëîΣ┐íµü»',
  `reserveCard_2` int(11) NOT NULL DEFAULT 0 COMMENT 'σ░ÅΣ╝ÖΣ╝┤σìíτëîΣ┐íµü»',
  `position_0` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉÄµÅ┤ΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `position_1` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉÄµÅ┤ΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `position_2` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉÄµÅ┤ΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `position_3` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉÄµÅ┤ΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `position_4` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉÄµÅ┤ΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `position_5` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉÄµÅ┤ΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `position_6` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉÄµÅ┤ΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `position_7` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉÄµÅ┤ΘÿƒΣ╝ìσìíτëîΣ┐íµü»',
  `position_8` int(11) NOT NULL DEFAULT 0 COMMENT 'σÉÄµÅ┤ΘÿƒΣ╝ìσìíτëîΣ┐íµü»'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢ΘÿƒΣ╝ìΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `title`
--

CREATE TABLE `title` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `title_id` int(11) NOT NULL DEFAULT 0 COMMENT 'τº░σÅ╖id',
  `value` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σ╜ôσëìµò░σÇ╝',
  `is_using` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµ¡úσ£¿Σ╜┐τö¿',
  `is_active` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªµ┐Çµ┤╗',
  `active_time` int(11) NOT NULL DEFAULT 0 COMMENT 'µ┐Çµ┤╗µù╢Θù┤',
  `is_reward` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªΦÄ╖σ╛ùσÑûσè▒'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `unlockreward`
--

CREATE TABLE `unlockreward` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢ID',
  `fetchRefId` varchar(500) NOT NULL DEFAULT '' COMMENT 'σ╖▓τ╗ÅΘóåσÅûσÑûσè▒τÜärefId'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Φ»òτé╝σà│σìíτ¢╕σà│Σ┐íµü»Φ«░σ╜ò';

-- --------------------------------------------------------

--
-- Table structure for table `viprecord`
--

CREATE TABLE `viprecord` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ID',
  `totalRecharge` int(11) NOT NULL DEFAULT 0 COMMENT 'τ┤»Φ«íσààσÇ╝,τö¿Σ║ÄΦ«íτ«ùvipτ¡ëτ║º',
  `lastFetchPrivateTime_0` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_1` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_2` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_3` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_4` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_5` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_6` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_7` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_8` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_9` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_10` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_11` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_12` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_13` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_14` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_15` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º',
  `lastFetchPrivateTime_16` int(11) NOT NULL DEFAULT 0 COMMENT 'τë╣µ¥âτñ╝σîàµù╢Θù┤[τºÆ],σîàσÉ½0τ║º'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='VIPΣ┐íµü»τ¢╕σà│Φ«░σ╜òΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `warspirit`
--

CREATE TABLE `warspirit` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `spirit_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿτü╡id',
  `skill` int(11) NOT NULL DEFAULT 0 COMMENT 'µèÇΦâ╜τ¡ëτ║º',
  `star` int(11) NOT NULL DEFAULT 0 COMMENT 'µÿƒτ║º',
  `is_selected` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'µÿ»σÉªσç║µêÿ'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µêÿτü╡Σ┐íµü»Φí¿';

-- --------------------------------------------------------

--
-- Table structure for table `warspirittreasurerecord`
--

CREATE TABLE `warspirittreasurerecord` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢id',
  `spirit_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µêÿτü╡id',
  `time` int(11) NOT NULL DEFAULT 0 COMMENT 'ΦÄ╖σ╛ùµù╢Θù┤'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µêÿτü╡µè╜σÑûΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `worldboss`
--

CREATE TABLE `worldboss` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `bossId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossσà│σìíID(WorldBossΘ¥ÖµÇüΦí¿ID)',
  `bossHp` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossσ╜ôσëìτöƒσæ╜(σñìµ┤╗µù╢σê╖µû░)',
  `bossMaxHp` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossµ£Çσñºτöƒσæ╜(σñìµ┤╗µù╢σê╖µû░)',
  `bossLevel` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossσ╜ôσëìτ¡ëτ║º',
  `isDead` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Bossµÿ»σÉªσ╖▓µ¡╗Σ║í',
  `deadTime` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossµ¡╗Σ║íµù╢Θù┤',
  `reviveTime` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossσñìµ┤╗µù╢Θù┤',
  `lastKillCid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Σ╕èµ¼íσç╗µ¥ÇΦÇàpid'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Σ╕ûτòîbossΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `worldbosschallenge`
--

CREATE TABLE `worldbosschallenge` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢pid',
  `teamLevel` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢τ¡ëτ║º',
  `challengeTimes` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µîæµêÿµ¼íµò░',
  `inspiringTimes_0` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `inspiringTimes_1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `inspiringTimes_2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `inspiringTimes_3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `inspiringTimes_4` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `inspiringTimes_5` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `inspiringTimes_6` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `inspiringTimes_7` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `inspiringTimes_8` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `inspiringTimes_9` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Θ╝ôΦê₧µ¼íµò░',
  `totalDamage_0` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_4` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_5` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_6` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_7` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_8` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `totalDamage_9` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_0` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_1` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_2` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_3` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_4` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_5` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_6` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_7` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_8` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `damageRank_9` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BOSSΣ╝ñσ«│',
  `beginFightTime` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢σ╝Çσºïµîæµêÿbossµù╢Θù┤,τö¿Σ║ÄΦ«íτ«ùτÄ⌐σ«╢µêÿµûùµù╢Θù┤',
  `leaveFightTime` bigint(20) NOT NULL DEFAULT 0 COMMENT 'τÄ⌐σ«╢ΘÇÇσç║bossµù╢Θù┤,τö¿Σ║ÄΦ«íτ«ùτÄ⌐σ«╢µîæµêÿCDµù╢Θù┤',
  `attackTimes` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Σ╗èµùÑµö╗σç╗µ¼íµò░',
  `resurrection` int(11) NOT NULL DEFAULT 0 COMMENT 'σñìµ┤╗µ¼íµò░',
  `autoChallenge` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'σ«Üµù╢Φç¬σè¿µîæµêÿΣ╕ûτòîBOSS'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢µîæµêÿΣ╕ûτòîBossΣ┐íµü»';

-- --------------------------------------------------------

--
-- Table structure for table `worldbosskillrecord`
--

CREATE TABLE `worldbosskillrecord` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `bossId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'BossId[σ»╣σ║öΣ║ÄRefWorldBossΦí¿ID]',
  `bossLevel` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Bossτ¡ëτ║º',
  `killerPid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σç╗µ¥ÇΦÇàpid',
  `killerName` varchar(500) NOT NULL DEFAULT '' COMMENT 'σç╗µ¥ÇΦÇàσÉìτº░',
  `killerLevel` int(11) NOT NULL DEFAULT 0 COMMENT 'σç╗µ¥ÇΦÇàτ¡ëτ║º',
  `killerIcon` int(11) NOT NULL DEFAULT 0 COMMENT 'σç╗µ¥ÇΦÇàσñ┤σâÅ',
  `deathTime` int(11) NOT NULL DEFAULT 0 COMMENT 'Bossµ¡╗Σ║íµù╢Θù┤',
  `fightTime` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µêÿµûùµù╢Θù┤',
  `rewardItemId` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÑûσè▒τë⌐σôüΣ┐íµü»',
  `rewardItemCount` varchar(500) NOT NULL DEFAULT '' COMMENT 'σÑûσè▒τë⌐σôüµò░ΘçÅ'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Σ╕ûτòîbossσç╗µ¥ÇΦ«░σ╜ò';

-- --------------------------------------------------------

--
-- Table structure for table `worship`
--

CREATE TABLE `worship` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µëÇσ▒₧τÄ⌐σ«╢ID',
  `worshipTimes_0` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_1` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_2` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_3` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_4` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_5` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_6` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_7` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_8` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_9` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_10` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_11` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_12` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_13` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_14` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_15` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_16` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_17` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_18` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `worshipTimes_19` int(11) NOT NULL DEFAULT 0 COMMENT 'Φå£µï£µ¼íµò░',
  `beWorshipTimes_0` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_1` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_2` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_3` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_4` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_5` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_6` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_7` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_8` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_9` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_10` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_11` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_12` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_13` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_14` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_15` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_16` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_17` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_18` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░',
  `beWorshipTimes_19` int(11) NOT NULL DEFAULT 0 COMMENT 'Φó½Φå£µï£µ¼íµò░'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='τÄ⌐σ«╢Φå£µï£Σ┐íµü»';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `achievement`
--
ALTER TABLE `achievement`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

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
-- Indexes for table `arena_competitor`
--
ALTER TABLE `arena_competitor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `arena_fight_record`
--
ALTER TABLE `arena_fight_record`
  ADD PRIMARY KEY (`id`),
  ADD KEY `atkPid` (`atkPid`),
  ADD KEY `defPid` (`defPid`);

--
-- Indexes for table `character`
--
ALTER TABLE `character`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `chatmessage`
--
ALTER TABLE `chatmessage`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `clientdata`
--
ALTER TABLE `clientdata`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pid` (`pid`);

--
-- Indexes for table `clientguide`
--
ALTER TABLE `clientguide`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pid` (`pid`);

--
-- Indexes for table `confreward`
--
ALTER TABLE `confreward`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dailyactive`
--
ALTER TABLE `dailyactive`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `dailyplayerrefresh`
--
ALTER TABLE `dailyplayerrefresh`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pid` (`pid`);

--
-- Indexes for table `dailyworldrefresh`
--
ALTER TABLE `dailyworldrefresh`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dress`
--
ALTER TABLE `dress`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `droiyan`
--
ALTER TABLE `droiyan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `droiyan_record`
--
ALTER TABLE `droiyan_record`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `droiyan_treasure`
--
ALTER TABLE `droiyan_treasure`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `equip`
--
ALTER TABLE `equip`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fixedtimemail`
--
ALTER TABLE `fixedtimemail`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `friend`
--
ALTER TABLE `friend`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `friendapply`
--
ALTER TABLE `friendapply`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `globalmail`
--
ALTER TABLE `globalmail`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `globalrichrisk`
--
ALTER TABLE `globalrichrisk`
  ADD PRIMARY KEY (`id`),
  ADD KEY `chapter` (`chapter`);

--
-- Indexes for table `guild`
--
ALTER TABLE `guild`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `guildapply`
--
ALTER TABLE `guildapply`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `guildboard`
--
ALTER TABLE `guildboard`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guildboss`
--
ALTER TABLE `guildboss`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guildbosschallenge`
--
ALTER TABLE `guildbosschallenge`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guildlog`
--
ALTER TABLE `guildlog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guildmember`
--
ALTER TABLE `guildmember`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guildmemberskill`
--
ALTER TABLE `guildmemberskill`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guildtasklog`
--
ALTER TABLE `guildtasklog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guildwarapply`
--
ALTER TABLE `guildwarapply`
  ADD PRIMARY KEY (`id`),
  ADD KEY `guildId` (`guildId`);

--
-- Indexes for table `guildwarguildresult`
--
ALTER TABLE `guildwarguildresult`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guildwarpuppet`
--
ALTER TABLE `guildwarpuppet`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `guildwarresult`
--
ALTER TABLE `guildwarresult`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `guild_rank_record`
--
ALTER TABLE `guild_rank_record`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `instance_info`
--
ALTER TABLE `instance_info`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `lingbao`
--
ALTER TABLE `lingbao`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `longnvapply`
--
ALTER TABLE `longnvapply`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `longnvresult`
--
ALTER TABLE `longnvresult`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `longnvwarpuppet`
--
ALTER TABLE `longnvwarpuppet`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `longnvwarresult`
--
ALTER TABLE `longnvwarresult`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `lottery`
--
ALTER TABLE `lottery`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `marry`
--
ALTER TABLE `marry`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `marry_apply`
--
ALTER TABLE `marry_apply`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `marry_divorce_apply`
--
ALTER TABLE `marry_divorce_apply`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `newtitle`
--
ALTER TABLE `newtitle`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `open_id` (`open_id`);

--
-- Indexes for table `playericon`
--
ALTER TABLE `playericon`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `playericonborder`
--
ALTER TABLE `playericonborder`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `playermail`
--
ALTER TABLE `playermail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `playerrechargeinfo`
--
ALTER TABLE `playerrechargeinfo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `playerrechargerecord`
--
ALTER TABLE `playerrechargerecord`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `playerrecord`
--
ALTER TABLE `playerrecord`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pid` (`pid`);

--
-- Indexes for table `playersetting`
--
ALTER TABLE `playersetting`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `player_find_treasure`
--
ALTER TABLE `player_find_treasure`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `player_goods`
--
ALTER TABLE `player_goods`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `rankinfo`
--
ALTER TABLE `rankinfo`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rank_record`
--
ALTER TABLE `rank_record`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rechargeorder`
--
ALTER TABLE `rechargeorder`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cporderid` (`cporderid`);

--
-- Indexes for table `rechargereset`
--
ALTER TABLE `rechargereset`
  ADD PRIMARY KEY (`id`),
  ADD KEY `goodsid` (`goodsid`);

--
-- Indexes for table `red_packet`
--
ALTER TABLE `red_packet`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `red_packet_pick`
--
ALTER TABLE `red_packet_pick`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `richriskavatar`
--
ALTER TABLE `richriskavatar`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `richriskexchange`
--
ALTER TABLE `richriskexchange`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `serverconfig`
--
ALTER TABLE `serverconfig`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `steal_gold`
--
ALTER TABLE `steal_gold`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `steal_gold_news`
--
ALTER TABLE `steal_gold_news`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `storerecord`
--
ALTER TABLE `storerecord`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pid` (`pid`);

--
-- Indexes for table `tableversioninfo`
--
ALTER TABLE `tableversioninfo`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `taskinfo`
--
ALTER TABLE `taskinfo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `team`
--
ALTER TABLE `team`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `title`
--
ALTER TABLE `title`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `unlockreward`
--
ALTER TABLE `unlockreward`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- Indexes for table `viprecord`
--
ALTER TABLE `viprecord`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pid` (`pid`);

--
-- Indexes for table `warspirit`
--
ALTER TABLE `warspirit`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `warspirittreasurerecord`
--
ALTER TABLE `warspirittreasurerecord`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `worldboss`
--
ALTER TABLE `worldboss`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `worldbosschallenge`
--
ALTER TABLE `worldbosschallenge`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `worldbosskillrecord`
--
ALTER TABLE `worldbosskillrecord`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `worship`
--
ALTER TABLE `worship`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pid` (`pid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `achievement`
--
ALTER TABLE `achievement`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

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
-- AUTO_INCREMENT for table `arena_competitor`
--
ALTER TABLE `arena_competitor`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `arena_fight_record`
--
ALTER TABLE `arena_fight_record`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `character`
--
ALTER TABLE `character`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `chatmessage`
--
ALTER TABLE `chatmessage`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `clientdata`
--
ALTER TABLE `clientdata`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `clientguide`
--
ALTER TABLE `clientguide`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `confreward`
--
ALTER TABLE `confreward`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `dailyactive`
--
ALTER TABLE `dailyactive`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `dailyplayerrefresh`
--
ALTER TABLE `dailyplayerrefresh`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `dailyworldrefresh`
--
ALTER TABLE `dailyworldrefresh`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `dress`
--
ALTER TABLE `dress`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `droiyan`
--
ALTER TABLE `droiyan`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `droiyan_record`
--
ALTER TABLE `droiyan_record`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `droiyan_treasure`
--
ALTER TABLE `droiyan_treasure`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `equip`
--
ALTER TABLE `equip`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `fixedtimemail`
--
ALTER TABLE `fixedtimemail`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `friend`
--
ALTER TABLE `friend`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `friendapply`
--
ALTER TABLE `friendapply`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `globalmail`
--
ALTER TABLE `globalmail`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `globalrichrisk`
--
ALTER TABLE `globalrichrisk`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guild`
--
ALTER TABLE `guild`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildapply`
--
ALTER TABLE `guildapply`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildboard`
--
ALTER TABLE `guildboard`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildboss`
--
ALTER TABLE `guildboss`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildbosschallenge`
--
ALTER TABLE `guildbosschallenge`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildlog`
--
ALTER TABLE `guildlog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildmember`
--
ALTER TABLE `guildmember`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildmemberskill`
--
ALTER TABLE `guildmemberskill`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildtasklog`
--
ALTER TABLE `guildtasklog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildwarapply`
--
ALTER TABLE `guildwarapply`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildwarguildresult`
--
ALTER TABLE `guildwarguildresult`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildwarpuppet`
--
ALTER TABLE `guildwarpuppet`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guildwarresult`
--
ALTER TABLE `guildwarresult`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `guild_rank_record`
--
ALTER TABLE `guild_rank_record`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `instance_info`
--
ALTER TABLE `instance_info`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `item`
--
ALTER TABLE `item`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `lingbao`
--
ALTER TABLE `lingbao`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `longnvapply`
--
ALTER TABLE `longnvapply`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `longnvresult`
--
ALTER TABLE `longnvresult`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `longnvwarpuppet`
--
ALTER TABLE `longnvwarpuppet`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `longnvwarresult`
--
ALTER TABLE `longnvwarresult`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `lottery`
--
ALTER TABLE `lottery`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `marry`
--
ALTER TABLE `marry`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `marry_apply`
--
ALTER TABLE `marry_apply`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `marry_divorce_apply`
--
ALTER TABLE `marry_divorce_apply`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `newtitle`
--
ALTER TABLE `newtitle`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `player`
--
ALTER TABLE `player`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `playericon`
--
ALTER TABLE `playericon`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `playericonborder`
--
ALTER TABLE `playericonborder`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `playermail`
--
ALTER TABLE `playermail`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `playerrechargeinfo`
--
ALTER TABLE `playerrechargeinfo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `playerrechargerecord`
--
ALTER TABLE `playerrechargerecord`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `playerrecord`
--
ALTER TABLE `playerrecord`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `playersetting`
--
ALTER TABLE `playersetting`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `player_find_treasure`
--
ALTER TABLE `player_find_treasure`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `player_goods`
--
ALTER TABLE `player_goods`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `rankinfo`
--
ALTER TABLE `rankinfo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `rank_record`
--
ALTER TABLE `rank_record`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `rechargeorder`
--
ALTER TABLE `rechargeorder`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `rechargereset`
--
ALTER TABLE `rechargereset`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `red_packet`
--
ALTER TABLE `red_packet`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `red_packet_pick`
--
ALTER TABLE `red_packet_pick`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `richriskavatar`
--
ALTER TABLE `richriskavatar`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `richriskexchange`
--
ALTER TABLE `richriskexchange`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `serverconfig`
--
ALTER TABLE `serverconfig`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `steal_gold`
--
ALTER TABLE `steal_gold`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `steal_gold_news`
--
ALTER TABLE `steal_gold_news`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `storerecord`
--
ALTER TABLE `storerecord`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `taskinfo`
--
ALTER TABLE `taskinfo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `team`
--
ALTER TABLE `team`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `title`
--
ALTER TABLE `title`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `unlockreward`
--
ALTER TABLE `unlockreward`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `viprecord`
--
ALTER TABLE `viprecord`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `warspirit`
--
ALTER TABLE `warspirit`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `warspirittreasurerecord`
--
ALTER TABLE `warspirittreasurerecord`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `worldboss`
--
ALTER TABLE `worldboss`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `worldbosschallenge`
--
ALTER TABLE `worldbosschallenge`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `worldbosskillrecord`
--
ALTER TABLE `worldbosskillrecord`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `worship`
--
ALTER TABLE `worship`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
