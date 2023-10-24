create table wth7.user_team
(
    id         bigint auto_increment comment 'id'
        primary key,
    userId     bigint                             not null comment '用户Id',
    teamId     bigint                             not null comment '队伍id',
    joinTime   datetime                           null comment '加入队伍时间',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint                            not null comment '是否删除（逻辑删除）'
)
    comment '用户-队伍表';
