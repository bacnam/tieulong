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
-- Database: `longnu_dbhunkftalklog`
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
-- Table structure for table `chatlog`
--

CREATE TABLE `chatlog` (
  `id` bigint(20) NOT NULL COMMENT 'Φç¬σó₧Σ╕╗Θö«',
  `server_id` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿ID',
  `timestamp` int(11) NOT NULL DEFAULT 0 COMMENT 'µùÑσ┐ùµù╢Θù┤(µù╢Θù┤µê│)',
  `date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT 'µùÑσ┐ùµù╢Θù┤(yyyymmdd)',
  `chatType` int(11) NOT NULL DEFAULT 0 COMMENT 'Φüèσñ⌐τ▒╗σ₧ï',
  `receivePId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'µÄÑσÅùΦÇàid',
  `senderPId` bigint(20) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüΦÇàid',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT 'σåàσ«╣',
  `sendTime` int(11) NOT NULL DEFAULT 0 COMMENT 'σÅæΘÇüµù╢Θù┤',
  `serverId` int(11) NOT NULL DEFAULT 0 COMMENT 'µ£ìσèíσÖ¿id'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='µ┤╗σè¿µôìΣ╜£µùÑσ┐ùΦí¿';

-- --------------------------------------------------------

--
-- Table structure for table `tableversioninfo`
--

CREATE TABLE `tableversioninfo` (
  `ID` int(11) NOT NULL,
  `Version` varchar(64) NOT NULL,
  `Info` varchar(64) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activityrequest`
--
ALTER TABLE `activityrequest`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `chatlog`
--
ALTER TABLE `chatlog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tableversioninfo`
--
ALTER TABLE `tableversioninfo`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activityrequest`
--
ALTER TABLE `activityrequest`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';

--
-- AUTO_INCREMENT for table `chatlog`
--
ALTER TABLE `chatlog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Φç¬σó₧Σ╕╗Θö«';
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
