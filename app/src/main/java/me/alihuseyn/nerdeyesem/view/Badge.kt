package me.alihuseyn.nerdeyesem.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.TextView
import me.alihuseyn.nerdeyesem.R

/**
 * <h1>Badge View</h1>
 * <p>
 *     Show badge according to the variant inside of textview
 *     There are 7 badge type
 * </p>
 */
class Badge(context: Context, attrs: AttributeSet? = null) : TextView(context, attrs) {
    private var variant: String = "primary"

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Badge,
            0, 0
        ).apply {
            try {
                setBadgeVariant(getString(R.styleable.Badge_variant).toString())
            } finally {
                recycle()
            }
        }
    }

    /**
     * Set badge variant
     *
     * @param variant String variant type
     */
    fun setBadgeVariant(variant: String) {
        this.variant = variant
        val drawable = this@Badge.context.getDrawable(R.drawable.badge) as GradientDrawable
        when (variant) {
            "primary" -> {
                drawable.setColor(resources.getColor(R.color.primary))
                drawable.setStroke(1, resources.getColor(R.color.primary))
                this@Badge.setTextColor(Color.WHITE)
            }
            "secondary" -> {
                drawable.setColor(resources.getColor(R.color.secondary))
                drawable.setStroke(1, resources.getColor(R.color.secondary))
                this@Badge.setTextColor(Color.WHITE)
            }
            "success" -> {
                drawable.setColor(resources.getColor(R.color.success))
                drawable.setStroke(1, resources.getColor(R.color.success))
                this@Badge.setTextColor(Color.WHITE)
            }
            "danger" -> {
                drawable.setColor(resources.getColor(R.color.danger))
                drawable.setStroke(1, resources.getColor(R.color.danger))
                this@Badge.setTextColor(Color.WHITE)
            }
            "warning" -> {
                drawable.setColor(resources.getColor(R.color.warning))
                drawable.setStroke(1, resources.getColor(R.color.warning))
                this@Badge.setTextColor(Color.BLACK)
            }
            "info" -> {
                drawable.setColor(resources.getColor(R.color.info))
                drawable.setStroke(1, resources.getColor(R.color.info))
                this@Badge.setTextColor(Color.WHITE)
            }
            "dark" -> {
                drawable.setColor(resources.getColor(R.color.dark))
                drawable.setStroke(1, resources.getColor(R.color.dark))
                this@Badge.setTextColor(Color.WHITE)
            }
        }
        this@Badge.background = drawable
    }
}