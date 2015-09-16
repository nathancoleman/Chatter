package server.matching;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import data.structure.UserProfile;

/**
 * Tests the functionality of the PercentMatchUserMatcher class.
 */
public class PercentMatchUserMatcherTest {
    
    /**
     * Tests that this matcher will return true when the percent match is equal to the threshold.
     */
    @Test
    public void testSuccessfulMatchAtThreshold() {
        UserProfile user1 = new UserProfile("Seth");
        UserProfile user2 = new UserProfile("Charles");
        
        String[] commonAttrib1 = { "profession", "Software Developer" };
        String[] commonAttrib2 = { "hobby", "Reading" };
        String[] uniqueAttrib1 = { "location", "New York, NY" };
        String[] uniqueAttrib2 = { "location", "Tuscaloosa, AL" };
        String[] uniqueAttrib3 = { "favoriteFood", "Chicken Pot Pie" };
        
        // Set common attributes (2 common attributes)
        user1.setAttribute(commonAttrib1[0], commonAttrib1[1]);
        user2.setAttribute(commonAttrib1[0], commonAttrib1[1]);
        user1.setAttribute(commonAttrib2[0], commonAttrib2[1]);
        user2.setAttribute(commonAttrib2[0], commonAttrib2[1]);
        
        // Set unique attributes (2 unique atributes for user1, and one unique attribute for user2)
        user1.setAttribute(uniqueAttrib1[0], uniqueAttrib1[1]);
        user2.setAttribute(uniqueAttrib2[0], uniqueAttrib2[1]);
        user1.setAttribute(uniqueAttrib3[0], uniqueAttrib3[1]);
        
        // Exactly the percentage of user1's attributes that match user2's attributes
        final double THRESHOLD = 0.5;
        UserMatcher matcher = new PercentMatchUserMatcher(THRESHOLD);
        
        assertTrue(
                "Exactly the required percentage of attributes were a match, but the matcher returned false!",
                matcher.matches(user1, user2));
    }
    
    /**
     * Tests that this matcher will return true when the percent match is above the threshold.
     */
    @Test
    public void testSuccessfulMatchAboveThreshold() {
        UserProfile user1 = new UserProfile("Seth");
        UserProfile user2 = new UserProfile("Charles");
        
        String[] commonAttrib1 = { "profession", "Software Developer" };
        String[] commonAttrib2 = { "hobby", "Reading" };
        String[] uniqueAttrib1 = { "location", "New York, NY" };
        String[] uniqueAttrib2 = { "location", "Tuscaloosa, AL" };
        
        // Set common attributes (2 common attributes)
        user1.setAttribute(commonAttrib1[0], commonAttrib1[1]);
        user2.setAttribute(commonAttrib1[0], commonAttrib1[1]);
        user1.setAttribute(commonAttrib2[0], commonAttrib2[1]);
        user2.setAttribute(commonAttrib2[0], commonAttrib2[1]);
        
        // Set unique attributes (1 unique atribute for user1, and one unique attribute for user2)
        user1.setAttribute(uniqueAttrib1[0], uniqueAttrib1[1]);
        user2.setAttribute(uniqueAttrib2[0], uniqueAttrib2[1]);
        
        // Exactly the percentage of user1's attributes that match user2's attributes
        final double THRESHOLD = 0.5;
        UserMatcher matcher = new PercentMatchUserMatcher(THRESHOLD);
        
        assertTrue(
                "More than the required percentage of attributes were a match, but the matcher returned false!",
                matcher.matches(user1, user2));
    }
    
    /**
     * Tests that this matcher will return false when the percent match is below the threshold.
     */
    @Test
    public void testUnsuccessfulMatchBelowThreshold() {
        UserProfile user1 = new UserProfile("Seth");
        UserProfile user2 = new UserProfile("Charles");
        
        String[] commonAttrib1 = { "profession", "Software Developer" };
        String[] uniqueAttrib1 = { "hobby", "Reading" };
        String[] uniqueAttrib2 = { "location", "New York, NY" };
        
        // Set common attributes (1 common attribute)
        user1.setAttribute(commonAttrib1[0], commonAttrib1[1]);
        user2.setAttribute(commonAttrib1[0], commonAttrib1[1]);
        
        // Set unique attributes (2 unique atributes for user1)
        user1.setAttribute(uniqueAttrib1[0], uniqueAttrib1[1]);
        user1.setAttribute(uniqueAttrib2[0], uniqueAttrib2[1]);
        
        // Exactly the percentage of user1's attributes that match user2's attributes
        final double THRESHOLD = 0.5;
        UserMatcher matcher = new PercentMatchUserMatcher(THRESHOLD);
        
        assertFalse(
                "Fewer than the required percentage of attributes were a match, but the matcher returned true!",
                matcher.matches(user1, user2));
    }
}
