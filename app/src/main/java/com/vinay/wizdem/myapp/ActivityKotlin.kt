package com.vinay.wizdem.myapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.telephony.SmsManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ActivityKotlin : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var textView: TextView? = null
    private var phone: FloatingActionButton? = null
    private var message: FloatingActionButton? = null
    private var linkedin: FloatingActionButton? = null
    private var stack: FloatingActionButton? = null
    private var phone_number: String? = null

    private val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return cm.activeNetworkInfo != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.content_text) as TextView
        phone_number = getString(R.string.phone_number)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        phone = findViewById(R.id.phone) as FloatingActionButton
        message = findViewById(R.id.message) as FloatingActionButton
        linkedin = findViewById(R.id.ln) as FloatingActionButton
        stack = findViewById(R.id.stack) as FloatingActionButton
        phone!!.setOnClickListener { callPhoneRequestPermission() }
        message!!.setOnClickListener { messageRequestPermission() }
        linkedin!!.setOnClickListener {
            if (isNetworkConnected) {
                openWebView(getString(R.string.ln_link))
            } else {
                Toast.makeText(this@ActivityKotlin, "No Internet..", Toast.LENGTH_SHORT).show()
            }
        }
        stack!!.setOnClickListener {
            if (isNetworkConnected) {
                openWebView(getString(R.string.stack_link))
            } else {
                Toast.makeText(this@ActivityKotlin, "No Internet..", Toast.LENGTH_SHORT).show()
            }
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun messageComposeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("SMS")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        input.hint = "Compose Text for Vinay.."
        builder.setView(input)

        builder.setPositiveButton("Send", null)
        builder.setNegativeButton("Cancel", null)
        val mAlertdlg = builder.create()
        mAlertdlg.setOnShowListener {
            val send = mAlertdlg.getButton(AlertDialog.BUTTON_POSITIVE)
            send.setOnClickListener {
                val text = input.text.toString()
                if (text.isNullOrEmpty()) {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(phone_number, null, text, null, null)
                    Toast.makeText(this@ActivityKotlin, "Sending SMS to Vinay..", Toast.LENGTH_SHORT).show()
                    mAlertdlg.dismiss()
                } else {
                    Toast.makeText(this@ActivityKotlin, "Enter text message..", Toast.LENGTH_SHORT).show()

                }
            }

            val cancel = mAlertdlg.getButton(AlertDialog.BUTTON_NEGATIVE)
            cancel.setOnClickListener { mAlertdlg.dismiss() }
        }

        mAlertdlg.show()
    }

    private fun callConfirmDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Please Confirm..")
        builder.setMessage("You want to call Vinay Reddy?")

        builder.setPositiveButton("YES") { dialog, which ->
            val phoneIntent = Intent(Intent.ACTION_CALL)
            phoneIntent.data = Uri.parse(phone_number)
            //  startActivity(phoneIntent);
            dialog.dismiss()
        }

        builder.setNegativeButton("NO") { dialog, which ->
            // Do nothing
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALL_PERMEISSIONS_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callConfirmDialog()
                } else {
                    Toast.makeText(this@ActivityKotlin, "MyApp was denied call permissions.", Toast.LENGTH_LONG).show()
                }
            }
            SMS_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    messageComposeDialog()
                } else {
                    Toast.makeText(this@ActivityKotlin, "MyApp was denied SMS permissions.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    internal fun callPhoneRequestPermission() {

        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this@ActivityKotlin,
                    arrayOf(Manifest.permission.CALL_PHONE), CALL_PERMEISSIONS_REQUEST)

        } else {
            callConfirmDialog()
        }

    }

    internal fun messageRequestPermission() {
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this@ActivityKotlin,
                    arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_REQUEST)


        } else {
            messageComposeDialog()
        }


    }

    internal fun openWebView(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.share_link) {
            shareIntent()

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        textView!!.setText(R.string.home)
        val id = item.itemId

        if (id == R.id.profile) {
            textView!!.setText(R.string.about_me)
        } else if (id == R.id.work) {
            textView!!.setText(R.string.experience)
        } else if (id == R.id.contact) {
            textView!!.setText(R.string.contact)
        } else if (id == R.id.nav_share) {
            shareIntent()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    internal fun shareIntent() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.app_link))
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share_hint)))
    }

    companion object {
        private val CALL_PERMEISSIONS_REQUEST = 1
        private val SMS_PERMISSION_REQUEST = 2
    }
}
