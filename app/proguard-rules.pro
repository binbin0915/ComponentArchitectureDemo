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

# -------------------------友盟统计相关-----------------------
-keep class com.umeng.** {*;}

-keep class org.repackage.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.uc.** { *; }
-keep class com.efs.** { *; }
# -------------------------友盟统计相关-----------------------

# -------------------------腾讯bugly相关-----------------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
# -------------------------腾讯bugly相关-----------------------