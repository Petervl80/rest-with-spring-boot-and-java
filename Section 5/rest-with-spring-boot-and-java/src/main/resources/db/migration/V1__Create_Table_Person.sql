CREATE TABLE IF NOT EXISTS `person` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `person` (`id`, `address`, `first_name`, `gender`, `last_name`) VALUES
	(3, 'Recife', 'Sla', 'Male', 'Senna'),
	(4, 'Recife', 'Sla', 'Male', 'Senna');