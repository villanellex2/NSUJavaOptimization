#include <jni.h>        // JNI header provided by JDK
#include <stdio.h>      // C Standard IO Header
#include "HelloJNI.h"
#include <string.h>

JNIEXPORT jobject JNICALL Java_HelloJNI_getCpuInfo(JNIEnv *javaEnv, jclass cls){
     jclass map = (*javaEnv)->FindClass(javaEnv, "java/util/HashMap");
        if(map == NULL) {
            return NULL;
        }

        jsize map_len = 1;
        jmethodID init = (*javaEnv)->GetMethodID(javaEnv, map, "<init>", "(I)V");
        jobject hashMap = (*javaEnv)->NewObject(javaEnv, map, init, map_len);
        jmethodID put = (*javaEnv)->GetMethodID(javaEnv, map, "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

        FILE *fp = fopen("/proc/cpuinfo", "r");
        char *line = NULL;
        while (1 > 0) {
            int n = getline(&line, &n, fp);
            if (n <= 0) break;
            if (n == 1) continue;
            char *keyString = strtok(line, " : ");
            char *valueString = line + strlen(keyString) + 1;
            valueString[strcspn(valueString, "\n")] = 0;

            jstring key = (*javaEnv)->NewStringUTF(javaEnv, keyString);
            jstring value = (*javaEnv)->NewStringUTF(javaEnv, valueString);

            (*javaEnv)->CallObjectMethod(javaEnv, hashMap, put, key, value);

            (*javaEnv)->DeleteLocalRef(javaEnv, key);
            (*javaEnv)->DeleteLocalRef(javaEnv, value);
        }
        fclose (fp);
        jobject hashMapGlobal = (*javaEnv)->NewGlobalRef(javaEnv, hashMap);
        (*javaEnv)->DeleteLocalRef(javaEnv, hashMap);
        (*javaEnv)->DeleteLocalRef(javaEnv, map);

        return hashMapGlobal;
}
