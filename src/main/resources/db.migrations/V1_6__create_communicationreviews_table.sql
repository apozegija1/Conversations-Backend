CREATE TABLE IF NOT EXISTS `communicationreviews` (
   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
   `date` timestamp NOT NULL,
   `communication_id` int(11) unsigned NULL,
   `rating` int(2) unsigned NOT NULL,
   `description` varchar(255) NULL,
   PRIMARY KEY (`id`),
   FOREIGN KEY `fk_communicationreviews_communication_idx` (`communication_id`) REFERENCES communications(id) ON DELETE SET NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
