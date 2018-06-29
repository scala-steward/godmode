package com.jcranky.godmode

import utest._
import utest.runner.Framework
import utest.ufansi.Attrs

class GodModeTestFramework extends Framework {

  override def exceptionStackFrameHighlighter(s: StackTraceElement): Boolean = {
    s.getClassName.contains("godmode")
  }

  override def exceptionLineNumberColor: Attrs = toggledColor(ufansi.Color.Cyan)

  override def exceptionMsgColor: Attrs = toggledColor(ufansi.Color.Yellow)

  override def formatWrapWidth: Int = 150
}
