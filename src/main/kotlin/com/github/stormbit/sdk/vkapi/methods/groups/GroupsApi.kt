package com.github.stormbit.sdk.vkapi.methods.groups

import com.github.stormbit.sdk.clients.Client
import com.github.stormbit.sdk.objects.models.Address
import com.github.stormbit.sdk.objects.models.Community
import com.github.stormbit.sdk.objects.models.LongPollServerResponse
import com.github.stormbit.sdk.utils.append
import com.github.stormbit.sdk.utils.serialize
import com.github.stormbit.sdk.utils.toInt
import com.github.stormbit.sdk.utils.unixtime
import com.github.stormbit.sdk.vkapi.VkApiRequest
import com.github.stormbit.sdk.vkapi.execute
import com.github.stormbit.sdk.vkapi.methods.*
import io.ktor.util.date.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonObject

@Suppress("unused", "MemberVisibilityCanBePrivate")
class GroupsApi(client: Client) : MethodsContext(client) {
    fun addAddress(
        groupId: Int,
        title: String,
        address: String,
        countryId: Int,
        cityId: Int,
        latitude: Double,
        longitude: Double,
        additionalAddress: String? = null,
        metroId: Int? = null,
        phone: String? = null,
        workInfoStatus: Address.WorkInfoStatus? = null,
        timetable: Address.Timetable? = null,
        isMainAddress: Boolean? = null
    ): VkApiRequest<JsonObject> = Methods.addAddress.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("title", title)
        append("address", address)
        append("country_id", countryId)
        append("city_id", cityId)
        append("latitude", latitude)
        append("longitude", longitude)
        append("additional_address", additionalAddress)
        append("metro_id", metroId)
        append("phone", phone)
        append("work_info_status", workInfoStatus?.value)
        append("timetable", timetable?.serialize())
        append("is_main_address", isMainAddress?.toInt())
    }


    fun addCallbackServer(
        groupId: Int,
        url: String,
        title: String,
        secretKey: String? = null
    ): VkApiRequest<JsonObject> = Methods.addCallbackServer.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("url", url)
        append("title", title)
        append("secret_key", secretKey)
    }


    fun addLink(
        groupId: Int,
        link: String,
        text: String? = null
    ): VkApiRequest<JsonObject> = Methods.addLink.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("link", link)
        append("text", text)
    }


    fun approveRequest(
        groupId: Int,
        userId: Int
    ): VkApiRequest<JsonObject> = Methods.approveRequest.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("user_id", userId)
    }


    fun ban(
        groupId: Int,
        ownerId: Int,
        endDate: GMTDate? = null,
        reason: Community.CommunityBan.Reason,
        comment: String? = null,
        commentVisible: Boolean
    ): VkApiRequest<JsonObject> = Methods.ban.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("owner_id", ownerId)
        append("end_date", endDate?.unixtime)
        append("reason", reason.value)
        append("comment", comment)
        append("comment_visible", commentVisible.toInt())
    }


    private fun create(
        title: String,
        type: GroupType,
        description: String? = null,
        publicCategory: Int? = null,
        subtype: PublicSubtype? = null
    ): VkApiRequest<JsonObject> = Methods.create.call(JsonObject.serializer()) {
        append("title", title)
        append("description", description)
        append("type", type.value)
        append("public_category", publicCategory)
        append("subtype", subtype?.value)
    }


    fun createEvent(
        title: String,
        description: String
    ): VkApiRequest<JsonObject> = create(
        title = title,
        type = GroupType.EVENT,
        description = description
    )


    fun createGroup(
        title: String,
        description: String
    ): VkApiRequest<JsonObject> = create(
        title = title,
        type = GroupType.GROUP,
        description = description
    )


    fun createPublic(
        title: String,
        subtype: PublicSubtype,
        publicCategory: Int? = null
    ): VkApiRequest<JsonObject> = create(
        title = title,
        type = GroupType.PUBLIC,
        publicCategory = publicCategory,
        subtype = subtype
    )


    fun deleteAddress(
        groupId: Int,
        addressId: Int
    ): VkApiRequest<JsonObject> = Methods.deleteAddress.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("address_id", addressId)
    }


    fun deleteCallbackServer(
        groupId: Int,
        serverId: Int
    ): VkApiRequest<JsonObject> = Methods.deleteCallbackServer.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("server_id", serverId)
    }


    fun deleteLink(
        groupId: Int,
        linkId: Int
    ): VkApiRequest<JsonObject> = Methods.deleteLink.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("link_id", linkId)
    }


    fun disableOnline(
        groupId: Int
    ): VkApiRequest<JsonObject> = Methods.disableOnline.call(JsonObject.serializer()) {
        append("group_id", groupId)
    }


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
        secondarySection: Community.MainSectionType? = null
    ): VkApiRequest<JsonObject> = Methods.edit.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("title", title)
        append("description", description)
        append("screen_name", screenName)
        append("access", access?.value)
        append("country", countryId)
        append("city", cityId)
        append("website", website)
        append("subject", subject?.value)
        append("email", email)
        append("phone", phone)
        append("rss", rss)
        append("event_start_date", eventStartDate?.unixtime)
        append("event_finish_date", eventFinishDate?.unixtime)
        append("event_group_id", eventGroupId)
        append("public_category", publicCategory)
        append("public_subcategory", publicSubcategory)
        append("public_date", publicDate)
        append("wall", wall)
        append("topics", topics)
        append("photos", photos)
        append("video", video)
        append("audio", audio)
        append("docs", docs)
        append("wiki", wiki)
        append("links", links?.toInt())
        append("events", events?.toInt())
        append("places", places?.toInt())
        append("contacts", contacts?.toInt())
        append("messages", messages?.toInt())
        append("articles", articles?.toInt())
        append("addresses", addresses?.toInt())
        append("age_limits", ageLimits?.value)
        append("market", market?.toInt())
        append("market_comments", marketComments?.toInt())
        append("market_country", marketCountries?.joinToString(","))
        append("market_city", marketCities?.joinToString(","))
        append("market_currency", marketCurrency?.value)
        append("market_contact", marketContact)
        append("market_wiki", marketWiki)
        append("obscene_filter", obsceneFilter?.toInt())
        append("obscene_stopwords", obsceneStopwords?.toInt())
        append("obscene_words", obsceneWords?.joinToString(","))
        append("main_section", mainSection?.value)
        append("secondary_section", secondarySection?.value)
    }


    fun editAddress(
        groupId: Int,
        addressId: Int,
        title: String? = null,
        address: String? = null,
        additionalAddress: String? = null,
        countryId: Int? = null,
        cityId: Int? = null,
        metroId: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        phone: String? = null,
        workInfoStatus: Address.WorkInfoStatus? = null,
        timetable: Address.Timetable? = null,
        isMainAddress: Boolean? = null
    ): VkApiRequest<JsonObject> = Methods.editAddress.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("address_id", addressId)
        append("title", title)
        append("address", address)
        append("additional_address", additionalAddress)
        append("country_id", countryId)
        append("city_id", cityId)
        append("metro_id", metroId)
        append("latitude", latitude)
        append("longitude", longitude)
        append("phone", phone)
        append("work_info_status", workInfoStatus?.value)
        append("timetable", timetable?.serialize())
        append("is_main_address", isMainAddress?.toInt())
    }


    fun editPublic(
        groupId: Int,
        title: String? = null,
        description: String? = null,
        screenName: String? = null,
        access: Community.CloseType? = null,
        countryId: Int? = null,
        cityId: Int? = null,
        website: String? = null,
        subject: GroupSubject? = null,
        rss: String? = null,
        publicCategory: Int? = null,
        publicSubcategory: Int? = null,
        publicDate: String? = null,
        wall: PublicUnitAccessType? = null,
        topics: PublicUnitAccessType? = null,
        photos: PublicUnitAccessType? = null,
        video: PublicUnitAccessType? = null,
        audio: PublicUnitAccessType? = null,
        docs: PublicUnitAccessType? = null,
        wiki: PublicUnitAccessType? = null,
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
        secondarySection: Community.MainSectionType? = null
    ): VkApiRequest<JsonObject> = edit(
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
        secondarySection = secondarySection
    )


    fun editEvent(
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
        wall: GroupUnitAccessTypeExtended? = null,
        topics: GroupUnitAccessType? = null,
        photos: GroupUnitAccessType? = null,
        video: GroupUnitAccessType? = null,
        audio: GroupUnitAccessType? = null,
        docs: GroupUnitAccessType? = null,
        wiki: GroupUnitAccessType? = null,
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
        secondarySection: Community.MainSectionType? = null
    ): VkApiRequest<JsonObject> = edit(
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
        secondarySection = secondarySection
    )


    fun editGroup(
        groupId: Int,
        title: String? = null,
        description: String? = null,
        screenName: String? = null,
        access: Community.CloseType?,
        countryId: Int? = null,
        cityId: Int? = null,
        website: String? = null,
        subject: GroupSubject? = null,
        rss: String? = null,
        wall: GroupUnitAccessTypeExtended? = null,
        topics: GroupUnitAccessType? = null,
        photos: GroupUnitAccessType? = null,
        video: GroupUnitAccessType? = null,
        audio: GroupUnitAccessType? = null,
        docs: GroupUnitAccessType? = null,
        wiki: GroupUnitAccessType? = null,
        messages: Boolean? = null,
        articles: Boolean? = null,
        addresses: Boolean? = null,
        market: Boolean? = null,
        marketComments: Boolean? = null,
        marketCountries: List<Int>?,
        marketCities: List<Int>?,
        ageLimits: Community.AgeLimits?,
        marketCurrency: MarketCurrency? = null,
        marketContact: Int? = null,
        marketWiki: Int? = null,
        obsceneFilter: Boolean? = null,
        obsceneStopwords: Boolean? = null,
        obsceneWords: List<String>?,
        mainSection: Community.MainSectionType?,
        secondarySection: Community.MainSectionType?
    ): VkApiRequest<JsonObject> = edit(
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
        secondarySection = secondarySection
    )


    fun editCallbackServer(
        groupId: Int,
        serverId: Int,
        url: String,
        title: String,
        secretKey: String? = null
    ): VkApiRequest<JsonObject> = Methods.editCallbackServer.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("server_id", serverId)
        append("url", url)
        append("title", title)
        append("secret_key", secretKey)
    }


    fun editLink(
        groupId: Int,
        linkId: Int,
        text: String? = null
    ): VkApiRequest<JsonObject> = Methods.editLink.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("link_id", linkId)
        append("text", text)
    }


    fun editManager(
        groupId: Int,
        userId: Int,
        role: CommunityManagerRole? = null,
        isContact: Boolean? = null,
        contactPosition: String? = null,
        contactPhone: String? = null,
        contactEmail: String? = null
    ): VkApiRequest<JsonObject> = Methods.editManager.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("user_id", userId)
        append("role", role?.value)
        append("is_contact", isContact?.toInt())
        append("contact_position", contactPosition)
        append("contact_phone", contactPhone)
        append("contact_email", contactEmail)
    }


    fun editPlace(
        groupId: Int,
        title: String? = null,
        address: String? = null,
        countryId: Int? = null,
        cityId: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): VkApiRequest<JsonObject> = Methods.editPlace.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("title", title)
        append("address", address)
        append("country_id", countryId)
        append("city_id", cityId)
        append("latitude", latitude)
        append("longitude", longitude)
    }


    fun enableOnline(
        groupId: Int
    ): VkApiRequest<JsonObject> = Methods.enableOnline.call(JsonObject.serializer()) {
        append("group_id", groupId)
    }


    fun getIds(
        userId: Int? = null,
        filter: GroupsFilter? = null,
        offset: Int,
        count: Int
    ): VkApiRequest<JsonObject> = Methods.get.call(JsonObject.serializer()) {
        append("user_id", userId)
        append("filter", filter?.value)
        append("offset", offset)
        append("count", count)
    }


    fun get(
        userId: Int? = null,
        filter: GroupsFilter? = null,
        fields: List<CommunityOptionalField>?,
        offset: Int,
        count: Int
    ): VkApiRequest<JsonObject> = Methods.get.call(JsonObject.serializer()) {
        append("user_id", userId)
        append("extended", 1)
        append("filter", filter?.value)
        append("fields", fields?.joinToString(",") { it.value })
        append("offset", offset)
        append("count", count)
    }


    fun getAddresses(
        groupId: Int,
        addressIds: List<Int>?,
        latitude: Double? = null,
        longitude: Double? = null,
        offset: Int,
        count: Int,
        fields: List<AddressOptionalFields>?
    ): VkApiRequest<JsonObject> = Methods.getAddresses.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("address_ids", addressIds?.joinToString(","))
        append("latitude", latitude)
        append("longitude", longitude)
        append("offset", offset)
        append("count", count)
        append("fields", fields?.joinToString(",") { it.value })
    }


    fun getBanned(
        groupId: Int,
        offset: Int,
        count: Int,
        fields: List<ObjectField>,
        ownerId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.getBanned.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("offset", offset)
        append("count", count)
        append("fields", fields.joinToString(",") { it.value })
        append("owner_id", ownerId)
    }


    fun getById(
        groupIds: List<Int>,
        communityFields: List<CommunityOptionalField>? = null
    ): VkApiRequest<List<Community>> = Methods.getById.call(ListSerializer(Community.serializer())) {
        append("group_ids", groupIds.joinToString(","))
        append("fields", communityFields?.joinToString(",") { it.value })
    }


    suspend fun getName(
        groupId: Int
    ): String = getById(listOf(groupId)).execute()[0].name

    fun getCallbackConfirmationCode(
        groupId: Int
    ): VkApiRequest<JsonObject> = Methods.getCallbackConfirmationCode.call(JsonObject.serializer()) {
        append("group_id", groupId)
    }


    fun getCallbackServers(
        groupId: Int,
        serverIds: List<Int>?
    ): VkApiRequest<JsonObject> = Methods.getCallbackServers.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("server_ids", serverIds?.joinToString(","))
    }


    fun getCallbackSettings(
        groupId: Int,
        serverId: Int
    ): VkApiRequest<JsonObject> = Methods.getCallbackSettings.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("server_id", serverId)
    }


    fun getCatalog(
        categoryId: Int? = null,
        subcategoryId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.getCatalog.call(JsonObject.serializer()) {
        append("category_id", categoryId)
        append("subcategory_id", subcategoryId)
    }


    fun getCatalogInfo(
        extended: Boolean,
        withSubcategories: Boolean
    ): VkApiRequest<JsonObject> = Methods.getCatalogInfo.call(JsonObject.serializer()) {
        append("extended", extended.toInt())
        append("subcategories", withSubcategories.toInt())
    }


    fun getInvitedUsers(
        groupId: Int,
        offset: Int,
        count: Int,
        userFields: List<UserOptionalField>,
        nameCase: NameCase
    ): VkApiRequest<JsonObject> = Methods.getInvitedUsers.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("offset", offset)
        append("count", count)
        append("fields", userFields.joinToString(",") { it.value })
        append("name_case", nameCase.value)
    }


    fun getInvites(
        offset: Int,
        count: Int,
        extended: Boolean
    ): VkApiRequest<JsonObject> = Methods.getInvites.call(JsonObject.serializer()) {
        append("offset", offset)
        append("count", count)
        append("extended", extended.toInt())
    }


    fun getLongPollServer(
        groupId: Int
    ): VkApiRequest<LongPollServerResponse> = Methods.getLongPollServer.call(LongPollServerResponse.serializer()) {
        append("group_id", groupId)
    }


    fun getLongPollSettings(
        groupId: Int
    ): VkApiRequest<JsonObject> = Methods.getLongPollSettings.call(JsonObject.serializer()) {
        append("group_id", groupId)
    }


    fun getMembersIds(
        groupId: Int,
        sort: CommunityMembersSort,
        offset: Int,
        count: Int,
        onlyFriends: Boolean
    ): VkApiRequest<JsonObject> = Methods.getMembers.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("sort", sort.value)
        append("offset", offset)
        append("count", count)
        append("filter", if (onlyFriends) "friends" else null)
    }


    fun getMembers(
        groupId: Int,
        sort: CommunityMembersSort,
        offset: Int,
        count: Int,
        userFields: List<UserOptionalField>,
        onlyFriends: Boolean
    ): VkApiRequest<JsonObject> = Methods.getMembers.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("sort", sort.value)
        append("offset", offset)
        append("count", count)
        append("fields", userFields.joinToString(",") { it.value })
        append("filter", if (onlyFriends) "friends" else null)
    }


    fun getManagersIds(
        groupId: Int,
        sort: CommunityMembersSort,
        offset: Int,
        count: Int
    ): VkApiRequest<JsonObject> = Methods.getMembers.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("sort", sort.value)
        append("offset", offset)
        append("count", count)
        append("filter", "managers")
    }


    fun getManagers(
        groupId: Int,
        sort: CommunityMembersSort,
        offset: Int,
        count: Int,
        userFields: List<UserOptionalField>
    ): VkApiRequest<JsonObject> = Methods.getMembers.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("sort", sort.value)
        append("offset", offset)
        append("count", count)
        append("fields", userFields.joinToString(",") { it.value })
        append("filter", "managers")
    }


    fun getOnlineStatus(
        groupId: Int
    ): VkApiRequest<JsonObject> = Methods.getOnlineStatus.call(JsonObject.serializer()) {
        append("group_id", groupId)
    }


    fun getRequestsIds(
        groupId: Int,
        offset: Int,
        count: Int
    ): VkApiRequest<JsonObject> = Methods.getRequests.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("offset", offset)
        append("count", count)
    }


    fun getRequests(
        groupId: Int,
        offset: Int,
        count: Int,
        userFields: List<UserOptionalField>
    ): VkApiRequest<JsonObject> = Methods.getRequests.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("offset", offset)
        append("count", count)
        append("fields", userFields.joinToString(",") { it.value })
    }


    fun getSettings(
        groupId: Int
    ): VkApiRequest<JsonObject> = Methods.getSettings.call(JsonObject.serializer()) {
        append("group_id", groupId)
    }


    fun getTokenPermissions(): VkApiRequest<JsonObject> = Methods.getTokenPermissions.call(JsonObject.serializer())


    fun invite(
        groupId: Int,
        userId: Int
    ): VkApiRequest<JsonObject> = Methods.invite.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("user_id", userId)
    }


    fun isMember(
        groupId: Int,
        userId: Int? = null
    ): VkApiRequest<JsonObject> = Methods.isMember.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("user_id", userId)
        append("extended", 1)
    }


    fun isMembers(
        groupId: Int,
        userIds: List<Int>
    ): VkApiRequest<JsonObject> = Methods.isMember.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("user_ids", userIds.joinToString(","))
    }


    fun join(
        groupId: Int,
        notSure: Boolean? = null
    ): VkApiRequest<JsonObject> = Methods.join.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("not_sure", notSure?.toInt())
    }


    fun leave(
        groupId: Int
    ): VkApiRequest<JsonObject> = Methods.leave.call(JsonObject.serializer()) {
        append("group_id", groupId)
    }


    fun removeUser(
        groupId: Int,
        userId: Int
    ): VkApiRequest<JsonObject> = Methods.removeUser.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("user_id", userId)
    }


    fun reorderLink(
        groupId: Int,
        linkId: Int,
        after: Int
    ): VkApiRequest<JsonObject> = Methods.reorderLink.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("link_id", linkId)
        append("after", after)
    }


    fun search(
        query: String,
        type: Community.Type?,
        countryId: Int? = null,
        cityId: Int? = null,
        isFuture: Boolean? = null,
        hasMarket: Boolean? = null,
        sort: CommunitySearchOrder,
        offset: Int,
        count: Int
    ): VkApiRequest<JsonObject> = Methods.search.call(JsonObject.serializer()) {
        append("q", query)
        append("type", type?.value)
        append("country_id", countryId)
        append("city_id", cityId)
        append("future", isFuture?.toInt())
        append("market", hasMarket?.toInt())
        append("sort", sort.value)
        append("offset", offset)
        append("count", count)
    }


    fun setCallbackSettings(
        groupId: Int,
        serverId: Int,
        apiVersion: String? = null,
        messageReply: Boolean? = null,
        messagesEdit: Boolean? = null,
        messageAllow: Boolean? = null,
        messageDeny: Boolean? = null,
        messageTypingState: Boolean? = null,
        messageRead: Boolean? = null,
        photoNew: Boolean? = null,
        photoCommentNew: Boolean? = null,
        photoCommentEdit: Boolean? = null,
        photoCommentRestore: Boolean? = null,
        photoCommentDelete: Boolean? = null,
        audioNew: Boolean? = null,
        videoNew: Boolean? = null,
        videoCommentNew: Boolean? = null,
        videoCommentEdit: Boolean? = null,
        videoCommentRestore: Boolean? = null,
        videoCommentDelete: Boolean? = null,
        wallPostNew: Boolean? = null,
        wallRepost: Boolean? = null,
        wallReplyNew: Boolean? = null,
        wallReplyEdit: Boolean? = null,
        wallReplyRestore: Boolean? = null,
        wallReplyDelete: Boolean? = null,
        boardPostNew: Boolean? = null,
        boardPostEdit: Boolean? = null,
        boardPostRestore: Boolean? = null,
        boardPostDelete: Boolean? = null,
        marketCommentNew: Boolean? = null,
        marketCommentEdit: Boolean? = null,
        marketCommentRestore: Boolean? = null,
        marketCommentDelete: Boolean? = null,
        groupLeave: Boolean? = null,
        groupJoin: Boolean? = null,
        userBlock: Boolean? = null,
        userUnblock: Boolean? = null,
        pollVoteNew: Boolean? = null,
        groupOfficersEdit: Boolean? = null,
        groupChangeSettings: Boolean? = null,
        groupChangePhoto: Boolean? = null,
        vkPayTransaction: Boolean? = null,
        appPayload: Boolean? = null
    ): VkApiRequest<JsonObject> = Methods.setCallbackSettings.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("server_id", serverId)
        append("api_version", apiVersion)
        append("message_reply", messageReply?.toInt())
        append("messages_edit", messagesEdit?.toInt())
        append("message_allow", messageAllow?.toInt())
        append("message_deny", messageDeny?.toInt())
        append("message_typing_state", messageTypingState?.toInt())
        append("message_read", messageRead?.toInt())
        append("photo_new", photoNew?.toInt())
        append("photo_comment_new", photoCommentNew?.toInt())
        append("photo_comment_edit", photoCommentEdit?.toInt())
        append("photo_comment_restore", photoCommentRestore?.toInt())
        append("photo_comment_delete", photoCommentDelete?.toInt())
        append("audio_new", audioNew?.toInt())
        append("video_new", videoNew?.toInt())
        append("video_comment_new", videoCommentNew?.toInt())
        append("video_comment_edit", videoCommentEdit?.toInt())
        append("video_comment_restore", videoCommentRestore?.toInt())
        append("video_comment_delete", videoCommentDelete?.toInt())
        append("wall_post_new", wallPostNew?.toInt())
        append("wall_repost", wallRepost?.toInt())
        append("wall_reply_new", wallReplyNew?.toInt())
        append("wall_reply_edit", wallReplyEdit?.toInt())
        append("wall_reply_restore", wallReplyRestore?.toInt())
        append("wall_reply_delete", wallReplyDelete?.toInt())
        append("board_post_new", boardPostNew?.toInt())
        append("board_post_edit", boardPostEdit?.toInt())
        append("board_post_restore", boardPostRestore?.toInt())
        append("board_post_delete", boardPostDelete?.toInt())
        append("market_comment_new", marketCommentNew?.toInt())
        append("market_comment_edit", marketCommentEdit?.toInt())
        append("market_comment_restore", marketCommentRestore?.toInt())
        append("market_comment_delete", marketCommentDelete?.toInt())
        append("group_leave", groupLeave?.toInt())
        append("group_join", groupJoin?.toInt())
        append("user_block", userBlock?.toInt())
        append("user_unblock", userUnblock?.toInt())
        append("poll_vote_new", pollVoteNew?.toInt())
        append("group_officers_edit", groupOfficersEdit?.toInt())
        append("group_change_settings", groupChangeSettings?.toInt())
        append("group_change_photo", groupChangePhoto?.toInt())
        append("vkpay_transaction", vkPayTransaction?.toInt())
        append("app_payload", appPayload?.toInt())
    }


    fun setLongPollSettings(
        groupId: Int,
        apiVersion: String,
        enabled: Boolean? = null,
        messageReply: Boolean? = null,
        messagesEdit: Boolean? = null,
        messageAllow: Boolean? = null,
        messageDeny: Boolean? = null,
        messageTypingState: Boolean? = null,
        messageRead: Boolean? = null,
        photoNew: Boolean? = null,
        photoCommentNew: Boolean? = null,
        photoCommentEdit: Boolean? = null,
        photoCommentRestore: Boolean? = null,
        photoCommentDelete: Boolean? = null,
        audioNew: Boolean? = null,
        videoNew: Boolean? = null,
        videoCommentNew: Boolean? = null,
        videoCommentEdit: Boolean? = null,
        videoCommentRestore: Boolean? = null,
        videoCommentDelete: Boolean? = null,
        wallPostNew: Boolean? = null,
        wallRepost: Boolean? = null,
        wallReplyNew: Boolean? = null,
        wallReplyEdit: Boolean? = null,
        wallReplyRestore: Boolean? = null,
        wallReplyDelete: Boolean? = null,
        boardPostNew: Boolean? = null,
        boardPostEdit: Boolean? = null,
        boardPostRestore: Boolean? = null,
        boardPostDelete: Boolean? = null,
        marketCommentNew: Boolean? = null,
        marketCommentEdit: Boolean? = null,
        marketCommentRestore: Boolean? = null,
        marketCommentDelete: Boolean? = null,
        groupLeave: Boolean? = null,
        groupJoin: Boolean? = null,
        userBlock: Boolean? = null,
        userUnblock: Boolean? = null,
        pollVoteNew: Boolean? = null,
        groupOfficersEdit: Boolean? = null,
        groupChangeSettings: Boolean? = null,
        groupChangePhoto: Boolean? = null,
        vkPayTransaction: Boolean? = null,
        appPayload: Boolean? = null
    ): VkApiRequest<JsonObject> = Methods.setLongPollSettings.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("api_version", apiVersion)
        append("enabled", enabled?.toInt())
        append("message_reply", messageReply?.toInt())
        append("messages_edit", messagesEdit?.toInt())
        append("message_allow", messageAllow?.toInt())
        append("message_deny", messageDeny?.toInt())
        append("message_typing_state", messageTypingState?.toInt())
        append("message_read", messageRead?.toInt())
        append("photo_new", photoNew?.toInt())
        append("photo_comment_new", photoCommentNew?.toInt())
        append("photo_comment_edit", photoCommentEdit?.toInt())
        append("photo_comment_restore", photoCommentRestore?.toInt())
        append("photo_comment_delete", photoCommentDelete?.toInt())
        append("audio_new", audioNew?.toInt())
        append("video_new", videoNew?.toInt())
        append("video_comment_new", videoCommentNew?.toInt())
        append("video_comment_edit", videoCommentEdit?.toInt())
        append("video_comment_restore", videoCommentRestore?.toInt())
        append("video_comment_delete", videoCommentDelete?.toInt())
        append("wall_post_new", wallPostNew?.toInt())
        append("wall_repost", wallRepost?.toInt())
        append("wall_reply_new", wallReplyNew?.toInt())
        append("wall_reply_edit", wallReplyEdit?.toInt())
        append("wall_reply_restore", wallReplyRestore?.toInt())
        append("wall_reply_delete", wallReplyDelete?.toInt())
        append("board_post_new", boardPostNew?.toInt())
        append("board_post_edit", boardPostEdit?.toInt())
        append("board_post_restore", boardPostRestore?.toInt())
        append("board_post_delete", boardPostDelete?.toInt())
        append("market_comment_new", marketCommentNew?.toInt())
        append("market_comment_edit", marketCommentEdit?.toInt())
        append("market_comment_restore", marketCommentRestore?.toInt())
        append("market_comment_delete", marketCommentDelete?.toInt())
        append("group_leave", groupLeave?.toInt())
        append("group_join", groupJoin?.toInt())
        append("user_block", userBlock?.toInt())
        append("user_unblock", userUnblock?.toInt())
        append("poll_vote_new", pollVoteNew?.toInt())
        append("group_officers_edit", groupOfficersEdit?.toInt())
        append("group_change_settings", groupChangeSettings?.toInt())
        append("group_change_photo", groupChangePhoto?.toInt())
        append("vkpay_transaction", vkPayTransaction?.toInt())
        append("app_payload", appPayload?.toInt())
    }


    fun setSettings(
        groupId: Int,
        messages: Boolean? = null,
        botsCapabilities: Boolean? = null,
        botsStartButton: Boolean? = null,
        botsAddToChat: Boolean? = null
    ): VkApiRequest<JsonObject> = Methods.setSettings.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("messages", messages?.toInt())
        append("bots_capabilities", botsCapabilities?.toInt())
        append("bots_start_button", botsStartButton?.toInt())
        append("bots_add_to_chat", botsAddToChat?.toInt())
    }


    fun unban(
        groupId: Int,
        ownerId: Int
    ): VkApiRequest<JsonObject> = Methods.unban.call(JsonObject.serializer()) {
        append("group_id", groupId)
        append("owner_id", ownerId)
    }

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