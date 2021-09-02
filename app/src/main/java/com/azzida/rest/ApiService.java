package com.azzida.rest;


import com.azzida.model.ApplicationAcceptedModel;
import com.azzida.model.CheckerModel;
import com.azzida.model.CreateJobModel;
import com.azzida.model.CreatePaymentModel;
import com.azzida.model.DisputeModel;
import com.azzida.model.EditProfileModel;
import com.azzida.model.GetCheckerReportModel;
import com.azzida.model.GetCustomerCardsModel;
import com.azzida.model.GetJobByIdModel;
import com.azzida.model.GetJobCategoryModel;
import com.azzida.model.GetJobDetailModel;
import com.azzida.model.GetJobFeeModel;
import com.azzida.model.GetRecentModel;
import com.azzida.model.GetUserChatModel;
import com.azzida.model.JobApplicationModel;
import com.azzida.model.JobCompleteModel;
import com.azzida.model.ListerCompletedJobModel;
import com.azzida.model.ListerInProgressJobModel;
import com.azzida.model.ListerJobCancelledListModel;
import com.azzida.model.MyJobModel;
import com.azzida.model.OfferAcceptModel;
import com.azzida.model.PostAssociateModel;
import com.azzida.model.ProfileModel;
import com.azzida.model.PromoCodeModel;
import com.azzida.model.ReferalBalanceModel;
import com.azzida.model.SaveCardModel;
import com.azzida.model.SaveChatModel;
import com.azzida.model.SaveJobImagesModel;
import com.azzida.model.SaveRateModel;
import com.azzida.model.SeekerCompletedJobModel;
import com.azzida.model.SeekerInprogressJobModel;
import com.azzida.model.SeekerJobCancelledListModel;
import com.azzida.model.SignInModel;
import com.azzida.model.SignUpModel;
import com.azzida.model.SuccessModel;
import com.azzida.model.TipModel;
import com.azzida.model.UserModel;
import com.azzida.model.ViewApplicantDetailModel;
import com.azzida.model.ViewApplicantListModel;
import com.azzida.model.ViewListerUserModel;
import com.azzida.model.ViewPaymentTransactionModel;
import com.azzida.model.map.MapModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;


public interface ApiService {

   /* @Multipart
    @POST(ApiUrls.URL_CreateJob)
    Call<CreateJobModel> CreateJob(@PartMap() Map<String, RequestBody> partMap,
                                   @Part MultipartBody.Part imagePic


    );*/

    @FormUrlEncoded
    @POST(ApiUrls.URL_CreateJob)
    Call<CreateJobModel> CreateJob(@Field("Id") String Id,
                                   @Field("UserId") String UserId,
                                   @Field("JobTitle") String JobTitle,
                                   @Field("HowLong") String HowLong,
                                   @Field("Amount") String Amount,
                                   @Field("JobCategory") String JobCategory,
                                   @Field("Location") String Location,
                                   @Field("FromDate") String FromDate,
                                   @Field("JobDescription") String JobDescription,
                                   @Field("Latitude") String Latitude,
                                   @Field("Longitude") String Longitude,
                                   @Field("imglist") String imglist
    );


    @GET(ApiUrls.URL_GetJobById)
    Call<GetJobByIdModel> GetJobById(@Query("JobId") String JobId


    );


    @GET(ApiUrls.URL_GetJobDetail)
    Call<GetJobDetailModel> GetJobDetail(@Query("JobId") String JobId,
                                         @Query("UserId") String UserId


    );


    @GET(ApiUrls.URL_MyJob)
    Call<MyJobModel> MyJob(@Query("UserId") String UserId,
                           @Query("Category") String Category,
                           @Query("radius") String radius,
                           @Query("latitude") String latitude,
                           @Query("longitude") String longitude,
                           @Query("minprice") String minprice,
                           @Query("maxprice") String maxprice,
                           @Query("show") String show,
                           @Query("showactive") String showactive
    );



    @GET(ApiUrls.URL_Map)
    Call<MapModel> Map(@Query("latlng") String latlng,
                       @Query("sensor") String sensor,
                       @Query("key") String key

    );

    @POST(ApiUrls.URL_GetAppLink)
    Call<SuccessModel> GetAppLink(@Query("Id") String Id,
                                  @Query("applink") String applink

    );

    @GET(ApiUrls.URL_GetProfile)
    Call<ProfileModel> GetProfile(@Query("UserId") String UserId

    );

    @GET(ApiUrls.URL_GetProfile)
    Call<UserModel> GetUser(@Query("UserId") String UserId

    );


