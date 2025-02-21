package com.progressoft.FxDealsWarehouse.util;

import java.nio.file.Path;
import java.util.Set;

@FunctionalInterface
public interface FileReader {
    Set<String> read ( Path path );
}