package data.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests the functionality of the Post class.
 */
public class PostTest {
    
    private Post post;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() {
        post = new Post("Seth", "Hello, world!");
    }

    @After
    public void after() {
        post = null;
    }

    @Test
    public void testConstructorWithNullUserId() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Neither user_id nor content may be null.");
        new Post(null, "");
    }

    @Test
    public void testConstructorWithNullContent() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Neither user_id nor content may be null.");
        new Post("", null);
    }

    /**
     * Tests IllegalArgumentException thrown w/ invalid id
     */
    @Test
    public void testSetIdIllegalArg() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Id must be >= 0.");
        post.setId(-1);
    }

    @Test
    public void  testSetId() {
        boolean thrown = false;
        try {
            post.setId(0);
            post.setId(Integer.MAX_VALUE);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        assertFalse("Valid id was passed in, but IllegalArgumentException was thrown!", thrown);
    }
    
    /**
     * Tests the overridden equals() method for the Post class in the successful case.
     */
    @Test
    public void testEquals() {
        Post post2 = new Post("Seth", "Hello, world!");
        
        // Test Symmetry of operation
        assertTrue("All attributes were the same, but equals() returned false!",
                post.equals(post2));
        assertTrue("All attributes were the same, but equals() returned false!",
                post2.equals(post));
    }
    
    /**
     * Tests that the overridden equals() method for the Post class returns false when the candidate
     * is null.
     */
    @Test
    public void testNotEqualsNull() {
        assertFalse("The candidate post was null, but equals() returned true!", post.equals(null));
    }
    
    /**
     * Tests that the overridden equals() method for the Post class returns false when the posts
     * differ.
     */
    @Test
    public void testNotEquals() {
        // First, change only the name
        Post post2 = new Post("Charles", "Hello, world!");
        assertFalse("The candidate post was had a different name, but equals() returned true!",
                post.equals(post2));
        
        // Second change only the content
        post2 = new Post("Seth", "Goodbye, world!");
        assertFalse("The candidate post was had a different name, but equals() returned true!",
                post.equals(post2));
    }
    
    /**
     * Tests that the hashCode() method returns the same integer for equivalent objects.
     */
    @Test
    public void testHashCodeForEquivalents() {
        Post post2 = new Post("Seth", "Hello, world!");
        
        // First, validate assumption that these posts are actually equivalent.
        assertEquals("Two identical Posts are not considered equivalent!", post, post2);
        
        assertEquals("The two objects are equivalent, but hashCode() returned different integers!",
                post.hashCode(), post2.hashCode());
    }
    
    /**
     * Tests that the hashCode() method returns the same integer on successive calls.
     */
    @Test
    public void testHashCodeConsistency() {
        int firstCode = post.hashCode();
        
        for (int i = 0; i < 100; i++) {
            if (firstCode != post.hashCode()) {
                fail("The hashCode() method does not always return the same integer on successive calls!");
                return;
            }
        }
    }
}
