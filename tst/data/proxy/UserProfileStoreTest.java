package data.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import data.structure.UserProfile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserProfileStoreTest {

    UserProfileStore ups;

    @Before
    public void before() {
        ups = new UserProfileStore();
    }

    @After
    public void after() {
        ups = null;
    }

    /**
     * Tests null return for missing UserProfile id.
     */
    @Test
    public void testGetProfileNonExistent() {
        UserProfile profile = ups.getProfile("missing_user_id");

        assertNull("A missing UserProfile id was passed in, but null was not returned", profile);
    }

    /**
     * Tests the basic write/retrieve/delete functionality of the UserProfileStore.
     */
    @Test
    public void testStandardUserProfileCycle() {
        // Create two users
        final UserProfile u1 = new UserProfile("Seth");
        final UserProfile u2 = new UserProfile("Nathan");

        // Write users to user profile store
        ups.write(u1);
        ups.write(u2);

        // Check returned users
        assertEquals("Users do not match!", u1, ups.getProfile(u1.getId()));
        assertEquals("Users do not match!", u2, ups.getProfile(u2.getId()));

        // Remove two users
        ups.delete(u1.getId());
        ups.delete(u2.getId());

        // Check returned users
        assertNull("User was not deleted!", ups.getProfile(u1.getId()));
        assertNull("User was not deleted!", ups.getProfile(u2.getId()));
    }
}
