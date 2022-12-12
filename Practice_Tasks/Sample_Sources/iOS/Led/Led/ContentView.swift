//
//  ContentView.swift
//  Led
//
//  Created by Dmitrii Toksaitov on 10/12/22.
//

import SwiftUI
import Alamofire

struct ContentView: View {
    var body: some View {
        VStack {
            Button("Switch the LED") {
                AF.request("http://10.10.1.205").response { response in
                    debugPrint(response)
                }
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
