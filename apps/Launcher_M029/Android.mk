#2016-06-14 add
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := Launcher_M029
LOCAL_MODULE_TAGS := optional

LOCAL_CERTIFICATE := platform
#LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_OVERRIDES_PACKAGES := Launcher2

LOCAL_MULTILIB := 32
LOCAL_SRC_FILES := \
    $(call all-java-files-under, src) \
    $(call all-renderscript-files-under, src)

LOCAL_STATIC_JAVA_LIBRARIES := \
    android-common \
    android-support-v13

LOCAL_JAVA_LIBRARIES := \
    services \
    com.mstar.android

include $(BUILD_PACKAGE)

include $(call all-makefiles-under,$(LOCAL_PATH))
