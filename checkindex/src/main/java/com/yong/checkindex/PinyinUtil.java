package com.yong.checkindex;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by yanyl on 2016/12/29.
 */
public class PinyinUtil {

    public static String getPinyin(String chinese){

        if(TextUtils.isEmpty(chinese)) return null;
        //设置转化的拼音大小写，或声调

        HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        //1、由于只能对单个汉子转化，所以将字符串转化为字符数组，然后对单个字符转化，最后拼接
        char[] charArray=chinese.toCharArray();
        String pinyin="";
        for (int i=0;i<charArray.length;i++){
            // 2.过滤空格
            if (Character.isWhitespace(charArray[i])) continue;
            //3.判断字符是否是汉字
            //汉字占两个字节一个字节的范围-128···127 汉字大于127
            if (charArray[i]>127){
                try {
                    //出现多音字
                    String [] pinyinArr= PinyinHelper.toHanyuPinyinStringArray(charArray[i],format);
                   if (pinyinArr!=null){
                       pinyin+=pinyinArr[0];//取第一个
                   }else{
                       //忽略
                   }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyin+=charArray[i];
            }
        }
        return pinyin;
    }

}
