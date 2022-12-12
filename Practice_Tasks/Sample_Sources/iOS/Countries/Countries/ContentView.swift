//
//  ContentView.swift
//  Countries
//
//  Created by Dmitrii Toksaitov on 28/4/22.
//

import SwiftUI

struct CountryView: View {
    var flag: String
    
    var body: some View {
        Text(flag)
    }
}

struct ContentView: View {
    @StateObject var fetcher = CountryFetcher()
    
    var body: some View {
        NavigationView {
            ScrollView {
                LazyVStack(alignment: .leading) {
                    if (fetcher.countries.count > 0) {
                        ForEach(fetcher.countries, id: \.self.name.common) { country in
                            NavigationLink(destination: CountryView(flag: country.flag)) {
                                Text("\(country.flag) \(country.name.common)")
                                    .font(.system(size: 18))
                                    .bold()
                                    .foregroundColor(.gray)
                            }
                            .padding(.horizontal, 20)
                            .padding(.vertical, 5)
                        }
                    } else {
                        Text("Loading...")
                    }
                }
                .navigationTitle("Countries")
                .task {
                    try? await fetcher.fetchData()
                }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
