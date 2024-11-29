package com.example.skegoapp.data

import android.os.Parcel
import android.os.Parcelable

data class Task(
    var id: Int, // Tambahkan ID
    var title: String,
    var deadline: String,
    val fullDeadline: String,
    var priority: String,
    var type: String,
    var duration: Int,
    var status: String,
    var detail: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(), // Baca ID dari Parcel
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id) // Tulis ID ke Parcel
        parcel.writeString(title)
        parcel.writeString(deadline)
        parcel.writeString(fullDeadline)
        parcel.writeString(priority)
        parcel.writeString(type)
        parcel.writeInt(duration)
        parcel.writeString(status)
        parcel.writeString(detail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}




