package utils

// Map a picture ID to a file
fun getProfilePic(profilePicId: Int): String {
    return when (profilePicId) {
        0 -> "anya.jpg"
        1 -> "asa_mitaka.jpg"
        2 -> "boston_pizza.jpg"
        3 -> "cayde_6.jpg"
        4 -> "commander_psyduck.jpg"
        5 -> "saxophone_man.jpg"
        6 -> "sunset.jpg"
        7 -> "uwaterloo_mascot.jpg"
        else -> "pfp_tmp.svg"
    }
}