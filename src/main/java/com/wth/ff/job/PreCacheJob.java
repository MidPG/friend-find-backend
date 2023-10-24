package com.wth.ff.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wth.ff.model.domain.User;
import com.wth.ff.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.wth.ff.constant.UserConstant.USER_RECOMMEND_KEY;

/**
 *  使用定时任务进行缓存预热
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     *  重点用户，进行缓存预热的key
     */
    private List<Long> mainUserList = Arrays.asList(1L);

    /**
     *  使用分布式锁来完成定时任务，多台机器部署项目时，只有一台执行定时任务即可
     */
    @Scheduled(cron = "0 30 22 * * ?")
    public void doCacheRecommend() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        RLock lock = redissonClient.getLock("ff:preCacheLock");
        try {
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                log.info("getLock:" + Thread.currentThread().getId());
                // 查数据库
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                // 写缓存 30s过期
                String redisKey = USER_RECOMMEND_KEY + mainUserList.toString();
                try {
                    valueOperations.set(redisKey, userPage, 30, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.error("缓存写入失败",e);
                }
            }
        } catch (Exception e) {
            log.error("缓存预热失败", e);
        } finally {
            // 最后要释放锁，必须是自己的锁
            if (lock.isHeldByCurrentThread()) {
                log.info("unlock" + Thread.currentThread().getId());
                lock.unlock();
            }
        }


    }


}
