package com.ice.httpclient.widget

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.SweepGradient
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import java.text.DecimalFormat
import com.ice.httpclient.R
import kotlin.math.log
import kotlin.math.max
import kotlin.math.min

/**
 * Desc:圆形进度条,进度圆环
 *      带阴影 带进度 带动画 带圆角
 *      兼职工作的出勤率进度自定义View
 *
 * Created by icewater on 2024/01/19.
 */
class MyCircleProgressView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    //是否开启抗锯齿
    private var antiAlias: Boolean = true

    //圆心位置
    private lateinit var centerPosition: Point

    //半径
    private var radius: Float? = null

    //声明边界矩形
    private var mRectF: RectF? = null

    //声明背景圆画笔
    private lateinit var mBgCirPaint: Paint //画笔
    private var mBgCirColor: Int? = null //颜色
    private var mBgCirWidth: Float = 15f //圆环背景宽度

    //声明进度圆的画笔
    private lateinit var mCirPaint: Paint //画笔
    private var mCirColor: Int? = null //颜色
    private var mCirWidth: Float = 15f //主圆的宽度

    //绘制的起始角度和滑过角度(默认从顶部开始绘制，绘制360度)
    private var mStartAngle: Float = -90f
    private var mSweepAngle: Float = 360f

    //动画时间（默认一秒）
    private var mAnimTime: Int = 1000

    //属性动画
    private var mAnimator: ValueAnimator? = null

    //动画进度
    private var mAnimPercent: Float = 0f

    //当前进度值
    private var mValue: String? = null

    //最大进度值(默认为100)
    private var mMaxProgress: Float = 100f

    //绘制数值
    private lateinit var mValuePaint: TextPaint
    private var mValueSize: Float? = null
    private var mValueColor: Int? = null

    //绘制进度的后缀-默认为百分号%
    private var mUnit: CharSequence? = "%"

    //绘制描述
    private var mHint: CharSequence? = null
    private var mHintShow: Boolean = false
    private lateinit var mHintPaint: TextPaint
    private var mHintSize: Float? = null
    private var mHintColor: Int? = null

    //颜色渐变色
    private var isGradient: Boolean? = null
    private var mGradientColors: IntArray? = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
    private var mGradientColor: Int? = null
    private var mSweepGradient: SweepGradient? = null
    private var mMatrix: Matrix? = null

    //阴影
    private var mShadowColor: Int? = null
    private var mShadowSize: Float? = null
    private var mShadowIsShow: Boolean = false

    //保留的小数位数(默认0位)
    private var mDigit: Int = 0

    //是否需要动画(默认需要动画)
    private var isAnim: Boolean = true

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mAnimPercent = 0f
        centerPosition = Point() //初始化圆心属性
        mRectF = RectF()
        mAnimator = ValueAnimator() //初始化属性动画
        initAttrs(context, attrs) //初始化属性
        initPaint() //初始化画笔
    }


    /**
     * 初始化属性
     */
    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.MyCircleProgressView)

        isAnim = typedArray.getBoolean(R.styleable.MyCircleProgressView_isAnim, true)
        mAnimTime = typedArray.getInt(R.styleable.MyCircleProgressView_animTime, 1000)
        mDigit = typedArray.getInt(R.styleable.MyCircleProgressView_digit, 0)
        mBgCirWidth = typedArray.getDimension(R.styleable.MyCircleProgressView_bgCirWidth, 15f)
        mBgCirColor = typedArray.getColor(
            R.styleable.MyCircleProgressView_bgCirColor, Color.parseColor("#DDE3F6")
        )
        mCirWidth = typedArray.getDimension(R.styleable.MyCircleProgressView_cirWidth, 15f)
        mCirColor = typedArray.getColor(
            R.styleable.MyCircleProgressView_cirColor, Color.parseColor("#3277F9")
        )
        mStartAngle = typedArray.getFloat(R.styleable.MyCircleProgressView_startAngle, -90f)
        mSweepAngle = typedArray.getFloat(R.styleable.MyCircleProgressView_sweepAngle, 360f)
        mValue = typedArray.getString(R.styleable.MyCircleProgressView_value)
        mUnit = typedArray.getString(R.styleable.MyCircleProgressView_unit)
        mValueSize = typedArray.getDimension(R.styleable.MyCircleProgressView_valueSize, 15f)
        mValueColor = typedArray.getColor(
            R.styleable.MyCircleProgressView_valueColor, Color.parseColor("#5456A4")
        )
        mMaxProgress = typedArray.getFloat(R.styleable.MyCircleProgressView_maxProgress, 100f)
        mHint = typedArray.getString(R.styleable.MyCircleProgressView_hint)
        mHintShow = typedArray.getBoolean(R.styleable.MyCircleProgressView_hintShow, false)
        mHintSize = typedArray.getDimension(R.styleable.MyCircleProgressView_hintSize, 13f)
        mHintColor = typedArray.getColor(
            R.styleable.MyCircleProgressView_hintColor, Color.parseColor("#8098B2")
        )
        mShadowIsShow = typedArray.getBoolean(R.styleable.MyCircleProgressView_shadowShow, false)
        mShadowSize = typedArray.getDimension(R.styleable.MyCircleProgressView_shadowSize, 8f)
        mShadowColor = typedArray.getColor(
            R.styleable.MyCircleProgressView_shadowColor, Color.parseColor("#734947B2")
        )
        isGradient = typedArray.getBoolean(R.styleable.MyCircleProgressView_isGradient, false)
        mGradientColor = typedArray.getResourceId(R.styleable.MyCircleProgressView_gradient, 0)
        if (mGradientColor != 0) {
            mGradientColors = resources.getIntArray(mGradientColor!!)
        }

        typedArray.recycle()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() { //圆画笔（主圆的画笔设置）
        mCirPaint = Paint()
        mCirPaint.isAntiAlias = antiAlias //是否开启抗锯齿
        mCirPaint.style = Paint.Style.STROKE //画笔样式
        mCirPaint.strokeWidth = mCirWidth //画笔宽度
        mCirPaint.strokeCap = Paint.Cap.ROUND  //笔刷样式（圆角的效果）
        mCirPaint.color = mCirColor!! //画笔颜色

        //背景圆画笔（一般和主圆一样大或者小于主圆的宽度）
        mBgCirPaint = Paint()
        mBgCirPaint.isAntiAlias = antiAlias
        mBgCirPaint.style = Paint.Style.STROKE
        mBgCirPaint.strokeWidth = mBgCirWidth
        mBgCirPaint.strokeCap = Paint.Cap.ROUND
        mBgCirPaint.color = mBgCirColor!!

        //初始化进度文字的字体画笔
        mValuePaint = TextPaint()
        mValuePaint.isAntiAlias = antiAlias  //是否抗锯齿
        mValuePaint.textSize = mValueSize!!  //字体大小
        mValuePaint.color = mValueColor!!  //字体颜色
        mValuePaint.textAlign = Paint.Align.CENTER //从中间向两边绘制，不需要再次计算文字

        //初始化提示文本的字体画笔
        mHintPaint = TextPaint()
        mHintPaint.isAntiAlias = antiAlias
        mHintPaint.textSize = mHintSize!!
        mHintPaint.color = mHintColor!!
        mHintPaint.textAlign = Paint.Align.CENTER
    }

    /**
     * 设置圆形和矩阵的大小,设置圆心位置
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh) //圆心位置
        centerPosition.x = w / 2
        centerPosition.y = h / 2

        //半径
        val maxCirWidth = max(mCirWidth, mBgCirWidth)
        val minWidth = min(
            w - paddingLeft - paddingRight - 2 * maxCirWidth,
            h - paddingBottom - paddingTop - 2 * maxCirWidth
        )

        radius = minWidth / 2

        //矩形坐标
        mRectF!!.left = centerPosition.x - radius!! - maxCirWidth / 2
        mRectF!!.top = centerPosition.y - radius!! - maxCirWidth / 2
        mRectF!!.right = centerPosition.x + radius!! + maxCirWidth / 2
        mRectF!!.bottom = centerPosition.y + radius!! + maxCirWidth / 2

        if (isGradient!!) {
            setupGradientCircle() //设置圆环画笔颜色渐变
        }
    }

    /**
     * 核心方法-绘制文本与圆环
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawText(canvas)
        drawCircle(canvas)
    }


    /**
     * 绘制中心的文本
     */
    private fun drawText(canvas: Canvas) {
        if (mHintShow) {
            canvas.drawText(
                mValue + mUnit, centerPosition.x.toFloat(), centerPosition.y.toFloat(), mValuePaint
            )

            canvas.drawText(
                mHint.toString(),
                centerPosition.x.toFloat(),
                centerPosition.y - mHintPaint.ascent() + 15,   //设置间距
                mHintPaint
            )
        } else { //进度文字居中
            canvas.drawText(
                mValue + mUnit,
                centerPosition.x.toFloat(),
                centerPosition.y - (mValuePaint.ascent() + mValuePaint.descent()) / 2,
                mValuePaint
            )
        }
    }

    /**
     * 使用渐变色画圆
     */
    private fun setupGradientCircle() {
        mSweepGradient = SweepGradient(
            centerPosition.x.toFloat(), centerPosition.y.toFloat(), mGradientColors!!, null
        )
        mMatrix = Matrix()
        mMatrix?.setRotate(
            -90f, centerPosition.x.toFloat(), centerPosition.y.toFloat()
        ) //渐变色初始位置从12点开始
        mSweepGradient?.setLocalMatrix(mMatrix) //设置渐变色初始位置(默认初始位置3点钟)
        mCirPaint.shader = mSweepGradient
    }

    /**
     * 画圆（主要的圆）
     */
    private fun drawCircle(canvas: Canvas?) {
        canvas?.save()
        if (mShadowIsShow) {
            mCirPaint.setShadowLayer(mShadowSize!!, 0f, 0f, mShadowColor!!) //设置阴影
        }

        //画背景圆
        canvas?.drawArc(mRectF!!, mStartAngle, mSweepAngle, false, mBgCirPaint)

        //画圆
        canvas?.drawArc(mRectF!!, mStartAngle, mSweepAngle * mAnimPercent, false, mCirPaint)
        canvas?.restore()
    }

    /**
     * 设置当前需要展示的进度值
     */
    fun setProgress(value: String): MyCircleProgressView {
        return setProgress(value, 100f)
    }

    private fun setProgress(value: String, maxValue: Float): MyCircleProgressView {
        if (isNum(value)) {
            mValue = value
            mMaxProgress = maxValue

            //当前的进度和最大的进度，去做动画的绘制
            val start = mAnimPercent
            val end = value.toFloat() / maxValue
            startAnim(start, end, mAnimTime)

        } else {
            mValue = value
        }
        return this
    }

    /**
     * 执行属性动画
     */
    private fun startAnim(start: Float, end: Float, animTime: Int) {
        mAnimator = ValueAnimator.ofFloat(start, end)
        mAnimator?.duration = animTime.toLong()
        mAnimator?.addUpdateListener { //得到当前的动画进度并赋值
            mAnimPercent = it.animatedValue as Float

            //根据当前的动画得到当前的值
            mValue = if (isAnim) {
                CircleUtil.roundByScale((mAnimPercent * mMaxProgress).toDouble(), mDigit)
            } else {
                CircleUtil.roundByScale(mValue!!.toDouble(), mDigit)
            }

            //不停的重绘当前值-表现出动画的效果
            postInvalidate()
        }
        mAnimator?.start()
    }

    /**
     * 设置动画时长
     * */
    fun setAnimTime(animTime: Int): MyCircleProgressView {
        this.mAnimTime = animTime
        invalidate()
        return this
    }


    /**
     * 是否渐变色
     * */
    fun setIsGradient(isGradient: Boolean): MyCircleProgressView {
        this.isGradient = isGradient
        invalidate()
        return this
    }

    /**
     * 设置渐变色
     * */
    fun setGradientColors(gradientColors: IntArray): MyCircleProgressView {
        mGradientColors = gradientColors
        setupGradientCircle()
        return this
    }

    /**
     * 是否显示阴影
     * */
    fun setShadowEnable(enable: Boolean): MyCircleProgressView {
        mShadowIsShow = enable
        invalidate()
        return this
    }

    /**
     * 设置小数位数
     * */
    fun setDigit(digit: Int): MyCircleProgressView {
        mDigit = digit
        invalidate()
        return this
    }

    //判断当前的值是否是数字类型
    private fun isNum(str: String): Boolean {
        try {
            val toDouble = str.toDouble()
        } catch (e: Exception) {
            return false
        }
        return true
    }


    /**
     * 内部工具类
     */
    private class CircleUtil {

        companion object {
            /**
             * 将double格式化为指定小数位的String，不足小数位用0补全
             *
             * @param v     需要格式化的数字
             * @param scale 小数点后保留几位
             * @return
             */
            fun roundByScale(v: Double, scale: Int): String {
                if (scale < 0) {
                    throw IllegalArgumentException("参数错误，必须设置大于0的数字")
                }
                if (scale == 0) {
                    return DecimalFormat("0").format(v)
                }
                var formatStr = "0."

                for (i in 0 until scale) {
                    formatStr += "0"
                }
                return DecimalFormat(formatStr).format(v);
            }

            fun dp2px(context: Context, dpValue: Float): Int {
                val scale = Resources.getSystem().displayMetrics.density
                return (dpValue * scale + 0.5f).toInt()
            }

            fun px2dp(context: Context, pxValue: Float): Int {
                val scale = context.resources.displayMetrics.density
                return (pxValue / scale + 0.5f).toInt()
            }

            fun px2sp(context: Context, pxValue: Float): Int {
                val fontScale = context.resources.displayMetrics.scaledDensity
                return (pxValue / fontScale + 0.5f).toInt()
            }

            fun sp2px(context: Context, spValue: Float): Int {
                val fontScale = context.resources.displayMetrics.scaledDensity
                return (spValue * fontScale + 0.5f).toInt()
            }
        }
    }
}