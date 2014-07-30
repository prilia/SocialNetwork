package com.bionic.socialNetwork.rest;

import com.bionic.socialNetwork.logic.EditUserProfileLogic;
import com.bionic.socialNetwork.logic.UserAvatarLogic;
import com.bionic.socialNetwork.logic.UserLogic;
import com.bionic.socialNetwork.logic.lists.InterestList;
import com.bionic.socialNetwork.logic.lists.PostsList;
import com.bionic.socialNetwork.logic.lists.UserGroupsList;
import com.bionic.socialNetwork.models.User;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;

/**
 * @author Dmytro Troshchuk, Igor Kozhevnikov, Denis Biyovskiy
 * @version 1.00  18.07.14.
 */


@Path("user{id}")
public class UserController {
    @GET
    @Path("interests")
    @Produces(MediaType.APPLICATION_JSON)
    public InterestList getInterests(@PathParam("id") long id) {
        return new InterestList(id);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public InputStream getPage(@Context ServletContext context) {
        return context.getResourceAsStream("/WEB-INF/pages/user.html");


    }

    @GET
    @Path("getUser")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") long id) {
        return new UserLogic().getUser(id);
    }

    @POST
    @Path("createPost")
    @Produces(MediaType.APPLICATION_JSON)
    public String addPost(@Context HttpServletRequest request,
                          @FormParam("msg") String msg) {
        return "{\"status\": " + new UserLogic()
                .createPost((Long) request.getAttribute("userId"), msg) + "}";
    }

    @POST
    @Path("deletePost")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePost(@Context HttpServletRequest request,
                             @FormParam("postId") long postId) {
        return "{\"status\": " + new UserLogic()
                .deletePost((Long) request.getAttribute("userId"), postId) +
               "}";
    }

    @GET
    @Path("posts{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public PostsList getPosts(@PathParam("id") long id,
                              @PathParam("page") int page) {
        return new PostsList(id, page);

    }

    @GET
    @Path("groups{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserGroupsList getGroups(@PathParam("id") long id,
                                    @PathParam("page") int page) {
        return new UserGroupsList(id, page);
    }

    @POST
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String editUser(@Context HttpServletRequest request,
                           @FormParam("interests") String interests,
                           @FormParam("name") String name,
                           @FormParam("surname") String surname,
                           @FormParam("position") String position){
        long userId = (Long) request.getAttribute("userId");
        EditUserProfileLogic editUserProfile = new EditUserProfileLogic();
        editUserProfile.edit(userId, name, surname, position, interests);
        return "{\"status\": true}";

    }

    @POST
    @Path("setAvatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String setAvatar(@FormDataParam("file") InputStream uploadedInputStream,
                         @FormDataParam("file") FormDataContentDisposition fileDetail,
                         @Context HttpServletRequest request,
                         @Context ServletContext context){

        long userId = (Long)request.getAttribute("userId");
        String uploadedFileLocation = context.getRealPath("/WEB-INF");
        UserAvatarLogic userAvatarLogic = new UserAvatarLogic();
        userAvatarLogic.saveAvatar(uploadedInputStream, uploadedFileLocation, fileDetail.getFileName(), userId);
        return "{\"status\": true}";
    }

    @GET
    @Path("getAvatar")
    @Produces({"image/png", "image/jpeg"})
    public Response getAvatar(@Context ServletContext context,
                              @PathParam("id") long id){


        UserAvatarLogic userAvatarLogic = new UserAvatarLogic();
        File file = userAvatarLogic.getAvatar(context.getRealPath("/WEB-INF"), id);
        return  Response.ok(file).build();
    }


    @GET
    @Path("exit")
    @Produces(MediaType.APPLICATION_JSON)
    public String exit(@Context HttpServletResponse response) {
        Cookie cookie = new Cookie("sessionId", "0");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        Cookie userIdCookie = new Cookie("userId", "0");
        userIdCookie.setPath("/");
        userIdCookie.setMaxAge(0);
        response.addCookie(userIdCookie);
        return "{\"status\": true}";
    }
}
