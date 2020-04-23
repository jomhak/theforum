package com.jomhak.theforum.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long postId;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String content;
	
	private LocalDateTime created;
	
	// private User user;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	// @JoinColumn(name = "commentId")
	private List<Comment> comments;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "categoryId")
	private Category category;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "userId")
	private User user;


	public Post() {
		
	}
	
	public Post(@NotBlank String title, @NotBlank String content, LocalDateTime created, Category category, User user) {
		super();
		this.title = title;
		this.content = content;
		this.created = created;
		this.category = category;
		this.user = user;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(@NotBlank String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(@NotBlank String content) {
		this.content = content;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		if (this.category != null) {
			return "Post [postId=" + postId + ", title=" + title + ", content=" + content + ", created=" + created
					+ ", comments=" + comments + ", category=" + this.getCategory() + "]";
		} else {
			return "Post [postId=" + postId + ", title=" + title + ", content=" + content + ", created=" + created
					+ ", comments=" + comments + "]";
		}
		
		
		
		
	}
	
	

}
