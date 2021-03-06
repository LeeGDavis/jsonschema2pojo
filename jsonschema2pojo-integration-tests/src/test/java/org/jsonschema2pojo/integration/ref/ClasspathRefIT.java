/**
 * Copyright © 2010-2013 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jsonschema2pojo.integration.ref;

import static org.jsonschema2pojo.integration.util.CodeGenerationHelper.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class ClasspathRefIT {

    private static Class<?> classpathRefsClass;

    @BeforeClass
    public static void generateAndCompileEnum() throws ClassNotFoundException {

        ClassLoader classpathRefsClassLoader = generateAndCompile("/schema/ref/classpathRefs.json", "com.example");

        classpathRefsClass = classpathRefsClassLoader.loadClass("com.example.ClasspathRefs");

    }

    @Test
    public void refToClasspathResourceUsingClasspathProtocolIsReadSuccessfully() throws NoSuchMethodException {

        Class<?> aClass = classpathRefsClass.getMethod("getPropertyClasspathRef").getReturnType();

        assertThat(aClass.getName(), is("com.example.PropertyClasspathRef"));
        assertThat(aClass.getMethods(), hasItemInArray(hasProperty("name", equalTo("getPropertyOfA"))));
    }

    @Test
    public void refToClasspathResourceUsingResourceProtocolIsReadSuccessfully() throws NoSuchMethodException {

        Class<?> aClass = classpathRefsClass.getMethod("getPropertyResourceRef").getReturnType();

        assertThat(aClass.getName(), is("com.example.PropertyResourceRef"));
        assertThat(aClass.getMethods(), hasItemInArray(hasProperty("name", equalTo("getTitle"))));
    }

    @Test
    public void refToClasspathResourceUsingJavaProtocolIsReadSuccessfully() throws NoSuchMethodException {

        Class<?> aClass = classpathRefsClass.getMethod("getPropertyJavaRef").getReturnType();

        assertThat(aClass.getName(), is("com.example.PropertyJavaRef"));
        assertThat(aClass.getMethods(), hasItemInArray(hasProperty("name", equalTo("getDescription"))));
    }

    @Test
    public void relativeRefToClasspathResourceWithinClasspathResource() throws NoSuchMethodException {

        Class<?> aClass = classpathRefsClass.getMethod("getTransitiveRelativeClasspathRef").getReturnType();

        assertThat(aClass.getName(), is("com.example.TransitiveRelativeClasspathRef"));
        assertThat(aClass.getMethods(), hasItemInArray(hasProperty("name", equalTo("getPropertyRelativeRef"))));

        Class<?> bClass = aClass.getMethod("getPropertyRelativeRef").getReturnType();

        assertThat(bClass.getName(), is("com.example.PropertyRelativeRef"));
        assertThat(bClass.getMethods(), hasItemInArray(hasProperty("name", equalTo("getTransitive"))));

    }

}
