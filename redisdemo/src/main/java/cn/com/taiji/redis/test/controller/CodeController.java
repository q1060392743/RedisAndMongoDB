package cn.com.taiji.redis.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author SongChong created by 2018/12/12 0012 19:21
 */
@RestController
public class CodeController {

    @Autowired
    private StringRedisTemplate template;

    @RequestMapping("/sendMsg")
    public String sendMsg(String phoneNum) {
        int count;
        long tempTime;

        if (!template.hasKey(phoneNum)) {
            count = 1;
            String countString = String.valueOf(count);
            template.opsForValue().set(phoneNum, countString, 10, TimeUnit.SECONDS);

            System.out.println("发送的手机号为：" + phoneNum);
            System.out.println("当前时间为：" + System.currentTimeMillis());
            return phoneNum + "发送成功，已发送" + template.opsForValue().get(phoneNum) + "次";
        } else {
            count = Integer.parseInt(template.opsForValue().get(phoneNum));
            count++;
            if (count > 3) {
                template.opsForValue().get(phoneNum);
                return "请求次数过多";
            } else {
                String countString = String.valueOf(count);
                tempTime = template.getExpire(phoneNum);
                template.opsForValue().set(phoneNum, countString, tempTime, TimeUnit.SECONDS);
                System.out.println("发送的手机号为：" + phoneNum);
                System.out.println("当前时间为：" + System.currentTimeMillis());
                return phoneNum + "发送成功，已发送" + template.opsForValue().get(phoneNum) + "次";
            }
        }
    }
}