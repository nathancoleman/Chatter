package data.structure;

import static org.junit.Assert.assertTrue;

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
}
