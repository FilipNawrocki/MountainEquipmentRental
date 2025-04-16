package pl.example1.mountainequipmentrental.Model

import android.os.Parcel
import android.os.Parcelable

data class GearModel(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val available: Boolean = true,
    val pricePerWeek: Double = 0.0,
    val categoryName: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeByte(if (available) 1 else 0)
        parcel.writeDouble(pricePerWeek)
        parcel.writeString(categoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GearModel> {
        override fun createFromParcel(parcel: Parcel): GearModel {
            return GearModel(parcel)
        }

        override fun newArray(size: Int): Array<GearModel?> {
            return arrayOfNulls(size)
        }
    }
}
