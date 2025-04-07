package pl.example1.mountainequipmentrental.Model

data class GearModel(
    var id: String = "",
    val Name: String = "",
    val description: String = "",
    val isAvailable: Boolean = true,
    val pricePerWeek: Double = 0.0){

}
