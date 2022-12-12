package com.toksaitov_d.kaboom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toksaitov_d.kaboom.ui.theme.KaboomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaboomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BombControls()
                }
            }
        }
    }
}

enum class BombState {
    BEING_DISARMED, DISARMED, EXPLODED
}

@Composable
fun BombControls() {
    var bt1Presses by rememberSaveable { mutableStateOf((0..5).random()) }
    var bt1Pressed by rememberSaveable { mutableStateOf(0) }

    var bt2Presses by rememberSaveable { mutableStateOf((0..5).random()) }
    var bt2Pressed by rememberSaveable { mutableStateOf(0) }

    var bt3Presses by rememberSaveable { mutableStateOf((0..5).random()) }
    var bt3Pressed by rememberSaveable { mutableStateOf(0) }

    // TODO
    var codeWordsEntered by rememberSaveable { mutableStateOf("") }

    val shouldCutRedWire = rememberSaveable { mutableStateOf((0..1).random() == 0) }
    val redWireConnected = rememberSaveable { mutableStateOf(true) }

    val shouldCutGreenWire = rememberSaveable { mutableStateOf((0..1).random() == 0) }
    val greenWireConnected = rememberSaveable { mutableStateOf(true) }

    val shouldCutBlueWire = rememberSaveable { mutableStateOf((0..1).random() == 0) }
    val blueWireConnected = rememberSaveable { mutableStateOf(true) }

    // TODO
    var password by rememberSaveable { mutableStateOf("") }

    var state by rememberSaveable { mutableStateOf(BombState.BEING_DISARMED) }

    fun defuse() {
        state = if (bt1Pressed == bt1Presses &&
                    bt2Pressed == bt2Presses &&
                    bt3Pressed == bt3Presses &&
                    shouldCutRedWire.value   == !redWireConnected.value   &&
                    shouldCutGreenWire.value == !greenWireConnected.value &&
                    shouldCutBlueWire.value  == !blueWireConnected.value) { // TODO
            BombState.DISARMED
        } else {
            BombState.EXPLODED
        }
    }

    fun restore() {
        bt1Presses = (0..5).random()
        bt1Pressed = 0

        bt2Presses = (0..5).random()
        bt2Pressed = 0

        bt3Presses = (0..5).random()
        bt3Pressed = 0

        // TODO
        codeWordsEntered = ""

        shouldCutRedWire.value = (0..1).random() == 0
        redWireConnected.value = true

        shouldCutGreenWire.value = (0..1).random() == 0
        greenWireConnected.value = true

        shouldCutBlueWire.value = (0..1).random() == 0
        blueWireConnected.value = true

        // TODO
        password = ""

        state = BombState.BEING_DISARMED
    }

    fun generateInstructionText(): String {
        var instructions = mutableListOf<String>()

        if (bt1Presses > 0) {
            instructions.add("Press 'Button 1' $bt1Presses ${if (bt1Presses == 1) "time" else "times"}.")
        }
        if (bt2Presses > 0) {
            instructions.add("Press 'Button 2' $bt2Presses ${if (bt2Presses == 1) "time" else "times"}.")
        }
        if (bt3Presses > 0) {
            instructions.add("Press 'Button 3' $bt3Presses ${if (bt3Presses == 1) "time" else "times"}.")
        }

        // TODO

        if (shouldCutRedWire.value) {
            instructions.add("Cut the red wire.")
        }
        if (shouldCutGreenWire.value) {
            instructions.add("Cut the green wire.")
        }
        if (shouldCutBlueWire.value) {
            instructions.add("Cut the blue wire.")
        }

        // TODO

        return instructions.joinToString(separator = " ")
    }

    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = "Kaboom!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        when (state) {
            BombState.EXPLODED -> {
                Image(
                    painter = painterResource(id = R.drawable.explosion),
                    contentDescription = "The bomb has exploded",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            BombState.DISARMED -> {
                Image(
                    painter = painterResource(id = R.drawable.ic_sun),
                    contentDescription = "The bomb has been disarmed",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            BombState.BEING_DISARMED -> {
                Text(
                    text = "Disarm the bomb by following the instructions:",
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = generateInstructionText(),
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Button(onClick = { bt1Pressed++ }) {
                        Text("1")
                    }
                    Button(onClick = { bt2Pressed++ }) {
                        Text("2")
                    }
                    Button(onClick = { bt3Pressed++ }) {
                        Text("3")
                    }
                }
                TextField(
                    value = codeWordsEntered,
                    onValueChange = { codeWordsEntered = it },
                    singleLine = true,
                    label = { Text("Code Words") },
                    modifier = Modifier.fillMaxWidth()
                )

                val switchTopPadding = 8.dp
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Red wire", color = Color.Red)
                    Switch(
                        checked = redWireConnected.value,
                        onCheckedChange = { redWireConnected.value = it },
                        modifier = Modifier.padding(top = switchTopPadding)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Green wire", color = Color.Green)
                    Switch(
                        checked = greenWireConnected.value,
                        onCheckedChange = { greenWireConnected.value = it },
                        modifier = Modifier.padding(top = switchTopPadding)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Blue wire", color = Color.Blue)
                    Switch(
                        checked = blueWireConnected.value,
                        onCheckedChange = { blueWireConnected.value = it },
                        modifier = Modifier.padding(top = switchTopPadding)
                    )
                }

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            if (state == BombState.BEING_DISARMED) {
                Button(onClick = { defuse() }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)) {
                    Text("Defuse")
                }
            }
            Button(onClick = { restore() }) {
                Text("Restart")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KaboomTheme {
        BombControls()
    }
}