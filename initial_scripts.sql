/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.1.49-community : Database - financial_sys
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`financial_sys` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `financial_sys`;

/*Table structure for table `borrow` */

DROP TABLE IF EXISTS `borrow`;

CREATE TABLE `borrow` (
  `borrow_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL,
  `from_who` varchar(50) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `last_update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active_flg` char(1) NOT NULL DEFAULT 'Y',
  `occur_ts` timestamp NULL DEFAULT NULL,
  `add_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`borrow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `borrow_history` */

DROP TABLE IF EXISTS `borrow_history`;

CREATE TABLE `borrow_history` (
  `borrow_history_id` int(11) NOT NULL AUTO_INCREMENT,
  `borrow_id` int(11) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `last_update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active_flg` char(1) NOT NULL DEFAULT 'Y',
  `occur_ts` timestamp NULL DEFAULT NULL,
  `add_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`borrow_history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `category_dfntn` */

DROP TABLE IF EXISTS `category_dfntn`;

CREATE TABLE `category_dfntn` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `last_update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active_flg` char(1) NOT NULL DEFAULT 'Y',
  `add_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `consume` */

DROP TABLE IF EXISTS `consume`;

CREATE TABLE `consume` (
  `consume_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL,
  `dest` varchar(50) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `last_update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active_flg` char(1) NOT NULL DEFAULT 'Y',
  `occur_ts` timestamp NULL DEFAULT NULL,
  `add_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`consume_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `deposit` */

DROP TABLE IF EXISTS `deposit`;

CREATE TABLE `deposit` (
  `deposit_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL,
  `source` varchar(50) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `last_update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active_flg` char(1) NOT NULL DEFAULT 'Y',
  `occur_ts` timestamp NULL DEFAULT NULL,
  `add_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`deposit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `lend` */

DROP TABLE IF EXISTS `lend`;

CREATE TABLE `lend` (
  `lend_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL,
  `to_who` varchar(50) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `last_update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active_flg` char(1) NOT NULL DEFAULT 'Y',
  `occur_ts` timestamp NULL DEFAULT NULL,
  `add_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`lend_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `lend_history` */

DROP TABLE IF EXISTS `lend_history`;

CREATE TABLE `lend_history` (
  `lend_history_id` int(11) NOT NULL AUTO_INCREMENT,
  `lend_id` int(11) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `last_update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active_flg` char(1) NOT NULL DEFAULT 'Y',
  `occur_ts` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `add_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`lend_history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `lk_ref` */

DROP TABLE IF EXISTS `lk_ref`;

CREATE TABLE `lk_ref` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_type` varchar(500) NOT NULL,
  `ref_key` varchar(500) NOT NULL,
  `ref_value` varchar(1000) NOT NULL,
  `ref_desc` varchar(10000) DEFAULT NULL,
  `last_update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active_flg` char(1) NOT NULL DEFAULT 'Y',
  `add_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_msg` */

DROP TABLE IF EXISTS `tb_msg`;

CREATE TABLE `tb_msg` (
  `msg_id` int(11) NOT NULL,
  `msg_content` varchar(2000) DEFAULT NULL,
  `active_flg` char(1) NOT NULL DEFAULT 'Y'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `type_dfntn` */

DROP TABLE IF EXISTS `type_dfntn`;

CREATE TABLE `type_dfntn` (
  `type_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `last_update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `category_id` int(11) NOT NULL,
  `active_flg` char(1) NOT NULL DEFAULT 'Y',
  `add_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/* Procedure structure for procedure `getAllRec` */

/*!50003 DROP PROCEDURE IF EXISTS  `getAllRec` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllRec`()
BEGIN
	select * from mine;
    END */$$
DELIMITER ;

/* Procedure structure for procedure `save` */

/*!50003 DROP PROCEDURE IF EXISTS  `save` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `save`(in in_id int)
BEGIN
	insert into mine(id, ts) values(in_id, now());
    END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


/*Data for the table `category_dfntn` */

insert  into `category_dfntn`(`category_id`,`category_name`,`description`,`last_update_ts`,`active_flg`,`add_ts`) values (1,'收入','所有进账收入','2018-06-10 17:11:55','Y','2018-06-15 20:28:46'),(2,'支出','所有向外支出','2018-06-10 17:12:54','Y','2018-06-15 20:28:46'),(3,'借入','所有借贷性质','2018-06-10 17:13:37','Y','2018-06-15 20:28:46'),(4,'借出','所有向外借出','2018-06-10 17:13:57','Y','2018-06-15 20:28:46');

/*Data for the table `tb_msg` */

insert  into `tb_msg`(`msg_id`,`msg_content`,`active_flg`) values (1,'大的类别，不应该在页面上可改，因为影响到tab的数量，但目前程序未禁止（更新：已禁止）','N'),(2,'时间显示在系统制作之前的记录可能并不准确，某些数据仅供参考','Y'),(3,'偿还欠款的支出条目也应该记录在对应的\'借入\'偿还历史中','Y'),(4,'信用卡、花呗的还款可以不记入支出项(最好记入，以便核对)，因为其由对应日常消费等支出构成','N'),(5,'即使有公积金按月转账入账，也应该完整记录每月房贷的准确支出','N'),(6,'<ul><li>条目一</li><li style=\'color: yellow\'>条目二</li></ul>','N'),(7,'2018/6/22，存款和支出面板已开始支持filters','N'),(8,'对账说明：<ul style=\'color: yellow\'>\r\n<li>存款为固定储蓄（非总收入），但可用于支付房贷支出等;<br/>\r\n鉴于此：<i><b>实际存款 = 存款TAB总和 - 支出TAB特定类型（偿还房贷+首付）</b></i>\r\n</li>\r\n<li>存款已扣除每月预留的生活支出;<br/>\r\n故：<i><b>总收入 = 存款TAB总和 + 支出TAB特定类型（日常消费+特殊消费=花呗+信用卡）</b></i>\r\n</li>\r\n<li>若有特殊支出动用固定存款，应记录在特定的支出类型中（尚未建立，如\'动用固存的支出\'）;</li>\r\n</ul>','Y'),(9,'待实现的需求：<ul style=\'color: yellow\'>\r\n<li>FilterPanel支持多个Filter</li>\r\n<li>借入借出账目的结清状态，status[\'Not started\', \'In progress\', \'Complete\']</li>\r\n<li>目前按照更新时间排序，需要增加排序选择项</li>\r\n</ul>','Y');

/* Customized lookup data insert */
INSERT INTO `financial_sys`.`lk_ref`(`id`,`ref_type`,`ref_key`,`ref_value`,`ref_desc`,`last_update_ts`,`active_flg`,`add_ts`) VALUES ( NULL,'CREDIT_CARD_PERIOD','FROM','2019-04-08','Start date for credit card period',CURRENT_TIMESTAMP,'Y',NOW());
INSERT INTO `financial_sys`.`lk_ref`(`id`,`ref_type`,`ref_key`,`ref_value`,`ref_desc`,`last_update_ts`,`active_flg`,`add_ts`) VALUES ( NULL,'CREDIT_CARD_PERIOD','TO','2019-05-08','End date for credit card period',CURRENT_TIMESTAMP,'Y',NOW());

