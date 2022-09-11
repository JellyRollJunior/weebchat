package com.example.weebchat.helpers

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.weebchat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FirebaseHelper {

    companion object {
        private const val logTAG = "Firebase Helper"
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()

        fun loginUser(activity: Activity, email: String, password: String) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(activity, "Incorrect credentials entered!", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        fun createUser(activity: Activity, name: String, email: String, password: String, photoUri: Uri?) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Sign up successful", Toast.LENGTH_SHORT).show()
                        uploadImageToFirebaseStorage(activity, name, photoUri)
                    } else {
                        Toast.makeText(activity, "Error signing up occurred", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        private fun uploadImageToFirebaseStorage(activity: Activity, name: String, photoUri: Uri?) {
            if (photoUri == null) {
                Log.d(logTAG, "Selected photo Uri is null")
                return
            }
            // creates a random string name for the file
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(photoUri)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.d(logTAG, "Successfully uploaded image: $filename")
                        ref.downloadUrl.addOnSuccessListener {
                            Log.d(logTAG, "File Location: $it")
                            saveUserToFirebaseStorage(activity, name, it.toString())
                        }
                    } else {
                        Log.d(logTAG, "Upload image unsuccessful: $filename")
                    }
                }
        }

        private fun saveUserToFirebaseStorage(activity: Activity, name: String, profileImageUrl: String) {
            val uid = auth.uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
            val user = User(name, uid, profileImageUrl)

            ref.setValue(user)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.d(logTAG, "User successfully saved to database")
                    } else {
                        Log.d(logTAG, "User could not be saved to database")
                    }
                }
        }
    }
}