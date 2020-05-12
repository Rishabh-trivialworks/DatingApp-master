package com.quintus.labs.datingapp.xmpp.utils;


import com.quintus.labs.datingapp.Utils.TempStorage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextPreventionHelper {

    public static void getPreventedString(String messageText) {
//        TempStorage.getPreventTextList();
//
//        StringBuffer buffer = new StringBuffer(messageText);
//
////        Arabic (0600—06FF, 225 characters)
////        Arabic Supplement (0750—077F, 48 characters)
////        Arabic Extended-A (08A0—08FF, 39 characters)
////        Arabic Presentation Forms-A (FB50—FDFF, 608 characters)
////        Arabic Presentation Forms-B (FE70—FEFF, 140 characters)
////        Rumi Numeral Symbols (10E60—10E7F, 31 characters)
////        Arabic Mathematical Alphabetic Symbols (1EE00—1EEFF, 143 characters)
//
////        final String REGEX_WORD = "\\b(\\w*[a-zA-Z\u0600-\u06ff]{2,}\\w*)\\b";
////
////        Matcher matcher = Pattern.compile(REGEX_WORD).matcher(messageText);
////        while (matcher.find()) {
////
////            final String word = messageText.substring(matcher.start(), matcher.end());
////
////            String replacement = TempStorage.getPreventTextHash().get(word.toLowerCase());
////            if (replacement != null) {
////                buffer.replace(matcher.start(), matcher.end(), replacement);
////            }
////        }
//
////        StringBuffer regex = null;
//
//        if (TempStorage.getPreventTextHash().isEmpty()) {
//            return messageText;
//        } else {
////            regex = new StringBuffer();
////            regex.append("\\b(\\w*");
////
////            for (int i = 0; i < TempStorage.getPreventTextHash().size(); i++) {
////                regex.append(TempStorage.getPreventTextList().get(i).getText());
////                if (i != TempStorage.getPreventTextList().size() - 1) {
////                    regex.append("|");
////                }
////            }
////            regex.append("\\w*)\\b");
//
//            Matcher matcher = Pattern.compile(TempStorage.getTextPreventionRegex(), Pattern.CASE_INSENSITIVE).matcher(messageText);
//            while (matcher.find()) {
//
//                final String word = messageText.substring(matcher.start(), matcher.end());
//
//                String replacement = TempStorage.getPreventTextHash().get(word.toLowerCase());
//                if (replacement != null) {
//                    buffer.replace(matcher.start(), matcher.end(), replacement);
//                }
//            }
//        }
//        return buffer.toString();
    }

}
