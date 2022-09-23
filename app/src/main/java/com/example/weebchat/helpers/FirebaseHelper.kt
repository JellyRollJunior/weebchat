package com.example.weebchat.helpers

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.weebchat.data.ChatMessage
import com.example.weebchat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FirebaseHelper {

    companion object {
        private const val logTAG = "Firebase Helper"
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()

        fun getUid(): String {
            return auth.uid.toString()
        }

        fun isLoggedIn(): Boolean {
            auth.uid ?: return false
            return true
        }

        fun loginUser(email: String, password: String): Boolean {
            var loginSuccess = false
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    loginSuccess = true
                }
            return loginSuccess
        }

        fun createUser(activity: Activity, name: String, email: String, password: String, photoUri: Uri?){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(activity, "Sign up successful", Toast.LENGTH_SHORT).show()
                    uploadImageToFirebaseStorage(name, photoUri)
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "Error signing up occurred", Toast.LENGTH_SHORT).show()
                }
        }

        private fun uploadImageToFirebaseStorage(name: String, photoUri: Uri?) {
            if (photoUri == null) {
                Log.d(logTAG, "Selected photo Uri is null")
                return
            }
            // creates a random string name for the file
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(photoUri)
                .addOnSuccessListener {
                    Log.d(logTAG, "Successfully uploaded image: $filename")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(logTAG, "File Location: $it")
                        saveUserToFirebaseStorage(name, it.toString())
                    }
                }
                .addOnFailureListener {
                        Log.d(logTAG, "Upload image unsuccessful: $filename")
                }
        }

        private fun saveUserToFirebaseStorage(name: String, profileImageUrl: String) {
            val uid = auth.uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
            val user = User(name, uid, profileImageUrl)

            ref.setValue(user)
                .addOnSuccessListener {
                    Log.d(logTAG, "User successfully saved to database")
                }
                .addOnFailureListener {
                    Log.d(logTAG, "User could not be saved to database")
                }
        }

        fun saveMessage(messageText: String, senderUid: String, receiverUid: String) {
            val ref = getMessagesRefPush(senderUid, receiverUid)
            // send to firebase storage
            val message = ChatMessage(
                ref.key!!,
                messageText,
                senderUid,
                receiverUid,
                System.currentTimeMillis()
            )
            ref.setValue(message)
                .addOnSuccessListener {
                    Log.d(logTAG, "Saved our chat message: ${ref.key}")
                }
            // save latest message for retrieval in latest messages fragment
            saveLatestMessage(message, senderUid, receiverUid)

            // need to create a reverse reference for the other user to view messages as well
            // so we can chat with ourselves without creating two messages
            if (senderUid != receiverUid) {
                val reverseRef = getMessagesRefPush(receiverUid, senderUid)
                reverseRef.setValue(message)
                    .addOnSuccessListener {
                        Log.d(logTAG, "Saved our chat message: ${ref.key}")
                    }
                saveLatestMessage(message, receiverUid, senderUid)
            }
        }

        private fun saveLatestMessage(message: ChatMessage, senderUid: String, receiverUid: String) {
            val ref = getLatestMessageRef(senderUid, receiverUid)
            ref.setValue(message)
                .addOnSuccessListener {
                    Log.d(logTAG, "Saved latest message: ${ref.key}")
                }
        }

        fun getUserRef(): DatabaseReference  {
            return FirebaseDatabase.getInstance().getReference("/users")
        }

        fun getCurrentUserRef(): DatabaseReference {
            return FirebaseDatabase.getInstance().getReference("/users/${getUid()}")
        }

        fun getMessagesRef(senderUid: String, receiverUid: String): DatabaseReference {
            return FirebaseDatabase.getInstance().getReference("/user-messages/$senderUid/$receiverUid")
        }

        private fun getMessagesRefPush(senderUid: String, receiverUid: String): DatabaseReference {
            // push creates a new node in the database /messages/newNodeID
            return FirebaseDatabase.getInstance().getReference("/user-messages/$senderUid/$receiverUid").push()
        }

        private fun getLatestMessageRef(senderUid: String, receiverUid: String): DatabaseReference {
            return FirebaseDatabase.getInstance().getReference("/latest-messages/$senderUid/$receiverUid")
        }

        fun getLatestMessagesRef(uid: String): DatabaseReference {
            Log.d(logTAG, "/latest-messages/$uid")
            return FirebaseDatabase.getInstance().getReference("/latest-messages/$uid")
        }

        fun signOut() {
            auth.signOut()
        }
    }
}