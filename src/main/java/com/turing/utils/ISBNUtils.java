package com.turing.utils;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.UnknownFormatConversionException;
import java.util.regex.Pattern;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 14:30:34
 */
@Slf4j
public class ISBNUtils
{
    /**
     * 所需值
     */
    private static final String FORMAT_TEN = "^[0-9]{%s}[0-9X]{1}$";
    private static final String FORMAT_THIRTEEN = "^[0-9]{%s}$";
    private static final Integer ZERO = 0;
    private static final Integer ONE = 1;
    private static final Integer TWO = 2;
    private static final Integer THREE = 3;
    private static final Integer NINE = 9;
    private static final Integer TEN = 10;
    private static final Integer ELEVEN = 11;
    private static final Integer THIRTEEN = 13;
    private static final String X = "X";


    public static Boolean checkIsbn(String isbn) {
        //获取计算加权和所需要的值
        String substring = isbn.substring(ZERO, isbn.length() - ONE);
        //加权和S
        Integer s = ZERO;
        //由于ISBN，10位和13位校验方法不相同，先进行区分
        if (Pattern.matches(String.format(FORMAT_TEN, NINE), isbn)) {
            //获取比较值并转化为Integer
            Integer comparison = null;
            String comparisonStr = null;
            try {
                comparison = Integer.parseInt(isbn.substring(isbn.length() - 1));
            } catch (NumberFormatException e) {
                comparisonStr = X;
            }
            //转化为Integer数组
            Integer[] isbnArr;
            try {
                isbnArr = (Integer[]) ConvertUtils.convert(substring.split(""), Integer.class);
            } catch (UnknownFormatConversionException e) {
                log.error("ISBN编码转化Integer数组失败:{}", e);
                return false;
            }
            //递减值j
            Integer j = TEN;
            //计算加权和：即将ISBN号码前9位数字分别乘以10, 9, 8, 7, 6, 5, 4, 3, 2，然将它们相加，得到加权和
            for (int i = ZERO; i < isbnArr.length; i++, j--) {
                s = isbnArr[i] * j + s;
            }
            //取余数M
            Integer m = s % ELEVEN;
            //计算校验码N
            Integer N = ELEVEN - m;
            if (StringUtils.isNotEmpty(comparisonStr)) {
                return N.equals(TEN) ? true : false;
            } else {
                return ONE.equals(N) ? ZERO.equals(comparison) ? true : false : comparison.equals(N) ? true : false;
            }
        }
        if (Pattern.matches(String.format(FORMAT_THIRTEEN, THIRTEEN), isbn)) {
            //获取比较值并转化为Integer
            Integer comparison = Integer.parseInt(isbn.substring(isbn.length() - 1));
            //转化为Integer数组
            Integer[] isbnArr = (Integer[]) ConvertUtils.convert(substring.split(""), Integer.class);
            //计算加权和：用1分别乘ISBN的前12位中的奇数位，用3乘以偶数位，然后将两者相加，即得到加权和
            for (int i = ZERO; i < isbnArr.length; i++) {
                s=i%TWO==ZERO?isbnArr[i]*ONE+s:isbnArr[i]*THREE+s;
            }
            //取余数M
            Integer m = s % TEN;
            //计算校验码N
            Integer N = TEN - m;
            return N.equals(TEN) ? comparison.equals(ZERO) ? true : false : comparison.equals(N) ? true : false;
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(String.format("10位测试结果:%s", checkIsbn("7309045475")));
        System.out.println(String.format("13位测试结果:%s", checkIsbn("9787212058123")));
    }

}
