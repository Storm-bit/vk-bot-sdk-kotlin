package com.github.stormbit.sdk.utils.vkapi.methods.users

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.callSync
import com.github.stormbit.sdk.utils.vkapi.methods.*
import org.json.JSONObject

@Suppress("unused")
class UsersApi(private val client: Client) {
    fun get(
            userNames: List<String>? = null,
            userFields: List<UserOptionalField>? = null,
            nameCase: NameCase? = null
    ): JSONObject = Methods.get.callSync(client, JSONObject()
            .put("user_ids", userNames?.joinToString(","))
            .put("fields", userFields?.joinToString(",") { it.value })
            .put("name_case", nameCase?.value)
    )

    fun getById(
            userIds: List<Int>,
            userFields: List<UserOptionalField>? = null,
            nameCase: NameCase = NameCase.NOM
    ): JSONObject = get(
            userNames = userIds.map(Int::toString),
            userFields = userFields,
            nameCase = nameCase
    )

    fun getName(
            userId: Int,
            nameCase: NameCase = NameCase.NOM
    ): String {
        val user = getById(listOf(userId), null, nameCase)

        val response = user.getJSONArray("response").getJSONObject(0)

        return "${response.getString("first_name")} ${response.getString("last_name")}"
    }

    fun getFollowers(
            userId: Int?,
            offset: Int,
            count: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase
    ): JSONObject = Methods.getFollowers.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("offset", offset)
            .put("count", count)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("name_case", nameCase.value)
    )

    fun getNearby(
            latitude: Double,
            longitude: Double,
            accuracy: Int?,
            timeout: Int,
            radius: NearbyRadius,
            userFields: List<UserOptionalField>,
            nameCase: NameCase,
            needDescription: Boolean
    ): JSONObject = Methods.getNearby.callSync(client, JSONObject()
            .put("latitude", latitude)
            .put("longitude", longitude)
            .put("accuracy", accuracy)
            .put("timeout", timeout)
            .put("radius", radius.value)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("name_case", nameCase.value)
            .put("need_description", needDescription.asInt())
    )

    fun getSubscriptions(
            userId: Int?,
            offset: Int,
            count: Int,
            fields: List<ObjectField>
    ): JSONObject = Methods.getSubscriptions.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("extended", 1)
            .put("offset", offset)
            .put("count", count)
            .put("fields", fields.joinToString(",") { it.value })
    )

    fun getSubscriptionsIds(
            userId: Int?
    ): JSONObject = Methods.getSubscriptions.callSync(client, JSONObject()
            .put("user_id", userId)
    )

    fun isAppUser(
            userId: Int?
    ): JSONObject = Methods.isAppUser.callSync(client, JSONObject()
            .put("user_id", userId)
    )

    fun report(
            userId: Int,
            complaintType: UserReportComplaintType,
            comment: String?
    ): JSONObject = Methods.report.callSync(client, JSONObject()
            .put("user_id", userId)
            .put("type", complaintType.value)
            .put("comment", comment)
    )

    fun search(
            query: String?,
            sort: UserSearchSort,
            offset: Int,
            count: Int,
            userFields: List<UserOptionalField>,
            cityId: Int?,
            countryId: Int?,
            hometown: String?,
            universityCountryId: Int?,
            universityId: Int?,
            universityYear: Int?,
            universityFacultyId: Int?,
            universityChairId: Int?,
            sex: Sex,
            relationStatus: RelationStatus,
            ageFrom: Int?,
            ageTo: Int?,
            birthDay: Int?,
            birthMonth: Int?,
            birthYear: Int?,
            onlyOnline: Boolean,
            onlyWithPhoto: Boolean,
            schoolCountryId: Int?,
            schoolCityId: Int?,
            schoolClassId: Int?,
            schoolId: Int?,
            schoolYear: Int?,
            religion: String?,
            interests: String?,
            companyName: String?,
            positionName: String?,
            groupId: Int?,
            fromList: List<UsersListType>?
    ): JSONObject = Methods.search.callSync(client, JSONObject()
            .put("q", query)
            .put("sort", sort.value)
            .put("offset", offset)
            .put("count", count)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("city", cityId)
            .put("country", countryId)
            .put("hometown", hometown)
            .put("university_country", universityCountryId)
            .put("university", universityId)
            .put("university_year", universityYear)
            .put("university_faculty", universityFacultyId)
            .put("university_chair", universityChairId)
            .put("sex", sex.value)
            .put("status", relationStatus.value)
            .put("age_from", ageFrom)
            .put("age_to", ageTo)
            .put("birth_day", birthDay)
            .put("birth_month", birthMonth)
            .put("birth_year", birthYear)
            .put("online", onlyOnline.asInt())
            .put("has_photo", onlyWithPhoto.asInt())
            .put("school_country", schoolCountryId)
            .put("school_city", schoolCityId)
            .put("school_class", schoolClassId)
            .put("school", schoolId)
            .put("school_year", schoolYear)
            .put("religion", religion)
            .put("interests", interests)
            .put("company", companyName)
            .put("position", positionName)
            .put("group_id", groupId)
            .put("from_list", fromList?.joinToString(",") { it.value })
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