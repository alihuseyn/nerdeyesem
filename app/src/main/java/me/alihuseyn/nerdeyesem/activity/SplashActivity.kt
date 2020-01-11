package me.alihuseyn.nerdeyesem.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import me.alihuseyn.nerdeyesem.R

/**
 * <h1>SplashActivity Class</h1>
 * <p>
 *  Splash Activity helps to show splash screen
 * </p>
 */
class SplashActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, 2000)
    }
}