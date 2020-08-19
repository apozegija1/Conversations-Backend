CREATE TABLE IF NOT EXISTS `companies` (
   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
   `name` varchar(255) NOT NULL,
   `address` varchar(255) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
