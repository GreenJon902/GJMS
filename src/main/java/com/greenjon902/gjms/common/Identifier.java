package com.greenjon902.gjms.common;

import java.util.regex.Pattern;

public interface Identifier {
    String getNamespace();
    String getValue();
    String build();
}
