create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    user_name    varchar(256)      null comment '用户昵称',
    password     varchar(512)      not null comment '密码',
    user_account varchar(256)      null comment '账户',
    gender       tinyint           null comment '性别 0-男性 1-女性',
    phone        varchar(256)      null comment '电话',
    email        varchar(512)      null comment '邮箱',
    avatar       varchar(1024)     null comment '头像链接',
    status       tinyint default 0 null comment '状态 0 - 正常',
    gmt_create   datetime          null comment '创建时间',
    gmt_modified datetime          null comment '修改时间',
    is_deleted   tinyint default 0 null comment '逻辑删除',
    user_role    tinyint default 0 null comment '权限 0 - 普通 1 - 管理员',
    planet_code  varchar(256)      null comment '星球编号'
)
    comment '用户';

alter table user add column tags varchar(1024) null comment '标签列表';