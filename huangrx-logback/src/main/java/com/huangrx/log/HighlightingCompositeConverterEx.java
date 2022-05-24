package com.huangrx.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * Logback 自定义突出显示颜色
 *
 * @author    hrenxiang
 * @since     2022/5/24 1:05 PM
 */
public class HighlightingCompositeConverterEx extends ForegroundCompositeConverterBase<ILoggingEvent> {

    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        Level level = event.getLevel();
        switch (level.toInt()) {
        case Level.ERROR_INT:
            // same as default color scheme
            return ANSIConstants.BOLD + ANSIConstants.RED_FG;
        case Level.WARN_INT:
            // same as default color scheme
            return ANSIConstants.RED_FG;
        case Level.INFO_INT:
            // use CYAN instead of BLUE
            return ANSIConstants.CYAN_FG;
        default:
            return ANSIConstants.DEFAULT_FG;
        }
    }

}