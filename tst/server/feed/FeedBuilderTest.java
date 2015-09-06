package server.feed;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import server.matching.PercentMatchUserMatcher;
import server.matching.UserMatcher;
import data.proxy.PostStore;
import data.proxy.UserProfileStore;
import data.structure.Post;
import data.structure.UserProfile;

/**
 * Tests the functionality of the FeedBuilder class.
 */
public class FeedBuilderTest {
    
    /**
     * Tests the normal case of getFeedForUser().
     */
    @Test
    public void testGetFeedForUserormalCase() {
        final UserProfile user1 = new UserProfile("Seth");
        final UserProfile user2 = new UserProfile("Charles");
        final UserProfile user3 = new UserProfile("Nathan");
        
        // Set a single common attribute, so that user1 and user2 are guaranteed to match.
        final String ATTRIBUTE_NAME = "profession";
        final String ATTRIBUTE_VALUE = "Software Developer";
        user1.setAttribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        user2.setAttribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        
        // Set only a single non-matching attribute for user3, so that it is guaranteed to not
        // match.
        user3.setAttribute("uniqueAttribute", "I'm special!");
        
        UserProfileStore userStore = createMock(UserProfileStore.class);
        expect(userStore.getUsersForPredicate(anyObject(Predicate.class))).andReturn(
                Arrays.asList(user1, user2)).atLeastOnce();
        replay(userStore);
        
        Post user1Post1 = new Post(user1.getId(), "post1");
        Post user1Post2 = new Post(user1.getId(), "post2");
        Post user2Post1 = new Post(user2.getId(), "post1");
        Post user2Post2 = new Post(user2.getId(), "post2");
        
        List<Post> expectedPosts = Arrays.asList(user1Post1, user1Post2, user2Post1, user2Post2);
        
        PostStore postStore = createMock(PostStore.class);
        expect(postStore.getPostsByUser(eq(user1.getId()), anyObject(Predicate.class))).andReturn(
                Arrays.asList(user1Post1, user1Post2)).atLeastOnce();
        expect(postStore.getPostsByUser(eq(user2.getId()), anyObject(Predicate.class))).andReturn(
                Arrays.asList(user2Post1, user2Post2)).atLeastOnce();
        replay(postStore);
        
        final UserMatcher matcher = new PercentMatchUserMatcher(1);
        Predicate<UserProfile> userPredicate = new Predicate<UserProfile>() {
            @Override
            public boolean test(UserProfile candidate) {
                return matcher.matches(user1, candidate);
            }
        };
        Predicate<Post> postPredicate = new Predicate<Post>() {
            @Override
            public boolean test(Post candidate) {
                return true;
            }
        };
        
        FeedBuilder feedBuilder = new FeedBuilder(postStore, userStore, userPredicate,
                postPredicate);
        
        List<Post> feed = feedBuilder.getFeedForUser(user1);
        
        assertTrue("Some expected posts were not included in the feed!",
                feed.containsAll(expectedPosts));
        
        assertTrue("The feed contained some posts that should not have been included!",
                feed.size() == expectedPosts.size());
        
        verify(postStore);
        verify(userStore);
    }
}
