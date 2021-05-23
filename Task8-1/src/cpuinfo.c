#include <graalvm/llvm/polyglot.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void* cpuinfo() {

    void *mapClass = polyglot_java_type("java.util.HashMap");
	void *hashMap = polyglot_new_instance(mapClass);

    FILE *fp = fopen ("/proc/cpuinfo", "r");

    char *line = NULL;
    int n = 0;
    while (1 > 0) {
        int n = getline(&line, &n, fp);
        if (n <= 0) break;
        if (n == 1) continue;

        char *c_key = strtok(line, ": ");
        char *c_value = line + strlen(c_key) + 1;
        c_value[strcspn(c_value, "\n")] = 0;

        void *key = polyglot_from_string(c_key, "UTF-8");
		void *value = polyglot_from_string(c_value, "UTF-8");

        polyglot_invoke(hashMap, "put", key, value);
    }

    fclose (fp);
    return hashMap;
}