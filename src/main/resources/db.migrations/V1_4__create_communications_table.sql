CREATE TABLE IF NOT EXISTS `communications` (
   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
   `type_id` int(11) unsigned NOT NULL,
   `agent_id` int(11) unsigned NULL,
   `customer_id` int(11) unsigned NULL,
   `start_time` timestamp NOT NULL,
   `end_time` timestamp NULL,
   `text` varchar(255) NOT NULL,
   `message_id` varchar(255) NULL,
   PRIMARY KEY (`id`),
   FOREIGN KEY `fk_communication_agent1_idx` (`agent_id`) REFERENCES users(id) ON DELETE SET NULL, # If agent is removed we can still save data related to their communication
   FOREIGN KEY `fk_communication_customer1_idx` (`customer_id`) REFERENCES users(id) ON DELETE SET NULL,
   FOREIGN KEY `fk_communication_type1_idx` (`type_id`) REFERENCES communicationtypes(id) ON DELETE CASCADE # If type is deleted then we don't know what happened which kind of conversation it was so delete
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
