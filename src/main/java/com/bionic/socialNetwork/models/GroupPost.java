package com.bionic.socialNetwork.models;

import javax.persistence.*;

/**
 * GroupPost entity
 *
 * @author yoalex5
 * @version 1.00 16.07.14
 */
@Entity
@Table (name = "Posts_Of_Groups")
public class GroupPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_post_id")
    private long groupPostId;

    @Column(name = "post")
    private String post;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public GroupPost() {

    }

    public GroupPost(Group group, User user,  String post) {
        this.group = group;
        this.user = user;
        this.post = post;
    }

    public long getGroupPostId() {
        return groupPostId;
    }

    public void setGroupPostId(long groupPostId) {
        this.groupPostId = groupPostId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
