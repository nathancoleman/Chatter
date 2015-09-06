package server.matching;

import data.structure.UserProfile;

/**
 * {@inheritDoc} PercentMatchUserMatcher matches users based on a certain percentage of the user's
 * attributes being a match with the other user.
 */
public class PercentMatchUserMatcher implements UserMatcher {
    
    private final double threshold;
    
    /**
     * Constructor requires a double threshold for the minimum percent match.
     * 
     * @param threshold
     * @throws IllegalArgumentException if threshold is outside [0,1]
     */
    public PercentMatchUserMatcher(double threshold) {
        if (threshold < 0 || threshold > 1) {
            throw new IllegalArgumentException(
                    "The threshold percentage must be in the range [0,1]!");
        }
        this.threshold = threshold;
    }
    
    /**
     * {@inheritDoc} This implementation matches based on a required percentage match threshold.
     */
    @Override
    public boolean matches(UserProfile primary, UserProfile secondary) {
        return (double) primary.getAttributeIntersection(secondary).size()
                / primary.getAttributes().size() >= this.threshold;
    }
}
