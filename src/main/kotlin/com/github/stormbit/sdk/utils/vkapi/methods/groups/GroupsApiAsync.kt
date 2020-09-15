package com.github.stormbit.sdk.utils.vkapi.methods.groups

import com.github.stormbit.sdk.callbacks.Callback
import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.utils.Utils.Companion.asInt
import com.github.stormbit.sdk.utils.Utils.Companion.call
import com.github.stormbit.sdk.utils.Utils.Companion.unixtime
import com.github.stormbit.sdk.utils.put
import com.github.stormbit.sdk.utils.serialize
import com.github.stormbit.sdk.utils.vkapi.methods.*
import io.ktor.util.date.*
import com.google.gson.JsonObject

@Suppress("unused")
class GroupsApiAsync(private val client: Client) {
    fun addAddress(
            groupId: Int,
            title: String,
            address: String,
            countryId: Int,
            cityId: Int,
            latitude: Double,
            longitude: Double,
            additionalAddress: String?,
            metroId: Int?,
            phone: String?,
            workInfoStatus: Address.WorkInfoStatus?,
            timetable: Address.Timetable?,
            isMainAddress: Boolean?,
            callback: Callback<JsonObject?>
    ) = Methods.addAddress.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("title", title)
            .put("address", address)
            .put("country_id", countryId)
            .put("city_id", cityId)
            .put("latitude", latitude)
            .put("longitude", longitude)
            .put("additional_address", additionalAddress)
            .put("metro_id", metroId)
            .put("phone", phone)
            .put("work_info_status", workInfoStatus?.value)
            .put("timetable", timetable?.serialize())
            .put("is_main_address", isMainAddress?.asInt())
    )

    fun addCallbackServer(
            groupId: Int,
            url: String,
            title: String,
            secretKey: String?,
            callback: Callback<JsonObject?>
    ) = Methods.addCallbackServer.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("url", url)
            .put("title", title)
            .put("secret_key", secretKey)
    )

    fun addLink(
            groupId: Int,
            link: String,
            text: String?,
            callback: Callback<JsonObject?>
    ) = Methods.addLink.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("link", link)
            .put("text", text)
    )

    fun approveRequest(
            groupId: Int,
            userId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.approveRequest.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("user_id", userId)
    )

    fun ban(
            groupId: Int,
            ownerId: Int,
            endDate: GMTDate?,
            reason: Community.CommunityBan.Reason,
            comment: String?,
            commentVisible: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.ban.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("owner_id", ownerId)
            .put("end_date", endDate?.unixtime)
            .put("reason", reason.value)
            .put("comment", comment)
            .put("comment_visible", commentVisible.asInt())
    )

    private fun create(
            title: String,
            type: GroupType,
            description: String? = null,
            publicCategory: Int? = null,
            subtype: PublicSubtype? = null,
            callback: Callback<JsonObject?>
    ) = Methods.create.call(client, callback, JsonObject()
            .put("title", title)
            .put("description", description)
            .put("type", type.value)
            .put("public_category", publicCategory)
            .put("subtype", subtype?.value)
    )

    fun createEvent(
            title: String,
            description: String,
            callback: Callback<JsonObject?>
    ) = create(
            title = title,
            type = GroupType.EVENT,
            description = description,
            callback = callback
    )

    fun createGroup(
            title: String,
            description: String,
            callback: Callback<JsonObject?>
    ) = create(
            title = title,
            type = GroupType.GROUP,
            description = description,
            callback = callback
    )

    fun createPublic(
            title: String,
            subtype: PublicSubtype,
            publicCategory: Int?,
            callback: Callback<JsonObject?>
    ) = create(
            title = title,
            type = GroupType.PUBLIC,
            publicCategory = publicCategory,
            subtype = subtype,
            callback = callback
    )

    fun deleteAddress(
            groupId: Int,
            addressId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.deleteAddress.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("address_id", addressId)
    )

    fun deleteCallbackServer(
            groupId: Int,
            serverId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.deleteCallbackServer.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("server_id", serverId)
    )

    fun deleteLink(
            groupId: Int,
            linkId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.deleteLink.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("link_id", linkId)
    )

    fun disableOnline(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.disableOnline.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )

    private fun edit(
            groupId: Int,
            title: String? = null,
            description: String? = null,
            screenName: String? = null,
            access: Community.CloseType? = null,
            countryId: Int? = null,
            cityId: Int? = null,
            website: String? = null,
            subject: GroupSubject? = null,
            email: String? = null,
            phone: String? = null,
            rss: String? = null,
            eventStartDate: GMTDate? = null,
            eventFinishDate: GMTDate? = null,
            eventGroupId: Int? = null,
            publicCategory: Int? = null,
            publicSubcategory: Int? = null,
            publicDate: String? = null,
            wall: Int? = null,
            topics: Int? = null,
            photos: Int? = null,
            video: Int? = null,
            audio: Int? = null,
            docs: Int? = null,
            wiki: Int? = null,
            links: Boolean? = null,
            events: Boolean? = null,
            places: Boolean? = null,
            contacts: Boolean? = null,
            messages: Boolean? = null,
            articles: Boolean? = null,
            addresses: Boolean? = null,
            market: Boolean? = null,
            marketComments: Boolean? = null,
            marketCountries: List<Int>? = null,
            marketCities: List<Int>? = null,
            ageLimits: Community.AgeLimits? = null,
            marketCurrency: MarketCurrency? = null,
            marketContact: Int? = null,
            marketWiki: Int? = null,
            obsceneFilter: Boolean? = null,
            obsceneStopwords: Boolean? = null,
            obsceneWords: List<String>? = null,
            mainSection: Community.MainSectionType? = null,
            secondarySection: Community.MainSectionType? = null,
            callback: Callback<JsonObject?>
    ) = Methods.edit.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("title", title)
            .put("description", description)
            .put("screen_name", screenName)
            .put("access", access?.value)
            .put("country", countryId)
            .put("city", cityId)
            .put("website", website)
            .put("subject", subject?.value)
            .put("email", email)
            .put("phone", phone)
            .put("rss", rss)
            .put("event_start_date", eventStartDate?.unixtime)
            .put("event_finish_date", eventFinishDate?.unixtime)
            .put("event_group_id", eventGroupId)
            .put("public_category", publicCategory)
            .put("public_subcategory", publicSubcategory)
            .put("public_date", publicDate)
            .put("wall", wall)
            .put("topics", topics)
            .put("photos", photos)
            .put("video", video)
            .put("audio", audio)
            .put("docs", docs)
            .put("wiki", wiki)
            .put("links", links?.asInt())
            .put("events", events?.asInt())
            .put("places", places?.asInt())
            .put("contacts", contacts?.asInt())
            .put("messages", messages?.asInt())
            .put("articles", articles?.asInt())
            .put("addresses", addresses?.asInt())
            .put("age_limits", ageLimits?.value)
            .put("market", market?.asInt())
            .put("market_comments", marketComments?.asInt())
            .put("market_country", marketCountries?.joinToString(","))
            .put("market_city", marketCities?.joinToString(","))
            .put("market_currency", marketCurrency?.value)
            .put("market_contact", marketContact)
            .put("market_wiki", marketWiki)
            .put("obscene_filter", obsceneFilter?.asInt())
            .put("obscene_stopwords", obsceneStopwords?.asInt())
            .put("obscene_words", obsceneWords?.joinToString(","))
            .put("main_section", mainSection?.value)
            .put("secondary_section", secondarySection?.value)
    )

    fun editAddress(
            groupId: Int,
            addressId: Int,
            title: String?,
            address: String?,
            additionalAddress: String?,
            countryId: Int?,
            cityId: Int?,
            metroId: Int?,
            latitude: Double?,
            longitude: Double?,
            phone: String?,
            workInfoStatus: Address.WorkInfoStatus?,
            timetable: Address.Timetable?,
            isMainAddress: Boolean?,
            callback: Callback<JsonObject?>
    ) = Methods.editAddress.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("address_id", addressId)
            .put("title", title)
            .put("address", address)
            .put("additional_address", additionalAddress)
            .put("country_id", countryId)
            .put("city_id", cityId)
            .put("metro_id", metroId)
            .put("latitude", latitude)
            .put("longitude", longitude)
            .put("phone", phone)
            .put("work_info_status", workInfoStatus?.value)
            .put("timetable", timetable?.serialize())
            .put("is_main_address", isMainAddress?.asInt())
    )

    fun editPublic(
            groupId: Int,
            title: String?,
            description: String?,
            screenName: String?,
            access: Community.CloseType?,
            countryId: Int?,
            cityId: Int?,
            website: String?,
            subject: GroupSubject?,
            rss: String?,
            publicCategory: Int?,
            publicSubcategory: Int?,
            publicDate: String?,
            wall: PublicUnitAccessType?,
            topics: PublicUnitAccessType?,
            photos: PublicUnitAccessType?,
            video: PublicUnitAccessType?,
            audio: PublicUnitAccessType?,
            docs: PublicUnitAccessType?,
            wiki: PublicUnitAccessType?,
            links: Boolean?,
            events: Boolean?,
            places: Boolean?,
            contacts: Boolean?,
            messages: Boolean?,
            articles: Boolean?,
            addresses: Boolean?,
            market: Boolean?,
            marketComments: Boolean?,
            marketCountries: List<Int>?,
            marketCities: List<Int>?,
            ageLimits: Community.AgeLimits?,
            marketCurrency: MarketCurrency?,
            marketContact: Int?,
            marketWiki: Int?,
            obsceneFilter: Boolean?,
            obsceneStopwords: Boolean?,
            obsceneWords: List<String>?,
            mainSection: Community.MainSectionType?,
            secondarySection: Community.MainSectionType?,
            callback: Callback<JsonObject?>
    ) = edit(
            groupId = groupId,
            title = title,
            description = description,
            screenName = screenName,
            access = access,
            countryId = countryId,
            cityId = cityId,
            website = website,
            subject = subject,
            rss = rss,
            publicCategory = publicCategory,
            publicSubcategory = publicSubcategory,
            publicDate = publicDate,
            wall = wall?.value,
            topics = topics?.value,
            photos = photos?.value,
            video = video?.value,
            audio = audio?.value,
            docs = docs?.value,
            wiki = wiki?.value,
            links = links,
            events = events,
            places = places,
            contacts = contacts,
            messages = messages,
            articles = articles,
            addresses = addresses,
            market = market,
            marketComments = marketComments,
            marketCountries = marketCountries,
            marketCities = marketCities,
            ageLimits = ageLimits,
            marketCurrency = marketCurrency,
            marketContact = marketContact,
            marketWiki = marketWiki,
            obsceneFilter = obsceneFilter,
            obsceneStopwords = obsceneStopwords,
            obsceneWords = obsceneWords,
            mainSection = mainSection,
            secondarySection = secondarySection,
            callback = callback
    )

    fun editEvent(
            groupId: Int,
            title: String?,
            description: String?,
            screenName: String?,
            access: Community.CloseType?,
            countryId: Int?,
            cityId: Int?,
            website: String?,
            subject: GroupSubject?,
            email: String?,
            phone: String?,
            rss: String?,
            eventStartDate: GMTDate?,
            eventFinishDate: GMTDate?,
            eventGroupId: Int?,
            wall: GroupUnitAccessTypeExtended?,
            topics: GroupUnitAccessType?,
            photos: GroupUnitAccessType?,
            video: GroupUnitAccessType?,
            audio: GroupUnitAccessType?,
            docs: GroupUnitAccessType?,
            wiki: GroupUnitAccessType?,
            messages: Boolean?,
            articles: Boolean?,
            addresses: Boolean?,
            market: Boolean?,
            marketComments: Boolean?,
            marketCountries: List<Int>?,
            marketCities: List<Int>?,
            ageLimits: Community.AgeLimits?,
            marketCurrency: MarketCurrency?,
            marketContact: Int?,
            marketWiki: Int?,
            obsceneFilter: Boolean?,
            obsceneStopwords: Boolean?,
            obsceneWords: List<String>?,
            mainSection: Community.MainSectionType?,
            secondarySection: Community.MainSectionType?,
            callback: Callback<JsonObject?>
    ) = edit(
            groupId = groupId,
            title = title,
            description = description,
            screenName = screenName,
            access = access,
            countryId = countryId,
            cityId = cityId,
            website = website,
            subject = subject,
            email = email,
            phone = phone,
            rss = rss,
            eventStartDate = eventStartDate,
            eventFinishDate = eventFinishDate,
            eventGroupId = eventGroupId,
            wall = wall?.value,
            topics = topics?.value,
            photos = photos?.value,
            video = video?.value,
            audio = audio?.value,
            docs = docs?.value,
            wiki = wiki?.value,
            messages = messages,
            articles = articles,
            addresses = addresses,
            market = market,
            marketComments = marketComments,
            marketCountries = marketCountries,
            marketCities = marketCities,
            ageLimits = ageLimits,
            marketCurrency = marketCurrency,
            marketContact = marketContact,
            marketWiki = marketWiki,
            obsceneFilter = obsceneFilter,
            obsceneStopwords = obsceneStopwords,
            obsceneWords = obsceneWords,
            mainSection = mainSection,
            secondarySection = secondarySection,
            callback = callback
    )

    fun editGroup(
            groupId: Int,
            title: String?,
            description: String?,
            screenName: String?,
            access: Community.CloseType?,
            countryId: Int?,
            cityId: Int?,
            website: String?,
            subject: GroupSubject?,
            rss: String?,
            wall: GroupUnitAccessTypeExtended?,
            topics: GroupUnitAccessType?,
            photos: GroupUnitAccessType?,
            video: GroupUnitAccessType?,
            audio: GroupUnitAccessType?,
            docs: GroupUnitAccessType?,
            wiki: GroupUnitAccessType?,
            messages: Boolean?,
            articles: Boolean?,
            addresses: Boolean?,
            market: Boolean?,
            marketComments: Boolean?,
            marketCountries: List<Int>?,
            marketCities: List<Int>?,
            ageLimits: Community.AgeLimits?,
            marketCurrency: MarketCurrency?,
            marketContact: Int?,
            marketWiki: Int?,
            obsceneFilter: Boolean?,
            obsceneStopwords: Boolean?,
            obsceneWords: List<String>?,
            mainSection: Community.MainSectionType?,
            secondarySection: Community.MainSectionType?,
            callback: Callback<JsonObject?>
    ) = edit(
            groupId = groupId,
            title = title,
            description = description,
            screenName = screenName,
            access = access,
            countryId = countryId,
            cityId = cityId,
            website = website,
            subject = subject,
            rss = rss,
            wall = wall?.value,
            topics = topics?.value,
            photos = photos?.value,
            video = video?.value,
            audio = audio?.value,
            docs = docs?.value,
            wiki = wiki?.value,
            messages = messages,
            articles = articles,
            addresses = addresses,
            market = market,
            marketComments = marketComments,
            marketCountries = marketCountries,
            marketCities = marketCities,
            ageLimits = ageLimits,
            marketCurrency = marketCurrency,
            marketContact = marketContact,
            marketWiki = marketWiki,
            obsceneFilter = obsceneFilter,
            obsceneStopwords = obsceneStopwords,
            obsceneWords = obsceneWords,
            mainSection = mainSection,
            secondarySection = secondarySection,
            callback = callback
    )

    fun editCallbackServer(
            groupId: Int,
            serverId: Int,
            url: String,
            title: String,
            secretKey: String?,
            callback: Callback<JsonObject?>
    ) = Methods.editCallbackServer.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("server_id", serverId)
            .put("url", url)
            .put("title", title)
            .put("secret_key", secretKey)
    )

    fun editLink(
            groupId: Int,
            linkId: Int,
            text: String?,
            callback: Callback<JsonObject?>
    ) = Methods.editLink.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("link_id", linkId)
            .put("text", text)
    )

    fun editManager(
            groupId: Int,
            userId: Int,
            role: CommunityManagerRole?,
            isContact: Boolean?,
            contactPosition: String?,
            contactPhone: String?,
            contactEmail: String?,
            callback: Callback<JsonObject?>
    ) = Methods.editManager.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("user_id", userId)
            .put("role", role?.value)
            .put("is_contact", isContact?.asInt())
            .put("contact_position", contactPosition)
            .put("contact_phone", contactPhone)
            .put("contact_email", contactEmail)
    )

    fun editPlace(
            groupId: Int,
            title: String?,
            address: String?,
            countryId: Int?,
            cityId: Int?,
            latitude: Double?,
            longitude: Double?,
            callback: Callback<JsonObject?>
    ) = Methods.editPlace.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("title", title)
            .put("address", address)
            .put("country_id", countryId)
            .put("city_id", cityId)
            .put("latitude", latitude)
            .put("longitude", longitude)
    )

    fun enableOnline(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.enableOnline.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )

    fun getIds(
            userId: Int?,
            filter: GroupsFilter?,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.get.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("filter", filter?.value)
            .put("offset", offset)
            .put("count", count)
    )

    fun get(
            userId: Int?,
            filter: GroupsFilter?,
            fields: List<CommunityOptionalField>?,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.get.call(client, callback, JsonObject()
            .put("user_id", userId)
            .put("extended", 1)
            .put("filter", filter?.value)
            .put("fields", fields?.joinToString(",") { it.value })
            .put("offset", offset)
            .put("count", count)
    )

    fun getAddresses(
            groupId: Int,
            addressIds: List<Int>?,
            latitude: Double?,
            longitude: Double?,
            offset: Int,
            count: Int,
            fields: List<AddressOptionalFields>?,
            callback: Callback<JsonObject?>

    ) = Methods.getAddresses.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("address_ids", addressIds?.joinToString(","))
            .put("latitude", latitude)
            .put("longitude", longitude)
            .put("offset", offset)
            .put("count", count)
            .put("fields", fields?.joinToString(",") { it.value })
    )

    fun getBanned(
            groupId: Int,
            offset: Int,
            count: Int,
            fields: List<ObjectField>,
            ownerId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getBanned.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("offset", offset)
            .put("count", count)
            .put("fields", fields.joinToString(",") { it.value })
            .put("owner_id", ownerId)
    )

    fun getByScreenName(
            groupNames: List<String>,
            communityFields: List<CommunityOptionalField>?,
            callback: Callback<JsonObject?>
    ) = Methods.getById.call(client, callback, JsonObject()
            .put("group_ids", groupNames.joinToString(","))
            .put("fields", communityFields?.joinToString(",") { it.value })
    )

    fun getById(
            groupIds: List<Int>,
            communityFields: List<CommunityOptionalField>?,
            callback: Callback<JsonObject?>
    ) = getByScreenName(
            groupNames = groupIds.map(Int::toString),
            communityFields = communityFields,
            callback
    )

    fun getCallbackConfirmationCode(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getCallbackConfirmationCode.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )

    fun getCallbackServers(
            groupId: Int,
            serverIds: List<Int>?,
            callback: Callback<JsonObject?>
    ) = Methods.getCallbackServers.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("server_ids", serverIds?.joinToString(","))
    )

    fun getCallbackSettings(
            groupId: Int,
            serverId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getCallbackSettings.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("server_id", serverId)
    )

    fun getCatalog(
            categoryId: Int?,
            subcategoryId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.getCatalog.call(client, callback, JsonObject()
            .put("category_id", categoryId)
            .put("subcategory_id", subcategoryId)
    )

    fun getCatalogInfo(
            extended: Boolean,
            withSubcategories: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getCatalogInfo.call(client, callback, JsonObject()
            .put("extended", extended.asInt())
            .put("subcategories", withSubcategories.asInt())
    )

    fun getInvitedUsers(
            groupId: Int,
            offset: Int,
            count: Int,
            userFields: List<UserOptionalField>,
            nameCase: NameCase,
            callback: Callback<JsonObject?>
    ) = Methods.getInvitedUsers.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("offset", offset)
            .put("count", count)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("name_case", nameCase.value)
    )

    fun getInvites(
            offset: Int,
            count: Int,
            extended: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getInvites.call(client, callback, JsonObject()
            .put("offset", offset)
            .put("count", count)
            .put("extended", extended.asInt())
    )

    fun getLongPollServer(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getLongPollServer.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )

    fun getLongPollSettings(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getLongPollSettings.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )

    fun getMembersIds(
            groupId: Int,
            sort: CommunityMembersSort,
            offset: Int,
            count: Int,
            onlyFriends: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getMembers.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("sort", sort.value)
            .put("offset", offset)
            .put("count", count)
            .put("filter", if (onlyFriends) "friends" else null)
    )

    fun getMembers(
            groupId: Int,
            sort: CommunityMembersSort,
            offset: Int,
            count: Int,
            userFields: List<UserOptionalField>,
            onlyFriends: Boolean,
            callback: Callback<JsonObject?>
    ) = Methods.getMembers.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("sort", sort.value)
            .put("offset", offset)
            .put("count", count)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("filter", if (onlyFriends) "friends" else null)
    )

    fun getManagersIds(
            groupId: Int,
            sort: CommunityMembersSort,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getMembers.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("sort", sort.value)
            .put("offset", offset)
            .put("count", count)
            .put("filter", "managers")
    )

    fun getManagers(
            groupId: Int,
            sort: CommunityMembersSort,
            offset: Int,
            count: Int,
            userFields: List<UserOptionalField>,
            callback: Callback<JsonObject?>
    ) = Methods.getMembers.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("sort", sort.value)
            .put("offset", offset)
            .put("count", count)
            .put("fields", userFields.joinToString(",") { it.value })
            .put("filter", "managers")
    )

    fun getOnlineStatus(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getOnlineStatus.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )

    fun getRequestsIds(
            groupId: Int,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getRequests.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("offset", offset)
            .put("count", count)
    )

    fun getRequests(
            groupId: Int,
            offset: Int,
            count: Int,
            userFields: List<UserOptionalField>,
            callback: Callback<JsonObject?>
    ) = Methods.getRequests.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("offset", offset)
            .put("count", count)
            .put("fields", userFields.joinToString(",") { it.value })
    )

    fun getSettings(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.getSettings.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )

    fun getTokenPermissions(callback: Callback<JsonObject?>) = Methods.getTokenPermissions.call(client, callback, null)

    fun invite(
            groupId: Int,
            userId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.invite.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("user_id", userId)
    )

    fun isMember(
            groupId: Int,
            userId: Int?,
            callback: Callback<JsonObject?>
    ) = Methods.isMember.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("user_id", userId)
            .put("extended", 1)
    )

    fun isMembers(
            groupId: Int,
            userIds: List<Int>,
            callback: Callback<JsonObject?>
    ) = Methods.isMember.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("user_ids", userIds.joinToString(","))
    )

    fun join(
            groupId: Int,
            notSure: Boolean?,
            callback: Callback<JsonObject?>
    ) = Methods.join.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("not_sure", notSure?.asInt())
    )

    fun leave(
            groupId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.leave.call(client, callback, JsonObject()
            .put("group_id", groupId)
    )

    fun removeUser(
            groupId: Int,
            userId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.removeUser.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("user_id", userId)
    )

    fun reorderLink(
            groupId: Int,
            linkId: Int,
            after: Int,
            callback: Callback<JsonObject?>
    ) = Methods.reorderLink.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("link_id", linkId)
            .put("after", after)
    )

    fun search(
            query: String,
            type: Community.Type?,
            countryId: Int?,
            cityId: Int?,
            isFuture: Boolean?,
            hasMarket: Boolean?,
            sort: CommunitySearchOrder,
            offset: Int,
            count: Int,
            callback: Callback<JsonObject?>
    ) = Methods.search.call(client, callback, JsonObject()
            .put("q", query)
            .put("type", type?.value)
            .put("country_id", countryId)
            .put("city_id", cityId)
            .put("future", isFuture?.asInt())
            .put("market", hasMarket?.asInt())
            .put("sort", sort.value)
            .put("offset", offset)
            .put("count", count)
    )

    fun setCallbackSettings(
            groupId: Int,
            serverId: Int,
            apiVersion: String?,
            messageReply: Boolean?,
            messagesEdit: Boolean?,
            messageAllow: Boolean?,
            messageDeny: Boolean?,
            messageTypingState: Boolean?,
            messageRead: Boolean?,
            photoNew: Boolean?,
            photoCommentNew: Boolean?,
            photoCommentEdit: Boolean?,
            photoCommentRestore: Boolean?,
            photoCommentDelete: Boolean?,
            audioNew: Boolean?,
            videoNew: Boolean?,
            videoCommentNew: Boolean?,
            videoCommentEdit: Boolean?,
            videoCommentRestore: Boolean?,
            videoCommentDelete: Boolean?,
            wallPostNew: Boolean?,
            wallRepost: Boolean?,
            wallReplyNew: Boolean?,
            wallReplyEdit: Boolean?,
            wallReplyRestore: Boolean?,
            wallReplyDelete: Boolean?,
            boardPostNew: Boolean?,
            boardPostEdit: Boolean?,
            boardPostRestore: Boolean?,
            boardPostDelete: Boolean?,
            marketCommentNew: Boolean?,
            marketCommentEdit: Boolean?,
            marketCommentRestore: Boolean?,
            marketCommentDelete: Boolean?,
            groupLeave: Boolean?,
            groupJoin: Boolean?,
            userBlock: Boolean?,
            userUnblock: Boolean?,
            pollVoteNew: Boolean?,
            groupOfficersEdit: Boolean?,
            groupChangeSettings: Boolean?,
            groupChangePhoto: Boolean?,
            vkPayTransaction: Boolean?,
            appPayload: Boolean?,
            callback: Callback<JsonObject?>
    ) = Methods.setCallbackSettings.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("server_id", serverId)
            .put("api_version", apiVersion)
            .put("message_reply", messageReply?.asInt())
            .put("messages_edit", messagesEdit?.asInt())
            .put("message_allow", messageAllow?.asInt())
            .put("message_deny", messageDeny?.asInt())
            .put("message_typing_state", messageTypingState?.asInt())
            .put("message_read", messageRead?.asInt())
            .put("photo_new", photoNew?.asInt())
            .put("photo_comment_new", photoCommentNew?.asInt())
            .put("photo_comment_edit", photoCommentEdit?.asInt())
            .put("photo_comment_restore", photoCommentRestore?.asInt())
            .put("photo_comment_delete", photoCommentDelete?.asInt())
            .put("audio_new", audioNew?.asInt())
            .put("video_new", videoNew?.asInt())
            .put("video_comment_new", videoCommentNew?.asInt())
            .put("video_comment_edit", videoCommentEdit?.asInt())
            .put("video_comment_restore", videoCommentRestore?.asInt())
            .put("video_comment_delete", videoCommentDelete?.asInt())
            .put("wall_post_new", wallPostNew?.asInt())
            .put("wall_repost", wallRepost?.asInt())
            .put("wall_reply_new", wallReplyNew?.asInt())
            .put("wall_reply_edit", wallReplyEdit?.asInt())
            .put("wall_reply_restore", wallReplyRestore?.asInt())
            .put("wall_reply_delete", wallReplyDelete?.asInt())
            .put("board_post_new", boardPostNew?.asInt())
            .put("board_post_edit", boardPostEdit?.asInt())
            .put("board_post_restore", boardPostRestore?.asInt())
            .put("board_post_delete", boardPostDelete?.asInt())
            .put("market_comment_new", marketCommentNew?.asInt())
            .put("market_comment_edit", marketCommentEdit?.asInt())
            .put("market_comment_restore", marketCommentRestore?.asInt())
            .put("market_comment_delete", marketCommentDelete?.asInt())
            .put("group_leave", groupLeave?.asInt())
            .put("group_join", groupJoin?.asInt())
            .put("user_block", userBlock?.asInt())
            .put("user_unblock", userUnblock?.asInt())
            .put("poll_vote_new", pollVoteNew?.asInt())
            .put("group_officers_edit", groupOfficersEdit?.asInt())
            .put("group_change_settings", groupChangeSettings?.asInt())
            .put("group_change_photo", groupChangePhoto?.asInt())
            .put("vkpay_transaction", vkPayTransaction?.asInt())
            .put("app_payload", appPayload?.asInt())
    )

    fun setLongPollSettings(
            groupId: Int,
            apiVersion: String,
            enabled: Boolean?,
            messageReply: Boolean?,
            messagesEdit: Boolean?,
            messageAllow: Boolean?,
            messageDeny: Boolean?,
            messageTypingState: Boolean?,
            messageRead: Boolean?,
            photoNew: Boolean?,
            photoCommentNew: Boolean?,
            photoCommentEdit: Boolean?,
            photoCommentRestore: Boolean?,
            photoCommentDelete: Boolean?,
            audioNew: Boolean?,
            videoNew: Boolean?,
            videoCommentNew: Boolean?,
            videoCommentEdit: Boolean?,
            videoCommentRestore: Boolean?,
            videoCommentDelete: Boolean?,
            wallPostNew: Boolean?,
            wallRepost: Boolean?,
            wallReplyNew: Boolean?,
            wallReplyEdit: Boolean?,
            wallReplyRestore: Boolean?,
            wallReplyDelete: Boolean?,
            boardPostNew: Boolean?,
            boardPostEdit: Boolean?,
            boardPostRestore: Boolean?,
            boardPostDelete: Boolean?,
            marketCommentNew: Boolean?,
            marketCommentEdit: Boolean?,
            marketCommentRestore: Boolean?,
            marketCommentDelete: Boolean?,
            groupLeave: Boolean?,
            groupJoin: Boolean?,
            userBlock: Boolean?,
            userUnblock: Boolean?,
            pollVoteNew: Boolean?,
            groupOfficersEdit: Boolean?,
            groupChangeSettings: Boolean?,
            groupChangePhoto: Boolean?,
            vkPayTransaction: Boolean?,
            appPayload: Boolean?,
            callback: Callback<JsonObject?>
    ) = Methods.setLongPollSettings.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("api_version", apiVersion)
            .put("enabled", enabled?.asInt())
            .put("message_reply", messageReply?.asInt())
            .put("messages_edit", messagesEdit?.asInt())
            .put("message_allow", messageAllow?.asInt())
            .put("message_deny", messageDeny?.asInt())
            .put("message_typing_state", messageTypingState?.asInt())
            .put("message_read", messageRead?.asInt())
            .put("photo_new", photoNew?.asInt())
            .put("photo_comment_new", photoCommentNew?.asInt())
            .put("photo_comment_edit", photoCommentEdit?.asInt())
            .put("photo_comment_restore", photoCommentRestore?.asInt())
            .put("photo_comment_delete", photoCommentDelete?.asInt())
            .put("audio_new", audioNew?.asInt())
            .put("video_new", videoNew?.asInt())
            .put("video_comment_new", videoCommentNew?.asInt())
            .put("video_comment_edit", videoCommentEdit?.asInt())
            .put("video_comment_restore", videoCommentRestore?.asInt())
            .put("video_comment_delete", videoCommentDelete?.asInt())
            .put("wall_post_new", wallPostNew?.asInt())
            .put("wall_repost", wallRepost?.asInt())
            .put("wall_reply_new", wallReplyNew?.asInt())
            .put("wall_reply_edit", wallReplyEdit?.asInt())
            .put("wall_reply_restore", wallReplyRestore?.asInt())
            .put("wall_reply_delete", wallReplyDelete?.asInt())
            .put("board_post_new", boardPostNew?.asInt())
            .put("board_post_edit", boardPostEdit?.asInt())
            .put("board_post_restore", boardPostRestore?.asInt())
            .put("board_post_delete", boardPostDelete?.asInt())
            .put("market_comment_new", marketCommentNew?.asInt())
            .put("market_comment_edit", marketCommentEdit?.asInt())
            .put("market_comment_restore", marketCommentRestore?.asInt())
            .put("market_comment_delete", marketCommentDelete?.asInt())
            .put("group_leave", groupLeave?.asInt())
            .put("group_join", groupJoin?.asInt())
            .put("user_block", userBlock?.asInt())
            .put("user_unblock", userUnblock?.asInt())
            .put("poll_vote_new", pollVoteNew?.asInt())
            .put("group_officers_edit", groupOfficersEdit?.asInt())
            .put("group_change_settings", groupChangeSettings?.asInt())
            .put("group_change_photo", groupChangePhoto?.asInt())
            .put("vkpay_transaction", vkPayTransaction?.asInt())
            .put("app_payload", appPayload?.asInt())
    )

    fun setSettings(
            groupId: Int,
            messages: Boolean?,
            botsCapabilities: Boolean?,
            botsStartButton: Boolean?,
            botsAddToChat: Boolean?,
            callback: Callback<JsonObject?>
    ) = Methods.setSettings.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("messages", messages?.asInt())
            .put("bots_capabilities", botsCapabilities?.asInt())
            .put("bots_start_button", botsStartButton?.asInt())
            .put("bots_add_to_chat", botsAddToChat?.asInt())
    )

    fun unban(
            groupId: Int,
            ownerId: Int,
            callback: Callback<JsonObject?>
    ) = Methods.unban.call(client, callback, JsonObject()
            .put("group_id", groupId)
            .put("owner_id", ownerId)
    )

    companion object {
        object Methods {
            private const val it = "groups."
            const val addAddress = it + "addAddress"
            const val addCallbackServer = it + "addCallbackServer"
            const val addLink = it + "addLink"
            const val approveRequest = it + "approveRequest"
            const val ban = it + "ban"
            const val create = it + "create"
            const val deleteAddress = it + "deleteAddress"
            const val deleteCallbackServer = it + "deleteCallbackServer"
            const val deleteLink = it + "deleteLink"
            const val disableOnline = it + "disableOnline"
            const val edit = it + "edit"
            const val editAddress = it + "editAddress"
            const val editCallbackServer = it + "editCallbackServer"
            const val editLink = it + "editLink"
            const val editManager = it + "editManager"
            const val editPlace = it + "editPlace"
            const val enableOnline = it + "enableOnline"
            const val get = it + "get"
            const val getAddresses = it + "getAddresses"
            const val getBanned = it + "getBanned"
            const val getById = it + "getById"
            const val getCallbackConfirmationCode = it + "getCallbackConfirmationCode"
            const val getCallbackServers = it + "getCallbackServers"
            const val getCallbackSettings = it + "getCallbackSettings"
            const val getCatalog = it + "getCatalog"
            const val getCatalogInfo = it + "getCatalogInfo"
            const val getInvitedUsers = it + "getInvitedUsers"
            const val getInvites = it + "getInvites"
            const val getLongPollServer = it + "getLongPollServer"
            const val getLongPollSettings = it + "getLongPollSettings"
            const val getMembers = it + "getMembers"
            const val getOnlineStatus = it + "getOnlineStatus"
            const val getRequests = it + "getRequests"
            const val getSettings = it + "getSettings"
            const val getTokenPermissions = it + "getTokenPermissions"
            const val invite = it + "invite"
            const val isMember = it + "isMember"
            const val join = it + "join"
            const val leave = it + "leave"
            const val removeUser = it + "removeUser"
            const val reorderLink = it + "reorderLink"
            const val search = it + "search"
            const val setCallbackSettings = it + "setCallbackSettings"
            const val setLongPollSettings = it + "setLongPollSettings"
            const val setSettings = it + "setSettings"
            const val unban = it + "unban"
        }
    }
}