package me.alihuseyn.nerdeyesem.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import me.alihuseyn.nerdeyesem.R
import co.infinum.goldfinger.Goldfinger
import com.google.android.material.snackbar.Snackbar

/**
 * <h1>AuthActivity Activity</h1>
 * <p>
 *    Auth Activity which show fingerprint authentication
 *    Also allow to skip if not supported
 * </p>
 */
class AuthActivity : AppCompatActivity() {

    /**
     * Auth object
     */
    lateinit var auth: Goldfinger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        auth = Goldfinger.Builder(this).build()

        // Skip Button Click
        findViewById<Button>(R.id.auth_skip).setOnClickListener {
            this.skipAuthentication()
        }

        // Authentication Button
        findViewById<ImageButton>(R.id.auth_fingerprint).setOnClickListener {
            this.authenticate()
        }
    }

    /**
     * Authenticate prompt and listener
     */
    private fun authenticate() {
        if (auth.canAuthenticate()) {
            val prompt = Goldfinger.PromptParams.Builder(this)
                .title(R.string.verify_identity_title)
                .negativeButtonText(R.string.cancel)
                .description(R.string.verify_identity_description)
                .subtitle(R.string.verify_identity_subtitle)
                .build()

            auth.authenticate(prompt, object : Goldfinger.Callback {
                override fun onError(e: Exception) {
                    if (!e.message.isNullOrEmpty()) {
                        Snackbar
                            .make(findViewById(R.id.auth_container), e.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onResult(result: Goldfinger.Result) {
                    if (result.type().equals(Goldfinger.Type.SUCCESS)) {
                        this@AuthActivity.skipAuthentication()
                    } else {
                        if (!result.message().isNullOrEmpty()) {
                            Snackbar
                                .make(
                                    findViewById<RelativeLayout>(R.id.auth_container),
                                    result.message().toString(),
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                }
            })

        } else {
            Snackbar
                .make(findViewById(R.id.auth_container), R.string.auth_not_supported, Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Skip authentication and forward main activity
     */
    private fun skipAuthentication() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
