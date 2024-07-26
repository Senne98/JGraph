package org.senne.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static List reverseList(List list) {
        List reversedList = new ArrayList();
        for (int i = list.size() - 1; i >= 0; i--) {
            reversedList.add(list.get(i));
        }
        return reversedList;
    }
}
