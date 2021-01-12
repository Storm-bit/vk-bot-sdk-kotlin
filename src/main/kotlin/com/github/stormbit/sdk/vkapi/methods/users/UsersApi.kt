package com.github.stormbit.sdk.vkapi.methods.users

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.models.Community
import com.github.stormbit.sdk.objects.models.ListResponse
import com.github.stormbit.sdk.objects.models.User
import com.github.stormbit.sdk.utils.parametersOf
import com.github.stormbit.sdk.utils.toInt
import com.github.stormbit.sdk.vkapi.methods.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

@Suppress("unused")
class UsersApi(private val client: Client) : MethodsContext() {
    fun get(
            userNames: List<String>? = null,
            userFields: List<UserOptionalField>? = null,
            nameCase: NameCase? = null
    ): List<User>? = Methods.get.callSync(
        client, ListSerializer(User.serializer()), parametersOf {
            append("user_ids", userNames?.joinToString(","))
            append("fields", userFields?.joinToString(",") { it.value })
            append("name_case", nameCase?.value)
        }
    )

    fun getById(
            userIds: List<Int>,
            userFields: List<UserOptionalField>? = null,
            nameCase: NameCase = NameCase.NOM
    ): List<User>? = get(
            userNames = userIds.map(Int::toString),
            userFields = userFields,
            nameCase = nameCase
    )

    fun getName(
            userId: Int,
            nameCase: NameCase = NameCase.NOM
    ): String {
        val user = getById(listOf(userId), null, nameCase)!![0]

        return "${user.firstName} ${user.lastName}"
    }

    fun getFollowers(
            userId: Int? = null,
            offset: Int,
            count: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase
    ): List<User>? = Methods.getFollowers.callSync(
        client, ListSerializer(User.serializer()), parametersOf {
            append("user_id", userId)
            append("offset", offset)
            append("count", count)
            append("fields", userFields.joinToString(",") { it.value })
            append("name_case", nameCase.value)
        }
    )

    fun getNearby(
            latitude: Double,
            longitude: Double,
            accuracy: Int? = null,
            timeout: Int,
            radius: NearbyRadius,
            userFields: List<UserOptionalField>,
            nameCase: NameCase,
            needDescription: Boolean
    ): List<User>? = Methods.getNearby.callSync(
        client, ListSerializer(User.serializer()), parametersOf {
            append("latitude", latitude)
            append("longitude", longitude)
            append("accuracy", accuracy)
            append("timeout", timeout)
            append("radius", radius.value)
            append("fields", userFields.joinToString(",") { it.value })
            append("name_case", nameCase.value)
            append("need_description", needDescription.toInt())
        }
    )

    fun getSubscriptions(
            userId: Int? = null,
            offset: Int = 0,
            count: Int = 1,
            fields: List<ObjectField> = emptyList()
    ): ListResponse<Community>? = Methods.getSubscriptions.callSync(
        client, ListResponse.serializer(Community.serializer()), parametersOf {
            append("user_id", userId)
            append("extended", 1)
            append("offset", offset)
            append("count", count)
            append("fields", fields.joinToString(",") { it.value })
        }
    )

    fun getSubscriptionsIds(
            userId: Int? = null
    ): List<Int>? = getSubscriptions(userId)?.items?.map { it.id }

    fun isAppUser(
            userId: Int? = null
    ): Int? = Methods.isAppUser.callSync(
        client, Int.serializer(), parametersOf {
            append("user_id", userId)
        }
    )

    fun report(
            userId: Int,
            complaintType: UserReportComplaintType,
            comment: String? = null
    ): Int? = Methods.report.callSync(
        client, Int.serializer(), parametersOf {
            append("user_id", userId)
            append("type", complaintType.value)
            append("comment", comment)
        }
    )

    fun search(
            query: String? = null,
            sort: UserSearchSort,
            offset: Int,
            count: Int,
            userFields: List<UserOptionalField>,
            cityId: Int? = null,
            countryId: Int? = null,
            hometown: String? = null,
            universityCountryId: Int? = null,
            universityId: Int? = null,
            universityYear: Int? = null,
            universityFacultyId: Int? = null,
            universityChairId: Int? = null,
            sex: Sex,
            relationStatus: RelationStatus,
            ageFrom: Int? = null,
            ageTo: Int? = null,
            birthDay: Int? = null,
            birthMonth: Int? = null,
            birthYear: Int? = null,
            onlyOnline: Boolean,
            onlyWithPhoto: Boolean,
            schoolCountryId: Int? = null,
            schoolCityId: Int? = null,
            schoolClassId: Int? = null,
            schoolId: Int? = null,
            schoolYear: Int? = null,
            religion: String? = null,
            interests: String? = null,
            companyName: String? = null,
            positionName: String? = null,
            groupId: Int? = null,
            fromList: List<UsersListType>? = null
    ): ListResponse<User>? = Methods.search.callSync(
        client, ListResponse.serializer(User.serializer()), parametersOf {
            append("q", query)
            append("sort", sort.value)
            append("offset", offset)
            append("count", count)
            append("fields", userFields.joinToString(",") { it.value })
            append("city", cityId)
            append("country", countryId)
            append("hometown", hometown)
            append("university_country", universityCountryId)
            append("university", universityId)
            append("university_year", universityYear)
            append("university_faculty", universityFacultyId)
            append("university_chair", universityChairId)
            append("sex", sex.value)
            append("status", relationStatus.value)
            append("age_from", ageFrom)
            append("age_to", ageTo)
            append("birth_day", birthDay)
            append("birth_month", birthMonth)
            append("birth_year", birthYear)
            append("online", onlyOnline.toInt())
            append("has_photo", onlyWithPhoto.toInt())
            append("school_country", schoolCountryId)
            append("school_city", schoolCityId)
            append("school_class", schoolClassId)
            append("school", schoolId)
            append("school_year", schoolYear)
            append("religion", religion)
            append("interests", interests)
            append("company", companyName)
            append("position", positionName)
            append("group_id", groupId)
            append("from_list", fromList?.joinToString(",") { it.value })
        }
    )

    companion object {
        object Methods {
            private const val it = "users."
            const val get = it + "get"
            const val getFollowers = it + "getFollowers"
            const val getNearby = it + "getNearby"
            const val getSubscriptions = it + "getSubscriptions"
            const val isAppUser = it + "isAppUser"
            const val report = it + "report"
            const val search = it + "search"
        }
    }
}