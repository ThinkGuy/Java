package com.filetool.main;

import com.filetool.util.FileUtil;
import com.filetool.util.LogUtil;
import com.routesearch.route.Route;

/**
 * 工具入口
 * 
 * @author
 * @since 2016-3-1
 * @version v1.0
 */
public class Main
{
    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.err.println("please input args: graphFilePath, conditionFilePath, resultFilePath");
            return;
        }

        String graphFilePath = args[0];
        String conditionFilePath = args[1];
        String resultFilePath = args[2];

        LogUtil.printLog("Begin");

        // 读取输入文件
        String graphContent = FileUtil.read(graphFilePath, null);
        String conditionContent = FileUtil.read(conditionFilePath, null);

        // 功能实现入口
        String resultStr = Route.searchRoute(graphContent, conditionContent);

        // 写入输出文件
        FileUtil.write(resultFilePath, resultStr, false);

        LogUtil.printLog("End");
    }

}
