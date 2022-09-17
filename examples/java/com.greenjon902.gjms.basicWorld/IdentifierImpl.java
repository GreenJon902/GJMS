package com.greenjon902.gjms.basicWorld;

import com.greenjon902.gjms.common.Identifier;

import java.util.regex.Pattern;

public class IdentifierImpl implements Identifier {
    private static Pattern namespacePattern = Pattern.compile("[a-z0-9.\\-_]");
    private static Pattern valuePattern = Pattern.compile("[a-z0-9.\\-_/]");

    private final String namespace;
    private final String value;

    public IdentifierImpl(String namespace, String value) {
        assert namespacePattern.matcher(namespace).matches();
        assert valuePattern.matcher(value).matches();

        this.namespace = namespace;
        this.value = value;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String build() {
        return namespace + ":" + value;
    }
}
