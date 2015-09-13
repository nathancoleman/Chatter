package data.proxy;

import java.util.HashMap;
import java.util.Map;

import data.structure.UserProfile;

/**
 * UserProfileStore manages access to the stored user profiles.
 */
public class UserProfileStore {
    protected Map<String, UserProfile> userProfiles;
    
    /**
     * Basic default constructor for UserProfileStore.
     */
    public UserProfileStore() {
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
    public void delete(String id) { userProfiles.remove(id); }
    
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
}
