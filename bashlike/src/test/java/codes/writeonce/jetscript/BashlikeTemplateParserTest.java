/*
 * Copyright (c) 2015, Alexey Romenskiy, All rights reserved.
 *
 * This file is part of JetScript library
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package codes.writeonce.jetscript;

import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BashlikeTemplateParserTest {

    @Test
    public void testParseTemplate1() throws Exception {
        final PropertyResolver resolver = getResolver("path", "test.txt");
        assertEquals("test.foo", new BashlikeTemplateParser().parseTemplate(resolver, "${path/\\.txt$/.foo}").evaluate());
    }

    @Test
    public void testParseTemplate2() throws Exception {
        final PropertyResolver resolver = getResolver("foo", "abc", "bar", "def");
        assertEquals("adefc", new BashlikeTemplateParser().parseTemplate(resolver, "${foo/b/$bar}").evaluate());
        assertEquals("a3c", new BashlikeTemplateParser().parseTemplate(resolver, "${foo/b/$#bar}").evaluate());
        assertEquals("azdefxc", new BashlikeTemplateParser().parseTemplate(resolver, "${foo/b/z${bar}x}").evaluate());
        assertEquals("az3xc", new BashlikeTemplateParser().parseTemplate(resolver, "${foo/b/z${#bar}x}").evaluate());
        assertEquals("a$barc", new BashlikeTemplateParser().parseTemplate(resolver, "${foo/b/\\$bar}").evaluate());
        assertEquals("a\\$barc", new BashlikeTemplateParser().parseTemplate(resolver, "${foo/b/\\\\\\$bar}").evaluate());
        assertEquals("a\\defc", new BashlikeTemplateParser().parseTemplate(resolver, "${foo/b/\\\\$bar}").evaluate());
    }

    @Test
    public void testParseTemplate3() throws Exception {
        final PropertyResolver resolver = getResolver("foo", "abc", "bar", "DEF");
        assertEquals("ABC", new BashlikeTemplateParser().parseTemplate(resolver, "${foo~uc}").evaluate());
        assertEquals("def", new BashlikeTemplateParser().parseTemplate(resolver, "${bar~lc}").evaluate());
    }

    @Test
    public void testParseTemplate4() throws Exception {
        final PropertyResolver resolver = getResolver("foo", "abc", "bar", "def");
        assertEquals("abc", new BashlikeTemplateParser().parseTemplate(resolver, "${foo-asd}").evaluate());
        assertEquals("asd", new BashlikeTemplateParser().parseTemplate(resolver, "${zxc-asd}").evaluate());
        assertEquals("abc", new BashlikeTemplateParser().parseTemplate(resolver, "${foo-$bar}").evaluate());
        assertEquals("def", new BashlikeTemplateParser().parseTemplate(resolver, "${zxc-$bar}").evaluate());
    }

    private PropertyResolver getResolver(String... entries) {

        assertEquals(0, entries.length % 2);
        final Map<String, PropertyHolder> map = new HashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            final String value = entries[i + 1];
            map.put(entries[i], new PropertyHolder() {
                @Override
                public boolean isConstant() {
                    return true;
                }

                @Override
                public String getValue() throws PropertyResolverException {
                    return value;
                }
            });
        }

        return new PropertyResolver() {
            @Nonnull
            @Override
            public PropertyHolder getPropertyHolder(String propertyName) throws PropertyResolverException {
                final PropertyHolder propertyHolder = map.get(propertyName);
                if (propertyHolder == null) {
                    return new PropertyHolder() {
                        @Override
                        public boolean isConstant() {
                            return true;
                        }

                        @Override
                        public String getValue() throws PropertyResolverException {
                            return null;
                        }
                    };
                }
                return propertyHolder;
            }
        };
    }
}
