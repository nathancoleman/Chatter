package server.feed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import data.proxy.PostStore;
import data.proxy.UserProfileStore;
import data.structure.Post;
import data.structure.UserProfile;

/**
 * FeedBuilder is responsible for building the feed that is sent to each user.
 */
public class FeedBuilder {
    
    private final PostStore postStore;
    private final UserProfileStore userStore;
    private final Predicate<UserProfile> userPredicate;
    private final Predicate<Post> postPredicate;
    
    /**
     * Constructor requires a PostStore, UserProfileStore, and UserMatcher.
     * 
     * @param postStore
     * @param userStore
     * @param userPredicate used to determine which users' posts should be considered relevant
     * @param postPredicate used to determine which posts should be included in the final feed
     * @throws IllegalArgumentException if any argument is null
     */
    public FeedBuilder(PostStore postStore, UserProfileStore userStore,
            Predicate<UserProfile> userPredicate, Predicate<Post> postPredicate) {
        if (postStore == null) {
            throw new IllegalArgumentException("Post Store cannot be null!");
        }
        if (userStore == null) {
            throw new IllegalArgumentException("User Store cannot be null!");
        }
        if (userPredicate == null) {
            throw new IllegalArgumentException("User Predicate cannot be null!");
        }
        if (postPredicate == null) {
            throw new IllegalArgumentException("Post Predicate cannot be null!");
        }
        this.postStore = postStore;
        this.userStore = userStore;
        this.userPredicate = userPredicate;
        this.postPredicate = postPredicate;
    }
    
    /**
     * Returns the post feed for the specified user.
     * 
     * @param user
     * @return list of posts relevant to user
     */
    public List<Post> getFeedForUser(final UserProfile user) {
        List<Post> posts = new ArrayList<Post>();
        
        Collection<UserProfile> relevantUsers = this.userStore
                .getUsersForPredicate(this.userPredicate);
        
        for (UserProfile relevantUser : relevantUsers) {
            posts.addAll(postStore.getPostsByUser(relevantUser.getId(), this.postPredicate));
        }
        
        return posts;
    }
}
