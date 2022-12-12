//
//  ContentView.swift
//  Greeter
//
//  Created by Dmitrii Toksaitov on 19/9/22.
//

import SwiftUI

struct ContentView: View {
    @State var name = ""

    var body: some View {
        VStack {
            TextField("You name", text: $name)
            if name.count > 0 {
                Text("Hi, \(name)!")
            } else {
                Text("Hi, please enter your name above...")
            }
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
