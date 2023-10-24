create table tag
(
    id           bigint auto_increment comment 'id' primary key,
    tag_name     varchar(256)                       null comment '标签名',
    user_id      bigint                             null comment '用户 id',
    parent_id    bigint                             null comment '父标签 id',
    is_parent    tinyint                            null comment '是否为父标签 0-否 1-是',
    gmt_create   datetime default current_timestamp null comment '创建时间',
    gmt_modified datetime default current_timestamp null on update current_timestamp comment '修改时间',
    is_deleted   tinyint default 0                  null comment '逻辑删除'
)
    comment '标签';

create index idx_user_id
    on tag (user_id);

create index uniIdx_tag_name
    on tag (tag_name)