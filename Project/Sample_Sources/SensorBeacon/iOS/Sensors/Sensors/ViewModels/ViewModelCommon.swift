//
//  ViewModelCommon.swift
//  Sensors
//
//  Created by Dmitrii Toksaitov on 12/12/22.
//

import Foundation

let serviceBaseURL = "http://192.168.0.?:3000"
let token = "?"

enum ServiceError: Error {
    case error(String)
}

struct ServiceErrorReplyBody: Decodable {
    var message: String
}

struct ServiceEmptyBody: Encodable { }