    @Multipart
    @POST(ApiUrls.URL_Dispute)
    Call<DisputeModel> Dispute(@PartMap() Map<String, RequestBody> partMap,
                               @Part MultipartBody.Part imagePic


    );

    @FormUrlEncoded
    @POST(ApiUrls.URL_Dispute)
    Call<DisputeModel> DisputeNew(@Field("Id") String Id,
                                  @Field("UserId") String UserId,
                                  @Field("JobId") String JobId,
                                  @Field("DisputeReason") String DisputeReason,
                                  @Field("PostAssociate") String PostAssociate,
                                  @Field("Description") String Description,
                                  @Field("image") String image

    );


    @Multipart
    @POST(ApiUrls.URL_SaveJobImages)
    Call<SaveJobImagesModel> SaveJobImages(@Part List<MultipartBody.Part> imagePic


    );

    @FormUrlEncoded
    @POST(ApiUrls.URL_Checker)
    Call<CheckerModel> Checker(@Header("Authorization") String access_token,
                               @Field("first_name") String first_name,
                               @Field("middle_name") String middle_name,
                               @Field("last_name") String last_name,
                               @Field("email") String email,
                               @Field("phone") String phone,
                               @Field("zipcode") String zipcode,
                               @Field("dob") String dob,
                               @Field("ssn") String ssn,
                               @Field("no_middle_name") Boolean no_middle_name


    );

   /* @Headers("Content-Type: application/json")
    @POST(ApiUrls.URL_Checker)
    Call<CheckerModel> Checker(@Header("Authorization") String access_token,
                               @Body String body


    );*/

    @GET(ApiUrls.URL_ViewListerUser)
    Call<ViewListerUserModel> ViewListerUser(@Query("UserId") String UserId

    );


    @GET(ApiUrls.URL_PostAssociate)
    Call<PostAssociateModel> PostAssociate(@Query("UserId") String UserId

    );

    @GET(ApiUrls.URL_SeekerInprogressJob)
    Call<SeekerInprogressJobModel> SeekerInprogressJob(@Query("UserId") String UserId

    );

    @POST(ApiUrls.URL_GetPromoCodeList)
    Call<PromoCodeModel> GetPromoCodeList(@Query("UserId") String UserId

    );


    @GET(ApiUrls.URL_ViewApplicantList)
    Call<ViewApplicantListModel> ViewApplicantList(@Query("JobId") String JobId

    );

    @POST(ApiUrls.URL_JobComplete)
    Call<JobCompleteModel> JobComplete(@Query("Id") String Id,
                                       @Query("CompleteDate") String CompleteDate,
                                       @Query("IsComplete") String IsComplete

    );

    @POST(ApiUrls.URL_SaveCard)
    Call<SaveCardModel> SaveCard(@Query("TokenId") String TokenId,
                                 @Query("UserId") String UserId,
                                 @Query("TokenUsed") String TokenUsed

    );

    @POST(ApiUrls.URL_DeleteCard)
    Call<SuccessModel> DeleteCard(@Query("CardId") String CardId

    );


    @POST(ApiUrls.URL_UpdateCandidateData)
    Call<SuccessModel> UpdateCandidateData(@Query("UserId") String UserId,
                                           @Query("CandidateId") String CandidateId,
                                           @Query("CFirstName") String CFirstName,
                                           @Query("CLastName") String CLastName,
                                           @Query("CMiddleName") String CMiddleName,
                                           @Query("CDOB") String CDOB,
                                           @Query("Cssn") String Cssn,
                                           @Query("CEmail") String CEmail,
                                           @Query("CPhone") String CPhone,
                                           @Query("CZipCode") String CZipCode,
                                           @Query("CCreatedAt") String CCreatedAt,
                                           @Query("IpAddress") String IpAddress

    );

    @POST(ApiUrls.URL_GetCustomerCards)
    Call<GetCustomerCardsModel> GetCustomerCards(@Query("UserId") String UserId

    );

    @POST(ApiUrls.URL_CheckerReport)
    Call<GetCheckerReportModel> CheckerReport(@Query("UserId") String UserId,
                                              @Query("CandidateId") String CandidateId

    );

    @GET(ApiUrls.URL_GetJobFee)
    Call<GetJobFeeModel> GetJobFee(

    );

    @GET(ApiUrls.URL_GetRecentActivity)
    Call<GetRecentModel> GetRecent(@Query("UserId") String UserId

    );

