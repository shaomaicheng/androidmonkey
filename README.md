使用方式

```
compile 'com.mistong.android:ewtmonkey:1.0.0'
```

在 AndroidManifest.xml 指定 instrumentation

```xml
<instrumentation
        android:name="com.mistong.andorid.monkey.MstMonkeyInstrumentation"
        android:targetPackage="YOU_PACKAGE_NAME"/>
```

启动后运行如下命令：

`adb shell am instrument MONKEY_TEST_PACKAGE/RUNNER_CLASS`

（下面这条命令参考 monkey 文档用法）

`adb shell monkey -p MONKEY_TEST_PACKAGE --throttle 300 --pct-touch 60 --pct-motion 15 --pct-syskeys 10 --pct-appswitch 15 -s `date +%H%M%S` -v -v -v --monitor-native-crashes --ignore-timeouts  --hprof --bugreport  COUNT > monkey_test.txt`
