package org.junit.extensions.dynamicsuite.engine;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2014 Christof Schöll
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DirectoryScanner implements ClassScanner {

    public static final String CLASS_ENDING = ".class";
    public static final String SOURCE_ENDING = ".java";

    private final File basePath;

    private final List<String> foundClassNames = new ArrayList<String>();

    public DirectoryScanner(File basePath) {
        this.basePath = basePath;
        recursiveAdd(basePath);
    }

    @Override
    public List<String> listClassNames() {
        return foundClassNames;
    }

    protected void recursiveAdd(File current) {
        if (current.isDirectory()) {
            File[] files = current.listFiles();
            for (File file : files) {
                recursiveAdd(file);
            }
        } else {
            String basePathName = basePath.getAbsolutePath();
            String className = current.getAbsolutePath();
            String classNameLc = className.toLowerCase();
            if (classNameLc.endsWith(CLASS_ENDING) || classNameLc.endsWith(SOURCE_ENDING)) {
                className = extractClassName(basePathName, className);
                foundClassNames.add(className);
            }
        }
    }

    protected String extractClassName(String basePath, String classFileName) {
        String className = classFileName;
        className = StringUtils.removeStart(className, basePath);
        className = StringUtils.removeStart(className, File.separator);
        className = StringUtils.removeEnd(className, CLASS_ENDING);
        className = StringUtils.removeEnd(className, SOURCE_ENDING);
        className = StringUtils.replace(className, File.separator, ".");
        return className;
    }


}
