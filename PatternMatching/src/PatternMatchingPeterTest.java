import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This is a not-so-basic set of unit tests for ParternMatching.
 * <p>
 * It currently checks that your pattern matching algos correctly find the
 * patterns and throw their excpetions. It does a few tests for efficiency
 * (the examples from Saikrishna slides).
 * <p>
 * Using the IntelliJ debugger with random tests:
 * The Random object rand used to create the arrays for each test iteration is
 * reseeded each iteration by a random long derived from the test number.
 * The test number gets printed whenever a test fails. So to repeat a
 * specific test, just set testNumber to be the seed of the test you want to
 * run.
 * @author CS 1332 TAs + Peter Wilson
 * @version 2.2
 */
public class PatternMatchingPeterTest {

    private static final int TEST_LOOPS = 20000;  //Number of tests to run
    private static final int MAX_TEXT_SIZE = 30;   //Biggest string to test on
    private static final int MIN_TEXT_SIZE = 1;   //Smallest string to test on
    private static final int MAX_PATTERN_SIZE = 9;   //Biggest pattern
    //Number of characters in alphabet
    private static final int BIG_ALPHABET_SIZE = 15;
    //Number of characters in alphabet
    private static final int SMALL_ALPHABET_SIZE = 7;
    private static final int SHORT_TIMEOUT = 200;
    private static final int LONG_TIMEOUT = 2000;  //For the long tests
    private static Random rand;   //Generates random numbers for each tests
    private int testNumber; //Which number test we are on. For repeating tests
    private String pattern;
    private String text;
    private int[] failTable;
    private Map<Character, Integer> lastOccurrence;
    private List<Integer> answer;
    private List<Integer> foundMatches;
    /**
     * This method runs whenever your test fails. It prints useful debug data
     */
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            if (testNumber != -1) {
                System.out.println("Random seed when test below failed: " + testNumber + " Use to repeat a specific test iteration in the debugger.");
            }
            if (text != null) {
                System.out.println("Text when test failed:\n" + text);
            }
            if (pattern != null) {
                System.out.println("Pattern when test failed:\n" + pattern);
            }
            if (failTable != null) {
                System.out.println("Failure table when test failed:\n" + Arrays.toString(failTable));
            }
            if (lastOccurrence != null) {
                System.out.println("Last Occurrence Table when test "
                        + "failed:\n" + lastOccurrence);
            }
            if (answer != null) {
                String answerString;
                System.out.println("Expected matches when test failed:\n" + answer);
            }
            if (foundMatches != null) {
                System.out.println("Your matches when test failed:\n" + foundMatches);
            }
        }
    };
    private CharacterComparator comp;

    @Before
    public void setUp() {
        /*
        We need a way to repeat a SPECIFIC test when a random test fails.
        Therefore, EVERY time we run a random test, we seed the test's random
        number generator with a function of which test number we are on
         */
        testNumber = -1;

        comp = new CharacterComparator();
    }

    //KMP Tests

    //Exceptions
    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullPatternKMP() {
        PatternMatching.kmp(null, "a", comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullTextKMP() {
        PatternMatching.kmp("a", null, comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testLen0PatternKMP() {
        PatternMatching.kmp("", "a", comp);
    }

    @Test(timeout = SHORT_TIMEOUT)  //Shouldn't throw an excpetion
    public void testLen0TextKMP() {
        PatternMatching.kmp("bed", "", comp);
        assertEquals("Pattern longer than text.", 0, comp.getComparisonCount());
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCompKMP() {
        PatternMatching.kmp("bed", "a", null);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullPatternBuildFailTable() {
        PatternMatching.buildFailureTable(null, comp);
    }

    @Test(timeout = SHORT_TIMEOUT)  //DOESN'T fail at length-0 per javadocs
    public void testLen0PatternBuildFailTable() {
        PatternMatching.buildFailureTable("", comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCompBuildFailTable() {
        PatternMatching.buildFailureTable("bed", null);
    }

    @Test(timeout = SHORT_TIMEOUT)
    public void testBuildSaiSlideFailureTable() {
        //From Saikrishna slides
        pattern = "amanama";
        int[] expectedFailTable = {0, 0, 1, 0, 1, 2, 3};
        failTable = PatternMatching.buildFailureTable(pattern, comp);
        assertArrayEquals(expectedFailTable, failTable);
        assertEquals(7, comp.getComparisonCount());

        comp = new CharacterComparator();
        pattern = "andanandandan";
        int[] expectedFailTable2 = {0, 0, 0, 1, 2, 1, 2, 3, 4, 5, 3, 4, 5};
        failTable = PatternMatching.buildFailureTable(pattern, comp);
        assertArrayEquals(expectedFailTable2, failTable);
        assertEquals(14, comp.getComparisonCount());
    }

    @Test(timeout = SHORT_TIMEOUT)
    public void testSaiSlideKMP() {
        //From Saikrishna slides
        pattern = "easee";
        text = "ealeaseealle";
        answer = Arrays.asList(3);
        foundMatches = PatternMatching.kmp(pattern, text, comp);
        assertListsEquals(answer, foundMatches);
        assertEquals(5 + 11, comp.getComparisonCount());

        comp = new CharacterComparator();
        pattern = "ababccabab";
        text = "ababababccababccabab";
        answer = Arrays.asList(4, 10);
        foundMatches = PatternMatching.kmp(pattern, text, comp);
        assertListsEquals(answer, foundMatches);
        assertEquals(10 + 22, comp.getComparisonCount());
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testBuildFailureTable() {
        for (testNumber = 0; testNumber < TEST_LOOPS; testNumber++) {
            /*
            Change testNumber to repeat a specific test! For example, if the
            test you fail is on test 137, do:
             */
            //testNumber = 137;

            rand = new Random(randomSeedStretcher(testNumber));

            comp = new CharacterComparator();

            /*
            To get better tests, we will use the machinery dedicated to
            setting up tests with repeated patterns in them to generate
            interesting PATTERNS with repeated elements for the failure
            tables in them
             */
            //Start with small arrays and scale larger
            int textLen =
                    testNumber * (MAX_TEXT_SIZE - MIN_TEXT_SIZE) / TEST_LOOPS + MIN_TEXT_SIZE;
            int patternLen =
                    rand.nextInt(Math.min(5, textLen)) + 1;

            int match2 = rand.nextInt(textLen - patternLen + 2);
            setTest(0, match2, textLen, patternLen, rand);

            pattern = text;
            text = null; //We aren't using the text this time

            failTable = PatternMatching.buildFailureTable(pattern, comp);

            assertEquals("First entry must be 0", 0, failTable[0]);

            for (int i = 1; i < failTable.length; i++) {
                assertTrue("Index " + i + " of failure table was not a strict"
                                + " match between prefix and suffix.",
                        prefixSuffixMatch(pattern.substring(0, i + 1),
                                failTable[i]));

                //Now check there were no longer possible matches
                for (int j = failTable[i] + 1; j < i; j++) {
                    assertFalse("At " + i + " Found a longer match: " + j,
                            prefixSuffixMatch(pattern.substring(0, i + 1), j));
                }

            }


            //System.out.println("Pattern: " + pattern + " Fail table: " +
            // Arrays.toString(failTable) + " Number: " + testNumber);

        }
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testKMPSearch() {
        for (testNumber = 0; testNumber < TEST_LOOPS; testNumber++) {
            /*
            Change testNumber to repeat a specific test! For example, if the
            test you fail is on test 137, do:
             */
            //testNumber = 137;

            rand = new Random(randomSeedStretcher(testNumber));

            comp = new CharacterComparator();

            //Start with small arrays and scale larger
            int textLen =
                    testNumber * (MAX_TEXT_SIZE - MIN_TEXT_SIZE) / TEST_LOOPS + MIN_TEXT_SIZE;
            int patternLen =
                    rand.nextInt(Math.min(MAX_PATTERN_SIZE, textLen)) + 1;

            //The +2 let it run off the end of the array. If it does, that
            // index will be ignored
            int match1 = rand.nextInt(textLen - patternLen + 2);
            int match2 = rand.nextInt(textLen - patternLen + 2);

            setTest(match1, match2, textLen, patternLen, rand);

            /*
            System.out.println("Text: " + text + ". Pattern: " + pattern
                    + " Matches: " + answer + " Number: " + testNumber);
            */
            foundMatches = PatternMatching.kmp(pattern, text, comp);
            assertListsEquals(answer, foundMatches);
        }
    }

    //Boyer-Moore

    //Exceptions
    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullPatternBM() {
        PatternMatching.boyerMoore(null, "a", comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullTextBM() {
        PatternMatching.boyerMoore("a", null, comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testLen0PatternBM() {
        PatternMatching.boyerMoore("", "a", comp);
    }

    @Test(timeout = SHORT_TIMEOUT)  //Shouldn't throw an excpetion
    public void testLen0TextBM() {
        PatternMatching.boyerMoore("bed", "", comp);
        assertEquals("Pattern longer than text.", 0, comp.getComparisonCount());
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCompBM() {
        PatternMatching.boyerMoore("bed", "a", null);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullPatternBuildLastTable() {
        PatternMatching.buildFailureTable(null, comp);
    }

    @Test(timeout = SHORT_TIMEOUT)  //DOESN'T fail at length-0 per javadocs
    public void testLen0PatternBuildLastTable() {
        PatternMatching.buildFailureTable("", comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullCompBuildLastTable() {
        PatternMatching.buildFailureTable("bed", null);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testBuildLastTable() {
        for (testNumber = 0; testNumber < TEST_LOOPS; testNumber++) {
            /*
            Change testNumber to repeat a specific test! For example, if the
            test you fail is on test 137, do:
             */
            //testNumber = 137;

            rand = new Random(randomSeedStretcher(testNumber));

            //Start with small arrays and scale larger
            int patternLen =
                    testNumber * (MAX_TEXT_SIZE - MIN_TEXT_SIZE) / TEST_LOOPS + MIN_TEXT_SIZE;

            pattern = genRandStr(SMALL_ALPHABET_SIZE, patternLen, rand);

            lastOccurrence = PatternMatching.buildLastTable(pattern);

            for (int i = 0; i < pattern.length(); i++) {
                assertNotNull("Index " + i + " not in table.",
                        lastOccurrence.get(pattern.charAt(i)));
            }

            for (Map.Entry<Character, Integer> entry
                    : lastOccurrence.entrySet()) {
                assertEquals("Character not at indicated position.",
                        pattern.charAt(entry.getValue()), (int) entry.getKey());
                assertFalse("Later occurrence found.",
                        pattern.substring(entry.getValue() + 1).contains(entry.getValue().toString()));
            }
            /*
            System.out.println("Pattern: " + pattern + " Last table: " +
                    lastOccurrence + " Number: " + testNumber);
             */
        }
    }

    @Test(timeout = SHORT_TIMEOUT)
    public void testSaiSlideBM() {
        //From Saikrishna slides
        pattern = "ckcm";
        text = "abdacccmkckcm";
        answer = Arrays.asList(9);
        foundMatches = PatternMatching.boyerMoore(pattern, text, comp);
        assertListsEquals(answer, foundMatches);
        assertEquals(10, comp.getComparisonCount());
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testBMSearch() {
        for (testNumber = 0; testNumber < TEST_LOOPS; testNumber++) {
            /*
            Change testNumber to repeat a specific test! For example, if the
            test you fail is on test 137, do:
             */
            //testNumber = 137;

            rand = new Random(randomSeedStretcher(testNumber));

            comp = new CharacterComparator();

            //Start with small arrays and scale larger
            int textLen =
                    testNumber * (MAX_TEXT_SIZE - MIN_TEXT_SIZE) / TEST_LOOPS + MIN_TEXT_SIZE;
            int patternLen =
                    rand.nextInt(Math.min(MAX_PATTERN_SIZE, textLen)) + 1;

            //The +2 let it run off the end of the array. If it does, that
            // index will be ignored
            int match1 = rand.nextInt(textLen - patternLen + 2);
            int match2 = rand.nextInt(textLen - patternLen + 2);

            setTest(match1, match2, textLen, patternLen, rand);

            /*
            System.out.println("Text: " + text + ". Pattern: " + pattern
                    + " Matches: " + answer + " Number: " + testNumber);
            */
            foundMatches = PatternMatching.boyerMoore(pattern, text, comp);
            assertListsEquals(answer, foundMatches);
        }
    }

    //Rabin-Karp tests

    //Exceptions
    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullPatternRK() {
        PatternMatching.rabinKarp(null, "a", comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testNullTextRK() {
        PatternMatching.rabinKarp("a", null, comp);
    }

    @Test(timeout = SHORT_TIMEOUT, expected = IllegalArgumentException.class)
    public void testLen0PatternRK() {
        PatternMatching.rabinKarp("", "a", comp);
    }

    @Test(timeout = SHORT_TIMEOUT)  //Shouldn't throw an excpetion
    public void testLen0TextRK() {
        PatternMatching.rabinKarp("bed", "", comp);
        assertEquals("Pattern longer than text.", 0, comp.getComparisonCount());
    }

    @Test(timeout = LONG_TIMEOUT)
    public void testRabinKarpSearch() {
        for (testNumber = 0; testNumber < TEST_LOOPS; testNumber++) {
            /*
            Change testNumber to repeat a specific test! For example, if the
            test you fail is on test 137, do:
             */
            //testNumber = 137;

            rand = new Random(randomSeedStretcher(testNumber));

            comp = new CharacterComparator();

            //Start with small arrays and scale larger
            int textLen =
                    testNumber * (MAX_TEXT_SIZE - MIN_TEXT_SIZE) / TEST_LOOPS + MIN_TEXT_SIZE;
            int patternLen =
                    rand.nextInt(Math.min(MAX_PATTERN_SIZE, textLen)) + 1;

            //The +2 let it run off the end of the array. If it does, that
            // index will be ignored
            int match1 = rand.nextInt(textLen - patternLen + 2);
            int match2 = rand.nextInt(textLen - patternLen + 2);

            setTest(match1, match2, textLen, patternLen, rand);

            /*
            System.out.println("Text: " + text + ". Pattern: " + pattern
                    + " Matches: " + answer + " Number: " + testNumber);
            */
            foundMatches = PatternMatching.rabinKarp(pattern, text, comp);
            assertListsEquals(answer, foundMatches);
        }
    }

    //Stuff for generating arrays

    /**
     * Attempt to generate a random pattern and text that match at AT
     * the two given indicies. However, may only match 1 or none.
     * <p>
     * Writes the pattern, text, and answer to the class fields.
     * <p>
     * @param match1        one index to match
     * @param match2        other index to match
     * @param textLength    length of the text to generate
     * @param patternLength length of the pattern to generate
     * @param theRand       random to use for repeatability
     */
    private void setTest(int match1, int match2,
                         int textLength, int patternLength,
                         Random theRand) {
        int minMatch = Math.min(match1, match2);
        int maxMatch = Math.max(match1, match2);

        if (patternLength == 0 || textLength == 0) {
            throw new IllegalArgumentException("To generate pattern and text,"
                    + " their lengths must be >0");
        }
        if (patternLength > textLength) {
            setTestNoMatch(textLength, patternLength, theRand);

        } else if (maxMatch + patternLength > textLength) {
            if (minMatch + patternLength > textLength) {
                setTestNoMatch(textLength, patternLength, theRand);

            } else {
                setTest1Match(minMatch, textLength, patternLength, theRand);

            }
        } else if (match1 == match2) {
            setTest1Match(match1, textLength, patternLength, theRand);

        } else {
            setTest2Match(match1, match2, textLength, patternLength, theRand);

        }
    }

    /**
     * Generates a random pattern and text that match at AT LEAST the two given
     * indicies and writes the pattern, text, and answer to the class fields.
     * <p>
     * It does this by generating the pattern character by character and
     * seeing if the pattern is running onto itself. If it is, then we know
     * the rest of the pattern.
     * <p>
     * Ex: length-10 array, length-3 pattern, with matches at 0, 2:
     * Loop 1: [z, _, z, _, _, _, _, _, _, _]
     * Loop 2: [z, g, z, g, _, _, _, _, _, _]
     * Loop 3: [z, g, z, g, z, _, _, _, _, _]
     * Then randomly fill the rest of the array.
     * @param minMatch      one index to match
     * @param maxMatch      other index to match
     * @param textLength    length of the text to generate
     * @param patternLength length of the pattern to generate
     * @param theRand       random to use for repeatability
     */
    private void setTest2Match(int minMatch, int maxMatch, int textLength,
                               int patternLength, Random theRand) {


        char[] textArr = new char[textLength];
        char[] patternArr = new char[patternLength];

        //Pattern start (Loop 1 in Javadoc)
        textArr[minMatch] = textArr[maxMatch] = patternArr[0] =
                randChar(BIG_ALPHABET_SIZE, theRand);

        int i = 1;
        while (i < patternLength && textArr[minMatch + i] == 0) {
            //Haven't run back onto pattern yet. Loop 2 in Javadoc.
            textArr[minMatch + i] = textArr[maxMatch + i] = patternArr[i] =
                    randChar(BIG_ALPHABET_SIZE, theRand);
            i++;
        }
        //Ran back onto pattern (Loop 3 in Javadoc), so just continue
        // extending the pattern).
        for (; i < patternLength; i++) {
            patternArr[i] = textArr[maxMatch + i] = textArr[minMatch + i];
        }
        //Finish filling array
        for (int j = 0; j < textLength; j++) {
            if (textArr[j] == 0) {
                textArr[j] = randChar(BIG_ALPHABET_SIZE, theRand);
            }
        }

        text = new String(textArr);
        pattern = new String(patternArr);
        answer = findAllMatches(pattern, text);
    }

    /**
     * Generate random pattern and text that match at AT LEAST the given
     * index and writes the pattern, text, and answer to the class fields.
     * @param match         Index to match
     * @param textLength    length of the text to generate
     * @param patternLength length of the pattern to generate
     * @param theRand       random to use for repeatability
     */
    private void setTest1Match(int match, int textLength, int patternLength,
                               Random theRand) {
        if (patternLength == 0 || textLength == 0) {
            throw new IllegalArgumentException("To generate pattern and text,"
                    + " their lengths must be >0");
        } else if (patternLength > textLength) {
            throw new IllegalArgumentException("No match possible: pattern "
                    + "longer than text.");
        }
        if (match + patternLength > textLength) {
            throw new IllegalArgumentException("Pattern too long to put there");
        }

        char[] textArr = new char[textLength];
        char[] patternArr = new char[patternLength];


        for (int i = 0; i < patternLength; i++) {
            textArr[i + match] = patternArr[i] = randChar(BIG_ALPHABET_SIZE,
                    theRand);
        }
        for (int i = 0; i < match; i++) {
            textArr[i] = randChar(BIG_ALPHABET_SIZE, theRand);
        }
        for (int i = match + patternLength; i < textLength; i++) {
            textArr[i] = randChar(BIG_ALPHABET_SIZE, theRand);
        }

        text = new String(textArr);
        pattern = new String(patternArr);
        answer = findAllMatches(pattern, text);
    }

    /**
     * Generates a text and pattern with (probably) no matches.
     * @param textLength    length of the text to generate
     * @param patternLength length of the pattern to generate
     * @param theRand       random to use for repeatability
     */
    private void setTestNoMatch(int textLength, int patternLength,
                                Random theRand) {
        text = genRandStr(BIG_ALPHABET_SIZE, textLength, theRand);
        pattern = genRandStr(BIG_ALPHABET_SIZE, patternLength, theRand);
        answer = findAllMatches(pattern, text);
    }

    /**
     * Generates a random string of specified length
     * @param alphabetSize size of alphabet
     * @param strLength    length of string
     * @param theRand      random to use for repeatability
     * @return random string
     */
    private String genRandStr(int alphabetSize, int strLength, Random theRand) {
        char[] strArr = new char[strLength];

        for (int i = 0; i < strLength; i++) {
            strArr[i] = randChar(BIG_ALPHABET_SIZE, theRand);
        }
        return new String(strArr);
    }

    /**
     * Generates a random character from a to alphabetSize. Ex. if alphabet
     * size is 3, then will return a, b, or c.
     * @param alphabetSize size of alphabet
     * @param theRand      random to use for test repeatability
     * @return random character
     */
    private char randChar(int alphabetSize, Random theRand) {
        return (char) (theRand.nextInt(alphabetSize) + 'a');
    }

    /**
     * Finds all matches of pattern in text using Java built-ins
     * @param pattern pattern
     * @param text    text
     * @return Sorted list of matches
     */
    private List<Integer> findAllMatches(final String pattern,
                                         final String text) {
        LinkedList<Integer> matches = new LinkedList<>();
        int matchInd = -1;
        do {
            matchInd = text.indexOf(pattern, matchInd + 1);
            if (matchInd != -1) {
                matches.addLast(matchInd);
            }
        }
        while (matchInd != -1);
        return matches;
    }

    /**
     * AssertEquals for lists. Not very efficient.
     * @param expected Expected list
     * @param actual   Actual list
     */
    private void assertListsEquals(List expected, List actual) {
        Object[] expectedArr = expected.toArray();
        Object[] actualArr = actual.toArray();
        assertArrayEquals("Expected: " + Arrays.deepToString(expectedArr)
                        + " Was: " + Arrays.deepToString(actualArr),
                expectedArr, actualArr);
    }

    /**
     * Returns if we found a valid prefix and suffix pair of given length
     * @param pattern   text to check
     * @param prefixLen length of prefix and suffix to check
     * @return true if prefix == suffix
     */
    private boolean prefixSuffixMatch(String pattern, int prefixLen) {
        if (pattern.length() <= prefixLen) {
            //If too long, fail immediately
            return false;
        } else if (!pattern.substring(0, prefixLen).equals(
                pattern.substring(pattern.length() - prefixLen))) {
            //Check prefix and suffix equal
            return false;
        }
        return true;

    }

    /**
     * It turned out that just seeding the random with a continually
     * increasing sequence caused it to not be random. This uses the MD5 hash
     * to generate pseudorandom seeds so random is a good pseudorandom number
     * generator.
     * @param loopNum int to stretch
     * @return a random seed
     */
    private long randomSeedStretcher(int loopNum) {
        try {
            byte[] loopNumBytes = new byte[5];
            loopNumBytes[0] = (byte) (loopNum >> 24);
            loopNumBytes[1] = (byte) (loopNum >> 16);
            loopNumBytes[2] = (byte) (loopNum >> 8);
            loopNumBytes[3] = (byte) loopNum;
            loopNumBytes[4] = (byte) 42; //Salting it

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(loopNumBytes);

            long result = 0;
            for (int i = 0; i < 8; i++) {
                result += digest[i] << (8 * i);
            }
            return result;

        } catch (NoSuchAlgorithmException e) {
        }

        throw new IllegalStateException("This really shouldn't have happened "
                + "- I hard-coded that algorithm.");
    }

}