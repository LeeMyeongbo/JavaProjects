package com.sec.bestreviewer.util;

import java.util.List;

import static com.sec.bestreviewer.util.OptionType.*;

public class OptionParser {

    private boolean isPrintOn = false;
    private boolean isAndCondition = false;
    private boolean isOrCondition = false;
    private OptionGroup optionGroup;

    public OptionParser(List<String> options) {
        if (options.isEmpty()) {
            return;
        }
        isPrintOn = PRINT.equals(options.get(0));

        if (options.size() > 3) {
            String conditionOption = options.get(3);
            isAndCondition = AND.equals(conditionOption);
            isOrCondition = OR.equals(conditionOption);
        }

        if (isSecondOptionGroupExist()) {
            optionGroup = new OptionGroup(options.get(1), options.get(2), options.get(4), options.get(5));
        } else {
            optionGroup = new OptionGroup(options.get(1), options.get(2), NO_OPTION, NO_OPTION);
        }
    }

    public boolean isSecondOptionGroupExist() {
        return isAndCondition || isOrCondition;
    }

    public boolean isPrintOn() {
        return isPrintOn;
    }

    public boolean isAndCondition() {
        return isAndCondition;
    }

    public boolean isOrCondition() {
        return isOrCondition;
    }

    public OptionGroup getOptionGroup() {
        return optionGroup;
    }
}
