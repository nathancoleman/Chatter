package data.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PostTest {
	
	/**
	 * Tests the argument requirements for the Post constructor.
	 */
	@Test
	public void testConstructorRequirements() {
		boolean thrown = false;
		
		try {
			new Post(null, "");
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		
		assertTrue("A null user was passed in, but no IllegalArgumentException was thrown!", thrown);
		
		thrown = false;
		
		try {
			new Post("", null);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		
		assertTrue("Null content was passed in, but no IllegalArgumentException was thrown!", thrown);
	}
	
	/**
	 * Tests the overridden equals() method for the Post class in the successful case.
	 */
	@Test
	public void testEquals() {
		Post post1 = new Post("Seth", "Hello, world!");
		Post post2 = new Post("Seth", "Hello, world!");
		
		assertTrue("All attributes were the same, but equals() returned false!", post1.equals(post2));
	}
	
	/**
	 * Tests that the overridden equals() method for the Post class returns false when the candidate is null.
	 */
	@Test
	public void testNotEqualsNull() {
		Post post1 = new Post("Seth", "Hello, world!");
		Post post2 = null;
		
		assertFalse("The candidate post was null, but equals() returned true!", post1.equals(post2));
	}
	
	/**
	 * Tests that the overridden equals() method for the Post class returns false when the posts differ.
	 */
	@Test
	public void testNotEquals() {
		Post post1 = new Post("Seth", "Hello, world!");
		
		//First, change only the name
		Post post2 = new Post("Charles", "Hello, world!");
		assertFalse("The candidate post was had a different name, but equals() returned true!", post1.equals(post2));
		
		//Second change only the content
		post2 = new Post("Seth", "Goodbye, world!");
		assertFalse("The candidate post was had a different name, but equals() returned true!", post1.equals(post2));
	}
	
	/**
	 * Tests that the hashCode() method returns the same integer for equivalent objects.
	 */
	@Test
	public void testHashCodeForEquivalents() {
		Post post1 = new Post("Seth", "Hello, world!");
		Post post2 = new Post("Seth", "Hello, world!");
		
		assertEquals("The two objects are equivalent, but hashCode() returned different integers!", post1.hashCode(), post2.hashCode());
	}
	
	/**
	 * Tests that the hashCode() method returns the same integer on successive calls.
	 */
	@Test
	public void testHashCodeConsistency() {
		Post post1 = new Post("Seth", "Hello, world!");
		
		int firstCode = post1.hashCode();
		
		for (int i = 0; i < 100; i++) {
			if (firstCode != post1.hashCode()) {
				fail("The hashCode() method does not always return the same integer on successive calls!");
				return;
			}
		}
	}
}
