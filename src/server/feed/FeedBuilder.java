package server.feed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import server.matching.UserMatcher;
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
    private final UserMatcher userMatcher;
    private final Predicate<Post> postPredicate;
    
    /**
     * Constructor requires a PostStore, UserProfileStore, and UserMatcher.
     * 
     * @param postStore
     * @param userStore
     * @param userMatcher used to determine which users' posts should be considered relevant
     * @param postPredicate used to determine which posts should be included in the final feed
     * @throws IllegalArgumentException if any argument is null
     */
    public FeedBuilder(PostStore postStore, UserProfileStore userStore, UserMatcher userMatcher,
            Predicate<Post> postPredicate) {
        if (postStore == null) {
            throw new IllegalArgumentException("Post Store cannot be null!");
        }
        if (userStore == null) {
            throw new IllegalArgumentException("User Store cannot be null!");
        }
        if (userMatcher == null) {
            throw new IllegalArgumentException("User Matcher cannot be null!");
        }
        if (postPredicate == null) {
            throw new IllegalArgumentException("Post Predicate cannot be null!");
        }
        this.postStore = postStore;
        this.userStore = userStore;
        this.userMatcher = userMatcher;
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
        
        Predicate<UserProfile> userPredicate = new Predicate<UserProfile>() {
            @Override
            public boolean test(UserProfile candidate) {
                return userMatcher.matches(user, candidate);
            }
        };
        
        Collection<UserProfile> relevantUsers = this.userStore.getUsersForPredicate(userPredicate);
        
        for (UserProfile relevantUser : relevantUsers) {
            posts.addAll(postStore.getPostsByUser(relevantUser.getId(), this.postPredicate));
        }
        
        return posts;
    }
}
