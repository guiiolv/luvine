package com.luvine.common.domain.util;

public final class RequestInfoUtil {

    private RequestInfoUtil() {}

    public static String normalizeIp(String deviceInfo) {
        if (deviceInfo == null) return null;
        if (deviceInfo.contains(",")) {
            deviceInfo = deviceInfo.split(",")[0].trim();
        }
        return deviceInfo;
    }

    public static String truncateDeviceInfo(String deviceInfo) {
        if (deviceInfo == null) return null;
        return deviceInfo.substring(0, Math.min(deviceInfo.length(), 255));
    }
}