package com.github.stormbit.sdk.objects.models

import com.github.stormbit.sdk.utils.BooleanInt
import com.github.stormbit.sdk.utils.EnumIntSerializer
import com.github.stormbit.sdk.utils.IntEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
data class User(
    @SerialName("about") val about: String? = null,
    @SerialName("activities") val activities: String? = null,
    @SerialName("bdate") val bdate: String? = null,
    @SerialName("blacklisted") val blacklisted: BooleanInt? = null,
    @SerialName("blacklisted_by_me") val blacklistedByMe: BooleanInt? = null,
    @SerialName("books") val books: String? = null,
    @SerialName("can_access_closed") val canAccessClosed: Boolean,
    @SerialName("can_be_invited_group") val canBeInvitedGroup: Boolean? = null,
    @SerialName("can_invite_to_chats") val canInviteToChats: Boolean? = null,
    @SerialName("can_post") val canPost: BooleanInt? = null,
    @SerialName("can_see_all_posts") val canSeeAllPosts: BooleanInt? = null,
    @SerialName("can_see_audio") val canSeeAudio: BooleanInt? = null,
    @SerialName("can_send_friend_request") val canSendFriendRequest: BooleanInt? = null,
    @SerialName("can_write_private_message") val canWritePrivateMessage: BooleanInt? = null,
    @SerialName("career") val career: List<Career>? = null,
    @SerialName("city") val city: City? = null,
    @SerialName("common_count") val commonCount: Int? = null,
    @SerialName("country") val country: Country? = null,
    @SerialName("crop_photo") val cropPhoto: CropPhoto? = null,
    @SerialName("domain") val domain: String? = null,
    @SerialName("faculty") val faculty: Int? = null,
    @SerialName("faculty_name") val facultyName: String? = null,
    @SerialName("first_name") val firstName: String,
    @SerialName("followers_count") val followersCount: Int? = null,
    @SerialName("friend_status") val friendStatus: Int? = null,
    @SerialName("games") val games: String? = null,
    @SerialName("graduation") val graduation: Int? = null,
    @SerialName("has_mobile") val hasMobile: BooleanInt? = null,
    @SerialName("has_photo") val hasPhoto: BooleanInt? = null,
    @SerialName("home_phone") val homePhone: String? = null,
    @SerialName("home_town") val homeTown: String? = null,
    @SerialName("id") val id: Int,
    @SerialName("interests") val interests: String? = null,
    @SerialName("is_closed") val isClosed: Boolean,
    @SerialName("is_favorite") val isFavorite: BooleanInt? = null,
    @SerialName("is_friend") val isFriend: BooleanInt? = null,
    @SerialName("is_hidden_from_feed") val isHiddenFromFeed: BooleanInt? = null,
    @SerialName("last_name") val lastName: String,
    @SerialName("last_seen") val lastSeen: LastSeen? = null,
    @SerialName("maiden_name") val maidenName: String? = null,
    @SerialName("military") val military: List<Military>? = null,
    @SerialName("mobile_phone") val mobilePhone: String? = null,
    @SerialName("movies") val movies: String? = null,
    @SerialName("music") val music: String? = null,
    @SerialName("nickname") val nickname: String? = null,
    @SerialName("online") val isOnline: BooleanInt? = null,
    @SerialName("personal") val personal: Personal? = null,
    @SerialName("photo_100") val photo100: String? = null,
    @SerialName("photo_200") val photo200: String? = null,
    @SerialName("photo_200_orig") val photo200Orig: String? = null,
    @SerialName("photo_400_orig") val photo400Orig: String? = null,
    @SerialName("photo_50") val photo50: String? = null,
    @SerialName("photo_id") val photoId: String? = null,
    @SerialName("photo_max") val photoMax: String? = null,
    @SerialName("photo_max_orig") val photoMaxOrig: String? = null,
    @SerialName("quotes") val quotes: String? = null,
    @SerialName("relation") val relation: RelationStatus? = null,
    @SerialName("relation_partner") val relationPartner: RelationPartner? = null,
    @SerialName("relatives") val relatives: List<Relative>? = null,
    @SerialName("schools") val schools: List<School>? = null,
    @SerialName("screen_name") val screenName: String? = null,
    @SerialName("sex") val sex: Sex? = null,
    @SerialName("site") val site: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("tv") val tv: String? = null,
    @SerialName("universities") val universities: List<University>? = null,
    @SerialName("university") val university: University? = null,
    @SerialName("university_name") val universityName: String? = null,
    @SerialName("verified") val isVerified: BooleanInt? = null
) {

    val fullName: String = "$firstName $lastName"

    @Serializable
    data class City(
        @SerialName("id") val id: Int? = null,
        @SerialName("title") val title: String? = null
    )

    @Serializable
    data class Country(
        @SerialName("id") val id: Int? = null,
        @SerialName("title") val title: String? = null
    )

    @Serializable
    data class CropPhoto(
        @SerialName("crop") val crop: Crop? = null,
        @SerialName("photo") val photo: Photo? = null,
        @SerialName("rect") val rect: Rect? = null
    ) {

        @Serializable
        data class Crop(
            @SerialName("x") val x: Double? = null,
            @SerialName("x2") val x2: Int? = null,
            @SerialName("y") val y: Double? = null,
            @SerialName("y2") val y2: Double? = null
        )

        @Serializable
        data class Photo(
            @SerialName("album_id") val albumId: Int? = null,
            @SerialName("date") val date: Int? = null,
            @SerialName("has_tags") val hasTags: Boolean? = null,
            @SerialName("id") val id: Int? = null,
            @SerialName("owner_id") val ownerId: Int? = null,
            @SerialName("post_id") val postId: Int? = null,
            @SerialName("sizes") val sizes: List<Size?>? = null,
            @SerialName("text") val text: String? = null
        ) {

            @Serializable
            data class Size(
                @SerialName("height") val height: Int? = null,
                @SerialName("type") val type: String? = null,
                @SerialName("url") val url: String? = null,
                @SerialName("width") val width: Int? = null
            )
        }

        @Serializable
        data class Rect(
            @SerialName("x") val x: Double? = null,
            @SerialName("x2") val x2: Double? = null,
            @SerialName("y") val y: Double? = null,
            @SerialName("y2") val y2: Double? = null
        )
    }

    @Serializable
    data class LastSeen(
        @SerialName("platform") val platform: Int? = null,
        @SerialName("time") val time: Int? = null
    )

    @Serializable
    data class Personal(
        @SerialName("alcohol") val alcohol: Int? = null,
        @SerialName("inspired_by") val inspiredBy: String? = null,
        @SerialName("life_main") val lifeMain: Int? = null,
        @SerialName("people_main") val peopleMain: Int? = null,
        @SerialName("religion") val religion: String? = null,
        @SerialName("smoking") val smoking: Int? = null
    )
    @Serializable
    data class Military(
        @SerialName("unit") val unit: String? = null,
        @SerialName("unit_id") val unitId: Int? = null,
        @SerialName("country_id") val countryId: Int? = null,
        @SerialName("senderType") val yearFrom: Int? = null,
        @SerialName("until") val yearUntil: Int? = null
    )

    @Serializable
    data class University(
        @SerialName("id") val id: Int? = null,
        @SerialName("country") val countryId: Int? = null,
        @SerialName("city") val cityId: Int? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("faculty") val facultyId: Int? = null,
        @SerialName("faculty_name") val facultyName: String? = null,
        @SerialName("chair") val chairId: Int? = null,
        @SerialName("chair_name") val chairName: String? = null,
        @SerialName("graduation") val yearGraduation: Int? = null,
        @SerialName("education_form") val educationForm: String? = null,
        @SerialName("education_status") val educationStatus: String? = null
    )

    @Serializable
    data class Career(
        @SerialName("group_id") val groupId: Int? = null,
        @SerialName("company") val company: String? = null,
        @SerialName("country_id") val countryId: Int? = null,
        @SerialName("city_id") val cityId: Int? = null,
        @SerialName("city_name") val cityName: String? = null,
        @SerialName("senderType") val yearFrom: Int? = null,
        @SerialName("until") val yearUntil: Int? = null,
        @SerialName("position") val position: String? = null
    )

    @Serializable
    data class Relative(
        @SerialName("type") val type: Type,
        @SerialName("id") val userId: Int? = null,
        @SerialName("name") val name: Int? = null) {

        @Serializable
        enum class Type(val value: String) {
            @SerialName("child") CHILD("child"),
            @SerialName("sibling") SIBLING("sibling"),
            @SerialName("parent") PARENT("parent"),
            @SerialName("grandparent") GRANDPARENT("grandparent"),
            @SerialName("grandchild") GRANDCHILD("grandchild")
        }
    }

    @Serializable
    data class School(
        @SerialName("id") val id: Int? = null,
        @SerialName("country") val countryId: Int? = null,
        @SerialName("city") val cityId: Int? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("year_from") val yearFrom: Int? = null,
        @SerialName("year_to") val yearTo: Int? = null,
        @SerialName("year_graduated") val yearGraduated: Int? = null,
        @SerialName("class") val classLetter: String? = null,
        @SerialName("speciality") val speciality: String? = null,
        @SerialName("type") val type: Type? = null,
        @SerialName("type_str") val typeName: String? = null
    ) {

        @Serializable(with = Type.Companion::class)
        enum class Type(override val value: Int) : IntEnum {
            SCHOOL(0),
            GYMNASIUM(1),
            LYCEUM(2),
            BOARDING_SCHOOL(3),
            EVENING_SCHOOL(4),
            MUSIC_SCHOOL(5),
            SPORT_SCHOOL(6),
            ARTISTIC_SCHOOL(7),
            COLLEGE(8),
            PROFESSIONAL_LYCEUM(9),
            TECHNICAL_COLLEGE(10),
            VOCATIONAL(11),
            SPECIALIZED_SCHOOL(12),
            ART_SCHOOL(13);

            companion object : EnumIntSerializer<Type>(Type::class, values())
        }
    }

    @Serializable(with = RelationStatus.Companion::class)
    enum class RelationStatus(override val value: Int) : IntEnum {
        SINGLE(1),
        RELATIONSHIP(2),
        ENGAGED(3),
        MARRIED(4),
        COMPLICATED(5),
        ACTIVELY_SEARCHING(6),
        LOVE(7),
        CIVIL_UNION(8),
        NOT_SPECIFIED(0);

        companion object : EnumIntSerializer<RelationStatus>(RelationStatus::class, values())
    }

    @Serializable
    data class RelationPartner(
        @SerialName("id") val id: Int,
        @SerialName("first_name") val firstName: String,
        @SerialName("last_name") val lastName: String
    )

    @Serializable(with = Sex.Companion::class)
    enum class Sex(override val value: Int) : IntEnum {
        FEMALE(1),
        MALE(2),
        NOT_SPECIFIED(0);

        companion object : EnumIntSerializer<Sex>(Sex::class, values())
    }
}