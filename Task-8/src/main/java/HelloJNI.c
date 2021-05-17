#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char* getCPUInfo(){
    FILE *fp = fopen("/proc/cpuinfo", "r");long size = fsize(fp);
    char* fcontent;
    fcontent = malloc(size);
    fread(fcontent, 1, size, fp);
    return fcontent;
}
