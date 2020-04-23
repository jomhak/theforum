package com.jomhak.theforum.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;



@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long commentId;
	
	@NotBlank
	private String content;
	
	private LocalDateTime created;
	
	@ManyToOne
	@JoinColumn(name = "postId")
	@JsonBackReference
	private Post post;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "userId")
	private User user;
	
	public Comment() {
		
	}
	
	public Comment(@NotBlank String content, LocalDateTime created, Post post, User user) {
		super();
		this.content = content;
		this.created = created;
		this.post = post;
		this.user = user;
	}
	
	

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", content=" + content + ", created=" + created + ", post=" + post
				+ "]";
	}
	
	// private User user;
	
	
}
