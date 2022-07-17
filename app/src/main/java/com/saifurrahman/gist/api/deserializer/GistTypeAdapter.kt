package com.saifurrahman.gist.api.deserializer

import android.util.Log
import com.saifurrahman.gist.model.Gist
import com.squareup.moshi.*
import java.lang.reflect.Type

class GistTypeAdapter(private val delegate: JsonAdapter<Gist>) : JsonAdapter<Gist>() {
    object Factory : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<*>? {
            if (Types.getRawType(type) == Gist::class.java) {
                val delegate = moshi.nextAdapter<Gist>(this, type, annotations)
                return GistTypeAdapter(delegate)
            }
            return null
        }

    }

    override fun fromJson(reader: JsonReader): Gist {
        var id = ""
        var url = ""
        var filename = ""
        var username = ""

        var createdAt: String? = null
        var updatedAt: String? = null
        var description: String? = null
        var userImageUrl: String? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> {
                    id = reader.nextString()
                }
                "url" -> {
                    url = reader.nextString()
                }
                "created_at" -> {
                    createdAt = reader.nextString()
                }
                "updated_at" -> {
                    updatedAt = reader.nextString()
                }
                "description" -> {
                    if (reader.peek() == JsonReader.Token.STRING) {
                        description = reader.nextString()
                    } else {
                        reader.skipValue()
                    }
                }
                "files" -> {
                    if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
                        reader.beginObject()
                        while (reader.hasNext()) {
                            if (filename.isNotEmpty()) {
                                reader.skipValue()
                                continue
                            }

                            if (reader.peek() == JsonReader.Token.NAME) {
                                filename = reader.nextName()
                                if (filename.isNotEmpty()) {
                                    reader.skipValue()
                                }
                            } else {
                                reader.skipValue()
                            }
                        }

                        reader.endObject()
                    } else {
                        reader.skipValue()
                    }
                }
                "owner" -> {
                    if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
                        reader.beginObject()
                        while (reader.hasNext()) {
                            when (reader.nextName()) {
                                "login" -> {
                                    username = reader.nextString()
                                }
                                "avatar_url" -> {
                                    userImageUrl = reader.nextString()
                                }
                                else -> {
                                    reader.skipValue()
                                }
                            }
                        }
                        reader.endObject()
                    } else {
                        reader.skipValue()
                    }
                }
                else -> {
                    reader.skipValue()
                }
            }
        }
        reader.endObject()
        val gist = Gist(id, url, filename, username)
        gist.createdAt = createdAt
        gist.updatedAt = updatedAt
        gist.description = description
        gist.userImageUrl = userImageUrl

        return gist
    }

    override fun toJson(writer: JsonWriter, value: Gist?) {
        delegate.toJson(writer, value)
    }
}