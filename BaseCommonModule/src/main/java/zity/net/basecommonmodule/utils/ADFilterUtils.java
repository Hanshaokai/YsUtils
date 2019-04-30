package zity.net.basecommonmodule.utils;

import android.content.Context;
import android.content.res.Resources;

import zity.net.basecommonmodule.R;

/**
 * author：hanshaokai
 * date： 2019/1/22 0022 11:38
 * describe： 广告屏蔽过滤方法类
 */

public class ADFilterUtils {
    public ADFilterUtils() {
        throw new UnsupportedOperationException("can not instant");
    }

    // 校验第三方url是否在白名单
    public static Boolean thirdPartyWhiteListUrl(Context context, String url) {
        Resources resources = context.getResources();
        String[] urls = resources.getStringArray(R.array.whiteUrl);
        for (String s : urls) {
            if (url.startsWith(s)) {
                return true;
            }
        }
        return false;
    }


}
