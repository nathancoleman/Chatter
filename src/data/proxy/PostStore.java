package data.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.structure.Post;

/**
 * PostStore controls access to the posts that have been collected from users.
 */
public class PostStore {
    private Map<String, List<Post>> postsByUser;
    
    /**
     * Basic default constructor for PostStore.
     */
    public PostStore() {
        postsByUser = new HashMap<String, List<Post>>();
    }
    
    /**
     * Writes a post to storage.
     * 
     * @param post The Post object to store
     * @throws IllegalArgumentException
     *             if post is null
     */
    public void write(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null.");
        }
        String user = post.getUser();
        if (!postsByUser.containsKey(user)) {
            postsByUser.put(user, new ArrayList<Post>());
        }
        postsByUser.get(post.getUser()).add(post);
    }
    
    /**
     * Gets the posts for the specified user from storage.
     * 
     * @param id The String id for the UserProfile to retrieve
     * @return the user's posts
     * @throws IllegalArgumentException
     *             if user is null
     */
    public List<Post> getPostsByUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (postsByUser.containsKey(id)) {
            return postsByUser.get(id);
        } else {
            return new ArrayList<Post>();
        }
    }
}
