
#include "route.h"
#include "lib_record.h"
#include <stdio.h>

//你要完成的功能总入口
void search_route(char *topo[5000], int edge_num, char *demand)
{
    unsigned short result[] = {2, 6, 3};//示例中的一个解

    for (int i = 0; i < 3; i++)
        record_result(result[i]);
}
