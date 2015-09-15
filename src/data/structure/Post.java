package data.structure;

/**
 * Post represents a single user_id post, with both a user_id and String contents
 * associated with it.
 */
public class Post {
    private String user_id;
    private String content;
    private int id;

    public static final int PENDING_WRITE = -1;
    
    /**
     * Constructor for Post, requiring all fields.
     * 
     * @param user_id The String id for the user_id
     * @param content The content for this Post
     * @throws IllegalArgumentException
     *             if any argument is null
     */
    public Post(String user_id, String content) {
        if (user_id == null || content == null) {
            throw new IllegalArgumentException(
                    "Neither user_id nor content may be null.");
        }

        this.user_id = user_id;
        this.content = content;
        this.id = PENDING_WRITE;
    }
    
    /**
     * Getter for user_id.
     * 
     * @return user_id
     */
    public String getUser() {
        return user_id;
    }
    
    /**
     * Getter for content.
     * 
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter for id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for id with overwrite parameter.
     *
     * @param value The new value for Post id
     * @throws IllegalArgumentException
     *              if id is < 0
     */
    public void setId(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Id must be >= 0.");
        }

        id = value;
    }
    
    /**
     * Override of Object.equals()
     * 
     * @param obj
     *            the candidate for equality
     * @return boolean for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Post)) {
            return false;
        }
        
        Post otherPost = (Post) obj;
        
        return getUser().equals(otherPost.getUser())
                && getContent().equals(otherPost.getContent());
    }
    
    /**
     * Override of Object.hashCode()
     * 
     * @return the integer hashcode
     */
    @Override
    public int hashCode() {
        return (getUser() + getContent()).hashCode();
    }
}
