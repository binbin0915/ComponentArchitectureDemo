# -------------------------腾讯tbs相关-----------------------
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}
# -------------------------腾讯tbs相关-----------------------

# -------------------------腾讯bugly相关-----------------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
# -------------------------腾讯bugly相关-----------------------