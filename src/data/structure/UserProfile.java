package data.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * UserProfile represents a user, containing all user metadata.
 */
public class UserProfile {
    private final String id;
    private final Map<String, String> attributes;
    
    /**
     * Constructor requires id, but not attributes.
     * 
     * @param id
     */
    public UserProfile(String id) {
        this(id, null);
    }
    
    /**
     * Constructor requires non-null id and can take attributes.
     * 
     * @param id
     * @param attributes
     * @throws IllegalArgumentException if id is null
     */
    public UserProfile(String id, Map<String, String> attributes) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        this.id = id;
        this.attributes = attributes == null ? new HashMap<String, String>() : attributes;
        validateAttributes(this.attributes);
    }
    
    /**
     * Getter for id.
     * 
     * @return id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the value of the specified attribute, or null if the user does not have a value set for
     * this attribute.
     * 
     * @param attributeId
     * @return attribute value or null
     */
    public String getAttribute(String attributeId) {
        return this.attributes.get(attributeId);
    }
    
    /**
     * Sets the value of the specified attribute.
     * 
     * @param attributeId
     * @param attributeValue
     */
    public void setAttribute(String attributeId, String attributeValue) {
        validateAttribute(attributeId, attributeValue);
        this.attributes.put(attributeId, attributeValue);
    }
    
    /**
     * Returns the intersection between the attributes of the two users.
     * 
     * @param otherUser
     * @return intersection as Map
     * @throws IllegalArgumentException if otherUser is null
     */
    public Map<String, String> getAttributeIntersection(UserProfile otherUser) {
        if (otherUser == null) {
            throw new IllegalArgumentException("Other User Profile cannot be null!");
        }
        Map<String, String> intersection = new HashMap<String, String>();
        for (Entry<String, String> myAttribute : this.attributes.entrySet()) {
            String attributeId = myAttribute.getKey();
            String attributeValue = myAttribute.getValue();
            if (attributeValue.equals(otherUser.getAttribute(attributeId))) {
                intersection.put(attributeId, attributeValue);
            }
        }
        return intersection;
    }
    
    /**
     * Override of Object.equals(), based on user ID.
     * 
     * @param obj candidate for equality
     * @return boolean for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserProfile)) {
            return false;
        }
        UserProfile user = (UserProfile) obj;
        return this.id.equals(user.getId());
    }
    
    /**
     * Override of Object.hashCode(), based on user ID.
     * 
     * @return hashCode for user
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
    
    /**
     * Validates a set of attributes
     * 
     * @param attributes
     */
    private void validateAttributes(Map<String, String> attributes) {
        for (Entry<String, String> attribute : attributes.entrySet()) {
            validateAttribute(attribute.getKey(), attribute.getValue());
        }
    }
    
    /**
     * Validates an attribute mapping.
     * 
     * @param attributeId
     * @param attributeValue
     * @throws IllegalArgumentException if an attribute mapping is invalid.
     */
    private void validateAttribute(String attributeId, String attributeValue) {
        if (attributeId == null) {
            throw new IllegalArgumentException("An attribute cannot have a null ID!");
        }
        if (attributeValue == null) {
            throw new IllegalArgumentException("An attribute cannot have a null value!");
        }
    }
    
}
