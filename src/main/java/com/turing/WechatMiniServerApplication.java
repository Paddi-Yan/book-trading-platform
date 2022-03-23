package com.turing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 又蠢又笨的懒羊羊程序猿
 */
@SpringBootApplication
@EnableScheduling//开启定时任务，用于榜单的刷新
public class WechatMiniServerApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(WechatMiniServerApplication.class, args);
    }

}
