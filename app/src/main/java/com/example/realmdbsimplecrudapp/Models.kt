package com.example.realmdbsimplecrudapp

import android.provider.ContactsContract
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(

    @PrimaryKey var id: Long = 0,

    var name: String = "",

    var age: Int = 0,

    var notes : RealmList<Note> = RealmList()


) : RealmObject()



open class Note(

    var id: Int = 0,

    var text: String = ""


) : RealmObject()