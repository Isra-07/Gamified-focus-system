package com.thrive.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thrive.core.ui.ThriveTheme
import com.thrive.domain.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    @Inject lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThriveTheme {
                SplashScreen(
                    onFinished = { isLoggedIn ->
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("start_destination", if (isLoggedIn) "home" else "sign_in")
                        }
                        startActivity(intent)
                        finish()
                    },
                    checkUser = {
                        withContext(Dispatchers.IO) {
                            userRepository.getUser() != null
                        }
                    }
                )
            }
        }
    }
}

// ── Particle ──────────────────────────────────────────────────────────────────
private data class Particle(
    var x: Float, var y: Float,
    var vx: Float, var vy: Float,
    var radius: Float, var alpha: Float,
    val color: Color, val decay: Float
)

// ── Splash Screen ─────────────────────────────────────────────────────────────
@Composable
fun SplashScreen(
    onFinished: (isLoggedIn: Boolean) -> Unit,
    checkUser: suspend () -> Boolean
) {
    var phase by remember { mutableIntStateOf(0) }
    // 0 = pop in, 1 = bounce, 2 = explode, 3 = done

    val mascotScale by animateFloatAsState(
        targetValue = when (phase) {
            0 -> 1f
            2 -> 1.7f
            else -> 1f
        },
        animationSpec = when (phase) {
            0 -> spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
            2 -> tween(450, easing = EaseIn)
            else -> snap()
        },
        label = "scale"
    )

    val mascotAlpha by animateFloatAsState(
        targetValue = if (phase >= 2) 0f else 1f,
        animationSpec = tween(400),
        label = "alpha"
    )

    val bounceAnim = rememberInfiniteTransition(label = "bounce")
    val bounceY by bounceAnim.animateFloat(
        initialValue = 0f, targetValue = -12f,
        animationSpec = infiniteRepeatable(tween(450), RepeatMode.Reverse),
        label = "bounceY"
    )

    val particles = remember { mutableStateListOf<Particle>() }
    var spawnDone by remember { mutableStateOf(false) }

    var shockwave by remember { mutableStateOf(0f) }
    val shockwaveAnim by animateFloatAsState(
        targetValue = shockwave,
        animationSpec = tween(700, easing = EaseOut),
        label = "shock"
    )

    val particleColors = listOf(
        Color(0xFF7C6FCD), Color(0xFFA78BFA), Color(0xFFC4B5FD),
        Color(0xFF9B8FE8), Color(0xFFF472B6), Color(0xFFFBBF24), Color.White
    )

    fun spawnParticles(cx: Float, cy: Float) {
        repeat(130) {
            val angle = Random.nextFloat() * 2f * PI.toFloat()
            val speed = 3f + Random.nextFloat() * 10f
            particles.add(
                Particle(
                    x = cx, y = cy,
                    vx = cos(angle) * speed,
                    vy = sin(angle) * speed,
                    radius = 2f + Random.nextFloat() * 5f,
                    alpha = 1f,
                    color = particleColors.random(),
                    decay = 0.011f + Random.nextFloat() * 0.016f
                )
            )
        }
    }

    // Main animation sequence
    LaunchedEffect(Unit) {
        // Phase 0: mascot pops in (spring animation)
        delay(700)
        // Phase 1: cute bounce
        phase = 1
        delay(1300)
        // Phase 2: explode
        phase = 2
        shockwave = 1f
        spawnDone = false
        delay(100)
        spawnDone = true
        delay(1000)
        // Check DB then navigate
        val isLoggedIn = checkUser()
        phase = 3
        onFinished(isLoggedIn)
    }

    Box(Modifier.fillMaxSize(), Alignment.Center) {

        // Particles + shockwave canvas
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f - 50f

            if (spawnDone && particles.isEmpty()) spawnParticles(cx, cy)

            // Shockwave rings
            if (shockwaveAnim > 0f) {
                listOf(
                    Triple(0f, Color(0xFFA78BFA), 0.65f),
                    Triple(0.15f, Color(0xFFC4B5FD), 0.5f),
                    Triple(0.28f, Color(0xFFF472B6), 0.45f)
                ).forEach { (offset, color, maxAlpha) ->
                    val rp = (shockwaveAnim - offset).coerceIn(0f, 1f)
                    if (rp > 0f) {
                        drawCircle(
                            color = color.copy(alpha = maxAlpha * (1f - rp)),
                            radius = rp * size.minDimension * 0.72f,
                            center = Offset(cx, cy),
                            style = Stroke((3f * (1f - rp)).coerceAtLeast(0.5f))
                        )
                    }
                }
            }

            // Particles
            val iter = particles.iterator()
            while (iter.hasNext()) {
                val p = iter.next()
                p.x += p.vx; p.y += p.vy
                p.vy += 0.12f; p.vx *= 0.985f
                p.alpha -= p.decay
                if (p.alpha <= 0f) {
                    iter.remove()
                    continue
                }
                drawCircle(
                    color = p.color.copy(alpha = p.alpha),
                    radius = p.radius,
                    center = Offset(p.x, p.y)
                )
            }
        }

        // Mascot + text
        val density = LocalDensity.current
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = mascotScale,
                    scaleY = mascotScale,
                    alpha = mascotAlpha,
                    translationY = if (phase == 1) with(density) { bounceY.dp.toPx() } else 0f
                )
        ) {
            BrainMascot(Modifier.size(148.dp))
            Spacer(Modifier.height(16.dp))
            Text(
                "Thrive",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("Gamified Focus System", fontSize = 13.sp, color = Color(0xFF6B6B82))
        }
    }
}

