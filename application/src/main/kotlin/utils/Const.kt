package utils

object Constants {
    const val BASE_URL  = "http://127.0.0.1:8080"

    // Number of possible profile picture options
    // Note: there are actually NUM_PFP - 1 total options, since NUM_PFP - 1 serves
    //       as the upper bound for the randomly generated ID
    const val NUM_PFP = 8
}
