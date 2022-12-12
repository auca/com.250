//
//  Country.swift
//  Countries
//
//  Created by Dmitrii Toksaitov on 28/4/22.
//

import Foundation

struct CountryName: Codable {
    var common: String
    var official: String
}

struct Country: Codable {
    var name: CountryName
    var flag: String
}
