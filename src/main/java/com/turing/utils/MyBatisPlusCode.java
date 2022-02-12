package com.turing.utils;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

public class MyBatisPlusCode {//代码生成

    public static void main (String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/sys?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("qds") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("I:\\ProjectAll\\work\\wechat-mini-server\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com") // 设置父包名
                            .moduleName("turing") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "I:\\ProjectAll\\work\\wechat-mini-server\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("community_infor") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                //                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
