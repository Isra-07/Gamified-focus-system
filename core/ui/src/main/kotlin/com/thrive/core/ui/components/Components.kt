package com.thrive.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.thrive.core.ui.theme.Aqua
import com.thrive.core.ui.theme.Coral
import com.thrive.core.ui.theme.DividerDark
import com.thrive.core.ui.theme.Leaf
import com.thrive.core.ui.theme.Midnight
import com.thrive.core.ui.theme.Night
import com.thrive.core.ui.theme.PinkGlow
import com.thrive.core.ui.theme.SurfaceCard
import com.thrive.core.ui.theme.SurfaceDark
import com.thrive.core.ui.theme.SurfaceRaised
import com.thrive.core.ui.theme.TextPrimary
import com.thrive.core.ui.theme.TextSecondary
import com.thrive.core.ui.theme.Violet
import com.thrive.core.ui.theme.VioletBright

@Composable
fun ThriveScreen(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Midnight, Night, Night)
                )
            )
            .padding(horizontal = 16.dp)
    ) {
        content()
    }
}

@Composable
fun StarfieldBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val stars = listOf(
            Offset(size.width * 0.08f, size.height * 0.05f),
            Offset(size.width * 0.18f, size.height * 0.12f),
            Offset(size.width * 0.66f, size.height * 0.08f),
            Offset(size.width * 0.88f, size.height * 0.16f),
            Offset(size.width * 0.25f, size.height * 0.24f),
            Offset(size.width * 0.77f, size.height * 0.28f)
        )
        stars.forEach { point ->
            drawCircle(Color.White.copy(alpha = 0.35f), radius = 2.2f, center = point)
        }
    }
}

@Composable
fun ThriveCard(
    modifier: Modifier = Modifier,
    accent: Color = DividerDark,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard.copy(alpha = 0.95f)),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f))
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.02f), Color.Transparent)
                    )
                )
                .padding(18.dp)
        ) {
            content()
        }
    }
}

@Composable
fun ThriveButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    glowColor: Color = Violet,
    containerBrush: Brush = Brush.horizontalGradient(listOf(Violet, VioletBright)),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .shadow(18.dp, RoundedCornerShape(18.dp), ambientColor = glowColor.copy(alpha = 0.35f), spotColor = glowColor.copy(alpha = 0.45f)),
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = TextPrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(containerBrush, RoundedCornerShape(18.dp))
                .padding(vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                }
                Text(text, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun PrimaryGreenButton(text: String, onClick: () -> Unit) {
    ThriveButton(
        text = text,
        glowColor = Leaf,
        containerBrush = Brush.horizontalGradient(listOf(Color(0xFF4ED979), Color(0xFF53D786))),
        onClick = onClick
    )
}

@Composable
fun ThriveTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label.uppercase(), color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            placeholder = { Text(label, color = TextSecondary.copy(alpha = 0.7f)) },
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DividerDark,
                unfocusedBorderColor = DividerDark,
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = VioletBright,
                focusedLabelColor = VioletBright,
                unfocusedLabelColor = TextSecondary
            )
        )
    }
}

@Composable
fun BackIconButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick, indication = null, interactionSource = MutableInteractionSource())
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
    }
}

@Composable
fun MetricTile(label: String, value: String, subtitle: String = "", color: Color = Aqua) {
    ThriveCard(accent = color) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(label, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                Text(value, color = color, style = MaterialTheme.typography.headlineSmall)
                if (subtitle.isNotBlank()) {
                    Text(subtitle, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
            }
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color.copy(alpha = 0.14f))
                    .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(value.take(2), color = color, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun ProgressCard(title: String, subtitle: String, progress: Float, accent: Color = Aqua) {
    ThriveCard(accent = accent) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, color = TextPrimary, style = MaterialTheme.typography.titleLarge)
            Text(subtitle, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = accent,
                trackColor = SurfaceRaised
            )
        }
    }
}

@Composable
fun TimerRing(progress: Float, text: String) {
    Box(modifier = Modifier.size(250.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 16.dp.toPx()
            val diameter = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            drawArc(
                color = Color(0xFF323446),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = diameter,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(VioletBright, PinkGlow, Aqua, VioletBright)),
                startAngle = -90f,
                sweepAngle = (progress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                topLeft = topLeft,
                size = diameter,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text, color = TextPrimary, style = MaterialTheme.typography.displaySmall)
            Text("25:00 session", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun Chip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) Brush.horizontalGradient(listOf(Violet, VioletBright)) else Brush.horizontalGradient(listOf(SurfaceRaised, SurfaceRaised)), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = if (selected) TextPrimary else TextSecondary, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomPillBar(items: List<BottomBarItem>, currentRoute: String, onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(SurfaceDark.copy(alpha = 0.96f))
            .border(1.dp, DividerDark, RoundedCornerShape(32.dp))
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            Column(
                modifier = Modifier.clickable { onNavigate(item.route) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(item.icon, contentDescription = item.label, tint = if (selected) item.selectedColor else TextSecondary)
                Text(item.label, color = if (selected) item.selectedColor else TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

data class BottomBarItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedColor: Color
)

@Composable
fun WarningBanner(visible: Boolean, message: String) {
    AnimatedVisibility(visible = visible) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Coral.copy(alpha = 0.14f),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(12.dp),
                color = TextPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
