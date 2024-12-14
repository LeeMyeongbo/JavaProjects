package com.sec.bestreviewer;

import java.util.List;

public class TokenGroup {

    String type;
    List<String> options;
    List<String> params;

    TokenGroup(String type, List<String> options, List<String> params) {
        this.type = type;
        this.options = options;
        this.params = params;
    }

    String getType() {
        return type;
    }

    List<String> getOptions() {
        return options;
    }

    List<String> getParams() {
        return params;
    }

}
