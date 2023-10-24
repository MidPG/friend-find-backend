package com.wth.ff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wth.ff.mapper.TagMapper;
import com.wth.ff.model.domain.Tag;
import com.wth.ff.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author 79499
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2023-09-04 20:57:29
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService{

}




