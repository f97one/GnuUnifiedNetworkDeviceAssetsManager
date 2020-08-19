insert into role (role_id, role_name, manager) values (1, '管理者', true);

insert into organization (org_id, ancestor_org_id, org_name) values (1, null, '全社');

insert into authority (authority_id, authority_name, admin_role) values (1, 'システム管理者', true), (2, '一般ユーザー', false);

insert into app_user (user_id, username, password, display_name, email, passwd_last_modified, joined_org_id, role_id)
values (1, 'admin', '$2a$10$z4BRWD1/2DbzvhT82qLsF.cKFWGHpkGgK8hAM7AZ4HkitQpL7UXYO', 'Admin', 'admin@example.com', null, 1, 1);

insert into user_authority (authority_id, user_id) values (1, 1);
