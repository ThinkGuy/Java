看到中文readme是一件非常亲切的事情！仔细看完此文档即可完成第一个用例：

1、整体指引：
  1)使用一键式脚本编译后打包;
  2)如果编译成功会在bin路径下生成可执行二进制文件"future_net.jar"；
  3)使用如下格式调用并调试程序，例如：
	Linux环境：		root> sh startup.sh /xxx/topo.csv /xxx/demand.csv /xxx/result.csv
	(说明：topo.csv和demand.csv是输入文件，result.csv是输出文件)
  4)将future_net.jar与代码打包压缩到同一个压缩包下，压缩包格式为tar.gz，即可上传答案。要求future_net.jar在压缩包中不得放到文件夹内，如下路径结构所示。
  futrue_net.tat.gz
    └── future_net.jar
    └── code/

2、SDK目录结构：
├── bin/                        二进制文件路径
	└── future_net.jar			可执行的jar文件，由一键式打包脚本生成
	└── startup.bat				Windows环境一键式执行脚本
	└── startup.sh				Linux环境一键式执行脚本
├── code/						源代码路径
	├── future_net/             
		├── bin/				编译后创建的class文件
		├── src/				java源代码路径
		└── build.txt			需要编译的java文件说明
├── test						测试文件路径
└── build.bat                   Windows环境一键式打包脚本
└── build.sh                   	Linux环境一键式打包脚本
└── makelist.txt                需要编译的java文件，供脚本调用
└── readme.txt                  你正在看的文件 -_-" 这不用介绍了吧
	注意：
	1)如果增加了源文件需要修改makelist.txt文件；
	2)如果运行时提示startup.sh文件第14行错误，请按照你本地系统情况修改此行，此脚本只是用来方便调用，修改后不会影响在判题平台的判题。

3、SDK代码说明：
  我们已经提供了保姆式的服务，你只需要做：
  1)完成Route.java文件中的searchRoute方法；
  2)返回结果并输出;
  3)如果计算结果为没有路径，则直接输出NA。
  SDK已经实现了读取文件、按要求格式写文件以及打印开始和结束时间的功能。为了便于调试，SDK将读入的信息全部在屏幕输出，可根据自身的需要酌情删除此打印信息。
  注意：读取文件功能是指，将图的信息文件和路径信息文件按行读取到内存，其在内存中的存储格式仍是字符串格式。因为这些信息以什么格式存储涉及到算法设计，这样做是为了不禁锢你的思路。