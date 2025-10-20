package alcala.jose.practica9

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private val userRef= FirebaseDatabase.getInstance().getReference("Users")

    // 1. Declare Views as class properties
    private lateinit var btnSave: Button
    private lateinit var name: EditText
    private lateinit var lastName: EditText
    private lateinit var age: EditText
    private lateinit var listV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Initialize Views once in onCreate
        btnSave = findViewById(R.id.save_button)
        name = findViewById(R.id.et_name)
        lastName = findViewById(R.id.et_lastName)
        age = findViewById(R.id.et_age)
        listV = findViewById(R.id.list_textView)


        btnSave.setOnClickListener { saveMarkForm() }

        userRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousName: String?) {}
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousName: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                // 3. Add try-catch to prevent crashes from malformed data
                try {
                    val usuario = dataSnapshot.getValue(User::class.java)
                    if (usuario != null) {
                        writeMark(usuario)
                    }
                } catch (e: DatabaseException) {
                    // Log the error or handle it silently
                    // This will prevent the app from crashing
                    e.printStackTrace()
                }
            }
        })
    }

    private fun saveMarkForm() {
        // 4. Use the already initialized views
        val usuario = User(
            name.text.toString(),
            lastName.text.toString(),
            age.text.toString()
        )

        userRef.push().setValue(usuario)

        // 5. Clear fields after saving (Good Practice)
        name.text.clear()
        lastName.text.clear()
        age.text.clear()
    }

    private fun writeMark(mark: User) {
        // 6. Use the initialized TextView and append text efficiently
        listV.append("$mark\n")
    }
}