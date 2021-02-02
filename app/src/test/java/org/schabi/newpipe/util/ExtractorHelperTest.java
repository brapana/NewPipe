package org.schabi.newpipe.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Testing class that tests ExtractorHelper's capitalizeIfAllUppercase() function.
 * Each function tests one of the following 9 input partitions (see report document for details).
 * 1. Empty strings (consisting of strings of size 0, or purely whitespace [“   ”, “”, etc.]).
 * 2. Strings that are made up of only lowercase characters (“string”, etc.).
 * 3. Strings that are made up of only uppercase characters (“STRING”, etc.).
 * 4. Strings that are made up of only uppercase characters including symbols (“STRING!”, etc.).
 * 5. Strings with a single uppercase letter at the beginning (“String”, etc.).
 * 6. Strings with a single uppercase letter at the end (“strinG”, etc.).
 * 7. Strings with a single lowercase letter at the beginning (“sTRING”, etc.).
 * 8. Strings with a single lowercase letter at the end (“STRINg”, etc.).
 * 9. Mixed strings that do not follow any of the above partition schemes (“sTrInG!”, etc.)
 */
public class ExtractorHelperTest {
    /**
     * 1. emptyStrings() – asserts that inputs “” and  “  ” correctly return “” and “ ”,
     * respectively.
     */
    @Test
    public void emptyStrings() {
        assertEquals("", ExtractorHelper.capitalizeIfAllUppercase(""));
        assertEquals("  ", ExtractorHelper.capitalizeIfAllUppercase("  "));
    }

    /**
     * 2. allLowercase() – asserts that inputs “string” and “this is a string” correctly
     * return the original strings.
     */
    @Test
    public void allLowercase() {
        assertEquals("string", ExtractorHelper.capitalizeIfAllUppercase("string"));
        assertEquals("this is a string",
                ExtractorHelper.capitalizeIfAllUppercase("this is a string"));
    }

    /**
     * 3. allUppercase() – asserts that inputs “STRING” and “THIS IS A STRING” correctly return
     * the modified title cased string. This partition and the partition below are the only
     * partitions that should change the original string.
     */
    @Test
    public void allUppercase() {
        assertEquals("String", ExtractorHelper.capitalizeIfAllUppercase("STRING"));
        assertEquals("This is a string",
                ExtractorHelper.capitalizeIfAllUppercase("THIS IS A STRING"));
    }

    /**
     * 4. allUppercaseWithSymbols() – asserts that inputs “STRING!” and “THIS? IS? A? STRING?”
     * correctly return the modified title cased strings (leaving the symbols where they are).
     */
    @Test
    public void allUppercaseWithSymbols() {
        assertEquals("String!", ExtractorHelper.capitalizeIfAllUppercase("STRING!"));
        assertEquals("This? is? a? string?",
                ExtractorHelper.capitalizeIfAllUppercase("THIS? IS? A? STRING?"));
    }

    /**
     * 5. singleUppercaseAtBeginning() – asserts that inputs “String” and “This is a String”
     * correctly return the original strings.
     */
    @Test
    public void singleUppercaseAtBeginning() {
        assertEquals("String", ExtractorHelper.capitalizeIfAllUppercase("String"));
        assertEquals("This is a string",
                ExtractorHelper.capitalizeIfAllUppercase("This is a string"));
    }

    /**
     * 6. singleUppercaseAtEnd() – asserts that inputs “strinG” and “this is a strinG” correctly
     * return the original strings.
     */
    @Test
    public void singleUppercaseAtEnd() {
        assertEquals("strinG", ExtractorHelper.capitalizeIfAllUppercase("strinG"));
        assertEquals("this is a strinG",
                ExtractorHelper.capitalizeIfAllUppercase("this is a strinG"));
    }

    /**
     * 7. singleLowercaseAtBeginning() – asserts that inputs “sTRING” and “tHIS IS A STRING”
     * correctly return the original strings.
     */
    @Test
    public void singleLowercaseAtBeginning() {
        assertEquals("sTRING", ExtractorHelper.capitalizeIfAllUppercase("sTRING"));
        assertEquals("tHIS IS A STRING",
                ExtractorHelper.capitalizeIfAllUppercase("tHIS IS A STRING"));
    }

    /**
     * 8. singleLowercaseAtEnd() – asserts that inputs “STRINg” and “THIS IS A STRINg” correctly
     * return the original strings.
     */
    @Test
    public void singleLowercaseAtEnd() {
        assertEquals("STRINg", ExtractorHelper.capitalizeIfAllUppercase("STRINg"));
        assertEquals("THIS IS A STRINg",
                ExtractorHelper.capitalizeIfAllUppercase("THIS IS A STRINg"));
    }

    /**
     * 9. mixedStrings() – asserts that inputs “sTrInG!” and “ThIS? iS? A? StrINg?” correctly
     * return the original strings.
     */
    @Test
    public void mixedStrings() {
        assertEquals("sTrInG!", ExtractorHelper.capitalizeIfAllUppercase("sTrInG!"));
        assertEquals("ThIS? iS? A? StrINg?",
                ExtractorHelper.capitalizeIfAllUppercase("ThIS? iS? A? StrINg?"));
    }
}
