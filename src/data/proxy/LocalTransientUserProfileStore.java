package data.proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import data.proxy.utils.FunctionalUtils;
import data.structure.UserProfile;

/**
 * LocalTransientUserProfileStore manages access to the stored user profiles locally in memory.
 */
public class LocalTransientUserProfileStore implements UserProfileStore {
    private Map<String, UserProfile> userProfiles;
    
    /**
     * Basic default constructor for UserProfileStore.
     */
    public LocalTransientUserProfileStore() {
        userProfiles = new HashMap<String, UserProfile>();
    }
    
    /**
     * Writes a user profile to storage.
     * 
     * @param profile The UserProfile object to store
     */
    public void write(UserProfile profile) {
        userProfiles.put(profile.getId(), profile);
    }
    
    /**
     * Remove a user profile from storage.
     *
     * @param id The String id for the UserProfile to delete
     */
    public void delete(String id) {
        userProfiles.remove(id);
    }
    
    /**
     * Gets the posts for the specified user from storage.
     * 
     * @param id The String id for the UserProfile to retrieve
     * @return the user's profile, or null if the user does not exist
     */
    public UserProfile getProfile(String id) {
        if (!userProfiles.containsKey(id)) {
            return null;
        } else {
            return userProfiles.get(id);
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