    @POST(ApiUrls.URL_CreatePayment)
    Call<CreatePaymentModel> CreatePayment(@Query("JobId") String JobId,
                                           @Query("UserId") String UserId,
                                           @Query("ToUserId") String ToUserId,
                                           @Query("refbalance") String refbalance,
                                           @Query("CustomerId") String CustomerId,
                                           @Query("TotalAmount") String TotalAmount,
                                           @Query("PaymentToken") String PaymentToken,
                                           @Query("PaymentType") String PaymentType,
                                           @Query("PromoCode") String PromoCode,
                                           @Query("TokenUsed") String TokenUsed

    );

    @GET(ApiUrls.SeekerCompletedJob)
    Call<SeekerCompletedJobModel> SeekerCompletedJob(@Query("UserId") String UserId

    );


    @GET(ApiUrls.URL_ViewPaymentTransaction)
    Call<ViewPaymentTransactionModel> ViewPaymentTransaction(@Query("UserId") String UserId

    );


    @POST(ApiUrls.URL_Tip)
    Call<TipModel> Tip(@Query("Id") String Id,
                       @Query("UserId") String UserId,
                       @Query("JobId") String JobId,
                       @Query("TippingAmount") String TippingAmount,
                       @Query("TotalAmount") String TotalAmount,
                       @Query("SeekerId") String SeekerId,
                       @Query("SeekerRate") String SeekerRate,
                       @Query("paymentId") String paymentId

    );

    @GET(ApiUrls.URL_ListerCompletedJob)
    Call<ListerCompletedJobModel> ListerCompletedJob(@Query("UserId") String UserId

    );

    @GET(ApiUrls.URL_ListerJobCancelledList)
    Call<ListerJobCancelledListModel> ListerJobCancelledList(@Query("UserId") String UserId

    );

    @GET(ApiUrls.URL_SeekerJobCancelledList)
    Call<SeekerJobCancelledListModel> SeekerJobCancelledList(@Query("UserId") String UserId

    );

    @GET(ApiUrls.URL_ViewApplicantDetail)
    Call<ViewApplicantDetailModel> ViewApplicantDetail(@Query("SeekerId") String SeekerId

    );

    @FormUrlEncoded
    @POST(ApiUrls.URL_SignUp)
    Call<SignUpModel> SignUp(@Field("Id") String Id,
                             @Field("RoleId") String RoleId,
                             @Field("FirstName") String FirstName,
                             @Field("LastName") String LastName,
                             @Field("UserPassword") String UserPassword,
                             @Field("UserEmail") String UserEmail,
                             @Field("Skills") String Skills,
                             @Field("DeviceId") String DeviceId,
                             @Field("DeviceType") String DeviceType,
                             @Field("EmailType") String EmailType,
                             @Field("UserName") String UserName,
                             @Field("Image") String Image,
                             @Field("JobType") String JobType,
                             @Field("ReferalCode") String ReferalCode,
                             @Field("StripeAccId") String StripeAccId
    );


    @Multipart
    @POST(ApiUrls.URL_SignUp)
    Call<EditProfileModel> EditProfile(@PartMap() Map<String, RequestBody> partMap,
                                       @Part MultipartBody.Part imagePic


    );


    @FormUrlEncoded
    @POST(ApiUrls.URL_SignUp)
    Call<EditProfileModel> EditProfileNw(@Field("Id") String Id,
                                         @Field("RoleId") String RoleId,
                                         @Field("FirstName") String FirstName,
                                         @Field("LastName") String LastName,
                                         @Field("UserPassword") String UserPassword,
                                         @Field("UserEmail") String UserEmail,
                                         @Field("Skills") String Skills,
                                         @Field("DeviceId") String DeviceId,
                                         @Field("DeviceType") String DeviceType,
                                         @Field("EmailType") String EmailType,
                                         @Field("UserName") String UserName,
                                         @Field("Image") String Image,
                                         @Field("JobType") String JobType,
                                         @Field("ReferalCode") String ReferalCode,
                                         @Field("StripeAccId") String StripeAccId


    );


    @POST(ApiUrls.URL_SaveRate)
    Call<SaveRateModel> SaveRate(@Query("UserId") String UserId,
                                 @Query("JobId") String JobId,
                                 @Query("Rate") String Rate,
                                 @Query("SeekerId") String SeekerId

    );


    @POST(ApiUrls.URL_OfferAccept)
    Call<OfferAcceptModel> OfferAccept(@Query("JobId") String JobId,
                                       @Query("SeekerId") String SeekerId,
                                       @Query("ListerId") String ListerId,
                                       @Query("IsAcceptedbySeeker") String IsAcceptedbySeeker

    );


    @POST(ApiUrls.URL_CancelJobById)
    Call<SuccessModel> CancelJob(@Query("JobId") String JobId,
                                     @Query("Reason") String Reason,
                                     @Query("UserId") String UserId,
                                     @Query("DateRequest") String DateRequest

    );

