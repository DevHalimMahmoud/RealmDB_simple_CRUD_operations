package com.example.realmdbsimplecrudapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.realmdbsimplecrudapp.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("testdb.realm")
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)

        val realm: Realm = Realm.getDefaultInstance()

        create(realm)
        read(realm)
        update(realm)
        delete(realm)
    }

    private fun update(realm: Realm) {
        binding.update.setOnClickListener(View.OnClickListener {

            if (realm.isEmpty) {

                Toast.makeText(
                    applicationContext,
                    "Database is Empty no data to update",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                updateDummyData()
                Toast.makeText(
                    applicationContext,
                    "Data Updated!",
                    Toast.LENGTH_SHORT
                ).show()

            }

        })
    }

    private fun delete(realm: Realm) {
        binding.delete.setOnClickListener(View.OnClickListener {

            if (realm.isEmpty) {

                Toast.makeText(
                    applicationContext,
                    "Database is Empty there is nothing to delete",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                realm.executeTransaction { realm ->
                    realm.deleteAll()
                }

                Toast.makeText(
                    applicationContext,
                    "Database deleted! ",
                    Toast.LENGTH_SHORT
                ).show()

                binding.txv.text = "Database deleted"

            }
        })


    }

    private fun read(realm: Realm) {
        binding.read.setOnClickListener(View.OnClickListener {
            if (!realm.isEmpty) {


                val users = realm.where<User>().findAll()

                binding.txv.text = users.asJSON()?.toString()

                Toast.makeText(applicationContext, "Data readed successfully", Toast.LENGTH_SHORT)
                    .show()

            } else {

                Toast.makeText(
                    applicationContext,
                    "Database is Empty",
                    Toast.LENGTH_SHORT
                ).show()

            }
        })

    }

    private fun create(realm: Realm) {

        binding.create.setOnClickListener(View.OnClickListener {

            if (realm.isEmpty) {
                createDummyData()
                Toast.makeText(
                    applicationContext,
                    "Fake Data added Press read to see it",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    applicationContext,
                    "Database is not Empty You can Update or delete",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }


    private fun createDummyData() {
        val realm: Realm = Realm.getDefaultInstance()
        //The writing to our database always must be in a transaction
        realm.executeTransaction() {


            for (userCount in 0 until 10) {
                //The value we send as parameter is the primary key
                val user = realm.createObject<User>(userCount)
                user.name = "testName{$userCount}"
                user.age = 30

                val notes: RealmList<Note> = RealmList()
                for (noteCount in 0 until 2) {
                    val note = realm.createObject<Note>()
                    note.id = noteCount
                    note.text = "note${noteCount}"
                    notes.add(note)
                }
                user.notes = notes
            }


        }
    }

    private fun updateDummyData() {


        val realm: Realm = Realm.getDefaultInstance()
        val users: RealmResults<User> =
            realm.where(User::class.java).findAll()
        val notes: RealmResults<Note> =
            realm.where(Note::class.java).findAll()
        realm.beginTransaction()
        for (user in users) {
            user.id
            user.age = 21
            user.name = "AbdelHalim"

            val notesList: RealmList<Note> = RealmList()
            for (note in notes) {
                note.id
                note.text = "This is my note"
                notesList.add(note)
            }

            user.notes = notesList
        }

        realm.commitTransaction();

    }
}