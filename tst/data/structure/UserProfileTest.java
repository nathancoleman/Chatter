package data.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests the functionality of the UserProfile class.
 */
public class UserProfileTest {
    
    /**
     * Tests the argument requirements for the UserProfile constructors.
     */
    @Test
    public void testConstructorRequirements() {
        boolean thrown = false;
        
        try {
            new UserProfile(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        
        assertTrue("A null id was passed in, but no IllegalArgumentException was thrown!", thrown);
        
        thrown = false;
        
        try {
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("attribute1", null);
            new UserProfile("123abc", attributes);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        
        assertTrue(
                "A null attribute value was passed in, but no IllegalArgumentException was thrown!",
                thrown);
        
        thrown = false;
        
        try {
            new UserProfile("123abc", null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        
        assertFalse("A valid id was passed in, but an IllegalArgumentException was thrown!", thrown);
        
        thrown = false;
        
        try {
            new UserProfile("123abc", new HashMap<String, String>());
        } catch (Exception e) {
            thrown = true;
        }
        
        assertFalse("Valid arguments were passed in, but an Exception was thrown!", thrown);
    }
    
    /**
     * Tests that the getAttribute() method returns the correct attribute when found.
     */
    @Test
    public void testGetAttributeFound() {
        String attributeId = "location";
        String attributeValue = "New York, NY";
        String id = "123";
        
        UserProfile profile = new UserProfile(id);
        
        profile.setAttribute(attributeId, attributeValue);
        
        assertEquals("The returned attribute value was incorrect!",
                UserProfile.getNormalizedAttributeString(attributeValue),
                profile.getAttribute(attributeId));
    }
    
    /**
     * Tests that the getAttribute() method returns null when not found.
     */
    @Test
    public void testGetAttributeNotFound() {
        String attributeId = "location";
        String attributeValue = "New York, NY";
        String id = "123";
        
        UserProfile profile = new UserProfile(id);
        
        profile.setAttribute(attributeId, attributeValue);
        
        String missingAttribute = "missingAttribute";
        
        assertNull("The profile returned a non-null value for a missing attribute!",
                profile.getAttribute(missingAttribute));
    }
    
    /**
     * Tests that a null attribute ID is not accepted.
     */
    @Test
    public void testSetAttributeNullId() {
        String attributeValue = "New York, NY";
        String id = "123";
        
        UserProfile profile = new UserProfile(id);
        
        boolean thrown = false;
        
        try {
            profile.setAttribute(null, attributeValue);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        
        assertTrue(
                "A null attribute ID was passed in, but no IllegalArgumentException was thrown!",
                thrown);
    }
    
    /**
     * Tests that a null attribute value is not accepted.
     */
    @Test
    public void testSetAttributeNullValue() {
        String attributeId = "location";
        String id = "123";
        
        UserProfile profile = new UserProfile(id);
        
        boolean thrown = false;
        
        try {
            profile.setAttribute(attributeId, null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        
        assertTrue(
                "A null attribute value was passed in, but no IllegalArgumentException was thrown!",
                thrown);
    }
    
    /**
     * Tests that the "attribute intersection" between two users is calculated correctly.
     */
    @Test
    public void testGetAttributeIntersection() {
        UserProfile user1 = new UserProfile("Seth");
        UserProfile user2 = new UserProfile("Charles");
        
        String[] commonAttrib1 = { UserProfile.getNormalizedAttributeString("profession"),
                UserProfile.getNormalizedAttributeString("Software Developer") };
        String[] commonAttrib2 = { UserProfile.getNormalizedAttributeString("hobby"),
                UserProfile.getNormalizedAttributeString("Reading") };
        String[] uniqueAttrib1 = { UserProfile.getNormalizedAttributeString("location"),
                UserProfile.getNormalizedAttributeString("New York, NY") };
        String[] uniqueAttrib2 = { UserProfile.getNormalizedAttributeString("location"),
                UserProfile.getNormalizedAttributeString("Tuscaloosa, AL") };
        
        // Set common attributes
        user1.setAttribute(commonAttrib1[0], commonAttrib1[1]);
        user2.setAttribute(commonAttrib1[0], commonAttrib1[1]);
        user1.setAttribute(commonAttrib2[0], commonAttrib2[1]);
        user2.setAttribute(commonAttrib2[0], commonAttrib2[1]);
        
        // Set unique attributes
        user1.setAttribute(uniqueAttrib1[0], uniqueAttrib1[1]);
        user2.setAttribute(uniqueAttrib2[0], uniqueAttrib2[1]);
        
        // Build expected intersection
        Map<String, String> intersection = new HashMap<String, String>();
        intersection.put(commonAttrib1[0], commonAttrib1[1]);
        intersection.put(commonAttrib2[0], commonAttrib2[1]);
        
        // Test symmetry of operation
        assertEquals("The intersection was not calculated correctly!", intersection,
                user1.getAttributeIntersection(user2));
        assertEquals("The intersection was not calculated correctly!", intersection,
                user2.getAttributeIntersection(user1));
    }
    
    /**
     * Tests the overridden equals() method for the UserProfile class in the successful case.
     */
    @Test
    public void testEquals() {
        UserProfile user1 = new UserProfile("Seth");
        UserProfile user2 = new UserProfile("Seth");
        
        // Test Symmetry of operation
        assertTrue("The two IDs were the same, but equals() returned false!", user1.equals(user2));
        assertTrue("The two IDs were the same, but equals() returned false!", user2.equals(user1));
    }
    
    /**
     * Tests that the overridden equals() method for the UserProfile class returns false when the
     * candidate is null.
     */
    @Test
    public void testNotEqualsNull() {
        UserProfile user1 = new UserProfile("Seth");
        
        assertFalse("The candidate user was null, but equals() returned true!", user1.equals(null));
    }
    
    /**
     * Tests that the overridden equals() method for the UserProfile class returns false when the
     * user IDs differ.
     */
    @Test
    public void testNotEquals() {
        UserProfile user1 = new UserProfile("Seth");
        UserProfile user2 = new UserProfile("Charles");
        
        assertFalse("The candidate user was had a different ID, but equals() returned true!",
                user1.equals(user2));
    }
    
    /**
     * Tests that the hashCode() method returns the same integer for equivalent objects.
     */
    @Test
    public void testHashCodeForEquivalents() {
        UserProfile user1 = new UserProfile("Seth");
        UserProfile user2 = new UserProfile("Seth");
        
        // First, validate assumption that these users are actually equivalent.
        assertEquals("Two identical Posts are not considered equivalent!", user1, user2);
        
        assertEquals("The two objects are equivalent, but hashCode() returned different integers!",
                user1.hashCode(), user2.hashCode());
    }
    
    /**
     * Tests that the hashCode() method returns the same integer on successive calls.
     */
    @Test
    public void testHashCodeConsistency() {
        UserProfile user1 = new UserProfile("Seth");
        
        int firstCode = user1.hashCode();
        
        for (int i = 0; i < 100; i++) {
            if (firstCode != user1.hashCode()) {
                fail("The hashCode() method does not always return the same integer on successive calls!");
                return;
            }
        }
    }
}
