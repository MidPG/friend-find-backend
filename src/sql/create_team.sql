create table team
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(255)                       not null comment '队伍名称',
    description varchar(1024)                      null comment '队伍描述',
    maxNum      int      default 1                 not null comment '最大人数',
    expireTime  datetime                           null comment '过期时间',
    userId      bigint   default 0                 not null comment '创建用户id',
    status      int                                not null comment '队伍状态 0 - 公开 1 - 私有 2 - 加密',
    password    varchar(512)                       null comment '队伍密码',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete    tinyint  default 0                 not null comment '是否删除（逻辑删除）'
)
    comment '队伍';