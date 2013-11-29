#!/bin/sh
BLOCKLAUNCHER_REPO_PATH=~/fromdesktop/Documents/repos/MCPELauncher
javadoc -docletpath bin -classpath bin:$BLOCKLAUNCHER_REPO_PATH/bin/classes:$BLOCKLAUNCHER_REPO_PATH/libs/smalljs.jar:\
$ANDROID_HOME/platforms/android-10/android.jar -sourcepath $BLOCKLAUNCHER_REPO_PATH/src \
 -doclet net.zhuoweizhang.mcpelauncher.doclet.BlueberryDoclet net.zhuoweizhang.mcpelauncher
