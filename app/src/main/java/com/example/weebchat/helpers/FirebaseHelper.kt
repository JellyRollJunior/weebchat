package com.example.weebchat.helpers

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.weebchat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FirebaseHelper {

    companion object {
        private const val logTAG = "Firebase Helper"
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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

        fun getUserRef(): DatabaseReference  {
            return FirebaseDatabase.getInstance().getReference("/users")
        }

        fun signOut() {
            auth.signOut()
        }
    }
}