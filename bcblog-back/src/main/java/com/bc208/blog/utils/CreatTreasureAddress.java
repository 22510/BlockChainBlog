package com.bc208.blog.utils;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.bc208.blog.utils.RedisConstants.TREASURE_DIG;

/**
 * @author QingheLi
 */
@Slf4j
public class CreatTreasureAddress extends QuartzJobBean {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // 中心点和四个角经纬度
        double[] center = {108.837465215, 34.123274375};
        double[] point1 = {108.8303095, 34.131791};
        double[] point2 = {108.82381377, 34.12135875};
        double[] point3 = {108.83698984, 34.11531941};
        double[] point4 = {108.84364958, 34.12619334};
        // 获取随机点的经纬度
        Map<String, String> treasurePoints = new HashMap<>(50);
        String point = "";
        for (int i = 0; i < 50; i++) {
            double[] treasurePoint = getRandomPoint(center, point1, point2, point3, point4);
            point = treasurePoint[0] + "," + treasurePoint[1];
            treasurePoints.put( "p"+i, point);
        }
        stringRedisTemplate.opsForHash().putAll(TREASURE_DIG, treasurePoints);
        stringRedisTemplate.expire(TREASURE_DIG, 1, TimeUnit.DAYS);
        // System.out.println("Random Point: [" + randomPoint[0] + ", " + randomPoint[1] + "]");
        log.warn("宝藏经纬度组上传");
    }

    public static double[] getRandomPoint(double[] center, double[] point1, double[] point2, double[] point3, double[] point4) {
        double radius = getRandomRadius(center, point1, point2, point3, point4);
        double angle = getRandomAngle();

        // 计算新点的经纬度
        double newLongitude = center[0] + radius * Math.cos(angle);
        double newLatitude = center[1] + radius * Math.sin(angle);

        return new double[]{newLongitude, newLatitude};
    }

    private static double getRandomRadius(double[] center, double[] point1, double[] point2, double[] point3, double[] point4) {
        // 计算正方形区域的对角线长度作为半径
        double diagonal = getDistance(center[0], center[1], point1[0], point1[1]);
        return new Random().nextDouble() * diagonal / 2.0;
    }

    private static double getRandomAngle() {
        // 随机生成一个角度（弧度）
        return new Random().nextDouble() * 2 * Math.PI;
    }

    private static double getDistance(double x1, double y1, double x2, double y2) {
        // 计算两点之间的距离
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
