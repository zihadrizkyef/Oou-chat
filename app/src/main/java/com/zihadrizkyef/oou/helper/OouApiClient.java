package com.zihadrizkyef.oou.helper;

import com.zihadrizkyef.oou.model.AddFriend;
import com.zihadrizkyef.oou.model.ChangeProfilePicture;
import com.zihadrizkyef.oou.model.Chat;
import com.zihadrizkyef.oou.model.ChatRoom;
import com.zihadrizkyef.oou.model.CreateChatRoom;
import com.zihadrizkyef.oou.model.DeleteFriend;
import com.zihadrizkyef.oou.model.EditProfile;
import com.zihadrizkyef.oou.model.LoginUser;
import com.zihadrizkyef.oou.model.RegisterUser;
import com.zihadrizkyef.oou.model.SendChat;
import com.zihadrizkyef.oou.model.UserProfile;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @FormUrlEncoded
    @POST("editProfile.php")
    Call<EditProfile> editProfile(
            @Field("id") Integer id,
            @Field("name") String name,
            @Field("bio") String bio,
            @Field("frbs_notif_id") String frbsNotifId
    );

    @FormUrlEncoded
    @POST("sendChat.php")
    Call<SendChat> sendChat(
            @Field("id") Integer id,
            @Field("chat_room_id") Integer chatRoomId,
            @Field("text") String text
    );

    @GET("chatRowList.php")
    Call<List<Chat>> chatRowList(
            @Query("room_id") Integer roomId,
            @Query("offset") Integer offset
    );

    @Multipart
    @POST("changeProfilePicture.php")
    Call<ChangeProfilePicture> changeProfilePicture(
            @Part("id") RequestBody id,
            @Part MultipartBody.Part image
    );
}
