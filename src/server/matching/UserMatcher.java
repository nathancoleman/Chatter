package server.matching;

import data.structure.UserProfile;

/**
 * UserMatcher provides an interface for determining whether or not two users should be considered
 * relevant to each other. One user is the primary user, which means that the decision revolves
 * around whether or not the secondary user is relevant to the primary user when some asymmetry
 * might exist.
 */
public interface UserMatcher {
    
    /**
     * Determines whether or not the secondary user should be considered relevant to the primary
     * user.
     * 
     * @param primary
     * @param secondary
     * @return true if the secondary user is relevant to the primary user, and false otherwise
     */
    public boolean matches(UserProfile primary, UserProfile secondary);
}
