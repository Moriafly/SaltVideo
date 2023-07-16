package com.salt.video.util.sort;

import java.util.Comparator;

/**
 * Compares Strings (or any other CharSequence subclass) using the
 * <a href="http://blog.codinghorror.com/sorting-for-humans-natural-sort-order/">natural sort</a> /
 * <a href="http://www.davekoelle.com/alphanum.html">alphanum algorithm</a> which gives a more
 * "natural" ordering when presenting the sorted list of strings to humans.
 *
 * <p>
 * This is a fast implementation of the original algorithm which produces no garbage during its run.
 * There is also a case-insensitive variant you might want to use:
 * CaseInsensitiveSimpleNaturalComparator. This is a fully self-contained implementation
 * compiled with Java 1.6 for maximum compatibility which does not add any additional dependencies
 * to your project.
 *
 * <p>
 * There are still limitations of this implementation to be aware of (which hopefully will be
 * addressed in future releases):
 *
 * <ul>
 * <li>Does not play nice with Unicode, especially characters which are outside of the BMP (ie.
 * codepoints with values larger than {@link Character#MAX_VALUE}).</li>
 * <li>Does not handle fractions or grouping characters properly.</li>
 * <li>Only understands integer values up to 2^64-1.</li>
 * </ul>
 */
public final class SimpleNaturalComparator<T extends CharSequence>
        extends AbstractSimpleNaturalComparator<T> implements Comparator<T> {
    @SuppressWarnings("rawtypes")
    private static final Comparator INSTANCE = new SimpleNaturalComparator();

    private SimpleNaturalComparator() {
        // to be instantiated only internally
    }

    @Override
    int compareChars(char c1, char c2) {
        return c1 - c2;
    }

    @SuppressWarnings("unchecked")
    public static <T extends CharSequence> Comparator<T> getInstance() {
        return INSTANCE;
    }
}
