package com.zihadrizkyef.oou.helper;

import com.zihadrizkyef.oou.model.AddFriend;
import com.zihadrizkyef.oou.model.ChatRoom;
import com.zihadrizkyef.oou.model.CreateChatRoom;
import com.zihadrizkyef.oou.model.DeleteFriend;
import com.zihadrizkyef.oou.model.LoginUser;
import com.zihadrizkyef.oou.model.RegisterUser;
import com.zihadrizkyef.oou.model.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 01/08/17.
 */

public interface OouApiClient {
    @FormUrlEncoded
    @POST("registerUser.php")
    Call<RegisterUser> registerUser (
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name,
            @Field("frbs_notif_id") String frbs_notif_id
    );

    @FormUrlEncoded
    @POST("loginUser.php")
    Call<LoginUser> loginUser (
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("readProfile.php")
    Call<UserProfile> readProfile (
            @Query("username") String username
    );

    @FormUrlEncoded
    @POST("addFriend.php")
    Call<AddFriend> addFriend (
            @Field("id1") Integer id1,
            @Field("id2") Integer id2
    );

    @FormUrlEncoded
    @POST("deleteFriend.php")
    Call<DeleteFriend> deleteFriend (
            @Field("id1") Integer id1,
            @Field("id2") Integer id2
    );

    @GET("friendList.php")
    Call<List<UserProfile>> friendList (
            @Query("id") Integer id
    );

    @FormUrlEncoded
    @POST("createChatRoom.php")
    Call<CreateChatRoom> createChatRoom (
            @Field("creator_id") Integer creatorId,
            @Field("recepient_id") Integer recepientId,
            @Field("is_group") Integer isGroup,
            @Field("name") String groupName
    );

    @GET("chatRoomList.php")
    Call<List<ChatRoom>> chatRoomList (
            @Query("id") Integer id
    );
}
