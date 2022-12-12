//
//  ContentView.swift
//  Kaboom
//
//  Created by Dmitrii Toksaitov on 10/10/22.
//

import SwiftUI

enum BombState {
    case beingDisarmed, disarmed, exploded
}

struct BombData {
    static let textData = [
        "code words": ["all", "kinds", "of", "code", "words"],
        "passwords": ["password", "12345", "friend", "mellon"]
    ]
}

class Bomb: ObservableObject {
    @Published var bt1Presses = Int.random(in: 0...5)
    @Published var bt1Pressed = 0

    @Published var bt2Presses = Int.random(in: 0...5)
    @Published var bt2Pressed = 0

    @Published var bt3Presses = Int.random(in: 0...5)
    @Published var bt3Pressed = 0

    @Published var codeWordsToEnter =
        BombData.textData["code words"]!.shuffled().prefix(3).joined(separator: " ")
    @Published var codeWordsEntered = ""

    @Published var shouldCutRedWire = Bool.random()
    @Published var redWireConnected = true

    @Published var shouldCutGreenWire = Bool.random()
    @Published var greenWireConnected = true

    @Published var shouldCutBlueWire = Bool.random()
    @Published var blueWireConnected = true

    @Published var passwordToEnter =
        BombData.textData["passwords"]!.randomElement()!
    @Published var passwordEntered = ""

    @Published var state = BombState.beingDisarmed

    func defuse() {
        if bt1Pressed == bt1Presses &&
           bt2Pressed == bt2Presses &&
           bt3Pressed == bt3Presses &&
           codeWordsEntered == codeWordsToEnter      &&
           shouldCutRedWire   == !redWireConnected   &&
           shouldCutGreenWire == !greenWireConnected &&
           shouldCutBlueWire  == !blueWireConnected  &&
           passwordEntered == passwordToEnter {
            state = .disarmed
        } else {
            state = .exploded
        }
    }

    func restore() {
        bt1Presses = Int.random(in: 0...5)
        bt1Pressed = 0

        bt2Presses = Int.random(in: 0...5)
        bt2Pressed = 0

        bt3Presses = Int.random(in: 0...5)
        bt3Pressed = 0

        codeWordsToEnter = BombData.textData["code words"]!.shuffled().prefix(3).joined(separator: " ")
        codeWordsEntered = ""

        shouldCutRedWire = Bool.random()
        redWireConnected = true

        shouldCutGreenWire = Bool.random()
        greenWireConnected = true

        shouldCutBlueWire = Bool.random()
        blueWireConnected = true

        passwordToEnter = BombData.textData["passwords"]!.randomElement()!
        passwordEntered = ""

        state = .beingDisarmed
    }
}

struct ContentView: View {
    @StateObject var bomb = Bomb()

    func generateInstructionText() -> String {
        var instructions: [String] = []

        if bomb.bt1Presses > 0 {
            instructions.append("Press 'Button 1' \(bomb.bt1Presses) \(bomb.bt1Presses == 1 ? "time" : "times").")
        }
        if bomb.bt2Presses > 0 {
            instructions.append("Press 'Button 2' \(bomb.bt2Presses) \(bomb.bt2Presses == 1 ? "time" : "times").")
        }
        if bomb.bt3Presses > 0 {
            instructions.append("Press 'Button 3' \(bomb.bt3Presses) \(bomb.bt3Presses == 1 ? "time" : "times").")
        }

        instructions.append("Enter the code words: '\(bomb.codeWordsToEnter)'.")

        if bomb.shouldCutRedWire {
            instructions.append("Cut the red wire.")
        }
        if bomb.shouldCutGreenWire {
            instructions.append("Cut the green wire.")
        }
        if bomb.shouldCutBlueWire {
            instructions.append("Cut the blue wire.")
        }

        instructions.append("Enter the password: '\(bomb.passwordToEnter)'.")

        return instructions.joined(separator: " ")
    }

    var body: some View {
        VStack {
            Text("Kaboom!")
                .font(.title)
                .bold()
            Form {
                switch bomb.state {
                case .exploded:
                    Image("Explosion")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                case .disarmed:
                    Image(systemName: "sun.min")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .foregroundColor(.green)
                        .padding(70)
                case .beingDisarmed:
                    Text("Disarm the bomb by following the instructions:")
                        .padding()
                    Text(generateInstructionText())
                    HStack(spacing: 50) {
                        Spacer()
                        Button("1") {
                            bomb.bt1Pressed += 1
                        }
                        Button("2") {
                            bomb.bt2Pressed += 1
                        }
                        Button("3") {
                            bomb.bt3Pressed += 1
                        }
                        Spacer()
                    }.buttonStyle(.borderedProminent)
                    TextField("Code Words", text: $bomb.codeWordsEntered)
                        .textInputAutocapitalization(.never)
                    Toggle(isOn: $bomb.redWireConnected) {
                        Text("Red wire").foregroundColor(.red)
                    }
                    Toggle(isOn: $bomb.greenWireConnected) {
                        Text("Green wire").foregroundColor(.green)
                    }
                    Toggle(isOn: $bomb.blueWireConnected) {
                        Text("Blue wire").foregroundColor(.blue)
                    }
                    SecureField("Password", text: $bomb.passwordEntered)
                }

                HStack() {
                    Spacer()
                    if bomb.state == .beingDisarmed {
                        Button("Defuse", role: .destructive) {
                            bomb.defuse()
                        }
                    }
                    Button("Restart") {
                        bomb.restore()
                    }
                    Spacer()
                }.buttonStyle(.borderedProminent)
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
