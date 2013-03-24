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
}
