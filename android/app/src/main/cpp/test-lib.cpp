//
// Created by wingjay on 2018/7/4.
// Refer to http://cfanr.cn/2017/08/05/Android-NDK-dev-JNI-s-practice/
//
#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jint JAVA_com_wingjay_titan_android_jni_Counter_increase(
        JNIEnv *env,
        jobject jobj
) {
    // 1. 获取class；2. 读取class里的count变量ID；3. 读取该变量ID的值并自增
    jclass jclazz = env->GetObjectClass(jobj);
    jfieldID fid = env->GetFieldID(jclazz, "num", "I");
    jint count = env->GetIntField(jobj, fid);
    count++;
    return count;
}

extern "C"
JNIEXPORT void JAVA_com_wingjay_titan_android_jni_Counter_processStatic(
        JNIEnv *env,
        jobject jobj
)
{
    jclass jclazz = env->GetObjectClass(jobj);
    jfieldID fid = env->GetStaticFieldID(jclazz, "staticCount", "Ljava/lang/String");
    jstring value = (jstring) env->GetStaticObjectField(jclazz, fid);
    // change value
    std::string str = env->GetStringUTFChars(value, JNI_FALSE);
    str.assign("static: after jni");
    // convert to jstring
    jstring newValue = env->NewStringUTF(str.c_str());
    env->SetStaticObjectField(jclazz, fid, newValue);
}

extern "C"
JNIEXPORT void JAVA_com_wingjay_titan_android_jni_Counter_innerIncrease(
        JNIEnv *env,
        jobject jobj
)
{
    jclass jclazz = env->GetObjectClass(jobj);
    jfieldID fid = env->GetFieldID(jclazz, "innerCount", "I");
    jint value = env->GetIntField(jobj, fid);
    env->SetIntField(jobj, fid, value + 10);
}

extern "C"
JNIEXPORT jint JAVA_com_wingjay_titan_android_jni_Counter_sumOf(
        JNIEnv *env,
        jobject jobj,
        jintArray input
)
{
    jint* array = env->GetIntArrayElements(input, JNI_FALSE);
    jint len = env->GetArrayLength(input);
    jint sum = 0;
    for (jint i = 0; i < len; ++i) {
        sum += array[i];
    }
    env->ReleaseIntArrayElements(input, array, 0); // ?
    return sum;
}

extern "C"
JNIEXPORT jobject JAVA_com_wingjay_titan_android_jni_Counter_TenYearsYounger(
        JNIEnv *env,
        jobject jobj,
        jobject oldPerson
)
{
    jclass jclazz = env->GetObjectClass(oldPerson);
    jmethodID constructMId = env->GetMethodID(jclazz, "<init>", "(ILjava/lang/String;)V");

    jfieldID nameFId = env->GetFieldID(jclazz, "name", "Ljava/lang/String");
    jstring jName = (jstring) env->GetObjectField(oldPerson, nameFId);

    // change name
    std::string name = env->GetStringUTFChars(jName, JNI_FALSE);
    size_t newNameLen = 10+strlen(name.c_str());
    char newName[newNameLen];
    strcat(newName, "New: ");
    strcat(newName, name.c_str());

    jstring j_new_name = env->NewStringUTF(newName);

    // change age
    jfieldID ageFId = env->GetFieldID(jclazz, "age", "I");
    jint age = env->GetIntField(oldPerson, ageFId);
    age -= 10;

    return env->NewObject(jclazz, constructMId, age, j_new_name);
}

// 注册函数映射表
static int registerNativeMethods(JNIEnv *env,
                                 const char* className,
                                 const JNINativeMethod* methods,
                                 int methodNum)
{
    jclass jclazz = env->FindClass(className);
    jint ret = env->RegisterNatives(jclazz, methods, methodNum);
    if (ret < 0) {
        return JNI_ERR;
    }
    env->DeleteLocalRef(jclazz);
    return JNI_OK;
}

// 该函数为自动注册的
static jstring autoRegister(JNIEnv *env, jobject, jstring input)
{
    std::string val = env->GetStringUTFChars(input, JNI_FALSE);
    char newVal[10 + strlen(val.c_str())];
    strcat(newVal, "AutoRegister: ");
    strcat(newVal, val.c_str());
    return env->NewStringUTF(newVal);
}

jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    JNIEnv *env = nullptr;
    if(vm->GetEnv((void **)&env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_VERSION_1_4;
    }

    // 通过修改下面的className 和 methods 就能自动注册 java class 里的native方法
    const char* className = "com.wingjay.titan_android.jni";
    JNINativeMethod methods[] = {
            {"autoRegisterFun", "(Ljava/lang/String;)Ljava/lang/String;", (void*) autoRegister},
    };
    registerNativeMethods(
            env,
            className,
            methods,
            sizeof(methods) / sizeof(JNINativeMethod)
    );
}