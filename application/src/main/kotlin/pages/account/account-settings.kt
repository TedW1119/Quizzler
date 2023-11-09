package pages.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoClient
import composables.button
import composables.dropdown
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import pages.Accounts

@Composable
@Preview
fun accountSettings(changePage: (String) -> Unit, accountId: ObjectId) {
    var account: Accounts

    // Query the user's account data
    // TODO: maybe move all of this MongoDB logic to a centralized file
    val uri = "mongodb+srv://abnormally:distributed@abnormally-distributed.naumhbd.mongodb.net/?retryWrites=true&w=majority"
    val client = MongoClient.create(uri)
    val database = client.getDatabase("abnormally-distributed")
    val collection = database.getCollection<Accounts>("accounts")
    runBlocking {
        account = collection.find(Filters.eq("_id", accountId)).firstOrNull()
            ?: Accounts(0, "", "", "", "", "", "")
        // TODO: display error message if no account was found for the accountId
    }

    // Display account data, and store changes
    val accountData = AccountSettingsFormData(
        account.name,
        account.username,
        account.email,
        account.educationLevel,
        account.profilePicId
    )

    // Handle button presses
    fun handleCancel() {

    }
    fun handleSaveChanges() {

    }

    // Handle updating a field value
    fun updateField(field: String, value: String) {
        when (field) {
            "Name" -> accountData.name = value
            "Username" -> accountData.username = value
            "Email" -> accountData.email = value
            "Education Level" -> accountData.educationLevel = value
            "Profile Picture" -> accountData.profilePictureId = value
        }
    }

    // Render the UI
    Box(Modifier.fillMaxSize().padding(15.dp)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            // Avatar
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource("hamburger.png"),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(150.dp).width(150.dp).clip(CircleShape)
                )
            }

            // Account fields
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Name", true, ::updateField, accountData.name)
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Username", true, ::updateField, accountData.username)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Email", true, ::updateField, accountData.email)
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    // TODO: fix passing the dropdown a list of items
                    //dropdown("Education Level", educationLevelOptions, ::updateField, accountData.educationLevel)
                    dropdown("Education Level", ::updateField, accountData.educationLevel)
                }
            }

            // Cancel and save buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.weight(1f).padding(10.dp), contentAlignment = Alignment.CenterEnd) {
                    button("Cancel", false, ::handleCancel)
                }
                Box(modifier = Modifier.weight(1f).padding(10.dp), contentAlignment = Alignment.CenterStart) {
                    button("Save Changes", true, ::handleSaveChanges)
                }
            }
        }
    }

}