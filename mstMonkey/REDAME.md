`adb shell am instrument MONKEY_TEST_PACKAGE/RUNNER_CLASS`

'adb shell monkey -p MONKEY_TEST_PACKAGE --throttle 300 --pct-touch 60 --pct-motion 15 --pct-syskeys 10 --pct-appswitch 15 -s `date +%H%M%S` -v -v -v --monitor-native-crashes --ignore-timeouts  --hprof --bugreport  COUNT > monkey_test.txt'