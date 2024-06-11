package com.example.firebaseliveapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebaseliveapp.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseViewModel : ViewModel() {

    //Firebase Dienst Instanzen laden
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    val usersCollectionReference = firestore.collection("users")

    var userDataDocumentReference: DocumentReference? = null

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser : LiveData<FirebaseUser?>
        get() = _currentUser

    init {
        setupUserEnv()
    }

    fun setupUserEnv() {
        val user = auth.currentUser

        _currentUser.postValue(user)

        if(user != null){
            //Immer wenn der User eingeloggt ist muss diese Variable definiert sein
            userDataDocumentReference = usersCollectionReference.document(user.uid)
        }
    }

    fun setProfile(profile: UserData){
        if(userDataDocumentReference == null) {
            //Funktion abbrechen
            return
        }

        userDataDocumentReference!!.set(profile)
    }


    fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            setupUserEnv()
        }
    }

    fun register(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
    }

    fun signOut() {
        auth.signOut()
        setupUserEnv()
    }



}