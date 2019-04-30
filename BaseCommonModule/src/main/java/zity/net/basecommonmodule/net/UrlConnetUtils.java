package zity.net.basecommonmodule.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.utils.LogUtils;
import zity.net.basecommonmodule.utils.SPUtils;

/**
 * author：hanshaokai
 * date： 2018/7/27 0027 14:50
 * describe：
 */

public class UrlConnetUtils {
    public static String connetUrlParams(String url, Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        /*        stringBuilder.append(Constants.BASE_URL);*/
        stringBuilder.append(url);
        if (map != null && map.size() != 0) {
            int i = 0;
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> enty : entries) {
                if (i == 0) {
                    //判断之前有参数则不需要加？
                    if (url.contains("?")) {
                        stringBuilder.append("&");
                    } else {
                        stringBuilder.append("?");
                    }
                    stringBuilder.append(enty.getKey());
                    stringBuilder.append("=");
                    stringBuilder.append(enty.getValue());
                } else {
                    stringBuilder.append("&");
                    stringBuilder.append(enty.getKey());
                    stringBuilder.append("=");
                    stringBuilder.append(enty.getValue());
                }
                i++;
            }
        }
        LogUtils.d("加载网页：" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static String connetUserIdUrl(String url) {
        Map<String, String> map = new HashMap<>();
        int userId = SPUtils.getInstance().getInt(Constants.USER_ID_KEY);
        if (userId == -1) {
            map.put("userId", "");
        } else {
            map.put("userId", userId + "");
        }
    /*    if (url.equals(UrlContants.student_class_page) || url.equals(UrlContants.student_main_page)
                || url.equals(UrlContants.teacher_team_page)
                || url.equals(UrlContants.student_team_page)
                || url.equals(UrlContants.parent_team_page)
                ) {
        }*/
        String orgId = SPUtils.getInstance().getInt(Constants.ORG_ID_KEY)+"";
        if (!orgId.equals("-1")) {
            map.put("orgId", orgId + "");
        } else {
            map.put("orgId", orgId + "");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);
        if (map != null && map.size() != 0) {
            int i = 0;
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> enty : entries) {
                if (i == 0) {
                    //判断之前有参数则不需要加？
                    if (url.contains("?")) {
                        stringBuilder.append("&");
                    } else {
                        stringBuilder.append("?");
                    }
                    stringBuilder.append(enty.getKey());
                    stringBuilder.append("=");
                    stringBuilder.append(enty.getValue());
                } else {
                    stringBuilder.append("&");
                    stringBuilder.append(enty.getKey());
                    stringBuilder.append("=");
                    stringBuilder.append(enty.getValue());
                }
                i++;
            }
        }
        LogUtils.d("加载网页：" + stringBuilder.toString());
        return stringBuilder.toString();
    }
}
