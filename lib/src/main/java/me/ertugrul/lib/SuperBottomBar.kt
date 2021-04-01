package me.ertugrul.lib

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.forEach

class SuperBottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var menu: Menu? = null

    @ColorInt
    private var tabBackgroundColor: Int = Color.parseColor(DEFAULT_BACKGROUND_COLOR)

    @ColorInt
    private var activeColor: Int = Color.parseColor(DEFAULT_ACTIVE_COLOR)

    @ColorInt
    private var passiveColor: Int = Color.parseColor(DEFAULT_PASSIVE_COLOR)

    @ColorInt
    private var pressedColor: Int = Color.parseColor(DEFAULT_PRESSED_COLOR)

    private var itemTextSize: Float = DEFAULT_ITEM_TEXT_SIZE

    private var itemIconSize: Float = DEFAULT_ICON_SIZE

    private var itemIconMargin: Float = DEFAULT_ICON_MARGIN

    private var animationDuration: Int = DEFAULT_ANIMATION_DURATION

    private var endScale: Float = DEFAULT_END_SCALE

    private var startScale: Float = DEFAULT_START_SCALE

    private var activeItem = DEFAULT_ACTIVE_ITEM

    private var layoutWidth: Float = 0f
    private var layoutHeight: Float = 0f

    private var itemWidth: Float = 0f
    private var itemHeight: Float = 0f

    private var currentItemPosition: Int = 0
    private var currentItemColor = activeColor
    private var currentScalePercent = DEFAULT_START_SCALE

    private val paintBackground = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = tabBackgroundColor
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = passiveColor
        textSize = itemTextSize
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private var bottomBarItemList = mutableListOf<BottomBarItem>()

    private var onItemSelectedListener: OnItemSelectedListener? = null

    private var onItemReselectedListener: OnItemReselectedListener? = null

    var onItemSelected: ((Int) -> Unit)? = null

    var onItemReselected: ((Int) -> Unit)? = null


    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.SuperBottomBar,
            defStyleAttr,
            0
        )
        try {
            tabBackgroundColor = typedArray.getColor(
                R.styleable.SuperBottomBar_activeColor,
                Color.parseColor(DEFAULT_BACKGROUND_COLOR)
            )
            activeColor = typedArray.getColor(
                R.styleable.SuperBottomBar_activeColor,
                Color.parseColor(DEFAULT_ACTIVE_COLOR)
            )
            passiveColor = typedArray.getColor(
                R.styleable.SuperBottomBar_passiveColor,
                Color.parseColor(DEFAULT_PASSIVE_COLOR)
            )
            pressedColor = typedArray.getColor(
                R.styleable.SuperBottomBar_pressedColor,
                Color.parseColor(DEFAULT_PRESSED_COLOR)
            )

            activeItem = typedArray.getInteger(R.styleable.SuperBottomBar_initialActiveItem, DEFAULT_ACTIVE_ITEM)

            itemTextSize = typedArray.getDimension(R.styleable.SuperBottomBar_textSize, DEFAULT_ITEM_TEXT_SIZE)

            itemIconSize = typedArray.getDimension(R.styleable.SuperBottomBar_iconSize, DEFAULT_ICON_SIZE)

            itemIconMargin = typedArray.getDimension(R.styleable.SuperBottomBar_iconMargin, DEFAULT_ICON_MARGIN)

            animationDuration = typedArray.getInteger(
                R.styleable.SuperBottomBar_animationDuration,
                DEFAULT_ANIMATION_DURATION
            )

            endScale = 1 - (typedArray.getInteger(
                R.styleable.SuperBottomBar_scalePercent,
                DEFAULT_SCALE_PERCENT
            )).toFloat() / 100

            val _menu = PopupMenu(context, null).menu
            MenuInflater(context).inflate(
                typedArray.getResourceId(
                    R.styleable.SuperBottomBar_menu,
                    0
                ), _menu
            )
            menu = _menu

            menu!!.forEach { item ->
                bottomBarItemList.add(BottomBarItem(item.title, item.icon))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        layoutWidth = w.toFloat()
        layoutHeight = h.toFloat()

        var lastX = 0F

        itemWidth = layoutWidth / bottomBarItemList.size
        itemHeight = layoutHeight

        for (item in bottomBarItemList) {
            item.rect = RectF(lastX, 0F, itemWidth + lastX, height.toFloat())
            lastX += itemWidth
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(
            0f, 0f,
            width.toFloat(),
            height.toFloat(),
            paintBackground
        )

        bottomBarItemList.forEachIndexed { index, bottomBarItem ->
            var iconSize = itemIconSize
            var textSize = itemTextSize

            if (index == currentItemPosition) {
                iconSize = currentScalePercent * itemIconSize
                textSize = currentScalePercent * itemTextSize
            }

            bottomBarItem.icon.mutate()
            bottomBarItem.icon.setBounds(
                bottomBarItem.rect.centerX().toInt() - iconSize.toInt() / 2,
                height / 2 - iconSize.toInt() + itemIconMargin.toInt() / 2,
                bottomBarItem.rect.centerX().toInt() + iconSize.toInt() / 2,
                height / 2 + itemIconMargin.toInt() / 2
            )

            val colorTo = when (index) {
                activeItem -> activeColor
                currentItemPosition -> currentItemColor
                else -> passiveColor
            }

            DrawableCompat.setTint(
                bottomBarItem.icon,
                colorTo
            )

            bottomBarItem.icon.draw(canvas)

            this.paintText.textSize = textSize
            this.paintText.color = colorTo

            val textHeight = (paintText.descent() + paintText.ascent()) / 2

            canvas.drawText(
                bottomBarItem.text.toString(),
                bottomBarItem.rect.centerX(),
                bottomBarItem.rect.centerY() - textHeight + iconSize / 3 + (this.itemIconMargin / 2),
                paintText
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            bottomBarItemList.forEachIndexed { index, bottomBarItem ->
                if (bottomBarItem.rect.contains(event.x, event.y)) {
                    scaleAnimation(startScale, endScale)
                    colorAnimation(true, index, false)
                    currentItemPosition = index
                }
            }
        }

        if (event.action == MotionEvent.ACTION_UP) {
            bottomBarItemList.forEachIndexed { index, bottomBarItem ->
                if (index == this.currentItemPosition) {
                    if (bottomBarItem.rect.contains(event.x, event.y)) {
                        scaleAnimation(endScale, startScale)
                        colorAnimation(false, index, false)
                        selectItem(index)
                    } else {
                        colorAnimation(false, index, true)
                        scaleAnimation(endScale, startScale)
                    }
                }
            }
        }
        return true
    }

    private fun selectItem(pos: Int) {
        if (activeItem != pos) {
            activeItem = pos
            onItemSelectedListener?.onItemSelect(activeItem)
            onItemSelected?.invoke(activeItem)
        } else {
            onItemReselectedListener?.onItemReselect(pos)
            onItemReselected?.invoke(pos)
        }
    }

    private fun scaleAnimation(startScale: Float, endScale: Float) {
        ValueAnimator.ofFloat(startScale, endScale).apply {
            duration = animationDuration.toLong()
            addUpdateListener {
                currentScalePercent = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun colorAnimation(activeToPassive: Boolean, pos: Int, isOutside: Boolean) {
        val currentColor = if (pos == currentItemPosition) activeColor else passiveColor

        val colorFrom = if (activeToPassive) currentColor else pressedColor
        var colorTo = if (activeToPassive) pressedColor else currentColor
        if (isOutside) colorTo = passiveColor

        ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo).apply {
            duration = animationDuration.toLong()
            addUpdateListener { animator ->
                currentItemColor = animator.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    fun setOnItemSelectListener(onItemSelectedListener: OnItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener
    }

    fun setOnItemReselectListener(onItemReselectedListener: OnItemReselectedListener) {
        this.onItemReselectedListener = onItemReselectedListener
    }


    companion object {
        private const val DEFAULT_BACKGROUND_COLOR = "#1a1a1a"
        private const val DEFAULT_ACTIVE_COLOR = "#FFFFFF"
        private const val DEFAULT_PRESSED_COLOR = "#535353"
        private const val DEFAULT_PASSIVE_COLOR = "#AFB0B4"

        private const val DEFAULT_ICON_MARGIN = 16f
        private const val DEFAULT_ITEM_TEXT_SIZE = 20f
        private const val DEFAULT_ANIMATION_DURATION = 200
        private const val DEFAULT_ICON_SIZE = 60f

        private const val DEFAULT_START_SCALE = 1f
        private const val DEFAULT_END_SCALE = 0.5f
        private const val DEFAULT_SCALE_PERCENT = 8
        private const val DEFAULT_ACTIVE_ITEM = 0
    }
}