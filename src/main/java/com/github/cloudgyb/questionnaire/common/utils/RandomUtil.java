package com.github.cloudgyb.questionnaire.common.utils;

import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * 随机字符串生成工具类
 *
 * @author geng
 * 2020/11/4
 */
public class RandomUtil {

    public static String randomNumber(int len) {
        if (len < 0)
            len = 0;
        final Random random = new Random();
        final IntStream ints = random.ints(len, 0, 10);
        StringBuilder sb = new StringBuilder();
        ints.forEach(sb::append);
        return sb.toString();
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        final String s = randomNumber(5);
        System.out.println(s);
    }
}
