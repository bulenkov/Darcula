package com.bulenkov.darcula.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Konstantin Bulenkov
 */
public class StringUtil {
  public static List<String> split(String s, String separator) {
    return split(s, separator, true);
  }

  public static List<String> split(String s, String separator,
                                   boolean excludeSeparator) {
    return split(s, separator, excludeSeparator, true);
  }

  public static List<String> split(String s, String separator,
                                   boolean excludeSeparator, boolean excludeEmptyStrings) {
    if (separator.isEmpty()) {
      return Collections.singletonList(s);
    }
    List<String> result = new ArrayList<String>();
    int pos = 0;
    while (true) {
      int index = s.indexOf(separator, pos);
      if (index == -1) break;
      final int nextPos = index + separator.length();
      String token = s.substring(pos, excludeSeparator ? index : nextPos);
      if (!token.isEmpty() || !excludeEmptyStrings) {
        result.add(token);
      }
      pos = nextPos;
    }
    if (pos < s.length() || (!excludeEmptyStrings && pos == s.length())) {
      result.add(s.substring(pos, s.length()));
    }
    return result;
  }

  public static int naturalCompare(String string1, String string2) {
    return naturalCompare(string1, string2, false);
  }

  private static int naturalCompare(String string1, String string2, boolean caseSensitive) {
    final int string1Length = string1.length();
    final int string2Length = string2.length();
    for (int i = 0, j = 0; i < string1Length && j < string2Length; i++, j++) {
      char ch1 = string1.charAt(i);
      char ch2 = string2.charAt(j);
      if ((isDigit(ch1) || ch1 == ' ') && (isDigit(ch2) || ch2 == ' ')) {
        int startNum1 = i;
        while (ch1 == ' ' || ch1 == '0') { // skip leading spaces and zeros
          startNum1++;
          if (startNum1 >= string1Length) break;
          ch1 = string1.charAt(startNum1);
        }
        int startNum2 = j;
        while (ch2 == ' ' || ch2 == '0') {
          startNum2++;
          if (startNum2 >= string2Length) break;
          ch2 = string2.charAt(startNum2);
        }
        i = startNum1;
        j = startNum2;
        while (i < string1Length && isDigit(string1.charAt(i))) i++;
        while (j < string2Length && isDigit(string2.charAt(j))) j++;
        String digits1 = string1.substring(startNum1, i);
        String digits2 = string2.substring(startNum2, j);
        if (digits1.length() != digits2.length()) {
          return digits1.length() - digits2.length();
        }
        int numberDiff = digits1.compareTo(digits2);
        if (numberDiff != 0) {
          return numberDiff;
        }
        i--;
        j--;
        final int lengthDiff = (i - startNum1) - (j - startNum2);
        if (lengthDiff != 0) {
          return lengthDiff;
        }
        for (; startNum1 < i; startNum1++, startNum2++) {
          final int diff = string1.charAt(startNum1) - string2.charAt(startNum2);
          if (diff != 0) {
            return diff;
          }
        }
      }
      else {
        if (caseSensitive) {
          return ch1 - ch2;
        }
        else {
          // similar logic to charsMatch() below
          if (ch1 != ch2) {
            final int diff1 = toUpperCase(ch1) - toUpperCase(ch2);
            if (diff1 != 0) {
              final int diff2 = toLowerCase(ch1) - toLowerCase(ch2);
              if (diff2 != 0) {
                return diff2;
              }
            }
          }
        }
      }
    }
    if (!caseSensitive && string1Length == string2Length) {
      // do case sensitive compare if case insensitive strings are equal
      return naturalCompare(string1, string2, true);
    }
    return string1Length - string2Length;
  }

  public static char toUpperCase(char a) {
    if (a < 'a') {
      return a;
    }
    if (a <= 'z') {
      return (char)(a + ('A' - 'a'));
    }
    return Character.toUpperCase(a);
  }

  public static char toLowerCase(char a) {
    if (a < 'A' || a >= 'a' && a <= 'z') {
      return a;
    }

    if (a <= 'Z') {
      return (char)(a + ('a' - 'A'));
    }

    return Character.toLowerCase(a);
  }

  private static boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }
}
