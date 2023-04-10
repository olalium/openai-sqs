package com.projects.openaisqs.openai.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ModelResponse(
    val data: List<Model>,
    @JsonProperty("object") val objectName: String
)

data class Model(
    val id: String,
    val permission: List<ModelPermission>,
    @JsonProperty("created") val createdTimestamp: Long,
    @JsonProperty("owned_by") val ownedBy: String
)

data class ModelPermission(
    val id: String,
    val created: Long,
    val organization: String,
    @JsonProperty("allow_create_engine") val allowCreateEngine: Boolean,
    @JsonProperty("allow_sampling") val allowSampling: Boolean,
    @JsonProperty("allow_logprobs") val allowLogprobs: Boolean,
    @JsonProperty("allow_search_indices") val allowSearchIndices: Boolean,
    @JsonProperty("allow_view") val allowView: Boolean,
    @JsonProperty("allow_fine_tuning") val allowFineTuning: Boolean,
    @JsonProperty("is_blocking") val isBlocking: Boolean,
)