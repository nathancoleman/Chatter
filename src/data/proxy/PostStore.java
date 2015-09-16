package data.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import data.proxy.utils.FunctionalUtils;
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
     * @throws IllegalArgumentException if post is null
     */
    public void write(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null.");
        }

        String user = post.getUser();
        if (!postsByUser.containsKey(user)) {
            postsByUser.put(user, new ArrayList<Post>());
        }
        post.setId(getNextPostIdForUser(user));
        postsByUser.get(post.getUser()).add(post);
    }

    /**
     * Remove a post from storage given the user and id for the post.
     *
     * @param user_id The String id for user
     * @param post_id The int id for the Post to remove from storage
     * @throws IllegalArgumentException
     *             if user_id is null
     */
    public void delete(String user_id, int post_id) {
        if (user_id == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }

        List<Post> posts = postsByUser.get(user_id);
        for (Post post : posts) {
            if (post.getId() == post_id)
            {
                posts.remove(post);
                break;
            }
        }
    }

    /**
     * Remove a post from storage given the Post object.
     *
     * @param post The Post object to remove from storage
     * @throws IllegalArgumentException if post is null
     */
    public void delete(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null.");
        }

        delete(post.getUser(), post.getId());
    }

    /**
     * Gets the post for the user specified by the id from storage.
     *
     * @param user_id The String id for the user
     * @param post_id The int id for the Post to retrieve
     * @return the request Post object if found, null otherwise
     * @throws IllegalArgumentException
     *             if user_id is null
     */
    public Post getPost(String user_id, int post_id) {
        if (user_id == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }

        List<Post> userPosts = getPostsByUser(user_id);
        for (Post post : userPosts) {
            if (post.getId() == post_id)
            {
                return post;
            }
        }

        return null;
    }

    /**
     * Gets the posts for the specified user from storage.
     * 
     * @param user_id The String id for the UserProfile to retrieve
     * @return the user's posts
     * @throws IllegalArgumentException if user is null
     */
    public List<Post> getPostsByUser(String user) {
        Predicate<Post> predicate = new Predicate<Post>() {
            @Override
            public boolean test(Post t) {
                return true;
            }
        };
        return getPostsByUser(user, predicate);
    }
    
    /**
     * Gets the posts for the specified user from storage.
     * 
     * @param user
     * @return the user's posts
     * @throws IllegalArgumentException if user is null
     */
    public List<Post> getPostsByUser(String user, Predicate<Post> predicate) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (predicate == null) {
            throw new IllegalArgumentException(
                    "Predicate cannot be null. Use getPostByUser(user) instead.");
        }
        if (postsByUser.containsKey(user)) {
            return (List<Post>) FunctionalUtils.filteredAddAll(postsByUser.get(user), predicate,
                    new ArrayList<Post>());
        } else {
            return new ArrayList<Post>();
        }
    }

    /**
     * Gets the next post ID available for a particular user
     *
     * @param user_id The String id for the user
     * @return the user's post count + 1
     * @throws IllegalArgumentException
     *             if user_id is null
     */
    private int getNextPostIdForUser(String user_id) {
        if (user_id == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }

        return getPostsByUser(user_id).size() + 1;
    }
}
