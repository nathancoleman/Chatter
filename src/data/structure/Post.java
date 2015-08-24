package data.structure;

/**
 * Post represents a single user post, with both
 * a user and String contents associated with it.
 */
public class Post {
	private String user;
	private String content;
	
	/**
	 * Constructor for Post, requiring all fields.
	 * @param user
	 * @param content
	 * @throws IllegalArgumentException if any argument is null
	 */
	public Post(String user, String content) {
		if (user == null || content == null) {
			throw new IllegalArgumentException("Neither user nor content may be null.");
		}
		this.user = user;
		this.content = content;
	}

	/**
	 * Getter for user.
	 * @return user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Getter for content.
	 * @return content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Override of Object.equals()
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
}
