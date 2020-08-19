CREATE TABLE IF NOT EXISTS `userroles` (
   `user_id` int(11) unsigned NOT NULL,
   `role_id` int(11) unsigned NOT NULL,
   FOREIGN KEY `fk_userroles_user1_idx` (`user_id`) REFERENCES users(id) ON DELETE CASCADE,
   FOREIGN KEY `fk_userroles_role1_idx` (`role_id`) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
