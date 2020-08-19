CREATE TABLE IF NOT EXISTS `users` (
   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
   `username` varchar(255) NOT NULL,
   `password` varchar(255) NOT NULL,
   `first_name` varchar(255) NOT NULL,
   `last_name` varchar(255) NOT NULL,
   `gender` varchar(255) NULL,
   `email` varchar(255) NOT NULL,
   `phone` varchar(255) NULL,
   `company_id` int(11) unsigned DEFAULT NULL ,
   `activated` tinyint DEFAULT FALSE,
   PRIMARY KEY (`id`),
   UNIQUE KEY `unique_user_username` (`username`),
   FOREIGN KEY `fk_user_company1_idx` (`company_id`) REFERENCES companies(id) ON DELETE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
