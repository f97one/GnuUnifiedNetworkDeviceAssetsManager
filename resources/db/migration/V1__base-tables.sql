-- Project Name : g-u-n-d-a-m_db
-- Date/Time    : 2020/07/11 13:38:42
-- Author       : HAJIME Fukuna
-- RDBMS Type   : PostgreSQL
-- Application  : A5:SQL Mk-2

/*
  BackupToTempTable, RestoreFromTempTable疑似命令が付加されています。
  これにより、drop table, create table 後もデータが残ります。
  この機能は一時的に $$TableName のような一時テーブルを作成します。
*/

-- デバイス調査履歴
--* BackupToTempTable
drop table if exists device_survey_hist cascade;

--* RestoreFromTempTable
create table device_survey_hist
(
    survey_id      uuid                                               not null,
    surveyed_at    timestamp without time zone default current_timestamp not null,
    device_id      uuid,
    hostname       character varying(64),
    device_name    character varying(200),
    ip_address     character varying(32),
    os_version     character varying(64),
    mem_size       bigint,
    cpu_name       character varying(64),
    disk_size      bigint,
    disk_free_size bigint,
    survey_method  integer                  default 1,
    constraint device_survey_hist_PKC primary key (survey_id)
);

-- デバイス使用
--* BackupToTempTable
drop table if exists device_usage cascade;

--* RestoreFromTempTable
create table device_usage
(
    device_id uuid    not null,
    user_id   integer not null,
    constraint device_usage_PKC primary key (device_id, user_id)
);

-- ユーザー権限
--* BackupToTempTable
drop table if exists user_authority cascade;

--* RestoreFromTempTable
create table user_authority
(
    authority_id integer not null,
    user_id      integer not null,
    constraint user_authority_PKC primary key (authority_id, user_id)
);

-- ユーザー
--* BackupToTempTable
drop table if exists app_user cascade;

drop sequence if exists app_user_user_id_seq;
create sequence app_user_user_id_seq;
--* RestoreFromTempTable
create table app_user
(
    user_id              integer                not null default nextval('app_user_user_id_seq'),
    username             character varying(32)  not null,
    password             character varying(128) not null,
    display_name         character varying(128),
    email                character varying(128),
    passwd_last_modified timestamp without time zone default current_timestamp,
    joined_org_id        integer,
    role_id              integer,
    constraint app_user_PKC primary key (user_id)
);

-- 権限
--* BackupToTempTable
drop table if exists authority cascade;

drop sequence if exists authority_authority_id_seq;
create sequence authority_authority_id_seq;
--* RestoreFromTempTable
create table authority
(
    authority_id   integer               not null default nextval('authority_authority_id_seq'),
    authority_name character varying(16) not null,
    admin_role     boolean default false,
    constraint authority_PKC primary key (authority_id)
);

-- デバイス
--* BackupToTempTable
drop table if exists device cascade;

--* RestoreFromTempTable
create table device
(
    device_id      uuid                   not null,
    hostname       character varying(64),
    device_name    character varying(200) not null,
    ip_address     character varying(32),
    os_id          integer                not null,
    os_version     character varying(64),
    mem_size       bigint,
    cpu_name       character varying(64),
    disk_size      bigint,
    remarks        text,
    is_provisional boolean default true,
    constraint device_PKC primary key (device_id)
);

-- OS
--* BackupToTempTable
drop table if exists operating_system cascade;

drop sequence if exists operating_system_os_id_seq;
create sequence operating_system_os_id_seq;
--* RestoreFromTempTable
create table operating_system
(
    os_id   integer               not null default nextval('operating_system_os_id_seq'),
    os_name character varying(32) not null,
    constraint operating_system_PKC primary key (os_id)
);

-- 組織
--* BackupToTempTable
drop table if exists organization cascade;

drop sequence if exists organization_org_id_seq;
create sequence organization_org_id_seq;
--* RestoreFromTempTable
create table organization
(
    org_id          integer not null default nextval('organization_org_id_seq'),
    ancestor_org_id integer,
    org_name        character varying(32),
    constraint organization_PKC primary key (org_id)
);

