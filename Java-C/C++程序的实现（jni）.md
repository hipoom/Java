# **Java调用C/C++程序的实现（jni）**
---

**转载请注明出处：**http://blog.csdn.net/H28496/article/details/49787459

## **1. 关于本地方法**
### 1. 如何声明本地方法？
通过native关键字可以声明一个本地方法。
```java
/**
 * 获得一个输入的字符
 */
public native char getChar();
```
### 2. 怎么加载dll文件？
```java
static{
	System.loadLibrary("NativeConsole"); // 需要和dll文件的名字相同
}
```

### 3. 代码示例
```java
package zhp;

/**
 * 一些和控制台有关的native类
 * @author 郑海鹏
 * @since 2015年11月11日
 */
public class NativeConsole {
	
	static{
		System.loadLibrary("NativeConsole");
	}
	
	/**
	 * 获得一个输入的字符
	 */
	public native char getChar();

	/**
	 * 设置控制台光标
	 */
	public native void setCursorPosition(int row, int col);
	
	/**
	 * 清屏
	 */
	public native void cls();
}
```

## **2. 如何调用本地方法**
```java
package zhp;

public class Test {

	public static void main(String[] args) {
		NativeConsole nativeConsole = new NativeConsole();
		nativeConsole.cls();	// 清屏
		nativeConsole.setCursorPosition(10, 10); // 设置光标位置
		
		while(true){
			char c = nativeConsole.getChar();
			System.out.println("[" + c + "]");
			if(c == '#'){ // 输入#退出
				break;
			}
		}
	}

}
```

## **3. 如何编译**
假设两个文件的路径为：
C:\src\zhp\NativeConsole.java
C:\src\zhp\Test.java

**操作流程如下：**
``` flow
st=>start: 
end=>end: 
classpath=>operation: 为你的计算机添加jdk/bin的路径到环境变量中
cmd=>operation: 切换工作目录到C:/src
javac1=>operation: 编译NativeConsole和Test类，生成*.class文件
javah=>operation: 生成NativeConsole.h头文件
vs1=>operation: 打开VS2013，创建一个win32程序，选择空的dll项目
copy1=>operation: 将..\jdk_8_60\include下面的文件（包括文件夹）复制到..\Visual Studio 2013\VC\include文件夹下
newH=>operation: 在VS项目的解决方案>头文件下添加一个现有项，把NativeConsole.h头文件加进去
newCpp=>operation: 在VS项目的解决方案>源文件下新建一个cpp文件，实现NativeConsole.h中声明的那些方法
isX64=>condition: 64位
changeToX64=>operation: 将活动解决方案平台改为X64
build=>operation: 编译程序，生成NativeConsole.dll
copyDll=>operation: 将编译好的dll程序复制到C:\src\文件夹下
run=>operation: 执行Test

st->classpath->cmd->javac1->javah->vs1->copy1->newH->newCpp->isX64
isX64(yes)->changeToX64->build
isX64(no)->build
build->copyDll->run->end

```

### 1. 怎么生成*.class文件
```
    c:\src>javac -encoding utf-8 ./zhp/Test.java
    c:\src>javac -encoding utf-8 ./zhp/NativeConsole.java
```
编码格式看情况可以去掉。

### 2. 怎么生成*.h头文件
```
    c:\src>javah zhp.NativeConsole
```
javah命令需要在包外面执行，且类名包含包名。生成的头文件放在了C:\src文件夹下。

### 3. 怎么将活动解决方案平台改为X64
在VS上 生成>配置管理器>活动解决方案平台>下拉选择新建>下拉键入或选择新平台>选择X64

### 4. 怎么执行
```
c:\src>java zhp/Test
```

**转载请注明出处：**http://blog.csdn.net/H28496/article/details/49787459
