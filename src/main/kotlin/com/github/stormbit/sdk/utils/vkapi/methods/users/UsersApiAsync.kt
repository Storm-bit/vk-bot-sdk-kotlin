package com.github.stormbit.sdk.utils.vkapi.methods.users

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.call
import com.github.stormbit.sdk.utils.getJsonObject
import com.github.stormbit.sdk.utils.getString
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.vkapi.methods.*
import com.google.gson.JsonObject

@Suppress("unused")
class UsersApiAsync(private val client: Client) {
    fun get(
            userNames: List<String>? = null,
            userFields: List<UserOptionalField>? = null,
            nameCase: NameCase? = null,
            callback: Callback<JsonObject?>
    ) = Methods.get.call(client, callback, JsonObject()
            .put("user_ids", userNames?.joinToString(","))
            .put("fields", userFields?.joinToString(",") { it.value })
            .put("name_case", nameCase?.value)
    )

    fun getById(
            userIds: List<Int>,
            userFields: List<UserOptionalField>? = null,
            nameCase: NameCase = NameCase.NOM,
            callback: Callback<JsonObject?>
    ) = get(
            userNames = userIds.map(Int::toString),
            userFields = userFields,
            nameCase = nameCase,
            callback
    )

    fun getName(
            userId: Int,
            nameCase: NameCase = NameCase.NOM,
            callback: Callback<String>
    ) {
        getById(listOf(userId), null, nameCase) {

            val response = it!!.getAsJsonArray("response").getJsonObject(0)

            callback.onResult("${response.getString("first_name")} ${response.getString("last_name")}")
        }
    }

    fun getFollowers(
            userId: Int? = null,
            offset: Int,
            count: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase,
            callback: Callback<JsonObject?>
    ) = Methods.getFollowers.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("offset", offset)
            .put("count", count)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("name_case", nameCase.value)
    )

    fun getNearby(
            latitude: Double,
            longitude: Double,
            accuracy: Int? = null,
            timeout: Int,
            radius: NearbyRadius,
            userFields: List<UserOptionalField>,
            nameCase: NameCase,
            needDescription: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getNearby.call(client, callback, JsonObject()
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
            userId: Int? = null,
            offset: Int,
            count: Int,
            fields: List<ObjectField>,
            callback: Callback<JsonObject?>
    ) = Methods.getSubscriptions.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("extended", 1)
            .put("offset", offset)
            .put("count", count)
            .put("fields", fields.joinToString(",") { it.value })
    )

    fun getSubscriptionsIds(
            userId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.getSubscriptions.call(client, callback, JsonObject()
            .put("user_id", userId)
    )

    fun isAppUser(
            userId: Int? = null,
            callback: Callback<JsonObject?>
    ) = Methods.isAppUser.call(client, callback, JsonObject()
            .put("user_id", userId)
    )

    fun report(
            userId: Int,
            complaintType: UserReportComplaintType,
            comment: String? = null,
            callback: Callback<JsonObject?>
    ) = Methods.report.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("type", complaintType.value)
            .put("comment", comment)
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
            fromList: List<UsersListType>? = null,
            callback: Callback<JsonObject?>
    ) = Methods.search.call(client, callback, JsonObject()
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