import os
import time
timestr = time.strftime("%Y%m%d%H%M%S", time.localtime())

output = '/Users/chenglei/android-project/androidmonkey/mstMonkey/app/build/outputs/monkey-' + timestr

pkgname = 'com.android.mistong'

command = 'adb shell monkey -p ' + pkgname + ' --pct-syskeys 0 --pct-touch 100 --throttle 1000 -v 500 > ' + output + '.text'

os.system(command)

