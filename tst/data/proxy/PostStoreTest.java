package data.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import data.structure.Post;

public class PostStoreTest {
	
	/**
	 * Tests the argument requirements of the write() method.
	 */
	@Test
	public void testWriteIllegalArgument() {
		PostStore ps = new PostStore();
		
		boolean thrown = false;
		
		try {
			ps.write(null);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		
		assertTrue("A null post was passed in, but no IllegalArgumentException was thrown.", thrown);
	}
	
	/**
	 * Tests the argument requirements of the getPostsByUser() method.
	 */
	@Test
	public void testGetPostsByUserIllegalArgument() {
		PostStore ps = new PostStore();
		
		boolean thrown = false;
		
		try {
			ps.getPostsByUser(null);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		
		assertTrue("A null user was passed in, but no IllegalArgumentException was thrown.", thrown);
	}
	
	/**
	 * Tests the basic write/retrieve functionality of the PostStore.
	 */
	@Test
	public void testStandardPostCycle() {
		PostStore ps = new PostStore();
		
		//Create two users
		final String u1 = "Seth";
		final String u2 = "Charles";
		
		//Create posts for u1
		Post p1 = new Post(u1, "Hey!");
		Post p2 = new Post(u1, "Yo!");
		
		//Add u1 posts to list
		List<Post> u1Posts = new ArrayList<Post>();
		u1Posts.add(p1);
		u1Posts.add(p2);

		//Create posts for u2
		Post p3 = new Post(u2, "Sup?");
		Post p4 = new Post(u2, "Hola!");

		//Add u2 posts to list
		List<Post> u2Posts = new ArrayList<Post>();
		u2Posts.add(p3);
		u2Posts.add(p4);
		
		//Write posts to post store
		ps.write(p1);
		ps.write(p2);
		ps.write(p3);
		ps.write(p4);
		
		//Check returned posts for u1
		List<Post> posts = ps.getPostsByUser(u1);
		for (int i = 0; i < posts.size(); i++) {
			assertEquals("Posts do not match!", u1Posts.get(i), posts.get(i));
		}

		//Check returned posts for u2
		posts = ps.getPostsByUser(u2);
		for (int i = 0; i < posts.size(); i++) {
			assertEquals("Posts do not match!", u2Posts.get(i), posts.get(i));
		}
	}
}
