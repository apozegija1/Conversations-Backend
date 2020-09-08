INSERT INTO roles (id, role_name) VALUES (1, 'User') SELECT 'foo' WHERE NOT EXISTS (SELECT * FROM roles);
INSERT INTO roles (id, role_name) VALUES (2, 'SuperAdmin') SELECT 'foo' WHERE NOT EXISTS (SELECT * FROM roles);
INSERT INTO roles (id, role_name) VALUES (3, 'CompanyAdmin') SELECT 'foo' WHERE NOT EXISTS (SELECT * FROM roles);
INSERT INTO roles (id, role_name) VALUES (4, 'Agent') SELECT 'foo' WHERE NOT EXISTS (SELECT * FROM roles);

INSERT INTO users (id, username, password, first_name, last_name, email, gender, phone, company_id, activated, created_at) VALUES (1, 'SuperDzej', '$2a$10$Vs28N6SlCcb78RtesA8okeKOYkXG76VFGwTnMIxmeHRV54ojlyvAG', 'Dzej', 'Muha', 'dmuharemov1@etf.unsa.ba', null, null, null,  1, CURRENT_TIMESTAMP) WHERE NOT EXISTS (SELECT * FROM users);

INSERT INTO userroles (user_id, role_id) VALUES (1, 2) WHERE NOT EXISTS (SELECT * FROM userroles);

INSERT INTO users (id, username, password, first_name, last_name, email, gender, phone, company_id, activated, created_at) VALUES (2, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 'admin', 'admin@admin.ba', null, null, null,  1, CURRENT_TIMESTAMP);

INSERT INTO userroles (user_id, role_id) VALUES (2, 2);


INSERT INTO communicationtypes (id, type) VALUES (1, 'SMS');
INSERT INTO communicationtypes (id, type) VALUES (2, 'Video');
INSERT INTO communicationtypes (id, type) VALUES (3, 'Audio');
