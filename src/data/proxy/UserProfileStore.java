package data.proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import data.proxy.utils.FunctionalUtils;
import data.structure.UserProfile;

/**
 * UserProfileStore manages access to the stored user profiles.
 */
public class UserProfileStore {
    private Map<String, UserProfile> userProfiles;
    
    /**
     * Basic default constructor for UserProfileStore.
     */
    public UserProfileStore() {
        userProfiles = new HashMap<String, UserProfile>();
    }
    
    /**
     * Writes a user profile to storage.
     * 
     * @param profile
     */
    public void write(UserProfile profile) {
        userProfiles.put(profile.getId(), profile);
    }
    
    /**
     * Gets the posts for the specified user from storage.
     * 
     * @param user
     * @return the user's profile, or null if the user does not exist
     */
    public UserProfile getProfile(String user) {
        if (!userProfiles.containsKey(user)) {
            return null;
        } else {
            return userProfiles.get(user);
        }
    }
    
    /**
     * Returns a filtered collection of users who match the given condition.
     * 
     * @param predicate
     * @return filtered collection of users
     */
    public Collection<UserProfile> getUsersForPredicate(Predicate<UserProfile> predicate) {
        return FunctionalUtils.filteredAddAll(this.userProfiles.values(), predicate,
                new ArrayList<UserProfile>());
    }
}
