package util

// if there is a better way of doing this plz do
object Constants {
    const val MONGO_URI = "mongodb+srv://abnormally:distributed@abnormally-distributed.naumhbd.mongodb.net/?retryWrites=true&w=majority"
    const val MONGO_DB = "abnormally-distributed"
    const val QUIZ_COLLECTION = "quizzes"
    const val QUESTION_COLLECTION = "questions"
    const val ACCOUNT_COLLECTION = "accounts"
    const val NOTE_COLLECTION = "notes"
    const val OPENAI_TOKEN = "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"
    const val OPENAI_MODEL_ID = "text-davinci-003"
    const val OPENAI_MAX_TOKENS = 600
    const val OPENAI_TEMPERATURE = 0.02
}
