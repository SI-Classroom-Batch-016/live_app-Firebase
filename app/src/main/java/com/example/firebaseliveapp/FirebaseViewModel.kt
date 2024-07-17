package com.example.firebaseliveapp

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebaseliveapp.data.Note
import com.example.firebaseliveapp.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseViewModel : ViewModel() {

    val apiKey = BuildConfig.apiKey

    init {
        Log.d("BuildConfigField", "$apiKey")
    }


    //Firebase Dienst Instanzen laden
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    val usersCollectionReference = firestore.collection("users")


    //Relevante User Daten
    var userDataDocumentReference: DocumentReference? = null

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser : LiveData<FirebaseUser?>
        get() = _currentUser

    init {
        setupUserEnv()
    }

    fun setupUserEnv() {
        val user : FirebaseUser? = auth.currentUser
        _currentUser.postValue(user)
    }

    fun setProfile(profile: UserData){
        if(userDataDocumentReference == null) {
            //Funktion abbrechen
            return
        }
        userDataDocumentReference!!.set(profile)
    }

    fun uploadImage(uri: Uri) {
        // Erstellen einer Referenz und des Upload Tasks

        val imageRef = storage.reference.child(
            "images/"
                + currentUser.value!!.uid +
                "/profilepic")

        val uploadTask = imageRef.putFile(uri)

        // Ausführen des UploadTasks
        uploadTask.addOnCompleteListener {


            imageRef.downloadUrl.addOnSuccessListener {
                val imageUrl = it.toString()
                Log.d("ProfilePicUrl", imageUrl)
                userDataDocumentReference?.update("profilePic" , imageUrl)
            }
        }
    }


    fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            setupUserEnv()
        }
    }

    fun register(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            //Initialisiert unter anderen die Document Reference zu dem Profil
            setupUserEnv()

            userDataDocumentReference?.set(UserData())
        }
    }

    fun signOut() {
        auth.signOut()
        setupUserEnv()
    }

    val notesCollectionReference = firestore.collection("notes")

    fun addNote(testNote: Note) {
        val newNote = testNote.copy(creatorUid = currentUser.value!!.uid)
        notesCollectionReference.add(newNote)
    }


}