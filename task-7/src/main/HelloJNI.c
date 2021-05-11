#include <jni.h>        // JNI header provided by JDK
#include <stdio.h>      // C Standard IO Header
#include "HelloJNI.h"
#include <string.h>

JNIEXPORT jobject JNICALL Java_HelloJNI_getCpuInfo(JNIEnv *javaEnv, jclass cls){
     jclass map_class = (*javaEnv)->FindClass(javaEnv, "java/util/HashMap");
        if(map_class == NULL) {
            return NULL;
        }

        jsize map_class_len = 1;
        jmethodID constructor = (*javaEnv)->GetMethodID(javaEnv, map_class, "<init>", "(I)V");
        jobject map_object = (*javaEnv)->NewObject(javaEnv, map_class, constructor, map_class_len);
        jmethodID put = (*javaEnv)->GetMethodID(javaEnv, map_class, "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

        FILE *fp = fopen("/proc/cpuinfo", "r");
        char *line = NULL;
        while (1 > 0) {
            int n = getline(&line, &n, fp);
            if (n <= 0) break;
            if (n == 1) continue;
            char *c_key = strtok(line, " : ");
            char *c_value = line + strlen(c_key) + 1;
            c_value[strcspn(c_value, "\n")] = 0;

            jstring java_key = (*javaEnv)->NewStringUTF(javaEnv, c_key);
            jstring java_value = (*javaEnv)->NewStringUTF(javaEnv, c_value);

            (*javaEnv)->CallObjectMethod(javaEnv, map_object, put, java_key, java_value);
        }
        fclose (fp);
        jobject map_objectGlobal = (*javaEnv)->NewGlobalRef(javaEnv, map_object);

        return map_objectGlobal;
}