    @FormUrlEncoded
    @POST(ApiUrls.URL_CancelSeekerJobApplication)
    Call<SuccessModel> CancelSeekerJobApplication(@Field("UserId") String UserId,
                                                      @Field("JobId") String JobId

    );

    @FormUrlEncoded
    @POST(ApiUrls.URL_JobCancelApplicationAcceptReject)
    Call<SuccessModel> JobCancelApplicationAcceptReject(@Field("UserId") String UserId,
                                                            @Field("JobId") String JobId,
                                                            @Field("IsAccept") String IsAccept,
                                                            @Field("DateReply") String DateReply

    );

    @POST(ApiUrls.URL_RePostJob)
    Call<SuccessModel> RePostJob(@Query("JobId") String JobId,
                                     @Query("UserId") String UserId,
                                     @Query("FromDate") String FromDate

    );

    @POST(ApiUrls.URL_SaveChat)
    Call<SaveChatModel> SaveChat(@Query("Id") String Id,
                                 @Query("ToId") String ToId,
                                 @Query("FromId") String FromId,
                                 @Query("IsTyping") String IsTyping,
                                 @Query("UserMessage") String UserMessage,
                                 @Query("MessageDateTime") String MessageDateTime,
                                 @Query("JobId") String JobId

    );


    @POST(ApiUrls.URL_JobApplication)
    Call<JobApplicationModel> JobApplication(@Query("Id") String Id,
                                             @Query("SeekerId") String SeekerId,
                                             @Query("ListerId") String ListerId,
                                             @Query("JobId") String JobId,
                                             @Query("IsApply") String IsApply

    );


    @GET(ApiUrls.URL_GetUserChat)
    Call<GetUserChatModel> GetUserChat(@Query("FromId") String FromId,
                                       @Query("ToId") String ToId,
                                       @Query("JobId") String JobId

    );


    @GET(ApiUrls.URL_RetrieveStripeAccount)
    Call<SuccessModel> RetrieveStripeAccount(@Query("code") String code,
                                             @Query("userid") String userid,
                                             @Query("accountnumber") String accountnumber,
                                             @Query("TokenUsed") String TokenUsed

    );


    @GET(ApiUrls.URL_PayoutToConnectedAccount)
    Call<SuccessModel> PayoutToConnectedAccount(@Query("amount") String amount,
                                                @Query("accountnumber") String accountnumber,
                                                @Query("UserId") String UserId,
                                                @Query("TokenUsed") String TokenUsed

    );

    @POST(ApiUrls.URL_ForgotPassword)
    Call<SignInModel> ForgotPassword(@Query("Email") String Email


    );

    @POST(ApiUrls.URL_LOGIN)
    Call<SignInModel> Login(@Query("UserName") String UserName,
                            @Query("UserPassword") String UserPassword,
                            @Query("deviceId") String deviceId,
                            @Query("devicetype") String devicetype


    );

    @POST(ApiUrls.URL_FacebookGoogleLogin)
    Call<SignInModel> FacebookGoogleLogin(@Query("Email") String Email,
                                          @Query("UserName") String UserName,
                                          @Query("TokenId") String TokenId,
                                          @Query("deviceId") String deviceId,
                                          @Query("devicetype") String devicetype,
                                          @Query("Provider") String Provider


    );

    @POST(ApiUrls.URL_UpdateUserLatLong)
    Call<SignInModel> UpdateUserLatLong(@Query("UserId") String UserId,
                                        @Query("Latitude") String Latitude,
                                        @Query("Longitude") String Longitude


    );

    @GET(ApiUrls.URL_ListerInProgressJob)
    Call<ListerInProgressJobModel> ListerInProgressJob(@Query("UserId") String UserId

    );

    @GET(ApiUrls.URL_GetJobCategory)
    Call<GetJobCategoryModel> GetJobCategory();

    @GET(ApiUrls.URL_GetReferalBalance)
    Call<ReferalBalanceModel> GetReferalBalance(@Query("UserId") String UserId

    );

    @POST(ApiUrls.URL_Logout)
    Call<GetJobCategoryModel> Logout(@Query("UserId") String UserId

    );

    @POST(ApiUrls.URL_ApplicationAccepted)
    Call<ApplicationAcceptedModel> ApplicationAccepted(@Query("JobId") String JobId,
                                                       @Query("SeekerId") String SeekerId,
                                                       @Query("ListerId") String ListerId,
                                                       @Query("IsAcceptedByLister") String IsAcceptedByLister


    );

}