-- 役職
--* BackupToTempTable
drop table if exists role cascade;

drop sequence if exists role_role_id_seq;
create sequence role_role_id_seq;
--* RestoreFromTempTable
create table role
(
    role_id   integer not null default nextval('role_role_id_seq'),
    role_name character varying(16),
    manager   boolean default false,
    constraint role_PKC primary key (role_id)
);

alter table app_user
    add constraint app_user_FK1 foreign key (role_id) references role (role_id);

alter table app_user
    add constraint app_user_FK2 foreign key (joined_org_id) references organization (org_id);

alter table device
    add constraint device_FK1 foreign key (os_id) references operating_system (os_id);

alter table device_usage
    add constraint device_usage_FK1 foreign key (device_id) references device (device_id);

alter table device_usage
    add constraint device_usage_FK2 foreign key (user_id) references app_user (user_id);

alter table user_authority
    add constraint user_authority_FK1 foreign key (authority_id) references authority (authority_id);

alter table user_authority
    add constraint user_authority_FK2 foreign key (user_id) references app_user (user_id);

comment on table device_survey_hist is 'デバイス調査履歴';
comment on column device_survey_hist.survey_id is 'サーベイID';
comment on column device_survey_hist.surveyed_at is '調査日時';
comment on column device_survey_hist.device_id is '調査時デバイスID';
comment on column device_survey_hist.hostname is '調査時ホスト名';
comment on column device_survey_hist.device_name is 'デバイス名';
comment on column device_survey_hist.ip_address is '調査時IPアドレス';
comment on column device_survey_hist.os_version is '調査時OSバージョン';
comment on column device_survey_hist.mem_size is 'メモリ容量';
comment on column device_survey_hist.cpu_name is 'プロセッサ名';
comment on column device_survey_hist.disk_size is '合計固定ディスク容量';
comment on column device_survey_hist.disk_free_size is '固定ディスク残量';
comment on column device_survey_hist.survey_method is '調査メソッド:1 -> 定期収集
2 -> 端末からの手動報告
3 -> 管理サーバからの強制収集
4 -> 初回接続による仮登録';

comment on table device_usage is 'デバイス使用';
comment on column device_usage.device_id is 'デバイスID';
comment on column device_usage.user_id is 'ユーザーID';

comment on table user_authority is 'ユーザー権限';
comment on column user_authority.authority_id is '権限ID';
comment on column user_authority.user_id is 'ユーザーID';

comment on table app_user is 'ユーザー';
comment on column app_user.user_id is 'ユーザーID';
comment on column app_user.username is 'ログイン名';
comment on column app_user.password is 'パスワード';
comment on column app_user.display_name is '表示名';
comment on column app_user.email is 'Eメール';
comment on column app_user.passwd_last_modified is 'パスワード最終更新日時';
comment on column app_user.joined_org_id is '所属組織ID';
comment on column app_user.role_id is '役職ID';

comment on table authority is '権限';
comment on column authority.authority_id is 'ロールID';
comment on column authority.authority_name is 'ロール名';
comment on column authority.admin_role is '管理者';

comment on table device is 'デバイス';
comment on column device.device_id is 'デバイスID';
comment on column device.hostname is 'ホスト名';
comment on column device.device_name is 'デバイス名';
comment on column device.ip_address is 'IPアドレス';
comment on column device.os_id is 'OS番号';
comment on column device.os_version is 'OSバージョン';
comment on column device.mem_size is 'メモリ容量';
comment on column device.cpu_name is 'プロセッサ名';
comment on column device.disk_size is '合計固定ディスク容量';
comment on column device.remarks is '注釈';
comment on column device.is_provisional is '仮登録';

comment on table operating_system is 'OS';
comment on column operating_system.os_id is 'OS番号';
comment on column operating_system.os_name is 'OS名';

comment on table organization is '組織';
comment on column organization.org_id is '組織ID';
comment on column organization.ancestor_org_id is '上位組織ID';
comment on column organization.org_name is '組織名';

comment on table role is '役職';
comment on column role.role_id is '役職ID';
comment on column role.role_name is '役職名';
comment on column role.manager is '管理職権限';
