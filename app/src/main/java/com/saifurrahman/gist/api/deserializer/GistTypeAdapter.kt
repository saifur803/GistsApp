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

    override fun fromJson(reader: JsonReader): Gist? {
        var id = ""
        var url = ""
        var filename = ""
        var username = ""

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> {
                    id = reader.nextString()
                }
                "url" -> {
                    url = reader.nextString()
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
                            if (reader.nextName() == "login") {
                                username = reader.nextString()
                            } else {
                                reader.skipValue()
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

        return Gist(id, url, filename, username)
    }

    override fun toJson(writer: JsonWriter, value: Gist?) {
        delegate.toJson(writer, value)
    }
}