// ── Brain mascot drawn with Canvas API ────────────────────────────────────────
@Composable
fun BrainMascot(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val s = w / 150f

        fun f(v: Number) = v.toFloat() * s

        // Shadow
        drawOval(
            Color(0xFF7C6FCD).copy(0.15f),
            Offset(cx - f(32), h - f(10)),
            Size(f(64), f(12))
        )

        // Body
        drawRoundRect(
            Color(0xFF6C5FC7),
            Offset(cx - f(20), f(116)),
            Size(f(40), f(30)),
            CornerRadius(f(15))
        )

        // Legs
        drawRoundRect(
            Color(0xFF6C5FC7),
            Offset(cx - f(18), f(136)),
            Size(f(13), f(20)),
            CornerRadius(f(6))
        )
        drawRoundRect(
            Color(0xFF6C5FC7),
            Offset(cx + f(5), f(136)),
            Size(f(13), f(20)),
            CornerRadius(f(6))
        )

        // Feet
        drawOval(Color(0xFF5347A8), Offset(cx - f(25), f(154)), Size(f(20), f(11)))
        drawOval(Color(0xFF5347A8), Offset(cx + f(5), f(154)), Size(f(20), f(11)))

        // Arms
        drawLine(
            Color(0xFF6C5FC7),
            Offset(cx - f(18), f(128)),
            Offset(cx - f(38), f(114)),
            f(10)
        )
        drawLine(
            Color(0xFF6C5FC7),
            Offset(cx + f(18), f(128)),
            Offset(cx + f(38), f(114)),
            f(10)
        )

        // Hands
        drawCircle(Color(0xFF7C6FCD), f(8), Offset(cx - f(40), f(111)))
        drawCircle(Color(0xFF7C6FCD), f(8), Offset(cx + f(40), f(111)))

        // Neck
        drawRoundRect(
            Color(0xFF7C6FCD),
            Offset(cx - f(10), f(108)),
            Size(f(20), f(13)),
            CornerRadius(f(6))
        )

        // Brain head shape — two lobes
        val brain = Path().apply {
            moveTo(cx, f(18))
            cubicTo(cx - f(17), f(18), cx - f(33), f(26), cx - f(39), f(40))
            cubicTo(cx - f(47), f(56), cx - f(45), f(70), cx - f(41), f(80))
            cubicTo(cx - f(47), f(86), cx - f(49), f(96), cx - f(43), f(104))
            cubicTo(cx - f(37), f(112), cx - f(25), f(114), cx - f(15), f(112))
            cubicTo(cx - f(11), f(116), cx - f(5), f(118), cx, f(118))
            cubicTo(cx + f(5), f(118), cx + f(11), f(116), cx + f(15), f(112))
            cubicTo(cx + f(25), f(114), cx + f(37), f(112), cx + f(43), f(104))
            cubicTo(cx + f(49), f(96), cx + f(47), f(86), cx + f(41), f(80))
            cubicTo(cx + f(45), f(70), cx + f(47), f(56), cx + f(39), f(40))
            cubicTo(cx + f(33), f(26), cx + f(17), f(18), cx, f(18))
            close()
        }
        drawPath(brain, Color(0xFF8B7ED8))

        // Center groove
        drawLine(
            Color(0xFF6C5FC7).copy(0.6f),
            Offset(cx, f(20)),
            Offset(cx, f(118)),
            f(2.5f)
        )

        // Brain folds LEFT
        val fold = Color(0xFF6155B0)
        drawLine(fold.copy(0.7f), Offset(cx - f(39), f(42)), Offset(cx - f(45), f(58)), f(2.5f))
        drawLine(fold.copy(0.7f), Offset(cx - f(45), f(58)), Offset(cx - f(41), f(78)), f(2.5f))
        drawLine(fold.copy(0.5f), Offset(cx - f(35), f(30)), Offset(cx - f(41), f(46)), f(2f))
        drawLine(fold.copy(0.4f), Offset(cx - f(25), f(22)), Offset(cx - f(33), f(36)), f(2f))

        // Brain folds RIGHT
        drawLine(fold.copy(0.7f), Offset(cx + f(39), f(42)), Offset(cx + f(45), f(58)), f(2.5f))
        drawLine(fold.copy(0.7f), Offset(cx + f(45), f(58)), Offset(cx + f(41), f(78)), f(2.5f))
        drawLine(fold.copy(0.5f), Offset(cx + f(35), f(30)), Offset(cx + f(41), f(46)), f(2f))
        drawLine(fold.copy(0.4f), Offset(cx + f(25), f(22)), Offset(cx + f(33), f(36)), f(2f))

        // Wave lines LEFT
        val waveL = Path().apply {
            moveTo(cx - f(41), f(74))
            quadraticBezierTo(cx - f(33), f(66), cx - f(25), f(74))
            quadraticBezierTo(cx - f(17), f(82), cx - f(9), f(74))
        }
        drawPath(
            waveL,
            Color(0xFFC4B5FD).copy(0.55f),
            style = Stroke(f(2f), cap = StrokeCap.Round)
        )

        // Wave lines RIGHT
        val waveR = Path().apply {
            moveTo(cx + f(9), f(74))
            quadraticBezierTo(cx + f(17), f(66), cx + f(25), f(74))
            quadraticBezierTo(cx + f(33), f(82), cx + f(41), f(74))
        }
        drawPath(
            waveR,
            Color(0xFFC4B5FD).copy(0.55f),
            style = Stroke(f(2f), cap = StrokeCap.Round)
        )

        // Inner face glow
        drawOval(Color(0xFF9B8FE8).copy(0.3f), Offset(cx - f(28), f(54)), Size(f(56), f(48)))

        // Headphone cups
        drawCircle(Color(0xFF5347A8), f(10), Offset(cx - f(52), f(72)))
        drawCircle(Color(0xFF6C5FC7), f(5.5f), Offset(cx - f(52), f(72)))
        drawCircle(Color(0xFF5347A8), f(10), Offset(cx + f(52), f(72)))
        drawCircle(Color(0xFF6C5FC7), f(5.5f), Offset(cx + f(52), f(72)))

        // Headphone band
        val band = Path().apply {
            moveTo(cx - f(52), f(62))
            quadraticBezierTo(cx, f(22), cx + f(52), f(62))
        }
        drawPath(band, Color(0xFF5347A8), style = Stroke(f(4.5f), cap = StrokeCap.Round))

        // Eyes
        drawRoundRect(Color(0xFF1A1228), Offset(cx - f(29), f(64)), Size(f(22), f(25)), CornerRadius(f(11)))
        drawRoundRect(Color(0xFF1A1228), Offset(cx + f(7), f(64)), Size(f(22), f(25)), CornerRadius(f(11)))

        // Eye shines
        drawCircle(Color.White.copy(0.92f), f(4.5f), Offset(cx - f(22), f(70)))
        drawCircle(Color.White.copy(0.92f), f(4.5f), Offset(cx + f(14), f(70)))
        drawCircle(Color.White.copy(0.4f), f(2f), Offset(cx - f(12), f(81)))
        drawCircle(Color.White.copy(0.4f), f(2f), Offset(cx + f(24), f(81)))

        // Closed smile
        val smile = Path().apply {
            moveTo(cx - f(14), f(98))
            quadraticBezierTo(cx, f(108), cx + f(14), f(98))
        }
        drawPath(smile, Color(0xFF5347A8), style = Stroke(f(3f), cap = StrokeCap.Round))
        drawCircle(Color(0xFF5347A8).copy(0.55f), f(2f), Offset(cx - f(14), f(98)))
        drawCircle(Color(0xFF5347A8).copy(0.55f), f(2f), Offset(cx + f(14), f(98)))

        // Blush
        drawOval(Color(0xFFF472B6).copy(0.3f), Offset(cx - f(43), f(90)), Size(f(18), f(10)))
        drawOval(Color(0xFFF472B6).copy(0.3f), Offset(cx + f(25), f(90)), Size(f(18), f(10)))

        // Sparkles
        drawCircle(Color(0xFFC4B5FD).copy(0.65f), f(3f), Offset(cx - f(54), f(26)))
        drawCircle(Color(0xFFA78BFA).copy(0.6f), f(2.5f), Offset(cx + f(54), f(24)))
        drawCircle(Color(0xFF7C6FCD).copy(0.45f), f(2f), Offset(cx - f(60), f(50)))
        drawCircle(Color(0xFF7C6FCD).copy(0.45f), f(2f), Offset(cx + f(60), f(50)))
    }
}

